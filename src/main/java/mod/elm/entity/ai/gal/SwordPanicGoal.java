package mod.elm.entity.ai.gal;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;

public class SwordPanicGoal extends Goal {
	private static final EntityPredicate ENTITY_PREDICATE = (new EntityPredicate()).setDistance(10.0D).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks().setLineOfSiteRequired();
	protected final CreatureEntity creature;
	protected final double speed;
	protected double randPosX;
	protected double randPosY;
	protected double randPosZ;
	protected PlayerEntity closestPlayer;

	public SwordPanicGoal(CreatureEntity creature, double speedIn) {
		this.creature = creature;
		this.speed = speedIn;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		this.closestPlayer = this.creature.world.getClosestPlayer(ENTITY_PREDICATE, this.creature);
		if (closestPlayer != null) {
			return checkItem();
		}
		return false;
	}

	private boolean checkItem() {
		if (closestPlayer.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem ||
				closestPlayer.getHeldItem(Hand.MAIN_HAND).getItem() instanceof AxeItem ||
				closestPlayer.getHeldItem(Hand.OFF_HAND).getItem() instanceof SwordItem ||
				closestPlayer.getHeldItem(Hand.OFF_HAND).getItem() instanceof AxeItem) {
			return true;
		}
		return false;
	}

	protected boolean findRandomPosition() {
		Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 5, 4);
		if (vec3d == null) {
			return false;
		} else {
			this.randPosX = vec3d.x;
			this.randPosY = vec3d.y;
			this.randPosZ = vec3d.z;
			return true;
		}
	}

	@Override
	public void startExecuting() {
		this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
	}


	@Override
	public boolean shouldContinueExecuting() {
		return !this.creature.getNavigator().noPath();
	}

	@Nullable
	protected BlockPos getRandPos(IBlockReader worldIn, Entity entityIn, int horizontalRange, int verticalRange) {
		BlockPos blockpos = new BlockPos(entityIn);
		int i = blockpos.getX();
		int j = blockpos.getY();
		int k = blockpos.getZ();
		float f = (float)(horizontalRange * horizontalRange * verticalRange * 2);
		BlockPos blockpos1 = null;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for(int l = i - horizontalRange; l <= i + horizontalRange; ++l) {
			for(int i1 = j - verticalRange; i1 <= j + verticalRange; ++i1) {
				for(int j1 = k - horizontalRange; j1 <= k + horizontalRange; ++j1) {
					blockpos$mutableblockpos.setPos(l, i1, j1);
					if (worldIn.getFluidState(blockpos$mutableblockpos).isTagged(FluidTags.WATER)) {
						float f1 = (float)((l - i) * (l - i) + (i1 - j) * (i1 - j) + (j1 - k) * (j1 - k));
						if (f1 < f) {
							f = f1;
							blockpos1 = new BlockPos(blockpos$mutableblockpos);
						}
					}
				}
			}
		}
		return blockpos1;
	}
}