package mod.elm.effect;

import mod.elm.core.Mod_Elm;
import mod.elm.util.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class EffectElm extends Effect {

	public EffectElm(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_LIGHT) {
			World world = entity.world;
			int light = world.getLight(entity.getPosition());
			if (light + amplifier > 7) {
				entity.attackEntityFrom(new EntityDamageSource("elm_bloodcurse_light", entity).setDamageBypassesArmor().setDamageIsAbsolute(), 1);
			}

			this.removeAttributesModifiersFromEntity(entity, entity.getAttributes(), amplifier);
			this.applyAttributesModifiersToEntity(entity, entity.getAttributes(), amplifier);
		} else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_WATER) {
			if (entity.isInWater()) {
				entity.attackEntityFrom(new EntityDamageSource("elm_bloodcurse_water", entity).setDamageBypassesArmor().setDamageIsAbsolute(), 1);
			}

			this.removeAttributesModifiersFromEntity(entity, entity.getAttributes(), amplifier);
			this.applyAttributesModifiersToEntity(entity, entity.getAttributes(), amplifier);
		} else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_WARM) {
			World world = entity.world;
			float tmp = world.getBiome(entity.getPosition()).getTemperature(entity.getPosition());
			if (amplifier >= 3 && (tmp+amplifier/10.0) >= 1.5) {
				entity.attackEntityFrom(new EntityDamageSource("elm_bloodcurse_warm", entity).setDamageBypassesArmor().setDamageIsAbsolute(), amplifier-2);
			}

			this.removeAttributesModifiersFromEntity(entity, entity.getAttributes(), amplifier);
			this.applyAttributesModifiersToEntity(entity, entity.getAttributes(), amplifier);
		} else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_COLD) {
			World world = entity.world;
			float tmp = world.getBiome(entity.getPosition()).getTemperature(entity.getPosition());
			if (amplifier >= 3 && (tmp-amplifier/10.0) <= 0.5) {
				entity.attackEntityFrom(new EntityDamageSource("elm_bloodcurse_warm", entity).setDamageBypassesArmor().setDamageIsAbsolute(), amplifier-2);
			}

			this.removeAttributesModifiersFromEntity(entity, entity.getAttributes(), amplifier);
			this.applyAttributesModifiersToEntity(entity, entity.getAttributes(), amplifier);
		} else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODPARASITE) {
			if (entity.isPotionActive(Effects.POISON)) {
				// i毒状態の場合エフェクト削除
				entity.removePotionEffect(this);
				if (entity instanceof PlayerEntity) {
					((PlayerEntity)entity).sendMessage(new TranslationTextComponent("elm.bloodpalasite_melt"));
				}
				return;
			}
			if (entity.isPotionActive(Effects.HUNGER)) {
				// i空腹状態の場合確率でエフェクト削除
				if (ModUtil.randomF() < 0.6F) {
					entity.removePotionEffect(this);
					if (entity instanceof PlayerEntity) {
						((PlayerEntity)entity).sendMessage(new TranslationTextComponent("elm.bloodpalasite_melt"));
					}
					return;
				}
			}
			World world = entity.world;
			EffectInstance effect = entity.getActivePotionEffect(this);
			if (effect.getDuration() == 1) {
				if (!world.isRemote) {
					PlayerEntity player = (entity instanceof PlayerEntity)?(PlayerEntity)entity:null;
					if (player != null && !player.isCreative()) {
						// iプレイヤーの場合インベントリの内容をぶちまける
						if (entity instanceof PlayerEntity) {
							InventoryHelper.dropInventoryItems(world, entity.getPosition(), ((PlayerEntity)entity).inventory);
						}
						// i死ぬまでダメージを与え続ける
						while(entity.isAlive()) {
							entity.attackEntityFrom(new DamageSource("elm_bloodpalasite").setDamageBypassesArmor().setDamageIsAbsolute(), 9999);
						}
					}

					// i蝙蝠を大量召喚
					for (int i = 0; i < 20; i++) {
						Entity ent = EntityType.BAT.spawn(world, ItemStack.EMPTY, null, entity.getPosition(), SpawnReason.NATURAL, true, false);
						if (ent != null) {
							((BatEntity)ent).addPotionEffect(new EffectInstance(Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_LIGHT, 1200, 20));
						}
					}
				}
			} else {
				if ((effect.getDuration() % (60 * 20))  == 0 && ModUtil.randomF() <= 0.7) {
					entity.attackEntityFrom(new EntityDamageSource("elm_bloodpalasite_move", entity).setDamageBypassesArmor().setDamageIsAbsolute(), 1);

					if (entity instanceof PlayerEntity) {
						// iプレイヤーの場合進行度に合わせてメッセージ
						PlayerEntity player = (PlayerEntity)entity;
						if (entity.isAlive()) {
							if (effect.getDuration() <= 120 * 20) {
								player.sendMessage(new TranslationTextComponent("elm.bloodpalasite_soon"));
							} else if (effect.getDuration() <= 480 * 20) {
								player.sendMessage(new TranslationTextComponent("elm.bloodpalasite_3thstage"));
							} else if (effect.getDuration() <= 600 * 20) {
								player.sendMessage(new TranslationTextComponent("elm.bloodpalasite_2ndstage"));
							} else if (effect.getDuration() <= 1200 * 20) {
								player.sendMessage(new TranslationTextComponent("elm.bloodpalasite_1ststage"));
							}
						} else {
							player.sendMessage(new TranslationTextComponent("elm.bloodpalasite_dead"));
						}
					}
				}
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int t = 0;
		if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_LIGHT ||
			this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_WATER ||
			this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_WARM ||
			this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_COLD) {
			return ((duration % 60) == 0);
		} else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODPARASITE) {
			return true;
		}
		return true;
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public void applyAttributesModifiersToEntity(LivingEntity entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		if (checkApplyEffect(entity, amplifier)) {
			super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
		}
	}

	private boolean checkApplyEffect(LivingEntity entity, int amplifier) {
		boolean ret = true;
		if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_LIGHT) {
			World world = entity.world;
			int light = world.getLight(entity.getPosition());
			if (light + amplifier <= 7) {
				ret = false;
			}
		}else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_WATER) {
			ret = !entity.isInWater();
		}else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_WARM) {
			World world = entity.world;
			float tmp = world.getBiome(entity.getPosition()).getTemperature(entity.getPosition());
			if ((tmp+amplifier/10.0) < 1.5) {
				ret = false;
			}
		} else if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_COLD) {
			World world = entity.world;
			float tmp = world.getBiome(entity.getPosition()).getTemperature(entity.getPosition());
			if ((tmp-amplifier/10.0) > 0.5) {
				ret = false;
			}
		}
		return ret;
	}
}