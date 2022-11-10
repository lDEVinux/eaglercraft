package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class GuiCommandBlock extends GuiScreen {
	/** Text field containing the command block's command. */
	private GuiTextField commandTextField;

	/** Command block being edited. */
	private final TileEntityCommandBlock commandBlock;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;

	public GuiCommandBlock(TileEntityCommandBlock par1) {
		this.commandBlock = par1;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.commandTextField.updateCursorCounter();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		EaglerAdapter.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("gui.done")));
		this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
		this.commandTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 150, 60, 300, 20);
		this.commandTextField.setMaxStringLength(32767);
		this.commandTextField.setFocused(true);
		this.commandTextField.setText(this.commandBlock.getCommand());
		this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
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
				this.mc.displayGuiScreen((GuiScreen) null);
			} else if (par1GuiButton.id == 0) {
				String var2 = "MC|AdvCdm";
				ByteArrayOutputStream var3 = new ByteArrayOutputStream();
				DataOutputStream var4 = new DataOutputStream(var3);

				try {
					var4.writeInt(this.commandBlock.xCoord);
					var4.writeInt(this.commandBlock.yCoord);
					var4.writeInt(this.commandBlock.zCoord);
					Packet.writeString(this.commandTextField.getText(), var4);
					this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
				} catch (Exception var6) {
					var6.printStackTrace();
				}

				this.mc.displayGuiScreen((GuiScreen) null);
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		this.commandTextField.textboxKeyTyped(par1, par2);
		this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;

		if (par2 != 28 && par1 != 13) {
			if (par2 == 1) {
				this.actionPerformed(this.cancelBtn);
			}
		} else {
			this.actionPerformed(this.doneBtn);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.commandTextField.mouseClicked(par1, par2, par3);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("advMode.setCommand"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(this.fontRenderer, var4.translateKey("advMode.command"), this.width / 2 - 150, 47, 10526880);
		this.drawString(this.fontRenderer, var4.translateKey("advMode.nearestPlayer"), this.width / 2 - 150, 97, 10526880);
		this.drawString(this.fontRenderer, var4.translateKey("advMode.randomPlayer"), this.width / 2 - 150, 108, 10526880);
		this.drawString(this.fontRenderer, var4.translateKey("advMode.allPlayers"), this.width / 2 - 150, 119, 10526880);
		this.commandTextField.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	public boolean blockHotKeys() {
		return commandTextField.isFocused();
	}
}
