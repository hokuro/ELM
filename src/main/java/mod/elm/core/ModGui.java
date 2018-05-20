package mod.elm.core;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGui implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity ent = null;
		//Entity target = Mod_FantomBlade.proxy.getGuiTarget();

//		if (id == GUI_ID_BLADEFORGE){
//			ent = world.getTileEntity(pos);
//			if((ent instanceof TileEntityBladeforge)){
//				return new ContainerBladeforge(player.inventory, (IInventory)ent);
//			}
//		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity ent = null;
		//Entity target = Mod_FantomBlade.proxy.getGuiTarget();
//		if (id == GUI_ID_BLADEFORGE){
//			ent = world.getTileEntity(pos);
//			if((ent instanceof TileEntityBladeforge)){
//				return new GuiBladeforge(player, (IInventory)ent);
//			}
//		}
		return null;
	}

}