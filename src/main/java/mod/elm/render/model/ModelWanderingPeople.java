package mod.elm.render.model;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.entity.EntityWanderingPeople;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.potion.Effects;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelWanderingPeople<T extends LivingEntity> extends BipedModel<T> implements IModelElmTexture{
	public RendererModel skirt;
	public RendererModel hariR1;
	public RendererModel hairR2;
	public RendererModel hairR3;
	public RendererModel hairR4;
	public RendererModel hariR5;
	public RendererModel hariL1;
	public RendererModel hairL2;
	public RendererModel hairL3;
	public RendererModel hairL4;
	public RendererModel hairL5;
	public RendererModel backHair1;
	public RendererModel backHair3;
	public RendererModel backHair2;
	public RendererModel ponytail1;
	public RendererModel ponytail2;
	public RendererModel ponytail3;
	public RendererModel cheak;
	private float remainingItemUseTime2;
	public static final List<ResourceLocation> textures = new ArrayList<ResourceLocation>();

	public static final float HAIRR4_ROTATE_X = -0.3346075F;
	public static final float HAIRR5_ROTATE_X = 0.3490659F;
	public static final float HAIRL4_ROTATE_X = -0.3490659F;
	public static final float HAIRL5_ROTATE_X = 0.3490659F;

	public ModelWanderingPeople() {
		this(0.0F);
	}

	public ModelWanderingPeople(float modelSize) {
		this(modelSize, 0.0F, 128, 64);
	}

	public ModelWanderingPeople(float modelSize, float offset, int textureWidthIn, int textureHeightIn) {
		super(modelSize, offset, textureWidthIn, textureHeightIn);
		this.bipedHead = new RendererModel(this, 0, 0);
		this.bipedHead.addBox(-5F, -12F, -5F, 8, 8, 8);
		this.bipedHead.setRotationPoint(1F, 4F, 1F);
		this.bipedHead.setTextureSize(128, 64);
		this.bipedHead.mirror = true;

		this.bipedBody = new RendererModel(this, 32, 0);
		this.bipedBody.addBox(-3F, 0F, -2F, 6, 5, 4);
		this.bipedBody.setRotationPoint(0F, 0F, 0F);
		this.bipedBody.setTextureSize(128, 64);
		this.bipedBody.mirror = true;

		this.bipedRightArm = new RendererModel(this, 52, 0);
		this.bipedRightArm.addBox(-2F, -1F, -1F, 2, 6, 2);
		this.bipedRightArm.setRotationPoint(-3F, 1.5F, 0F);
		this.bipedRightArm.setTextureSize(128, 64);
		this.bipedRightArm.mirror = true;

		this.bipedLeftArm = new RendererModel(this, 60, 0);
		this.bipedLeftArm.addBox(3F, 1.5F, 0F, 2, 6, 2);
		this.bipedLeftArm.setRotationPoint(0F, -1F, -1F);
		this.bipedLeftArm.setTextureSize(128, 64);
		this.bipedLeftArm.mirror = true;

		this.bipedRightLeg = new RendererModel(this, 32, 19);
		this.bipedRightLeg.addBox(-2F, 0F, -2F, 3, 7, 4);
		this.bipedRightLeg.setRotationPoint(-1F, 5F, 0F);
		this.bipedRightLeg.setTextureSize(128, 64);
		this.bipedRightLeg.mirror = true;

		this.bipedLeftLeg = new RendererModel(this, 46, 19);
		this.bipedLeftLeg.addBox(-1F, 0F, -2F, 3, 7, 4);
		this.bipedLeftLeg.setRotationPoint(1F, 5F, 0F);
		this.bipedLeftLeg.setTextureSize(128, 64);
		this.bipedLeftLeg.mirror = true;

		this.skirt = new RendererModel(this, 0, 16);
		this.skirt.addBox(-4F, -2F, -3F, 8, 6, 6);
		this.skirt.setRotationPoint(0F, 6F, 0F);
		this.skirt.setTextureSize(128, 64);
		this.skirt.mirror = true;

		this.hariR1 = new RendererModel(this, 0, 41);
		this.hariR1.addBox(-6F, -11F, -0.8F, 1, 3, 3);
		this.hariR1.setRotationPoint(0F, 0F, 0F);
		this.hariR1.setTextureSize(128, 64);
		this.hariR1.mirror = true;

		this.hairR2 = new RendererModel(this, 0, 32);
		this.hairR2.addBox(-6.5F, -10.8F, 0F, 1, 7, 2);
		this.hairR2.setRotationPoint(0F, 0F, 0F);
		this.hairR2.setTextureSize(128, 64);
		this.hairR2.mirror = true;

		this.hairR3 = new RendererModel(this, 6, 32);
		this.hairR3.addBox(-7.5F, -10.8F, 0F, 1, 6, 2);
		this.hairR3.setRotationPoint(0F, 0F, 0F);
		this.hairR3.setTextureSize(128, 64);
		this.hairR3.mirror = true;

		this.hairR4 = new RendererModel(this, 8, 41);
		this.hairR4.addBox(-7F, -9.8F, -5.2F, 1, 0, 1);
		this.hairR4.setRotationPoint(0F, 0F, 0F);
		this.hairR4.setTextureSize(128, 64);
		this.hairR4.mirror = true;
		this.setRotation(hairR4, HAIRR4_ROTATE_X, 0F, 0F);

		this.hariR5 = new RendererModel(this, 8, 42);
		this.hariR5.addBox(-6F, -9.3F, 5.7F, 1, 0, 1);
		this.hariR5.setRotationPoint(0F, 0F, 0F);
		this.hariR5.setTextureSize(128, 64);
		this.hariR5.mirror = true;
		this.setRotation(hariR5, HAIRR5_ROTATE_X, 0F, 0F);

		this.hariL1 = new RendererModel(this, 13, 41);
		this.hariL1.addBox(3F, -11F, -0.8F, 1, 3, 3);
		this.hariL1.setRotationPoint(0F, 0F, 0F);
		this.hariL1.setTextureSize(128, 64);
		this.hariL1.mirror = true;

		this.hairL2 = new RendererModel(this, 13, 32);
		this.hairL2.addBox(3.5F, -10.8F, 0F, 1, 7, 2);
		this.hairL2.setRotationPoint(0F, 0F, 0F);
		this.hairL2.setTextureSize(128, 64);
		this.hairL2.mirror = true;

		this.hairL3 = new RendererModel(this, 19, 32);
		this.hairL3.addBox(4.5F, -10.8F, 0F, 1, 6, 2);
		this.hairL3.setRotationPoint(0F, 0F, 0F);
		this.hairL3.setTextureSize(128, 64);
		this.hairL3.mirror = true;

		this.hairL4 = new RendererModel(this, 21, 41);
		this.hairL4.addBox(3F, -9.8F, -5.3F, 1, 0, 1);
		this.hairL4.setRotationPoint(0F, 0F, 0F);
		this.hairL4.setTextureSize(128, 64);
		this.hairL4.mirror = true;
		this.setRotation(hairL4, HAIRL4_ROTATE_X, 0F, 0F);

		this.hairL5 = new RendererModel(this, 21, 42);
		this.hairL5.addBox(3F, -9.3F, 5.7F, 1, 0, 1);
		this.hairL5.setRotationPoint(0F, 0F, 0F);
		this.hairL5.setTextureSize(128, 64);
		this.hairL5.mirror = true;
		this.setRotation(hairL5, HAIRL5_ROTATE_X, 0F, 0F);

		this.backHair1 = new RendererModel(this, 70, 0);
		this.backHair1.addBox(-5F, -4F, 3F, 8, 6, 0);
		this.backHair1.setRotationPoint(0F, 0F, 0F);
		this.backHair1.setTextureSize(128, 64);
		this.backHair1.mirror = true;

		this.backHair2 = new RendererModel(this, 68, 0);
		this.backHair2.addBox(3F, -4F, 2F, 0, 6, 1);
		this.backHair2.setRotationPoint(0F, 0F, 0F);
		this.backHair2.setTextureSize(128, 64);
		this.backHair2.mirror = true;

		this.backHair3 = new RendererModel(this, 86, 0);
		this.backHair3.addBox(-5F, -4F, 2F, 0, 6, 1);
		this.backHair3.setRotationPoint(0F, 0F, 0F);
		this.backHair3.setTextureSize(128, 64);
		this.backHair3.mirror = true;

		this.ponytail1 = new RendererModel(this, 69, 12);
		this.ponytail1.addBox(-2F, -10.8F, 3.5F, 2, 9, 1);
		this.ponytail1.setRotationPoint(0F, 0F, 0F);
		this.ponytail1.setTextureSize(128, 64);
		this.ponytail1.mirror = true;

		this.ponytail2 = new RendererModel(this, 69, 8);
		this.ponytail2.addBox(-3F, -11F, 3F, 4, 3, 1);
		this.ponytail2.setRotationPoint(0F, 0F, 0F);
		this.ponytail2.setTextureSize(128, 64);
		this.ponytail2.mirror = true;

		this.ponytail3 = new RendererModel(this, 75, 12);
		this.ponytail3.addBox(-2F, -10.8F, 4.5F, 2, 7, 1);
		this.ponytail3.setRotationPoint(0F, 0F, 0F);
		this.ponytail3.setTextureSize(128, 64);
		this.ponytail3.mirror = true;

		this.cheak = new RendererModel(this, 0, 56);
		this.cheak.addBox(-5F, -12F, -5.1F, 8, 8, 0);
		this.cheak.setRotationPoint(0F, 0F, 0F);
		this.cheak.setTextureSize(128, 64);
		this.cheak.mirror = true;

		this.bipedHead.addChild(this.hariR1);
		this.bipedHead.addChild(this.hairR2);
		this.bipedHead.addChild(this.hairR3);
		this.bipedHead.addChild(this.hairR4);
		this.bipedHead.addChild(this.hariR5);
		this.bipedHead.addChild(this.hariL1);
		this.bipedHead.addChild(this.hairL2);
		this.bipedHead.addChild(this.hairL3);
		this.bipedHead.addChild(this.hairL4);
		this.bipedHead.addChild(this.hairL5);
		this.bipedHead.addChild(this.backHair1);
		this.bipedHead.addChild(this.backHair2);
		this.bipedHead.addChild(this.backHair3);
		this.bipedHead.addChild(this.ponytail1);
		this.bipedHead.addChild(this.ponytail2);
		this.bipedHead.addChild(this.ponytail3);
		this.bipedHead.addChild(this.cheak);

		this.bipedBody.addChild(this.skirt);

		if (textures.size() ==0) {
			makeTextures();
		}
	}

	private void makeTextures() {
		for (int i = 0; i < 10079; i++) {
			textures.add(new ResourceLocation("elm:textures/entity/wanderer/wanderingpeopel_"+ String.format("%05d", i) + ".png"));
		}
	}

	private void setRotation(RendererModel model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.pushMatrix();
		if (entityIn.shouldRenderSneaking()) {
			GlStateManager.translatef(0.0F, 0.2F, 0.0F);
		}
		this.bipedHead.render(scale);
		this.bipedBody.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedRightLeg.render(scale);
		this.bipedLeftLeg.render(scale);
		GlStateManager.popMatrix();
	}

	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		//super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		boolean flag = entityIn.getTicksElytraFlying() > 4;
		boolean flag1 = entityIn.func_213314_bj();
		this.bipedHead.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		if (flag) {
			this.bipedHead.rotateAngleX = (-(float)Math.PI / 4F);
		} else if (this.swimAnimation > 0.0F) {
			if (flag1) {
				this.bipedHead.rotateAngleX = this.func_205060_a(this.bipedHead.rotateAngleX, (-(float)Math.PI / 4F), this.swimAnimation);
			} else {
				this.bipedHead.rotateAngleX = this.func_205060_a(this.bipedHead.rotateAngleX, headPitch * ((float)Math.PI / 180F), this.swimAnimation);
			}
		} else {
			this.bipedHead.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		}

		this.bipedBody.rotateAngleY = 0.0F;
		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedRightArm.rotationPointX = -3.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointX = -1.0F;
		float f = 1.0F;
		if (flag) {
		f = (float)entityIn.getMotion().lengthSquared();
		f = f / 0.2F;
		f = f * f * f;
		}

		if (f < 1.0F) {
		f = 1.0F;
		}

		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		this.bipedRightLeg.rotateAngleZ = 0.0F;
		this.bipedLeftLeg.rotateAngleZ = 0.0F;
		if (this.isSitting) {
			this.bipedRightArm.rotateAngleX += (-(float)Math.PI / 5F);
			this.bipedLeftArm.rotateAngleX += (-(float)Math.PI / 5F);
			this.bipedRightLeg.rotateAngleX = -1.4137167F;
			this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
			this.bipedRightLeg.rotateAngleZ = 0.07853982F;
			this.bipedLeftLeg.rotateAngleX = -1.4137167F;
			this.bipedLeftLeg.rotateAngleY = (-(float)Math.PI / 10F);
			this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		switch(this.leftArmPose) {
		case EMPTY:
			this.bipedLeftArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
			this.bipedLeftArm.rotateAngleY = ((float)Math.PI / 6F);
		break;
		case ITEM:
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
			this.bipedLeftArm.rotateAngleY = 0.0F;
		}

		switch(this.rightArmPose) {
		case EMPTY:
			this.bipedRightArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
			this.bipedRightArm.rotateAngleY = (-(float)Math.PI / 6F);
			break;
		case ITEM:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
			this.bipedRightArm.rotateAngleY = 0.0F;
			break;
		case THROW_SPEAR:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - (float)Math.PI;
			this.bipedRightArm.rotateAngleY = 0.0F;
		}

		if (this.leftArmPose == BipedModel.ArmPose.THROW_SPEAR && this.rightArmPose != BipedModel.ArmPose.BLOCK && this.rightArmPose != BipedModel.ArmPose.THROW_SPEAR && this.rightArmPose != BipedModel.ArmPose.BOW_AND_ARROW) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - (float)Math.PI;
			this.bipedLeftArm.rotateAngleY = 0.0F;
		}

		if (this.swingProgress > 0.0F) {
			HandSide handside = this.func_217147_a(entityIn);
			RendererModel renderermodel = this.getArmForSide(handside);
			float f1 = this.swingProgress;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float)Math.PI * 2F)) * 0.2F;
			if (handside == HandSide.LEFT) {
				this.bipedBody.rotateAngleY *= -1.0F;
			}

			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f1 = 1.0F - this.swingProgress;
			f1 = f1 * f1;
			f1 = f1 * f1;
			f1 = 1.0F - f1;
			float f2 = MathHelper.sin(f1 * (float)Math.PI);
			float f3 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			renderermodel.rotateAngleX = (float)((double)renderermodel.rotateAngleX - ((double)f2 * 1.2D + (double)f3));
			renderermodel.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			renderermodel.rotateAngleZ += MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4F;
		}

		if (this.isSneak) {
			this.bipedBody.rotateAngleX = 0.5F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedRightLeg.rotationPointZ = 0.0F;
			this.bipedLeftLeg.rotationPointZ = 0.0F;
			this.bipedRightLeg.rotationPointY = 5.0F;
			this.bipedLeftLeg.rotationPointY = 5.0F;
			this.bipedHead.rotationPointY = 4.0F;
		} else {
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0F;
			this.bipedLeftLeg.rotationPointZ = 0F;
			this.bipedRightLeg.rotationPointY = 5.0F;
			this.bipedLeftLeg.rotationPointY = 5.0F;
			this.bipedHead.rotationPointY = 4.0F;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		if (this.rightArmPose == BipedModel.ArmPose.BOW_AND_ARROW) {
		this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
		this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY + 0.4F;
		this.bipedRightArm.rotateAngleX = (-(float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
		this.bipedLeftArm.rotateAngleX = (-(float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
		} else if (this.leftArmPose == BipedModel.ArmPose.BOW_AND_ARROW && this.rightArmPose != BipedModel.ArmPose.THROW_SPEAR && this.rightArmPose != BipedModel.ArmPose.BLOCK) {
		this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY - 0.4F;
		this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY;
		this.bipedRightArm.rotateAngleX = (-(float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
		this.bipedLeftArm.rotateAngleX = (-(float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
		}

		float f4 = (float)CrossbowItem.getChargeTime(entityIn.getActiveItemStack());
		if (this.rightArmPose == BipedModel.ArmPose.CROSSBOW_CHARGE) {
			this.bipedRightArm.rotateAngleY = -0.8F;
			this.bipedRightArm.rotateAngleX = -0.97079635F;
			this.bipedLeftArm.rotateAngleX = -0.97079635F;
			float f5 = MathHelper.clamp(this.remainingItemUseTime2, 0.0F, f4);
			this.bipedLeftArm.rotateAngleY = MathHelper.lerp(f5 / f4, 0.4F, 0.85F);
			this.bipedLeftArm.rotateAngleX = MathHelper.lerp(f5 / f4, this.bipedLeftArm.rotateAngleX, (-(float)Math.PI / 2F));
		} else if (this.leftArmPose == BipedModel.ArmPose.CROSSBOW_CHARGE) {
			this.bipedLeftArm.rotateAngleY = 0.8F;
			this.bipedRightArm.rotateAngleX = -0.97079635F;
			this.bipedLeftArm.rotateAngleX = -0.97079635F;
			float f6 = MathHelper.clamp(this.remainingItemUseTime2, 0.0F, f4);
			this.bipedRightArm.rotateAngleY = MathHelper.lerp(f6 / f4, -0.4F, -0.85F);
			this.bipedRightArm.rotateAngleX = MathHelper.lerp(f6 / f4, this.bipedRightArm.rotateAngleX, (-(float)Math.PI / 2F));
		}

		if (this.rightArmPose == BipedModel.ArmPose.CROSSBOW_HOLD && this.swingProgress <= 0.0F) {
			this.bipedRightArm.rotateAngleY = -0.3F + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.6F + this.bipedHead.rotateAngleY;
			this.bipedRightArm.rotateAngleX = (-(float)Math.PI / 2F) + this.bipedHead.rotateAngleX + 0.1F;
			this.bipedLeftArm.rotateAngleX = -1.5F + this.bipedHead.rotateAngleX;
		} else if (this.leftArmPose == BipedModel.ArmPose.CROSSBOW_HOLD) {
			this.bipedRightArm.rotateAngleY = -0.6F + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.3F + this.bipedHead.rotateAngleY;
			this.bipedRightArm.rotateAngleX = -1.5F + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = (-(float)Math.PI / 2F) + this.bipedHead.rotateAngleX + 0.1F;
		}

		if (this.swimAnimation > 0.0F) {
			float f7 = limbSwing % 26.0F;
			float f8 = this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
			if (f7 < 14.0F) {
				this.bipedLeftArm.rotateAngleX = this.func_205060_a(this.bipedLeftArm.rotateAngleX, 0.0F, this.swimAnimation);
				this.bipedRightArm.rotateAngleX = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleX, 0.0F);
				this.bipedLeftArm.rotateAngleY = this.func_205060_a(this.bipedLeftArm.rotateAngleY, (float)Math.PI, this.swimAnimation);
				this.bipedRightArm.rotateAngleY = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleY, (float)Math.PI);
				this.bipedLeftArm.rotateAngleZ = this.func_205060_a(this.bipedLeftArm.rotateAngleZ, (float)Math.PI + 1.8707964F * this.getRotateAdditional(f7) / this.getRotateAdditional(14.0F), this.swimAnimation);
				this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, (float)Math.PI - 1.8707964F * this.getRotateAdditional(f7) / this.getRotateAdditional(14.0F));
			} else if (f7 >= 14.0F && f7 < 22.0F) {
				float f10 = (f7 - 14.0F) / 8.0F;
				this.bipedLeftArm.rotateAngleX = this.func_205060_a(this.bipedLeftArm.rotateAngleX, ((float)Math.PI / 2F) * f10, this.swimAnimation);
				this.bipedRightArm.rotateAngleX = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleX, ((float)Math.PI / 2F) * f10);
				this.bipedLeftArm.rotateAngleY = this.func_205060_a(this.bipedLeftArm.rotateAngleY, (float)Math.PI, this.swimAnimation);
				this.bipedRightArm.rotateAngleY = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleY, (float)Math.PI);
				this.bipedLeftArm.rotateAngleZ = this.func_205060_a(this.bipedLeftArm.rotateAngleZ, 5.012389F - 1.8707964F * f10, this.swimAnimation);
				this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, 1.2707963F + 1.8707964F * f10);
			} else if (f7 >= 22.0F && f7 < 26.0F) {
				float f9 = (f7 - 22.0F) / 4.0F;
				this.bipedLeftArm.rotateAngleX = this.func_205060_a(this.bipedLeftArm.rotateAngleX, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f9, this.swimAnimation);
				this.bipedRightArm.rotateAngleX = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleX, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f9);
				this.bipedLeftArm.rotateAngleY = this.func_205060_a(this.bipedLeftArm.rotateAngleY, (float)Math.PI, this.swimAnimation);
				this.bipedRightArm.rotateAngleY = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleY, (float)Math.PI);
				this.bipedLeftArm.rotateAngleZ = this.func_205060_a(this.bipedLeftArm.rotateAngleZ, (float)Math.PI, this.swimAnimation);
				this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, (float)Math.PI);
			}

			float f11 = 0.3F;
			float f12 = 0.33333334F;
			this.bipedLeftLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedLeftLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F + (float)Math.PI));
			this.bipedRightLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedRightLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F));
		}

		this.bipedHeadwear.copyModelAngles(this.bipedHead);
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		this.remainingItemUseTime2 = (float)entityIn.getItemInUseMaxCount();
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	private float getRotateAdditional(float swing) {
		return -65.0F * swing + swing * swing;
	}


	public void setVisible(EntityWanderingPeople wander) {
		boolean visible = true;
		if (wander.getActivePotionEffect(Effects.INVISIBILITY) != null) {
			visible = false;
		}
		this.bipedHead.showModel = visible;
		this.bipedBody.showModel = visible;
		this.bipedRightArm.showModel = visible;
		this.bipedLeftArm.showModel = visible;
		this.bipedRightLeg.showModel = visible;
		this.bipedLeftLeg.showModel = visible;
		this.skirt.showModel = visible;
		int modelType = (wander.getModelType()%11)+1;
		boolean isCheak = wander.isIntarest();

		if (modelType == 3 || modelType == 4 || modelType == 6 || modelType == 7 || modelType == 9 || modelType == 10) {
			this.hariR1.showModel = visible;
			this.hairR2.showModel = visible;
			this.hairR3.showModel = visible;
			this.hairR4.showModel = visible;
			this.hariR5.showModel = visible;
		}else {
			this.hariR1.showModel = false;
			this.hairR2.showModel = false;
			this.hairR3.showModel = false;
			this.hairR4.showModel = false;
			this.hariR5.showModel = false;
		}

		if (modelType == 2 || modelType == 4 || modelType == 5 || modelType == 7 || modelType == 8 || modelType == 10) {
			this.hariL1.showModel = visible;
			this.hairL2.showModel = visible;
			this.hairL3.showModel = visible;
			this.hairL4.showModel = visible;
			this.hairL5.showModel = visible;
		}else {
			this.hariL1.showModel = false;
			this.hairL2.showModel = false;
			this.hairL3.showModel = false;
			this.hairL4.showModel = false;
			this.hairL5.showModel = false;
		}

		if (modelType == 1 || modelType == 5 || modelType == 6 || modelType == 7) {
			this.backHair1.showModel = visible;
			this.backHair3.showModel = visible;
			this.backHair2.showModel = visible;
		}else {
			this.backHair1.showModel = false;
			this.backHair3.showModel = false;
			this.backHair2.showModel = false;
		}

		if (modelType == 8 || modelType == 9 || modelType == 10 || modelType == 11) {
			this.ponytail1.showModel = visible;
			this.ponytail2.showModel = visible;
			this.ponytail3.showModel = visible;
		}else {
			this.ponytail1.showModel = false;
			this.ponytail2.showModel = false;
			this.ponytail3.showModel = false;
		}

		if (isCheak) {
			this.cheak.showModel = visible;
		}else {
			this.cheak.showModel = false;
		}
	}

	@Override
	public ResourceLocation getTexture(EntityWanderingPeople entity) {
		int modelType = entity.getModelType2();
		int slot = modelType%textures.size();
		return textures.get(slot);
	}

}