package mod.elm.core;

import mod.elm.block.BlockCore;
import mod.elm.item.ItemCore;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModRegister {
	public static void RegisterBlock(FMLPreInitializationEvent event) {
		BlockCore.register(event);
	}

	public static void RegisterItem(FMLPreInitializationEvent event){
		ItemCore.register(event);
	}

	public static void RegisterEntity(CommonProxy proxy){

		// タイルエンティティはserverとclientで登録方法が違う為プロキシで分ける
		proxy.registerTileEntity();
	}

	public static void RegisterRender(CommonProxy proxy){
		// レンダーはクライアントの未登録
		proxy.registerRender();
	}

	public static void RegisterRecipe(){
	}

	public static void RegisterMessage(){
	}

	public static void RegisterSounds(){
	}


	public static void RegisterEvent(){
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
}
