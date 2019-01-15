package mod.elm.item;

import mod.elm.core.SoundManager;
import mod.elm.potion.PotionCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemImoutoEyes extends Item {
	public ItemImoutoEyes() {
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if (!worldIn.isRemote){
			playerIn.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,210,21));
			playerIn.addPotionEffect(new PotionEffect(PotionCore.imouto_confusion,2600,0));
			playerIn.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,2600,1));

			// 周囲21m以内のモブに光り輝く効果
			for (Entity ent : worldIn.loadedEntityList){
				if (ent instanceof EntityLivingBase && ent.getEntityId() != playerIn.getEntityId()){
					EntityLivingBase living = (EntityLivingBase)ent;
					if (living.getDistance(playerIn) <= 21){
						living.addPotionEffect(new PotionEffect(MobEffects.GLOWING,210,0));
					}
				}
			}
			if (!playerIn.isCreative()){
				playerIn.getHeldItem(handIn).shrink(1);
			}
		}else{
    		worldIn.playSound(playerIn, playerIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

}
