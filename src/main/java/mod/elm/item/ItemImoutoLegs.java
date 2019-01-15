package mod.elm.item;

import com.google.common.collect.Multimap;

import mod.elm.core.Mod_ElonaMobs;
import mod.elm.core.SoundManager;
import mod.elm.util.ModUtil;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemImoutoLegs extends ItemAxe {

    private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};

	public ItemImoutoLegs() {
		super(ToolMaterial.DIAMOND);
        this.attackDamage =021;
        this.attackSpeed = -0.21F;
        this.toolMaterial = Mod_ElonaMobs.materialImouto;
        this.maxStackSize = 1;
        this.setMaxDamage(Mod_ElonaMobs.materialImouto.getMaxUses());
        this.efficiency = Mod_ElonaMobs.materialImouto.getEfficiency();
        this.attackDamage =Mod_ElonaMobs.materialImouto.getAttackDamage();
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
    	// 距離21以内の生き物を21m上に蹴り飛ばす

		EntityLivingBase viewEntity = ModUtil.getClosestLivingEntity(21, 1.0F);
    	if (viewEntity != null && viewEntity.getUniqueID() != playerIn.getUniqueID()){
			for (Entity ent : worldIn.loadedEntityList){
				if (ent.getEntityId() == viewEntity.getEntityId()){
					viewEntity = (EntityLivingBase)ent;
				}
			}
			if (!worldIn.isRemote){
				BlockPos nowPos = playerIn.getPosition().offset(playerIn.getHorizontalFacing());
				for (int i = 0; i < 22; i ++){
					if (worldIn.getBlockState(nowPos.offset(EnumFacing.UP)).getMaterial() == Material.AIR){
						nowPos = nowPos.offset(EnumFacing.UP);
					}else{
						break;
					}
				}
				viewEntity.setPositionAndRotation(nowPos.getX(), nowPos.getY(), nowPos.getZ(), viewEntity.rotationYaw, viewEntity.rotationPitch);
				playerIn.getHeldItem(handIn).damageItem(1, playerIn);
			}else{
        		worldIn.playSound(playerIn, playerIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
    	}
    	return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
