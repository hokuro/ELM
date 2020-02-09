package mod.elm.render.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;

public class ModelCube extends Model {
	RendererModel Shape1;

	public ModelCube() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.Shape1 = new RendererModel(this, 0, 0);
		this.Shape1.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
		this.Shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Shape1.setTextureSize(64, 32);
		this.Shape1.mirror = true;
		setRotationAngles( 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, null);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.Shape1.render(f5);
	}

	public void render(float f5){
		this.Shape1.render(f5);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn){
		Shape1.rotateAngleX = limbSwing;
		Shape1.rotateAngleY = limbSwingAmount;
		Shape1.rotateAngleZ = ageInTicks;
	}
}
