package mod.elm.render;

import org.lwjgl.opengl.GL11;

import mod.elm.entity.EntityElmHand;
import mod.elm.render.model.ModelHand;
import mod.elm.render.model.ModelLRKind;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;

public class RenderElmHand extends EntityRenderer<EntityElmHand> {
	private static final ResourceLocation tex = new ResourceLocation("elm","textures/entity/item/hand.png");
	private ModelHand modelL;
	private ModelHand modelR;

	public RenderElmHand(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.0F;
		modelL = new ModelHand(ModelLRKind.LEFT);
		modelR = new ModelHand(ModelLRKind.RIGHT);

	}

	@Override
	public void doRender(EntityElmHand entity, double d, double d1, double d2, float f, float f1) {
		RenderEntity(entity, d, d1, d2, f, f1);
	}

	public void RenderEntity(EntityElmHand entity, double x, double y, double z, float f, float f1) {

		if (entity.getShowTime() < entity.getTickTime()){
			GL11.glPushMatrix();
			// iサイズ調整
			GL11.glTranslatef((float)x,(float)y,(float)z);

			EntitySize size = entity.getSize(Pose.STANDING);
			float scale = 0.0625F;
			GL11.glScalef(size.width + 0.625F, size.height + 0.625F, size.width + 0.625F);
			// i角度調整
			double yaw = entity.getPitchYaw().y;
			double pit = entity.getPitchYaw().x;
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(yaw, 0, 1, 0);
			GL11.glRotated(pit, 1, 0, 0);

			this.renderManager.textureManager.bindTexture(getEntityTexture(entity));
			if (entity.getLRKind() == ModelLRKind.LEFT) {
				GL11.glRotated(15, 0, 0, 1);
				modelL.render(entity, scale);
			} else {
				GL11.glRotated(-15, 0, 0, 1);
				modelR.render(entity, scale);
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityElmHand entity) {
		return tex;
	}
}
