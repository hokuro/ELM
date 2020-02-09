package mod.elm.render;

import org.lwjgl.opengl.GL11;

import mod.elm.entity.EntityElmFoot;
import mod.elm.render.model.ModelFoot;
import mod.elm.render.model.ModelLRKind;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;

public class RenderElmFoot extends EntityRenderer<EntityElmFoot> {
	private static final ResourceLocation tex = new ResourceLocation("elm","textures/entity/item/foot.png");
	private ModelFoot modelL;
	private ModelFoot modelR;

	public RenderElmFoot(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.0F;
		modelL = new ModelFoot(ModelLRKind.LEFT);
		modelR = new ModelFoot(ModelLRKind.RIGHT);

	}

	@Override
	public void doRender(EntityElmFoot entity, double d, double d1, double d2, float f, float f1) {
		RenderEntity(entity, d, d1, d2, f, f1);
	}

	public void RenderEntity(EntityElmFoot entity, double d, double d1, double d2, float f, float f1) {

		if (entity.getShowTime() < entity.getTickTime()) {
			GL11.glPushMatrix();

			// iサイズ調整
			GL11.glTranslatef((float) d, (float) d1, (float) d2);

			EntitySize size = entity.getSize(Pose.STANDING);
			float scale =0.625F;
			GL11.glScalef(size.width * 0.0625F, size.height * 0.0625F, size.width * 0.0625F);
			// i角度調整
			double yaw = entity.getPitchYaw().y;
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(yaw+180, 0, 1, 0);

			this.renderManager.textureManager.bindTexture(getEntityTexture(entity));
			if (entity.getLRKind() == ModelLRKind.LEFT) {
				modelL.render(entity, scale);
			} else {
				modelR.render(entity, scale);
			}
			GL11.glPopMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityElmFoot entity) {
		return tex;
	}
}