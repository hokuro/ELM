package mod.elm.render;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.entity.EntityElmBrain;
import mod.elm.render.model.ModelBrain;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public class RenderElmBrain extends EntityRenderer<EntityElmBrain> {
	private static final ResourceLocation tex = new ResourceLocation("elm","textures/entity/item/brain.png");
	private ModelBrain model;

	public RenderElmBrain(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.0F;
		model = new ModelBrain();

	}

	@Override
	public void doRender(EntityElmBrain entity, double d, double d1, double d2, float f, float f1) {
		RenderEntity(entity, d, d1, d2, f, f1);
	}

	public void RenderEntity(EntityElmBrain entity, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();

		// iサイズ調整
		GL11.glTranslatef((float) d, (float) d1, (float) d2);

		float scale =1.0F;
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		// i角度調整
		double yaw = entity.getPitchYaw().y;
		double pit = entity.getPitchYaw().x;
		GL11.glRotated(180, 1, 0, 0);
		GL11.glRotated(yaw, 0, 1, 0);
		GL11.glRotated(pit,0,0,1);

		if (entity.getPhase() == EntityElmBrain.PHASE_HUGGER) {
			int md = entity.getTickCount() % 30;
			if (md < 15) {
				float rate = 0.2F*md/14F;
				GL11.glScalef(1.0F - rate, 1.0F - rate, 1.0F - rate);
			} else {
				float rate = 0.2F*(15-(md-14F))/14F;
				GL11.glScalef(1.0F - rate, 1.0F - rate, 1.0F - rate);
			}
		}

		if (this.renderOutlines) {
			this.renderManager.textureManager.bindTexture(getEntityTexture(entity));
			model.render(entity, scale);
		}else {
			boolean flag1 = this.setBrightness(entity, f1, true);
			this.renderManager.textureManager.bindTexture(getEntityTexture(entity));
			model.render(entity, scale);
			if (flag1) {
				this.unsetBrightness();
			}
		}

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityElmBrain entity) {
		return tex;
	}

	protected final FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
	private static final DynamicTexture TEXTURE_BRIGHTNESS = Util.make(new DynamicTexture(16, 16, false), (p_203414_0_) -> {
		p_203414_0_.getTextureData().untrack();
		for(int i = 0; i < 16; ++i) {
			for(int j = 0; j < 16; ++j) {
				p_203414_0_.getTextureData().setPixelRGBA(j, i, -1);
			}
		}
		p_203414_0_.updateDynamicTexture();
	});

	protected boolean setBrightness(EntityElmBrain entitylivingbaseIn, float partialTicks, boolean combineTextures) {
		float f = entitylivingbaseIn.getBrightness();
		int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
		boolean flag = (i >> 24 & 255) > 0;
		boolean flag1 = entitylivingbaseIn.hurtTime > 0;
		if (!flag && !flag1) {
			return false;
		} else if (!flag && !combineTextures) {
			return false;
		} else {
			GlStateManager.activeTexture(GLX.GL_TEXTURE0);
			GlStateManager.enableTexture();
			GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_TEXTURE0);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PRIMARY_COLOR);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_TEXTURE0);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
			GlStateManager.activeTexture(GLX.GL_TEXTURE1);
			GlStateManager.enableTexture();
			GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, GLX.GL_INTERPOLATE);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_CONSTANT);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE2_RGB, GLX.GL_CONSTANT);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND2_RGB, 770);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
			this.brightnessBuffer.position(0);
			if (flag1) {
				this.brightnessBuffer.put(1.0F);
				this.brightnessBuffer.put(0.0F);
				this.brightnessBuffer.put(0.0F);
				this.brightnessBuffer.put(0.3F);
			} else {
				float f1 = (float)(i >> 24 & 255) / 255.0F;
				float f2 = (float)(i >> 16 & 255) / 255.0F;
				float f3 = (float)(i >> 8 & 255) / 255.0F;
				float f4 = (float)(i & 255) / 255.0F;
				this.brightnessBuffer.put(f2);
				this.brightnessBuffer.put(f3);
				this.brightnessBuffer.put(f4);
				this.brightnessBuffer.put(1.0F - f1);
			}

			this.brightnessBuffer.flip();
			GlStateManager.texEnv(8960, 8705, this.brightnessBuffer);
			GlStateManager.activeTexture(GLX.GL_TEXTURE2);
			GlStateManager.enableTexture();
			GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
			GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_TEXTURE1);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
			GlStateManager.activeTexture(GLX.GL_TEXTURE0);
			return true;
		}
	}

	protected void unsetBrightness() {
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
		GlStateManager.enableTexture();
		GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_TEXTURE0);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PRIMARY_COLOR);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 8448);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_TEXTURE0);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_ALPHA, GLX.GL_PRIMARY_COLOR);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_ALPHA, 770);
		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, 5890);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, 5890);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.activeTexture(GLX.GL_TEXTURE2);
		GlStateManager.disableTexture();
		GlStateManager.bindTexture(0);
		GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, 5890);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, 5890);
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
	}

	protected int getColorMultiplier(EntityElmBrain entitylivingbaseIn, float lightBrightness, float partialTickTime) {
		return 0;
	}
}