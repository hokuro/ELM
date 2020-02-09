package mod.elm.creative;

import mod.elm.item.ItemCore;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupElm extends ItemGroup {

	public ItemGroupElm(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		// TODO 自動生成されたメソッド・スタブ
		return new ItemStack(ItemCore.item_mobanalyzer);
	}

	@Override
	public String getTranslationKey() {
		return "ELM Item";
	}
}
