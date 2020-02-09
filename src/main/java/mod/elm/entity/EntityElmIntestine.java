package mod.elm.entity;
import mod.elm.core.Mod_Elm;
import mod.elm.core.log.ModLog;
import mod.elm.util.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityElmIntestine extends ThrowableEntity {

	public static final DataParameter<Integer> OWNERS = EntityDataManager.createKey(EntityElmIntestine.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> LENGTH = EntityDataManager.createKey(EntityElmIntestine.class, DataSerializers.VARINT);
	public static final String NAME = "intestine";
	public static final int WATE_TIME = 20;
	private boolean isRet;
	private Entity target;
	private ItemStack base;
	private int wait_count = 0;

	public EntityElmIntestine(FMLPlayMessages.SpawnEntity packet, World world) {
		this(Mod_Elm.RegistryEvents.INTESTINE, world);
	}

	public EntityElmIntestine(EntityType<? extends ThrowableEntity> etype, World world) {
		super(etype, world);
		isRet = false;
		target = null;
	}

	public EntityElmIntestine(World world, LivingEntity entityliving, ItemStack stack, int len) {
		super(Mod_Elm.RegistryEvents.INTESTINE, entityliving, world);
		isRet = false;
		target = null;
		base = stack;
		this.setLength(len);
	}

	@Override
	public void tick() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		if (!this.world.isRemote) {
			if (!this.isRet) {
				// i投げたやつとの距離を取得
				LivingEntity thrower = this.getThrower();
				if (Math.sqrt(this.getDistanceSq(thrower)) <= this.getLength() && wait_count < WATE_TIME) {
					// iぶつかり判定
					AxisAlignedBB axisalignedbb = this.getBoundingBox().expand(this.getMotion()).grow(5.0D);
					RayTraceResult raytraceresult = ProjectileHelper.func_221267_a(this, axisalignedbb, (p_213880_1_) -> {return !p_213880_1_.isSpectator() && p_213880_1_.canBeCollidedWith();}, RayTraceContext.BlockMode.OUTLINE, true);

					if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
						this.onImpact(raytraceresult);
					}
					if (wait_count <= 0) {
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
					}else {
						wait_count++;
					}
				} else {
					this.wait_count++;
					if (wait_count >= WATE_TIME) {
						// i折り返す
						this.isRet = true;
						this.setMotion(0,0,0);
					}
				}
			} else {
				// i投げ主の元に戻ってくるようにする
				LivingEntity entity = this.getThrower();
				Vec3d vec3d = new Vec3d(entity.posX - this.posX, entity.posY + (double)entity.getEyeHeight() - this.posY, entity.posZ - this.posZ);
				double d0 = 0.05D;
				this.setMotion(this.getMotion().scale(0.95D).add(vec3d.normalize().scale(d0)));
				Vec3d vec3d1 = this.getMotion();
				this.posX += vec3d1.x;
				this.posY += vec3d1.y;
				this.posZ += vec3d1.z;
				this.setPosition(this.posX, this.posY, this.posZ);
				if (!this.world.isRemote) {
					if (this.getDistanceSq(entity) < 3) {
						if (this.base.getMaxDamage() > this.base.getDamage() + 1) {
							if (entity instanceof PlayerEntity) {
									this.base.setDamage(this.base.getDamage() + 1);
									((PlayerEntity)entity).addItemStackToInventory(this.base);
							} else {
								ModUtil.spawnItemStack(this.world, this.posX, this.posY, this.posZ, this.base, Mod_Elm.rnd);
							}
						}
						this.remove();
					}
				}
				if (this.target != null) {
					// iくっついてるエンティティも一緒に持って帰る
					target.setPosition(this.posX, this.posY, this.posZ);
					target.fallDistance = 0;
				}
			}
		}
		Entity et = this.getThrower();
		Vec3d polar = ModUtil.makePolar(new Vec3d(et.posX-this.posX, et.posY - this.posY, et.posZ - this.posZ).normalize());
		this.prevRotationPitch = this.rotationPitch = (float)polar.y;
		this.prevRotationYaw = this.rotationYaw = (float)polar.z;
	}

	@Override
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		super.shoot(entityThrower, rotationPitchIn, rotationYawIn, pitchOffset, velocity, inaccuracy);
		setID(entityThrower.getEntityId());
	}


	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.getType() == RayTraceResult.Type.ENTITY) {
			target = ((EntityRayTraceResult)result).getEntity();
		}
		this.playSound(SoundEvents.BLOCK_SLIME_BLOCK_STEP, 1.0F, 1.0F);
		this.isRet = true;
		this.setMotion(0,0,0);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(OWNERS, 0);
		this.dataManager.register(LENGTH, 0);
	}

	public int getID() {
		return this.dataManager.get(OWNERS);
	}

	protected void setID(int id) {
		this.dataManager.set(OWNERS, id);
	}

	public int getLength() {
		return this.dataManager.get(LENGTH);
	}

	protected void setLength(int len) {
		this.dataManager.set(LENGTH, len);
	}


	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("isret", isRet);
		if (target != null) {
			compound.putInt("target", target.getEntityId());
		}
		compound.put("baseitem", base.write(new CompoundNBT()));
		compound.putInt("wait", wait_count);
		compound.putInt("length", this.getLength());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.isRet = compound.getBoolean("isret");
		if (compound.contains("target")) {
			int id = compound.getInt("target");
			target = world.getEntityByID(id);
		}
		base = ItemStack.read(compound.getCompound("baseitem"));
		wait_count = compound.getInt("wait");
		this.setID(this.owner.getEntityId());
		this.setLength(compound.getInt("length"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.setMotion(x, y, z);
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
			this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (double)(180F / (float)Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}

	@Override
	public LivingEntity getThrower() {
		LivingEntity ret = super.getThrower();
		try {
			if (ret == null) {
				ret = (LivingEntity) this.world.getEntityByID(this.getID());
				owner = ret;
			}
		}catch(Throwable e) {
			ModLog.log().error("cant get throwable entity");
		}
		return ret;
	}
}

