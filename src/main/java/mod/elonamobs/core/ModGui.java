package mod.fbd.core;


import mod.fbd.core.log.ModLog;
import mod.fbd.entity.mob.EntityBladeSmith;
import mod.fbd.gui.GuiAutomatic;
import mod.fbd.gui.GuiBladeAlter;
import mod.fbd.gui.GuiBladeSmith;
import mod.fbd.gui.GuiBladeforge;
import mod.fbd.gui.GuiCartridge;
import mod.fbd.gui.GuiGunCustomize;
import mod.fbd.gui.GuiRevolver;
import mod.fbd.inventory.ContainerAutomatic;
import mod.fbd.inventory.ContainerBladeAlter;
import mod.fbd.inventory.ContainerBladeSmith;
import mod.fbd.inventory.ContainerBladeforge;
import mod.fbd.inventory.ContainerCartridge;
import mod.fbd.inventory.ContainerGunCustomizer;
import mod.fbd.inventory.ContainerRevolver;
import mod.fbd.item.ItemAutomatic;
import mod.fbd.item.ItemCartridge;
import mod.fbd.item.ItemRevolver;
import mod.fbd.tileentity.TileEntityBladeAlter;
import mod.fbd.tileentity.TileEntityBladeforge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGui implements IGuiHandler {
	public static final int GUI_ID_BLADEFORGE = 0;
	public static final int GUI_ID_BLADESMITH = 100;
	public static final int GUI_ID_BLADEALTER = 1;
	public static final int GUI_ID_REVOLVER_OFFHAND = 2;
	public static final int GUI_ID_REVOLVER_MAINHAND = 3;
	public static final int GUI_ID_AUTOMATIC_OFFHAND = 4;
	public static final int GUI_ID_AUTOMATIC_MAINHAND = 5;
	public static final int GUI_ID_CARTRIDGE = 6;
	public static final int GUI_ID_GUNCUSTOMAIZER = 7;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity ent = null;
		//Entity target = Mod_FantomBlade.proxy.getGuiTarget();

		if (id == GUI_ID_BLADEFORGE){
			ent = world.getTileEntity(pos);
			if((ent instanceof TileEntityBladeforge)){
				return new ContainerBladeforge(player.inventory, (IInventory)ent);
			}
		}

		if (id == GUI_ID_BLADEALTER){
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityBladeAlter){
				return new ContainerBladeAlter(player, (IInventory)ent);
			}
		}

		if (id == GUI_ID_REVOLVER_OFFHAND || id == GUI_ID_REVOLVER_MAINHAND){
			ItemStack gunStack;
			if (id == GUI_ID_REVOLVER_OFFHAND){
				gunStack = player.getHeldItemOffhand();
			}else{
				gunStack = player.getHeldItemMainhand();
			}
			if(!gunStack.isEmpty() && gunStack.getItem() instanceof ItemRevolver){
				return new ContainerRevolver(player, ((ItemRevolver)gunStack.getItem()).getInventory(gunStack));
			}
		}

		if (id == GUI_ID_AUTOMATIC_OFFHAND || id == GUI_ID_AUTOMATIC_MAINHAND){
			ItemStack gunStack;
			if (id == GUI_ID_AUTOMATIC_OFFHAND){
				gunStack = player.getHeldItemOffhand();
			}else{
				gunStack = player.getHeldItemMainhand();
			}
			if(!gunStack.isEmpty() && gunStack.getItem() instanceof ItemAutomatic){
				return new ContainerAutomatic(player, ((ItemAutomatic)gunStack.getItem()).getInventory(gunStack));
			}
		}

		if (id == GUI_ID_CARTRIDGE){
			ItemStack gunStack = player.getHeldItemMainhand();
			if(!gunStack.isEmpty() && gunStack.getItem() instanceof ItemCartridge){
				return new ContainerCartridge(player, ((ItemCartridge)gunStack.getItem()).getInventory(gunStack));
			}
		}

		if (id == GUI_ID_GUNCUSTOMAIZER){
			return new ContainerGunCustomizer(player, world);
		}

		if (id >= GUI_ID_BLADESMITH){
			int entid = id - GUI_ID_BLADESMITH;
			ModLog.log().debug("server gui entity id:"+entid);
			Entity entity = world.getEntityByID(entid);
			if (entity instanceof EntityBladeSmith){
				Object obj = new ContainerBladeSmith(player.inventory, ((EntityBladeSmith)entity).getSmithInventory(), world);
				//Mod_FantomBlade.proxy.setGuiTarget(null);
				return obj;
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity ent = null;
		//Entity target = Mod_FantomBlade.proxy.getGuiTarget();
		if (id == GUI_ID_BLADEFORGE){
			ent = world.getTileEntity(pos);
			if((ent instanceof TileEntityBladeforge)){
				return new GuiBladeforge(player, (IInventory)ent);
			}
		}

		if (id == GUI_ID_BLADEALTER){
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityBladeAlter){
				return new GuiBladeAlter(player, (IInventory)ent);
			}
		}

		if (id == GUI_ID_REVOLVER_OFFHAND || id == GUI_ID_REVOLVER_MAINHAND){
			ItemStack gunStack;
			if (id == GUI_ID_REVOLVER_OFFHAND){
				gunStack = player.getHeldItemOffhand();
			}else{
				gunStack = player.getHeldItemMainhand();
			}
			if(!gunStack.isEmpty() && gunStack.getItem() instanceof ItemRevolver){
				return new GuiRevolver(player,((ItemRevolver)gunStack.getItem()).getInventory(gunStack));
			}
		}

		if (id == GUI_ID_AUTOMATIC_OFFHAND || id == GUI_ID_AUTOMATIC_MAINHAND){
			ItemStack gunStack;
			if (id == GUI_ID_AUTOMATIC_OFFHAND){
				gunStack = player.getHeldItemOffhand();
			}else{
				gunStack = player.getHeldItemMainhand();
			}
			if(!gunStack.isEmpty() && gunStack.getItem() instanceof ItemAutomatic){
				return new GuiAutomatic(player, ((ItemAutomatic)gunStack.getItem()).getInventory(gunStack));
			}
		}

		if (id == GUI_ID_CARTRIDGE){
			ItemStack gunStack = player.getHeldItemMainhand();
			if(!gunStack.isEmpty() && gunStack.getItem() instanceof ItemCartridge){
				return new GuiCartridge(player, ((ItemCartridge)gunStack.getItem()).getInventory(gunStack));
			}
		}

		if (id == GUI_ID_GUNCUSTOMAIZER){
			return new GuiGunCustomize(player, world);
		}

		if (id >= GUI_ID_BLADESMITH) {
			int entid = id - GUI_ID_BLADESMITH;
			ModLog.log().debug("client gui entity id:"+entid);
			Entity entity = world.getEntityByID(entid);
			if (entity instanceof EntityBladeSmith){
				Object obj = new GuiBladeSmith(player, (EntityBladeSmith)entity);
				//Mod_FantomBlade.proxy.setGuiTarget(null);
				return obj;
			}
		}
		return null;
	}

}