package mod.elm.item;

import mod.elm.core.Mod_ElonaMobs;
import mod.elm.core.SoundManager;
import mod.elm.potion.PotionCore;
import mod.elm.util.ModUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemImoutoBlain extends ItemPickaxe {
	public static final String NAME = "imoutobrain";

	public ItemImoutoBlain() {
		super(Mod_ElonaMobs.materialImouto);
		// 修復不可
		this.setNoRepair();
	}

    public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        return 999.9F;
    }

	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
    	// モブに攻撃しても耐久減少は1にする
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	// 見ているモブの身体能力をダウンさせる
		EntityLivingBase viewEntity = ModUtil.getClosestLivingEntity(21, 1.0F);
    	if (viewEntity != null && viewEntity.getUniqueID() != playerIn.getUniqueID()){
			for (Entity ent : worldIn.loadedEntityList){
				if (ent.getEntityId() == viewEntity.getEntityId()){
					viewEntity = (EntityLivingBase)ent;
				}
			}
			if (!worldIn.isRemote){
				if (viewEntity instanceof EntityPlayer){
					viewEntity.addPotionEffect(new PotionEffect(PotionCore.imouto_confusion,666,0));
				}
				viewEntity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,210,21));
				viewEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,210,21));
				viewEntity.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,210,21));
				playerIn.getHeldItem(handIn).damageItem(1, playerIn);
			}else{
        		worldIn.playSound(playerIn, playerIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
    	}
    	return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}