package mod.elm.entity;

public class EntityRegisterInfo {

	private final String registerName;
	private final int entityId;

	public EntityRegisterInfo(String name, int id){
		registerName = name;
		entityId = id;
	}

	public String Name(){return registerName;}
	public int ID(){return entityId;}
}
