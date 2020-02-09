package mod.elm.entity;

import mod.elm.core.Mod_Elm;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityElmStomach extends ThrowableEntity {

	public static final String NAME = "stomach";
	private int sizeTime;
	private int cntTime = 0;

	public EntityElmStomach(FMLPlayMessages.SpawnEntity packet, World world) {
		this(Mod_Elm.RegistryEvents.STOMACH, world);
	}

	public EntityElmStomach(EntityType<? extends ThrowableEntity> etype, World world) {
		super(etype, world);
	}

	public EntityElmStomach(World world, LivingEntity entityliving, int time) {
		super(Mod_Elm.RegistryEvents.STOMACH, entityliving, world);
		sizeTime = time;
	}

	@Override
	public void tick() {
		super.tick();
		cntTime++;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			// i苦痛のポーション効果の雲を生成するs
			this.makeAreaOfEffectCloud();
			this.remove();
			this.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
		}
	}

	private void makeAreaOfEffectCloud() {
		AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.posX, this.posY, this.posZ);
		areaeffectcloudentity.setOwner(this.getThrower());
		areaeffectcloudentity.setRadius(5.0F);
		areaeffectcloudentity.setRadiusOnUse(-0.5F);
		areaeffectcloudentity.setWaitTime(20*3);
		areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());
		areaeffectcloudentity.setPotion(Potions.HARMING);
		areaeffectcloudentity.setDuration(20 * this.sizeTime);
		areaeffectcloudentity.addEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 20 * this.sizeTime, (int)Math.sqrt(this.sizeTime/10)));

		this.world.addEntity(areaeffectcloudentity);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerData() {

	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("sizetme", this.sizeTime);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.sizeTime = compound.getInt("sizetme");
	}

	public int getCntTime() {
		return this.cntTime;
	}
}

