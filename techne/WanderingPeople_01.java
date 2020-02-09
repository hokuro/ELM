// Date: 2019/10/26 14:03:46
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.minecraft.src;

public class ModelNew extends ModelBase
{
  //fields
    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer rightarm;
    ModelRenderer leftarm;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
    ModelRenderer skirt;
    ModelRenderer Shape1;
  
  public ModelNew()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      head = new ModelRenderer(this, 0, 0);
      head.addBox(-5F, -12F, -5F, 8, 8, 8);
      head.setRotationPoint(1F, 4F, 1F);
      head.setTextureSize(128, 64);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
      body = new ModelRenderer(this, 32, 0);
      body.addBox(-3F, 0F, -2F, 6, 5, 4);
      body.setRotationPoint(0F, 0F, 0F);
      body.setTextureSize(128, 64);
      body.mirror = true;
      setRotation(body, 0F, 0F, 0F);
      rightarm = new ModelRenderer(this, 52, 0);
      rightarm.addBox(-2F, -1F, -1F, 2, 6, 2);
      rightarm.setRotationPoint(-3F, 1.5F, 0F);
      rightarm.setTextureSize(128, 64);
      rightarm.mirror = true;
      setRotation(rightarm, 0F, 0F, 0F);
      leftarm = new ModelRenderer(this, 60, 0);
      leftarm.addBox(3F, 1.5F, 0F, 2, 6, 2);
      leftarm.setRotationPoint(0F, -1F, -1F);
      leftarm.setTextureSize(128, 64);
      leftarm.mirror = true;
      setRotation(leftarm, 0F, 0F, 0F);
      rightleg = new ModelRenderer(this, 32, 19);
      rightleg.addBox(-2F, 0F, -2F, 3, 7, 4);
      rightleg.setRotationPoint(-1F, 5F, 0F);
      rightleg.setTextureSize(128, 64);
      rightleg.mirror = true;
      setRotation(rightleg, 0F, 0F, 0F);
      leftleg = new ModelRenderer(this, 46, 19);
      leftleg.addBox(-1F, 0F, -2F, 3, 7, 4);
      leftleg.setRotationPoint(1F, 5F, 0F);
      leftleg.setTextureSize(128, 64);
      leftleg.mirror = true;
      setRotation(leftleg, 0F, 0F, 0F);
      skirt = new ModelRenderer(this, 0, 16);
      skirt.addBox(-4F, -2F, -3F, 8, 6, 6);
      skirt.setRotationPoint(0F, 6F, 0F);
      skirt.setTextureSize(128, 64);
      skirt.mirror = true;
      setRotation(skirt, 0F, 0F, 0F);
      Shape1 = new ModelRenderer(this, 68, 0);
      Shape1.addBox(-5F, -4F, 3F, 8, 6, 0);
      Shape1.setRotationPoint(1F, 4F, 1F);
      Shape1.setTextureSize(128, 64);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    head.render(f5);
    body.render(f5);
    rightarm.render(f5);
    leftarm.render(f5);
    rightleg.render(f5);
    leftleg.render(f5);
    skirt.render(f5);
    Shape1.render(f5);
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
