package mod.elm.render;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.entity.EntityWanderingPeople;
import mod.elm.render.model.IModelElmTexture;
import mod.elm.render.model.ModelWanderingPeople;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderWanderingPeople extends LivingRenderer<EntityWanderingPeople, ModelWanderingPeople<EntityWanderingPeople>> {

	public RenderWanderingPeople(EntityRendererManager renderManager) {
		super(renderManager, new ModelWanderingPeople<>(0.0F), 0.5F);
		this.addLayer(new ArrowLayer<>(this));
	}

	public void doRender(EntityWanderingPeople entity, double x, double y, double z, float entityYaw, float partialTicks) {
		double d0 = y-0.7;
		if (entity.shouldRenderSneaking()) {
			d0 = y - 0.125D;
		}
		this.setModelVisibilities(entity);
		super.doRender(entity, x, d0, z, entityYaw, partialTicks);
	}

	private void setModelVisibilities(EntityWanderingPeople wander) {
		ModelWanderingPeople<EntityWanderingPeople> ModelWanderingPeople = this.getEntityModel();
		ItemStack itemstack = wander.getHeldItemMainhand();
		ItemStack itemstack1 = wander.getHeldItemOffhand();
		ModelWanderingPeople.setVisible(wander);
		ModelWanderingPeople.isSneak = wander.shouldRenderSneaking();
		BipedModel.ArmPose bipedmodel$armpose = this.func_217766_a(wander, itemstack, itemstack1, Hand.MAIN_HAND);
		BipedModel.ArmPose bipedmodel$armpose1 = this.func_217766_a(wander, itemstack, itemstack1, Hand.OFF_HAND);
		if (wander.getPrimaryHand() == HandSide.RIGHT) {
			ModelWanderingPeople.rightArmPose = bipedmodel$armpose;
			ModelWanderingPeople.leftArmPose = bipedmodel$armpose1;
		} else {
			ModelWanderingPeople.rightArmPose = bipedmodel$armpose1;
			ModelWanderingPeople.leftArmPose = bipedmodel$armpose;
		}
	}

	private BipedModel.ArmPose func_217766_a(EntityWanderingPeople p_217766_1_, ItemStack p_217766_2_, ItemStack p_217766_3_, Hand p_217766_4_) {
		BipedModel.ArmPose bipedmodel$armpose = BipedModel.ArmPose.EMPTY;
		ItemStack itemstack = p_217766_4_ == Hand.MAIN_HAND ? p_217766_2_ : p_217766_3_;
		if (!itemstack.isEmpty()) {
			bipedmodel$armpose = BipedModel.ArmPose.ITEM;
			if (p_217766_1_.getItemInUseCount() > 0) {
				UseAction useaction = itemstack.getUseAction();
				if (useaction == UseAction.BLOCK) {
					bipedmodel$armpose = BipedModel.ArmPose.BLOCK;
				} else if (useaction == UseAction.BOW) {
					bipedmodel$armpose = BipedModel.ArmPose.BOW_AND_ARROW;
				} else if (useaction == UseAction.SPEAR) {
					bipedmodel$armpose = BipedModel.ArmPose.THROW_SPEAR;
				} else if (useaction == UseAction.CROSSBOW && p_217766_4_ == p_217766_1_.getActiveHand()) {
					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_CHARGE;
				}
			} else {
				boolean flag3 = p_217766_2_.getItem() == Items.CROSSBOW;
				boolean flag = CrossbowItem.isCharged(p_217766_2_);
				boolean flag1 = p_217766_3_.getItem() == Items.CROSSBOW;
				boolean flag2 = CrossbowItem.isCharged(p_217766_3_);
				if (flag3 && flag) {
					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
				}

				if (flag1 && flag2 && p_217766_2_.getItem().getUseAction(p_217766_2_) == UseAction.NONE) {
					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
				}
			}
		}
		return bipedmodel$armpose;
	}

	public ResourceLocation getEntityTexture(EntityWanderingPeople entity) {
		return entity.Texture((IModelElmTexture)this.entityModel);
	}

	protected void preRenderCallback(EntityWanderingPeople entitylivingbaseIn, float partialTickTime) {
		float f = 0.9375F;
		GlStateManager.scalef(0.9375F, 0.9375F, 0.9375F);
	}

	protected void renderEntityName(EntityWanderingPeople entityIn, double x, double y, double z, String name, double distanceSq) {
//	      double d0 = entityIn.getDistanceSq(this.renderManager.info.getProjectedView());
//	      if (!(d0 > (double)(maxDistance * maxDistance))) {
//	         boolean flag = entityIn.shouldRenderSneaking();
//	         float f = this.renderManager.playerViewY;
//	         float f1 = this.renderManager.playerViewX;
//	         float f2 = entityIn.getHeight() + 0.5F - (flag ? 0.25F : 0.0F);
//	         int i = "deadmau5".equals(str) ? -10 : 0;
//	         GameRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float)x, (float)y + f2, (float)z, i, f, f1, flag);
//	      }
	}

	public void renderRightArm(EntityWanderingPeople wander) {
		float f = 1.0F;
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelWanderingPeople<EntityWanderingPeople> ModelWanderingPeople = this.getEntityModel();
		this.setModelVisibilities(wander);
		GlStateManager.enableBlend();
		ModelWanderingPeople.swingProgress = 0.0F;
		ModelWanderingPeople.isSneak = false;
		ModelWanderingPeople.swimAnimation = 0.0F;
		ModelWanderingPeople.setRotationAngles(wander, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		ModelWanderingPeople.bipedRightArm.rotateAngleX = 0.0F;
		ModelWanderingPeople.bipedRightArm.render(0.0625F);
		GlStateManager.disableBlend();
	}


	public void renderLeftArm(EntityWanderingPeople wander) {
		float f = 1.0F;
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelWanderingPeople<EntityWanderingPeople> ModelWanderingPeople = this.getEntityModel();
		this.setModelVisibilities(wander);
		GlStateManager.enableBlend();
		ModelWanderingPeople.isSneak = false;
		ModelWanderingPeople.swingProgress = 0.0F;
		ModelWanderingPeople.swimAnimation = 0.0F;
		ModelWanderingPeople.setRotationAngles(wander, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		ModelWanderingPeople.bipedLeftArm.rotateAngleX = 0.0F;
		ModelWanderingPeople.bipedLeftArm.render(0.0625F);
		GlStateManager.disableBlend();
	}

	protected void applyRotations(EntityWanderingPeople entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
		float f = entityLiving.getSwimAnimation(partialTicks);
		if (entityLiving.isElytraFlying()) {
			super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
			float f1 = (float)entityLiving.getTicksElytraFlying() + partialTicks;
			float f2 = MathHelper.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
			if (!entityLiving.isSpinAttacking()) {
				GlStateManager.rotatef(f2 * (-90.0F - entityLiving.rotationPitch), 1.0F, 0.0F, 0.0F);
			}

			Vec3d vec3d = entityLiving.getLook(partialTicks);
			Vec3d vec3d1 = entityLiving.getMotion();
			double d0 = Entity.func_213296_b(vec3d1);
			double d1 = Entity.func_213296_b(vec3d);
			if (d0 > 0.0D && d1 > 0.0D) {
				double d2 = (vec3d1.x * vec3d.x + vec3d1.z * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
				double d3 = vec3d1.x * vec3d.z - vec3d1.z * vec3d.x;
				GlStateManager.rotatef((float)(Math.signum(d3) * Math.acos(d2)) * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
			}
		} else if (f > 0.0F) {
			super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
			float f3 = entityLiving.isInWater() ? -90.0F - entityLiving.rotationPitch : -90.0F;
			float f4 = MathHelper.lerp(f, 0.0F, f3);
			GlStateManager.rotatef(f4, 1.0F, 0.0F, 0.0F);
			if (entityLiving.func_213314_bj()) {
				GlStateManager.translatef(0.0F, -1.0F, 0.3F);
			}
		} else {
			super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
		}
	}
}