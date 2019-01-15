package mod.elm.entity;

import mod.elm.core.ModCommon;
import mod.elm.core.Mod_ElonaMobs;
import mod.elm.entity.enemy.EntityWandarer;
import mod.elm.entity.mob.EntityElderSister;
import mod.elm.entity.mob.EntityImoneko;
import mod.elm.entity.mob.EntityImouto;
import mod.elm.entity.mob.EntityImoutoWanko;
import mod.elm.entity.mob.EntityLittleGirl;
import mod.elm.entity.mob.EntityLittleSister;
import mod.elm.entity.mob.EntityYoujyo;
import mod.elm.entity.passive.EntityImoutoGolem;
import mod.elm.entity.passive.EntityImoutoSnowman;
import mod.elm.inventory.InventoryElmNormal;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityCore {

	public static final String NAME_ENTIYT_YOUJO = "youjyo";
	public static final String NAME_ENTITY_LITTLEGIRL = "littlegirl";
	public static final String NAME_ENTITY_LITTLESISTER = "littlesister";
	public static final String NAME_ENTITY_IMONEKO = "imoneko";
	public static final String NAME_ENTITY_ELDERSISTER = "eldersister";
	public static final String NAME_ENTITY_IMOUTO = "imouto";
	public static final String NAME_ENTITY_IMOUTOWANDARER = "wander";
	public static final String NAME_ENTITY_IMOUTOGOLEM = "imoutogolem";
	public static final String NAME_ENTITY_IMOUTOSNOWMAN = "imoutosnowman";
	public static final String NAME_ENTITY_IMOUTOWANKO = "imoutowanko";

	public static final EntityRegisterInfo ENTITY_YOUJYO = new EntityRegisterInfo(NAME_ENTIYT_YOUJO,0, EntityYoujyo.class, 10.0F, 0.0F, 10.0F, 5.0F);
	public static final EntityRegisterInfo ENTITY_LITTLEGIRL = new EntityRegisterInfo(NAME_ENTITY_LITTLEGIRL,1, EntityLittleGirl.class, 15.0F, 3.0F, 15.0F, 10.0F);
	public static final EntityRegisterInfo ENTITY_LITTLESISTER = new EntityRegisterInfo(NAME_ENTITY_LITTLESISTER,2,EntityLittleSister.class, 30.0F, 1.0F, 50.0F, 10.0F);
	public static final EntityRegisterInfo ENTITY_IMONEKO = new EntityRegisterInfo(NAME_ENTITY_IMONEKO,3,EntityImoneko.class, 15.0F, 2.0F, 20.0F, 10.0F);
	public static final EntityRegisterInfo ENTITY_ELDERSISTER = new EntityRegisterInfo(NAME_ENTITY_ELDERSISTER,4,EntityElderSister.class,20.0F,3.0F,20.0F,20.0F);
	public static final EntityRegisterInfo ENTITY_IMOUTO = new EntityRegisterInfo(NAME_ENTITY_IMOUTO,900,EntityImouto.class,15.0F,2.0F,15.0F,10.0F);
	public static final EntityRegisterInfo ENTITY_IMOUTOWANDARER = new EntityRegisterInfo(NAME_ENTITY_IMOUTOWANDARER,901,EntityWandarer.class,15.0F,2.0F,15.0F,10.0F);
	public static final EntityRegisterInfo ENTITY_IMOUTOGOLEM = new EntityRegisterInfo(NAME_ENTITY_IMOUTOGOLEM,902, EntityImoutoGolem.class,60.0F,1.0F,100.0F,0.0F);
	public static final EntityRegisterInfo ENTITY_IMOUTOSNOWMAN = new EntityRegisterInfo(NAME_ENTITY_IMOUTOSNOWMAN,903, EntityImoutoSnowman.class,10.0F,1.0F,100.0F,0.0F);
	public static final EntityRegisterInfo ENTITY_IMOUTOWANKO = new EntityRegisterInfo(NAME_ENTITY_IMOUTOWANKO,904, EntityImoutoWanko.class,20.0F,2.0F,20.0F,10.0F);

	public static void register() {
		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_YOUJYO.Name()),  ENTITY_YOUJYO.entityClass(),  ENTITY_YOUJYO.Name(), ENTITY_YOUJYO.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_LITTLEGIRL.Name()),  ENTITY_LITTLEGIRL.entityClass(),  ENTITY_LITTLEGIRL.Name(), ENTITY_LITTLEGIRL.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_LITTLESISTER.Name()),  ENTITY_LITTLESISTER.entityClass(),  ENTITY_LITTLESISTER.Name(), ENTITY_LITTLESISTER.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_IMONEKO.Name()),  ENTITY_IMONEKO.entityClass(),  ENTITY_IMONEKO.Name(), ENTITY_IMONEKO.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_ELDERSISTER.Name()),  ENTITY_ELDERSISTER.entityClass(),  ENTITY_ELDERSISTER.Name(), ENTITY_ELDERSISTER.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_IMOUTO.Name()),  ENTITY_IMOUTO.entityClass(),  ENTITY_IMOUTO.Name(), ENTITY_IMOUTO.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_IMOUTOWANDARER.Name()),  ENTITY_IMOUTOWANDARER.entityClass(),  ENTITY_IMOUTOWANDARER.Name(), ENTITY_IMOUTOWANDARER.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_IMOUTOGOLEM.Name()),  ENTITY_IMOUTOGOLEM.entityClass(),  ENTITY_IMOUTOGOLEM.Name(), ENTITY_IMOUTOGOLEM.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_IMOUTOSNOWMAN.Name()),  ENTITY_IMOUTOSNOWMAN.entityClass(),  ENTITY_IMOUTOSNOWMAN.Name(), ENTITY_IMOUTOSNOWMAN.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID,
				ENTITY_IMOUTOWANKO.Name()),  ENTITY_IMOUTOWANKO.entityClass(),  ENTITY_IMOUTOWANKO.Name(), ENTITY_IMOUTOWANKO.ID(),
				Mod_ElonaMobs.instance, 80, 5, true);
	}



	public static IInventory getInventorySlot(String name){
		IInventory ret = null;
		switch(name){
		case NAME_ENTIYT_YOUJO:
			ret = new InventoryElmNormal(9);
			break;
		case NAME_ENTITY_LITTLEGIRL:
			ret = new InventoryElmNormal(18);
			break;
		case NAME_ENTITY_LITTLESISTER:
			ret = new InventoryElmNormal(18);
			break;
		case NAME_ENTITY_IMONEKO:
			ret = new InventoryElmNormal(9);
			break;
		case NAME_ENTITY_ELDERSISTER:
			ret = new InventoryElmNormal(27);
			break;
		case NAME_ENTITY_IMOUTO:
			ret = new InventoryElmNormal(27);
			break;
		case NAME_ENTITY_IMOUTOWANDARER:
			ret = new InventoryElmNormal(2);
			break;
		case NAME_ENTITY_IMOUTOGOLEM:
			ret = new InventoryElmNormal(36);
			break;
		case NAME_ENTITY_IMOUTOSNOWMAN:
			ret = new InventoryElmNormal(2);
			break;
		case NAME_ENTITY_IMOUTOWANKO:
			ret = new InventoryElmNormal(9);
			break;
		}
		return ret;
	}

	public IInventory getEquipInventorySlot(){
		return null;
	}


}
