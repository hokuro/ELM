package mod.elm.core;

import mod.elm.tileentity.TileEntityImoutoHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	public CommonProxy(){
	}

	public World getClientWorld(){
		return null;
	}

	public void registerTileEntity(){
		GameRegistry.registerTileEntity(TileEntityImoutoHead.class, TileEntityImoutoHead.NAME);
//		GameRegistry.registerTileEntity(TileEntityAirPomp.class, TileEntityAirPomp.NAME);
//		GameRegistry.registerTileEntity(TileEntityBladeStand.class, TileEntityBladeStand.NAME);
//		GameRegistry.registerTileEntity(TileEntityBladeAlter.class, TileEntityBladeAlter.NAME);
	}

	public void registerRender(){

	}
	public EntityPlayer getEntityPlayerInstance() {return null;}

	protected Entity target = null;
	public void setGuiTarget(Entity ent){
		target = ent;
	}

	public Entity getGuiTarget(){
		return target;
	}
}