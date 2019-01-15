package mod.elm.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import mod.elm.entity.passive.EntityImoutoGolem;
import mod.elm.entity.passive.EntityImoutoSnowman;
import mod.elm.item.ItemCore;
import mod.elm.tileentity.TileEntityImoutoHead;
import mod.elm.util.ModUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockImoutoHead extends BlockContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger VERTICAL = PropertyInteger.create("vertical", 0, 2);
    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {
    		new AxisAlignedBB(0,		0,			0.125,		1,			0.8125,		0.875),	//N
    		new AxisAlignedBB(0,		0,			0.875,		1,			0.8125,		0.125),		//S
    		new AxisAlignedBB(0.125,	0,			0,			0.875,		0.8125,		1),			//E
    		new AxisAlignedBB(0.875,	0,			0,			0.125,		0.8125,		1),							//W

    		new AxisAlignedBB(0,		0.125,		0,			1,			0.875,		0.8125),
    		new AxisAlignedBB(0,		0.125,		0.1875,			1,			0.875,		1),
    		new AxisAlignedBB(0.1875,	0.125,		0,			1,			0.875,		1),
    		new AxisAlignedBB(0,		0.125,		0,			0.8125,		0.875,		1),

    		new AxisAlignedBB(0,		0.125,		0.1875,			1,			0.875,		1),
    		new AxisAlignedBB(0,		0.125,		0,			1,			0.875,		0.8125),
    		new AxisAlignedBB(0,		0.125,		0,			0.8125,		0.875,		1),
    		new AxisAlignedBB(0.1875,	0.125,		0,			1,			0.875,		1)}; 			// down west


    private static final Predicate<IBlockState> IS_IMOUTOHEAD = new Predicate<IBlockState>()
    {
        public boolean apply(@Nullable IBlockState p_apply_1_)
        {
            return p_apply_1_ != null && p_apply_1_.getBlock() instanceof BlockImoutoHead;
        }
    };


    private BlockPattern imoutosnowmanBasePattern;
    private BlockPattern imoutosnowmanPattern;
    private BlockPattern imoutogolemBasePattern;
    private BlockPattern imoutogolemPattern;
    private EnumImoutoHead rotten;


    protected BlockImoutoHead(EnumImoutoHead head) {
		super(BlockCore.material_imouto_block, MapColor.ADOBE);
		rotten = head;
        this.setDefaultState(this.blockState.getBaseState().withProperty(VERTICAL, 0).withProperty(FACING, EnumFacing.NORTH));
        this.setHardness(1.0F);
        this.setSoundType(SoundType.SLIME);
	}

    public EnumImoutoHead getRotten(){
    	return rotten;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }


    public static final Item[][] drops = {
    		{
    			ItemCore.item_imoutohair,
    			ItemCore.item_imoutoblain,
    			ItemCore.item_imoutoeye,
    			ItemCore.item_imoutoear,
    			ItemCore.item_imoutoskin,
    			ItemCore.item_imoutomeet},
    		{
    				ItemCore.item_imoutohair,
        			Items.ROTTEN_FLESH},
    		{
        				ItemCore.item_imoutobone,
        				ItemCore.item_imoutohair
    		}
    };
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	int idx = ModUtil.random(drops[rotten.getValue()].length);
    	Item ret;
    	if (rotten == EnumImoutoHead.NORMAL){
    		ret = drops[rotten.getValue()][idx];
    	}else if (rotten == EnumImoutoHead.ROTTEN){
    		ret = drops[rotten.getValue()][idx];
    	}else{
    		ret = drops[rotten.getValue()][idx];
    	}
        return ret;
    }

    @Override
    @Deprecated //Forge: State sensitive version
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }


    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = placer.getHorizontalFacing().getOpposite();

        try
        {
        	int vertical = 0;
        	if (((pos.getY() < placer.getPosition().getY()))){
        		vertical = 1; // UP
        	}else if (((pos.getY() > placer.getPosition().getY()))){
        		vertical = 2; // DOWN;
        	}

            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
            		.withProperty(FACING, enumfacing)
            		.withProperty(VERTICAL, vertical);
        }
        catch (IllegalArgumentException var11)
        {
            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(FACING, enumfacing).withProperty(VERTICAL,0);
        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        int vertical = state.getValue(VERTICAL);
        return AABB_BY_INDEX[getBoundingBoxIndex(enumfacing,vertical)];
    }


    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
    }

    private static int getBoundingBoxIndex(EnumFacing facing, int vertical)
    {
    	int retIdx = 0;
    	  if (facing == EnumFacing.NORTH){
          	if (vertical == 0){
          		retIdx= 0;
          	}else if (vertical == 1){
          		retIdx= 4;
          	}else{
          		retIdx= 8;
          	}
          }else if (facing == EnumFacing.SOUTH){
          	if (vertical == 0){
          		retIdx= 1;
          	}else if (vertical == 1){
          		retIdx= 5;
          	}else{
          		retIdx= 9;
          	}
          }else if (facing == EnumFacing.EAST){
          	if (vertical == 0){
          		retIdx= 2;
          	}else if (vertical == 1){
          		retIdx= 6;
          	}else{
          		retIdx= 10;
          	}
          }else if (facing == EnumFacing.WEST){
          	if (vertical == 0){
          		retIdx= 3;
          	}else if (vertical == 1){
          		retIdx= 7;
          	}else{
          		retIdx= 11;
          	}
          }
    	  return retIdx;
    }


    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(VERTICAL, Integer.valueOf((meta & 15) >> 2));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
        i = i | ((Integer)state.getValue(VERTICAL)).intValue() << 2;
        return i;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.getBlock() != this ? state : state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, VERTICAL});
    }

    public EnumFacing getFront(IBlockState state){
    	if (state.getBlock() instanceof BlockImoutoHead){
    		return state.getValue(FACING);
    	}
    	return EnumFacing.getFront(0);
    }

    public IBlockState setFront(IBlockState state, EnumFacing front){
    	if (state.getBlock() instanceof BlockImoutoHead){
    		return state.withProperty(FACING,front);
    	}
    	return state;
    }


    public boolean canDispenserPlace(World worldIn, BlockPos pos)
    {
        return this.getSnowmanBasePattern().match(worldIn, pos) != null || this.getGolemBasePattern().match(worldIn, pos) != null;
    }

    private void trySpawnGolem(World worldIn, BlockPos pos)
    {
        BlockPattern.PatternHelper blockpattern$patternhelper = this.getSnowmanPattern().match(worldIn, pos);

        if (blockpattern$patternhelper != null)
        {
            for (int i = 0; i < this.getSnowmanPattern().getThumbLength(); ++i)
            {
                BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
                worldIn.setBlockState(blockworldstate.getPos(), Blocks.AIR.getDefaultState(), 2);
                TileEntity te = worldIn.getTileEntity(blockworldstate.getPos());
                if (te != null){
                	worldIn.removeTileEntity(blockworldstate.getPos());
                }
            }

            EntityImoutoSnowman entitysnowman = new EntityImoutoSnowman(worldIn,rotten);
            BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            entitysnowman.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            entitysnowman.setRotten(rotten);
            worldIn.spawnEntity(entitysnowman);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitysnowman.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitysnowman);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int i1 = 0; i1 < this.getSnowmanPattern().getThumbLength(); ++i1)
            {
                BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(0, i1, 0);
                worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
            }
        }
        else
        {
            blockpattern$patternhelper = this.getGolemPattern().match(worldIn, pos);

            if (blockpattern$patternhelper != null)
            {
                for (int j = 0; j < this.getGolemPattern().getPalmLength(); ++j)
                {
                    for (int k = 0; k < this.getGolemPattern().getThumbLength(); ++k)
                    {
                        worldIn.setBlockState(blockpattern$patternhelper.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                        TileEntity te = worldIn.getTileEntity(blockpattern$patternhelper.translateOffset(j, k, 0).getPos());
                        if (te != null){
                        	worldIn.removeTileEntity(blockpattern$patternhelper.translateOffset(j, k, 0).getPos());
                        }
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                EntityImoutoGolem entityirongolem = new EntityImoutoGolem(worldIn,rotten);
                entityirongolem.setPlayerCreated(true);
                entityirongolem.setLocationAndAngles((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
                entityirongolem.setRotten(rotten);
                worldIn.spawnEntity(entityirongolem);

                for (EntityPlayerMP entityplayermp1 : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entityirongolem.getEntityBoundingBox().grow(5.0D)))
                {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp1, entityirongolem);
                }

                for (int j1 = 0; j1 < 120; ++j1)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double)blockpos.getX() + worldIn.rand.nextDouble(), (double)blockpos.getY() + worldIn.rand.nextDouble() * 3.9D, (double)blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                for (int k1 = 0; k1 < this.getGolemPattern().getPalmLength(); ++k1)
                {
                    for (int l1 = 0; l1 < this.getGolemPattern().getThumbLength(); ++l1)
                    {
                        BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(k1, l1, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                    }
                }
            }
        }
    }



    protected BlockPattern getSnowmanBasePattern()
    {
    	if (this.imoutosnowmanBasePattern == null){
            this.imoutosnowmanBasePattern = FactoryBlockPattern.start().aisle(" ", "#", "#")
            		.where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
    	}
        return imoutosnowmanBasePattern;
    }

    protected BlockPattern getSnowmanPattern()
    {
    	if (this.imoutosnowmanPattern == null){
            this.imoutosnowmanPattern = FactoryBlockPattern.start().aisle("^", "#", "#")
            		.where('^', BlockWorldState.hasState(IS_IMOUTOHEAD))
            		.where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
    	}
        return imoutosnowmanPattern;
    }

    protected BlockPattern getGolemBasePattern()
    {
    	if (this.imoutogolemBasePattern == null){
            this.imoutogolemBasePattern = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~")
            		.where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK)))
            		.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
    	}
        return imoutogolemBasePattern;
    }

    protected BlockPattern getGolemPattern(){
    	if (this.imoutogolemPattern == null){
            this.imoutogolemPattern = FactoryBlockPattern.start().aisle("~^~", "###", "~#~")
            		.where('^', BlockWorldState.hasState(IS_IMOUTOHEAD))
            		.where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK)))
            		.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
    	}
    	return imoutogolemPattern;
    }

	public static enum EnumImoutoHead{
		NORMAL(0),
		ROTTEN(1),
		SKALL(2);

		private int value;
		private EnumImoutoHead(int id){
			value = id;
		}

		public int getValue(){
			return value;
		}

		private static EnumImoutoHead[] values = {NORMAL,ROTTEN,SKALL};
		public static EnumImoutoHead getFromIndex(int idx){
			return values[idx];
		}

		public IBlockState getBlock(int meta) {
			IBlockState ret;
			switch(this){
			case NORMAL:
				ret = BlockCore.imouto_head_normal.getStateFromMeta(meta);
				break;
			case ROTTEN:
				ret = BlockCore.imouto_head_rotten.getStateFromMeta(meta);
				break;
			case SKALL:
				ret = BlockCore.imouto_head_skelton.getStateFromMeta(meta);
				break;
			default:
				ret = BlockCore.imouto_head_normal.getStateFromMeta(meta);
				break;

			}
			return ret;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityImoutoHead();
	}

	public static IBlockState setVertical(IBlockState state, int i) {
		return state.withProperty(VERTICAL, i);
	}

	public static IBlockState setFacing(IBlockState state, EnumFacing front) {
		return state.withProperty(FACING,front);
	}

	public static EnumFacing getFacing(IBlockState state) {
		return state.getValue(FACING);
	}
}
