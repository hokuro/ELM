package mod.elm.potion;

import mod.elm.core.ModCommon;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionCore {
	public static final String NAME_POTION_IMOUTOCONFIUSION = "imoutoconfusion";
	public static final String NAME_POTION_IMOUTOINSANITY = "imoutoinsanity";
	public static final String NAME_POTION_IMOUTOINSANITYBURN = "imoutoinsanityburn";


	public static Potion imouto_confusion;
	public static Potion imouto_sanity;
	public static Potion imouto_sanity_burn;

	public static void init(){
		imouto_confusion = new PotionImoutoConfiusion()
				.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,NAME_POTION_IMOUTOCONFIUSION));
		imouto_sanity = new PotionImoutoInsanity(false)
				.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,NAME_POTION_IMOUTOINSANITY));
		imouto_sanity_burn = new PotionImoutoInsanity(true)
				.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,NAME_POTION_IMOUTOINSANITYBURN));
	}

	public static void register(){
		PotionCore.init();
		ForgeRegistries.POTIONS.register(imouto_confusion);
		ForgeRegistries.POTIONS.register(imouto_sanity);
		ForgeRegistries.POTIONS.register(imouto_sanity_burn);
	}
}
