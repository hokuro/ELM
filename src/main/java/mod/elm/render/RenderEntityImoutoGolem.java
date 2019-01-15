package mod.elm.render;

import mod.elm.core.ModCommon;
import mod.elm.entity.passive.EntityImoutoGolem;
import mod.elm.model.ModelEntityImoutoGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityImoutoGolem extends RenderLiving<EntityImoutoGolem>
{
    private static final ResourceLocation IMOUTOIRON_GOLEM_TEXTURES = new ResourceLocation(ModCommon.MOD_ID, "textures/entity/imoutoiron_golem.png");
    private static final ResourceLocation ROTTENIMOUTOIRON_GOLEM_TEXTURES = new ResourceLocation(ModCommon.MOD_ID, "textures/entity/imoutoiron_golemrotten.png");
    private static final ResourceLocation SKELTOMIMOUTOIRON_GOLEM_TEXTURES = new ResourceLocation(ModCommon.MOD_ID, "textures/entity/imoutoiron_golemskelton.png");

    public RenderEntityImoutoGolem(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelEntityImoutoGolem(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityImoutoGolem entity)
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

    protected void applyRotations(EntityImoutoGolem entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);

        if ((double)entityLiving.limbSwingAmount >= 0.01D)
        {
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
}