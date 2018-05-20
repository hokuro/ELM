package mod.elm.block;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockImoutoHead extends BlockFacingContainer {

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
	}

    public EnumImoutoHead getRotten(){
    	return rotten;
    }



    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
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
            }

            EntityLivingBase entitysnowman = null;
            if (rotten == EnumImoutoHead.ROTTEN){

            }else if (rotten == EnumImoutoHead.SKALL){

            }else{

            }

            BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            entitysnowman.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
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
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                EntityIronGolem entityirongolem = null;
                if (rotten == EnumImoutoHead.ROTTEN){

                }else if (rotten == EnumImoutoHead.SKALL){

                }else{

                }
                entityirongolem.setPlayerCreated(true);
                entityirongolem.setLocationAndAngles((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
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


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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

	}
}
