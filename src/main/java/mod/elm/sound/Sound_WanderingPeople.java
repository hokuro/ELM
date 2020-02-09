package mod.elm.sound;

import mod.elm.entity.EntityWanderingPeople;
import mod.elm.util.ModUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Sound_WanderingPeople {
	public static SoundEvent getAmbientSound(World world, EntityWanderingPeople wander) {
		SoundEvent event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_HURT;;
		Biome biome = world.getBiome(wander.getPosition());
		if (wander.getHealth() < wander.getMaxHealth()/2.0F && ModUtil.randomF() < 0.3F) {
			// i疲れた
			event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_AMBIENT_HEALTH;
		}else if (wander.getHunger() <= 0.0F && ModUtil.randomF() < 0.3F) {
			// iおなかすいたボイス
			event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_AMBIENT_HUNGER;
		}else if (ModUtil.randomF() < 0.4F) {
			// i気温ボイス
			float temp = biome.getTemperature(wander.getPosition());
			if (temp < 0.2F) {
				// i寒いボイス
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_AMBIENT_COLD;
			} else if (temp > 1.0 ) {
				// i暑いボイス
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_AMBIENT_HOT;
			}
//		} else if (ModUtil.randomF() < 0.2F) {
//			// i観光ボイス
		} else if (ModUtil.randomF() < 0.3F) {
			// i光ボイス
			if (world.getLight(wander.getPosition()) < 7) {
				// i暗がりボイス
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_AMBIENT_DARK;
			}
		} else if (ModUtil.randomF() < 0.3F) {
			if (world.isThundering()) {
				// i嵐ボイス
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_WEATHER_THUNDER;
			}else if (world.isRaining()) {
				if (biome.getTemperature(wander.getPosition()) < 0.15) {
					// i雪ボイス
					event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_WEATHER_SNOW;
				} else if (biome.getTemperature(wander.getPosition()) < 0.95){
					// i雨ボイス
					event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_WEATHER_RAIN;
				}
			}
		}
		return event;
	}


	public static SoundEvent getDamageVoice(DamageSource source, EntityWanderingPeople wander) {
		SoundEvent event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_NORMAL;
		if (ModUtil.randomD() < 0.8F) {
			if (DamageSource.ANVIL == source || DamageSource.FALLING_BLOCK == source) {
				// iつぶれる
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_FALLBLOCK;
			}else if (DamageSource.CACTUS == source) {
				// iちくちくー
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_CACUTAS;
			}else if (DamageSource.CRAMMING == source) {
				// i圧迫
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_PRESS;
			}else if (DamageSource.FALL == source) {
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_FALL;
			}else if (DamageSource.IN_FIRE == source || DamageSource.ON_FIRE == source || DamageSource.LAVA == source) {
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_FLAME;
			}else if (DamageSource.IN_WALL == source) {
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_PRESS;
			}
		}
		return event;
	}


	public static SoundEvent getDeathSound() {
		return SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DEAD;
	}


	public static SoundEvent getStepSound() {
		return SoundCore.SOUND_VOICE_WANDERING_PEOPLE_STEP;
	}


	public static SoundEvent getVoiceFollowFoods(Item item, EntityWanderingPeople entityWanderingPeople) {
		SoundEvent event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_SEEFOOD;
		if (ModUtil.randomD() < 0.7D && entityWanderingPeople.getHealth()/entityWanderingPeople.getMaxHealth() < 0.6) {
			event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_AMBIENT_HUNGER;
		}
		return event;
	}


	public static SoundEvent getVoiceFollwJuery(Item item, EntityWanderingPeople entityWanderingPeople) {
		SoundEvent event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_SEEJEWEL;
		if (ModUtil.randomD() < 0.7D) {
			if (Items.DIAMOND == item.getItem()) {
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_SEEDIA;
			}else if (Items.EMERALD == item.getItem()) {
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_SEEEMELARD;
			}else if(Items.GOLD_INGOT == item.getItem() || Items.GOLD_NUGGET == item.getItem()) {
				event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_SEEGOLD;
			}
		}
		return event;
	}


	public static SoundEvent getVoiceTresur(Item item, EntityWanderingPeople entityWanderingPeople) {
		SoundEvent event = SoundCore.SOUND_VOICE_WANDERING_PEOPLE_TRASH;
		return event;
	}


	public static SoundEvent getVoiceFoods(Item item, boolean hasTrash, boolean isHunger,
			EntityWanderingPeople entityWanderingPeople) {
		return SoundCore.SOUND_VOICE_WANDERING_PEOPLE_EATFOOD;
	}


	public static SoundEvent getVoiceCantHave() {
		return SoundCore.SOUND_VOICE_WANDERING_PEOPLE_CANTHAVE;
	}


	public static SoundEvent getCake() {
		return SoundCore.SOUND_VOICE_WANDERING_PEOPLE_SEECAKE;
	}


	public static SoundEvent getAffrade() {
		return SoundCore.SOUND_VOICE_WANDERING_PEOPLE_DAMAGE_NORMAL;
	}
}
