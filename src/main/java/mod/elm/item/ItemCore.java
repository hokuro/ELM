package mod.elm.item;

import java.util.HashMap;
import java.util.Map;

import mod.elm.core.ModCommon;
import mod.elm.core.Mod_ElonaMobs;
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
	public static final String NAME_IMOUTO_FACE = "imouto_face";
	public static final String NAME_IMOUTO_BLOOD = "imouto_blood";
	public static final String NAME_IMOUTO_ARM = "imouto_arm";
	public static final String NAME_IMOUTO_BODY = "imouto_body";
	public static final String NAME_IMOUTO_HART = "imouto_hart";
	public static final String NAME_IMOUTO_LEG = "imouto_leg";
	public static final String NAME_IMOUTO_UTERUS = "imouto_uterus";
	public static final String NAME_IMOUTO_EYE = "imouto_eye";


	public static final String[] NAME_LIST = new String[]{
//			NAME_CANDY,
//			NAME_CANDY_MILLK,
			NAME_DIARY,
//			NAME_IMOUTO_BONE,
//			NAME_IMOUTO_FACE,
//			NAME_IMOUTO_BLOOD,
//			NAME_IMOUTO_ARM,
//			NAME_IMOUTO_BODY,
//			NAME_IMOUTO_HART,
//			NAME_IMOUTO_LEG,
//			NAME_IMOUTO_UTERUS,
//			NAME_IMOUTO_EYE,
	};

	private static Map<String,Item> itemMap;
	private static Map<String,ModelResourceLocation[]> resourceMap;

	public static Item item_diary;
	private static void init(){
		item_diary = new Item()
				.setRegistryName(NAME_DIARY)
				.setUnlocalizedName(NAME_DIARY)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		itemMap = new HashMap<String,Item>(){
			{put(NAME_DIARY,item_diary);}

		};


		resourceMap = new HashMap<String,ModelResourceLocation[]>(){
			{put(NAME_DIARY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_DIARY, "inventory")});}

		};
	}

	public static void register(FMLPreInitializationEvent event) {
		init();
		for (String key : NAME_LIST){
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