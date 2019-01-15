package mod.elm.item;

import java.util.ArrayList;
import java.util.List;

import mod.elm.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemImoutoBlood extends ItemPotion {

	public ItemImoutoBlood(){
        this.setMaxStackSize(64);
        this.setHasSubtypes(false);
	}

	@Override
	 public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	 {
	        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

	        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
	        {
	        	stack.shrink(1);
	        }

	        if (!worldIn.isRemote)
	        {
	        	// 発生するエンチャントの数を決める
	        	int count = Math.min(1,ModUtil.random(4));
	        	int wcount = count;
	        	List<Potion> effect = new ArrayList<Potion>();
	        	while(wcount > 0){
	        		for(ResourceLocation res : Potion.REGISTRY.getKeys()){
	        			if (ModUtil.random(10000)<5000){
	        				Potion posion = Potion.REGISTRY.getObject(res);
	        				if (!effect.contains(posion)){
	        					effect.add(posion);
	        				}
	        				wcount--;
	        			}
	        		}
	        	}

	        	for (Potion p : effect ){
    				if (p.isInstant()){
    					int amp = ModUtil.random(10);
    					entityLiving.addPotionEffect(new PotionEffect(p,0,amp));
    				}else{
    					int amp = ModUtil.random(10);
    					int dur = ModUtil.random(120)*100 + 200;
    					entityLiving.addPotionEffect(new PotionEffect(p,dur,amp));
    				}
	        	}
	        }

	        return stack;
	    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            items.add(new ItemStack(this));
        }
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        return I18n.translateToLocal("item.imouto_blood.name");
    }

}
