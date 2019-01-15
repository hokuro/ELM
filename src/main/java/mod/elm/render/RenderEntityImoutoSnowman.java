package mod.elm.render;

import mod.elm.core.ModCommon;
import mod.elm.entity.passive.EntityImoutoSnowman;
import mod.elm.model.ModelEntityImoutoSnowman;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityImoutoSnowman extends RenderLiving<EntityImoutoSnowman> {

	 private static final ResourceLocation IMOUTOIRON_GOLEM_TEXTURES = new ResourceLocation(ModCommon.MOD_ID, "textures/entity/imoutosnowman.png");
	 private static final ResourceLocation ROTTENIMOUTOIRON_GOLEM_TEXTURES = new ResourceLocation(ModCommon.MOD_ID, "textures/entity/imoutosnowmanrotten.png");
	 private static final ResourceLocation SKELTOMIMOUTOIRON_GOLEM_TEXTURES = new ResourceLocation(ModCommon.MOD_ID, "textures/entity/imoutosnowmanskelton.png");


	 public RenderEntityImoutoSnowman(RenderManager renderManagerIn)
	 {
		 super(renderManagerIn, new ModelEntityImoutoSnowman(), 0.5F);
	 }

	 /**
      * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
      */
	 public ResourceLocation getEntityTexture(EntityImoutoSnowman entity)
	 {
		 switch(entity.getRotten()){
			case NORMAL:
				return IMOUTOIRON_GOLEM_TEXTURES;
			case ROTTEN:
				return ROTTENIMOUTOIRON_GOLEM_TEXTURES;
			case SKALL:
				return SKELTOMIMOUTOIRON_GOLEM_TEXTURES;
			default:
				return IMOUTOIRON_GOLEM_TEXTURES;
		 }
	 }

	 public ModelEntityImoutoSnowman getMainModel()
	 {
		 return (ModelEntityImoutoSnowman)super.getMainModel();
	 }
}
