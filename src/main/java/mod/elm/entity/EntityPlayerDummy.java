package mod.elm.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class EntityPlayerDummy extends PlayerEntity {

	public EntityPlayerDummy(World worldIn, GameProfile gameProfile) {
		super(worldIn, gameProfile);
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}

	@Override
	public void read(CompoundNBT compound){}

	public void setHungry() {
		this.foodStats.setFoodLevel(0);
		this.foodStats.setFoodSaturationLevel(0);
	}

}