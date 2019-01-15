package mod.elm.item;

import mod.elm.core.SoundManager;
import mod.elm.entity.mob.EntityImoutoWanko;
import mod.elm.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemImoutoBone extends Item {
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if (!worldIn.isRemote){
			// わんこに使うとダメージを与える
			if (Minecraft.getMinecraft().objectMouseOver != null &&
					Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY){
					if ( Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityWolf){
						EntityWolf wolf = (EntityWolf) Minecraft.getMinecraft().objectMouseOver.entityHit;
						wolf.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), 5.0F);
						if (wolf.isDead && ModUtil.random(10000) < 1000){
							// 死んだ場合10%の確率で妹犬をスポーンさせる
							EntityImoutoWanko wanko = new EntityImoutoWanko(worldIn);
							wanko.setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
							//worldIn.spawnEntity(wanko);
						}
					}
				}
		}else{
			worldIn.playSound(playerIn, playerIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
