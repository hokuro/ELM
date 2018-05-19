package mod.fbd.config;

import mod.fbd.util.ModUtil;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigValue{
	private static final ModConfig config = new ModConfig();

	public static void init(FMLPreInitializationEvent event){
		config.init(new Class<?>[]{
			General.class
		}, event);
	}


	public static void setting() {
	}

	public static void save(){
		config.saveConfig();
	}

	public static class General{
		@ConfigProperty(comment="show debutlog")
		public static boolean isDebut=false;
		@ConfigProperty(comment="show debutlog")
		public static String[] BladeNames ={"刀"};
		@ConfigProperty(comment="show debutlog")
		public static boolean GunGuiAutoOpen = false;

		public static String getRandomName() {
			String ret = "刀";
			if (BladeNames.length != 0){
				ret = BladeNames[ModUtil.random(BladeNames.length)];
			}
			return ret;
		}
	}
}