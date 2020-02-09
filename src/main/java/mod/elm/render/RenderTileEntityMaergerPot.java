package mod.elm.render;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.render.model.ModelMaergerPot;
import mod.elm.tileentity.TileEntityMaergerPot;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityMaergerPot extends TileEntityRenderer<TileEntityMaergerPot> {
	private static final ResourceLocation tex_0 = new ResourceLocation("elm:textures/entity/pot/margerpot_0.png");
	private static final ResourceLocation tex_1 = new ResourceLocation("elm:textures/entity/pot/margerpot_1.png");
	private static final ResourceLocation tex_2 = new ResourceLocation("elm:textures/entity/pot/margerpot_2.png");
	private static final ResourceLocation tex_3 = new ResourceLocation("elm:textures/entity/pot/margerpot_3.png");

	private ModelMaergerPot mainModel = new ModelMaergerPot();

	@Override
	public void render(TileEntityMaergerPot te, double x, double y, double z, float partialTicks, int destroyStage) {
		renderFlapeMaker((TileEntityMaergerPot)te,x,y,z,partialTicks,destroyStage);
	}


	public void renderFlapeMaker(TileEntityMaergerPot te, double x, double y, double z, float partialTicks, int destroyStage) {

		int blc = te.getBloodCount();
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(5.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
	    	if (blc > 7) {
	    		this.bindTexture(tex_3);
			}else if (blc > 4) {
	    		this.bindTexture(tex_2);
			}else if (blc > 1) {
	    		this.bindTexture(tex_1);
			}else {
	    		this.bindTexture(tex_0);
			}
        }

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.translatef((float) x + 0.5F , (float) y + 1.0F, (float) z + 0.5F);
		GlStateManager.scaled(0.0625D,0.0625D,0.0625D);
		GlStateManager.rotatef(180,0F,0F,1F);
		GlStateManager.enableCull();
		GlStateManager.enableRescaleNormal();
		this.mainModel.render(te, 1.0F, blc > 0);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
	}
}
