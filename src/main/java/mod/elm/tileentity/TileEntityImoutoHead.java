package mod.elm.tileentity;

import mod.elm.block.BlockCore;
import mod.elm.block.BlockImoutoHead;
import mod.elm.block.BlockImoutoHead.EnumImoutoHead;
import mod.elm.core.SoundManager;
import mod.elm.util.ModUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;

public class TileEntityImoutoHead extends TileEntity implements ITickable{

	public static final String NAME = "tileentityimoutohead";
	private EnumImoutoHead rotten = null;
	private int nextCheckTime;
	private int chg_time;
	private int checkCount;

	public TileEntityImoutoHead(){
		rotten = null;
		nextCheckTime = ModUtil.random(5001)+1000;
		chg_time = 0;
		checkCount = 0;
	}

	private boolean test_1=false;
	private boolean test_2=false;
	private boolean test_3 = false;
	private boolean test_4 = false;
	@Override
	public void update() {
		IBlockState state = this.world.getBlockState(this.pos);
		if (state != null && state.getBlock() instanceof BlockImoutoHead){
			if (rotten == null){
				if (state.getBlock() instanceof BlockImoutoHead){
					rotten = ((BlockImoutoHead)state.getBlock()).getRotten();
				}
			}
			chg_time++;

			if (chg_time%100 == 0){
				if (!world.isRemote){
					if (ModUtil.random(10000) < 1000){
						// 1秒ごとに10%の確率でプレイヤーのいる方向に正面を向ける
						searchPlayer(state);
					}

					if (ModUtil.random(1000000) < 1){
						// 1秒ごとに1/1000000の確率でプレイヤーのいる方向へ移動
						movePlayer(state);
					}
					if (ModUtil.random(10000) < 300){
						// 1秒ごとに0.01%の確率でチャットメッセージ表示
						chatPlayer(state);
					}
				}else{
					if (ModUtil.random(10000) < 300){
						// 1秒ごとに0.03%の確率で鳴き声を再生
						soundPlayer(state);
					}
				}

			}
			if (chg_time >= nextCheckTime){
				if (!world.isRemote){
					if (rotten != EnumImoutoHead.SKALL){
						if (ModUtil.random(Math.max(1000-100*checkCount,100)) < Math.min(50 + 10 * ((checkCount<10?0:(checkCount-10))),90)){
							rottenChange(state);
						}
					}
					checkCount++;
				}
				chg_time = 0;
				nextCheckTime = ModUtil.random(5001)+1000;
			}
		}else{
			world.removeTileEntity(pos);
		}
	}

	private void rottenChange(IBlockState state){
		int meta = state.getBlock().getMetaFromState(state);
		IBlockState nextHead;
		if (rotten == EnumImoutoHead.NORMAL){
			nextHead = BlockCore.imouto_head_rotten.getStateFromMeta(meta);
		}else{
			nextHead = BlockCore.imouto_head_skelton.getStateFromMeta(meta);
		}
		state.getBlock().breakBlock(world, pos, state);
		world.setBlockToAir(pos);
		world.setBlockState(pos, nextHead);
	}

	private void searchPlayer(IBlockState state) {
		EntityPlayer player = getNearlyPlayer(state, 100);
		IBlockState nextState = state;
		if (player != null){
			double x =  player.posX - pos.getX();
			double y =  player.posY - pos.getY();
			double z =  player.posZ - pos.getZ();
			double distance = Math.sqrt(
					Math.pow(pos.getX()-player.posX,2)+
					Math.pow(pos.getY()-player.posY,2)+
					Math.pow(pos.getZ()-player.posZ,2));
			double hdeg = (Math.atan2(z,x)*180)/Math.PI;

			if (hdeg < 0){hdeg = 360+hdeg;}
			if (hdeg < 45 || hdeg > 315){
				nextState=BlockImoutoHead.setFacing(nextState,EnumFacing.EAST);
			}else if ( hdeg < 135 && hdeg > 45){
				nextState=BlockImoutoHead.setFacing(nextState,EnumFacing.SOUTH);
			}else if ( hdeg < 225 && hdeg > 135){
				nextState=BlockImoutoHead.setFacing(nextState,EnumFacing.WEST);
			}else if (hdeg < 315 && hdeg > 225){
				nextState=BlockImoutoHead.setFacing(nextState,EnumFacing.NORTH);
			}

			double xyDeg = (Math.atan2(y, x)*180)/Math.PI;
			double zyDeg = (Math.atan2(y, z)*180)/Math.PI;
			if (xyDeg < 0){xyDeg = 360 + xyDeg;}
			if (zyDeg < 0){zyDeg = 360 + zyDeg;}

			if (xyDeg > 75 && xyDeg < 105 && zyDeg > 75 && zyDeg <105){
				nextState = ((BlockImoutoHead)state.getBlock()).setVertical(nextState,1);
			}else if(xyDeg > 255 && xyDeg < 285 && zyDeg > 255 && zyDeg <285){
				nextState = ((BlockImoutoHead)state.getBlock()).setVertical(nextState,2);
			}else{
				nextState = ((BlockImoutoHead)state.getBlock()).setVertical(nextState,0);
			}

			if (nextState.getBlock().getMetaFromState(nextState) != state.getBlock().getMetaFromState(state)){
				// プレイヤーがこちらを見ているときは動かない
				if (player.getHorizontalFacing() != BlockImoutoHead.getFacing(nextState).getOpposite()){
					// 更新
					world.setBlockState(pos, nextState);
				}
			}
		}
	}

	private static final String[][] chatImoutoHead = {
			{"お兄ちゃん!","お兄ちゃん～お兄ちゃんっ!","お兄ちゃん?","お兄ちゃん♪","お兄ちゃん♪お兄ちゃん♪","お兄ちゃんー","お兄ちゃんーお兄ちゃんー","お兄ちゃん☆","お兄ちゃんっ!","お兄ちゃんー","お兄ちゃん？お兄ちゃん！お兄ちゃんっ!"},
			{"おにいちゃん!","おにいちゃん～おにいちゃんっ!","おにいちゃん？","おにいちゃん♪","おにいちゃん♪おにいちゃん♪","おにいちゃんー","おにいちゃんーおにいちゃんー","おにいちゃん☆","おにいちゃんっ!","おにいちゃんー","おにいちゃん？おにいちゃん！おにいちゃんっ!"},
			{"ｵﾆｲﾁｬﾝ!","ｵﾆｲﾁｬﾝ~ｵﾆｲﾁｬﾝｯ!","ｵﾆｲﾁｬﾝ?","ｵﾆｲﾁｬﾝ♪","ｵﾆｲﾁｬﾝ♪ｵﾆｲﾁｬﾝ♪","ｵﾆｲﾁｬﾝｰ","ｵﾆｲﾁｬﾝｰｵﾆｲﾁｬﾝｰ","ｵﾆｲﾁｬﾝ☆","ｵﾆｲﾁｬﾝｯ!","ｵﾆｲﾁｬﾝｰ","ｵﾆｲﾁｬﾝ?ｵﾆｲﾁｬﾝ!ｵﾆｲﾁｬﾝｯ!"}
	};
	private void chatPlayer(IBlockState state) {
		EntityPlayer player = getNearlyPlayer(state, 10);
		if (player != null){
			int txt = ModUtil.random(chatImoutoHead[rotten.getValue()].length);
			player.sendStatusMessage(new TextComponentString(chatImoutoHead[rotten.getValue()][txt]),false);
		}
	}

	private void movePlayer(IBlockState state) {
		EntityPlayer player = getNearlyPlayer(state, 100);
		BlockPos nextPos = pos.add(0,0,0);
		EnumFacing face = BlockImoutoHead.getFacing(state);
		int vertical = 0;
		if (player != null){
			double x =  player.posX - pos.getX();
			double y =  player.posY - pos.getY();
			double z =  player.posZ - pos.getZ();
			double distance = Math.sqrt(
					Math.pow(pos.getX()-player.posX,2)+
					Math.pow(pos.getY()-player.posY,2)+
					Math.pow(pos.getZ()-player.posZ,2));
			double hdeg = (Math.atan2(z,x)*180)/Math.PI;
			if (hdeg < 0){hdeg = 360+hdeg;}
			if (hdeg < 45 || hdeg > 315){
				nextPos = nextPos.offset(EnumFacing.EAST);
				face = EnumFacing.EAST;
			}else if ( hdeg < 135 && hdeg > 45){
				nextPos = nextPos.offset(EnumFacing.SOUTH);
				face = EnumFacing.SOUTH;
			}else if ( hdeg < 225 && hdeg > 135){
				nextPos = nextPos.offset(EnumFacing.WEST);
				face = EnumFacing.WEST;
			}else if (hdeg < 315 && hdeg > 225){
				nextPos = nextPos.offset(EnumFacing.NORTH);
				face = EnumFacing.NORTH;
			}

			double xyDeg = (Math.atan2(y, x)*180)/Math.PI;
			double zyDeg = (Math.atan2(y, z)*180)/Math.PI;
			if (xyDeg < 0){xyDeg = 360 + xyDeg;}
			if (zyDeg < 0){zyDeg = 360 + zyDeg;}

			if (xyDeg > 75 && xyDeg < 105 && zyDeg > 75 && zyDeg <105){
				nextPos = nextPos.offset(EnumFacing.UP);
				vertical = 1;
			}else if(xyDeg > 255 && xyDeg < 285 && zyDeg > 255 && zyDeg <285){
				nextPos = nextPos.offset(EnumFacing.DOWN);
				vertical = 2;
			}

			if (!nextPos.equals(pos)){
				// 更新
				if (nextPos.getY() > 0 && (player.getHorizontalFacing() != face.getOpposite()) &&
						((vertical == 0) ||
						 (vertical == 1 && MathHelper.wrapDegrees(player.rotationPitch) < -15) ||
						 (vertical == 2 && MathHelper.wrapDegrees(player.rotationPitch) > 15))
						){
					Material nextMat = world.getBlockState(nextPos).getMaterial();
					Material nextDMat = world.getBlockState(nextPos.offset(EnumFacing.DOWN)).getMaterial();
					if (nextMat == Material.AIR){
						state = BlockImoutoHead.setFacing(state, face);
						state = BlockImoutoHead.setVertical(state, vertical);
						int meta = state.getBlock().getMetaFromState(state);
						world.setBlockState(nextPos, rotten.getBlock(meta));
						world.setBlockToAir(pos);
					}
				}
			}
		}
	}

	private SoundEvent[] imouto_voice = {
			SoundManager.block_imoutohead_normal,
			SoundManager.block_imoutohead_rotten,
			SoundManager.block_imoutohead_skelton
	};
	private void soundPlayer(IBlockState state) {
		EntityPlayer player = getNearlyPlayer(state, 5);
		if (player != null){
			world.playSound(player, pos, imouto_voice[rotten.getValue()], SoundCategory.VOICE, 1.0F, 1.0F);
		}
	}

	private EntityPlayer getNearlyPlayer(IBlockState state, double range){
		EntityPlayer nearlyPlayer = null;
		double nearlyDistance = range+1;
		for (EntityPlayer pl : this.world.playerEntities){
			double distance = Math.sqrt(
					Math.pow(pos.getX()-pl.posX,2)+
					Math.pow(pos.getY()-pl.posY,2)+
					Math.pow(pos.getZ()-pl.posZ,2));
			if (distance < nearlyDistance){
				nearlyDistance = distance;
				nearlyPlayer = pl;
			}
		}
		return nearlyPlayer;
	}





    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        chg_time = compound.getInteger("chgTime");
        checkCount = compound.getInteger("checkCount");
        nextCheckTime = compound.getInteger("nexttime");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound =  super.writeToNBT(compound);
        compound.setInteger("chgTime",chg_time);
        compound.setInteger("checkCount",checkCount);
        compound.setInteger("nexttime",nextCheckTime);
        return compound;
    }

}
