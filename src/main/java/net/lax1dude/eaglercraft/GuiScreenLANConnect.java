package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.StringTranslate;

public class GuiScreenLANConnect extends GuiScreen {

	private final GuiScreen parent;
	private GuiTextField codeTextField;
	private final GuiNetworkSettingsButton relaysButton;
	
	private static String lastCode = "";
	
	public GuiScreenLANConnect(GuiScreen parent) {
		this.parent = parent;
		this.relaysButton = new GuiNetworkSettingsButton(this);
	}
	
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		EaglerAdapter.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("directConnect.lanWorldJoin")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
		this.codeTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, this.height / 4 + 27, 200, 20);
		this.codeTextField.setMaxStringLength(48);
		this.codeTextField.setFocused(true);
		this.codeTextField.setText(lastCode);
		((GuiButton) this.buttonList.get(0)).enabled = this.codeTextField.getText().trim().length() > 0;
	}

	public void onGuiClosed() {
		EaglerAdapter.enableRepeatEvents(false);
		lastCode = this.codeTextField.getText().trim();
	}
	
	protected void keyTyped(char par1, int par2) {
		if (this.codeTextField.textboxKeyTyped(par1, par2)) {
			((GuiButton) this.buttonList.get(0)).enabled = this.codeTextField.getText().trim().length() > 0;
		} else if (par2 == 28) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}
	}
	
	public void updateScreen() {
		this.codeTextField.updateCursorCounter();
	}
	
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.codeTextField.mouseClicked(par1, par2, par3);
		this.relaysButton.mouseClicked(par1, par2, par3);
	}
	
	public void drawScreen(int xx, int yy, float pt) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("selectServer.direct"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(this.fontRenderer, var4.translateKey("directConnect.lanWorldCode"), this.width / 2 - 100, this.height / 4 + 12, 10526880);
		this.drawCenteredString(this.fontRenderer, var4.translateKey("directConnect.networkSettingsNote"), this.width / 2, this.height / 4 + 67, 10526880);
		this.codeTextField.drawTextBox();
		super.drawScreen(xx, yy, pt);
		this.relaysButton.drawScreen(xx, yy);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(parent);
		}else if(par1GuiButton.id == 0) {
			mc.displayGuiScreen(new GuiScreenLANConnecting(parent, this.codeTextField.getText().trim()));
		}
	}
	
}
