package mod.elm.inventory;

import java.util.List;

import mod.elm.core.Mod_Elm;
import mod.elm.item.parts.ab.IItemElmParts;
import mod.elm.tileentity.ITileEntityParameter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ContainerMaergerPot extends Container {

	protected final IInventory inventory;
	protected final PlayerInventory playerInv;

	public ContainerMaergerPot(int id, PlayerInventory playerInvIn, TileEntity te){
		super(Mod_Elm.CONTAINER_MAERGERPOT, id);
		this.inventory = (IInventory)te;
		this.playerInv = playerInvIn;

		for (int row = 0; row < 3; row ++) {
			for (int col = 0; col < 3; col++) {
				addSlot(new Slot(inventory, col + row*3, 63 + col * 18, 17 + row * 18){
					public boolean isItemValid(ItemStack stack) {
						return (stack.getItem() instanceof IItemElmParts);
					}
				});
			}
		}

	    for (int k = 0; k < 3; ++k) {
	        for (int i1 = 0; i1 < 9; ++i1) {
	            this.addSlot(new Slot(playerInv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	        }
	    }

	    for (int l = 0; l < 9; ++l) {
	        this.addSlot(new Slot(playerInv, l, 8 + l * 18, 142));
	    }
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.inventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index > inventory.getSizeInventory()) {
				// iプレイヤーインベントリからコンテナへ
				if (!this.mergeItemStack(itemstack1, 0, inventory.getSizeInventory(), false)) {
					return ItemStack.EMPTY;
				}
			}else {
				// iコンテナからプレイヤーインベントリへ
				if (!this.mergeItemStack(itemstack1, inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		ITileEntityParameter te = (ITileEntityParameter)inventory;
		for (int i = 0; i < te.getFieldCount(); i++) {
			listener.sendWindowProperty(this, i, te.getField(i));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		ITileEntityParameter cr = (ITileEntityParameter)this.inventory;
		List<IContainerListener> listeners = ObfuscationReflectionHelper.getPrivateValue(Container.class, this, "listeners");
		for (int i = 0; i < listeners.size(); ++i) {
			for (int j = 0; j < cr.getFieldCount(); j++) {
				listeners.get(i).sendWindowProperty(this, j, cr.getField(j));
			}
		}
	}

	public TileEntity getTileEntity() {
		return (TileEntity)inventory;
	}

	@Override
	public void updateProgressBar(int id, int data) {
		ITileEntityParameter te = (ITileEntityParameter)inventory;
		te.setField(id, data);
	}
}
