package mod.elm.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundManager {
	public static String SOUND_BLOCKIMOUTOHED_NORMAL = "block.elm.imoutohead.normal";
	public static String SOUND_BLOCKIMOUTOHED_ROTTEN = "block.elm.imoutohead.rotten";
	public static String SOUND_BLOCKIMOUTOHED_SKELTON = "block.elm.imoutohead.skelton";
	public static String SOUND_ITEMIMOUTOARMOR_EQUIP = "item.elm.imoutoarmor.equip";
	public static String SOUND_ITEMIMOUTO_USE = "item.elm.imoutoitem.usevoice";
	public static String SOUND_ITEMIMOUTO_MEET = "item.elm.imoutoitem.meet";
	public static String SOUND_ITEMIMOUTO_COOKEDMEET = "item.elm.imoutoitem.cookedmeet";

	public static SoundEvent block_imoutohead_normal =
			new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_BLOCKIMOUTOHED_NORMAL))
			.setRegistryName(SOUND_BLOCKIMOUTOHED_NORMAL);
	public static SoundEvent block_imoutohead_rotten =
			new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_BLOCKIMOUTOHED_ROTTEN))
			.setRegistryName(SOUND_BLOCKIMOUTOHED_ROTTEN);
	public static SoundEvent block_imoutohead_skelton =
			new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_BLOCKIMOUTOHED_SKELTON))
			.setRegistryName(SOUND_BLOCKIMOUTOHED_SKELTON);

	public static SoundEvent item_aromor_equip_imouto =
			new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_ITEMIMOUTOARMOR_EQUIP))
			.setRegistryName(SOUND_ITEMIMOUTOARMOR_EQUIP);
	public static SoundEvent item_imouto_use =
			new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_ITEMIMOUTO_USE))
			.setRegistryName(SOUND_ITEMIMOUTO_USE);
	public static SoundEvent item_imouto_meet =
			new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_ITEMIMOUTO_MEET))
			.setRegistryName(SOUND_ITEMIMOUTO_MEET);
	public static SoundEvent item_imouto_cookedmeet =
			new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_ITEMIMOUTO_COOKEDMEET))
			.setRegistryName(SOUND_ITEMIMOUTO_COOKEDMEET);
}
