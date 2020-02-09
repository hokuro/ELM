package mod.elm.tileentity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import mod.elm.core.Mod_Elm;
import mod.elm.util.ModUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class TileEntityElmHead extends TileEntity implements  ITickableTileEntity, ITileEntityParameter{
	public static final String NAME = "tileentityelmhead";
	private String targetName;
	private Direction horizontalfacing;
	private Direction verticalfacing;
	private MobEntity target;
	private int nextFacingTime;
	private int nextHurtTime;
	private int nextGlowingTime;
	private NonNullList<ItemStack> inventory;

	public TileEntityElmHead() {
		this("");
	}

	public TileEntityElmHead(String target) {
		super(Mod_Elm.RegistryEvents.ELMHEAD);
		targetName = target;
		horizontalfacing = Direction.NORTH;
		verticalfacing = Direction.NORTH;
		target = null;
		nextFacingTime = 0;
		nextHurtTime = 0;
		nextGlowingTime = 0;
	}

	@Override
	public void tick() {
		// iターゲット未設定ならターゲットを設定
		if (target == null) {
			Optional<EntityType<?>> opt = Registry.ENTITY_TYPE.getValue(new ResourceLocation(targetName));
			if (opt.isPresent()) {
				target = (MobEntity)opt.get().create(world);
			}else {
				target = EntityType.ZOMBIE.create(world);
			}
		}

		// iランダム時間で近くのプレイヤーの方向を向く
		if (nextFacingTime == 0) {
			calicurateDirection();
		}

		// iランダム時間でなく
		if (world.isRemote && nextHurtTime == 0 && !(nextHurtTime < 0)) {
			hurt();
		}

		if (!world.isRemote && this.nextGlowingTime <= 0) {
			this.nextGlowingTime = 20 * 60 *10;
			List<MobEntity> ent = ((ServerWorld)world).getEntitiesWithinAABB(MobEntity.class, this.getRenderBoundingBox().expand(64,64,64).expand(-64,-64,-64));
			ent.forEach((e)->{
				e.addPotionEffect(new EffectInstance(Effects.GLOWING, nextGlowingTime, 0));
			});
		}


		// iランダム時間カウントダウン
		if (nextFacingTime > 0) {
			nextFacingTime--;
		}
		if (nextHurtTime > 0) {
			nextHurtTime--;
		}
		if (nextGlowingTime > 0) {
			nextGlowingTime--;
		}
	}

	private void calicurateDirection() {
		List<PlayerEntity> pls = this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getRenderBoundingBox().expand(10,10,10).expand(-10,-10,-10));
		if (pls.size() != 0) {
			PlayerEntity pl = pls.get(0);
			int x = (int)(pl.posX - this.pos.getX());
			int y = (int)(Math.abs(this.pos.getY()) - Math.abs(pl.posY));
			int z = (int)(pl.posZ - this.pos.getZ());
			double angle = MathHelper.wrapDegrees(Math.atan2(x, z) * (180/Math.PI));
			if ((angle >= 0 && angle <= 45) || (angle > -45 && angle < 0)) {
				// east
				this.horizontalfacing = Direction.SOUTH;
			} else if (angle > 45 && angle <= 135) {
				// south
				this.horizontalfacing = Direction.EAST;
			} else if ((angle <= 180 && angle > 135) || (angle <= -135 && angle >= -180) ) {
				// west
				this.horizontalfacing = Direction.NORTH;
			} else {
				// north
				this.horizontalfacing = Direction.WEST;
			}
			this.verticalfacing = Direction.WEST;
			if (Math.abs(y) <= 5 && (x != 0 || z != 0)) {
				// i上下差異5以下なら平面を向く
				this.verticalfacing = Direction.WEST;
			}else if (y < 0) {
				// iプレイヤーのほうが上にいる
				this.verticalfacing = Direction.UP;
			} else {
				// iプレイヤーのほうが下にいる
				this.verticalfacing = Direction.DOWN;
			}
		}
		nextFacingTime = 20 * ModUtil.random_1(10) * ModUtil.random_1(60);
	}

	private void hurt() {
		try {
			Method getAmbientSound = ObfuscationReflectionHelper.findMethod(MobEntity.class, "getAmbientSound");
			SoundEvent sound;
			sound = (SoundEvent)getAmbientSound.invoke(target, new Object[] {});
    		if (sound != null) {
    			this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
    		}
    		nextHurtTime = 20 * ModUtil.random_1(10) * ModUtil.random_1(30);
		} catch (Throwable e) {
			e.printStackTrace();
			nextHurtTime = 0;
		}
	}


	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
		targetName = compound.getString("targetname");
		horizontalfacing = Direction.byIndex(compound.getInt("horizontal"));
		verticalfacing = Direction.byIndex(compound.getInt("vertical"));
		nextFacingTime = compound.getInt("facingTime");
		nextHurtTime = compound.getInt("hurtTIme");
		nextGlowingTime = compound.getInt("glowingTime");
	}

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);

		compound.putString("targetname",targetName);
		compound.putInt("horizontal",horizontalfacing.getIndex());
		compound.putInt("vertical",verticalfacing.getIndex());
		compound.putInt("facingTime",nextFacingTime);
		compound.putInt("hurtTIme",nextHurtTime);
		compound.putInt("glowingTime",nextGlowingTime);

        return compound;
    }

	@Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT cp = super.getUpdateTag();
        return this.write(cp);
    }

	@Override
    public void handleUpdateTag(CompoundNBT tag) {
		super.handleUpdateTag(tag);
		this.read(tag);
    }

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()  {
        CompoundNBT CompoundNBT = new CompoundNBT();
        return new SUpdateTileEntityPacket(this.pos, 1,  this.write(CompoundNBT));
    }

	public static final int FIELD_HORIZONTALFACINT = 0;
	public static final int FIELD_VERTICALFACINT = 1;
	public static final int FIELD_FACINGTIME = 2;
	public static final int FIELD_HURTTIME = 3;

	public int getField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_HORIZONTALFACINT:
			ret = this.horizontalfacing.getIndex();
			break;
		case FIELD_VERTICALFACINT:
			ret = this.verticalfacing.getIndex();
			break;
		case FIELD_FACINGTIME:
			ret = this.nextFacingTime;
			break;
		case FIELD_HURTTIME:
			ret = this.nextHurtTime;
			break;
		default:
			ret = 0;
			break;
		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id){
		case FIELD_HORIZONTALFACINT:
			this.horizontalfacing = Direction.byIndex(value);
			break;
		case FIELD_VERTICALFACINT:
			this.verticalfacing = Direction.byIndex(value);
			break;
		case FIELD_FACINGTIME:
			this.nextFacingTime = value;
			break;
		case FIELD_HURTTIME:
			this.nextHurtTime = value;
			break;
		default:
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	public Direction getVertical() {
		return this.verticalfacing;
	}

	public Direction getHorizontal() {
		return this.horizontalfacing;
	}

	public String getTargetName() {
		return this.targetName;
	}
}
