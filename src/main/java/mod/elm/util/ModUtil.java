package mod.elm.util;

import java.util.List;
import java.util.Random;

import mod.elm.core.Mod_ElonaMobs;
import mod.elm.core.log.ModLog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ModUtil {
	public static enum CompaierLevel{
		LEVEL_EQUAL_ITEM,
		LEVEL_EQUAL_META,
		LEVEL_EQUAL_COUNT,
		LEVEL_EQUAL_ALL
	};

	public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2 ){
		if (stack1 == null){return false;}
		if (stack2 == null){return false;}
		if (stack1.isEmpty() && stack2.isEmpty()){return true;}
		return (stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata()));
	}

	public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2, CompaierLevel level){
		if (stack1 == null){return false;}
		if (stack2 == null){return false;}
		if (stack1.isEmpty() && stack2.isEmpty()){return true;}

		boolean ret = false;
		switch(level){
		case LEVEL_EQUAL_ALL:
			ret = ((stack1.getItem() == stack2.getItem()) &&
					stack1.getMetadata() == stack2.getMetadata() &&
					stack1.getCount() == stack2.getCount());
			break;
		case LEVEL_EQUAL_COUNT:
			ret = ((stack1.getItem() == stack2.getItem()) &&
					stack1.getCount() == stack2.getCount());
			break;
		case LEVEL_EQUAL_ITEM:
			ret = ((stack1.getItem() == stack2.getItem()));
			break;
		case LEVEL_EQUAL_META:
			ret = ((stack1.getItem() == stack2.getItem()) &&
					stack1.getMetadata() == stack2.getMetadata());
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
            EntityItem entityitem = new EntityItem(worldIn, x + (double)f, y + (double)f1, z + (double)f2, stack.splitStack(RANDOM.nextInt(21) + 10));
            float f3 = 0.05F;
            entityitem.motionX = RANDOM.nextGaussian() * 0.05000000074505806D;
            entityitem.motionY = RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
            entityitem.motionZ = RANDOM.nextGaussian() * 0.05000000074505806D;
            worldIn.spawnEntity(entityitem);
        }
    }



    private static int previousRand = 0;
	/**
	 * 0～n-1の範囲の乱数を取り出す
	 * @param n
	 * @return
	 */
	public static int random(int n) {
		if (n == 0){return 0;}
		Random rand = Mod_ElonaMobs.instance.rnd;
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

	/**
	 * 取り出した乱数の前回の値を取得する
	 * @return
	 */
	public static int randomPrevious(){
		return previousRand;
	}

	/**
	 * 0～n-1の範囲の乱数を取り出す
	 * @param n
	 * @return
	 */
	public static double randomD() {
		Random rand = Mod_ElonaMobs.instance.rnd;
		if ( rand == null){
			rand = new Random();
			rand.setSeed(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
		}
	    return rand.nextDouble();
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

	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String ... fieldNames)
    {
		return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, fieldNames);
    }

	public static <E> void setPrivateValue(Class<? super E> classToAccess, E instance, Object value, String ... fieldNames){
		ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldNames);
	}


	public static EntityLivingBase getClosestLivingEntity(double parDistance, float tick) {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			EntityLivingBase viewEntity = (mc.getRenderViewEntity() instanceof EntityLivingBase)?(EntityLivingBase)mc.getRenderViewEntity():null;
			EntityLivingBase Return = null;
			double closest = parDistance;
			Vec3d playerPosition;
			Vec3d lookFarCoord;

			// 見ているエンティティがあるかどうか
			if ((viewEntity != null) && (viewEntity instanceof EntityLivingBase)) {
				World worldObj = viewEntity.world;//　　.worldObj;
				RayTraceResult objectMouseOver = viewEntity.rayTrace(parDistance, tick);

				// プレイヤーの位置
				playerPosition = new Vec3d(viewEntity.getPosition());

				// 視線ベクトル
				Vec3d dirVec = viewEntity.getLookVec();
				// 視線座標
				lookFarCoord = playerPosition.addVector(dirVec.x * parDistance, dirVec.y * parDistance, dirVec.z * parDistance);

				// 視線が当たっているMobを取得
				List<EntityLivingBase> targettedEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
						 viewEntity.getEntityBoundingBox().expand(dirVec.x * parDistance, dirVec.y * parDistance, dirVec.z * parDistance));
				// 自分自身はMobから外す
				targettedEntities.remove(viewEntity);
				for (EntityLivingBase targettedEntity : targettedEntities) {
					if (targettedEntity != null) {
						// エンティティとの距離
						double precheck = viewEntity.getDistance(targettedEntity);
						// 視線が当たっているかどうか確認
						RayTraceResult mopElIntercept = targettedEntity.getEntityBoundingBox().calculateIntercept(playerPosition.addVector(0, viewEntity.getEyeHeight(), 0),lookFarCoord);
						if ((mopElIntercept != null) && (precheck < closest)) {
							Return = targettedEntity;
							closest = precheck;
							ModLog.log().debug("Hit " + Return.getName());
						}
					}
				}
			}
			if ((Return != null) && (!Return.isDead) && (!Return.isInvisible())) {
				return Return;
			}
		} catch (Throwable ex) {
		}
		return null;
	}



}
