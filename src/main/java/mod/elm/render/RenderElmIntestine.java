package mod.elm.render;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.entity.EntityElmIntestine;
import mod.elm.render.model.ModelCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderElmIntestine extends EntityRenderer<EntityElmIntestine> {
	private static final ResourceLocation tex = new ResourceLocation("elm","textures/entity/item/intestine.png");
	private ModelCube model;

	public RenderElmIntestine(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.5F;
		this.model = new ModelCube();
	}


	@Override
	public void doRender(EntityElmIntestine entity, double d, double d1, double d2, float f, float f1) {
		RenderEntityIntestine((EntityElmIntestine) entity, d, d1, d2, f, f1);
	}

	public void RenderEntityIntestine(EntityElmIntestine entitybomb, double x, double y, double z, float entityYaw, float partialTicks) {
		LivingEntity livingentity = entitybomb.getThrower();
		if (livingentity == null) {
			livingentity = Minecraft.getInstance().player;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translatef((float)x, (float)y, (float)z);
		GL11.glScalef(0.5F * 0.625F, 0.5F * 0.625F, 0.5F * 0.625F);
		GL11.glRotated(entitybomb.getPitchYaw().y, 0, 1, 0);
		GL11.glRotated(entitybomb.getPitchYaw().x, -1, 0, 0);
		this.renderManager.textureManager.bindTexture(getEntityTexture(entitybomb));
		this.model.render(entitybomb, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		renderLeash(entitybomb,livingentity,x,y,z,entityYaw,partialTicks);
	}

	private Vec3d getPosition(Entity entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
		double d0 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.lastTickPosX, entityLivingBaseIn.posX);
		double d1 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.lastTickPosY, entityLivingBaseIn.posY) + p_177110_2_;
		double d2 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.lastTickPosZ, entityLivingBaseIn.posZ);
		return new Vec3d(d0, d1, d2);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityElmIntestine entity) {
		return tex;
	}


	protected void renderLeash(EntityElmIntestine entityLivingIn, LivingEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
	      if (entity != null) {
	         y = y - (1.6D - (double)entityLivingIn.getHeight()*0.25);
	         Tessellator tessellator = Tessellator.getInstance();
	         BufferBuilder bufferbuilder = tessellator.getBuffer();
	         double d0 = (double)(MathHelper.lerp(partialTicks * 0.5F, entity.rotationYaw, entity.prevRotationYaw) * ((float)Math.PI / 180F));
	         double d1 = (double)(MathHelper.lerp(partialTicks * 0.5F, entity.rotationPitch, entity.prevRotationPitch) * ((float)Math.PI / 180F));
	         double d2 = Math.cos(d0);
	         double d3 = Math.sin(d0);
	         double d4 = Math.sin(d1);

	         double d5 = Math.cos(d1);
	         double d6 = entity.posX;
	         double d7 = entity.posY;
	         double d8 = entity.posZ;
	         double d9 = (double)(MathHelper.lerp(partialTicks, entityLivingIn.rotationYaw, entityLivingIn.prevRotationYaw) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
	         d2 = Math.cos(d9) * (double)entityLivingIn.getWidth() * 0.4D;
	         d3 = Math.sin(d9) * (double)entityLivingIn.getWidth() * 0.4D;
	         double d10 = entityLivingIn.posX + d2;
	         double d11 = entityLivingIn.posY;
	         double d12 = entityLivingIn.posZ + d3;
	         x = x + d2;
	         z = z + d3;
	         double d13 = (double)((float)(d6 - d10));
	         double d14 = (double)((float)(d7 - d11));
	         double d15 = (double)((float)(d8 - d12));

//	         ModLog.log().debug("###");
//	         ModLog.log().debug(d6 + "," + d7 + "," + d8);
//	         ModLog.log().debug(d10 + "," + d11 + "," + d12);
//	         ModLog.log().debug(d13 + "," + d14 + "," + d15);
//	         ModLog.log().debug("###");
	         GlStateManager.disableTexture();
	         GlStateManager.disableLighting();
	         GlStateManager.disableCull();
	         int i = 24;
	         double d16 = 0.025D;
	         bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

	         for(int j = 0; j <= 24; ++j) {
	            float f3 = (float)j / 24.0F;
	            bufferbuilder.pos(x + d13 * (double)f3 + 0.0D, y + d14 * (double)(f3 * f3 + f3) * 0.5D + (double)((24.0F - (float)j) / 18.0F + 0.125F), z + d15 * (double)f3).color(0.701960784F, 0.243137255F, 0.360784314F, 1.0F).endVertex();
	            bufferbuilder.pos(x + d13 * (double)f3 + 0.1D, y + d14 * (double)(f3 * f3 + f3) * 0.5D + (double)((24.0F - (float)j) / 18.0F + 0.125F) + 0.1D, z + d15 * (double)f3).color(0.701960784F, 0.243137255F, 0.360784314F, 1.0F).endVertex();
	         }

	         tessellator.draw();
	         GlStateManager.enableLighting();
	         GlStateManager.enableTexture();
	         GlStateManager.enableCull();
	      }
	   }
}
