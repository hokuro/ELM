package mod.elm.item.parts;

import java.util.Optional;

import mod.elm.entity.EntityElmFoot;
import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.render.model.ModelLRKind;
import mod.elm.sound.SoundCore;
import mod.elm.util.ModUtil;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemElmFoot extends ItemElmParts {
	public ItemElmFoot(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote){
			ItemStack stack = playerIn.getHeldItem(handIn);
			Optional<EntityType<?>> etype = this.getPartTargetEntity(stack);
			int damage = ModUtil.random_1(20);
			int range_w = 3;
			if (etype.isPresent()) {
				MobEntity ent = (MobEntity)etype.get().create(worldIn);
				damage = ModUtil.random_1((int)Math.round(ent.getMaxHealth()));
				range_w = Math.round(Math.max(etype.get().getWidth(), etype.get().getHeight()) * 3);
			}

			EntityElmFoot foot1 = new EntityElmFoot(worldIn, playerIn, new EntitySize(range_w,3.0F,false), ModelLRKind.LEFT, damage);
			foot1.setShowTime(5);
			foot1.setWaiteTime(20);
			worldIn.addEntity(foot1);
			EntityElmFoot footr = new EntityElmFoot(worldIn, playerIn, new EntitySize(range_w,3.0F,false), ModelLRKind.RIGHT, damage);
			footr.setShowTime(10);
			footr.setWaiteTime(25);
			worldIn.addEntity(footr);
			if (!playerIn.isCreative()){
				playerIn.getHeldItem(handIn).shrink(1);
			}
		}else{
    		worldIn.playSound(playerIn, playerIn.getPosition(), SoundCore.SOUND_ITEM_FOOTPRESS, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
