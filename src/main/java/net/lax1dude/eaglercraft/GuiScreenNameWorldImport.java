package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiCreateWorld;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.StringTranslate;

public class GuiScreenNameWorldImport extends GuiScreen {
	private GuiScreen parentGuiScreen;
	private GuiTextField theGuiTextField;
	private int importFormat;
	private String name;
	private String oldName;
	private boolean timeToImport = false;
	private boolean definetlyTimeToImport = false;
	private boolean isImporting = false;
	
	public GuiScreenNameWorldImport(GuiScreen menu, String name, int format) {
		this.parentGuiScreen = menu;
		this.importFormat = format;
		this.oldName = name;
		if(name.length() > 4 && (name.endsWith(".epk") || name.endsWith(".zip"))) {
			name = name.substring(0, name.length() - 4);
		}
		this.name = name;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if(!timeToImport) {
			this.theGuiTextField.updateCursorCounter();
		}
		if(definetlyTimeToImport && !isImporting) {
			isImporting = true;
			IntegratedServer.importWorld(GuiCreateWorld.makeUsableName(this.theGuiTextField.getText().trim()), EaglerAdapter.getFileChooserResult(), importFormat);
			mc.displayGuiScreen(new GuiScreenSingleplayerLoading(parentGuiScreen, "selectWorld.progress.importing." + importFormat, () -> IntegratedServer.isReady()));
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		if(!timeToImport) {
			StringTranslate var1 = StringTranslate.getInstance();
			EaglerAdapter.enableRepeatEvents(true);
			this.buttonList.clear();
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("selectWorld.progress.continue")));
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
			this.theGuiTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, this.height / 4 + 3, 200, 20);
			this.theGuiTextField.setFocused(true);
			this.theGuiTextField.setText(name);
		}
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
				EaglerAdapter.clearFileChooserResult();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			} else if (par1GuiButton.id == 0) {
				this.buttonList.clear();
				timeToImport = true;
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		this.theGuiTextField.textboxKeyTyped(par1, par2);
		((GuiButton) this.buttonList.get(0)).enabled = this.theGuiTextField.getText().trim().length() > 0;

		if (par1 == 13) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if(!timeToImport) {
			this.theGuiTextField.mouseClicked(par1, par2, par3);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		if(!timeToImport) {
			StringTranslate var4 = StringTranslate.getInstance();
			this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.importName"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, this.height / 4 - 60 + 50, 10526880);
			this.theGuiTextField.drawTextBox();
		}else {
			definetlyTimeToImport = true;
			long dots = (System.currentTimeMillis() / 500l) % 4l;
			String str = "Reading: '" + oldName + "'";
			this.drawString(fontRenderer, str + (dots > 0 ? "." : "") + (dots > 1 ? "." : "") + (dots > 2 ? "." : ""), (this.width - this.fontRenderer.getStringWidth(str)) / 2, this.height / 3 + 10, 0xFFFFFF);
		}
		super.drawScreen(par1, par2, par3);
	}
}
