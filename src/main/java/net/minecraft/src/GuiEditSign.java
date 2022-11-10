package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class GuiEditSign extends GuiScreen {
	/**
	 * This String is just a local copy of the characters allowed in text rendering
	 * of minecraft.
	 */
	private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;

	/** The title string that is displayed in the top-center of the screen. */
	protected String screenTitle = "Edit sign message:";

	/** Reference to the sign object. */
	private TileEntitySign entitySign;

	/** Counts the number of screen updates. */
	private int updateCounter;

	/** The number of the line that is being edited. */
	private int editLine = 0;

	/** "Done" button for the GUI. */
	private GuiButton doneBtn;

	public GuiEditSign(TileEntitySign par1TileEntitySign) {
		this.entitySign = par1TileEntitySign;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.buttonList.clear();
		EaglerAdapter.enableRepeatEvents(true);
		this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
		this.entitySign.setEditable(false);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		EaglerAdapter.enableRepeatEvents(false);
		NetClientHandler var1 = this.mc.getNetHandler();

		if (var1 != null) {
			var1.addToSendQueue(new Packet130UpdateSign(this.entitySign.xCoord, this.entitySign.yCoord, this.entitySign.zCoord, this.entitySign.signText));
		}

		this.entitySign.setEditable(true);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		++this.updateCounter;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 0) {
				this.entitySign.onInventoryChanged();
				this.mc.displayGuiScreen((GuiScreen) null);
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == 200) {
			this.editLine = this.editLine - 1 & 3;
		}

		if (par2 == 208 || par2 == 28) {
			this.editLine = this.editLine + 1 & 3;
		}

		if (par2 == 14 && this.entitySign.signText[this.editLine].length() > 0) {
			this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine].substring(0, this.entitySign.signText[this.editLine].length() - 1);
		}

		if (allowedCharacters.indexOf(par1) >= 0 && this.entitySign.signText[this.editLine].length() < 15) {
			this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine] + par1;
		}

		if (par2 == 1) {
			this.actionPerformed(this.doneBtn);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 16777215);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) (this.width / 2), 0.0F, 50.0F);
		float var4 = 93.75F;
		EaglerAdapter.glScalef(-var4, -var4, -var4);
		EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		Block var5 = this.entitySign.getBlockType();

		if (var5 == Block.signPost) {
			float var6 = (float) (this.entitySign.getBlockMetadata() * 360) / 16.0F;
			EaglerAdapter.glRotatef(var6, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glTranslatef(0.0F, -1.0625F, 0.0F);
		} else {
			int var8 = this.entitySign.getBlockMetadata();
			float var7 = 0.0F;

			if (var8 == 2) {
				var7 = 180.0F;
			}

			if (var8 == 4) {
				var7 = 90.0F;
			}

			if (var8 == 5) {
				var7 = -90.0F;
			}

			EaglerAdapter.glRotatef(var7, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glTranslatef(0.0F, -1.0625F, 0.0F);
		}

		if (this.updateCounter / 6 % 2 == 0) {
			this.entitySign.lineBeingEdited = this.editLine;
		}

		TileEntityRenderer.instance.renderTileEntityAt(this.entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
		this.entitySign.lineBeingEdited = -1;
		EaglerAdapter.glPopMatrix();
		super.drawScreen(par1, par2, par3);
	}
	
	public boolean blockHotKeys() {
		return true;
	}
}
