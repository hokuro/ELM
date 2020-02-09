package mod.elm.item.parts;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import mod.elm.entity.EntityElmBrain;
import mod.elm.item.parts.ab.ItemElmSwordParts;
import mod.elm.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemElmBrain extends ItemElmSwordParts {
	protected float attackATK;
	protected int attackTick;
	public ItemElmBrain(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (handIn == Hand.MAIN_HAND) {
			if (!worldIn.isRemote) {
				// iダメージを決める
				int damage = getMaxAttackDamage(stack);
				if (damage == 0) {
					Optional<EntityType<?>> etype = this.getPartTargetEntity(stack);
					if (etype.isPresent()){
						MobEntity mob = (MobEntity)etype.get().create(worldIn);
						damage = Math.round(mob.getMaxHealth());
					}
				}

				// iエンティティ召喚
				EntityElmBrain brain = new EntityElmBrain(worldIn, playerIn, damage);
				worldIn.addEntity(brain);

				// iアイテムを手放す
				if (!playerIn.isCreative()) {
					playerIn.setHeldItem(handIn, ItemStack.EMPTY);
					stack = ItemStack.EMPTY;
				}
			}
			return new ActionResult(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult(ActionResultType.SUCCESS, stack);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		ItemStack item = playerIn.getHeldItem(hand);
		World worldIn = playerIn.world;
		if (hand == Hand.MAIN_HAND) {
			if (!worldIn.isRemote) {
				if (target instanceof MobEntity || target instanceof PlayerEntity) {
					if (checkHelm(target)) {
						// iダメージを決める
						int damage = getMaxAttackDamage(stack);
						Optional<EntityType<?>> etype = this.getPartTargetEntity(item);
						if (etype.isPresent()){
							MobEntity mob = (MobEntity)etype.get().create(worldIn);
							damage = Math.round(mob.getMaxHealth());
						}

						// iエンティティ召喚
						EntityElmBrain brain = new EntityElmBrain(worldIn, playerIn, damage, target);
						worldIn.addEntity(brain);

						// iアイテムを手放す
						if (!playerIn.isCreative()) {
							playerIn.setHeldItem(hand, ItemStack.EMPTY);
						}
					} else {
						// i取り付けに失敗したら壊れる
						item.damageItem(100, playerIn, (pl)->{pl.sendBreakAnimation(hand);});
					}
				}
			}
			return true;
		}
		return false;
	}

	private boolean checkHelm(LivingEntity target) {
		boolean ret = true;
		Iterator<ItemStack> it = target.getArmorInventoryList().iterator();
		while(it.hasNext() && ret) {
			ItemStack item = it.next();
			// i頭装備をつけている場合は取り付けない
			if (item.getItem() instanceof ArmorItem &&  ((ArmorItem)item.getItem()).getEquipmentSlot() == EquipmentSlotType.HEAD) {
				ret = false;
			}
		}
		return ret;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int indexOfMainSlot, boolean isCurrent) {
		if (entity instanceof LivingEntity) {
			if (((LivingEntity)entity).getHeldItemMainhand().getItem() == this) {
				// i5秒ごとに攻撃力が変わる
				int tick = ItemElmBrain.getTick(stack);
				if ((attackTick % MAX_ATTACKTIC) == 0) {
					int damage = getMaxAttackDamage(stack);
					if (damage == 0) {
						// i最大攻撃力が設定されていなければ持ち主の現在HPが最大攻撃力
						damage = Math.round(((LivingEntity) entity).getHealth());
					}
					// i設定されているモブの最大体力までの範囲でランダム攻撃力
					attackATK = ModUtil.random_1(damage);
					if (entity instanceof LivingEntity){
						updateAttackAmplifier(stack,(LivingEntity)entity);
					}
				}
				attackTick++;
				//ItemElmBrain.setTick(stack, tick);
			}
		}
	}

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EquipmentSlotType.MAINHAND){
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackATK, AttributeModifier.Operation.ADDITION));
        }

        return multimap;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		int max = getMaxAttackDamage(stack);
		// i攻撃力の割合分ダメージを受ける
		return Math.max(1, Math.round((float)getMaxDamage() * ((this.attackATK/(float)max))));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
    	if (this.getPartTargetEntity(stack).isPresent()) {
    		int ret = this.getMaxEndurance(stack);
    		if (ret < 0) {
    			ret = Math.round(((MobEntity)this.getPartTargetEntity(stack).get().create(Minecraft.getInstance().world)).getMaxHealth() * 2);
    			this.setMaxEndurance(stack, ret);
    		}
    		return ret;
    	}
    	return stack.getItem().getMaxDamage();
    }


    public static float getAttackDamage(ItemStack stack){
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.contains("AttackDamage")){
        	return tag.getFloat("AttackDamage");
        }
        return 0.0F;
    }

    public static void setAttackDamage(ItemStack stack, float damager){
    	CompoundNBT tag = stack.getOrCreateTag();
    	tag.putFloat("AttackDamage", damager);
    }


    public static final int MAX_ATTACKTIC = 5 * 20;
    public static int getTick(ItemStack stack) {
    	CompoundNBT tag = stack.getOrCreateTag();
    	return tag.getInt("attackTick");
    }

    public static void setTick(ItemStack stack, int value) {
    	CompoundNBT tag = stack.getOrCreateTag();
    	tag.putInt("attackTick", value>=MAX_ATTACKTIC?0:value);
    }

    public static int getMaxAttackDamage(ItemStack stack) {
    	CompoundNBT tag = stack.getOrCreateTag();
    	if (tag.contains("maxattackdamage")) {
    		return tag.getInt("maxattackdamage");
    	}
    	return 0;
    }

    public static void setMaxAttackDamage(ItemStack stack, MobEntity entity) {
    	CompoundNBT tag = stack.getOrCreateTag();
    	tag.putInt("maxattackdamage", Math.round(entity.getMaxHealth()));
    }

    protected void updateAttackAmplifier(@Nonnull ItemStack itemStack, @Nonnull LivingEntity player) {
        CompoundNBT CompoundNBT = itemStack.getOrCreateTag();
        ListNBT nbtTagList = new ListNBT();
        nbtTagList.add(
                getAttrTag(
                        SharedMonsterAttributes.ATTACK_DAMAGE.getName()
                        , new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackATK, AttributeModifier.Operation.ADDITION)
                        , EquipmentSlotType.MAINHAND)
        );
        CompoundNBT.put("AttributeModifiers",nbtTagList);
        player.getAttributes().removeAttributeModifiers(itemStack.getAttributeModifiers(EquipmentSlotType.MAINHAND));
        player.getAttributes().applyAttributeModifiers(itemStack.getAttributeModifiers(EquipmentSlotType.MAINHAND));
    }

    public CompoundNBT getAttrTag(String attrName , AttributeModifier par0AttributeModifier, EquipmentSlotType slot) {
        CompoundNBT CompoundNBT = new CompoundNBT();
        CompoundNBT.putString("AttributeName",attrName);
        CompoundNBT.putString("Name", par0AttributeModifier.getName());
        CompoundNBT.putDouble("Amount", par0AttributeModifier.getAmount());
        CompoundNBT.putInt("Operation", par0AttributeModifier.getOperation().getId());
        CompoundNBT.putUniqueId("UUID",par0AttributeModifier.getID());
        CompoundNBT.putString("Slot", slot.getName());
        return CompoundNBT;
    }
}
