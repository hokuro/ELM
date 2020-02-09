package mod.elm.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import mod.elm.core.log.ModLog;
import mod.elm.item.ItemCore;
import mod.elm.item.parts.ab.IItemElmParts;
import mod.elm.tileentity.TileEntityElmHead;
import mod.elm.util.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public class BlockElmHead extends ContainerBlock {
	private String target_name = "";
	public BlockElmHead(Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityElmHead(target_name);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityElmHead();
	}

	private static final Item[] drops = new Item[] {
			ItemCore.item_meet, ItemCore.item_brain, ItemCore.item_meet,
			ItemCore.item_eye, ItemCore.item_bone, ItemCore.item_eye,
			ItemCore.item_skin, ItemCore.item_bone, ItemCore.item_skin
	};

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		for (Item item : drops) {
			if (ModUtil.randomD() < 0.3D) {
				ItemStack stack = new ItemStack(item,1);

				((IItemElmParts)item).setPartsTarget(stack, target_name);
				ret.add(stack);
			}
		}
		return ret;
	}

	@Override
	@Deprecated
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		if(worldIn.isRemote) {
			Registry.ENTITY_TYPE.forEach(e -> {
				ModLog.log().info(e.getRegistryName().toString());
			});
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		ItemStack item = context.getItem();
		this.target_name =  EntityType.ZOMBIE.getRegistryName().toString();
		if (item.getItem() instanceof IItemElmParts) {
			this.target_name = ((IItemElmParts)item.getItem()).getPartsTarget(item);
		}

		return this.getDefaultState();
	}

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    	if (state.getBlock() != newState.getBlock()) {
//	        TileEntity tileentity = worldIn.getTileEntity(pos);
//
//	        if (tileentity instanceof IInventory) {
//	            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
//	            worldIn.updateComparatorOutputLevel(pos, this);
//	        }
	        super.onReplaced(state, worldIn, pos, newState, isMoving);
    	}
    }
}
