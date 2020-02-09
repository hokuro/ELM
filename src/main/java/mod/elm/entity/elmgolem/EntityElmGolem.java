package mod.elm.entity.elmgolem;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import io.netty.util.internal.StringUtil;
import mod.elm.core.Mod_Elm;
import mod.elm.entity.ab.IGolemParameter;
import mod.elm.entity.ab.IHungryMob;
import mod.elm.entity.ab.IMobInfomationGetter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SCollectItemPacket;
import net.minecraft.stats.Stats;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityElmGolem extends CreatureEntity implements IMobInfomationGetter, IHungryMob{
	public static final String NAME = "elmgolem";

	private DataManagerElmGolem golemData;
	private float headRotationCourse;
	private float headRotationCourseOld;
	private boolean isShaking;
	private int inLove;
	private UUID playerInLove;
	private String mobRegisterName;
	private MobEntity mainMob;


	public EntityElmGolem(FMLPlayMessages.SpawnEntity packet, World world) {
		this(Mod_Elm.RegistryEvents.ELMGOLEM, world);
	}

	public EntityElmGolem(EntityType<? extends CreatureEntity> entityType, World world) {
		this(world,"");
	}

	public EntityElmGolem(World world, String mob) {
		super(Mod_Elm.RegistryEvents.ELMGOLEM, world);
		mobRegisterName = mob;
		golemData = new DataManagerElmGolem(this.dataManager);
	}

	public DataManagerElmGolem getGolemData() {
		return golemData;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	/*
	 * Initialize
	 */
	@Override
	protected void registerData() {
		super.registerData();
		this.golemData.registerData();
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		golemData.registerAttributes(this.getAttributes());
	}

	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		ILivingEntityData retData = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

		if (!StringUtil.isNullOrEmpty(this.mobRegisterName)) {
			Optional<EntityType<?>> entityType = Registry.ENTITY_TYPE.getValue(new ResourceLocation(this.mobRegisterName));
			if (entityType.isPresent()) {
				MobEntity living = (MobEntity)entityType.get().create((World)worldIn);
				living.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
				this.mainMob = living;
				if (living instanceof IGolemParameter) {
					this.golemData.additionalParamater((IGolemParameter)living);
				}
			}
		}

		return retData;
	}


	/*
	 *
	 * AI
	 *
	 */
	@Override
	protected void registerGoals() {
	}

	@Override
	protected void updateAITasks() {
	}

	@Override
	public void livingTick() {
		super.livingTick();
	}

	@Override
	public void tick() {
	      super.tick();
	}

	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();
		return super.processInteract(player, hand);
	}

	/*
	 *
	 * Save Data
	 *
	 */
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);

		compound.putString("targetmob_name", this.mainMob.getType().getRegistryName().toString());
		CompoundNBT nbt = new CompoundNBT();
		this.mainMob.writeAdditional(nbt);
		compound.put("targetmob_nbt", nbt);

		golemData.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

		this.mobRegisterName = compound.getString("targetmob_name");
		this.mainMob = (MobEntity) Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(this.mobRegisterName)).create(world);
		CompoundNBT nbt = (CompoundNBT)compound.get("targetmob_nbt");
		this.mainMob.read(nbt);

		golemData.readAdditional(compound);
	}

	/*
	 *
	 * Sound
	 *
	 */
	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WOLF_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_WOLF_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WOLF_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	public int getTalkInterval() {
		return 120;
	}


	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
		if (flag) {
			this.applyEnchantments(this, entityIn);
		}
		return flag;
	}

	@Override
	public void setAttackTarget(@Nullable LivingEntity entitylivingbaseIn) {
		super.setAttackTarget(entitylivingbaseIn);
	}

	@Override
	@Nullable
	public LivingEntity getAttackTarget() {
		return super.getAttackTarget();
	}


	@Override
	public boolean canAttack(LivingEntity target) {
		return super.canAttack(target);
	}

	@Override
	@Nullable
	public LivingEntity getRevengeTarget() {
		return super.getRevengeTarget();
	}

	@Override
	public int getRevengeTimer() {
		return super.getRevengeTimer();
	}

	@Override
	public void setRevengeTarget(@Nullable LivingEntity livingBase) {
		super.setRevengeTarget(livingBase);
	}


	@Override
	@Nullable
	public LivingEntity getLastAttackedEntity() {
		return super.getLastAttackedEntity();
	}

	@Override
	public int getLastAttackedEntityTime() {
		return super.getLastAttackedEntityTime();
	}

	@Override
	public void setLastAttackedEntity(Entity entityIn) {
		super.setLastAttackedEntity(entityIn);
	}

	@Override
	@Nullable
	public LivingEntity getAttackingEntity() {
		return super.getAttackingEntity();
	}

	@Override
	protected void damageArmor(float damage) {
	}

	@Override
	protected void damageShield(float damage) {

	}

	@Override
	protected float applyArmorCalculations(DamageSource source, float damage) {
		if (!source.isUnblockable()) {
			this.damageArmor(damage);
			damage = CombatRules.getDamageAfterAbsorb(damage, (float)this.getTotalArmorValue(), (float)this.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue());
		}
		return damage;
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if (!this.isInvulnerableTo(damageSrc)) {
			damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
			if (damageAmount <= 0) return;
			damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
			damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
			float f = damageAmount;
			damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
			this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
			float f1 = f - damageAmount;
			if (f1 > 0.0F && f1 < 3.4028235E37F && damageSrc.getTrueSource() instanceof ServerPlayerEntity) {
				((ServerPlayerEntity)damageSrc.getTrueSource()).addStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(f1 * 10.0F));
			}
			damageAmount = net.minecraftforge.common.ForgeHooks.onLivingDamage(this, damageSrc, damageAmount);
			if (damageAmount != 0.0F) {
				float f2 = this.getHealth();
				this.getCombatTracker().trackDamage(damageSrc, f2, damageAmount);
				this.setHealth(f2 - damageAmount); // Forge: moved to fix MC-121048
				this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
			}
		}
	}


	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
		net.minecraftforge.event.entity.living.LivingKnockBackEvent event = net.minecraftforge.common.ForgeHooks.onLivingKnockBack(this, entityIn, strength, xRatio, zRatio);
		if(event.isCanceled()) return;
		strength = event.getStrength(); xRatio = event.getRatioX(); zRatio = event.getRatioZ();
		if (!(this.rand.nextDouble() < this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue())) {
			this.isAirBorne = true;
			Vec3d vec3d = this.getMotion();
			Vec3d vec3d1 = (new Vec3d(xRatio, 0.0D, zRatio)).normalize().scale((double)strength);
			this.setMotion(vec3d.x / 2.0D - vec3d1.x, this.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double)strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);
		}
	}


	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
	}

	@Override
	protected void outOfWorld() {
		this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);
	}

	@Override
	protected int getExperiencePoints(PlayerEntity player) {
		return 0;
	}

	@Override
	protected void dropLoot(DamageSource p_213354_1_, boolean p_213354_2_) {

	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
		super.dropSpecialItems(source, looting, recentlyHitIn);
	}

	@Override
	protected float getDropChance(EquipmentSlotType slotIn) {
		return 0;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 8) {
			this.isShaking = true;
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (LIVING_FLAGS.equals(key) && this.world.isRemote) {
			if (this.isHandActive() && this.activeItemStack.isEmpty()) {
				this.activeItemStack = this.getHeldItem(this.getActiveHand());
				if (!this.activeItemStack.isEmpty()) {
					this.activeItemStackUseCount = this.activeItemStack.getUseDuration();
				}
			} else if (!this.isHandActive() && !this.activeItemStack.isEmpty()) {
				this.activeItemStack = ItemStack.EMPTY;
				this.activeItemStackUseCount = 0;
			}
		}
		super.notifyDataManagerChange(key);
	}

	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return super.canBeLeashedTo(player);
	}

	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	protected void checkDespawn() {

	}

	@Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
		return false;
	}

	/*
	 *
	 * Equipments
	 *
	 */
	@Override
	public Iterable<ItemStack> getHeldEquipment() {
		return super.getHeldEquipment();
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList(){
		return super.getArmorInventoryList();
	}

	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
		return super.getItemStackFromSlot(slotIn);
	}

	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
		super.setItemStackToSlot(slotIn, stack);
	}

	@Override
	public boolean startRiding(Entity entityIn, boolean force) {
		boolean flag = super.startRiding(entityIn, force);
		if (flag && this.getLeashed()) {
			this.clearLeashed(true, true);
		}
		return flag;
	}

	@Override
	public void stopRiding() {
		Entity entity = this.getRidingEntity();
		super.stopRiding();
		if (entity != null && entity != this.getRidingEntity() && !this.world.isRemote) {
			this.dismountEntity(entity);
		}
	}

	@Override
	public void updateRidden() {
		super.updateRidden();
		this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
		this.onGroundSpeedFactor = 0.0F;
		this.fallDistance = 0.0F;
	}

	@Override
	public void onItemPickup(Entity entityIn, int quantity) {
		if (!entityIn.removed && !this.world.isRemote && (entityIn instanceof ItemEntity || entityIn instanceof AbstractArrowEntity || entityIn instanceof ExperienceOrbEntity)) {
			((ServerWorld)this.world).getChunkProvider().sendToAllTracking(entityIn, new SCollectItemPacket(entityIn.getEntityId(), this.getEntityId(), quantity));
		}
	}

	@Override
	public boolean canEntityBeSeen(Entity entityIn) {
		Vec3d vec3d = new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
		Vec3d vec3d1 = new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ);
		return this.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() == RayTraceResult.Type.MISS;
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return true;
	}

	@Override
	public boolean hitByEntity(Entity entityIn) {
		return false;
	}

	@Override
	public void recalculateSize() {
		super.recalculateSize();
//		EntitySize entitysize = this.size;
//		Pose pose = this.getPose();
//		EntitySize entitysize1 = this.getSize(pose);
//		this.size = entitysize1;
//		this.eyeHeight = getEyeHeightForge(pose, entitysize1);
//		if (entitysize1.width < entitysize.width) {
//			double d0 = (double)entitysize1.width / 2.0D;
//			this.setBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double)entitysize1.height, this.posZ + d0));
//		} else {
//			AxisAlignedBB axisalignedbb = this.getBoundingBox();
//			this.setBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)entitysize1.width, axisalignedbb.minY + (double)entitysize1.height, axisalignedbb.minZ + (double)entitysize1.width));
//			if (entitysize1.width > entitysize.width && !this.firstUpdate && !this.world.isRemote) {
//				float f = entitysize.width - entitysize1.width;
//				this.move(MoverType.SELF, new Vec3d((double)f, 0.0D, (double)f));
//			}
//		}
	}

	@Override
	public int getHunger() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getMaxHunger() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setHunger(int value) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void addHunger(int value) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public String getInformation() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}