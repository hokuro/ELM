package mod.elm.entity.ai.gal;

import java.util.EnumSet;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mod.elm.entity.EntityPlayerDummy;
import mod.elm.entity.ab.IHungryMob;
import mod.elm.sound.SoundCore;
import mod.elm.util.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;;

public class SearchCake extends Goal {
	private static final EntityPredicate ENTITY_PREDICATE = (new EntityPredicate()).setDistance(10.0D).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks().setLineOfSiteRequired();
	protected final CreatureEntity creature;
	private final double speed;
	private double targetX;
	private double targetY;
	private double targetZ;
	private boolean isRunning;
	protected final int searcharea;
	protected BlockPos cakePos;
	protected boolean findCake;
	protected int cakeTick;
	protected EntityPlayerDummy dummy;

	public SearchCake(CreatureEntity creatureIn, double speedIn, int area) {
		this.creature = creatureIn;
		this.speed = speedIn;

		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		if (!(creatureIn.getNavigator() instanceof GroundPathNavigator)) {
			throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
		}

		this.searcharea = area;
		cakePos = BlockPos.ZERO;
		findCake = false;
		cakeTick = 0;
		dummy = new EntityPlayerDummy(creatureIn.world, new GameProfile(new UUID(0,0), "dummy"));
		makeArray();
	}

	@Override
	public boolean shouldExecute() {
		if (this.creature.isBeingRidden()) {
			return false;
		} else if (((IHungryMob)creature).getHunger() < ((IHungryMob)creature).getMaxHunger()/2){
			findCake = false;
			for (int nex = 0; nex < next_x.length && !findCake; nex++) {
				for (int i = 0; i < searcharea * 2 + 1; i++) {
					int y = 0;
					if (i%2 == 0) {
						y = 0 - (i/2);
					}else {
						y = i/2;
					}
					BlockState state = creature.world.getBlockState(creature.getPosition().add(next_x[nex], y, next_z[nex]));
					if (state.getBlock() instanceof CakeBlock) {
						cakePos = creature.getPosition().add(next_x[nex], y, next_z[nex]);
						findCake = true;
						break;
					}
				}
			}
			return findCake;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		BlockState state = creature.world.getBlockState(cakePos);
		if (findCake && state.getBlock() instanceof CakeBlock && ((IHungryMob)creature).getHunger() < ((IHungryMob)creature).getHunger()/2) {
			return true;
		}
		return this.shouldExecute();
	}

	@Override
	public void startExecuting() {
		this.targetX = cakePos.getX();
		this.targetY = cakePos.getY();
		this.targetZ = cakePos.getZ();
		this.isRunning = true;
	}

	@Override
	public void resetTask() {
		this.findCake = false;
		this.cakePos = BlockPos.ZERO;
		this.creature.getNavigator().clearPath();
		this.isRunning = false;
		this.cakeTick = 0;
	}

	@Override
	public void tick() {
		this.creature.getLookController().setLookPosition(targetX, targetY, targetZ, (float)(this.creature.getHorizontalFaceSpeed() + 20), (float)this.creature.getVerticalFaceSpeed());
		if ( Math.abs(this.creature.getDistanceSq(new Vec3d(targetX,targetY,targetZ))) < 2.0){
			cakeTick++;
			if (cakeTick > 200) {
				cakeTick = 0;
				if (ModUtil.randomD() < 0.3D) {
					BlockState state = creature.world.getBlockState(cakePos);
					creature.getActivePotionEffects().forEach(potion ->{
						dummy.addPotionEffect(new EffectInstance(potion.getPotion(),potion.getDuration(),potion.getAmplifier()));
					});
					dummy.setHungry();
					state.onBlockActivated(creature.world, dummy, Hand.MAIN_HAND, new BlockRayTraceResult(creature.getPositionVec(),creature.getHorizontalFacing(), cakePos, true));
					((IHungryMob)creature).addHunger(dummy.getFoodStats().getFoodLevel());
					creature.clearActivePotions();
					dummy.getActivePotionEffects().forEach(potion ->{
						creature.addPotionEffect(new EffectInstance(potion.getPotion(),potion.getDuration(),potion.getAmplifier()));
					});
					creature.playSound(SoundCore.SOUND_ENTITYAMBIENT_EATCAKE, 1.0F, 1.0F);
				}
			}
		} else {
			this.creature.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, this.speed);
		}
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	private int[] next_x;
	private int[] next_z;
	protected void makeArray(){
		int size = (searcharea * 2 + 1) * (searcharea * 2 + 1) -1;
		next_x = new int[size];
		next_z = new int[size];

		int maxLp =searcharea * 4 + 1;
		int len = 0;
		int x = 0;
		int z = 0;
		int idx = 0;
		for (int i = 0; i < maxLp; i ++){
			if (i%2 == 0 && i != maxLp-1){len++;}
			for (int j = 0; j < len; j++){
				switch(i%4){
				case 0: x++; break;
				case 1: z++;break;
				case 2: x--;break;
				case 3: z--;break;
				}


				next_x[idx] = x;
				next_z[idx] = z;
				idx++;
			}
		}
	}
}