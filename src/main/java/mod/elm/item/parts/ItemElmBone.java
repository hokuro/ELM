package mod.elm.item.parts;

import java.util.Optional;

import mod.elm.entity.EntityPlayerDummy;
import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.util.ModUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ItemElmBone extends ItemElmParts {

	public ItemElmBone(Properties properties) {
		super(properties);
	}


	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		ItemStack stack = context.getItem();
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();

		// i骨粉を使用したのと同じ効果
		EntityPlayerDummy dummy = new EntityPlayerDummy(world, player.getGameProfile());
		dummy.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BONE_MEAL));
		ItemUseContext bnContext = new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(context.getHitVec(), context.getFace(), context.getPos(), false));
		ActionResultType ret = Items.BONE_MEAL.onItemUse(bnContext);
		if (ret == ActionResultType.SUCCESS) {
			if (!world.isRemote &&  !player.isCreative()) {
				float rate = 0.5F;
				Optional<EntityType<?>> etype = this.getPartTargetEntity(stack);
				if (etype.isPresent()) {
					rate = Math.min(((MobEntity)etype.get().create(world)).getMaxHealth()*2, 80F);
				}

				// i一定確率で壊れる
				if (ModUtil.randomF()*100 > rate) {
					stack.shrink(1);
					if (stack.isEmpty() || stack.getCount() == 0) {
						stack = ItemStack.EMPTY;
					}
					player.setHeldItem(context.getHand(), stack);
				}
			}
		}
		return ret;
	}
}
