package mod.elm.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemImoutoTangue extends Item {
    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	if (!worldIn.isRemote){
    		playerIn.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH,0,21));
    		playerIn.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,210,21));
    		playerIn.addPotionEffect(new PotionEffect(MobEffects.HASTE,210,21));
    		playerIn.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,210,21));
    		playerIn.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY,210,21));
    		if (!playerIn.isCreative()){
    			playerIn.getHeldItem(hand).shrink(1);
    		}
    	}
       	return EnumActionResult.SUCCESS;
    }
}
