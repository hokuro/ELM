package mod.elm.item.parts;

import java.util.Optional;

import mod.elm.entity.EntityElmStomach;
import mod.elm.item.parts.ab.ItemElmParts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemElmStmach extends ItemElmParts {

	public ItemElmStmach(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote){
			Optional<EntityType<?>> etype = this.getPartTargetEntity(itemStackIn);
			int time = 30;
			if (etype.isPresent()) {
				float size = etype.get().getSize().height + etype.get().getSize().width;
				time = Math.round(size * size) * 10;
			}

			ThrowableEntity stomach = new EntityElmStomach(worldIn,playerIn, time);
			if (!playerIn.isCreative()){
				itemStackIn.shrink(1);
			}
			stomach.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.addEntity(stomach);
		} else {
			worldIn.playSound((PlayerEntity)playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (this.random.nextFloat() * 0.4F + 0.8F));
		}
        return new ActionResult(ActionResultType.SUCCESS, itemStackIn);
    }
}
