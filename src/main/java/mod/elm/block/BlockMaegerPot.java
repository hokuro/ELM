package mod.elm.block;

import javax.annotation.Nullable;

import mod.elm.inventory.ContainerMaergerPot;
import mod.elm.item.ItemCore;
import mod.elm.tileentity.TileEntityMaergerPot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockMaegerPot extends ContainerBlock {

	protected final VoxelShape collisionShapes;
	protected final VoxelShape shapes;
	protected BlockMaegerPot(Properties builder) {
		super(builder);
		this.collisionShapes = this.makeShapes();
		this.shapes = this.makeShapes();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (!playerIn.isSneaking()) {
			ItemStack item = playerIn.getHeldItem(handIn);
			TileEntity ent = worldIn.getTileEntity(pos);
			if (item.getItem() == ItemCore.item_blood) {
				// i血液を継ぎ足す
				if (ent instanceof TileEntityMaergerPot) {
					item = ((TileEntityMaergerPot)ent).addBlood(item);
					if (!worldIn.isRemote) {
						playerIn.setHeldItem(handIn, item);
					}
				}
				return true;
			} else if (item.getItem() == Items.FLINT_AND_STEEL) {
				if (ent instanceof TileEntityMaergerPot) {
					if (((TileEntityMaergerPot)ent).start()) {
						//worldIn.playSound(playerIn, pos, SoundCore.SOUND_BLOCK_START_MAERGERPOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
					} else {
						//worldIn.playSound(playerIn, pos, SoundCore.SOUND_BLOCK_MISS_MAERGERPOT, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
					playerIn.getHeldItem(handIn).damageItem(1, playerIn, (e)->{e.sendBreakAnimation(handIn);});
				}
			} else if (handIn == Hand.MAIN_HAND) {
				if (!worldIn.isRemote) {
					NetworkHooks.openGui((ServerPlayerEntity)playerIn,
							new INamedContainerProvider() {
								@Override
								@Nullable
								public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
									TileEntity ent = worldIn.getTileEntity(pos);
									if (ent instanceof TileEntityMaergerPot && !((TileEntityMaergerPot) ent).isRun()) {
										return new ContainerMaergerPot(id, playerInv, ent);
									}
									return null;
								}

								@Override
								public ITextComponent getDisplayName() {
									return new TranslationTextComponent("container.elm.maergerpot");
								}
							},
							(buf)->{
								buf.writeInt(pos.getX());
								buf.writeInt(pos.getY());
								buf.writeInt(pos.getZ());
							});
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityMaergerPot();
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof IInventory) {
					InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
					worldIn.updateComparatorOutputLevel(pos, this);
			}
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	protected VoxelShape makeShapes() {
		VoxelShape voxelshape = Block.makeCuboidShape(2, 0, 2 , 14, 1, 14);
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(2, 1, 1 , 14, 2, 2));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(2, 1, 14 , 14, 2, 15));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(14, 1, 2 , 15, 2, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(1, 1, 2 , 2, 2, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(2, 2, 0 , 14, 10, 1));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(15, 2, 2 , 16, 10, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(2, 2, 15 , 14, 10, 16));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(0, 2, 2 , 1, 10, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(1, 2, 1 , 2, 10, 2));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(14, 2, 1 , 15, 10, 2));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(14, 2, 14 , 15, 10, 15));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(1, 2, 14 , 2, 10, 15));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(2, 10, 1 , 14, 11, 2));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(14, 10, 2 , 15, 11, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(2, 10, 14 , 14, 11, 15));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(1, 10, 2 , 2, 11, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(12, 11, 2 , 14, 12, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(2, 11, 2 , 4, 12, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(4, 11, 12 , 12, 12, 14));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(4, 11, 2 , 12, 12, 4));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(11, 12, 4 , 12, 13, 12));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(4, 12, 4 , 5, 13, 12));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(5, 12, 11 , 11, 13, 12));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(5, 12, 4 , 11, 13, 5));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(5, 13, 5 , 11, 15, 6));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(5, 13, 6 , 6, 15, 10));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(10, 13, 6 , 11, 15, 10));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(5, 13, 10 , 11, 15, 11));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(11, 15, 4 , 12, 16, 12));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(5, 15, 4 , 11, 16, 5));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(4, 15, 4 , 5, 16, 12));
		voxelshape = VoxelShapes.or(voxelshape,Block.makeCuboidShape(5, 15, 11 , 11, 16, 12));
		return voxelshape;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.shapes;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.collisionShapes;
	}

	@Override
	public boolean hasCustomBreakingProgress(BlockState state) {
		return true;
	}
}
