package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.ConfigConstants;
import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.GuiNetworkSettingsButton;
import net.lax1dude.eaglercraft.GuiScreenConnectOption;
import net.lax1dude.eaglercraft.GuiScreenLANConnecting;
import net.lax1dude.eaglercraft.LANServerList;
import net.lax1dude.eaglercraft.LANServerList.LanServer;
import net.lax1dude.eaglercraft.RelayServer;

public class GuiMultiplayer extends GuiScreen {
	/** Number of outstanding ThreadPollServers threads */
	private static int threadsPending = 0;

	/** Lock object for use with synchronized() */
	private static Object lock = new Object();

	/**
	 * A reference to the screen object that created this. Used for navigating
	 * between screens.
	 */
	private GuiScreen parentScreen;

	/** Slot container for the server list */
	private GuiSlotServer serverSlotContainer;
	private static ServerList internetServerList = null;

	/** Index of the currently selected server */
	private int selectedServer = -1;
	private GuiButton field_96289_p;

	/** The 'Join Server' button */
	private GuiButton buttonSelect;

	/** The 'Delete' button */
	private GuiButton buttonDelete;

	/** The 'Delete' button was clicked */
	private boolean deleteClicked = false;

	/** The 'Add server' button was clicked */
	private boolean addClicked = false;

	/** The 'Edit' button was clicked */
	private boolean editClicked = false;

	/** The 'Direct Connect' button was clicked */
	private boolean directClicked = false;

	/** This GUI's lag tooltip text or null if no lag icon is being hovered. */
	private String lagTooltip = null;

	/** Instance of ServerData. */
	private ServerData theServerData = null;
	
	private boolean hasInitialRefresh = false;

	/** How many ticks this Gui is already opened */
	private int ticksOpened;
	private static LANServerList lanServerList = null;

	private static long lastCooldown = 0l;
	private static long lastRefresh = 0l;
	private static int cooldownTimer = 0;
	private static boolean isLockedOut = false;

	private final GuiNetworkSettingsButton relaysButton;
	
	public GuiMultiplayer(GuiScreen par1GuiScreen) {
		this.parentScreen = par1GuiScreen;
		this.relaysButton = new GuiNetworkSettingsButton(this);
		isLockedOut = false;
		if(lanServerList != null) {
			lanServerList.forceRefresh();
		}
	}
	
	public static void tickRefreshCooldown() {
		if(cooldownTimer > 0) {
			long t = System.currentTimeMillis();
			if(t - lastCooldown > 5000l) {
				--cooldownTimer;
				lastCooldown = t;
			}
		}
	}
	
	private static boolean testIfCanRefresh() {
		long t = System.currentTimeMillis();
		if(t - lastRefresh > 1000l) {
			lastRefresh = t;
			if(cooldownTimer < 8) {
				++cooldownTimer;
			}else {
				isLockedOut = true;
			}
			if(cooldownTimer < 5) {
				isLockedOut = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		EaglerAdapter.enableRepeatEvents(true);
		this.buttonList.clear();

		if (!hasInitialRefresh) {
			hasInitialRefresh = true;
			if(internetServerList == null) {
				internetServerList = new ServerList(this.mc);
			}else {
				if(testIfCanRefresh()) {
					internetServerList.loadServerList();
				}
			}
			if(lanServerList == null) {
				lanServerList = new LANServerList();
			}else {
				if(testIfCanRefresh()) {
					lanServerList.forceRefresh();
				}
			}
			this.serverSlotContainer = new GuiSlotServer(this);
		} else {
			this.serverSlotContainer.func_77207_a(this.width, this.height, 32, this.height - 64);
		}

		this.initGuiControls();
	}

	/**
	 * Populate the GuiScreen controlList
	 */
	public void initGuiControls() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.buttonList.add(this.field_96289_p = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, var1.translateKey("selectServer.edit")));
		this.buttonList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, var1.translateKey("selectServer.delete")));
		this.buttonList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("selectServer.select")));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, var1.translateKey("selectServer.direct")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, var1.translateKey("selectServer.add")));
		this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, var1.translateKey("selectServer.refresh")));
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, var1.translateKey("gui.cancel")));
		boolean var2 = this.selectedServer >= 0 && this.selectedServer < this.serverSlotContainer.getSize();
		this.buttonSelect.enabled = var2;
		this.field_96289_p.enabled = var2;
		this.buttonDelete.enabled = var2;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		internetServerList.updateServerPing();
		lanServerList.update();
		++this.ticksOpened;
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		EaglerAdapter.enableRepeatEvents(false);
	}
	
	public ServerData getTheServerData() {
		return this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "", false);
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 2) {
				String var2 = this.internetServerList.getServerData(this.selectedServer).serverName;

				if (var2 != null) {
					this.deleteClicked = true;
					StringTranslate var3 = StringTranslate.getInstance();
					String var4 = var3.translateKey("selectServer.deleteQuestion");
					String var5 = "\'" + var2 + "\' " + var3.translateKey("selectServer.deleteWarning");
					String var6 = var3.translateKey("selectServer.deleteButton");
					String var7 = var3.translateKey("gui.cancel");
					GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedServer);
					this.mc.displayGuiScreen(var8);
				}
			} else if (par1GuiButton.id == 1) {
				this.joinServer(this.selectedServer);
			} else if (par1GuiButton.id == 4) {
				this.directClicked = true;
				this.mc.displayGuiScreen(new GuiScreenConnectOption(this));
			} else if (par1GuiButton.id == 3) {
				this.addClicked = true;
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "", false)));
			} else if (par1GuiButton.id == 7) {
				this.editClicked = true;
				ServerData var9 = this.internetServerList.getServerData(this.selectedServer);
				this.theServerData = new ServerData(var9.serverName, var9.serverIP, false);
				this.theServerData.setHideAddress(var9.isHidingAddress());
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData));
			} else if (par1GuiButton.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (par1GuiButton.id == 8) {
				if(testIfCanRefresh()) {
					lastRefresh = 0;
					--cooldownTimer;
					this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
				}
			} else {
				this.serverSlotContainer.actionPerformed(par1GuiButton);
			}
		}
	}

	public void confirmClicked(boolean par1, int par2) {
		if (this.deleteClicked) {
			this.deleteClicked = false;

			if (par1) {
				internetServerList.removeServerData(par2);
				internetServerList.saveServerList();
				this.selectedServer = -1;
			}

			this.mc.displayGuiScreen(this);
		} else if (this.directClicked) {
			this.directClicked = false;

			if (par1) {
				this.connectToServer(this.theServerData);
			} else {
				this.mc.displayGuiScreen(this);
			}
		} else if (this.addClicked) {
			this.addClicked = false;

			if (par1) {
				internetServerList.addServerData(this.theServerData);
				internetServerList.saveServerList();
				this.selectedServer = -1;
			}

			this.mc.displayGuiScreen(this);
		} else if (this.editClicked) {
			this.editClicked = false;

			if (par1) {
				ServerData var3 = this.internetServerList.getServerData(this.selectedServer);
				var3.serverName = this.theServerData.serverName;
				var3.serverIP = this.theServerData.serverIP;
				var3.setHideAddress(this.theServerData.isHidingAddress());
				var3.pingSentTime = -1l;
				this.internetServerList.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		int var3 = this.selectedServer;

		if (par2 == 59) {
			this.mc.gameSettings.hideServerAddress = !this.mc.gameSettings.hideServerAddress;
			this.mc.gameSettings.saveOptions();
		} else {
			if (isShiftKeyDown() && par2 == 200) {
				if (var3 > ServerList.forcedServers.size() && var3 < this.internetServerList.countServers()) {
					this.internetServerList.swapServers(var3, var3 - 1);
					--this.selectedServer;

					if (var3 < this.internetServerList.countServers() - 1) {
						this.serverSlotContainer.func_77208_b(-this.serverSlotContainer.slotHeight);
					}
				}
			} else if (isShiftKeyDown() && par2 == 208) {
				if (var3 < this.internetServerList.countServers() - 1) {
					this.internetServerList.swapServers(var3, var3 + 1);
					++this.selectedServer;

					if (var3 > 0) {
						this.serverSlotContainer.func_77208_b(this.serverSlotContainer.slotHeight);
					}
				}
			} else if (par1 == 13) {
				this.actionPerformed((GuiButton) this.buttonList.get(2));
			} else {
				super.keyTyped(par1, par2);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.lagTooltip = null;
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		
		boolean showAyonull = ConfigConstants.ayonullTitle != null && ConfigConstants.ayonullLink != null;
		
		this.serverSlotContainer.top = showAyonull ? 42 : 32;
		this.serverSlotContainer.drawScreen(par1, par2, par3);
		
		if(showAyonull) {
			this.drawCenteredString(this.fontRenderer, ConfigConstants.ayonullTitle, this.width / 2, 12, 0xDDDD66);
			
			String link = ConfigConstants.ayonullLink;
			int linkWidth = fontRenderer.getStringWidth(link);
			boolean mouseOver = par1 > (this.width - linkWidth) / 2 - 10 && par1 < (this.width + linkWidth) / 2 + 10 && par2 > 21 && par2 < 35;
			this.drawString(this.fontRenderer, EnumChatFormatting.UNDERLINE + link, (this.width - linkWidth) / 2, 23, mouseOver ? 0xBBBBFF : 0x7777DD);
		}else {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("multiplayer.title"), this.width / 2, 16, 16777215);
		}
		
		super.drawScreen(par1, par2, par3);

		if (this.lagTooltip != null) {
			this.func_74007_a(this.lagTooltip, par1, par2);
		}
		
		if(isLockedOut) {
			String canYouNot = "can you not";
			int w = this.fontRenderer.getStringWidth(canYouNot);
			drawRect((this.width - w - 4) / 2, this.height - 80, (this.width + w + 4) / 2, this.height - 70, 0xCC000000);
			fontRenderer.drawStringWithShadow(canYouNot, (this.width - w) / 2, this.height - 79, 0xFFDD2222);
			if(cooldownTimer < 3) {
				isLockedOut = false;
			}
		}
		
		relaysButton.drawScreen(par1, par2);
	}
	

	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0 && ConfigConstants.ayonullTitle != null && ConfigConstants.ayonullLink != null) {
			int linkWidth = fontRenderer.getStringWidth(ConfigConstants.ayonullLink);
			boolean mouseOver = par1 > (this.width - linkWidth) / 2 - 10 && par1 < (this.width + linkWidth) / 2 + 10 && par2 > 21 && par2 < 35;
			if(mouseOver) {
				EaglerAdapter.openLink(ConfigConstants.ayonullLink);
				return;
			}
		}
		relaysButton.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}

	/**
	 * Join server by slot index
	 */
	private void joinServer(int par1) {
		if (par1 < internetServerList.countServers()) {
			this.connectToServer(this.internetServerList.getServerData(par1));
		} else {
			par1 -= internetServerList.countServers();

			if (par1 < lanServerList.countServers()) {
				LanServer var2 = lanServerList.getServer(par1);
				connectToLAN("Connecting to '" + var2.getLanServerMotd() + "'...", var2.getLanServerCode(), var2.getLanServerRelay());
			}
		}
	}

	private void connectToServer(ServerData par1ServerData) {
		this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, par1ServerData));
	}
	
	private void connectToLAN(String text, String code, RelayServer uri) {
		this.mc.loadingScreen.resetProgresAndWorkingMessage(text);
		this.mc.displayGuiScreen(new GuiScreenLANConnecting(this, code, uri));
	}

	protected void func_74007_a(String par1Str, int par2, int par3) {
		if (par1Str != null) {
			if(par1Str.indexOf('\n') >= 0) {
				String[] strs = par1Str.split("\n");
				int var6 = 0;
				int full = 0;
				for(int i = 0; i < strs.length; ++i) {
					strs[i] = strs[i].replace('\r', ' ').trim();
					if(strs[i].length() > 0) {
						int w = this.fontRenderer.getStringWidth(strs[i]);
						if(w > var6) {
							var6 = w;
						}
						++full;
					}
				}
				int var4 = par2 + 12;
				int var5 = par3 - 12;
				this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + full * 9 + 2, -1073741824, -1073741824);
				full = 0;
				for(int i = 0; i < strs.length; ++i) {
					if(strs[i].length() > 0) {
						this.fontRenderer.drawStringWithShadow(strs[i], var4, var5 + 9 * full++, -1);
					}
				}
			}else {
				int var4 = par2 + 12;
				int var5 = par3 - 12;
				int var6 = this.fontRenderer.getStringWidth(par1Str);
				this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
				this.fontRenderer.drawStringWithShadow(par1Str, var4, var5, -1);
			}
		}
	}

	static ServerList getInternetServerList(GuiMultiplayer par0GuiMultiplayer) {
		return internetServerList;
	}

	static LANServerList getListOfLanServers(GuiMultiplayer par0GuiMultiplayer) {
		return lanServerList;
	}

	static int getSelectedServer(GuiMultiplayer par0GuiMultiplayer) {
		int i = internetServerList.countServers() + lanServerList.countServers();
		if(par0GuiMultiplayer.selectedServer >= i && par0GuiMultiplayer.selectedServer > 0) {
			par0GuiMultiplayer.selectedServer = i - 1;
		}
		return par0GuiMultiplayer.selectedServer;
	}

	static int getAndSetSelectedServer(GuiMultiplayer par0GuiMultiplayer, int par1) {
		return par0GuiMultiplayer.selectedServer = par1;
	}

	/**
	 * Return buttonSelect GuiButton
	 */
	static GuiButton getButtonSelect(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.buttonSelect;
	}

	/**
	 * Return buttonEdit GuiButton
	 */
	static GuiButton getButtonEdit(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.field_96289_p;
	}

	/**
	 * Return buttonDelete GuiButton
	 */
	static GuiButton getButtonDelete(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.buttonDelete;
	}

	static void func_74008_b(GuiMultiplayer par0GuiMultiplayer, int par1) {
		par0GuiMultiplayer.joinServer(par1);
	}

	static int getTicksOpened(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.ticksOpened;
	}

	/**
	 * Returns the lock object for use with synchronized()
	 */
	static Object getLock() {
		return lock;
	}

	static int getThreadsPending() {
		return threadsPending;
	}

	static int increaseThreadsPending() {
		return threadsPending++;
	}

	static int decreaseThreadsPending() {
		return threadsPending--;
	}

	static String getAndSetLagTooltip(GuiMultiplayer par0GuiMultiplayer, String par1Str) {
		return par0GuiMultiplayer.lagTooltip = par1Str;
	}

}