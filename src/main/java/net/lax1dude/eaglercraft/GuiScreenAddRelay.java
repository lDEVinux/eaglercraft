package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.ServerData;
import net.minecraft.src.StringTranslate;

public class GuiScreenAddRelay extends GuiScreen {
	
	/** This GUI's parent GUI. */
	private GuiScreenRelay parentGui;
	private GuiTextField serverAddress;
	private GuiTextField serverName;

	public GuiScreenAddRelay(GuiScreenRelay par1GuiScreen) {
		this.parentGui = par1GuiScreen;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.serverName.updateCursorCounter();
		this.serverAddress.updateCursorCounter();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		EaglerAdapter.enableRepeatEvents(true);
		this.buttonList.clear();
		this.parentGui.addNewName = IntegratedServer.relayManager.makeNewRelayName();
		this.parentGui.addNewAddr = "";
		this.parentGui.addNewPrimary = IntegratedServer.relayManager.count() == 0;
		int sslOff = EaglerAdapter.isSSLPage() ? 36 : 0;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12 + sslOff, var1.translateKey("addRelay.add")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12 + sslOff, var1.translateKey("gui.cancel")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 142, var1.translateKey("addRelay.primary") + ": " + (this.parentGui.addNewPrimary ? var1.translateKey("gui.yes") : var1.translateKey("gui.no"))));
		this.serverName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 106, 200, 20);
		this.serverAddress = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
		this.serverAddress.setMaxStringLength(128);
		this.serverAddress.setFocused(true);
		((GuiButton) this.buttonList.get(0)).enabled = this.serverAddress.getText().length() > 0 && this.serverAddress.getText().split(":").length > 0 && this.serverName.getText().length() > 0;
		this.serverName.setText(this.parentGui.addNewName);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		EaglerAdapter.enableRepeatEvents(false);
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 1) {
				this.parentGui.confirmClicked(false, 0);
			} else if (par1GuiButton.id == 0) {
				this.parentGui.addNewName = this.serverName.getText();
				this.parentGui.addNewAddr = this.serverAddress.getText();
				this.parentGui.confirmClicked(true, 0);
			} else if (par1GuiButton.id == 2) {
				StringTranslate var2 = StringTranslate.getInstance();
				this.parentGui.addNewPrimary = !this.parentGui.addNewPrimary;
				((GuiButton) this.buttonList.get(2)).displayString = var2.translateKey("addRelay.primary") + ": " + (this.parentGui.addNewPrimary ? var2.translateKey("gui.yes") : var2.translateKey("gui.no"));
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		this.serverName.textboxKeyTyped(par1, par2);
		this.serverAddress.textboxKeyTyped(par1, par2);

		if (par1 == 9) {
			if (this.serverName.isFocused()) {
				this.serverName.setFocused(false);
				this.serverAddress.setFocused(true);
			} else {
				this.serverName.setFocused(true);
				this.serverAddress.setFocused(false);
			}
		}

		if (par1 == 13) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}

		((GuiButton) this.buttonList.get(0)).enabled = this.serverAddress.getText().length() > 0 && this.serverAddress.getText().split(":").length > 0 && this.serverName.getText().length() > 0;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.serverAddress.mouseClicked(par1, par2, par3);
		this.serverName.mouseClicked(par1, par2, par3);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("addRelay.title"), this.width / 2, 17, 16777215);
		this.drawString(this.fontRenderer, var4.translateKey("addRelay.address"), this.width / 2 - 100, 53, 10526880);
		this.drawString(this.fontRenderer, var4.translateKey("addRelay.name"), this.width / 2 - 100, 94, 10526880);
		if(EaglerAdapter.isSSLPage()) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("addServer.SSLWarn1"), this.width / 2, 169, 0xccccff);
			this.drawCenteredString(this.fontRenderer, var4.translateKey("addServer.SSLWarn2"), this.width / 2, 181, 0xccccff);
		}
		this.serverName.drawTextBox();
		this.serverAddress.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}
	
}
