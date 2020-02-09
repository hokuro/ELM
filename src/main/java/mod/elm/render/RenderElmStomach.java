package mod.elm.render;

import org.lwjgl.opengl.GL11;

import mod.elm.entity.EntityElmStomach;
import mod.elm.render.model.ModelStomach;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderElmStomach extends EntityRenderer<EntityElmStomach> {
	private static final ResourceLocation tex = new ResourceLocation("elm","textures/entity/item/stomach.png");
	private ModelStomach model;

	public RenderElmStomach(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.5F;
		this.model = new ModelStomach();
	}


	@Override
	public void doRender(EntityElmStomach entity, double d, double d1, double d2, float f, float f1) {
		RenderEntityStomach((EntityElmStomach) entity, d, d1, d2, f, f1);
	}

	public void RenderEntityStomach(EntityElmStomach entitybomb, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glScalef(0.625F, 0.625F, 0.625F);

		this.renderManager.textureManager.bindTexture(getEntityTexture(entitybomb));
		this.model.render(entitybomb, 0.0625F);

		GL11.glRotated(1, 0, entitybomb.getCntTime(), 0);

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityElmStomach entity) {
		return tex;
	}
}
