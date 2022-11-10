package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiScreenDirectConnect;
import net.minecraft.src.StringTranslate;

public class GuiScreenConnectOption extends GuiScreen {

	private final GuiMultiplayer guiScreen;
	private String title;
	private String prompt;
	
	private final GuiNetworkSettingsButton relaysButton;
	
	public GuiScreenConnectOption(GuiMultiplayer guiScreen) {
		this.guiScreen = guiScreen;
		this.relaysButton = new GuiNetworkSettingsButton(this);
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		title = var1.translateKey("selectServer.direct");
		prompt = var1.translateKey("directConnect.prompt");
		buttonList.clear();
		buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 - 60 + 90, var1.translateKey("directConnect.serverJoin")));
		buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 - 60 + 115, var1.translateKey("directConnect.lanWorld")));
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 - 60 + 155, var1.translateKey("gui.cancel")));
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			mc.displayGuiScreen(guiScreen);
		}else if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(new GuiScreenDirectConnect(guiScreen, guiScreen.getTheServerData()));
		}else if(par1GuiButton.id == 2) {
			GuiScreen scn = new GuiScreenLANConnect(guiScreen);
			if(IntegratedServer.relayManager.count() == 0) {
				mc.displayGuiScreen(new GuiScreenNoRelays(guiScreen, "noRelay.title"));
			}else {
				mc.displayGuiScreen(scn);
			}
		}
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, title, this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawCenteredString(this.fontRenderer, prompt, this.width / 2, this.height / 4 - 60 + 55, 0x999999);
		super.drawScreen(par1, par2, par3);
		relaysButton.drawScreen(par1, par2);
	}
	
	protected void mouseClicked(int par1, int par2, int par3) {
		relaysButton.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}

}
