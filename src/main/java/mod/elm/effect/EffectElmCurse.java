package mod.elm.effect;

import mod.elm.core.Mod_Elm;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

public class EffectElmCurse extends Effect{

	private final String damageType;
	public EffectElmCurse(String dt, int potioncolor) {
		super(EffectType.HARMFUL, potioncolor);
		damageType = dt;
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		if (this == Mod_Elm.RegistryEvents.EFFECT_BLOODCURSE_LIGHT) {
			World world = entity.world;
			int light = world.getLight(entity.getPosition());

			if (light + amplifier > 7) {
				entity.attackEntityFrom(new EntityDamageSource(damageType, entity).setDamageBypassesArmor(), 1);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int k = 25 >> amplifier;
		if (k > 0) {
			return (duration % k) == 0;
		}else {
			return true;
		}
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public void applyAttributesModifiersToEntity(LivingEntity entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		World world = entity.world;
		int light = world.getLight(entity.getPosition());

		if (light - amplifier < 7) {
			super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
		}
	}
}
