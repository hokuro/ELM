package mod.elm.item.tier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class TierBody implements IItemTier {

	@Override
	public int getMaxUses() {
		return 10;
	}

	@Override
	public float getEfficiency() {
		return 999;
	}

	@Override
	public float getAttackDamage() {
		return 0;
	}

	@Override
	public int getHarvestLevel() {
		return 999;
	}

	@Override
	public int getEnchantability() {
		return 999;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return null;
	}

}
