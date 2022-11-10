package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiScreenLicense extends GuiScreen {

	private final GuiScreen continueScreen;
	private boolean hasCheckedBox = false;
	private int beginOffset = 0;
	private GuiButton acceptButton;
	
	public GuiScreenLicense(GuiScreen scr) {
		continueScreen = scr;
	}
	
	public void initGui() {
		beginOffset = this.height / 2 - 100;
		if(beginOffset < 5) {
			beginOffset = 5;
		}
		this.buttonList.add(new GuiButton(1, this.width / 2 - 120, beginOffset + 180, 115, 20, new String(GuiScreenChangeWorldTimeout.line61)));
		this.buttonList.add(acceptButton = new GuiButton(2, this.width / 2 + 5, beginOffset + 180, 115, 20, new String(GuiScreenChangeWorldTimeout.line60)));
		acceptButton.enabled = false;
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 2) {
			LocalStorageManager.profileSettingsStorage.setBoolean("acceptLicense", true);
			LocalStorageManager.saveStorageP();
			mc.displayGuiScreen(continueScreen);
		}else if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(new GuiScreenLicenseDeclined());
		}
	}
	
	private static final TextureLocation beaconx = new TextureLocation("/gui/beacon.png");

	public void drawScreen(int mx, int my, float par3) {
		drawDefaultBackground();
		acceptButton.enabled = hasCheckedBox;
		super.drawScreen(mx,  my,  par3);
		
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glScalef(1.33f, 1.33f, 1.33f);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line00), width * 3 / 8, beginOffset * 3 / 4, 0xDDDD55);
		EaglerAdapter.glPopMatrix();
		
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line10), width / 2, beginOffset + 22, 0xFF7777);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line11), width / 2, beginOffset + 33, 0xFF7777);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line12), width / 2, beginOffset + 44, 0xFF7777);

		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line20), width / 2, beginOffset + 62, 0x448844);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line21), width / 2, beginOffset + 71, 0x448844);

		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line30), width * 4 / 6, (beginOffset + 89) * 4 / 3, 0x666666);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line31), width * 4 / 6, (beginOffset + 97) * 4 / 3, 0x999999);
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line32), width * 4 / 6, (beginOffset + 105) * 4 / 3, 0x999999);
		EaglerAdapter.glPopMatrix();
		
		drawCenteredString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line40), width / 2, beginOffset + 120, 0xFF7777);
		
		boolean mouseOverCheck = width / 2 - 100 < mx && width / 2 - 83 > mx && beginOffset + 142 < my && beginOffset + 159 > my;
		
		if(mouseOverCheck) {
			EaglerAdapter.glColor4f(0.7f, 0.7f, 1.0f, 1.0f);
		}else {
			EaglerAdapter.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
		}
		
		beaconx.bindTexture();
		
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
		drawTexturedModalRect((width / 2 - 100) * 4 / 3, (beginOffset + 142) * 4 / 3, 22, 219, 22, 22);
		EaglerAdapter.glPopMatrix();
		
		if(hasCheckedBox) {
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glColor4f(1.1f, 1.1f, 1.1f, 1.0f);
			EaglerAdapter.glTranslatef(0.5f, 0.5f, 0.0f);
			drawTexturedModalRect((width / 2 - 100), (beginOffset + 142), 90, 222, 16, 16);
			EaglerAdapter.glPopMatrix();
		}
		
		EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		drawString(fontRenderer, new String(GuiScreenChangeWorldTimeout.line50), width / 2 - 75, beginOffset + 147, 0xEEEEEE);
	}
	
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if(width / 2 - 100 < par1 && width / 2 - 83 > par1 && beginOffset + 142 < par2 && beginOffset + 159 > par2) {
			this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			hasCheckedBox = !hasCheckedBox;
		}
	}
	
}
