// Cubik Studio 2.9.480 Beta JAVA exporter
// Designed by Ohonou with Cubik Studio - https://cubik.studio

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CubikModel extends ModelBase {

    //fields
    public ModelRenderer e1;
    public ModelRenderer e2;
    public ModelRenderer e3;
    public ModelRenderer e4;
    public ModelRenderer e5;
    public ModelRenderer e6;
    public ModelRenderer e7;
    public ModelRenderer e8;
    public ModelRenderer e9;
    public ModelRenderer e10;
    public ModelRenderer e11;
    public ModelRenderer e12;

    public CubikModel()
    {
        textureWidth = 64;
        textureHeight = 64;

        e1 = new ModelRenderer(this, 0, 3);
        e1.setRotationPoint(-1F, 26F, 6F);
        e1.addBox(0F, -2F, 0F, 1, 2, 1);
        e1.setTextureSize(64, 64);
        e1.mirror = false;
        setRotation(e1, 0F, 0F, 0F);
        e2 = new ModelRenderer(this, 8, 13);
        e2.setRotationPoint(-1F, 25F, 5F);
        e2.addBox(0F, -7F, 0F, 1, 7, 1);
        e2.setTextureSize(64, 64);
        e2.mirror = false;
        setRotation(e2, 0F, 0F, 0F);
        e3 = new ModelRenderer(this, 24, 23);
        e3.setRotationPoint(-1F, 25F, 4F);
        e3.addBox(0F, -8F, 0F, 1, 8, 1);
        e3.setTextureSize(64, 64);
        e3.mirror = false;
        setRotation(e3, 0F, 0F, 0F);
        e4 = new ModelRenderer(this, 28, 23);
        e4.setRotationPoint(-1F, 26F, 3F);
        e4.addBox(0F, -8F, 0F, 1, 8, 1);
        e4.setTextureSize(64, 64);
        e4.mirror = false;
        setRotation(e4, 0F, 0F, 0F);
        e5 = new ModelRenderer(this, 16, 22);
        e5.setRotationPoint(-1F, 26F, 2F);
        e5.addBox(0F, -9F, 0F, 1, 9, 1);
        e5.setTextureSize(64, 64);
        e5.mirror = false;
        setRotation(e5, 0F, 0F, 0F);
        e6 = new ModelRenderer(this, 8, 21);
        e6.setRotationPoint(-1F, 26F, 1F);
        e6.addBox(0F, -10F, 0F, 1, 10, 1);
        e6.setTextureSize(64, 64);
        e6.mirror = false;
        setRotation(e6, 0F, 0F, 0F);
        e7 = new ModelRenderer(this, 0, 18);
        e7.setRotationPoint(-1F, 26F, -2F);
        e7.addBox(0F, -11F, 0F, 1, 11, 3);
        e7.setTextureSize(64, 64);
        e7.mirror = false;
        setRotation(e7, 0F, 0F, 0F);
        e8 = new ModelRenderer(this, 12, 21);
        e8.setRotationPoint(-1F, 27F, -3F);
        e8.addBox(0F, -10F, 0F, 1, 10, 1);
        e8.setTextureSize(64, 64);
        e8.mirror = false;
        setRotation(e8, 0F, 0F, 0F);
        e9 = new ModelRenderer(this, 0, 6);
        e9.setRotationPoint(-1F, 27F, -4F);
        e9.addBox(0F, -11F, 0F, 1, 11, 1);
        e9.setTextureSize(64, 64);
        e9.mirror = false;
        setRotation(e9, 0F, 0F, 0F);
        e10 = new ModelRenderer(this, 4, 6);
        e10.setRotationPoint(-1F, 28F, -5F);
        e10.addBox(0F, -11F, 0F, 1, 11, 1);
        e10.setTextureSize(64, 64);
        e10.mirror = false;
        setRotation(e10, 0F, 0F, 0F);
        e11 = new ModelRenderer(this, 20, 22);
        e11.setRotationPoint(-1F, 30F, -6F);
        e11.addBox(0F, -9F, 0F, 1, 9, 1);
        e11.setTextureSize(64, 64);
        e11.mirror = false;
        setRotation(e11, 0F, 0F, 0F);
        e12 = new ModelRenderer(this, 0, 0);
        e12.setRotationPoint(-1F, 36F, -7F);
        e12.addBox(0F, -2F, 0F, 1, 2, 1);
        e12.setTextureSize(64, 64);
        e12.mirror = false;
        setRotation(e12, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        e1.render(f5);
        e2.render(f5);
        e3.render(f5);
        e4.render(f5);
        e5.render(f5);
        e6.render(f5);
        e7.render(f5);
        e8.render(f5);
        e9.render(f5);
        e10.render(f5);
        e11.render(f5);
        e12.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
     
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
 
}