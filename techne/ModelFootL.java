// Date: 2019/11/12 21:14:09
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.minecraft.src;

public class ModelFoot extends ModelBase
{
  //fields
    ModelRenderer foot1;
    ModelRenderer foot2;
    ModelRenderer foot3;
    ModelRenderer f1;
    ModelRenderer f2;
    ModelRenderer f3;
    ModelRenderer f4;
    ModelRenderer f5;
  
  public ModelFoot()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      foot1 = new ModelRenderer(this, 0, 0);
      foot1.addBox(-7F, -1F, -8F, 15, 2, 13);
      foot1.setRotationPoint(0F, 0F, 0F);
      foot1.setTextureSize(64, 32);
      foot1.mirror = true;
      setRotation(foot1, 0F, 0F, 0F);
      foot2 = new ModelRenderer(this, 1, 1);
      foot2.addBox(-7F, -2F, -8F, 15, 1, 12);
      foot2.setRotationPoint(0F, 0F, 0F);
      foot2.setTextureSize(64, 32);
      foot2.mirror = true;
      setRotation(foot2, 0F, 0F, 0F);
      foot3 = new ModelRenderer(this, 2, 2);
      foot3.addBox(-7F, -3F, -8F, 15, 1, 11);
      foot3.setRotationPoint(0F, 0F, 0F);
      foot3.setTextureSize(64, 32);
      foot3.mirror = true;
      setRotation(foot3, 0F, 0F, 0F);
      f1 = new ModelRenderer(this, 42, 16);
      f1.addBox(5F, -1F, 5F, 3, 2, 3);
      f1.setRotationPoint(0F, 0F, 0F);
      f1.setTextureSize(64, 32);
      f1.mirror = true;
      setRotation(f1, 0F, 0F, 0F);
      f2 = new ModelRenderer(this, 31, 16);
      f2.addBox(2F, -1F, 5F, 2, 2, 3);
      f2.setRotationPoint(0F, 0F, 0F);
      f2.setTextureSize(64, 32);
      f2.mirror = true;
      setRotation(f2, 0F, 0F, 0F);
      f3 = new ModelRenderer(this, 20, 16);
      f3.addBox(-1F, -1F, 5F, 2, 2, 3);
      f3.setRotationPoint(0F, 0F, 0F);
      f3.setTextureSize(64, 32);
      f3.mirror = true;
      setRotation(f3, 0F, 0F, 0F);
      f4 = new ModelRenderer(this, 9, 16);
      f4.addBox(-4F, -1F, 5F, 2, 2, 3);
      f4.setRotationPoint(0F, 0F, 0F);
      f4.setTextureSize(64, 32);
      f4.mirror = true;
      setRotation(f4, 0F, 0F, 0F);
      f5 = new ModelRenderer(this, 0, 16);
      f5.addBox(-7F, -1F, 5F, 2, 2, 2);
      f5.setRotationPoint(0F, 0F, 0F);
      f5.setTextureSize(64, 32);
      f5.mirror = true;
      setRotation(f5, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    foot1.render(f5);
    foot2.render(f5);
    foot3.render(f5);
    f1.render(f5);
    f2.render(f5);
    f3.render(f5);
    f4.render(f5);
    f5.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5);
  }

}
