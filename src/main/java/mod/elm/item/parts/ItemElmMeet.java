package mod.elm.item.parts;

import java.util.Optional;

import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.util.ModUtil;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Food.Builder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class ItemElmMeet extends ItemElmParts {

	public ItemElmMeet(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (!worldIn.isRemote) {
			entityLiving.onFoodEaten(worldIn, makeFood(worldIn, stack));
			stack.shrink(1);
		}
		return stack;
	}

	private ItemStack makeFood(World worldIn, ItemStack stack) {
		Optional<EntityType<?>> oetype = this.getPartTargetEntity(stack);
		if (oetype.isPresent()) {
			EntityType etype = oetype.get();
			Builder newFood = new Food.Builder();
			MobEntity living = (MobEntity)etype.create(worldIn);
			int health = (int)(living.getMaxHealth() * 2);
			if (etype.getClassification() == EntityClassification.MONSTER) {
				if (living.getCreatureAttribute() == CreatureAttribute.UNDEAD) {
					newFood.effect(new EffectInstance(Effects.HUNGER, 20 * health, health/10), 1.0F - (health/100));
				} else if (living.getCreatureAttribute() == CreatureAttribute.ARTHROPOD) {
					newFood.effect(new EffectInstance(Effects.POISON, 20 * (int)health, (int)health/10), 1.0F - (health/100));
				} else if (living.getCreatureAttribute() == CreatureAttribute.WATER) {
					newFood.effect(new EffectInstance(Effects.DOLPHINS_GRACE, 20 * (int)health, -1 * (int)health/10), 1.0F - (health/100));
				}
				newFood.effect(new EffectInstance(Effects.NAUSEA, 20 * (int)health, (int)health/10), 1.0F - (health/100));
			} else {
				if (living.getCreatureAttribute() == CreatureAttribute.WATER) {
					newFood.effect(new EffectInstance(Effects.DOLPHINS_GRACE, 20 * (int)health, (int)health/10), 1.0F - (health/100));
				} else if (!(living instanceof AnimalEntity)) {
					newFood.effect(new EffectInstance(Effects.HUNGER, 20 * (int)health, (int)health/10), 1.0F - (health/100));
					newFood.effect(new EffectInstance(Effects.NAUSEA, 20 * (int)health, (int)health/10), 1.0F - (health/100));
				}
			}
			newFood.hunger(ModUtil.random(health)).saturation(health/20).meat();
			return new ItemStack(new Item(new Item.Properties().food(newFood.build())),1);
		}
		return stack;
	}


}
