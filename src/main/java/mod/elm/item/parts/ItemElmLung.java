package mod.elm.item.parts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;

import mod.elm.core.ModCommon;
import mod.elm.core.Mod_Elm;
import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ItemElmLung extends ItemElmParts {

	private static final IItemPropertyGetter LANG_MODE = (stack, world, entity) -> {
		if (stack.getItem() instanceof ItemElmLung) {
			boolean mode = ItemElmLung.getMode(stack);
			return BooleanUtils.toInteger(mode);
		}
		return 0;
	};

	public ItemElmLung(Properties properties) {
		super(properties.maxDamage(1));
		this.addPropertyOverride(new ResourceLocation(ModCommon.MOD_ID, "lungmode"), LANG_MODE);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof LivingEntity) {
			if (((LivingEntity) entityIn).getHeldItem(Hand.MAIN_HAND) == stack) {
				boolean mode = ItemElmLung.getMode(stack);
				if (getMaxDMG(stack) <= 0) {
					Optional<EntityType<?>> etype = this.getPartTargetEntity(stack);
					float health = 10;
					if (etype.isPresent()) {
						health = ((MobEntity)etype.get().create(worldIn)).getMaxHealth();
					}
					setMaxDMG(stack, Math.round(health * health));
				}
				if (mode && !entityIn.onGround && entityIn.getMotion().y < 0) {
					// i空中にいる間落下速度を低下する
					Vec3d motion = entityIn.getMotion();
					entityIn.setMotion(motion.x*0.95, -0.1, motion.z*0.95);
					entityIn.fallDistance = 0;
					// iダメージ蓄積
					ItemElmLung.setDMG(stack, ItemElmLung.getDMG(stack)+1);
					if (ModUtil.randomF() < ((float)ItemElmLung.getDMG(stack)/(float)this.getMaxDamage(stack))) {
						setDMG(stack,0);
						stack.damageItem(1, (PlayerEntity)entityIn, (e)->{e.sendBreakAnimation(Hand.MAIN_HAND);});
						if (stack.isEmpty()) {
							((LivingEntity) entityIn).setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (handIn == Hand.MAIN_HAND) {
			boolean mode = ItemElmLung.getMode(stack);
			ItemElmLung.setMode(stack, !mode);
			return new ActionResult(ActionResultType.SUCCESS, stack);
		}
		return new ActionResult(ActionResultType.FAIL, stack);
	}

	@Override
	public ItemStack getDefaultInstance() {
		return ItemElmLung.setMode(new ItemStack(this), false);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack stack = new ItemStack(this);
			try {
				List<EntityType<?>> lst = Registry.ENTITY_TYPE.stream().filter(e-> {return (e.create(Minecraft.getInstance().world) instanceof MobEntity);}).collect(Collectors.toList());
				this.setPartsTarget(stack, lst.get(ModUtil.random(lst.size())).getRegistryName().toString());
			}catch(Throwable e) {
				this.setPartsTarget(stack, Mod_Elm.RegistryEvents.WANDERINGPEOPEL.getRegistryName().toString());
			}
			ItemElmLung.setMode(stack, false);
			items.add(stack);
		}
	}

	@Override
    public int getMaxDamage(ItemStack stack) {
        int dmg = getMaxDMG(stack);
        return dmg<=0?1:dmg;
    }

	public static boolean getMode(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt.contains("langmode")) {
			return nbt.getBoolean("langmode");
		}
		return false;
	}

	public static ItemStack setMode(ItemStack stack, boolean mode) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putBoolean("langmode",mode);
		return stack;
	}

	public static int getDMG(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		return nbt.getInt("dmglung");
	}

	public static void setDMG(ItemStack stack, int value) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("dmglung", value);
	}

	public static int getMaxDMG(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		return nbt.getInt("maxdmglung");
	}

	public static void setMaxDMG(ItemStack stack, int value) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("maxdmglung", value);
	}
}
