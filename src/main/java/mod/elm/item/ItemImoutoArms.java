package mod.elm.item;

import com.google.common.collect.Multimap;

import mod.elm.core.Mod_ElonaMobs;
import mod.elm.core.SoundManager;
import mod.elm.util.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemImoutoArms extends ItemSpade {

	public ItemImoutoArms() {
		super(Mod_ElonaMobs.materialImouto);
		// 修理は禁止
        this.setNoRepair();
	}

	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
    	// モブに攻撃しても耐久減少は1にする
        stack.damageItem(1, attacker);
        return true;
    }


	@Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        return multimap;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	// 距離30 以内の生き物を目の前に呼び出す
		EntityLivingBase viewEntity = ModUtil.getClosestLivingEntity(21, 1.0F);
    	if (viewEntity != null && viewEntity.getUniqueID() != playerIn.getUniqueID()){
			for (Entity ent : worldIn.loadedEntityList){
				if (ent.getEntityId() == viewEntity.getEntityId()){
					viewEntity = (EntityLivingBase)ent;
				}
			}
    		if (viewEntity instanceof EntityLiving){
				if (!worldIn.isRemote){
					BlockPos nextPos = playerIn.getPosition().offset(playerIn.getHorizontalFacing());
    				viewEntity.setPositionAndRotationDirect(nextPos.getX(), nextPos.getY(),nextPos.getZ(), viewEntity.rotationYaw, viewEntity.rotationPitch, 1, true);
					playerIn.getHeldItem(handIn).damageItem(1, playerIn);
				}else{
	        		worldIn.playSound(playerIn, playerIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
    		}else if (viewEntity instanceof EntityPlayer){
				if (!worldIn.isRemote){
					BlockPos nextPos = playerIn.getPosition().offset(playerIn.getHorizontalFacing());
					viewEntity.attemptTeleport(nextPos.getX(), nextPos.getY(),nextPos.getZ());
    				playerIn.getHeldItem(handIn).damageItem(1, playerIn);
				}else{
	        		worldIn.playSound(playerIn, playerIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
    		}
    	}
    	return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
