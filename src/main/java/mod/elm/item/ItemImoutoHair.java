package mod.elm.item;

import mod.elm.core.SoundManager;
import mod.elm.potion.PotionCore;
import mod.elm.util.ModUtil;
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

public class ItemImoutoHair extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	// 距離21以内の生き物の動きを止めるをダウンさせる
		EntityLivingBase viewEntity = ModUtil.getClosestLivingEntity(21, 1.0F);
    	if (viewEntity != null && viewEntity.getUniqueID() != playerIn.getUniqueID()){
			for (Entity ent : worldIn.loadedEntityList){
				if (ent.getEntityId() == viewEntity.getEntityId()){
					viewEntity = (EntityLivingBase)ent;
				}
			}
			if (!worldIn.isRemote){
				viewEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,210,210));
				if (viewEntity instanceof EntityPlayer){
					viewEntity.addPotionEffect(new PotionEffect(PotionCore.imouto_confusion,210,0));
				}
				playerIn.getHeldItem(handIn).damageItem(1, playerIn);
			}else{
        		worldIn.playSound(playerIn, playerIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
    	}
    	return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
