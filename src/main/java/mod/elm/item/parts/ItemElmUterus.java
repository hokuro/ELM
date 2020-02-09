package mod.elm.item.parts;

import java.util.Objects;
import java.util.Optional;

import mod.elm.core.Mod_Elm;
import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.sound.SoundCore;
import mod.elm.util.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ItemElmUterus extends ItemElmParts {

	public ItemElmUterus(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			ItemStack itemstack = context.getItem();
			BlockPos blockpos = context.getPos();
			Direction direction = context.getFace();
			BlockState blockstate = world.getBlockState(blockpos);

			BlockPos blockpos1;
			if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.offset(direction);
			}
			try {
				String name = getChild(itemstack);
				Optional<EntityType<?>> oetype = Registry.ENTITY_TYPE.getValue(new ResourceLocation(name));
				if (oetype.isPresent()){
					EntityType etype = oetype.get();
					if (etype.spawn(world, itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
						if (ModUtil.random_1((int)(((MobEntity)etype.create(world)).getMaxHealth()/2)) < ModUtil.random(getDMG(itemstack))) {
							if (!context.getPlayer().abilities.isCreativeMode) {
								itemstack.shrink(1);
							}
						} else {
							setDMG(itemstack, getDMG(itemstack)+1);
						}
					}
				}else{
					// i失敗音
					//world.playSound(blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundCore.SOUND_ITEM_UTERUSMISS, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
					world.playSound(context.getPlayer(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundCore.SOUND_ITEM_UTERUSMISS, SoundCategory.PLAYERS, 1.0F, 1.0F);
					//((ServerWorld)world).playSound(context.getPlayer(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundCore.SOUND_ITEM_UTERUSMISS, SoundCategory.PLAYERS, 1.0F, 1.0F);
					context.getPlayer().sendStatusMessage(new StringTextComponent("can't summon"),false);
				}
			}catch(Throwable ex) {
				context.getPlayer().sendStatusMessage(new StringTextComponent("can't summon"),false);
			}
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (worldIn.isRemote) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else {
			RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
			if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
				return new ActionResult<>(ActionResultType.PASS, itemstack);
			} else {
				BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
				BlockPos blockpos = blockraytraceresult.getPos();
				if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
					return new ActionResult<>(ActionResultType.PASS, itemstack);
				} else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
					try {
						String name = getChild(itemstack);
						Optional<EntityType<?>> oetype = Registry.ENTITY_TYPE.getValue(new ResourceLocation(name));
						if (oetype.isPresent()){
							EntityType etype = oetype.get();
							if (etype.spawn(worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false) == null) {
				                  return new ActionResult<>(ActionResultType.PASS, itemstack);
							}
							if (ModUtil.random_1((int)(((MobEntity)etype.create(worldIn)).getMaxHealth()/2)) < ModUtil.random(getDMG(itemstack))) {
								if (!playerIn.abilities.isCreativeMode) {
									itemstack.shrink(1);
								}
							} else {
								setDMG(itemstack, getDMG(itemstack)+1);
							}
							return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
						}else{
							// i失敗音
							((ServerWorld)worldIn).playSound(playerIn, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundCore.SOUND_ITEM_UTERUSMISS, SoundCategory.PLAYERS, 1.0F, 1.0F);
							playerIn.sendStatusMessage(new StringTextComponent("can't summon"),false);
							return new ActionResult<>(ActionResultType.FAIL, itemstack);
						}
					}catch(Throwable ex) {
						playerIn.sendStatusMessage(new StringTextComponent("can't summon"),false);
						return new ActionResult<>(ActionResultType.FAIL, itemstack);
					}
				} else {
					return new ActionResult<>(ActionResultType.FAIL, itemstack);
				}
			}
		}
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		ItemStack item = playerIn.getHeldItem(hand);
		World worldIn = playerIn.world;
		if (hand == Hand.MAIN_HAND) {
			if (target instanceof MobEntity) {
				// iターゲットにダメージ
				target.attackEntityFrom(new EntityDamageSource("pyupyu",playerIn).setDamageBypassesArmor().setDamageIsAbsolute(), 1);
				if (addChild(stack,target.getType().getRegistryName().toString())) {
					// i成功の音
					playerIn.playSound(SoundCore.SOUND_ITEM_UTERUSVACUME, 1.0F, 1.0F);
				} else {
					// i破裂音
					playerIn.playSound(SoundCore.SOUND_ITEM_UTERUSBREAK, 1.0F, 1.0F);
					stack = ItemStack.EMPTY;
				}
				playerIn.setHeldItem(hand, stack);
			}

			return true;
		}
		return false;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack stack = new ItemStack(this);
			this.setPartsTarget(stack, Mod_Elm.RegistryEvents.WANDERINGPEOPEL.getRegistryName().toString());
			setMaxChild(stack, ModUtil.random_1(10));
			items.add(stack);
		}
	}

	public static int getDMG(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		return nbt.getInt("dmglung");
	}

	public static void setDMG(ItemStack stack, int value) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("dmglung", value);
	}

	public static String getChild(ItemStack stack) {
		String ret = "";
		CompoundNBT nbt = stack.getOrCreateTag();
		ListNBT lst;
		if (nbt.contains("children")) {
			lst = (ListNBT)nbt.get("children");
		} else {
			return ret;
		}
		if (lst.size() > 0) {
			ret = ((CompoundNBT)lst.get(0)).getString("mobstring");
			lst.remove(0);
		}
		return ret;
	}

	public static boolean addChild(ItemStack stack, String name) {
		boolean ret = false;
		CompoundNBT nbt = stack.getOrCreateTag();
		ListNBT lst;
		if (nbt.contains("children")) {
			lst = (ListNBT)nbt.get("children");
		} else {
			lst = new ListNBT();
		}
		if (lst.size() < getMaxChild(stack)) {
			CompoundNBT nbt2 = new CompoundNBT();
			nbt2.putString("mobstring", name);
			lst.add(nbt2);
			nbt.put("children", lst);
			stack.setTag(nbt);
			ret = true;
		}
		return ret;
	}

	public static int getMaxChild(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		return nbt.getInt("maxchild");
	}

	public static void setMaxChild(ItemStack stack, int value) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("maxchild", value);
	}
}
