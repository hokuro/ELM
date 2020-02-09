package mod.elm.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import javax.annotation.Nullable;

import mod.elm.config.MyModConfig;
import mod.elm.core.Mod_Elm;
import mod.elm.core.log.ModLog;
import mod.elm.entity.ab.IGolemParameter;
import mod.elm.entity.ab.IHungryMob;
import mod.elm.entity.ab.IMobInfomationGetter;
import mod.elm.entity.ai.gal.FollowAtFoods;
import mod.elm.entity.ai.gal.LookAtJewelry;
import mod.elm.entity.ai.gal.SearchCake;
import mod.elm.entity.ai.gal.SwordPanicGoal;
import mod.elm.entity.ai.gal.WaterAvoidingRandomHome;
import mod.elm.render.model.IModelElmTexture;
import mod.elm.sound.Sound_WanderingPeople;
import mod.elm.util.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityWanderingPeople extends CreatureEntity implements IMobInfomationGetter, IHungryMob, IGolemParameter{
	public static final String NAME = "wanderingpeopel";
	public static final int MAX_HUNGER = 20;
	public static final int HUNGER_TIME_SPAN = 1200;
	public static final DataParameter<Integer> MODEL_TYPE1 = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> MODEL_TYPE2 = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Boolean> IS_INTAREST = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Integer> HUNGER = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.BLOCK_POS);
	public static final DataParameter<Integer> POCKET_TRASH_COUNT = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> POCKET_EMELALD_COUNT = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> POCKET_DIAMOND_COUNT = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> POCKET_GOLDINGOT_COUNT = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> POCKET_GOLDNUGGET_COUNT = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> POCKET_LAPISLAZRI_COUNT = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> POCKET_FOOD_COUNT = EntityDataManager.createKey(EntityWanderingPeople.class, DataSerializers.VARINT);
	private Map<Item,IntSupplier> tresurGetter = new HashMap<Item,IntSupplier>(){
		{put(Items.EMERALD,  EntityWanderingPeople.this::getEmeCount);}
		{put(Items.DIAMOND,  EntityWanderingPeople.this::getDiaCount);}
		{put(Items.GOLD_INGOT,  EntityWanderingPeople.this::getGoldIngCount);}
		{put(Items.GOLD_NUGGET,  EntityWanderingPeople.this::getGoldNuggetCount);}
		{put(Items.LAPIS_LAZULI,  EntityWanderingPeople.this::getLapisCount);}
	};
	private Map<Item,IntConsumer> tresurSetter = new HashMap<Item,IntConsumer>(){
		{put(Items.EMERALD,  value -> setEmeCount(value));}
		{put(Items.DIAMOND,  value -> setDiaCount(value));}
		{put(Items.GOLD_INGOT,  value -> setGoldIngCount(value));}
		{put(Items.GOLD_NUGGET,  value -> setGoldNuggetCount(value));}
		{put(Items.LAPIS_LAZULI,  value -> setLapisCount(value));}
	};
	protected int ai_voice_tick = 0;
	protected int living_tick = 0;
	protected boolean  candespawn;

	public EntityWanderingPeople(EntityType<? extends EntityWanderingPeople> type, World worldIn) {
		super(type, worldIn);
	}

	public EntityWanderingPeople(FMLPlayMessages.SpawnEntity packet, World world) {
		this(Mod_Elm.RegistryEvents.WANDERINGPEOPEL, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new SwordPanicGoal(this, 3.0D));
		this.goalSelector.addGoal(1, new PanicGoal(this, 3.0D));
		this.goalSelector.addGoal(2, new LookAtJewelry(this, 1.25D, Ingredient.fromItems(Items.EMERALD, Items.DIAMOND, Items.GOLD_NUGGET, Items.GOLD_INGOT, Items.LAPIS_LAZULI),false));
		this.goalSelector.addGoal(2, new FollowAtFoods(this, 1.25D, false));
		this.goalSelector.addGoal(3, new SearchCake(this, 3.25D, 10));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomHome(this, 1.0D));
		this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(MODEL_TYPE1, 0);
		this.dataManager.register(MODEL_TYPE2, 0);
		this.dataManager.register(IS_INTAREST,false);
		this.dataManager.register(HUNGER, 0);
		this.dataManager.register(HOME_POS, BlockPos.ZERO);
		this.dataManager.register(POCKET_TRASH_COUNT, 0);
		this.dataManager.register(POCKET_EMELALD_COUNT, 0);
		this.dataManager.register(POCKET_DIAMOND_COUNT, 0);
		this.dataManager.register(POCKET_GOLDINGOT_COUNT, 0);
		this.dataManager.register(POCKET_GOLDNUGGET_COUNT, 0);
		this.dataManager.register(POCKET_LAPISLAZRI_COUNT, 0);
		this.dataManager.register(POCKET_FOOD_COUNT, 0);

	}

	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		initModelType();
		initPockets_Trash(difficultyIn);
		initPockets_Foods(difficultyIn);
		candespawn = true;
		this.setHunger(10 + ModUtil.random(11));
		this.setHome(new BlockPos(this));
		ModLog.log().debug(this.getHome().toString());
		return spawnDataIn;
	}

	private void initModelType() {
		int tp1 = ModUtil.random(Integer.MAX_VALUE);
		this.setModelType(tp1);
		int tp2 = ModUtil.random(Integer.MAX_VALUE);
		this.setModelType2(tp2);
		ModLog.log().debug("ModelType : " + tp1 + " : " + tp2);
	}

	private Map<Integer,Integer> hasItemStack = new HashMap<Integer,Integer>(){
		{put(300,1);} {put(800,2);} {put(1600,3);} {put(2600,4);} {put(5400,5);}
		{put(7400,6);} {put(8400,7);} {put(9200,8);} {put(9700,9);} {put(10000,10);}
	};

	private Map<Integer,Integer> hasItemCount = new HashMap<Integer,Integer>(){
		{put(100,1);} {put(600,2);} {put(1400,3);} {put(2400,4);} {put(5400,5);}
		{put(6400,6);} {put(8400,7);} {put(9200,8);} {put(9700,9);} {put(10000,10);}
	};

	private void initPockets_Trash(DifficultyInstance difficultyIn) {
		// i埋めるインベントリの数を決める
		int work = ModUtil.random(10000);
		int cnt = 0;
		for(int key : hasItemStack.keySet()) {
			if (work < key) {
				cnt = hasItemStack.get(key);
				break;
			}
		}
		int sum = 0;
		for (int i = 0; i < cnt; i++) {
			// aアイテムの個数を決める
			work = ModUtil.random(10000);
			for(int key : hasItemCount.keySet()) {
				if (work < key) {
					sum += hasItemCount.get(key);
					break;
				}
			}
		}
		this.setTrashCount(sum);
	}

	private void initPockets_Foods(DifficultyInstance difficultyIn) {
		int cnt = ModUtil.random(400)%16 + 3;
		this.setFoodCount(Items.BREAD.getFood().getHealing() * cnt);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.2F);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		ai_voice_tick ++;
		this.goalSelector.getRunningGoals().forEach((e)->{
			if (e.isRunning() &&  e.getGoal() instanceof LookAtJewelry) {
				// i宝石見つめ目ボイス再生
				if (ai_voice_tick % 200 == 0) {
					sound_voice_juery(((LookAtJewelry)e.getGoal()).getItem());
				}
				this.candespawn = false;
				this.idleTime = 0;
				this.setIntarest(true);
				return;
			}else if (e.isRunning() && e.getGoal() instanceof FollowAtFoods) {
				// 食べ物見つめ目ボイス
				if (ai_voice_tick % 200 == 0) {
					sound_voice_followfoods(((FollowAtFoods)e.getGoal()).getItem());
				}
				this.candespawn = false;
				this.idleTime = 0;
				this.setIntarest(true);
				return;
			}else if (e.isRunning() && e.getGoal() instanceof SearchCake) {
				// iケーキボイス
				if (ai_voice_tick % 200 == 0) {
					sound_voice_cake();
				}
				this.candespawn = false;
				this.idleTime = 0;
				this.setIntarest(true);
				return;
			} else if (e.isRunning() && e.getGoal() instanceof SwordPanicGoal) {
				// i恐怖ボイス
				if (ai_voice_tick % 200 == 0) {
					sound_voice_affrade();
				}
				this.candespawn = false;
				this.idleTime = 0;
				return;
			} else {
				this.candespawn = true;
				this.setIntarest(false);
			}
		});
	}


	@Override
	public void livingTick() {
		super.livingTick();
		living_tick++;
		if (!this.world.isRemote) {
			if ((living_tick % HUNGER_TIME_SPAN) == 0) {
				// iおなかへりんこ
				this.degreesHunger();
			}
			if (this.getHunger() <= 0.0F) {
				// i完全に空腹なら持ち物のアイテムを食べる
				eatFood();
			}
		}
	}

	private void eatFood() {
		int value = this.getFoodCount();
		int heal = Math.min(value, 10);
		this.increaseHunger(heal);
		this.setFoodCount(value - heal);
		this.idleTime = 0;
	}


	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
		return worldIn.getBrightness(pos) - 0.5F;
	}

	@Override
	public BlockPos getHomePosition() {
		return this.getHome();
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("modeltype1", this.getModelType());
		compound.putInt("modeltype2", this.getModelType2());
		compound.putBoolean("isIntarest", this.isIntarest());
		compound.putInt("hunger", this.getHunger());
		compound.putInt("HomePosX", this.getHome().getX());
		compound.putInt("HomePosY", this.getHome().getY());
		compound.putInt("HomePosZ", this.getHome().getZ());

		compound.putInt("pockets_trash", this.getTrashCount());
		for(ItemStack item : EntityWanderingPeople.ITEM_TRESUR.getMatchingStacks()) {
			compound.putInt("pockets_tresur_" + item.getItem().getRegistryName().getPath(), tresurGetter.get(item.getItem()).getAsInt());
		}
		compound.putInt("pockets_food", this.getFoodCount());
	}


	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		int tp1 = compound.getInt("modeltype1");
		if (tp1 == 0) { tp1 = ModUtil.random(Integer.MAX_VALUE);}
		this.setModelType(tp1);
		int tp2 = compound.getInt("modeltype2");
		if (tp2 == 0) { tp2 = ModUtil.random(Integer.MAX_VALUE);}
		this.setModelType2(tp2);
		this.setIntarest(compound.getBoolean("isIntarest"));
		this.setHunger(compound.getInt("hunger"));

		int i = compound.getInt("HomePosX");
		int j = compound.getInt("HomePosY");
		int k = compound.getInt("HomePosZ");
		this.setHome(new BlockPos(i, j, k));

		this.setTrashCount(compound.getInt("pockets_trash"));
		for(ItemStack item : EntityWanderingPeople.ITEM_TRESUR.getMatchingStacks()) {
			int value = compound.getInt("pockets_tresur_" + item.getItem().getRegistryName().getPath());
			tresurSetter.get(item.getItem()).accept(value);
		}
		this.setFoodCount(compound.getInt("pockets_food"));
	}

	@Override
	protected void spawnDrops(DamageSource source) {
		super.spawnDrops(source);

		if (!this.world.isRemote) {
			// iプレイヤーによる攻撃でお宝ポケットの中身を落とす
			if (source.getTrueSource() instanceof PlayerEntity && source != DamageSource.IN_FIRE && source != DamageSource.ON_FIRE && source != DamageSource.FIREWORKS) {
				for (ItemStack item : EntityWanderingPeople.ITEM_TRESUR.getMatchingStacks()) {
					dropTresur(item.getItem(), this.tresurGetter.get(item.getItem()).getAsInt());
				}
			}
		}
	}

	private void dropTresur(Item item, int count) {
		int wcount = count;
		while(wcount > 0) {
			int dropCount = Math.min(wcount, item.getMaxStackSize());
			ModUtil.spawnItemStack(this.world, this.posX, this.posY, this.posZ, new ItemStack(item, dropCount), Mod_Elm.rnd);
			wcount -= dropCount;
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return Sound_WanderingPeople.getAmbientSound(this.world, this);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return Sound_WanderingPeople.getDamageVoice(source, this);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return Sound_WanderingPeople.getDeathSound();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(Sound_WanderingPeople.getStepSound(), 0.15F, 1.0F);
	}

	private void sound_voice_followfoods(Item item) {
		if (item != Items.AIR) {
			SoundEvent event = Sound_WanderingPeople.getVoiceFollowFoods(item, this);
			this.playSound(event, 0.35F, 1.0F);
		}
	}

	private void sound_voice_juery(Item item) {
		if (item != Items.AIR) {
			SoundEvent event = Sound_WanderingPeople.getVoiceFollwJuery(item, this);
			this.playSound(event, 0.35F, 1.0F);
		}
	}

	private void sound_voice_gettresur(Item item) {
		SoundEvent event = Sound_WanderingPeople.getVoiceTresur(item, this);
		this.playSound(event, 0.35F, 1.0F);
	}

	private void sound_voice_getfood(Item item) {
		boolean hasTrash = this.hasTrash();
		boolean isHunger = this.getHunger() < (this.getMaxHunger() / 2.0F);
		SoundEvent event = Sound_WanderingPeople.getVoiceFoods(item, hasTrash, isHunger, this);
		this.playSound(event, 0.35F, 1.0F);
	}

	private void sound_voice_canthave() {
		SoundEvent event = Sound_WanderingPeople.getVoiceCantHave();
		this.playSound(event, 0.35F, 1.0F);
	}


	private void sound_voice_cake() {
		SoundEvent event = Sound_WanderingPeople.getCake();
		this.playSound(event, 0.35F, 1.0F);
	}


	private void sound_voice_affrade() {
		SoundEvent event = Sound_WanderingPeople.getAffrade();
		this.playSound(event, 0.35F, 1.0F);
	}



	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack item = player.getHeldItem(hand).copy();
		if (ITEM_TRESUR.test(item)) {
			// i宝物アイテム
			if (!this.world.isRemote){
				// iプレイヤーから根こそぎ奪い取る
				player.setHeldItem(hand, ItemStack.EMPTY);
				// iお宝をトレジャーポケットに入れる
				ItemStack over = addPocketsTresur(item);
				if (over.getCount() != item.getCount()) {
					// iゴミをプレゼント
					dropTrashBox();
				}
				if (!over.isEmpty()) {
					ModUtil.spawnItemStack(this.world, this.posX, this.posY, this.posZ, over, Mod_Elm.rnd);
				}
				if (this.tresurGetter.get(item.getItem()).getAsInt() < Integer.MAX_VALUE) {
					// iお礼ボイス
					sound_voice_gettresur(item.getItem());
				} else {
					// iもう持てないボイス
					sound_voice_canthave();
				}
				this.setTrashCount(this.getTrashCount()-1);
				this.idleTime = 0;
			}
		} else if (item.isFood()) {
			// i食べ物アイテム
			if (!this.world.isRemote) {
				// iプレイヤーから根こそぎ奪い取る
				player.setHeldItem(hand, ItemStack.EMPTY);
				// i食べ物を食べ物インベントリに入れる
				ItemStack over = addPocketsFoods(item);
				if (over.isEmpty()) {
					// iゴミをプレゼント
					dropTrashBox();
				} else {
					ModUtil.spawnItemStack(this.world, this.posX, this.posY, this.posZ, over, Mod_Elm.rnd);
				}
				if (Integer.MAX_VALUE - this.getFoodCount() > item.getItem().getFood().getHealing()) {
					// iお礼ボイス
					sound_voice_getfood(item.getItem());
				} else {
					// iもう持てないボイス
					sound_voice_canthave();
				}
				this.setTrashCount(this.getTrashCount()-1);
				this.idleTime = 0;
			}
		}
		return super.processInteract(player, hand);
	}

	public ItemStack addPocketsTresur(ItemStack item) {
		ItemStack ret = item.copy();
		int cnt = this.tresurGetter.get(item.getItem()).getAsInt();
		int add = Math.min(Integer.MAX_VALUE - cnt, ret.getCount());
		this.tresurSetter.get(item.getItem()).accept(cnt + add);
		ret.shrink(add);
		return ret;
	}

	public ItemStack addPocketsFoods(ItemStack item) {
		int hunger = this.getHunger();
		// iおなかがすいてるか、体力が半分くらいならすぐ食べる
		if (hunger < this.getMaxHunger()/4.0F || this.getHealth() < (this.getMaxHealth()/2.0F)) {
			this.increaseHunger(item.getItem().getFood().getHealing());
			return ItemStack.EMPTY;
		} else {
			// i非常食に取っておく
			int cnt = this.getFoodCount();
			if (Integer.MAX_VALUE - item.getItem().getFood().getHealing() < cnt) {
				// i食べ物袋がいっぱいなのですぐ食べる
				if (hunger < this.getMaxHealth() || this.getHealth() < this.getMaxHealth()) {
					this.increaseHunger(item.getItem().getFood().getHealing());
					return ItemStack.EMPTY;
				}else {
					// iおなかも体力もいっぱいなので受け取らない
					return item.copy();
				}
			} else {
				// i非常食袋に隠す
				this.setFoodCount(cnt+item.getItem().getFood().getHealing());
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean dropTrashBox() {
		if (hasTrash()) {
			ItemStack item;
			if (MyModConfig.Setting_WanderingPeople.getAdditionalItem().size() > 1 && ModUtil.random(10000) < 5) {
				item = new ItemStack(MyModConfig.Setting_WanderingPeople.getAdditionalItem().get(ModUtil.random(MyModConfig.Setting_WanderingPeople.getAdditionalItem().size())),1);
			} else {
				item = new ItemStack(PREZENTS.get(ModUtil.random(PREZENTS.size())),1);
			}
			if (ModUtil.randomF() < 0.25F ) {
				if (item.isDamageable()) {
					item.setDamage(ModUtil.random(item.getMaxDamage()));
				}
			}
			// iエンチャ付きカスタマイズ
			if (item.isEnchantable()) {
				if (ModUtil.randomF() < 0.25F) {
					EnchantmentHelper.addRandomEnchantment(Mod_Elm.rnd, item, (int)(5.0F + ModUtil.random((int)this.world.getDayTime()) * (float)ModUtil.random(18)), false);
				}
			}
			ModUtil.spawnItemStack(this.world, this.posX, this.posY, this.posZ, item, Mod_Elm.rnd);
			return true;
		}
		return false;
	}

	public boolean hasTrash() {
		return this.getTrashCount() > 0;
	}

	@Override
	public double getYOffset() {
		return 0.14D;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 1.3F;
	}

	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return candespawn;
	}

	@Override
	protected int getExperiencePoints(PlayerEntity player) {
		return 0;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static boolean spawan_place(EntityType<? extends Entity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		return world.getLightSubtracted(pos, 0) > 8;
	}

	public final int getMaxHunger() {
		return MAX_HUNGER;
	}

	public void degreesHunger() {
		this.degreesHunger(1);
	}

	public void degreesHunger(int value) {
		setHunger(getHunger() - value);
	}

	public void increaseHunger(int value){
		// i体力も微妙に回復
		this.heal(Math.max(value/10,0.5F));
		setHunger(getHunger() + value);
	}

	public void addHunger(int value) {
		setHunger(getHunger() + value);
	}

	public int getHunger() {
		return this.dataManager.get(HUNGER);
	}

	public void setHunger(int value) {
		this.dataManager.set(HUNGER,  MathHelper.clamp(value, 0, this.getMaxHunger()));
	}

	public void setHome(BlockPos position) {
		this.dataManager.set(HOME_POS, position);
	}

	private BlockPos getHome() {
		return this.dataManager.get(HOME_POS);
	}

	public int getModelType() {
		return this.dataManager.get(MODEL_TYPE1);
	}

	public void setModelType(int value) {
		this.dataManager.set(MODEL_TYPE1, value);
	}

	public int getModelType2() {
		return this.dataManager.get(MODEL_TYPE2);
	}

	public void setModelType2(int value) {
		this.dataManager.set(MODEL_TYPE2, value);
	}

	public boolean isIntarest() {
		return this.dataManager.get(IS_INTAREST);
	}

	public void setIntarest(boolean value) {
		this.dataManager.set(IS_INTAREST,value);
	}

	public int getTrashCount() {
		return this.dataManager.get(POCKET_TRASH_COUNT);
	}
	public void setTrashCount(int cnt) {
		this.dataManager.set(POCKET_TRASH_COUNT, cnt<0?0:cnt);
	}

	public int getEmeCount() {
		return this.dataManager.get(POCKET_EMELALD_COUNT);
	}
	public void setEmeCount(int cnt) {
		this.dataManager.set(POCKET_EMELALD_COUNT, cnt);
	}

	public int getDiaCount() {
		return this.dataManager.get(POCKET_DIAMOND_COUNT);
	}
	public void setDiaCount(int cnt) {
		this.dataManager.set(POCKET_DIAMOND_COUNT, cnt);
	}

	public int getGoldIngCount() {
		return this.dataManager.get(POCKET_GOLDINGOT_COUNT);
	}
	public void setGoldIngCount(int cnt) {
		this.dataManager.set(POCKET_GOLDINGOT_COUNT, cnt);
	}

	public int getGoldNuggetCount() {
		return this.dataManager.get(POCKET_GOLDNUGGET_COUNT);
	}
	public void setGoldNuggetCount(int cnt) {
		this.dataManager.set(POCKET_GOLDNUGGET_COUNT, cnt);
	}

	public int getLapisCount() {
		return this.dataManager.get(POCKET_LAPISLAZRI_COUNT);
	}
	public void setLapisCount(int cnt) {
		this.dataManager.set(POCKET_LAPISLAZRI_COUNT, cnt);
	}

	public int getFoodCount() {
		return this.dataManager.get(POCKET_FOOD_COUNT);
	}
	public void setFoodCount(int cnt) {
		this.dataManager.set(POCKET_FOOD_COUNT, cnt);
	}

	public ResourceLocation Texture(IModelElmTexture entityModel) {
		return entityModel.getTexture(this);
	}

	@Override
	public String getInformation() {
		String br = System.getProperty("line.separator");
		String ret = "Health         :" + this.getHealth() + "/" + this.getMaxHealth() + br +
		             "Hunger         :" + this.getHunger() + "/" + this.getMaxHunger() + br +
		             "TrashCount     :" + this.getTrashCount() + br +
		             "DiamondCount   :" + this.getDiaCount() + br +
		             "EmelardCount   :" + this.getEmeCount() + br +
		             "GoldIngotCount :" + this.getGoldIngCount() + br +
		             "GoldNuggetCount:" + this.getGoldNuggetCount() + br +
		             "LapisCount     :" + this.getLapisCount() + br +
		             "FoodCount      :" +  this.getFoodCount() + br +
		             "ModelType      :" + this.getModelType() + "/" + this.getModelType2() + br +
		             "Position       :" + this.posX + ", " + this.posY + ", " + this.posZ + br +
		             "HomePosition   :" + this.getHome().getX() + ", " + this.getHome().getY() + ", " + this.getHome().getZ() + br +
		             "Distance       :" + Math.sqrt(this.getDistanceSq(this.getHome().getX(),this.getHome().getY(),this.getHome().getZ())) + br;
		return ret;
	}

	public static final Ingredient ITEM_TRESUR = Ingredient.fromItems(Items.EMERALD, Items.DIAMOND, Items.GOLD_NUGGET, Items.GOLD_INGOT, Items.LAPIS_LAZULI);


	public static final List<Item> PREZENTS = new ArrayList<Item>() {
		{add(Items.STICK);}
		{add(Items.ACACIA_LOG);}
		{add(Items.ACACIA_SAPLING);}
		{add(Items.ALLIUM);}
		{add(Items.ANDESITE);}
		{add(Items.APPLE);}
		{add(Items.ARROW);}
		{add(Items.AZURE_BLUET);}
		{add(Items.BAMBOO);}
		{add(Items.BEETROOT_SEEDS);}
		{add(Items.BIRCH_LOG);}
		{add(Items.BIRCH_SAPLING);}
		{add(Items.BLACK_CARPET);}
		{add(Items.BLACK_CONCRETE);}
		{add(Items.BLACK_CONCRETE_POWDER);}
		{add(Items.BLACK_GLAZED_TERRACOTTA);}
		{add(Items.BLACK_STAINED_GLASS);}
		{add(Items.BLACK_STAINED_GLASS_PANE);}
		{add(Items.BLACK_TERRACOTTA);}
		{add(Items.BLACK_WOOL);}
		{add(Items.BLAZE_POWDER);}
		{add(Items.BLAZE_ROD);}
		{add(Items.BLUE_CARPET);}
		{add(Items.BLUE_CONCRETE);}
		{add(Items.BLUE_CONCRETE_POWDER);}
		{add(Items.BLUE_GLAZED_TERRACOTTA);}
		{add(Items.BLUE_ORCHID);}
		{add(Items.BLUE_STAINED_GLASS);}
		{add(Items.BLUE_STAINED_GLASS_PANE);}
		{add(Items.BLUE_TERRACOTTA);}
		{add(Items.BLUE_WOOL);}
		{add(Items.BONE);}
		{add(Items.BOW);}
		{add(Items.BOWL);}
		{add(Items.BROWN_CARPET);}
		{add(Items.BROWN_CONCRETE);}
		{add(Items.BROWN_CONCRETE_POWDER);}
		{add(Items.BROWN_GLAZED_TERRACOTTA);}
		{add(Items.BROWN_MUSHROOM);}
		{add(Items.BROWN_STAINED_GLASS);}
		{add(Items.BROWN_STAINED_GLASS_PANE);}
		{add(Items.BROWN_TERRACOTTA);}
		{add(Items.BROWN_WOOL);}
		{add(Items.BUCKET);}
		{add(Items.CACTUS);}
		{add(Items.CARTOGRAPHY_TABLE);}
		{add(Items.CHAINMAIL_BOOTS);}
		{add(Items.CHAINMAIL_CHESTPLATE);}
		{add(Items.CHAINMAIL_HELMET);}
		{add(Items.CHAINMAIL_LEGGINGS);}
		{add(Items.CHEST_MINECART);}
		{add(Items.CLAY);}
		{add(Items.CLAY_BALL);}
		{add(Items.COCOA_BEANS);}
		{add(Items.CORNFLOWER);}
		{add(Items.CROSSBOW);}
		{add(Items.CYAN_CARPET);}
		{add(Items.CYAN_CONCRETE);}
		{add(Items.CYAN_CONCRETE_POWDER);}
		{add(Items.CYAN_GLAZED_TERRACOTTA);}
		{add(Items.CYAN_STAINED_GLASS);}
		{add(Items.CYAN_STAINED_GLASS_PANE);}
		{add(Items.CYAN_TERRACOTTA);}
		{add(Items.CYAN_WOOL);}
		{add(Items.DANDELION);}
		{add(Items.DARK_OAK_LOG);}
		{add(Items.DARK_OAK_SAPLING);}
		{add(Items.DARK_PRISMARINE);}
		{add(Items.DIAMOND_AXE);}
		{add(Items.DIAMOND_BOOTS);}
		{add(Items.DIAMOND_CHESTPLATE);}
		{add(Items.DIAMOND_HELMET);}
		{add(Items.DIAMOND_HOE);}
		{add(Items.DIAMOND_HORSE_ARMOR);}
		{add(Items.DIAMOND_LEGGINGS);}
		{add(Items.DIAMOND_PICKAXE);}
		{add(Items.DIAMOND_SHOVEL);}
		{add(Items.DIAMOND_SWORD);}
		{add(Items.DIORITE);}
		{add(Items.DIRT);}
		{add(Items.EGG);}
		{add(Items.END_ROD);}
		{add(Items.ENDER_PEARL);}
		{add(Items.FEATHER);}
		{add(Items.FERMENTED_SPIDER_EYE);}
		{add(Items.FLETCHING_TABLE);}
		{add(Items.FLINT);}
		{add(Items.FLINT_AND_STEEL);}
		{add(Items.FLOWER_POT);}
		{add(Items.GHAST_TEAR);}
		{add(Items.GLASS_BOTTLE);}
		{add(Items.GOLDEN_AXE);}
		{add(Items.GOLDEN_BOOTS);}
		{add(Items.GOLDEN_CHESTPLATE);}
		{add(Items.GOLDEN_HELMET);}
		{add(Items.GOLDEN_HOE);}
		{add(Items.GOLDEN_HORSE_ARMOR);}
		{add(Items.GOLDEN_LEGGINGS);}
		{add(Items.GOLDEN_PICKAXE);}
		{add(Items.GOLDEN_SHOVEL);}
		{add(Items.GOLDEN_SWORD);}
		{add(Items.GRANITE);}
		{add(Items.GRAVEL);}
		{add(Items.GRAY_CARPET);}
		{add(Items.GRAY_CONCRETE);}
		{add(Items.GRAY_CONCRETE_POWDER);}
		{add(Items.GRAY_GLAZED_TERRACOTTA);}
		{add(Items.GRAY_STAINED_GLASS);}
		{add(Items.GRAY_STAINED_GLASS_PANE);}
		{add(Items.GRAY_TERRACOTTA);}
		{add(Items.GRAY_WOOL);}
		{add(Items.GREEN_CARPET);}
		{add(Items.GREEN_CONCRETE);}
		{add(Items.GREEN_CONCRETE_POWDER);}
		{add(Items.GREEN_GLAZED_TERRACOTTA);}
		{add(Items.GREEN_STAINED_GLASS);}
		{add(Items.GREEN_STAINED_GLASS_PANE);}
		{add(Items.GREEN_TERRACOTTA);}
		{add(Items.GREEN_WOOL);}
		{add(Items.GRINDSTONE);}
		{add(Items.GUNPOWDER);}
		{add(Items.HAY_BLOCK);}
		{add(Items.ICE);}
		{add(Items.INK_SAC);}
		{add(Items.IRON_AXE);}
		{add(Items.IRON_BOOTS);}
		{add(Items.IRON_CHESTPLATE);}
		{add(Items.IRON_HELMET);}
		{add(Items.IRON_HOE);}
		{add(Items.IRON_HORSE_ARMOR);}
		{add(Items.IRON_LEGGINGS);}
		{add(Items.IRON_PICKAXE);}
		{add(Items.IRON_SHOVEL);}
		{add(Items.IRON_SWORD);}
		{add(Items.JUNGLE_LOG);}
		{add(Items.JUNGLE_SAPLING);}
		{add(Items.KELP);}
		{add(Items.LADDER);}
		{add(Items.LEAD);}
		{add(Items.LEATHER);}
		{add(Items.LEATHER_BOOTS);}
		{add(Items.LEATHER_CHESTPLATE);}
		{add(Items.LEATHER_HELMET);}
		{add(Items.LEATHER_HORSE_ARMOR);}
		{add(Items.LEATHER_LEGGINGS);}
		{add(Items.LIGHT_BLUE_CARPET);}
		{add(Items.LIGHT_BLUE_CONCRETE);}
		{add(Items.LIGHT_BLUE_CONCRETE_POWDER);}
		{add(Items.LIGHT_BLUE_GLAZED_TERRACOTTA);}
		{add(Items.LIGHT_BLUE_STAINED_GLASS);}
		{add(Items.LIGHT_BLUE_STAINED_GLASS_PANE);}
		{add(Items.LIGHT_BLUE_TERRACOTTA);}
		{add(Items.LIGHT_BLUE_WOOL);}
		{add(Items.LIGHT_GRAY_CARPET);}
		{add(Items.LIGHT_GRAY_CONCRETE);}
		{add(Items.LIGHT_GRAY_CONCRETE_POWDER);}
		{add(Items.LIGHT_GRAY_GLAZED_TERRACOTTA);}
		{add(Items.LIGHT_GRAY_STAINED_GLASS);}
		{add(Items.LIGHT_GRAY_STAINED_GLASS_PANE);}
		{add(Items.LIGHT_GRAY_TERRACOTTA);}
		{add(Items.LIGHT_GRAY_WOOL);}
		{add(Items.LILAC);}
		{add(Items.LILY_OF_THE_VALLEY);}
		{add(Items.LIME_CARPET);}
		{add(Items.LIME_CONCRETE);}
		{add(Items.LIME_CONCRETE_POWDER);}
		{add(Items.LIME_GLAZED_TERRACOTTA);}
		{add(Items.LIME_STAINED_GLASS);}
		{add(Items.LIME_STAINED_GLASS_PANE);}
		{add(Items.LIME_TERRACOTTA);}
		{add(Items.LIME_WOOL);}
		{add(Items.MAGENTA_CARPET);}
		{add(Items.MAGENTA_CONCRETE);}
		{add(Items.MAGENTA_CONCRETE_POWDER);}
		{add(Items.MAGENTA_GLAZED_TERRACOTTA);}
		{add(Items.MAGENTA_STAINED_GLASS);}
		{add(Items.MAGENTA_STAINED_GLASS_PANE);}
		{add(Items.MAGENTA_TERRACOTTA);}
		{add(Items.MAGENTA_WOOL);}
		{add(Items.MAGMA_BLOCK);}
		{add(Items.MAGMA_CREAM);}
		{add(Items.MELON);}
		{add(Items.MELON_SEEDS);}
		{add(Items.MUSIC_DISC_11);}
		{add(Items.MUSIC_DISC_13);}
		{add(Items.MUSIC_DISC_BLOCKS);}
		{add(Items.MUSIC_DISC_CAT);}
		{add(Items.MUSIC_DISC_CHIRP);}
		{add(Items.MUSIC_DISC_FAR);}
		{add(Items.MUSIC_DISC_MALL);}
		{add(Items.MUSIC_DISC_MELLOHI);}
		{add(Items.MUSIC_DISC_STAL);}
		{add(Items.MUSIC_DISC_STRAD);}
		{add(Items.MUSIC_DISC_WAIT);}
		{add(Items.MUSIC_DISC_WARD);}
		{add(Items.NAME_TAG);}
		{add(Items.NETHER_WART);}
		{add(Items.OAK_LOG);}
		{add(Items.OAK_SAPLING);}
		{add(Items.OBSIDIAN);}
		{add(Items.ORANGE_CARPET);}
		{add(Items.ORANGE_CONCRETE);}
		{add(Items.ORANGE_CONCRETE_POWDER);}
		{add(Items.ORANGE_GLAZED_TERRACOTTA);}
		{add(Items.ORANGE_STAINED_GLASS);}
		{add(Items.ORANGE_STAINED_GLASS_PANE);}
		{add(Items.ORANGE_TERRACOTTA);}
		{add(Items.ORANGE_TULIP);}
		{add(Items.ORANGE_WOOL);}
		{add(Items.OXEYE_DAISY);}
		{add(Items.PAINTING);}
		{add(Items.PAPER);}
		{add(Items.PEONY);}
		{add(Items.PINK_CARPET);}
		{add(Items.PINK_CONCRETE);}
		{add(Items.PINK_CONCRETE_POWDER);}
		{add(Items.PINK_GLAZED_TERRACOTTA);}
		{add(Items.PINK_STAINED_GLASS);}
		{add(Items.PINK_STAINED_GLASS_PANE);}
		{add(Items.PINK_TERRACOTTA);}
		{add(Items.PINK_TULIP);}
		{add(Items.PINK_WOOL);}
		{add(Items.POLISHED_ANDESITE);}
		{add(Items.POLISHED_DIORITE);}
		{add(Items.POLISHED_GRANITE);}
		{add(Items.POPPY);}
		{add(Items.PRISMARINE);}
		{add(Items.PUMPKIN);}
		{add(Items.PUMPKIN_SEEDS);}
		{add(Items.PURPLE_CARPET);}
		{add(Items.PURPLE_CONCRETE);}
		{add(Items.PURPLE_CONCRETE_POWDER);}
		{add(Items.PURPLE_GLAZED_TERRACOTTA);}
		{add(Items.PURPLE_STAINED_GLASS);}
		{add(Items.PURPLE_STAINED_GLASS_PANE);}
		{add(Items.PURPLE_TERRACOTTA);}
		{add(Items.PURPLE_WOOL);}
		{add(Items.RABBIT_FOOT);}
		{add(Items.RABBIT_HIDE);}
		{add(Items.RAIL);}
		{add(Items.RED_CARPET);}
		{add(Items.RED_CONCRETE);}
		{add(Items.RED_CONCRETE_POWDER);}
		{add(Items.RED_GLAZED_TERRACOTTA);}
		{add(Items.RED_MUSHROOM);}
		{add(Items.RED_SAND);}
		{add(Items.RED_SANDSTONE);}
		{add(Items.RED_STAINED_GLASS);}
		{add(Items.RED_STAINED_GLASS_PANE);}
		{add(Items.RED_TERRACOTTA);}
		{add(Items.RED_TULIP);}
		{add(Items.RED_WOOL);}
		{add(Items.REDSTONE_TORCH);}
		{add(Items.ROSE_BUSH);}
		{add(Items.SADDLE);}
		{add(Items.SAND);}
		{add(Items.SEA_PICKLE);}
		{add(Items.SHEARS);}
		{add(Items.SLIME_BALL);}
		{add(Items.SMITHING_TABLE);}
		{add(Items.SNOWBALL);}
		{add(Items.SPRUCE_LOG);}
		{add(Items.SPRUCE_SAPLING);}
		{add(Items.STICK);}
		{add(Items.STONE);}
		{add(Items.STONE_AXE);}
		{add(Items.STONE_BUTTON);}
		{add(Items.STONE_HOE);}
		{add(Items.STONE_PICKAXE);}
		{add(Items.STONE_SHOVEL);}
		{add(Items.STONE_SWORD);}
		{add(Items.STONECUTTER);}
		{add(Items.STRING);}
		{add(Items.SUGAR);}
		{add(Items.SUGAR_CANE);}
		{add(Items.SUNFLOWER);}
		{add(Items.TNT);}
		{add(Items.TORCH);}
		{add(Items.TRIPWIRE_HOOK);}
		{add(Items.TURTLE_HELMET);}
		{add(Items.VINE);}
		{add(Items.WHEAT_SEEDS);}
		{add(Items.WHITE_CARPET);}
		{add(Items.WHITE_CONCRETE);}
		{add(Items.WHITE_CONCRETE_POWDER);}
		{add(Items.WHITE_GLAZED_TERRACOTTA);}
		{add(Items.WHITE_STAINED_GLASS);}
		{add(Items.WHITE_STAINED_GLASS_PANE);}
		{add(Items.WHITE_TERRACOTTA);}
		{add(Items.WHITE_TULIP);}
		{add(Items.WHITE_WOOL);}
		{add(Items.WITHER_ROSE);}
		{add(Items.WOODEN_AXE);}
		{add(Items.WOODEN_HOE);}
		{add(Items.WOODEN_PICKAXE);}
		{add(Items.WOODEN_SHOVEL);}
		{add(Items.WOODEN_SWORD);}
		{add(Items.YELLOW_CARPET);}
		{add(Items.YELLOW_CONCRETE);}
		{add(Items.YELLOW_CONCRETE_POWDER);}
		{add(Items.YELLOW_GLAZED_TERRACOTTA);}
		{add(Items.YELLOW_STAINED_GLASS);}
		{add(Items.YELLOW_STAINED_GLASS_PANE);}
		{add(Items.YELLOW_TERRACOTTA);}
		{add(Items.YELLOW_WOOL);}
	};

	@Override
	public float getHealthOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getFoodOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getBloodOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getSpilitOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getPhisicalAtkOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getMasicalAtkOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getPhisicalDefOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getMasicalDefOffset() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
}