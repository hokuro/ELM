package mod.elm.render.model;

import mod.elm.entity.EntityElmHand;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelHand extends Model{
	RendererModel hand;
	RendererModel f1;
	RendererModel f2;
	RendererModel f3;
	RendererModel f4;
	RendererModel f5;

	public ModelHand(ModelLRKind kind) {
		textureWidth = 64;
		textureHeight = 32;

		if (kind == ModelLRKind.LEFT) {
			hand = new RendererModel(this, 0, 0);
			hand.addBox(-6F, -10F, 0F, 12, 10, 1);
			hand.setRotationPoint(0F, 0F, 0F);
			hand.setTextureSize(64, 32);
			hand.mirror = true;
			setRotation(hand, 0F, 0F, 0F);
			f1 = new RendererModel(this, 27, 0);
			f1.addBox(-8F, -11F, 0F, 2, 12, 1);
			f1.setRotationPoint(0F, -1F, 0F);
			f1.setTextureSize(64, 32);
			f1.mirror = true;
			setRotation(f1, 0F, 0F, 0F);
			f2 = new RendererModel(this, 34, 0);
			f2.addBox(-5F, -15F, 0F, 2, 5, 1);
			f2.setRotationPoint(0F, 0F, 0F);
			f2.setTextureSize(64, 32);
			f2.mirror = true;
			setRotation(f2, 0F, 0F, 0F);
			f3 = new RendererModel(this, 41, 0);
			f3.addBox(-2F, -16F, 0F, 2, 6, 1);
			f3.setRotationPoint(0F, 0F, 0F);
			f3.setTextureSize(64, 32);
			f3.mirror = true;
			setRotation(f3, 0F, 0F, 0F);
			f4 = new RendererModel(this, 48, 0);
			f4.addBox(1F, -15F, 0F, 2, 5, 1);
			f4.setRotationPoint(0F, 0F, 0F);
			f4.setTextureSize(64, 32);
			f4.mirror = true;
			setRotation(f4, 0F, 0F, 0F);
			f5 = new RendererModel(this, 55, 0);
			f5.addBox(4F, -13F, 0F, 2, 3, 1);
			f5.setRotationPoint(0F, 0F, 0F);
			f5.setTextureSize(64, 32);
			f5.mirror = true;
			setRotation(f5, 0F, 0F, 0F);
		} else {
			hand = new RendererModel(this, 0, 0);
			hand.addBox(-8F, -10F, 0F, 12, 10, 1);
			hand.setRotationPoint(0F, 0F, 0F);
			hand.setTextureSize(64, 32);
			hand.mirror = true;
			setRotation(hand, 0F, 0F, 0F);
			f1 = new RendererModel(this, 27, 0);
			f1.addBox(4F, -11F, 0F, 2, 12, 1);
			f1.setRotationPoint(0F, -1F, 0F);
			f1.setTextureSize(64, 32);
			f1.mirror = true;
			setRotation(f1, 0F, 0F, 0F);
			f2 = new RendererModel(this, 34, 0);
			f2.addBox(1F, -15F, 0F, 2, 5, 1);
			f2.setRotationPoint(0F, 0F, 0F);
			f2.setTextureSize(64, 32);
			f2.mirror = true;
			setRotation(f2, 0F, 0F, 0F);
			f3 = new RendererModel(this, 41, 0);
			f3.addBox(-2F, -16F, 0F, 2, 6, 1);
			f3.setRotationPoint(0F, 0F, 0F);
			f3.setTextureSize(64, 32);
			f3.mirror = true;
			setRotation(f3, 0F, 0F, 0F);
			f4 = new RendererModel(this, 48, 0);
			f4.addBox(-5F, -15F, 0F, 2, 5, 1);
			f4.setRotationPoint(0F, 0F, 0F);
			f4.setTextureSize(64, 32);
			f4.mirror = true;
			setRotation(f4, 0F, 0F, 0F);
			f5 = new RendererModel(this, 55, 0);
			f5.addBox(-8F, -13F, 0F, 2, 3, 1);
			f5.setRotationPoint(0F, 0F, 0F);
			f5.setTextureSize(64, 32);
			f5.mirror = true;
			setRotation(f5, 0F, 0F, 0F);
		}
		hand.addChild(f1);
		hand.addChild(f2);
		hand.addChild(f3);
		hand.addChild(f4);
		hand.addChild(f5);
	}

	public void render(EntityElmHand entity, float scale) {
		hand.render(scale);
	}

	private void setRotation(RendererModel model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
