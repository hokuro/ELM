package mod.elm.entity.elmgolem;

import java.util.Optional;
import java.util.UUID;

import mod.elm.entity.ab.IGolemParameter;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;

public class DataManagerElmGolem {
	private static final int MAXINT = 200000000;
	public static final net.minecraft.entity.ai.attributes.IAttribute ATTACK_DAMAGE_MGK = new net.minecraft.entity.ai.attributes.RangedAttribute(null, "elmgolem.atk_magic", 2.0D, 0.0D, 1024.0D).setShouldWatch(true);
	public static final net.minecraft.entity.ai.attributes.IAttribute ARMOR_MGK = new net.minecraft.entity.ai.attributes.RangedAttribute(null, "elmgolem.def_magic", 0.0D, 0.0D, 30.0D).setShouldWatch(true);
	public static final net.minecraft.entity.ai.attributes.IAttribute ATTACK_SPAN = new net.minecraft.entity.ai.attributes.RangedAttribute(null, "elm.atk_span", 2.0D, 0.0D, 30.0D).setShouldWatch(true);

	public static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityElmGolem.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	public static final DataParameter<Integer> FOOD_LEVEL = EntityDataManager.createKey(EntityElmGolem.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> BLOOD_LEVEL = EntityDataManager.createKey(EntityElmGolem.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> SPILIT_LEVEL = EntityDataManager.createKey(EntityElmGolem.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> EXP_LEVEL = EntityDataManager.createKey(EntityElmGolem.class, DataSerializers.VARINT);

	public static final DataParameter<Boolean> IS_SHIT = EntityDataManager.createKey(EntityElmGolem.class, DataSerializers.BOOLEAN);

	public EntityDataManager dataManager;

	private float phisicalAttack;
	private float masicalAttack;
	private float phisicalDefence;
	private float masicalDefence;
	private int workSpanTime;

	private float maxHelathLevel;
	private int maxFoodLevel;
	private int maxBloodLevel;
	private int maxSpilitLevel;
	private int maxExpLevel;

	public DataManagerElmGolem(EntityDataManager manager) {
		this.dataManager = manager;
		maxHelathLevel = 20;
		maxFoodLevel = 10;
		maxBloodLevel = 600;
		maxSpilitLevel = 60;
		maxExpLevel = Integer.MAX_VALUE/10;

		phisicalAttack = 2;
		masicalAttack = 2;
		phisicalDefence = 0;
		masicalDefence = 0;
		workSpanTime = 100;
	}


	public void additionalParamater(IGolemParameter additional) {
		maxHelathLevel += additional.getHealthOffset();
		maxFoodLevel += additional.getFoodOffset();
		maxBloodLevel += additional.getBloodOffset();
		maxSpilitLevel += additional.getSpilitOffset();

		phisicalAttack += additional.getPhisicalAtkOffset();
		masicalAttack += additional.getMasicalAtkOffset();
		phisicalDefence += additional.getPhisicalDefOffset();
		masicalDefence += additional.getPhisicalDefOffset();
	}

	public void writeAdditional(CompoundNBT nbt) {
		nbt.putFloat("maxhealthlevel", this.maxHelathLevel);
		nbt.putInt("maxfoodlevel", this.maxFoodLevel);
		nbt.putInt("maxbloodlevel", this.maxBloodLevel);
		nbt.putInt("maxspilitlevel", this.maxSpilitLevel);

		nbt.putFloat("phisicalAttack", this.phisicalAttack);
		nbt.putFloat("masicalAttack", this.masicalAttack);
		nbt.putFloat("phisicalDefence", this.phisicalDefence);
		nbt.putFloat("masicalDefence", this.masicalDefence);
		nbt.putInt("workSpanTime", this.workSpanTime);

		nbt.putString("OWNER_UNIQUE_ID", this.getOwnerID().get().toString());
		nbt.putInt("FOOD_LEVEL", this.getFood_Level());
		nbt.putInt("BLOOD_LEVEL", this.getBlood_Level());
		nbt.putInt("SPILIT_LEVEL", this.getSpilit_Level());
		nbt.putInt("EXP_LEVEL", this.getExp_Level());
		nbt.putBoolean("IS_SHIT", this.getIsShit());
	}

	public void readAdditional(CompoundNBT nbt) {
		this.maxHelathLevel = nbt.getFloat("maxhealthlevel");
		this.maxFoodLevel = nbt.getInt("maxfoodlevel");
		this.maxBloodLevel = nbt.getInt("maxbloodlevel");
		this.maxSpilitLevel = nbt.getInt("maxspilitlevel");

		this.phisicalAttack = nbt.getFloat("phisicalAttack");
		this.masicalAttack = nbt.getFloat("masicalAttack");
		this.phisicalDefence = nbt.getFloat("phisicalDefence");
		this.masicalDefence = nbt.getFloat("masicalDefence");
		this.workSpanTime = nbt.getInt("workSpanTime");

		this.setOwnerId(UUID.fromString(nbt.getString("OWNER_UNIQUE_ID")));
		this.setFood_Level(nbt.getInt("FOOD_LEVEL"));
		this.setFood_Level(nbt.getInt("BLOOD_LEVEL"));
		this.setFood_Level(nbt.getInt("SPILIT_LEVEL"));
		this.setFood_Level(nbt.getInt("EXP_LEVEL"));
		this.setIsShit(nbt.getBoolean("IS_SHIT"));
	}

	public void registerAttributes(AbstractAttributeMap attribute) {
		attribute.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35.0D);
		attribute.registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		attribute.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		attribute.registerAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(2.0D);
		attribute.registerAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(2.0D);
		attribute.getAttributeInstance(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		attribute.getAttributeInstance(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2.0D);
		attribute.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2.0D);
		attribute.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.23F);
		attribute.registerAttribute(ATTACK_DAMAGE_MGK).setBaseValue(2.0D);
		attribute.registerAttribute(ARMOR_MGK).setBaseValue(2.0D);
		attribute.registerAttribute(ATTACK_SPAN).setBaseValue(5.0D);
	}

	public void registerData() {
		dataManager.register(OWNER_UNIQUE_ID, null);
		dataManager.register(FOOD_LEVEL, this.maxFoodLevel);
		dataManager.register(BLOOD_LEVEL, this.maxBloodLevel);
		dataManager.register(SPILIT_LEVEL, this.maxSpilitLevel);
		dataManager.register(EXP_LEVEL, 0);
		dataManager.register(IS_SHIT, false);
	}

	// OwnerId
	public void setOwnerId(UUID owner) {
		dataManager.set(OWNER_UNIQUE_ID, Optional.of(owner));
	}

	public Optional<UUID> getOwnerID() {
		return dataManager.get(OWNER_UNIQUE_ID);
	}

	// FoodLevel
	public void shrinkFood_Level(int value) {
		growFood_Level(-value);
	}

	public void growFood_Level(int value) {
		int data = getFood_Level() + value;
		setFood_Level(data);
	}

	public int getFood_Level() {
		return dataManager.get(FOOD_LEVEL);
	}

	public void setFood_Level(int value) {
		dataManager.set(FOOD_LEVEL, MathHelper.clamp(value, 0, this.maxFoodLevel));
	}

	// BloodLevel
	public void shrinkBlood_Level(int value) {
		growBlood_Level(-value);
	}

	public void growBlood_Level(int value) {
		int data = getFood_Level() + value;
		setBlood_Level(data);
	}

	public int getBlood_Level() {
		return dataManager.get(BLOOD_LEVEL);
	}

	public void setBlood_Level(int value) {
		dataManager.set(BLOOD_LEVEL, MathHelper.clamp(value, 0, this.maxBloodLevel));
	}

	// SpilitLevel
	public void shrinkSpilit_Level(int value) {
		growSpilit_Level(-value);
	}

	public void growSpilit_Level(int value) {
		int data = getSpilit_Level() + value;
		setSpilit_Level(data);
	}

	public int getSpilit_Level() {
		return dataManager.get(SPILIT_LEVEL);
	}

	public void setSpilit_Level(int value) {
		dataManager.set(SPILIT_LEVEL, MathHelper.clamp(value, 0, this.maxSpilitLevel));
	}

	// ExpLevel
	public void shrinkExp_Level(int value) {
		growExp_Level(-value);
	}

	public void growExp_Level(int value) {
		int data = getExp_Level() + value;
		setExp_Level(data);
	}

	public int getExp_Level() {
		return dataManager.get(EXP_LEVEL);
	}

	public void setExp_Level(int value) {
		dataManager.set(EXP_LEVEL, MathHelper.clamp(value, 0, this.maxExpLevel));
	}

	// isSith
	public void setIsShit(boolean value) {
		dataManager.set(IS_SHIT, value);
	}

	public boolean getIsShit() {
		return dataManager.get(IS_SHIT);
	}
}
