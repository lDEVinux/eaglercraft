package net.lax1dude.eaglercraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlider2;
import net.minecraft.src.StringTranslate;

public class GuiScreenChangeRelayTimeout extends GuiScreen {
	
	private GuiScreen parent;
	private GuiSlider2 slider;
	private String title;
	
	public GuiScreenChangeRelayTimeout(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		StringTranslate ts = StringTranslate.getInstance();
		title = ts.translateKey("networkSettings.relayTimeoutTitle");
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 3 + 55, ts.translateKey("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 3 + 85, ts.translateKey("gui.cancel")));
		slider = new GuiSlider2(0, width / 2 - 100, height / 3 + 10, 200, 20, (mc.gameSettings.relayTimeout - 1) / 14.0f, 1.0f) {
			public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
				if(super.mousePressed(par1Minecraft, par2, par3)) {
					this.displayString = "" + (int)((sliderValue * 14.0f) + 1.0f) + "s";
					return true;
				}else {
					return false;
				}
			}
			public void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
				super.mouseDragged(par1Minecraft, par2, par3);
				this.displayString = "" + (int)((sliderValue * 14.0f) + 1.0f) + "s";
			}
		};
		slider.displayString = "" + mc.gameSettings.relayTimeout + "s";
	}
	
	public void actionPerformed(GuiButton btn) {
		if(btn.id == 0) {
			mc.gameSettings.relayTimeout = (int)((slider.sliderValue * 14.0f) + 1.0f);
			mc.gameSettings.saveOptions();
			mc.displayGuiScreen(parent);
		}else if(btn.id == 1) {
			mc.displayGuiScreen(parent);
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, title, width / 2, height / 3 - 20, 0xFFFFFF);
		slider.drawButton(mc, par1, par2);
		super.drawScreen(par1, par2, par3);
	}
	
	public void mouseClicked(int mx, int my, int button) {
		slider.mousePressed(mc, mx, my);
		super.mouseClicked(mx, my, button);
	}

	public void mouseMovedOrUp(int par1, int par2, int par3) {
		if(par3 == 0) {
			slider.mouseReleased(par1, par2);
		}
		super.mouseMovedOrUp(par1, par2, par3);
	}
	
}
