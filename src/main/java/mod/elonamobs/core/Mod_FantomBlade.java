package mod.fbd.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import mod.fbd.config.ConfigValue;
import mod.fbd.core.log.ModLog;
import mod.fbd.creative.CreativeTabElmWepon;
import mod.fbd.item.ItemCore;
import mod.fbd.item.ItemTamahagane.EnumTamahagane;
import mod.fbd.resource.ResourceManager;
import mod.fbd.trade.VillagerTrade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

@Mod(modid = ModCommon.MOD_ID,
name = ModCommon.MOD_NAME,
version = ModCommon.MOD_VERSION,
acceptedMinecraftVersions = ModCommon.MOD_ACCEPTED_MC_VERSIONS)
public class Mod_FantomBlade {
	@Mod.Instance(ModCommon.MOD_ID)
	public static Mod_FantomBlade instance;
	@SidedProxy(clientSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_CLIENT_SIDE, serverSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_SERVER_SIDE)
	public static CommonProxy proxy;
	public static final SimpleNetworkWrapper Net_Instance = NetworkRegistry.INSTANCE.newSimpleChannel(ModCommon.MOD_CHANEL);
	public static final ModGui guiInstance = new ModGui();
	// タブ
	public static final CreativeTabElmWepon tabElmWepon = new CreativeTabElmWepon("Elm Wepon");
	public Random rnd;

	private ResourceManager texturemanager;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		rnd = new Random();
		rnd.setSeed(Calendar.MILLISECOND);

		// コンフィグ読み込み
		ConfigValue.init(event);

		// アイテム登録
		ModRegister.RegisterItem(event);

		// ブロック登録
		ModRegister.RegisterBlock(event);


		// エンティティ設定
		ModRegister.RegisterEntity(proxy);
		// レンダー設定
		ModRegister.RegisterRender(proxy);

		// サウンド登録
		ModRegister.RegisterSounds();
		
		// 村人登録
		ModRegister.RegisterVillager();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// メッセージ登録
		ModRegister.RegisterMessage();
		// レシピ追加
		//ModRegister.RegisterRecipe();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGui());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		// コンフィグ初期設定
		ConfigValue.setting();

		// 村人取引追加
		RegisterVillagerTrade();

		// イベント
		ModRegister.RegisterEvent();

		ItemStack test = new ItemStack(Items.DIAMOND_SWORD,1,0);
		for (ResourceLocation res : Enchantment.REGISTRY.getKeys()){
			Enchantment enc = Enchantment.getEnchantmentByLocation(res.toString());
			if (enc.canApply(test)){
				swordEnchantments.add(enc);
			}
		}
		for (ResourceLocation res : Potion.REGISTRY.getKeys()){
			Potion pot = Potion.getPotionFromResourceLocation(res.toString());
			swordPotion.add(new PotionEffect(pot,0,-1));
		}
	}
	public static List<Enchantment> swordEnchantments = new ArrayList<Enchantment>();
	public static List<PotionEffect> swordPotion = new ArrayList<PotionEffect>();


	public void RegisterVillagerTrade(){
		// トレード追加
		// 鍛冶屋の職業を取り出す
		VillagerProfession prof = VillagerRegistry.getById(3);
		// 防具職人
		VillagerCareer c0 = prof.getCareer(0);
		c0.addTrade(1, new ITradeList[]{
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_INGOT,ItemCore.item_zuku,  new EntityVillager.PriceInfo(4, 7)),
				new VillagerTrade.ListItemForAnotherItem(ItemCore.item_steel,new ItemStack(ItemCore.item_tamahagane,1,EnumTamahagane.SECOND_GRADE.getIndex()), new EntityVillager.PriceInfo(6, 12))
		});
		c0.addTrade(2, new ITradeList[]{
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_HELMET, ItemCore.item_zuku, new EntityVillager.PriceInfo(8, 12)),
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_CHESTPLATE, ItemCore.item_zuku, new EntityVillager.PriceInfo(10, 14)),
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_LEGGINGS,ItemCore.item_zuku,  new EntityVillager.PriceInfo(10, 14)),
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_BOOTS,ItemCore.item_zuku,  new EntityVillager.PriceInfo(8, 12))
		});
		c0.addTrade(3, new ITradeList[]{
			new VillagerTrade.ListItemForAnotherItem(Items.CHAINMAIL_BOOTS, ItemCore.item_zuku, new EntityVillager.PriceInfo(8, 12)),
			new VillagerTrade.ListItemForAnotherItem(Items.CHAINMAIL_LEGGINGS, ItemCore.item_zuku, new EntityVillager.PriceInfo(10, 16)),
			new VillagerTrade.ListItemForAnotherItem(Items.CHAINMAIL_HELMET, ItemCore.item_zuku, new EntityVillager.PriceInfo(10, 16)),
			new VillagerTrade.ListItemForAnotherItem(Items.CHAINMAIL_CHESTPLATE, ItemCore.item_zuku, new EntityVillager.PriceInfo(13, 17))
		});

		// 武器職人
		VillagerCareer c1 = prof.getCareer(1);
		c1.addTrade(1, new ITradeList[]{
				new VillagerTrade.ListItemForAnotherItem(Items.COAL, ItemCore.item_zuku, new EntityVillager.PriceInfo(16, 24)),
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_AXE, ItemCore.item_zuku, new EntityVillager.PriceInfo(9, 11)),
				new VillagerTrade.ListItemForAnotherItem(ItemCore.item_steel,new ItemStack(ItemCore.item_tamahagane,1,EnumTamahagane.SECOND_GRADE.getIndex()), new EntityVillager.PriceInfo(6, 12))
		});
		c1.addTrade(2, new ITradeList[]{
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_INGOT, ItemCore.item_zuku, new EntityVillager.PriceInfo(4, 7)),
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_SWORD, ItemCore.item_zuku, new EntityVillager.PriceInfo(12, 13))
		});


		// ツール職人
		VillagerCareer c2 = prof.getCareer(2);
		c2.addTrade(1, new ITradeList[]{
				new VillagerTrade.ListItemForAnotherItem(Items.COAL, ItemCore.item_zuku, new EntityVillager.PriceInfo(16, 24)),
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_SHOVEL, ItemCore.item_zuku, new EntityVillager.PriceInfo(8, 12)),
				new VillagerTrade.ListItemForAnotherItem(ItemCore.item_steel,new ItemStack(ItemCore.item_tamahagane,1,EnumTamahagane.SECOND_GRADE.getIndex()), new EntityVillager.PriceInfo(6, 12))
		});
		c2.addTrade(2, new ITradeList[]{
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_INGOT, ItemCore.item_zuku, new EntityVillager.PriceInfo(10, 15)),
				new VillagerTrade.ListItemForAnotherItem(Items.IRON_PICKAXE, ItemCore.item_zuku, new EntityVillager.PriceInfo(14, 17))
		});
	}


	public static final List<String> modelNames = new ArrayList<String>(){
		{add("bladesmith");}
		{add("normalblade");}
	};

	/**
	 * 指定した名称のモブのクラスを取得する
	 * @param name クラス名
	 * @return クラス
	 */
	public static String checkModelName(String name){
		int idx=-1;
		if ( ((idx = modelNames.indexOf(name)) >= 0)){
			return modelNames.get(idx);
		}
		return null;
	}

	public ResourceManager TextureManager(){
		if ( texturemanager == null){
			texturemanager = new ResourceManager();
			try{
			texturemanager.initResource();
			}catch(Exception ex){
				ModLog.log().info("Can't Use CusutomTexture");
			}
		}
		return texturemanager;
	}


//
//	public List<ResourceLocation> TEXTURES_BLADESMITH;
//	public List<ResourceLocation> TEXTURES_NORMALBLADE;
//	public void registerTextures(){
//		TEXTURES_BLADESMITH = new ArrayList<ResourceLocation>();
//		TEXTURES_BLADESMITH.add(new ResourceLocation("elm","textures/entity/bladesmith.png"));
//
//		TEXTURES_NORMALBLADE = new ArrayList<ResourceLocation>();
//		TEXTURES_NORMALBLADE.add(new ResourceLocation("elm","textures/entity/katana_nrmal.png"));
//
//	}
//
//	public ResourceLocation getRandomBladeSmithTexture(){
//		int index = rnd.nextInt(TEXTURES_BLADESMITH.size());
//		if (index < 0 || index >= TEXTURES_BLADESMITH.size()){
//			index = 0;
//		}
//		return TEXTURES_BLADESMITH.get(index);
//	}
//
//	public ResourceLocation getRandomBladeTexture(){
//		int index = rnd.nextInt(TEXTURES_NORMALBLADE.size());
//		if (index < 0 || index >= TEXTURES_NORMALBLADE.size()){
//			index = 0;
//		}
//		return TEXTURES_NORMALBLADE.get(index);
//	}
//
//	public boolean checkBladeSmithTexture(ResourceLocation name){
//		return TEXTURES_BLADESMITH.contains(name);
//	}
//
//	public boolean checkBlade(ResourceLocation name){
//		return TEXTURES_NORMALBLADE.contains(name);
//	}

}
