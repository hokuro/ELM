package mod.elm.item;

import java.util.HashMap;
import java.util.Map;

import mod.elm.core.ModCommon;
import mod.elm.core.Mod_ElonaMobs;
import mod.elm.core.log.ModLog;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemCore {
	// 妹素材
	public static ToolMaterial materialImout;



	public static final String NAME_CANDY = "candy";
	public static final String NAME_CANDY_MILLK = "candy_millk";
	public static final String NAME_DIARY = "diary";
	public static final String NAME_IMOUTO_BONE = "imouto_bone";
	public static final String NAME_IMOUTO_BLOOD = "imouto_blood";
	public static final String NAME_IMOUTO_ARM = "imouto_arm";
	public static final String NAME_IMOUTO_BODY = "imouto_body";
	public static final String NAME_IMOUTO_HART = "imouto_hart";
	public static final String NAME_IMOUTO_LEG = "imouto_leg";
	public static final String NAME_IMOUTO_UTERUS = "imouto_uterus";
	public static final String NAME_IMOUTO_EYE = "imouto_eye";
	public static final String NAME_IMOUTO_BLAIN = "imouto_blain";
	public static final String NAME_IMOUTO_MEET = "imouto_meet";
	public static final String NAME_IMOUTO_SKIN = "imouto_skin";
	public static final String NAME_IMOUTO_HAIR = "imouto_hair";
	public static final String NAME_IMOUTO_COOKEDMEET = "imouto_meetcooked";


	public static final String[] NAME_LIST = new String[]{
//			NAME_CANDY,
//			NAME_CANDY_MILLK,
			NAME_DIARY,
			NAME_IMOUTO_BONE,
			NAME_IMOUTO_BLOOD,
			NAME_IMOUTO_ARM,
			NAME_IMOUTO_BODY,
			NAME_IMOUTO_HART,
			NAME_IMOUTO_LEG,
			NAME_IMOUTO_UTERUS,
			NAME_IMOUTO_EYE,
			NAME_IMOUTO_BLAIN,
			NAME_IMOUTO_MEET,
			NAME_IMOUTO_SKIN,
			NAME_IMOUTO_HAIR,
			NAME_IMOUTO_COOKEDMEET
	};

	private static Map<String,Item> itemMap;
	private static Map<String,ModelResourceLocation[]> resourceMap;

	public static Item item_diary;
	public static Item item_candy;
	public static Item item_milkcandy;
	public static Item item_imoutobone;
	public static Item item_imoutoblood;
	public static Item item_imoutoarm;
	public static Item item_imoutobody;
	public static Item item_imoutohart;
	public static Item item_imoutoleg;
	public static Item item_imoutoutrus;
	public static Item item_imoutoeye;
	public static Item item_imoutoblain;
	public static Item item_imoutomeet;
	public static Item item_imoutoskin;
	public static Item item_imoutohair;
	public static Item item_imoutoear;
	public static Item item_imouto_cookedmeet;


	private static void init(){
		item_diary = new Item()
				.setRegistryName(NAME_DIARY)
				.setUnlocalizedName(NAME_DIARY)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutobone = new ItemImoutoBone()
				.setRegistryName(NAME_IMOUTO_BONE)
				.setUnlocalizedName(NAME_IMOUTO_BONE)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutoblood = new ItemImoutoBlood()
				.setRegistryName(NAME_IMOUTO_BLOOD)
				.setUnlocalizedName(NAME_IMOUTO_BLOOD)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutoarm = new ItemImoutoArms()
				.setRegistryName(NAME_IMOUTO_ARM)
				.setUnlocalizedName(NAME_IMOUTO_ARM)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutobody = new ItemImoutoBody()
				.setRegistryName(NAME_IMOUTO_BODY)
				.setUnlocalizedName(NAME_IMOUTO_BODY)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutohart = new ItemImoutoHart()
				.setRegistryName(NAME_IMOUTO_HART)
				.setUnlocalizedName(NAME_IMOUTO_HART)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutoleg = new ItemImoutoLegs()
				.setRegistryName(NAME_IMOUTO_LEG)
				.setUnlocalizedName(NAME_IMOUTO_LEG)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutoutrus = new ItemImoutoUtrus()
				.setRegistryName(NAME_IMOUTO_UTERUS)
				.setUnlocalizedName(NAME_IMOUTO_UTERUS)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutoeye = new ItemImoutoEyes()
				.setRegistryName(NAME_IMOUTO_EYE)
				.setUnlocalizedName(NAME_IMOUTO_EYE)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutoblain = new ItemImoutoBlain()
				.setRegistryName(NAME_IMOUTO_BLAIN)
				.setUnlocalizedName(NAME_IMOUTO_BLAIN)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutomeet = new ItemImoutoMeet(false)
				.setRegistryName(NAME_IMOUTO_MEET)
				.setUnlocalizedName(NAME_IMOUTO_MEET)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutoskin = new ItemImoutoSkin()
				.setRegistryName(NAME_IMOUTO_SKIN)
				.setUnlocalizedName(NAME_IMOUTO_SKIN)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imoutohair = new ItemImoutoHair()
				.setRegistryName(NAME_IMOUTO_HAIR)
				.setUnlocalizedName(NAME_IMOUTO_HAIR)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		item_imouto_cookedmeet = new ItemImoutoMeet(true)
				.setRegistryName(NAME_IMOUTO_COOKEDMEET)
				.setUnlocalizedName(NAME_IMOUTO_COOKEDMEET)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);




		itemMap = new HashMap<String,Item>(){
			{put(NAME_DIARY,item_diary);}
			{put(NAME_CANDY,item_candy);}
			{put(NAME_CANDY_MILLK,item_milkcandy);}
			{put(NAME_IMOUTO_BONE,item_imoutobone);}
			{put(NAME_IMOUTO_BLOOD,item_imoutoblood);}
			{put(NAME_IMOUTO_ARM,item_imoutoarm);}
			{put(NAME_IMOUTO_BODY,item_imoutobody);}
			{put(NAME_IMOUTO_HART,item_imoutohart);}
			{put(NAME_IMOUTO_LEG,item_imoutoleg);}
			{put(NAME_IMOUTO_UTERUS,item_imoutoutrus);}
			{put(NAME_IMOUTO_EYE,item_imoutoeye);}
			{put(NAME_IMOUTO_BLAIN,item_imoutoblain);}
			{put(NAME_IMOUTO_MEET,item_imoutomeet);}
			{put(NAME_IMOUTO_SKIN,item_imoutoskin);}
			{put(NAME_IMOUTO_HAIR,item_imoutohair);}
			{put(NAME_IMOUTO_COOKEDMEET,item_imouto_cookedmeet);}
		};


		resourceMap = new HashMap<String,ModelResourceLocation[]>(){
			{put(NAME_DIARY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_DIARY, "inventory")});}
			{put(NAME_CANDY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_CANDY, "inventory")});}
			{put(NAME_CANDY_MILLK,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_CANDY_MILLK, "inventory")});}
			{put(NAME_IMOUTO_BONE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_BONE, "inventory")});}
			{put(NAME_IMOUTO_BLOOD,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_BLOOD, "inventory")});}
			{put(NAME_IMOUTO_ARM,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_ARM, "inventory")});}
			{put(NAME_IMOUTO_BODY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_BODY, "inventory")});}
			{put(NAME_IMOUTO_HART,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_HART, "inventory")});}
			{put(NAME_IMOUTO_LEG,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_LEG, "inventory")});}
			{put(NAME_IMOUTO_UTERUS,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_UTERUS, "inventory")});}
			{put(NAME_IMOUTO_EYE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_EYE, "inventory")});}
			{put(NAME_IMOUTO_BLAIN,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_BLAIN, "inventory")});}
			{put(NAME_IMOUTO_MEET,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_MEET, "inventory")});}
			{put(NAME_IMOUTO_SKIN,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_SKIN, "inventory")});}
			{put(NAME_IMOUTO_HAIR,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_HAIR, "inventory")});}
			{put(NAME_IMOUTO_COOKEDMEET,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTO_COOKEDMEET, "inventory")});}
		};
	}

	public static void register(FMLPreInitializationEvent event) {
		init();
		for (String key : NAME_LIST){
			ModLog.log().debug(key);
			ForgeRegistries.ITEMS.register(itemMap.get(key));
		}

        //テクスチャ・モデル指定JSONファイル名の登録。
        if (event.getSide().isClient()) {
        	for (String key : NAME_LIST){
        		//1IDで複数モデルを登録するなら、上のメソッドで登録した登録名を指定する。
        		int cnt = 0;
        		for (ModelResourceLocation rc : resourceMap.get(key)){
        			ModelLoader.setCustomModelResourceLocation(itemMap.get(key), cnt, rc);
        			cnt++;
        		}
        	}
        }
	}
}