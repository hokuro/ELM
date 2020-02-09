package mod.elm.item.parts;

import java.util.Optional;

import mod.elm.entity.EntityElmIntestine;
import mod.elm.item.parts.ab.ItemElmParts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemElmIntestine extends ItemElmParts {

	public ItemElmIntestine(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote && hand == Hand.MAIN_HAND){
			int range = 20;
			Optional<EntityType<?>> etype = this.getPartTargetEntity(itemStackIn);
			if (etype.isPresent()) {
				range = Math.round(etype.get().create(worldIn).getSize(Pose.STANDING).height * 10);
			}

			ThrowableEntity stomach = new EntityElmIntestine(worldIn,playerIn,itemStackIn,range);
			playerIn.setHeldItem(hand, ItemStack.EMPTY);
			stomach.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.addEntity(stomach);
		} else {
			worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (this.random.nextFloat() * 0.4F + 0.8F));
		}
        return new ActionResult(ActionResultType.SUCCESS, itemStackIn);
    }
}
