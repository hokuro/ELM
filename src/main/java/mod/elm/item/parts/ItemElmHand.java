package mod.elm.item.parts;

import java.util.Optional;

import mod.elm.entity.EntityElmHand;
import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.render.model.ModelLRKind;
import mod.elm.util.ModUtil;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemElmHand extends ItemElmParts {

	public ItemElmHand(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote){
			ItemStack stack = playerIn.getHeldItem(handIn);
			Optional<EntityType<?>> etype = this.getPartTargetEntity(stack);
			int range_d = 10;
			int range_w = 3;
			float strength = 2.0F;
			if (etype.isPresent()) {
				range_d = Math.round(etype.get().getSize().height * 10);
				range_w = Math.round(etype.get().getSize().width * 3);
				MobEntity mob = (MobEntity)etype.get().create(worldIn);
				strength = ModUtil.random_1((int)Math.round(mob.getMaxHealth()));
			}

			EntityElmHand handl = new EntityElmHand(worldIn, playerIn, new EntitySize(range_w,range_w,false), range_d, strength, ModelLRKind.LEFT);
			handl.setShowTime(5);
			handl.setWaiteTime(20);
			worldIn.addEntity(handl);
			EntityElmHand handr = new EntityElmHand(worldIn, playerIn, new EntitySize(range_w,range_w,false), range_d, strength, ModelLRKind.RIGHT);
			handr.setShowTime(10);
			handr.setWaiteTime(25);
			worldIn.addEntity(handr);

			if (!playerIn.isCreative()){
				playerIn.getHeldItem(handIn).shrink(1);
			}
		}
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
