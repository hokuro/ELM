package mod.elm.core;

import java.util.Calendar;
import java.util.Random;

import mod.elm.config.ConfigValue;
import mod.elm.creative.CreativeTabELM;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = ModCommon.MOD_ID,
name = ModCommon.MOD_NAME,
version = ModCommon.MOD_VERSION,
acceptedMinecraftVersions = ModCommon.MOD_ACCEPTED_MC_VERSIONS)
public class Mod_ElonaMobs {
	@Mod.Instance(ModCommon.MOD_ID)
	public static Mod_ElonaMobs instance;
	@SidedProxy(clientSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_CLIENT_SIDE, serverSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_SERVER_SIDE)
	public static CommonProxy proxy;
	public static final SimpleNetworkWrapper Net_Instance = NetworkRegistry.INSTANCE.newSimpleChannel(ModCommon.MOD_CHANEL);
	public static final ModGui guiInstance = new ModGui();
	// タブ
	public static final CreativeTabELM tabElmWepon = new CreativeTabELM("ElonaMobs");

	public static ToolMaterial materialImouto;
	public static ArmorMaterial materialImoutoA;
	public Random rnd;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		rnd = new Random();
		rnd.setSeed(Calendar.MILLISECOND);

		// コンフィグ読み込み
		ConfigValue.init(event);
		materialImouto = EnumHelper.addToolMaterial("imouto", 0, 021, 999.90F, 999.9F, 0);
		materialImoutoA = EnumHelper.addArmorMaterial("imouto", new ResourceLocation(ModCommon.MOD_ID,"imouto").toString(), 021, new int[]{3, 6, 8, 3}, 21, SoundManager.item_aromor_equip_imouto, 21F);

		ModRegister.registerPreInit(event, proxy);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModRegister.registerInit();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		ModRegister.registerPostInit();
	}
}
