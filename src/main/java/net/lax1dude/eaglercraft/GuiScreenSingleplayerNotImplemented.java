package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiScreenSingleplayerNotImplemented extends GuiScreen {

	private GuiScreen mainmenu;
	private String featureName;
	
	public GuiScreenSingleplayerNotImplemented(GuiScreen mainmenu, String featureName) {
		this.mainmenu = mainmenu;
		this.featureName = featureName;
	}
	
	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 3 + 50, "I Understand"));
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();

		this.drawCenteredString(fontRenderer, "the feature '" + featureName + "' is incomplete", this.width / 2, this.height / 3, 0xFFFFFF);
		this.drawCenteredString(fontRenderer, "it will be added to Eaglercraft in the next update", this.width / 2, this.height / 3 + 20, 0xFFFFFF);
		
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(mainmenu);
		}
	}
	
}
