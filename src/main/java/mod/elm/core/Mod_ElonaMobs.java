package mod.elm.core;

import java.util.Calendar;
import java.util.Random;

import mod.elm.config.ConfigValue;
import mod.elm.creative.CreativeTabELM;
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
	public Random rnd;


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
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// メッセージ登録
		ModRegister.RegisterMessage();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGui());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		// イベント
		ModRegister.RegisterEvent();
	}
}
