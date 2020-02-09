package mod.elm.item.body;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import mod.elm.item.parts.ab.ItemElmToolParts;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemElmArm extends ItemElmToolParts {
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.RED_SAND, Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER);
	protected static final Map<Block, BlockState> field_195955_e = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.GRASS_PATH.getDefaultState()));

	public ItemElmArm(IItemTier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builder) {
		super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON, builder);
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
		Block block = blockIn.getBlock();
		return block == Blocks.SNOW || block == Blocks.SNOW_BLOCK;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		if (context.getFace() != Direction.DOWN && world.getBlockState(blockpos.up()).isAir(world, blockpos.up())) {
			BlockState blockstate = field_195955_e.get(world.getBlockState(blockpos).getBlock());
			if (blockstate != null) {
				PlayerEntity playerentity = context.getPlayer();
				world.playSound(playerentity, blockpos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
				if (!world.isRemote) {
					world.setBlockState(blockpos, blockstate, 11);
					if (playerentity != null) {
						context.getItem().damageItem(1, playerentity, (p_220041_1_) -> {
							p_220041_1_.sendBreakAnimation(context.getHand());
						});
					}
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (Hand.OFF_HAND == handIn) {
			ItemStack item = playerIn.getHeldItem(handIn);
			if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.SILK_TOUCH, playerIn) <= 0) {
				Map<Enchantment, Integer> enc = EnchantmentHelper.getEnchantments(item);
				enc.put(Enchantments.SILK_TOUCH, 1);
				EnchantmentHelper.setEnchantments(enc, item);
				playerIn.setHeldItem(handIn, item);
			}
			return new ActionResult(ActionResultType.SUCCESS, item);
		}
		return new ActionResult(ActionResultType.PASS, playerIn.getHeldItem(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		Map<Enchantment, Integer>  enc = Maps.newHashMap();
		enc.put(Enchantments.SILK_TOUCH , 1);
		EnchantmentHelper.setEnchantments(enc, stack);
		return stack;
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
}
