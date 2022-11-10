package net.minecraft.src;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;
import net.lax1dude.eaglercraft.sp.WorkerNetworkManager;
import net.minecraft.server.MinecraftServer;

public class NetLoginHandler extends NetHandler {
	/** The Random object used to generate serverId hex strings. */
	private static EaglercraftRandom rand = new EaglercraftRandom();

	/** Reference to the MinecraftServer object. */
	private final MinecraftServer mcServer;
	public final WorkerNetworkManager myTCPConnection;

	/**
	 * Returns if the login handler is finished and can be removed. It is set to
	 * true on either error or successful login.
	 */
	public boolean finishedProcessing = false;

	/** While waiting to login, if this field ++'s to 600 it will kick you. */
	private int loginTimer = 0;
	private String clientUsername = null;
	private volatile boolean field_72544_i = false;

	private boolean field_92079_k = false;

	private int hash = 0;
	private static int hashBase = 69696969;
	
	private int viewDistance = 2;

	public NetLoginHandler(MinecraftServer par1MinecraftServer, WorkerNetworkManager par2Socket) {
		this.mcServer = par1MinecraftServer;
		this.myTCPConnection = par2Socket;
		hash = ++hashBase;
	}
	
	public boolean shouldBeRemoved() {
		return this.finishedProcessing;
	}

	/**
	 * Logs the user in if a login packet is found, otherwise keeps processing
	 * network packets unless the timeout has occurred.
	 */
	public void handlePackets() {
		System.out.println("[Server][LOGIN][HANDLE][" + clientUsername + "]");
		if (this.field_72544_i) {
			this.initializePlayerConnection();
			return;
		}

		if (this.loginTimer++ == 600) {
			this.kickUser("Took too long to log in");
		} else {
			this.myTCPConnection.processReadPackets();
		}
	}
	
	public boolean equals(Object o) {
		return (o instanceof NetLoginHandler) && ((NetLoginHandler)o).hash == hash;
	}
	
	public int hashCode() {
		return hash;
	}

	/**
	 * Disconnects the user with the given reason.
	 */
	public void kickUser(String par1Str) {
		try {
			this.mcServer.getLogAgent().func_98233_a("Disconnecting " + this.getUsernameAndAddress() + ": " + par1Str);
			this.myTCPConnection.addToSendQueue(new Packet255KickDisconnect(par1Str));
			this.myTCPConnection.serverShutdown();
			this.finishedProcessing = true;
		} catch (Exception var3) {
			var3.printStackTrace();
		}
	}

	public void handleClientProtocol(Packet2ClientProtocol par1Packet2ClientProtocol) {
		this.clientUsername = par1Packet2ClientProtocol.getUsername();
		int var2 = 64 << 3 - par1Packet2ClientProtocol.getViewDistance();
		if(var2 > 400) {
			var2 = 400;
		}
		var2 = (var2 >> 5) + 2;
		this.viewDistance = var2;
		System.out.println("[Server][HANDSHAKE][" + this.clientUsername + "]");

		if (!this.clientUsername.equals(StringUtils.stripControlCodes(this.clientUsername))) {
			this.kickUser("Invalid username!");
		} else {
			if (par1Packet2ClientProtocol.getProtocolVersion() != 61) {
				if (par1Packet2ClientProtocol.getProtocolVersion() > 61) {
					this.kickUser("Outdated server!");
				} else {
					this.kickUser("Outdated client!");
				}
			}else {
				this.initializePlayerConnection();
			}
		}
	}

	public void handleClientCommand(Packet205ClientCommand par1Packet205ClientCommand) {
		if (par1Packet205ClientCommand.forceRespawn == 0) {
			if (this.field_92079_k) {
				this.kickUser("Duplicate login");
				return;
			}

			this.field_92079_k = true;
			this.field_72544_i = true;
		}
	}

	public void handleLogin(Packet1Login par1Packet1Login) {
	}

	/**
	 * on success the specified username is connected to the minecraftInstance,
	 * otherwise they are packet255'd
	 */
	public void initializePlayerConnection() {
		String var1 = this.mcServer.getConfigurationManager()
				.allowUserToConnect(this.clientUsername);

		if (var1 != null) {
			this.kickUser(var1);
		} else {
			EntityPlayerMP var2 = this.mcServer.getConfigurationManager().createPlayerForUser(this.clientUsername);
			if (var2 != null) {
				if (this.mcServer.getServerOwner().equals(this.clientUsername)) {
					var2.renderDistance = this.viewDistance;
				} else {
					EntityPlayerMP fard = this.mcServer.getConfigurationManager().getPlayerEntity(this.mcServer.getServerOwner());
					int maxRenderDistance = fard == null ? 10 : (fard.renderDistance > 10 ? 10 : fard.renderDistance);
					var2.renderDistance = this.viewDistance > maxRenderDistance ? maxRenderDistance : this.viewDistance;
				}
				this.mcServer.getConfigurationManager().initializeConnectionToPlayer(this.myTCPConnection, var2);
			}else {
				this.kickUser("Could not construct EntityPlayerMP for '" + var1 + "'");
			}
		}

		this.finishedProcessing = true;
	}

	public void handleErrorMessage(String par1Str, Object[] par2ArrayOfObj) {
		this.mcServer.getLogAgent().func_98233_a(this.getUsernameAndAddress() + " lost connection");
		this.finishedProcessing = true;
	}

	/**
	 * Handle a server ping packet.
	 */
	public void handleServerPing(Packet254ServerPing par1Packet254ServerPing) {
		try {
			ServerConfigurationManager var2 = this.mcServer.getConfigurationManager();
			String var3 = null;

			if (par1Packet254ServerPing.readSuccessfully == 1) {
				List var4 = Arrays.asList(new Serializable[] { Integer.valueOf(1), Integer.valueOf(61),
						this.mcServer.getMinecraftVersion(), this.mcServer.getMOTD(),
						Integer.valueOf(var2.getCurrentPlayerCount()), Integer.valueOf(var2.getMaxPlayers()) });
				Object var6;

				for (Iterator var5 = var4.iterator(); var5
						.hasNext(); var3 = var3 + var6.toString().replaceAll("\u0000", "")) {
					var6 = var5.next();

					if (var3 == null) {
						var3 = "\u00a7";
					} else {
						var3 = var3 + "\u0000";
					}
				}
			} else {
				var3 = this.mcServer.getMOTD() + "\u00a7" + var2.getCurrentPlayerCount() + "\u00a7"
						+ var2.getMaxPlayers();
			}
			
			this.myTCPConnection.addToSendQueue(new Packet255KickDisconnect(var3));
			this.myTCPConnection.serverShutdown();

			this.finishedProcessing = true;
		} catch (Exception var7) {
			var7.printStackTrace();
		}
	}

	/**
	 * Default handler called for packets that don't have their own handlers in
	 * NetServerHandler; kicks player from the server.
	 */
	public void unexpectedPacket(Packet par1Packet) {
		this.kickUser("Protocol error");
	}

	public String getUsernameAndAddress() {
		return this.clientUsername + "[EAG]";
	}

	/**
	 * determine if it is a server handler
	 */
	public boolean isServerHandler() {
		return true;
	}

	/**
	 * Returns the server Id randomly generated by this login handler.
	 */
	static String getServerId(NetLoginHandler par0NetLoginHandler) {
		return "you eagler";
	}

	/**
	 * Returns the reference to Minecraft Server.
	 */
	static MinecraftServer getLoginMinecraftServer(NetLoginHandler par0NetLoginHandler) {
		return par0NetLoginHandler.mcServer;
	}

	/**
	 * Returns the connecting client username.
	 */
	static String getClientUsername(NetLoginHandler par0NetLoginHandler) {
		return par0NetLoginHandler.clientUsername;
	}

	static boolean func_72531_a(NetLoginHandler par0NetLoginHandler, boolean par1) {
		return par0NetLoginHandler.field_72544_i = par1;
	}
}
