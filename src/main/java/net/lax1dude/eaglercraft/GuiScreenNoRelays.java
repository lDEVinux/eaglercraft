package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class GuiScreenNoRelays extends GuiScreen {
	
	private GuiScreen parent;
	private String title1;
	private String title2;
	private String title3;
	
	public GuiScreenNoRelays(GuiScreen parent, String title) {
		this.parent = parent;
		this.title1 = title;
		this.title2 = null;
		this.title3 = null;
	}
	
	public GuiScreenNoRelays(GuiScreen parent, String title1, String title2, String title3) {
		this.parent = parent;
		this.title1 = title1;
		this.title2 = title2;
		this.title3 = title3;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		buttonList.clear();
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 - 60 + 145, var1.translateKey("gui.cancel")));
		buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 - 60 + 115, var1.translateKey("directConnect.lanWorldRelay")));
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey(title1), this.width / 2, this.height / 4 - 60 + 70, 16777215);
		if(title2 != null) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey(title2), this.width / 2, this.height / 4 - 60 + 80, 0xCCCCCC);
		}
		if(title3 != null) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey(title3), this.width / 2, this.height / 4 - 60 + 90, 0xCCCCCC);
		}
		super.drawScreen(par1, par2, par3);
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			mc.displayGuiScreen(parent);
		}else if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(new GuiScreenRelay(parent));
		}
	}
	
}
