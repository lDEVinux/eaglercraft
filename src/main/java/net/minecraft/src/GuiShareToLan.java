package net.minecraft.src;

import net.lax1dude.eaglercraft.GuiNetworkSettingsButton;
import net.lax1dude.eaglercraft.GuiScreenNoRelays;
import net.lax1dude.eaglercraft.IntegratedServer;
import net.lax1dude.eaglercraft.IntegratedServerLAN;

public class GuiShareToLan extends GuiScreen {
	/**
	 * A reference to the screen object that created this. Used for navigating
	 * between screens.
	 */
	private final GuiScreen parentScreen;
	private GuiButton buttonAllowCommandsToggle;
	private GuiButton buttonGameMode;
	private GuiButton buttonHiddenToggle;

	/**
	 * The currently selected game mode. One of 'survival', 'creative', or
	 * 'adventure'
	 */
	private String gameMode;

	/** True if 'Allow Cheats' is currently enabled */
	private boolean allowCommands = false;
	
	private final GuiNetworkSettingsButton relaysButton;
	
	private boolean hiddenToggle = false;
	
	private GuiTextField codeTextField;

	public GuiShareToLan(GuiScreen par1GuiScreen) {
		this.parentScreen = par1GuiScreen;
		this.relaysButton = new GuiNetworkSettingsButton(this);
		this.gameMode = par1GuiScreen.mc.playerController.getGameType().getName();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height - 28, 150, 20,
				StatCollector.translateToLocal("lanServer.start")));
		this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height - 28, 150, 20,
				StatCollector.translateToLocal("gui.cancel")));
		this.buttonList.add(this.buttonGameMode = new GuiButton(104, this.width / 2 - 155, 160, 150, 20,
				StatCollector.translateToLocal("selectWorld.gameMode")));
		this.buttonList.add(this.buttonAllowCommandsToggle = new GuiButton(103, this.width / 2 + 5, 160, 150, 20,
				StatCollector.translateToLocal("selectWorld.allowCommands")));
		this.buttonList.add(this.buttonHiddenToggle = new GuiButton(105, this.width / 2 - 75, 190, 150, 20,
				StatCollector.translateToLocal("lanServer.hidden")));
		this.codeTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 100, 200, 20);
		this.codeTextField.setText(mc.thePlayer.username + "'s World");
		this.codeTextField.setFocused(true);
		this.codeTextField.setMaxStringLength(252);
		this.func_74088_g();
	}

	private void func_74088_g() {
		this.buttonGameMode.displayString = StatCollector.translateToLocal("selectWorld.gameMode") + " "
				+ StatCollector.translateToLocal("selectWorld.gameMode." + this.gameMode);
		this.buttonAllowCommandsToggle.displayString = StatCollector.translateToLocal("selectWorld.allowCommands")
				+ " ";
		this.buttonHiddenToggle.displayString = StatCollector.translateToLocal("lanServer.hidden")
				+ " ";

		if (this.allowCommands) {
			this.buttonAllowCommandsToggle.displayString = this.buttonAllowCommandsToggle.displayString
					+ StatCollector.translateToLocal("options.on");
		} else {
			this.buttonAllowCommandsToggle.displayString = this.buttonAllowCommandsToggle.displayString
					+ StatCollector.translateToLocal("options.off");
		}
		
		if(this.hiddenToggle) {
			this.buttonHiddenToggle.displayString = this.buttonHiddenToggle.displayString
					+ StatCollector.translateToLocal("options.on");
		} else {
			this.buttonHiddenToggle.displayString = this.buttonHiddenToggle.displayString
					+ StatCollector.translateToLocal("options.off");
		}
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 102) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else if (par1GuiButton.id == 104) {
			if (this.gameMode.equals("survival")) {
				this.gameMode = "creative";
			} else if (this.gameMode.equals("creative")) {
				this.gameMode = "adventure";
			} else {
				this.gameMode = "survival";
			}

			this.func_74088_g();
		} else if (par1GuiButton.id == 103) {
			this.allowCommands = !this.allowCommands;
			this.func_74088_g();
		}  else if (par1GuiButton.id == 105) {
			this.hiddenToggle = !this.hiddenToggle;
			this.func_74088_g();
		} else if (par1GuiButton.id == 101) {
			String worldName = this.codeTextField.getText().trim();
			if(worldName.length() == 0) {
				worldName = mc.thePlayer.username + "'s World";
			}
			if(worldName.length() >= 252) {
				worldName = worldName.substring(0, 252);
			}
			this.mc.displayGuiScreen((GuiScreen) null);
			LoadingScreenRenderer ls = mc.loadingScreen;
			String code = IntegratedServerLAN.shareToLAN((str) -> ls.resetProgresAndWorkingMessage(str), worldName, hiddenToggle);
			if (code != null) {
				IntegratedServer.configureLAN(EnumGameType.getByName(this.gameMode), this.allowCommands);
				this.mc.ingameGUI.getChatGUI().printChatMessage(StringTranslate.getInstance().translateKey("lanServer.opened")
						.replace("$relay$", IntegratedServerLAN.getCurrentURI()).replace("$code$", code));
				this.mc.lanState = true;
			} else {
				IntegratedServer.configureLAN(mc.theWorld.getWorldInfo().getGameType(), false);
				this.mc.displayGuiScreen(new GuiScreenNoRelays(this, "noRelay.titleFail"));
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("lanServer.title"), this.width / 2,
				50, 16777215);
		this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("lanServer.worldName"), this.width / 2,
				82, 16777215);
		this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("lanServer.otherPlayers"),
				this.width / 2, 142, 16777215);
		super.drawScreen(par1, par2, par3);
		this.relaysButton.drawScreen(par1, par2);
		this.codeTextField.drawTextBox();
	}
	
	public void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.relaysButton.mouseClicked(par1, par2, par3);
		this.codeTextField.mouseClicked(par1, par2, par3);
	}
	
	protected void keyTyped(char c, int k) {
		super.keyTyped(c, k);
		this.codeTextField.textboxKeyTyped(c, k);
	}
	
	public void updateScreen() {
		super.updateScreen();
		this.codeTextField.updateCursorCounter();
	}
	
}