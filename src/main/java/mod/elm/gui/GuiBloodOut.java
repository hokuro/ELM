package mod.elm.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.Vec3i;

public class GuiBloodOut extends AbstractGui {
		private final Minecraft mc;
		private List<Vec3i> tlList = Lists.newArrayList();
		public GuiBloodOut(Minecraft mcIn) {
			this.mc = mcIn;
		}

		public void render(int count, float alpha, boolean random) {
			GlStateManager.pushMatrix();
			int rd = 220;
			int gd = 20;
			int bd = 60;
			int alp= (int)(255*alpha);
			int color = (alp << 24) + (rd <<16) + (gd << 8) + bd;

			if (random || tlList.size() == 0 || tlList.size() < count) {
				tlList.clear();
				for (int i = 0; i < count; i ++) {
					tlList.add(new Vec3i(ModUtil.random(this.mc.mainWindow.getHeight()-20), ModUtil.random(this.mc.mainWindow.getFramebufferWidth()-20), 0));
				}
			}

			for (int i = 0; i < count; i ++) {
				int top;
				int left;
				if (tlList.size() < i) {
					top = tlList.get(i).getX();
					left = tlList.get(i).getY();
				} else {
					int rnd = ModUtil.random(tlList.size());
					top = tlList.get(rnd).getX();
					left = tlList.get(rnd).getY();
				}
				fill(left, top, 20 + left, 20 + top, color);
			}
			GlStateManager.enableBlend();
			GlStateManager.disableAlphaTest();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
}