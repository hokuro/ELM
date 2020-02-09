package mod.elm.item.parts;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import mod.elm.core.Mod_Elm;
import mod.elm.item.ItemCore;
import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.sound.SoundCore;
import mod.elm.util.ModUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ItemElmHart extends ItemElmParts {
	public static final String NAME_HEARTPRESS_DAMAGE = "elm_heartpress";


	public ItemElmHart(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (handIn == Hand.MAIN_HAND) {
			ItemStack item = playerIn.getHeldItem(handIn);
			if (!worldIn.isRemote ) {
				// i血液ぶっしゃー
				ItemStack blood = new ItemStack(ItemCore.item_blood,1);

				String targetName = "";
				// i視線が通っているモブの血液をぶっしゅー
				Vec3d look = playerIn.getLookVec();
				Vec3d next = look.add(playerIn.posX, playerIn.posY, playerIn.posZ);
				while("".equals(targetName) && Math.sqrt(playerIn.getDistanceSq(next)) <= 20) {
					List<LivingEntity> entitys = worldIn.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(next.x-2, next.y -2, next.z -2, next.x +2, next.y + 2, next.z +2));
					for (LivingEntity ent : entitys) {
						if ((ent instanceof MobEntity || ent instanceof PlayerEntity) && (ent != playerIn)) {
							if (!(ent instanceof PlayerEntity)) {
								targetName = ent.getType().getRegistryName().toString();
								next = next.scale(20);
							}
							ent.attackEntityFrom(new EntityDamageSource(NAME_HEARTPRESS_DAMAGE, playerIn).setDamageBypassesArmor().setDamageIsAbsolute(), 2);
							break;
						}
					}
					next = next.add(look.x, look.y, look.z);
				}

				if ("".equals(targetName)) {
					// i誰にも視線が通っていなければ誰かの血をぶっしゅー
					List<EntityType<?>> entitylist = Registry.ENTITY_TYPE.stream().filter(e -> {
						if (e.create(worldIn) instanceof MobEntity) {
							return true;
						}
						return false;
					}).collect(Collectors.toList());
					int index = ModUtil.random(entitylist.size());
					targetName = entitylist.get(index).getRegistryName().toString();
					// i血抜きによるダメージ
					playerIn.attackEntityFrom(new DamageSource(NAME_HEARTPRESS_DAMAGE).setDamageBypassesArmor().setDamageIsAbsolute(), 2);
				}
				this.setPartsTarget(blood,targetName);
				ItemElmBlood.setRandomPotionEffect(blood, ItemElmBlood.DEFAULT_EFFECT_NUM);
				ModUtil.spawnItemStack(worldIn, playerIn.posX + playerIn.getHorizontalFacing().getXOffset(), playerIn.posY, playerIn.posZ + playerIn.getHorizontalFacing().getZOffset(), blood, Mod_Elm.rnd);
				ItemElmHart.growBloodCount(item, 1);
				item.damageItem(1, playerIn, (pl)->{pl.sendBreakAnimation(handIn);});
			} else {
				playerIn.playSound(SoundCore.SOUND_ITEM_HARTPRESS, 1.0F, 1.0F);
			}
			return new ActionResult(ActionResultType.SUCCESS, item);
		}
		return new ActionResult(ActionResultType.PASS, playerIn.getHeldItem(handIn));
	}

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		int cnt = ItemElmHart.getBloodCount(stack);
		return ModUtil.random_1(400) * (cnt+1);
	}

	public static int getBloodCount(ItemStack stack) {
		return stack.getOrCreateTag().getInt("bloodcount");
	}

	public static void setBloodCount(ItemStack stack, int value) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("bloodcount",value);
	}

	public static void growBloodCount(ItemStack stack, int value) {
		ItemElmHart.setBloodCount(stack, ItemElmHart.getBloodCount(stack) + value);
	}

	public static void shrinkBloodCount(ItemStack stack, int value) {
		ItemElmHart.growBloodCount(stack, -1 * value);
	}
}
