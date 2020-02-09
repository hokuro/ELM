package mod.elm.entity;

import java.util.Iterator;
import java.util.UUID;

import mod.elm.core.Mod_Elm;
import mod.elm.core.log.ModLog;
import mod.elm.entity.ab.IMobInfomationGetter;
import mod.elm.item.ItemCore;
import mod.elm.sound.SoundCore;
import mod.elm.util.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityElmBrain extends Entity implements IEntityAdditionalSpawnData , IMobInfomationGetter{
	public static final String NAME = "elmbrain";
	public static final int PHASE_SPAWN = 0;
	public static final int PHASE_SEARCH =1;
	public static final int PHASE_TRACE = 2;
	public static final int PHASE_HUGGER = 3;
	public static final int PHASE_DEAD = 4;
	protected static final int[] PHASE = new int[] {PHASE_SPAWN,PHASE_SEARCH,PHASE_TRACE,PHASE_HUGGER,PHASE_DEAD};

	public static final DataParameter<Integer> DM_PHASE = EntityDataManager.createKey(EntityElmBrain.class, DataSerializers.VARINT);
	public static final DataParameter<Float> DM_HEALTH = EntityDataManager.createKey(EntityElmBrain.class, DataSerializers.FLOAT);

	public static final int LIFE_TIME = 5 * 60 * 20;	// 5分以上生きられない
	public static final int SEARCH_START = 5 * 20;		// 5秒間クルクルサーチ
	public static final int DAMGE_SPAN = 60;			// 3秒おきにダメージを与える
	public static final int DAMAGE_COUNT = 7;			// i最大7回ダメージを与える

	protected int tickCount;
	protected int lifeCount;
	protected int damageCount;
	protected BlockPos goalPos;
	protected LivingEntity target;
	protected int attackDamage;
	protected int prevPhase;
	public int hurtTime;


	public EntityElmBrain(FMLPlayMessages.SpawnEntity packet, World world) {
		this(Mod_Elm.RegistryEvents.BRAIN, world);
	}

	public EntityElmBrain(EntityType<?> etype, World world) {
		super(etype, world);
		init();
	}

	public EntityElmBrain(World world, LivingEntity user, int damageIn) {
		super(Mod_Elm.RegistryEvents.BRAIN, world);
		init();
		attackDamage = damageIn;

		this.initRotation(user.rotationPitch, user.rotationYaw);
		this.setPosition(user.posX, user.posY+user.getEyeHeight(), user.posZ);
		this.spawan(user.getPosition(), user.getHeight());
	}

	public EntityElmBrain(World world, LivingEntity user, int damageIn, LivingEntity targetIn) {
		super(Mod_Elm.RegistryEvents.BRAIN, world);
		init();

		attackDamage = damageIn;
		target = targetIn;

		// i直接つけた場合ライフカウントは0、攻撃回数も最大
		this.lifeCount = 0;
		this.damageCount = DAMAGE_COUNT-1;
		this.setPhase(PHASE_HUGGER);

		this.initRotation(target.rotationPitch, target.rotationYaw);
		this.setPosition(target.posX, target.posY + target.getHeight(), target.posZ);
	}

	public void init() {
		tickCount = 0;
		lifeCount = LIFE_TIME;
		target = null;

		this.setHealth(10);
		this.setPhase(0);
		prevPhase = this.getPhase();
	}

	private void initRotation(float pitch, float yaw) {
		Vec3d vec3d = ModUtil.makeVec(pitch, yaw);
		Vec3d polar = ModUtil.makePolar(vec3d.x, vec3d.y, vec3d.z);
		this.prevRotationYaw = this.rotationYaw = (float)polar.z;
		this.prevRotationPitch = this.rotationPitch = 0.0F;
	}

	private void spawan(BlockPos spawanPos, float height) {
		this.goalPos = spawanPos.add(0, height + 5, 0);
	}

	@Override
	public void tick() {
		if (!this.world.isRemote) {
			// iモーションを再計算
			calculateMotion();

			//i フェイズごとの処理
			switch(prevPhase) {
			case PHASE_SPAWN:
				if (move(2.0F)) {
					// i次のフェイズへ
					this.nextPhase();
				}
				break;
			case PHASE_SEARCH:
				if (tickCount >= SEARCH_START) {
					// iターゲットを検索
					if (searchTarget()) {
						// i 次のフェイズへ
						this.nextPhase();
					}
				}
				break;
			case PHASE_TRACE:
				if (move(2.0F)) {
					if (checkHelm()) {
						this.nextPhase();
						damageCount = 0;
					}
				}

				// iターゲットを見失ったら前のフェイズへ戻る
				if (target == null || !target.isAlive()) {
					this.prevPhase();
				}
				break;
			case PHASE_HUGGER:
				if (!hugger()) {
					this.nextPhase();
				} else {
					// i移動
					move(0.0F);
				}
				break;
			case PHASE_DEAD:
				this.remove();
				break;
			}
		}else {
			if (this.getPhase() == PHASE_SEARCH) {
				if ((this.tickCount % 10) == 0)
				this.world.playSound(this.posX, this.posY, this.posZ, SoundCore.SOUND_ITEM_BRAINSEARCH, SoundCategory.AMBIENT, 1.0F, 1.0F, true);
			} else if (this.getPhase() == PHASE_DEAD) {
				// i最終フェイズ
				for (int i  = 0; i < 30; i++ ) {
					this.world.addParticle(ParticleTypes.SMOKE,
							this.posX + 0.5F - ModUtil.randomF(), this.posY, this.posZ + 0.5F - ModUtil.randomF(),
							0.5F - ModUtil.randomF(), ModUtil.randomF(), 0.5F - ModUtil.randomF());
				}
				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.AMBIENT, 1.0F, 1.0F, true);
			}
		}
		// iティック更新
		this.tickCount++;
		// iフェイズが変わったらカウントをリセット
		if (prevPhase != this.getPhase()) {
			this.tickCount = 0;
			prevPhase = this.getPhase();
		}

		// iライフカウント計算
		if (this.getPhase() != PHASE_HUGGER) {
			// i攻撃フェイズ以外の場合ライフカウントを減らす
			this.lifeCount--;
			if (this.lifeCount < 0 && (Math.abs(this.lifeCount) % 25) == 0 ) {
				// iライフカウントが0以下になったら25tick毎に体力を減らす
				this.attackEntityFrom(DamageSource.DROWN, 1);
			}
		}
		if (hurtTime > 0) {
			hurtTime --;
		}
	}

	private boolean checkHelm() {
		boolean ret = true;
		Iterator<ItemStack> it = target.getArmorInventoryList().iterator();
		while(it.hasNext() && ret) {
			ItemStack item = it.next();
			// i頭装備をつけている場合は取り付けない
			if (item.getItem() instanceof ArmorItem &&  ((ArmorItem)item.getItem()).getEquipmentSlot() == EquipmentSlotType.HEAD) {
				ret = false;
				target = null;
			}
		}
		return ret;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.world.isRemote) {
			hurtTime = 5;
			return false;}
		if (source.isFireDamage()) {return false;}
		this.setHealth(this.getHealth() - amount);
		if (this.getHealth() <= 0.0) {
			// i死亡フェイズへ
			this.setPhase(PHASE_DEAD);
			return false;
		}

		// i鳴き声
		this.world.playSound(null,this.posX, this.posY, this.posZ, SoundCore.SOUND_ITEM_BRAINHURT, SoundCategory.AMBIENT, 1.0F, 1.0F);
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void outOfWorld() {
		this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);
	}

	private boolean move(float closs) {
		if (this.getPhase() != PHASE_HUGGER) {
			Vec3d vec3d = this.getMotion();
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;

			this.prevPosX += vec3d.x;
			this.prevPosY += vec3d.y;
			this.prevPosZ += vec3d.z;
			if (this.isInWater()) {
				for(int i = 0; i < 4; ++i) {
					float f2 = 0.25F;
					this.world.addParticle(ParticleTypes.BUBBLE, this.posX - vec3d.x * 0.25D, this.posY - vec3d.y * 0.25D, this.posZ - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
				}
			}
			this.setPosition(this.prevPosX, this.prevPosY, this.prevPosZ);
			if (Math.sqrt(this.getDistanceSq(goalPos.getX(), goalPos.getY(), goalPos.getZ())) <= closs) {
				return true;
			}
		} else {
			this.prevPosX += target.prevPosX;
			this.prevPosY += target.prevPosY + target.getHeight();
			this.prevPosZ += target.prevPosZ;

			// iターゲットの位置に移動
			this.setPosition(target.posX, target.posY + target.getHeight(), target.posZ);
			this.prevRotationPitch = this.rotationPitch = 0.0F;
			this.prevRotationYaw = this.rotationYaw = target.rotationYaw;
		}
		return false;
	}

	private void calculateMotion() {
		switch(this.getPhase()) {
		case PHASE_SPAWN:
			if (this.goalPos.getY() <= this.getPosition().getY()) {
				// i自分が目的地の上空にいる場合目的地を書き換える
				this.goalPos = this.getPosition();
				// 0.4ずつ下降
				this.setMotion(0, -0.01, 0);
			}else {
				// i0.4ずつ上昇
				this.setMotion(0,0.05,0);
			}
			break;
		case PHASE_SEARCH:
			// i移動なし 15度ずつ回転
			this.setMotion(0, 0, 0);
			this.rotationYaw += 15;
			break;
		case PHASE_TRACE:
			if (target != null && target.isAlive()) {
				int md = tickCount % 60;
				if (md > 0 && md < 40) {
					// i2秒間加速しながらターゲットを追いかける
					Vec3d vec3d = this.getMotion().normalize().scale(md/10.0F);
					this.setMotion(vec3d);
				} else if (md == 0 || md >= 40) {
					this.goalPos = this.target.getPosition();
					// i距離と角度を再計算
					double x = target.posX - this.posX;
					double y = target.posY - this.posY;
					double z = target.posZ - this.posZ;
					Vec3d vec3d = new Vec3d(x,y,z).normalize();
					if (md == 0) {
						// 3秒に一度モーションを再設定
						this.setMotion(vec3d);
					} else {
						// 3秒に1秒移動を止めて向きだけ変える
						this.setMotion(Vec3d.ZERO);
					}

					// i角度を計算
					Vec3d polar = ModUtil.makePolar(vec3d.x, vec3d.y, vec3d.z);
					this.prevRotationPitch = this.rotationPitch = (float)polar.y;
					this.prevRotationYaw = this.rotationYaw = (float)polar.z;
				}
			}
			break;
		case PHASE_HUGGER:
			// i移動しない
			this.setMotion(Vec3d.ZERO);
			break;
		case PHASE_DEAD:
			// i移動しない
			this.setMotion(Vec3d.ZERO);
			break;
		}
	}

	private boolean searchTarget() {
		// 64 * 64 範囲の生き物をランダムに1体選択
		boolean ret = false;
		LivingEntity entity = ModUtil.randomListSelect(this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().expand(-32,-32,-32).expand(32,32,32),
				(et)->{return (!et.isSpectator() && (et instanceof MobEntity || et instanceof PlayerEntity) && et.isAlive());}));
		if (entity != null) {
			target = entity;
			ret = true;
		}
		return ret;
	}

	private boolean hugger() {
		boolean ret = true;
		if (target != null && target.isAlive() && this.damageCount < DAMAGE_COUNT) {
			if (tickCount > 20 * 5 && (tickCount % DAMGE_SPAN) == 0) {
				target.attackEntityFrom(Mod_Elm.DamageSourceBrain, ModUtil.random_1(this.attackDamage));
				damageCount ++;
				world.playSound(null, this.posX, this.posY, this.posZ, SoundCore.SOUND_ITEM_BRAINDAMAGE, SoundCategory.VOICE, 1.0F, 1.0F);
			}
			this.goalPos = target.getPosition();
			ret = target.isAlive();
		}else {
			ret = false;
		}
		return ret;
	}

	@Override
	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() == ItemCore.item_mobanalyzer && hand == Hand.MAIN_HAND) {
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

	@Override
	protected void registerData() {
		this.dataManager.register(DM_PHASE,0);
		this.dataManager.register(DM_HEALTH, 10.0F);
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.setPhase(compound.getInt("pahse"));
		this.prevPhase = compound.getInt("prevphase");
		this.setHealth(compound.getFloat("health"));
		this.tickCount = compound.getInt("tickCount");
		this.lifeCount = compound.getInt("lifeCount");
		this.damageCount = compound.getInt("damageCount");
		this.attackDamage = compound.getInt("attack");
		this.goalPos = ModUtil.getBlockPos(compound, "goal");

		if (compound.contains("target_id")) {
			String id = compound.getString("target_id");
			if (!this.world.isRemote) {
				Entity et = ((ServerWorld)world).getEntityByUuid(UUID.fromString(id));
				if (et instanceof LivingEntity) {
					target = (LivingEntity)et;
				}
			}
		} else {
			target = null;
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("pahse", this.getPhaseIndex());
		compound.putInt("prevphase", this.prevPhase);
		compound.putFloat("health", this.getHealth());
		compound.putInt("tickCount", this.tickCount);
		compound.putInt("lifeCount", this.lifeCount);
		compound.putInt("damageCount", this.damageCount);
		compound.putInt("attack", this.attackDamage);
		compound = ModUtil.putBlockPos(compound, "goal", this.goalPos);
		if (target != null) {
			compound.putString("target_id", target.getUniqueID().toString());
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public int getLifeCount() {
		return this.lifeCount;
	}

	public int getTickCount() {
		return this.tickCount;
	}

	protected void nextPhase() {
		setPhase(getPhaseIndex()+1);
	}

	protected void prevPhase() {
		setPhase(getPhaseIndex()-1);
	}

	protected void setPhase(int phase) {
		this.dataManager.set(DM_PHASE, phase);
	}

	public int getPhase() {
		return PHASE[getPhaseIndex()];
	}

	protected int getPhaseIndex() {
		return this.dataManager.get(DM_PHASE);
	}

	protected void setHealth(float value) {
		this.dataManager.set(DM_HEALTH, value);
	}

	public float getHealth() {
		return this.dataManager.get(DM_HEALTH);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.lifeCount);
		buffer.writeInt(this.tickCount);
		buffer.writeInt(this.prevPhase);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.lifeCount = additionalData.readInt();
		this.tickCount = additionalData.readInt();
		this.prevPhase = additionalData.readInt();
	}

	@Override
	public String getInformation() {
		String br = System.getProperty("line.separator");
		String ret = "HP               :" + this.getHealth()+ "/10" + br +
		             "Phase            :" + this.prevPhase + "/" + this.getPhase() + br +
		             "TickCount        :" + this.tickCount + br +
		             "LifeCount        :" + this.lifeCount + br +
		             "DamageCount      :" + this.damageCount + br +
		             "GoalPos          :" + this.goalPos + br +
		             "Move             :" + this.getMotion().toString() + br +
		             "Pos              :" + this.getPosition().toString() + br +
		             "Yaw              :" + this.rotationYaw + br +
		             "Pitch            :" + this.rotationPitch + br +
		             "Distance         :" + Math.sqrt(this.getDistanceSq(goalPos.getX(), goalPos.getY(), goalPos.getZ())) + br +
		             "AttackDamage     :" + this.attackDamage + br;
		return ret;
	}

}
