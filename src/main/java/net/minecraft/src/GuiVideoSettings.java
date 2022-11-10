package net.minecraft.src;

public class GuiVideoSettings extends GuiScreen {
	private GuiScreen parentGuiScreen;

	/** The title string that is displayed in the top-center of the screen. */
	protected String screenTitle = "Video Settings";

	/** GUI game settings */
	private GameSettings guiGameSettings;

	/**
	 * True if the system is 64-bit (using a simple indexOf test on a system
	 * property)
	 */
	private boolean is64bit = false;

	/** An array of all of EnumOption's video options. */
	private static EnumOptions[] videoOptions = new EnumOptions[] { EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AMBIENT_OCCLUSION, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING,
			EnumOptions.GUI_SCALE, EnumOptions.GAMMA, EnumOptions.RENDER_CLOUDS, EnumOptions.ENABLE_FOG, EnumOptions.PARTICLES, EnumOptions.CHUNK_UPDATES, EnumOptions.ADDERALL };

	public GuiVideoSettings(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
		this.parentGuiScreen = par1GuiScreen;
		this.guiGameSettings = par2GameSettings;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.screenTitle = var1.translateKey("options.videoTitle");
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 178, var1.translateKey("gui.done")));
		this.is64bit = true;
		/*
		String[] var2 = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
		String[] var3 = var2;
		int var4 = var2.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			String var6 = var3[var5];
			String var7 = System.getProperty(var6);

			if (var7 != null && var7.contains("64")) {
				this.is64bit = true;
				break;
			}
		}
		*/

		int var9 = 0;
		EnumOptions[] var10 = videoOptions;
		int var11 = var10.length;

		for (int var12 = 0; var12 < var11; ++var12) {
			EnumOptions var8 = var10[var12];
			if(var8 == EnumOptions.ADDERALL && !mc.yeeState) continue;

			if (var8.getEnumFloat()) {
				this.buttonList.add(new GuiSlider(var8.returnEnumOrdinal(), this.width / 2 - 155 + var9 % 2 * 160, this.height / 7 + 24 * (var9 >> 1), var8, this.guiGameSettings.getKeyBinding(var8),
						this.guiGameSettings.getOptionFloatValue(var8)));
			} else {
				this.buttonList.add(new GuiSmallButton(var8.returnEnumOrdinal(), this.width / 2 - 155 + var9 % 2 * 160, this.height / 7 + 24 * (var9 >> 1), var8, this.guiGameSettings.getKeyBinding(var8)));
			}

			++var9;
		}
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			int var2 = this.guiGameSettings.guiScale;

			if (par1GuiButton.id < 100 && par1GuiButton instanceof GuiSmallButton) {
				this.guiGameSettings.setOptionValue(((GuiSmallButton) par1GuiButton).returnEnumOptions(), 1);
				par1GuiButton.displayString = this.guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
			}

			if (par1GuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			}

			if (this.guiGameSettings.guiScale != var2) {
				ScaledResolution var3 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int var4 = var3.getScaledWidth();
				int var5 = var3.getScaledHeight();
				this.setWorldAndResolution(this.mc, var4, var5);
				this.mc.voiceOverlay.setResolution(var4, var5);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, this.is64bit ? 20 : 5, 16777215);

		if (!this.is64bit && this.guiGameSettings.renderDistance == 0) {
			this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("options.farWarning1"), this.width / 2, this.height / 6 + 144 + 1, 11468800);
			this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("options.farWarning2"), this.width / 2, this.height / 6 + 144 + 13, 11468800);
		}

		super.drawScreen(par1, par2, par3);
	}
}
