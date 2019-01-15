package mod.elm.entity;

import net.minecraft.entity.Entity;

public class EntityRegisterInfo {

	private final String registerName;
	private final int entityId;
	private final Class<? extends Entity> entityClass;

	private float helth;
	private float attackSpeed;
	private float stamina;
	private float stomack;

	public EntityRegisterInfo(String name, int id, Class<? extends Entity> eclass, float helthIn, float attackIn, float staminaIn, float stomackIn ){
		registerName = name;
		entityId = id;
		entityClass = eclass;
		helth =helthIn;
		attackSpeed = attackIn;
		stamina = staminaIn;
		stomack = stomackIn;

	}

	public String Name(){return registerName;}
	public int ID(){return entityId;}
	public Class<? extends Entity> entityClass(){return entityClass;}
	public float maxHelth(){return this.helth;}
	public float attackSpeed(){return this.attackSpeed;}
	public float maxStamina(){return this.stamina;}
	public float maxStmack(){return this.stomack;}
}
