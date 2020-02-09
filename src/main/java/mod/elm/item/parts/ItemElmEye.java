package mod.elm.item.parts;

import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.util.ModUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemElmEye extends ItemElmParts {

	public ItemElmEye(Item.Properties property) {
		super(property);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote){
			ItemStack stack = playerIn.getHeldItem(handIn);
			this.getPartTargetEntity(stack).ifPresent(etype -> {
				// i使用者に暗視効果
				MobEntity mob = (MobEntity)etype.create(worldIn);
				int rnd = 500;
				if (mob instanceof MonsterEntity) {
					rnd = 250;
				}
				if (ModUtil.random(1000) < rnd) {
					playerIn.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 180 * 20, 0));
					playerIn.removePotionEffect(Effects.BLINDNESS);
				} else {
					playerIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 180 * 20, 0));
					playerIn.removePotionEffect(Effects.NIGHT_VISION);
				}

				// i周囲21m以内のモブに光り輝く効果
				int range = Math.round(mob.getSize(Pose.STANDING).height * 10);
				for (LivingEntity ent : worldIn.getEntitiesWithinAABB(LivingEntity.class, playerIn.getBoundingBox().expand(new Vec3d(range,range,range)).expand(-range,-range,-range))){
					if (ent != playerIn && !(ent instanceof ArmorStandEntity)) {
						ent.addPotionEffect(new EffectInstance(Effects.GLOWING, 180 * 20, 0));
					}
				}
				if (!playerIn.isCreative()){
					playerIn.getHeldItem(handIn).shrink(1);
				}
			});
		}else{
    		worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
