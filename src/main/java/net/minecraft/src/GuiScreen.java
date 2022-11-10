package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class GuiScreen extends Gui {
	public static final boolean isMacOs = Minecraft.getOs() == EnumOS.MACOS;

	/** Reference to the Minecraft object. */
	protected Minecraft mc;

	/** The width of the screen object. */
	public int width;

	/** The height of the screen object. */
	public int height;

	/** A list of all the buttons in this container. */
	protected List buttonList = new ArrayList();
	public boolean allowUserInput = false;

	/** The FontRenderer used by GuiScreen */
	protected FontRenderer fontRenderer;
	public GuiParticle guiParticles;

	/** The button that was just pressed. */
	private GuiButton selectedButton = null;
	private int eventButton = 0;
	private long field_85043_c = 0L;
	private int field_92018_d = 0;

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
			GuiButton var5 = (GuiButton) this.buttonList.get(var4);
			var5.drawButton(this.mc, par1, par2);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		//if (par2 == 1) {
		//	this.mc.displayGuiScreen((GuiScreen) null);
		//	this.mc.setIngameFocus();
		//}
	}

	/**
	 * Returns a string stored in the system clipboard.
	 */
	public static String getClipboardString() {
		try {
			String s = EaglerAdapter.getClipboard();
			return s == null ? "" : s;
		}catch(Throwable t) {
			return "";
		}
	}

	/**
	 * store a string in the system clipboard
	 */
	public static void setClipboardString(String par0Str) {
		try {
			EaglerAdapter.setClipboard(par0Str);
		}catch(Throwable t) {
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
				GuiButton var5 = (GuiButton) this.buttonList.get(var4);

				if (var5.mousePressed(this.mc, par1, par2)) {
					this.selectedButton = var5;
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(var5);
				}
			}
		}
	}

	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		if (this.selectedButton != null && par3 == 0) {
			this.selectedButton.mouseReleased(par1, par2);
			this.selectedButton = null;
		}
	}

	protected void func_85041_a(int par1, int par2, int par3, long par4) {
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
	}

	/**
	 * Causes the screen to lay out its subcomponents again. This is the equivalent
	 * of the Java call Container.validate()
	 */
	public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {
		this.guiParticles = new GuiParticle(par1Minecraft);
		this.mc = par1Minecraft;
		this.fontRenderer = par1Minecraft.fontRenderer;
		this.width = par2;
		this.height = par3;
		this.buttonList.clear();
		this.initGui();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
	}

	/**
	 * Delegates mouse and keyboard input.
	 */
	public void handleInput() {
		while (EaglerAdapter.mouseNext()) {
			this.handleMouseInput();
		}

		while (EaglerAdapter.keysNext()) {
			this.handleKeyboardInput();
		}
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() {
		int var1 = EaglerAdapter.mouseGetEventX() * this.width / this.mc.displayWidth;
		int var2 = this.height - EaglerAdapter.mouseGetEventY() * this.height / this.mc.displayHeight - 1;

		if (EaglerAdapter.mouseGetEventButtonState()) {
			if (this.mc.gameSettings.touchscreen && this.field_92018_d++ > 0) {
				return;
			}

			this.eventButton = EaglerAdapter.mouseGetEventButton();
			this.field_85043_c = Minecraft.getSystemTime();
			this.mouseClicked(var1, var2, this.eventButton);
		} else if (EaglerAdapter.mouseGetEventButton() != -1) {
			if (this.mc.gameSettings.touchscreen && --this.field_92018_d > 0) {
				return;
			}

			this.eventButton = -1;
			this.mouseMovedOrUp(var1, var2, EaglerAdapter.mouseGetEventButton());
		} else if (this.eventButton != -1 && this.field_85043_c > 0L) {
			long var3 = Minecraft.getSystemTime() - this.field_85043_c;
			this.func_85041_a(var1, var2, this.eventButton, var3);
		}
	}

	/**
	 * Handles keyboard input.
	 */
	public void handleKeyboardInput() {
		if (EaglerAdapter.getEventKeyState()) {
			int var1 = EaglerAdapter.getEventKey();
			char var2 = EaglerAdapter.getEventChar();

			if (var1 == 87) {
				this.mc.toggleFullscreen();
				return;
			}

			//if (isMacOs && var1 == 28 && var2 == 0) {
			//	var1 = 29;
			//}

			this.keyTyped(var2, var1);
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
	}

	/**
	 * Draws either a gradient over the background screen (when it exists) or a flat
	 * gradient over background.png
	 */
	public void drawDefaultBackground() {
		this.drawWorldBackground(0);
	}

	public void drawWorldBackground(int par1) {
		if (this.mc.theWorld != null) {
			this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		} else {
			this.drawBackground(par1);
		}
	}
	
	private static final TextureLocation tex_background = new TextureLocation("/gui/background.png");

	/**
	 * Draws the background (i is always 0 as of 1.2.2)
	 */
	public void drawBackground(int par1) {
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);
		Tessellator var2 = Tessellator.instance;
		tex_background.bindTexture();
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var3 = 32.0F;
		var2.startDrawingQuads();
		var2.setColorOpaque_I(4210752);
		var2.addVertexWithUV(0.0D, (double) this.height, 0.0D, 0.0D, (double) ((float) this.height / var3 + (float) par1));
		var2.addVertexWithUV((double) this.width, (double) this.height, 0.0D, (double) ((float) this.width / var3), (double) ((float) this.height / var3 + (float) par1));
		var2.addVertexWithUV((double) this.width, 0.0D, 0.0D, (double) ((float) this.width / var3), (double) par1);
		var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double) par1);
		var2.draw();
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return true;
	}

	public void confirmClicked(boolean par1, int par2) {
	}

	public static boolean isCtrlKeyDown() {
		return EaglerAdapter.isKeyDown(29) || EaglerAdapter.isKeyDown(157) || (isMacOs && (EaglerAdapter.isKeyDown(28) || EaglerAdapter.isKeyDown(219) || EaglerAdapter.isKeyDown(220)));
	}

	public static boolean isShiftKeyDown() {
		return EaglerAdapter.isKeyDown(42) || EaglerAdapter.isKeyDown(54);
	}
	
	public boolean blockHotKeys() {
		return false;
	}
}
