package mod.elm.render;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.render.model.ModelCube;
import mod.elm.tileentity.TileEntityElmHead;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RenderTileEntityElmHead extends TileEntityRenderer<TileEntityElmHead> {
	private static final ResourceLocation tex = new ResourceLocation("elm:textures/entity/tileentityelmhead/default_head.png");

	private ModelCube mainModel = new ModelCube();

	@Override
	public void render(TileEntityElmHead te, double x, double y, double z, float partialTicks, int destroyStage) {
		renderFreezer(te,x,y,z,partialTicks,destroyStage);
	}

	public void renderFreezer(TileEntityElmHead te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(5.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }else{
    		this.bindTexture(getTexture(te));
        }
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x + 0.5F , (float) y + 0.5F, (float) z + 0.5F);
		GlStateManager.scaled(0.0625,0.0625,0.0625D);
		GlStateManager.rotatef(180,0F,0F,1F);
		Direction vt = te.getVertical();
		Direction hr = te.getHorizontal();
		GlStateManager.rotated(90F * (hr.getHorizontalIndex()+2), 0, 1F, 0);
		GlStateManager.rotated(90F * (vt.getHorizontalIndex()-1), 1, 0, 0);
		if (vt == Direction.UP) {
			GlStateManager.rotatef( 90F, 1F,0F,0F);
		} else if (vt == Direction.DOWN) {
			GlStateManager.rotatef(-90F, 1F,0F,0F);
		}


		GlStateManager.enableCull();
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableLighting();
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 15 * 16, 15 * 16);
		this.mainModel.render(1.0F);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
	}

	public ResourceLocation getTexture(TileEntityElmHead te) {
		String name = te.getTargetName();
		ResourceLocation location1 = Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(name)).getRegistryName();
		ResourceLocation texLocation;
		if ("minecraft".equals(location1.getNamespace())){
			texLocation = new ResourceLocation("elm", "textures/entity/tileentityelmhead/" + location1.getPath() + ".png");
		} else {
			texLocation = new ResourceLocation("elm", "textures/entity/tileentityelmhead/" + location1.toString().replace(":", "_") + ".png");
		}
		ITextureObject texture = this.rendererDispatcher.textureManager.getTexture(texLocation);
		if (!(texture instanceof DynamicTexture)) {return texLocation;}
		return tex;
	}

}
