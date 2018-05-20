package mod.elm.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundManager {
	public static String SOUND_BLADESMITH_HART = "entity.fbd.bladesmithhart";
	public static String SOUND_BLADESMITH_DAMAGE = "entity.fbd.bladesmithdamage";
	public static String SOUND_BLADESMITH_DEAD = "entity.fbd.bladesmithdead";
	public static String SOUND_BLADESMITH_WORK = "entity.fbd.bladesmithwork";
	public static String SOUND_GUN_GUNSHOT = "item.fbd.gunshot";
	public static String SOUND_GUN_RELOAD ="item.fbd.reload";
	public static String SOUND_GUN_NOBURRET = "item.fbd.noburret";

	public static SoundEvent sound_bladesmith_hart = new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_BLADESMITH_HART))
			.setRegistryName(SOUND_BLADESMITH_HART);
	public static SoundEvent sound_bladesmith_damage = new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_BLADESMITH_DAMAGE))
			.setRegistryName(SOUND_BLADESMITH_DAMAGE);
	public static SoundEvent sound_bladesmith_dead = new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_BLADESMITH_DEAD))
			.setRegistryName(SOUND_BLADESMITH_DEAD);
	public static SoundEvent sound_bladesmith_work = new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_BLADESMITH_WORK))
			.setRegistryName(SOUND_BLADESMITH_WORK);
	public static SoundEvent sound_gun_gunshot = new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_GUN_GUNSHOT))
			.setRegistryName(SOUND_GUN_GUNSHOT);
	public static SoundEvent sound_gun_reload = new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_GUN_RELOAD))
			.setRegistryName(SOUND_GUN_RELOAD);
	public static SoundEvent sound_gun_noburret = new SoundEvent(new ResourceLocation(ModCommon.MOD_ID+":" + SOUND_GUN_NOBURRET))
			.setRegistryName(SOUND_GUN_NOBURRET);
}
