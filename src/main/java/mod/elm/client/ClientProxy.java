package mod.elm.client;

import mod.elm.core.CommonProxy;
import mod.elm.entity.passive.EntityImoutoGolem;
import mod.elm.entity.passive.EntityImoutoSnowman;
import mod.elm.render.RenderEntityImoutoGolem;
import mod.elm.render.RenderEntityImoutoSnowman;
import mod.elm.tileentity.TileEntityImoutoHead;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{
	public ClientProxy(){
	}

	@Override
	public World getClientWorld(){
		return FMLClientHandler.instance().getClient().world;
	}

	@Override
	public void registerTileEntity(){
		GameRegistry.registerTileEntity(TileEntityImoutoHead.class, TileEntityImoutoHead.NAME);
//		ClientRegistry.registerTileEntity(TileEntityAirPomp.class, TileEntityAirPomp.NAME, new RendernTileEntityAirPomp());
	}

	@Override
	public void registerRender(){
		RenderingRegistry.registerEntityRenderingHandler(EntityImoutoGolem.class,  new IRenderFactory<EntityImoutoGolem>() {
			@Override
			public Render<? super EntityImoutoGolem> createRenderFor(RenderManager manager) {
				return new RenderEntityImoutoGolem(manager);
			}
		});

		RenderingRegistry.registerEntityRenderingHandler(EntityImoutoSnowman.class,  new IRenderFactory<EntityImoutoSnowman>() {
			@Override
			public Render<? super EntityImoutoSnowman> createRenderFor(RenderManager manager) {
				return new RenderEntityImoutoSnowman(manager);
			}
		});
	}

	@Override
    public EntityPlayer getEntityPlayerInstance() {
        return Minecraft.getMinecraft().player;
    }
}