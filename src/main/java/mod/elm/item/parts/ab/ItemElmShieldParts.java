package mod.elm.item.parts.ab;

import java.util.List;
import java.util.stream.Collectors;

import mod.elm.core.Mod_Elm;
import mod.elm.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;

public class ItemElmShieldParts extends ShieldItem implements IItemElmParts {

	public ItemElmShieldParts(Properties builder) {
		super(builder);
	}

	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		String name = this.getPartTargetName(stack);
		return name + "'s " + I18n.format(this.getTranslationKey());
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack stack = this.getDefaultInstance();
			try {
				List<EntityType<?>> lst = Registry.ENTITY_TYPE.stream().filter(e-> {return (e.create(Minecraft.getInstance().world) instanceof MobEntity);}).collect(Collectors.toList());
				EntityType<?> etype = lst.get(ModUtil.random(lst.size()));
				this.setPartsTarget(stack, etype.getRegistryName().toString());
				this.setMaxEndurance(stack, Math.round(((MobEntity)etype.create(Minecraft.getInstance().world)).getMaxHealth()*2));
			}catch(Throwable e) {
				this.setPartsTarget(stack, Mod_Elm.RegistryEvents.WANDERINGPEOPEL.getRegistryName().toString());
				this.setMaxEndurance(stack, 41);
			}
			items.add(stack);
		}
	}
}
