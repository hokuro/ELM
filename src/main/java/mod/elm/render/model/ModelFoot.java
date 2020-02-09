package mod.elm.render.model;

import mod.elm.entity.EntityElmFoot;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelFoot extends Model {
	//fields
	RendererModel foot1;
	RendererModel foot2;
	RendererModel foot3;
	RendererModel f1;
	RendererModel f2;
	RendererModel f3;
	RendererModel f4;
	RendererModel f5;

	public ModelFoot(ModelLRKind kind) {
		textureWidth = 64;
		textureHeight = 32;

		if (kind == ModelLRKind.LEFT) {
			foot1 = new RendererModel(this, 0, 0);
			foot1.addBox(-7F, -1F, -8F, 15, 2, 13);
			foot1.setRotationPoint(0F, 0F, 0F);
			foot1.setTextureSize(64, 32);
			foot1.mirror = true;
			setRotation(foot1, 0F, 0F, 0F);
			foot2 = new RendererModel(this, 1, 1);
			foot2.addBox(-7F, -2F, -8F, 15, 1, 12);
			foot2.setRotationPoint(0F, 0F, 0F);
			foot2.setTextureSize(64, 32);
			foot2.mirror = true;
			setRotation(foot2, 0F, 0F, 0F);
			foot3 = new RendererModel(this, 2, 2);
			foot3.addBox(-7F, -3F, -8F, 15, 1, 11);
			foot3.setRotationPoint(0F, 0F, 0F);
			foot3.setTextureSize(64, 32);
			foot3.mirror = true;
			setRotation(foot3, 0F, 0F, 0F);
			f1 = new RendererModel(this, 42, 16);
			f1.addBox(5F, -1F, 5F, 3, 2, 3);
			f1.setRotationPoint(0F, 0F, 0F);
			f1.setTextureSize(64, 32);
			f1.mirror = true;
			setRotation(f1, 0F, 0F, 0F);
			f2 = new RendererModel(this, 31, 16);
			f2.addBox(2F, -1F, 5F, 2, 2, 3);
			f2.setRotationPoint(0F, 0F, 0F);
			f2.setTextureSize(64, 32);
			f2.mirror = true;
			setRotation(f2, 0F, 0F, 0F);
			f3 = new RendererModel(this, 20, 16);
			f3.addBox(-1F, -1F, 5F, 2, 2, 3);
			f3.setRotationPoint(0F, 0F, 0F);
			f3.setTextureSize(64, 32);
			f3.mirror = true;
			setRotation(f3, 0F, 0F, 0F);
			f4 = new RendererModel(this, 9, 16);
			f4.addBox(-4F, -1F, 5F, 2, 2, 3);
			f4.setRotationPoint(0F, 0F, 0F);
			f4.setTextureSize(64, 32);
			f4.mirror = true;
			setRotation(f4, 0F, 0F, 0F);
			f5 = new RendererModel(this, 0, 16);
			f5.addBox(-7F, -1F, 5F, 2, 2, 2);
			f5.setRotationPoint(0F, 0F, 0F);
			f5.setTextureSize(64, 32);
			f5.mirror = true;
			setRotation(f5, 0F, 0F, 0F);
		} else {
			foot1 = new RendererModel(this, 0, 0);
			foot1.addBox(-8F, -1F, -8F, 15, 2, 13);
			foot1.setRotationPoint(0F, 0F, 0F);
			foot1.setTextureSize(64, 32);
			foot1.mirror = true;
			setRotation(foot1, 0F, 0F, 0F);
			foot2 = new RendererModel(this, 1, 1);
			foot2.addBox(-8F, -2F, -8F, 15, 1, 12);
			foot2.setRotationPoint(0F, 0F, 0F);
			foot2.setTextureSize(64, 32);
			foot2.mirror = true;
			setRotation(foot2, 0F, 0F, 0F);
			foot3 = new RendererModel(this, 2, 2);
			foot3.addBox(-8F, -3F, -8F, 15, 1, 11);
			foot3.setRotationPoint(0F, 0F, 0F);
			foot3.setTextureSize(64, 32);
			foot3.mirror = true;
			setRotation(foot3, 0F, 0F, 0F);
			f1 = new RendererModel(this, 42, 16);
			f1.addBox(-8F, -1F, 5F, 3, 2, 3);
			f1.setRotationPoint(0F, 0F, 0F);
			f1.setTextureSize(64, 32);
			f1.mirror = true;
			setRotation(f1, 0F, 0F, 0F);
			f2 = new RendererModel(this, 31, 16);
			f2.addBox(-4F, -1F, 5F, 2, 2, 3);
			f2.setRotationPoint(0F, 0F, 0F);
			f2.setTextureSize(64, 32);
			f2.mirror = true;
			setRotation(f2, 0F, 0F, 0F);
			f3 = new RendererModel(this, 20, 16);
			f3.addBox(-1F, -1F, 5F, 2, 2, 3);
			f3.setRotationPoint(0F, 0F, 0F);
			f3.setTextureSize(64, 32);
			f3.mirror = true;
			setRotation(f3, 0F, 0F, 0F);
			f4 = new RendererModel(this, 9, 16);
			f4.addBox(2F, -1F, 5F, 2, 2, 3);
			f4.setRotationPoint(0F, 0F, 0F);
			f4.setTextureSize(64, 32);
			f4.mirror = true;
			setRotation(f4, 0F, 0F, 0F);
			f5 = new RendererModel(this, 0, 16);
			f5.addBox(5F, -1F, 5F, 2, 2, 2);
			f5.setRotationPoint(0F, 0F, 0F);
			f5.setTextureSize(64, 32);
			f5.mirror = true;
			setRotation(f5, 0F, 0F, 0F);
		}
		foot1.addChild(foot2);
		foot1.addChild(foot3);
		foot1.addChild(f1);
		foot1.addChild(f2);
		foot1.addChild(f3);
		foot1.addChild(f4);
		foot1.addChild(f5);
	}

	public void render(EntityElmFoot entity, float scale) {
		foot1.render(scale);
	}

	private void setRotation(RendererModel model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
