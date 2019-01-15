package mod.elm.core;

import mod.elm.block.BlockCore;
import mod.elm.entity.EntityCore;
import mod.elm.item.ItemCore;
import mod.elm.potion.PotionCore;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModRegister {
	public static void registerPreInit(FMLPreInitializationEvent event, CommonProxy proxy) {
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

		// ポーション登録
		ModRegister.RegisterPotion();
	}

	public static void registerInit() {
		// メッセージ登録
		ModRegister.RegisterMessage();
		NetworkRegistry.INSTANCE.registerGuiHandler(Mod_ElonaMobs.instance, new ModGui());
	}

	public static void registerPostInit() {
		// イベント
		ModRegister.RegisterEvent();
	}



	private static void RegisterBlock(FMLPreInitializationEvent event) {
		BlockCore.register(event);
	}

	private static void RegisterItem(FMLPreInitializationEvent event){
		ItemCore.register(event);
	}

	private static void RegisterEntity(CommonProxy proxy){
		EntityCore.register();
		// タイルエンティティはserverとclientで登録方法が違う為プロキシで分ける
		proxy.registerTileEntity();
	}

	private static void RegisterRender(CommonProxy proxy){
		// レンダーはクライアントの未登録
		proxy.registerRender();
	}

	private static void RegisterRecipe(){
	}

	private static void RegisterMessage(){
	}

	private static void RegisterSounds(){
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.block_imoutohead_normal);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.block_imoutohead_rotten);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.block_imoutohead_skelton);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.item_aromor_equip_imouto);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.item_imouto_use);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.item_imouto_meet);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.item_imouto_cookedmeet);
	}


	private static void RegisterEvent(){
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	private static void RegisterPotion(){
		PotionCore.register();
	}
}
