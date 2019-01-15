package mod.elm.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemImoutoHart extends ItemFood {

	public ItemImoutoHart() {
    	super(40,40,false);

	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,14400,999));
		player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION,0,999));
		player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH,0,999));
	}
}