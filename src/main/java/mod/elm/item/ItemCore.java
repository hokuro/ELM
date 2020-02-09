package mod.elm.item;

import java.util.HashMap;
import java.util.Map;

import mod.elm.core.Mod_Elm;
import mod.elm.item.body.ItemElmArm;
import mod.elm.item.body.ItemElmBody;
import mod.elm.item.body.ItemElmLeg;
import mod.elm.item.parts.ItemElmBlood;
import mod.elm.item.parts.ItemElmBone;
import mod.elm.item.parts.ItemElmBrain;
import mod.elm.item.parts.ItemElmEye;
import mod.elm.item.parts.ItemElmFoot;
import mod.elm.item.parts.ItemElmHand;
import mod.elm.item.parts.ItemElmHart;
import mod.elm.item.parts.ItemElmIntestine;
import mod.elm.item.parts.ItemElmLung;
import mod.elm.item.parts.ItemElmMeet;
import mod.elm.item.parts.ItemElmSkin;
import mod.elm.item.parts.ItemElmStmach;
import mod.elm.item.parts.ItemElmUterus;
import mod.elm.item.tier.TierBody;
import net.minecraft.item.Food;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemCore {
	public static final String NAME_RAWMAERGERPOT = "rawmeargerpot";
	public static final String NAME_MOBANALYZER = "mobanalyzer";
	public static final String NAME_BRAIN = "elm_brain";
	public static final String NAME_EYE = "elm_eye";
	public static final String NAME_HAND = "elm_hand";
	public static final String NAEM_FOOT = "elm_foot";
	public static final String NAME_HART = "elm_hart";
	public static final String NAME_STMACH = "elm_stmach";
	public static final String NAME_LUNG = "elm_lung";
	public static final String NAME_INTESTINE = "elm_intestines";
	public static final String NAME_UTERUS = "elm_uterus";
	public static final String NAME_BONE = "elm_bone";
	public static final String NAME_BLOOD = "elm_blood";
	public static final String NAME_MEET = "elm_meet";
	public static final String NAME_SKIN = "elm_skin";


	public static final String NAME_ELM_ARM = "elm_arm";
	public static final String NAEM_ELM_BODY = "elm_body";
	public static final String NAME_ELM_LEG = "elm_leg";


	public static final String NAME_DRIED_BRAINCELL = "dried_braincell";
	public static final String NAME_DRIED_BRAIN = "dried_brain";
	public static final String NAME_FRESH_BRAIN = "fresh_brain";

	public static final String[] NAME_LIST = new String[]{
			NAME_RAWMAERGERPOT,
			NAME_MOBANALYZER,
			NAME_BRAIN,
			NAME_EYE,
			NAME_HAND,
			NAEM_FOOT,
			NAME_HART,
			NAME_STMACH,
			NAME_LUNG,
			NAME_INTESTINE,
			NAME_UTERUS,
			NAME_BONE,
			NAME_BLOOD,
			NAME_MEET,
			NAME_SKIN,
			NAEM_ELM_BODY,
			NAME_ELM_ARM,
			NAME_ELM_LEG,
			NAME_DRIED_BRAINCELL,
			NAME_DRIED_BRAIN,
			NAME_FRESH_BRAIN,

	};

	public static IItemTier TIER_BODY = new TierBody();
	public static Item item_rawmaergerpot = new Item(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_RAWMAERGERPOT);
	public static Item item_mobanalyzer = new ItemMobAnalyzer(new Item.Properties().group(Mod_Elm.GROUPELM).maxStackSize(1)).setRegistryName(NAME_MOBANALYZER);
	public static Item item_brain = new ItemElmBrain(TIER_BODY, 0, 0.0F, new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_BRAIN);
	public static Item item_eye = new ItemElmEye(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_EYE);
	public static Item item_hand = new ItemElmHand(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_HAND);
	public static Item item_foot = new ItemElmFoot(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAEM_FOOT);
	public static Item item_hart = new ItemElmHart(new Item.Properties().maxDamage(4000).group(Mod_Elm.GROUPELM)).setRegistryName(NAME_HART);
	public static Item item_stmach = new ItemElmStmach(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_STMACH);
	public static Item item_lung = new ItemElmLung(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_LUNG);
	public static Item item_intestine = new ItemElmIntestine(new Item.Properties().maxDamage(60).group(Mod_Elm.GROUPELM)).setRegistryName(NAME_INTESTINE);
	public static Item item_uterus = new ItemElmUterus(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_UTERUS);
	public static Item item_bone = new ItemElmBone(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_BONE);
	public static Item item_blood = new ItemElmBlood(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_BLOOD);
	public static Item item_meet = new ItemElmMeet(new Item.Properties().group(Mod_Elm.GROUPELM).food(new Food.Builder().meat().build())).setRegistryName(NAME_MEET);
	public static Item item_skin = new ItemElmSkin(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_SKIN);
	public static Item item_elm_body = new ItemElmBody(new Item.Properties().maxDamage(10).group(Mod_Elm.GROUPELM)).setRegistryName(NAEM_ELM_BODY);
	public static Item item_elm_arm = new ItemElmArm(TIER_BODY, 0, 0.0F, new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_ELM_ARM);
	public static Item item_elm_leg = new ItemElmLeg(TIER_BODY, 0, 0.0F, new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_ELM_LEG);

	public static Item item_dried_braincell = new Item(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_DRIED_BRAINCELL);
	public static Item item_dried_brain = new Item(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_DRIED_BRAIN);
	public static Item item_fresh_brain = new Item(new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_FRESH_BRAIN);


	private static Map<String,Item> itemMap;
	public static void init(){
		itemMap = new HashMap<String,Item>(){
			{put(NAME_RAWMAERGERPOT,item_rawmaergerpot);}
			{put(NAME_MOBANALYZER, item_mobanalyzer);}
			{put(NAME_BRAIN, item_brain);}
			{put(NAME_EYE, item_eye);}
			{put(NAME_HAND, item_hand);}
			{put(NAEM_FOOT, item_foot);}
			{put(NAME_HART, item_hart);}
			{put(NAME_STMACH, item_stmach);}
			{put(NAME_LUNG, item_lung);}
			{put(NAME_INTESTINE, item_intestine);}
			{put(NAME_UTERUS, item_uterus);}
			{put(NAME_BONE, item_bone);}
			{put(NAME_BLOOD, item_blood);}
			{put(NAME_MEET, item_meet);}
			{put(NAME_SKIN, item_skin);}
			{put(NAEM_ELM_BODY, item_elm_body);}
			{put(NAME_ELM_ARM, item_elm_arm);}
			{put(NAME_ELM_LEG, item_elm_leg);}
			{put(NAME_DRIED_BRAINCELL, item_dried_braincell);}
			{put(NAME_DRIED_BRAIN, item_dried_brain);}
			{put(NAME_FRESH_BRAIN, item_fresh_brain);}
		};

	}

	public static void register(final RegistryEvent.Register<Item> event) {
		init();
		for (String key : NAME_LIST){
			if (itemMap.containsKey(key)) {
				event.getRegistry().register(itemMap.get(key));
			}
		}
	}
}
