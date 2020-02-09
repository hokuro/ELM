package mod.elm.entity.ai.gal;

import javax.annotation.Nullable;

import mod.elm.util.ModUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WaterAvoidingRandomHome extends RandomWalkingGoal {
	protected final float probability;

	public WaterAvoidingRandomHome(CreatureEntity creature, double speedIn) {
		this(creature, speedIn, 0.001F);
	}

	public WaterAvoidingRandomHome(CreatureEntity creature, double speedIn, float probabilityIn) {
		super(creature, speedIn);
		this.probability = probabilityIn;
	}

	public boolean shouldExecute() {
		if (this.creature.isBeingRidden()) {
			return false;
		} else {
			if (!this.mustUpdate) {
				if (this.creature.getIdleTime() >= 100) {
					return false;
				}

				if (this.creature.getRNG().nextInt(this.executionChance) != 0) {
					return false;
				}
			}
			BlockPos home = creature.getHomePosition();
			if (this.creature.getDistanceSq(home.getX(),home.getY(),home.getZ()) >= 100.0F) {
				this.x = this.creature.posX + ((this.creature.posX - home.getX()) / (ModUtil.random(6) + 5));
				this.y = this.creature.posY + ((this.creature.posY - home.getY()) / (ModUtil.random(6) + 5));
				this.z = this.creature.posZ + ((this.creature.posZ - home.getZ()) / (ModUtil.random(6) + 5));
				this.mustUpdate = false;
				return true;
			} else {
				return false;
			}
		}
	}

	@Nullable
	protected Vec3d getPosition() {
		if (this.creature.isInWaterOrBubbleColumn()) {
			Vec3d vec3d = RandomPositionGenerator.getLandPos(this.creature, 15, 7);
			return vec3d == null ? super.getPosition() : vec3d;
		} else {
			return this.creature.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.creature, 10, 7) : super.getPosition();
		}
	}
}