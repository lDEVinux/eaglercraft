package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiScreen;

public class GuiScreenLicenseDeclined extends GuiScreen {

	public void drawScreen(int mx, int my, float par3) {
		this.drawDefaultBackground();
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line70), width / 2, height / 3 - 10, 0xFFFFFF);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line71), width / 2, height / 3 + 18, 0xFF7777);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line72), width / 2, height / 3 + 35, 0x666666);
	}
}
