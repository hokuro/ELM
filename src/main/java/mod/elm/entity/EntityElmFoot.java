package mod.elm.entity;

import java.util.Iterator;

import mod.elm.core.Mod_Elm;
import mod.elm.core.log.ModLog;
import mod.elm.entity.ab.IMobInfomationGetter;
import mod.elm.item.ItemCore;
import mod.elm.render.model.ModelLRKind;
import mod.elm.sound.SoundCore;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityElmFoot extends Entity implements IEntityAdditionalSpawnData , IMobInfomationGetter{
	public static final String NAME = "elmfoot";

	protected ModelLRKind lrKind;
	protected BlockPos endPos;
	protected EntitySize footSize;
	protected float damage;
	protected int waiteTime;
	protected int showTime;
	protected int tickTime;

	public EntityElmFoot(FMLPlayMessages.SpawnEntity packet, World world) {
		this(Mod_Elm.RegistryEvents.FOOT, world);
	}

	public EntityElmFoot(EntityType<?> etype, World world) {
		super(etype, world);
	}

	public EntityElmFoot(World world, LivingEntity user, EntitySize size, ModelLRKind kind, float damageIn) {
		super(Mod_Elm.RegistryEvents.FOOT, world);
		footSize = size;
		this.lrKind = kind;
		this.damage = damageIn;

		// i進行方向を設定
		this.setVector(user, user.rotationPitch, user.rotationYaw, 0.0F, 2.0F);

		// i必要数分よこにずらしてざ行軸を回転
		double wid = (kind == ModelLRKind.RIGHT?-size.width/2:size.width/2);
		double off_x = (wid * Math.cos(this.rotationYaw/180*Math.PI));
		double off_z = (wid * Math.sin(this.rotationYaw/180*Math.PI));

		// i出現位置を設定
		this.setPosition(user.posX + off_x,
				(user.posY + 2) + (user.getHeight() * 3),
				user.posZ + off_z);
		endPos = user.getPosition().add(0,-1,0);
		// iサイズを再計算
		this.recalculateSize();

		waiteTime = 0;
		showTime = 0;
		tickTime = 0;
	}

	@Override
	protected void registerData() {
	}

	@Override
	public void recalculateSize() {
		super.recalculateSize();
		float wid = footSize.width/2;
		float hei = footSize.height;
		double yy = this.rotationYaw * Math.PI/180;
		double off_x = wid * Math.cos(yy) - Math.sin(yy);
		double off_z = wid * Math.sin(yy) + Math.cos(yy);
		AxisAlignedBB bb = new AxisAlignedBB(this.posX-off_x, this.posY, this.posZ - off_z, this.posX + off_x, this.posY + hei, this.posZ + off_z);
		this.setBoundingBox(bb);
	}

	@Override
	public void tick() {
		if (!this.world.isRemote) {
			if (this.showTime < this.tickTime) {
				if (this.posY > this.endPos.getY()) {
					if (this.showTime + this.waiteTime < this.tickTime) {
						// iぶつかり判定ブロック
						this.hitBlock();
						// iぶつかり判定
						this.hitEntity();
						this.move();
						this.setMotion(new Vec3d(0,-1,0).scale(2.0));
					}
				} else {
					this.setMotion(0,0,0);
					this.remove();
				}
			} else if(this.showTime == this.tickTime) {
	    		this.world.playSound(null, this.getPosition(), SoundCore.SOUND_ITEM_HANDPRESS, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
		this.tickTime++;
		super.tick();
	}

	private void hitBlock() {
		// i見つけたブロックはすべて破壊
		AxisAlignedBB axisalignedbb = this.getBoundingBox();
		int sx = (int) Math.round(axisalignedbb.minX);
		int sy = (int) Math.round(axisalignedbb.minY);
		int sz = (int) Math.round(axisalignedbb.minZ);
		int ex = (int) Math.round(Math.abs(axisalignedbb.maxX - axisalignedbb.minX));
		int ey = (int) Math.round(Math.abs(axisalignedbb.maxY - axisalignedbb.minY));
		int ez = (int) Math.round(Math.abs(axisalignedbb.maxZ - axisalignedbb.minZ));

		ModLog.log().debug(sx + "," + sy + "," + sz);
		for (int xl = 0; xl < ex; xl++) {
			for (int zl = 0; zl < ez; zl++) {
				for (int yl = 0; yl < ey; yl++) {
					BlockPos blp = new BlockPos(sx+xl,sy+yl,sz+zl);
					BlockState state = this.world.getBlockState(blp);
					if (!state.isAir() && state.isSolid() && state.getBlock() != Blocks.BEDROCK) {
						if (state.hasTileEntity()) {
							this.world.removeTileEntity(blp);
						}
						this.world.removeBlock(blp, false);
					}
				}
			}
		}
	}

	protected void hitEntity() {
		AxisAlignedBB axisalignedbb = this.getBoundingBox();
        for(Entity entity : world.getEntitiesInAABBexcluding(this, axisalignedbb, (p_215312_0_) -> {
            return (!p_215312_0_.isSpectator() && !(p_215312_0_ instanceof EntityElmFoot));
         })) {
        	boolean helmFlg = false;
        	if (entity instanceof LivingEntity) {
        		Iterator<ItemStack> it = ((LivingEntity)entity).getEquipmentAndArmor().iterator();
        		while(it.hasNext()) {
        			ItemStack ns = it.next();
        			if (ns.getItem() instanceof ArmorItem) {
        				if (((ArmorItem)ns.getItem()).getEquipmentSlot() == EquipmentSlotType.HEAD) {
        					helmFlg = true;
        					break;
        				}
        			}
        		}
        	}
        	if (!helmFlg) {
        		entity.attackEntityFrom(DamageSource.causeThornsDamage(this), damage);
        		entity.setMotion(0, -damage, 0);
        	}
			this.world.playSound(null, this.posX, this.posY, this.posZ, SoundCore.SOUND_ITEM_FOOTPRESS, SoundCategory.AMBIENT, 1.0F, 1.0F);
        }
	}

	private void move() {
		// i座標移動
		Vec3d vec3d = this.getMotion();
		this.posY += vec3d.y;
		if (this.isInWater()) {
			for(int i = 0; i < 4; ++i) {
				float f2 = 0.25F;
				this.world.addParticle(ParticleTypes.BUBBLE, this.posX - vec3d.x * 0.25D, this.posY - vec3d.y * 0.25D, this.posZ - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
			}
		}
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	@Override
	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
		if (hand == Hand.MAIN_HAND && player.getHeldItem(Hand.MAIN_HAND).getItem() == ItemCore.item_mobanalyzer) {
			String infromation = this.getInformation();
			if (this.world.isRemote) {
				ModLog.log().info("####### Client Mob Information #######");
				ModLog.log().info(infromation);
			} else {
				ModLog.log().info("####### Client Mob Information #######");
				ModLog.log().info(infromation);
			}
		}
		return false;
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return this.footSize;
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.lrKind = ModelLRKind.values()[compound.getInt("lrkind")];
		this.endPos = new BlockPos(
				compound.getInt("startx"),
				compound.getInt("starty"),
				compound.getInt("startz"));

		footSize = new EntitySize(
				compound.getFloat("realwidth"),
				compound.getFloat("realheight"),
				false);
		damage = compound.getFloat("damage");


		this.waiteTime = compound.getInt("waiteTime");
		this.showTime = compound.getInt("showTime");
		this.tickTime = compound.getInt("tickTime");
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("lrkind", this.lrKind.ordinal());
		compound.putInt("startx", this.endPos.getX());
		compound.putInt("starty", this.endPos.getY());
		compound.putInt("startz", this.endPos.getZ());
		compound.putFloat("realwidth", this.footSize.width);
		compound.putFloat("realheight", this.footSize.height);
		compound.putFloat("damage", this.damage);
		compound.putInt("waiteTime", this.waiteTime);
		compound.putInt("showTime", this.showTime);
		compound.putInt("tickTime", this.tickTime);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.lrKind.ordinal());
		buffer.writeFloat(this.footSize.height);
		buffer.writeFloat(this.footSize.width);
		buffer.writeBlockPos(this.endPos);
		buffer.writeInt(this.showTime);
		buffer.writeInt(this.tickTime);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.lrKind = ModelLRKind.values()[additionalData.readInt()];
		float h = additionalData.readFloat();
		float w = additionalData.readFloat();
		footSize = new EntitySize(w,h,false);
		this.endPos = additionalData.readBlockPos();
		this.showTime = additionalData.readInt();
		this.tickTime = additionalData.readInt();
	}

	public void setVector(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity) {
		float x = 0;
		float y = -1.0F;
		float z = 0;

		Vec3d vec3d = (new Vec3d(x, y, z)).normalize().scale(velocity);
		this.setMotion(vec3d);
		this.prevRotationYaw =  this.rotationYaw = rotationYawIn;
		this.prevRotationPitch = this.rotationPitch = 0.0F;
	}

	public ModelLRKind getLRKind() {
		return this.lrKind;
	}

	public void setWaiteTime(int time) {
		this.waiteTime = time;
	}

	public void setShowTime(int time) {
		this.showTime = time;
	}

	public int getShowTime() {
		return this.showTime;
	}

	public int getTickTime() {
		return this.tickTime;
	}

	@Override
	public String getInformation() {
		String br = System.getProperty("line.separator");
		String ret = "LRKind         :" + this.lrKind.toString() + br +
		             "Yaw            :" + this.rotationYaw + br +
		             "Pitch          :" + this.rotationPitch + br +
		             "Motion         :" + this.getMotion().toString() + br +
		             "Size           :" + this.getSize(Pose.STANDING).toString() + br +
		             "BlockPos       :" + this.getPosition().toString() + br +
		             "StartPos       :" + this.endPos.toString() + br +
		             "Distance       :" + Math.sqrt(this.getDistanceSq(endPos.getX(), endPos.getY(), endPos.getZ())) + "/" + this.damage + br;
		return ret;
	}
}
