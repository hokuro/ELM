package mod.elm.item.parts.ab;

import java.util.Optional;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface IItemElmParts {
	default void setPartsTarget(ItemStack stack, String name) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putString("partsTarget", name);
		stack.setTag(nbt);
	}

	default String getPartsTarget(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		String ret = nbt.getString("partsTarget");
		return ret;
	}

	default String getPartTargetName(ItemStack stack) {
		Optional<EntityType<?>> etype = getPartTargetEntity(stack);
		String ret = "";
		if (etype.isPresent()) {
			ret = I18n.format(etype.get().getTranslationKey());
		}
		return ret;
	}

	default Optional<EntityType<?>> getPartTargetEntity(ItemStack stack) {
		Optional<EntityType<?>> etype = Registry.ENTITY_TYPE.getValue(new ResourceLocation(getPartsTarget(stack)));
		return etype;
	}

	default void setMaxEndurance(ItemStack stack, int value) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("maxendurance", value);
		stack.setTag(nbt);
	}

	default int getMaxEndurance(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt.contains("maxendurance")) {
			nbt.getInt("maxendurance");
		}
		return -1;
	}


}
