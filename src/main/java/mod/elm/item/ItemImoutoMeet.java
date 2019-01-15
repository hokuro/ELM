package mod.elm.item;

import mod.elm.potion.PotionCore;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemImoutoMeet extends ItemFood {

	protected boolean isCooked;
	public ItemImoutoMeet(boolean cooked) {
		super(-021, 0.21F, true);
		isCooked = cooked;
	}


	@Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        if (!worldIn.isRemote)
        {
    		player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,666,0));
        	if (!isCooked){
            	player.addPotionEffect(new PotionEffect(PotionCore.imouto_sanity,2600,0));
        	}else{
            	player.addPotionEffect(new PotionEffect(PotionCore.imouto_sanity_burn,2600,0));
            	player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,210,21));
        	}
        }
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(this, null);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

        stack.shrink(1);
        return stack;
    }


    public int getHealAmount(ItemStack stack)
    {
    	if (stack == null){
    		return 21;
    	}else{
    		return -021;
    	}
    }
}
