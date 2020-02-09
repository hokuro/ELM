package mod.elm.render.model;

import mod.elm.entity.EntityElmStomach;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelStomach extends Model {

    //fields
    public RendererModel e1;
    public RendererModel e2;
    public RendererModel e3;
    public RendererModel e4;
    public RendererModel e5;
    public RendererModel e6;
    public RendererModel e7;
    public RendererModel e8;
    public RendererModel e9;
    public RendererModel e10;
    public RendererModel e11;
    public RendererModel e12;

    public ModelStomach() {
        textureWidth = 64;
        textureHeight = 64;

        e1 = new RendererModel(this, 0, 3);
        e1.setRotationPoint(0F, 0F, 0F);
        e1.addBox(-1F, -2F, 6F, 1, 2, 1);
        e1.setTextureSize(64, 64);
        e1.mirror = false;
        setRotation(e1, 0F, 0F, 0F);
        e2 = new RendererModel(this, 8, 13);
        e2.setRotationPoint(0F, 0F, 0F);
        e2.addBox(-1F, -3F, 5F, 1, 7, 1);
        e2.setTextureSize(64, 64);
        e2.mirror = false;
        setRotation(e2, 0F, 0F, 0F);
        e3 = new RendererModel(this, 24, 23);
        e3.setRotationPoint(0F, 0F, 0F);
        e3.addBox(-1F, -3F, 4F, 1, 8, 1);
        e3.setTextureSize(64, 64);
        e3.mirror = false;
        setRotation(e3, 0F, 0F, 0F);
        e4 = new RendererModel(this, 28, 23);
        e4.setRotationPoint(0F, 0F, 0F);
        e4.addBox(-1F, -2F, 3F, 1, 8, 1);
        e4.setTextureSize(64, 64);
        e4.mirror = false;
        setRotation(e4, 0F, 0F, 0F);
        e5 = new RendererModel(this, 16, 22);
        e5.setRotationPoint(0F, 0F, 0F);
        e5.addBox(-1F, -2F, 2F, 1, 9, 1);
        e5.setTextureSize(64, 64);
        e5.mirror = false;
        setRotation(e5, 0F, 0F, 0F);
        e6 = new RendererModel(this, 8, 21);
        e6.setRotationPoint(0F, 0F, 0F);
        e6.addBox(-1F, -2F, 1F, 1, 10, 1);
        e6.setTextureSize(64, 64);
        e6.mirror = false;
        setRotation(e6, 0F, 0F, 0F);
        e7 = new RendererModel(this, 0, 18);
        e7.setRotationPoint(0F, 0F, 0F);
        e7.addBox(-1F, -2F, -2F, 1, 11, 3);
        e7.setTextureSize(64, 64);
        e7.mirror = false;
        setRotation(e7, 0F, 0F, 0F);
        e8 = new RendererModel(this, 12, 21);
        e8.setRotationPoint(0F, 0F, 0F);
        e8.addBox(-1F, -1F, -3F, 1, 10, 1);
        e8.setTextureSize(64, 64);
        e8.mirror = false;
        setRotation(e8, 0F, 0F, 0F);
        e9 = new RendererModel(this, 0, 6);
        e9.setRotationPoint(0F, 0F, 0F);
        e9.addBox(-1F, -1F, -4F, 1, 11, 1);
        e9.setTextureSize(64, 64);
        e9.mirror = false;
        setRotation(e9, 0F, 0F, 0F);
        e10 = new RendererModel(this, 4, 6);
        e10.setRotationPoint(0F, 0F, 0F);
        e10.addBox(-1F, 0F, -5F, 1, 11, 1);
        e10.setTextureSize(64, 64);
        e10.mirror = false;
        setRotation(e10, 0F, 0F, 0F);
        e11 = new RendererModel(this, 20, 22);
        e11.setRotationPoint(0F, 0F, 0F);
        e11.addBox(-1F, 2F, -6F, 1, 9, 1);
        e11.setTextureSize(64, 64);
        e11.mirror = false;
        setRotation(e11, 0F, 0F, 0F);
        e12 = new RendererModel(this, 0, 0);
        e12.setRotationPoint(0F, 0F, 0F);
        e12.addBox(-1F, 8F, -7F, 1, 2, 1);
        e12.setTextureSize(64, 64);
        e12.mirror = false;
        setRotation(e12, 0F, 0F, 0F);

        e1.addChild(e2);
        e1.addChild(e3);
        e1.addChild(e4);
        e1.addChild(e5);
        e1.addChild(e6);
        e1.addChild(e7);
        e1.addChild(e8);
        e1.addChild(e9);
        e1.addChild(e10);
        e1.addChild(e11);
        e1.addChild(e12);
    }

    public void render(EntityElmStomach entity, float f5) {
        e1.render(f5);
    }

    public void setRotation(RendererModel parts, float x, float y, float z) {
    	parts.rotateAngleX = x;
    	parts.rotateAngleY = y;
    	parts.rotateAngleZ = z;
    }
}