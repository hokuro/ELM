package mod.elm.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.inventory.ContainerMaergerPot;
import mod.elm.tileentity.ITileEntityParameter;
import mod.elm.tileentity.TileEntityMaergerPot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiMaergerPot extends ContainerScreen<ContainerMaergerPot> {
	private static final ResourceLocation tex_0 = new ResourceLocation("elm:textures/gui/maegerpot_0.png");
	private static final ResourceLocation tex_1 = new ResourceLocation("elm:textures/gui/maegerpot_1.png");
	private static final ResourceLocation tex_2 = new ResourceLocation("elm:textures/gui/maegerpot_2.png");
	private static final ResourceLocation tex_3 = new ResourceLocation("elm:textures/gui/maegerpot_3.png");
	private static final int[][] POS1 = new int[][] {{10,25,0,185},{31,25,20,185},{10,44,40,185},{31,44,60,185}};
	private static final int[][] POS2 = new int[][] {{62,16,176,0},{62,16,176,54},{62,16,176,108},{62,16,176,162}};

	private int selectParts;
	protected final ITileEntityParameter te;
	public GuiMaergerPot(ContainerMaergerPot container, PlayerInventory inv, ITextComponent titleIn) {
		super(container, inv, titleIn);
		te = (ITileEntityParameter)container.getTileEntity();
		selectParts = 0;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		for (int i = 0; i < POS1.length; i++) {
			double xx = mouseX - (x + POS1[i][0]);
				double yy = mouseY - (y + POS1[i][1]);

				if (xx >= 0 && yy >= 0 && xx < 20 && yy < 18) {
					if ((i + 1) !=  this.selectParts) {
						this.selectParts = i+1;
					} else {
						this.selectParts = 0;
					}
				}
		}
		return true;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		// 背景
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		int blood = te.getField(TileEntityMaergerPot.FIELD_BLOODCOUNT);
		if (blood > 7) {
			Minecraft.getInstance().getTextureManager().bindTexture(tex_3);
		} else if (blood > 4) {
			Minecraft.getInstance().getTextureManager().bindTexture(tex_2);
		} else if (blood > 1) {
			Minecraft.getInstance().getTextureManager().bindTexture(tex_1);
		} else {
			Minecraft.getInstance().getTextureManager().bindTexture(tex_0);
		}
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
		int idx = selectParts - 1;
		if (idx >= 0) {
			this.blit(i + POS1[idx][0], j + POS1[idx][1], POS1[idx][2], POS1[idx][3], 20, 18);
			this.blit(i + POS2[idx][0], j + POS2[idx][1], POS2[idx][2], POS2[idx][3], 54, 54);
		}
	}
}
