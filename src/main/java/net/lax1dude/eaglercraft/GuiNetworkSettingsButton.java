package net.lax1dude.eaglercraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumChatFormatting;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class GuiNetworkSettingsButton extends Gui {

	private final GuiScreen screen;
	private final String text;
	private final Minecraft mc;
	
	public GuiNetworkSettingsButton(GuiScreen screen) {
		this.screen = screen;
		this.text = StringTranslate.getInstance().translateKey("directConnect.lanWorldRelay");
		this.mc = Minecraft.getMinecraft();
	}
	
	public void drawScreen(int xx, int yy) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
		
		int w = mc.fontRenderer.getStringWidth(text);
		boolean hover = xx > 1 && yy > 1 && xx < (w * 3 / 4) + 7 && yy < 12;
		
		drawString(mc.fontRenderer, EnumChatFormatting.UNDERLINE + text, 5, 5, hover ? 0xFFEEEE22 : 0xFFCCCCCC);
		
		EaglerAdapter.glPopMatrix();
	}
	
	public void mouseClicked(int xx, int yy, int btn) {
		int w = mc.fontRenderer.getStringWidth(text);
		if(xx > 2 && yy > 2 && xx < (w * 3 / 4) + 5 && yy < 12) {
			mc.displayGuiScreen(new GuiScreenRelay(screen));
			mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
		}
	}
	
}
