package mod.elm.item;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemImoutoBody extends ItemShield{
	public ItemImoutoBody()
	{
		super();
		this.setMaxDamage(021);
		this.setNoRepair();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 999999;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}


    public String getItemStackDisplayName(ItemStack stack)
    {
        return I18n.translateToLocal("item.imouto_body.name");
    }
}
