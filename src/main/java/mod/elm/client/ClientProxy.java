package mod.elm.client;

import mod.elm.core.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
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
//		ClientRegistry.registerTileEntity(TileEntityBladeforge.class, TileEntityBladeforge.NAME,new RenderTileEntityBladeforge());
//		ClientRegistry.registerTileEntity(TileEntityAirPomp.class, TileEntityAirPomp.NAME, new RendernTileEntityAirPomp());
//		ClientRegistry.registerTileEntity(TileEntityBladeStand.class, TileEntityBladeStand.NAME, new RenderTileEntityBladeStand());
//		ClientRegistry.registerTileEntity(TileEntityBladeAlter.class, TileEntityBladeAlter.NAME, new RenderTileEntityBladeAlter());
	}

	@Override
	public void registerRender(){
//		RenderingRegistry.registerEntityRenderingHandler(EntityBladeSmith.class,  new IRenderFactory<EntityBladeSmith>() {
//			@Override
//			public Render<? super EntityBladeSmith> createRenderFor(RenderManager manager) {
//				return new RenderEntityBladeSmith(manager, 0.5f);
//			}
//		});
//
//		RenderingRegistry.registerEntityRenderingHandler(EntityBurret.class,  new IRenderFactory<EntityBurret>() {
//			@Override
//			public Render<? super EntityBurret> createRenderFor(RenderManager manager) {
//				return new RenderEntityBurret(manager);
//			}
//		});
	}

	@Override
    public EntityPlayer getEntityPlayerInstance() {
        return Minecraft.getMinecraft().player;
    }
}