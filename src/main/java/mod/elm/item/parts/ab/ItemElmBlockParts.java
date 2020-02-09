package mod.elm.item.parts.ab;

import java.util.List;
import java.util.stream.Collectors;

import mod.elm.core.Mod_Elm;
import mod.elm.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;

public class ItemElmBlockParts extends BlockItem implements IItemElmParts {

	public ItemElmBlockParts(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		String name = this.getPartTargetName(stack);
		return name + "'s " + I18n.format(this.getTranslationKey());
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack stack = new ItemStack(this);
			try {
				List<EntityType<?>> lst = Registry.ENTITY_TYPE.stream().filter(e-> {return (e.create(Minecraft.getInstance().world) instanceof MobEntity);}).collect(Collectors.toList());
				this.setPartsTarget(stack, lst.get(ModUtil.random(lst.size())).getRegistryName().toString());
			}catch(Throwable e) {
				this.setPartsTarget(stack, Mod_Elm.RegistryEvents.WANDERINGPEOPEL.getRegistryName().toString());
			}
			items.add(stack);
		}
	}
}
