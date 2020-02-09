package mod.elm.util;

import java.util.List;
import java.util.Random;

import mod.elm.core.Mod_Elm;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ModUtil {
	public static enum CompaierLevel{
		LEVEL_EQUAL_ITEM,
		LEVEL_EQUAL_COUNT,
		LEVEL_EQUAL_ALL
	};

	public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2 ){
		if (stack1 == null){return false;}
		if (stack2 == null){return false;}
		if (stack1.isEmpty() && stack2.isEmpty()){return true;}
		return (stack2.getItem() == stack1.getItem() );
	}

	public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2, CompaierLevel level){
		if (stack1 == null){return false;}
		if (stack2 == null){return false;}
		if (stack1.isEmpty() && stack2.isEmpty()){return true;}

		boolean ret = false;
		switch(level){
		case LEVEL_EQUAL_ALL:
			ret = ((stack1.getItem() == stack2.getItem()) &&
					stack1.getCount() == stack2.getCount());
			break;
		case LEVEL_EQUAL_COUNT:
			ret = ((stack1.getItem() == stack2.getItem()) &&
					stack1.getCount() == stack2.getCount());
			break;
		case LEVEL_EQUAL_ITEM:
			ret = ((stack1.getItem() == stack2.getItem()));
			break;
		default:
			break;
		}

		return ret;
	}


	public static boolean containItemStack(ItemStack checkItem,List<ItemStack> itemArray, CompaierLevel leve){
		ItemStack[] stacks = itemArray.toArray(new ItemStack[itemArray.size()]);
		return containItemStack(checkItem,stacks,leve);
	}

	public static boolean containItemStack(ItemStack checkItem, ItemStack[] itemArray, CompaierLevel level) {
		boolean ret = false;
		for (ItemStack item: itemArray){
			if (compareItemStacks(checkItem, item,level)){
				ret = true;
				break;
			}
		}
		return ret;
	}

    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack, Random RANDOM)
    {
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty())
        {
        	ItemEntity entityitem = new ItemEntity(worldIn, x + (double)f, y + (double)f1, z + (double)f2, stack.split(RANDOM.nextInt(21) + 10));
            float f3 = 0.05F;
            entityitem.setMotion(RANDOM.nextGaussian() * 0.05000000074505806D,
                                 RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D,
                                 RANDOM.nextGaussian() * 0.05000000074505806D);
            worldIn.addEntity(entityitem);
        }
    }



    private static int previousRand = -1;
    /**
	 * 1～nの範囲の乱数を取り出す
	 * @param n
	 * @return
	 */
    public static int random_1(int n) {
    	previousRand = random(n)+1;
    	return previousRand;
    }

	/**
	 * 0～n-1の範囲の乱数を取り出す
	 * @param n
	 * @return
	 */
	public static int random(int n) {
		if (n == 0){return 0;}
		Random rand = Mod_Elm.rnd;
		if ( rand == null){
			rand = new Random();
			rand.setSeed(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
		}

	    long adjusted_max = (((long)Integer.MAX_VALUE) + 1) - (((long)Integer.MAX_VALUE) + 1) % n;
	    int r;
	    do {
	       r = Math.abs(rand.nextInt());
	    } while (r >= adjusted_max);
	    previousRand = (int)(((double)r / adjusted_max) * n);
	    return previousRand;
	}

	public static int getPrevisiousRand(int n) {
		if (ModUtil.previousRand < 0) {
			return random(n);
		}
		return ModUtil.previousRand;
	}
	public static int getPrevisiousRand_1(int n) {
		if (ModUtil.previousRand < 0) {
			return random_1(n);
		}
		return ModUtil.previousRand;
	}

	/**
	 * 取り出した乱数の前回の値を取得する
	 * @return
	 */
	public static int randomPrevious(){
		return previousRand;
	}

	public static int resetPreviousRand() {
		return previousRand = -1;
	}

	/**
	 * 0～n-1の範囲の乱数を取り出す
	 * @param n
	 * @return
	 */
	public static double randomD() {
		Random rand = Mod_Elm.rnd;
		if ( rand == null){
			rand = new Random();
			rand.setSeed(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
		}
	    return rand.nextDouble();
	}

	/**
	 * 0～n-1の範囲の乱数を取り出す
	 * @param n
	 * @return
	 */
	public static float randomF() {
		Random rand = Mod_Elm.rnd;
		if ( rand == null){
			rand = new Random();
			rand.setSeed(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
		}
	    return rand.nextFloat();
	}

	public static <T> T randomListSelect(List<T> list) {
		T ret = null;
		if (list != null && list.size() > 0) {
			ret = list.get(ModUtil.random(list.size()));
		}
		return ret;
	}

	/**
	 * 対象の文字列がnullまたは空文字か判定する
	 * @param value
	 * @return
	 */
	public static boolean StringisEmptyOrNull(String value){
		if (value == null || value.isEmpty()){
			return true;
		}
		return false;
	}

	private static String crlf = null;
	/**
	 * 使用環境の改行コードを取得する
	 * @return
	 */
	public static String ReturnCode(){
		if(crlf == null){
			crlf = System.getProperty("line.separator");
		}
		return crlf;
	}

	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String fieldNames)
    {
		return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, fieldNames);
    }

	public static <E> void setPrivateValue(Class<? super E> classToAccess, E instance, Object value, String fieldNames){
		ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldNames);
	}

	public static int IntMax(int arg1, int arg2, int... args) {
		int ret = arg1;
		if (arg1 < arg2) {
			ret = arg2;
		}
		for (int i : args) {
			if (ret < i) {
				ret = i;
			}
		}
		return ret;
	}


	public static CompoundNBT saveAllItems(CompoundNBT tag, NonNullList<ItemStack> list, boolean saveEmpty, String saveKey) {
		ListNBT listnbt = new ListNBT();
		for(ItemStack item : list) {
			if (!item.isEmpty() || saveEmpty) {
				CompoundNBT comoundnbt = new CompoundNBT();
				item.write(comoundnbt);
				listnbt.add(comoundnbt);
			}
		}
		if (!listnbt.isEmpty() || saveEmpty) {
			tag.put(saveKey, listnbt);
		}
		return tag;
	}

	public static void loadAllItems(CompoundNBT tag, NonNullList<ItemStack> list, String loadkey) {
		ListNBT listnbt = tag.getList(loadkey, 10);
		for(int i = 0; i < listnbt.size(); ++i) {
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			list.add(ItemStack.read(compoundnbt));
		}
	}

	public static Vec3d makeVec(float rotationPitchIn, float rotationYawIn) {
		// i向いている方向を計算
		float mx = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
		float my = -MathHelper.sin((rotationPitchIn) * ((float)Math.PI / 180F));
		float mz = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
		Vec3d vec3d = (new Vec3d(mx, my, mz)).normalize();
		return vec3d;
	}

	public static Vec3d makePolar(Vec3d vec) {
		return makePolar(vec.x, vec.y, vec.z);
	}

	public static Vec3d makePolar(double x, double y, double z) {
		double r = Math.sqrt(x*x + y*y + z*z);
		double f = Math.sqrt(x*x + z*z);
		double pit = MathHelper.atan2(y, (double)f) * (double)(180F / (float)Math.PI);
		double yaw = MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI);
		return new Vec3d(r,pit,yaw);
	}

	public static CompoundNBT putBlockPos(CompoundNBT compound, String key, BlockPos pos) {
		ListNBT listnbt = new ListNBT();
		CompoundNBT posNBT = new CompoundNBT();
		posNBT.putInt(key +"_posx", pos.getX());
		posNBT.putInt(key +"_posy", pos.getY());
		posNBT.putInt(key +"_posz", pos.getZ());
		compound.put(key, posNBT);
		return compound;
	}

	public static BlockPos getBlockPos(CompoundNBT compound, String key) {
		CompoundNBT nbt = (CompoundNBT)compound.get(key);
		return new BlockPos(
				nbt.getInt(key+"_posx"),
				nbt.getInt(key+"_posy"),
				nbt.getInt(key+"_posz")
				);
	}
}
