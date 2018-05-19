package mod.fbd.client;

import mod.fbd.core.CommonProxy;
import mod.fbd.entity.EntityBurret;
import mod.fbd.entity.mob.EntityBladeSmith;
import mod.fbd.render.RenderEntityBladeSmith;
import mod.fbd.render.RenderEntityBurret;
import mod.fbd.render.RenderTileEntityBladeAlter;
import mod.fbd.render.RenderTileEntityBladeStand;
import mod.fbd.render.RenderTileEntityBladeforge;
import mod.fbd.render.RendernTileEntityAirPomp;
import mod.fbd.tileentity.TileEntityAirPomp;
import mod.fbd.tileentity.TileEntityBladeAlter;
import mod.fbd.tileentity.TileEntityBladeStand;
import mod.fbd.tileentity.TileEntityBladeforge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
		ClientRegistry.registerTileEntity(TileEntityBladeforge.class, TileEntityBladeforge.NAME,new RenderTileEntityBladeforge());
		ClientRegistry.registerTileEntity(TileEntityAirPomp.class, TileEntityAirPomp.NAME, new RendernTileEntityAirPomp());
		ClientRegistry.registerTileEntity(TileEntityBladeStand.class, TileEntityBladeStand.NAME, new RenderTileEntityBladeStand());
		ClientRegistry.registerTileEntity(TileEntityBladeAlter.class, TileEntityBladeAlter.NAME, new RenderTileEntityBladeAlter());
	}

	@Override
	public void registerRender(){
		RenderingRegistry.registerEntityRenderingHandler(EntityBladeSmith.class,  new IRenderFactory<EntityBladeSmith>() {
			@Override
			public Render<? super EntityBladeSmith> createRenderFor(RenderManager manager) {
				return new RenderEntityBladeSmith(manager, 0.5f);
			}
		});

		RenderingRegistry.registerEntityRenderingHandler(EntityBurret.class,  new IRenderFactory<EntityBurret>() {
			@Override
			public Render<? super EntityBurret> createRenderFor(RenderManager manager) {
				return new RenderEntityBurret(manager);
			}
		});
	}

	@Override
    public EntityPlayer getEntityPlayerInstance() {
        return Minecraft.getMinecraft().player;
    }
}