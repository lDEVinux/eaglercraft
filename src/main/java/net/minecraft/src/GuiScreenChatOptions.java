package net.minecraft.src;

public class GuiScreenChatOptions extends GuiScreen {
	/** An array of all EnumOptions which are to do with chat. */
	private static final EnumOptions[] allScreenChatOptions = new EnumOptions[] { EnumOptions.CHAT_VISIBILITY,
			EnumOptions.CHAT_COLOR, EnumOptions.CHAT_LINKS, EnumOptions.CHAT_OPACITY, EnumOptions.CHAT_LINKS_PROMPT,
			EnumOptions.CHAT_SCALE, EnumOptions.CHAT_HEIGHT_FOCUSED, EnumOptions.CHAT_HEIGHT_UNFOCUSED,
			EnumOptions.CHAT_WIDTH };
	private static final EnumOptions[] allMultiplayerOptions = new EnumOptions[] { EnumOptions.SHOW_CAPE };

	/** Instance of GuiScreen. */
	private final GuiScreen theGuiScreen;

	/** Instance of GameSettings file. */
	private final GameSettings theSettings;
	private String theChatOptions;
	private String field_82268_n;
	private int field_82269_o = 0;

	public GuiScreenChatOptions(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
		this.theGuiScreen = par1GuiScreen;
		this.theSettings = par2GameSettings;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		int var2 = 0;
		this.theChatOptions = var1.translateKey("options.chat.title");
		this.field_82268_n = var1.translateKey("options.multiplayer.title");
		EnumOptions[] var3 = allScreenChatOptions;
		int var4 = var3.length;
		int var5;
		EnumOptions var6;

		for (var5 = 0; var5 < var4; ++var5) {
			var6 = var3[var5];

			if (var6.getEnumFloat()) {
				this.buttonList.add(new GuiSlider(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160,
						this.height / 6 + 24 * (var2 >> 1), var6, this.theSettings.getKeyBinding(var6),
						this.theSettings.getOptionFloatValue(var6)));
			} else {
				this.buttonList.add(new GuiSmallButton(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160,
						this.height / 6 + 24 * (var2 >> 1), var6, this.theSettings.getKeyBinding(var6)));
			}

			++var2;
		}

		if (var2 % 2 == 1) {
			++var2;
		}

		this.field_82269_o = this.height / 6 + 24 * (var2 >> 1);
		var2 += 2;
		var3 = allMultiplayerOptions;
		var4 = var3.length;

		for (var5 = 0; var5 < var4; ++var5) {
			var6 = var3[var5];

			if (var6.getEnumFloat()) {
				this.buttonList.add(new GuiSlider(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160,
						this.height / 6 + 24 * (var2 >> 1), var6, this.theSettings.getKeyBinding(var6),
						this.theSettings.getOptionFloatValue(var6)));
			} else {
				this.buttonList.add(new GuiSmallButton(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160,
						this.height / 6 + 24 * (var2 >> 1), var6, this.theSettings.getKeyBinding(var6)));
			}

			++var2;
		}

		this.buttonList
				.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id < 100 && par1GuiButton instanceof GuiSmallButton) {
				this.theSettings.setOptionValue(((GuiSmallButton) par1GuiButton).returnEnumOptions(), 1);
				par1GuiButton.displayString = this.theSettings
						.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
			}

			if (par1GuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.theGuiScreen);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.theChatOptions, this.width / 2, 20, 16777215);
		this.drawCenteredString(this.fontRenderer, this.field_82268_n, this.width / 2, this.field_82269_o + 7,
				16777215);
		super.drawScreen(par1, par2, par3);
	}
}
