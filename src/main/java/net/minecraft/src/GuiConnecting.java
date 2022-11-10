package net.minecraft.src;

import java.io.IOException;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerProfile;
import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.RateLimit;
import net.minecraft.client.Minecraft;

public class GuiConnecting extends GuiScreen {
	/** A reference to the NetClientHandler. */
	private NetClientHandler clientHandler;
	private INetworkManager networkConnection;
	private String uri;

	/** True if the connection attempt has been cancelled. */
	private boolean cancelled = false;
	private final GuiScreen field_98098_c;

	public GuiConnecting(GuiScreen par1GuiScreen, Minecraft par2Minecraft, ServerData par3ServerData) {
		this.mc = par2Minecraft;
		this.field_98098_c = par1GuiScreen;
		this.clientHandler = null;
		this.networkConnection = null;
		this.uri = par3ServerData.serverIP;
		//ServerAddress var4 = ServerAddress.func_78860_a(par3ServerData.serverIP);
		par2Minecraft.loadWorld((WorldClient) null);
		par2Minecraft.setServerData(par3ServerData);
		//this.spawnNewServerThread(var4.getIP(), var4.getPort());
	}

	//private void spawnNewServerThread(String par1Str, int par2) {
	//	System.out.println("Connecting to " + par1Str + ", " + par2);
	//	(new ThreadConnectToServer(this, par1Str, par2)).start();
	//}
	
	private int timer;

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if (timer > 2 && this.clientHandler == null) {
			try {
				String uria = null;
				if(uri.startsWith("ws://")) {
					uria = uri.substring(5);
				}else if(uri.startsWith("wss://")){
					uria = uri.substring(6);
				}else if(!uri.contains("://")){
					uria = uri;
					uri = ( EaglerAdapter.isSSLPage() ? "wss://" : "ws://") + uri;
				}else {
					this.mc.displayGuiScreen(new GuiDisconnected(this.field_98098_c, "connect.failed", "disconnect.genericReason", "invalid uri websocket protocol", ""));
					return;
				}
				int i = uria.lastIndexOf(':');
				int port = -1;
				
				if(i > 0 && uria.startsWith("[") && uria.charAt(i - 1) != ']') {
					i = -1;
				}
				
				if(i == -1) port = uri.startsWith("wss") ? 443 : 80;
				if(uria.endsWith("/")) uria = uria.substring(0, uria.length() - 1);
				
				if(port == -1) {
					try {
						int i2 = uria.indexOf('/');
						port = Integer.parseInt(uria.substring(i + 1, i2 == -1 ? uria.length() : i2 - 1));
					}catch(Throwable t) {
						this.mc.displayGuiScreen(new GuiDisconnected(this.field_98098_c, "connect.failed", "disconnect.genericReason", "invalid port number", ""));
						return;
					}
				}
				
				this.clientHandler = new NetClientHandler(this.mc, uri, 0);
				this.clientHandler.addToSendQueue(new Packet2ClientProtocol(69, EaglerProfile.username, uria, port));
				this.clientHandler.addToSendQueue(new Packet250CustomPayload("EAG|MySkin", EaglerProfile.getSkinPacket()));
				this.clientHandler.addToSendQueue(new Packet250CustomPayload("EAG|MyCape", EaglerProfile.getCapePacket()));
			} catch (IOException e) {
				try {
					this.clientHandler.disconnect();
				}catch(Throwable t) {
				}
				e.printStackTrace();
				showDisconnectScreen(e.toString());
			}
		}
		if(this.clientHandler != null) {
			this.clientHandler.processReadPackets();
		}
		if(timer > 5) {
			if(!EaglerAdapter.connectionOpen() && this.mc.currentScreen == this) {
				showDisconnectScreen("");
			}
		}
		++timer;
	}
	
	private void showDisconnectScreen(String e) {
		RateLimit l = EaglerAdapter.getRateLimitStatus();
		if(l == RateLimit.NOW_LOCKED) {
			this.mc.displayGuiScreen(new GuiDisconnected(this.field_98098_c, "disconnect.ipNowLocked", "disconnect.endOfStream", null));
		}else if(l == RateLimit.LOCKED) {
			this.mc.displayGuiScreen(new GuiDisconnected(this.field_98098_c, "disconnect.ipLocked", "disconnect.endOfStream", null));
		}else if(l == RateLimit.BLOCKED) {
			this.mc.displayGuiScreen(new GuiDisconnected(this.field_98098_c, "disconnect.ipBlocked", "disconnect.endOfStream", null));
		}else if(l == RateLimit.FAILED_POSSIBLY_LOCKED) {
			this.mc.displayGuiScreen(new GuiDisconnected(this.field_98098_c, "disconnect.ipFailedPossiblyLocked", "disconnect.endOfStream", null));
		}else {
			this.mc.displayGuiScreen(new GuiDisconnected(this.field_98098_c, "connect.failed", "disconnect.genericReason", "could not connect to "+uri, e));
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			this.cancelled = true;

			if (this.clientHandler != null) {
				this.clientHandler.disconnect();
			}
			
			this.mc.stopServerAndDisplayGuiScreen(this.field_98098_c);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		StringTranslate var4 = StringTranslate.getInstance();

		if (this.clientHandler == null) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.connecting"), this.width / 2, this.height / 2 - 50, 16777215);
			this.drawCenteredString(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 16777215);
		} else {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.authorizing"), this.width / 2, this.height / 2 - 50, 16777215);
			this.drawCenteredString(this.fontRenderer, this.clientHandler.field_72560_a, this.width / 2, this.height / 2 - 10, 16777215);
		}

		super.drawScreen(par1, par2, par3);
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Sets the NetClientHandler.
	 */
	public static NetClientHandler setNetClientHandler(GuiConnecting par0GuiConnecting, NetClientHandler par1NetClientHandler) {
		return par0GuiConnecting.clientHandler = par1NetClientHandler;
	}

	public static Minecraft func_74256_a(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	public static boolean isCancelled(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.cancelled;
	}

	public static Minecraft func_74254_c(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	/**
	 * Gets the NetClientHandler.
	 */
	public static NetClientHandler getNetClientHandler(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.clientHandler;
	}

	public static GuiScreen func_98097_e(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.field_98098_c;
	}

	public static Minecraft func_74250_f(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	public static Minecraft func_74251_g(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	public static Minecraft func_98096_h(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}
}
