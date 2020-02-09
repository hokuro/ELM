package mod.elm.entity;

import mod.elm.core.Mod_Elm;
import mod.elm.core.log.ModLog;
import mod.elm.entity.ab.IMobInfomationGetter;
import mod.elm.item.ItemCore;
import mod.elm.render.model.ModelLRKind;
import mod.elm.sound.SoundCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityElmHand extends Entity implements IEntityAdditionalSpawnData , IMobInfomationGetter{
	public static final String NAME = "elmhand";

	protected ModelLRKind lrKind;
	protected BlockPos startPos;
	protected EntitySize handSize;
	protected float maxDistance;
	protected int waiteTime;
	protected int showTime;
	protected int tickTime;
	protected float strength;

	public EntityElmHand(FMLPlayMessages.SpawnEntity packet, World world) {
		this(Mod_Elm.RegistryEvents.HAND, world);
	}

	public EntityElmHand(EntityType<?> etype, World world) {
		super(etype, world);
	}

	public EntityElmHand(World world, LivingEntity user, EntitySize size, float distance, float strengthIn, ModelLRKind kind) {
		super(Mod_Elm.RegistryEvents.HAND, world);
		this.handSize = size;
		this.maxDistance = distance;
		this.strength = strengthIn;
		this.lrKind = kind;

		// i進行方向を設定
		this.setVector(user, user.rotationPitch, user.rotationYaw);

		// i必要数分よこにずらしてざ行軸を回転
		double wid = (kind == ModelLRKind.RIGHT?-size.width/2:size.width/2);
		double off_x = (wid * Math.cos(this.rotationYaw/180*Math.PI));
		double off_z = (wid * Math.sin(this.rotationYaw/180*Math.PI));

		// i出現位置を設定
		this.setPosition(user.posX + this.getMotion().x * 3 + off_x,
				user.posY + this.getMotion().y + -1 * user.getHeight() * Math.sin(this.rotationPitch),
				user.posZ + this.getMotion().z * 3 + off_z);
		startPos = this.getPosition();
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
		float wid = handSize.width/2;
		float hei = handSize.height;
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
				if (Math.sqrt(this.getDistanceSq(startPos.getX(), startPos.getY(), startPos.getZ())) < this.maxDistance) {
					if (this.showTime + this.waiteTime < this.tickTime) {
						// iぶつかり判定
						this.hitEntity();
						this.move();
					}
				} else {
					this.remove();
				}
			} else if(this.showTime == this.tickTime) {
	    		this.world.playSound(null, this.getPosition(), SoundCore.SOUND_ITEM_HANDPRESS, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
		this.tickTime++;
		super.tick();
	}

	private void move() {
		// i座標移動
		Vec3d vec3d = this.getMotion();
		this.posX += vec3d.x;
		this.posY += vec3d.y;
		this.posZ += vec3d.z;
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

	protected void hitEntity() {
		AxisAlignedBB axisalignedbb = this.getBoundingBox().expand(this.getMotion());
        for(Entity entity : world.getEntitiesInAABBexcluding(this, axisalignedbb, (p_215312_0_) -> {
            return (!p_215312_0_.isSpectator() &&  !(p_215312_0_ instanceof EntityElmHand));
         })) {
			if (entity instanceof LivingEntity) {
				LivingEntity living = (LivingEntity)entity;
				int browx = living.getPosition().getX() - startPos.getX();
				int browz = living.getPosition().getZ() - startPos.getZ();
				living.knockBack(this, strength, browx<0?1:-1, browz<0?1:-1);
				living.attackEntityFrom(DamageSource.causeThornsDamage(this),1);
			} else {
				int browx = entity.getPosition().getX() - startPos.getX();
				int browz = entity.getPosition().getZ() - startPos.getZ();
				Vec3d vec3d = this.getMotion();
				Vec3d vec3d1 = (new Vec3d(browx<0?1:-1, 0.0D, browz<0?1:-1)).normalize().scale((double)strength);
				entity.setMotion(vec3d.x / 2.0D - vec3d1.x, this.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double)strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);
			}
			this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.AMBIENT, 1.0F, 1.0F);
			this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.AMBIENT, 1.0F, 1.0F, true);
        }
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return this.handSize;
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.lrKind = ModelLRKind.values()[compound.getInt("lrkind")];
		this.startPos = new BlockPos(
				compound.getInt("startx"),
				compound.getInt("starty"),
				compound.getInt("startz"));

		handSize = new EntitySize(
				compound.getFloat("realwidth"),
				compound.getFloat("realheight"),
				false);
		maxDistance = compound.getFloat("maxdistance");


		this.waiteTime = compound.getInt("waiteTime");
		this.showTime = compound.getInt("showTime");
		this.tickTime = compound.getInt("tickTime");
		this.strength = compound.getFloat("strength");
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("lrkind", this.lrKind.ordinal());
		compound.putInt("startx", this.startPos.getX());
		compound.putInt("starty", this.startPos.getY());
		compound.putInt("startz", this.startPos.getZ());
		compound.putFloat("realwidth", this.handSize.width);
		compound.putFloat("realheight", this.handSize.height);
		compound.putFloat("maxdistance", this.maxDistance);
		compound.putInt("waiteTime", this.waiteTime);
		compound.putInt("showTime", this.showTime);
		compound.putInt("tickTime", this.tickTime);
		compound.putFloat("strength", this.strength);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.lrKind.ordinal());
		buffer.writeFloat(this.handSize.height);
		buffer.writeFloat(this.handSize.width);
		buffer.writeBlockPos(this.startPos);
		buffer.writeFloat(this.maxDistance);
		buffer.writeInt(this.showTime);
		buffer.writeInt(this.tickTime);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.lrKind = ModelLRKind.values()[additionalData.readInt()];
		float h = additionalData.readFloat();
		float w = additionalData.readFloat();
		handSize = new EntitySize(w,h,false);
		this.startPos = additionalData.readBlockPos();
		this.maxDistance = additionalData.readFloat();
		this.showTime = additionalData.readInt();
		this.tickTime = additionalData.readInt();
	}

	public void setVector(Entity entityThrower, float rotationPitchIn, float rotationYawIn) {
		float x = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
		float y = -MathHelper.sin((rotationPitchIn) * ((float)Math.PI / 180F));
		float z = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));

		Vec3d vec3d = (new Vec3d(x, y, z)).normalize();
		this.setMotion(vec3d);
		float vel = MathHelper.sqrt(func_213296_b(vec3d));
		this.prevRotationYaw =  this.rotationYaw = rotationYawIn;
		this.prevRotationPitch = this.rotationPitch = rotationPitchIn;

		Vec3d vec3d2 = entityThrower.getMotion();
		this.setMotion(this.getMotion().add(vec3d2.x,  0.0D, vec3d2.z));
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
		             "StartPos       :" + this.startPos.toString() + br +
		             "Distance       :" + Math.sqrt(this.getDistanceSq(startPos.getX(), startPos.getY(), startPos.getZ())) + "/" + this.maxDistance + br +
		             "Strength       :" + this.strength + br;
		return ret;
	}
}
