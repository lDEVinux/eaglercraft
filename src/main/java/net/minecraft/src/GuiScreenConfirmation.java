package net.minecraft.src;

public class GuiScreenConfirmation extends GuiYesNo {
	private String field_96288_n;

	public GuiScreenConfirmation(GuiScreen par1GuiScreen, String par2Str, String par3Str, String par4Str, int par5) {
		super(par1GuiScreen, par2Str, par3Str, par5);
		this.field_96288_n = par4Str;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.buttonList.add(new GuiSmallButton(0, this.width / 2 - 155, this.height / 6 + 112, this.buttonText1));
		this.buttonList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 112, this.buttonText2));
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		this.drawCenteredString(this.fontRenderer, this.field_96288_n, this.width / 2, 110, 16777215);
	}
}
