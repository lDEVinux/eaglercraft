package net.minecraft.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.sp.IntegratedServer;
import net.lax1dude.eaglercraft.sp.SYS;
import net.lax1dude.eaglercraft.sp.VFSSaveHandler;
import net.lax1dude.eaglercraft.sp.VFile;
import net.lax1dude.eaglercraft.sp.WorkerListenThread;
import net.lax1dude.eaglercraft.sp.ipc.IPCPacket0DProgressUpdate;
import net.lax1dude.eaglercraft.sp.ipc.IPCPacket14StringList;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CommandBase;
import net.minecraft.src.DispenserBehaviors;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.ILogAgent;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.Profiler;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.StringUtils;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;

public abstract class MinecraftServer implements ICommandSender, Runnable {
	/** Instance of Minecraft Server. */
	protected static MinecraftServer mcServer = null;

	/** List of names of players who are online. */
	protected final List playersOnline = new ArrayList();
	protected final ICommandManager commandManager;
	public final Profiler theProfiler = new Profiler();

	/** The server world instances. */
	public WorldServer[] worldServers;

	/** The ServerConfigurationManager instance. */
	protected ServerConfigurationManager serverConfigManager;

	/**
	 * Indicates whether the server is running or not. Set to false to initiate a
	 * shutdown.
	 */
	protected boolean serverRunning = true;

	/** Indicates to other classes that the server is safely stopped. */
	protected boolean serverStopped = false;

	/** Incremented every tick. */
	protected int tickCounter = 0;

	/**
	 * The task the server is currently working on(and will output on
	 * outputPercentRemaining).
	 */
	protected String currentTask;

	/** The percentage of the current task finished so far. */
	protected int percentDone;

	/** True if the server has animals turned on. */
	protected boolean canSpawnAnimals;
	protected boolean canSpawnNPCs;

	/** Indicates whether PvP is active on the server or not. */
	protected boolean pvpEnabled;

	/** Determines if flight is allowed or not. */
	protected boolean allowFlight;

	/** The server MOTD string. */
	protected String motd;

	/** Maximum build height. */
	protected int buildLimit;
	protected long lastSentPacketID;
	protected long lastSentPacketSize;
	protected long lastReceivedID;
	protected long lastReceivedSize;
	public final long[] sentPacketCountArray = new long[100];
	public final long[] sentPacketSizeArray = new long[100];
	public final long[] receivedPacketCountArray = new long[100];
	public final long[] receivedPacketSizeArray = new long[100];
	public final long[] tickTimeArray = new long[100];

	/** Stats are [dimension][tick%100] system.nanoTime is stored. */
	public long[][] timeOfLastDimensionTick;

	/** Username of the server owner (for integrated servers) */
	protected String serverOwner;
	protected String folderName;

	/**
	 * If true, there is no need to save chunks or stop the server, because that is
	 * already being done.
	 */
	protected boolean worldIsBeingDeleted;
	protected String texturePack = "";
	protected boolean serverIsRunning = false;

	/**
	 * Set when warned for "Can't keep up", which triggers again after 15 seconds.
	 */
	protected long timeOfLastWarning;
	protected String userMessage;
	protected boolean startProfiling;
	protected boolean field_104057_T = false;

	private int tpsCounter = 0;
	private int tpsMeasure = 0;
	private long tpsTimer = 0l;

	public MinecraftServer(String folder) {
		mcServer = this;
		this.folderName = folder;
		this.commandManager = new ServerCommandManager();
		this.registerDispenseBehaviors();
	}

	/**
	 * Register all dispense behaviors.
	 */
	private void registerDispenseBehaviors() {
		DispenserBehaviors.func_96467_a();
	}

	/**
	 * Initialises the server and starts it.
	 */
	protected abstract boolean startServer() throws IOException;

	protected void convertMapIfNeeded(String par1Str) {
		// no
	}

	/**
	 * Typically "menu.convertingLevel", "menu.loadingLevel" or others.
	 */
	protected void setUserMessage(String par1Str) {
		IntegratedServer.sendIPCPacket(new IPCPacket0DProgressUpdate(par1Str, 0.0f));
		this.logInfo(par1Str);
		this.userMessage = par1Str;
	}
	
	protected void setUserMessage(String par1Str, float prog) {
		IntegratedServer.sendIPCPacket(new IPCPacket0DProgressUpdate(par1Str, prog));
		this.logInfo(par1Str + ": " + (prog > 1.0f ? "" + (int)prog : "" + (int)(prog * 100.0f) + "%"));
		this.userMessage = par1Str;
	}

	protected void loadAllWorlds(String par1Str, long par3, WorldSettings par5WorldType) {
		this.setUserMessage("menu.loadingLevel");
		this.worldServers = new WorldServer[3];
		this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
		ISaveHandler var7 = new VFSSaveHandler(new VFile("worlds", par1Str));
		WorldInfo var9 = var7.loadWorldInfo();
		WorldSettings var8;

		if (var9 == null) {
			if(par5WorldType == null) {
				throw new IllegalArgumentException("World '" + par1Str + "' does not exist and WorldSettings is null");
			}
			var8 = par5WorldType;
		} else {
			var8 = new WorldSettings(var9);
		}

		for (int var10 = 0; var10 < this.worldServers.length; ++var10) {
			byte var11 = 0;

			if (var10 == 1) {
				var11 = -1;
			}

			if (var10 == 2) {
				var11 = 1;
			}

			if (var10 == 0) {
				this.worldServers[var10] = new WorldServer(this, var7, par1Str, var11, var8, this.theProfiler, this.getLogAgent());
			} else {
				this.worldServers[var10] = new WorldServerMulti(this, var7, par1Str, var11, var8, this.worldServers[0],
						this.theProfiler, this.getLogAgent());
			}

			this.worldServers[var10].addWorldAccess(new WorldManager(this, this.worldServers[var10]));
			this.worldServers[var10].getWorldInfo().setGameType(this.getGameType());

			this.serverConfigManager.setPlayerManager(this.worldServers);
		}

		this.setDifficultyForAllWorlds(this.getDifficulty());
		this.setGameType(var8.getGameType());
		this.initialWorldChunkLoad();
	}

	protected void initialWorldChunkLoad() {
		int var5 = 0;
		//this.setUserMessage("menu.generatingTerrain");
		byte var6 = 0;
		this.setUserMessage("Preparing start region for level " + var6);
		
		// Removed 'spawn chunks' for performance, they are unnecessary
		
		/*
		WorldServer var7 = this.worldServers[var6];
		ChunkCoordinates var8 = var7.getSpawnPoint();
		long var9 = System.currentTimeMillis();
		
		int prepareRadius = 64;

		for (int var11 = -prepareRadius; var11 <= prepareRadius && this.isServerRunning(); var11 += 16) {
			for (int var12 = -prepareRadius; var12 <= prepareRadius && this.isServerRunning(); var12 += 16) {
				long var13 = System.currentTimeMillis();

				if (var13 - var9 > 1000L) {
					setUserMessage("Preparing spawn area", Math.min(var5 / 64.0f, 0.99f));
					var9 = var13;
				}

				++var5;
				var7.theChunkProviderServer.loadChunk(var8.posX + var11 >> 4, var8.posZ + var12 >> 4);
			}
		}
		 */
		
		this.clearCurrentTask();
	}

	public abstract boolean canStructuresSpawn();

	public abstract EnumGameType getGameType();

	/**
	 * Defaults to "1" (Easy) for the dedicated server, defaults to "2" (Normal) on
	 * the client.
	 */
	public abstract int getDifficulty();

	/**
	 * Defaults to false.
	 */
	public abstract boolean isHardcore();

	/**
	 * Used to display a percent remaining given text and the percentage.
	 */
	protected void outputPercentRemaining(String par1Str, int par2) {
		this.currentTask = par1Str;
		this.percentDone = par2;
		setUserMessage(par1Str, (par2 / 100.0f));
	}

	/**
	 * Set current task to null and set its percentage to 0.
	 */
	protected void clearCurrentTask() {
		this.currentTask = null;
		this.percentDone = 0;
	}

	/**
	 * par1 indicates if a log message should be output.
	 */
	public void saveAllWorlds(boolean par1) {
		if (!this.worldIsBeingDeleted) {
			WorldServer[] var2 = this.worldServers;
			int var3 = var2.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				WorldServer var5 = var2[var4];

				if (var5 != null) {
					setUserMessage("Saving chunks for level \'" + var5.getWorldInfo().getWorldName() + "\'/" + var5.provider.getDimensionName());

					try {
						var5.saveAllChunks(true, (IProgressUpdate) null);
					} catch (MinecraftException var7) {
						this.getLogAgent().func_98236_b(var7.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Saves all necessary data as preparation for stopping the server.
	 */
	public void stopServer() {
		if (!this.worldIsBeingDeleted) {
			setUserMessage("Stopping server");

			if (this.getNetworkThread() != null) {
				this.getNetworkThread().stopListening();
			}

			if (this.serverConfigManager != null) {
				this.getLogAgent().func_98233_a("Saving players");
				this.serverConfigManager.saveAllPlayerData();
				this.serverConfigManager.removeAllPlayers();
			}

			setUserMessage("Saving worlds");
			this.saveAllWorlds(false);

			for (int var1 = 0; var1 < this.worldServers.length; ++var1) {
				WorldServer var2 = this.worldServers[var1];
				var2.flush();
			}
		}
	}

	/**
	 * "getHostname" is already taken, but both return the hostname.
	 */
	public String getServerHostname() {
		return "127.1.1.1";
	}

	public void setHostname(String par1Str) {
		throw new IllegalArgumentException("variable removed");
	}

	public boolean isServerRunning() {
		return this.serverRunning;
	}

	/**
	 * Sets the serverRunning variable to false, in order to get the server to shut
	 * down.
	 */
	public void initiateShutdown() {
		this.serverRunning = false;
	}

	public void run() {
		try {
			if (this.startServer()) {
				long var1 = System.currentTimeMillis();

				for (long var50 = 0L; this.serverRunning; this.serverIsRunning = true) {
					long var5 = System.currentTimeMillis();
					long var7 = var5 - var1;

					if (var7 > 2000L && var1 - this.timeOfLastWarning >= 15000L) {
						this.getLogAgent().func_98236_b(
								"Can\'t keep up! Did the system time change, or is the server overloaded?");
						var7 = 2000L;
						this.timeOfLastWarning = var1;
					}

					if (var7 < 0L) {
						this.getLogAgent().func_98236_b("Time ran backwards! Did the system time change?");
						var7 = 0L;
					}

					var50 += var7;
					var1 = var5;

					if (this.worldServers[0].areAllPlayersAsleep()) {
						this.tick();
						var50 = 0L;
					} else {
						while (var50 > 50L) {
							var50 -= 50L;
							this.tick();
						}
					}

					Thread.sleep(1L);
				}
			} else {
				throw new RuntimeException("Server did not init correctly");
			}
		} catch (Throwable var48) {
			this.getLogAgent().logSevereException(
					"Encountered an unexpected exception " + var48.getClass().getSimpleName(), var48);
			var48.printStackTrace();
			IntegratedServer.throwExceptionToClient("Encountered an unexpected exception", var48);
		} finally {
			try {
				this.stopServer();
				this.serverStopped = true;
			} catch (Throwable var46) {
				var46.printStackTrace();
			} finally {
				this.systemExitNow();
			}
		}
	}

	protected VFile getDataDirectory() {
		return new VFile(".");
	}

	/**
	 * Directly calls System.exit(0), instantly killing the program.
	 */
	protected void systemExitNow() {
	}

	/**
	 * Main function called by run() every loop.
	 */
	protected void tick() {
		long var1 = System.nanoTime();
		AxisAlignedBB.getAABBPool().cleanPool();
		++this.tickCounter;

		if (this.startProfiling) {
			this.startProfiling = false;
			this.theProfiler.profilingEnabled = true;
			this.theProfiler.clearProfiling();
		}

		this.theProfiler.startSection("root");
		this.updateTimeLightAndEntities();

		if (this.tickCounter % 900 == 0) {
			this.theProfiler.startSection("save");
			this.serverConfigManager.saveAllPlayerData();
			this.saveAllWorlds(true);
			this.theProfiler.endSection();
		}

		this.theProfiler.startSection("tallying");
		this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - var1;
		this.sentPacketCountArray[this.tickCounter % 100] = Packet.sentID - this.lastSentPacketID;
		this.lastSentPacketID = Packet.sentID;
		this.sentPacketSizeArray[this.tickCounter % 100] = Packet.sentSize - this.lastSentPacketSize;
		this.lastSentPacketSize = Packet.sentSize;
		this.receivedPacketCountArray[this.tickCounter % 100] = Packet.receivedID - this.lastReceivedID;
		this.lastReceivedID = Packet.receivedID;
		this.receivedPacketSizeArray[this.tickCounter % 100] = Packet.receivedSize - this.lastReceivedSize;
		this.lastReceivedSize = Packet.receivedSize;
		this.theProfiler.endSection();
		this.theProfiler.endSection();
		
		++tpsCounter;
		long millis = System.currentTimeMillis();
		long elapsed = millis - tpsTimer;
		if(elapsed >= 1000l) {
			tpsTimer = millis;
			tpsMeasure = (int)(tpsCounter * 1000l / elapsed);
			IntegratedServer.sendIPCPacket(new IPCPacket14StringList(IPCPacket14StringList.SERVER_TPS, getTPSAndChunkBuffer()));
			tpsCounter = 0;
		}
	}
	
	public List<String> getTPSAndChunkBuffer() {
		ArrayList<String> strs = new ArrayList();
		strs.add("Ticks/Second: " + tpsCounter + "/20");
		
		int c = 0;
		int oc = 0;
		int e = 0;
		int te = 0;
		int r = 0;
		int w = 0;
		int g = 0;
		int tu = 0;
		int lu = 0;
		for(int i = 0; i < worldServers.length; ++i) {
			c += worldServers[i].getChunkProvider().getLoadedChunkCount();
			e += worldServers[i].loadedEntityList.size();
			te += worldServers[i].loadedTileEntityList.size();
			r += worldServers[i].getR();
			w += worldServers[i].getW();
			g += worldServers[i].getG();
			lu += worldServers[i].getLU();
			tu += worldServers[i].getTU();
		}
		for(EntityPlayerMP p : (List<EntityPlayerMP>)this.playersOnline) {
			oc += p.loadedChunks.size();
		}

		strs.add("Chunks: " + c + "/" + (c + oc));
		strs.add("Entities: " + e + "+" + te);
		strs.add("R: " + r + ", G: " + g + ", W: " + w);
		strs.add("TU: " + tu + " LU: " + lu);
		int pp = this.playersOnline.size();
		if(pp > 1) {
			strs.add("Players: " + pp);
		}
		return strs;
	}

	public void updateTimeLightAndEntities() {
		this.theProfiler.startSection("levels");
		int var1;

		for (var1 = 0; var1 < this.worldServers.length; ++var1) {
			long var2 = System.nanoTime();

			if (var1 == 0 || this.getAllowNether()) {
				WorldServer var4 = this.worldServers[var1];
				this.theProfiler.startSection(var4.getWorldInfo().getWorldName());
				this.theProfiler.startSection("pools");
				var4.getWorldVec3Pool().clear();
				this.theProfiler.endSection();

				if (this.tickCounter % 20 == 0) {
					this.theProfiler.startSection("timeSync");
					this.serverConfigManager.sendPacketToAllPlayersInDimension(
							new Packet4UpdateTime(var4.getTotalWorldTime(), var4.getWorldTime()),
							var4.provider.dimensionId);
					this.theProfiler.endSection();
				}

				this.theProfiler.startSection("tick");
				
				var4.tick();
				var4.updateEntities();
				
				this.theProfiler.endSection();
				this.theProfiler.startSection("tracker");
				var4.getEntityTracker().updateTrackedEntities();
				this.theProfiler.endSection();
				this.theProfiler.endSection();
			}

			this.timeOfLastDimensionTick[var1][this.tickCounter % 100] = System.nanoTime() - var2;
		}

		this.theProfiler.endStartSection("connection");
		this.getNetworkThread().handleNetworkListenThread();
		this.theProfiler.endStartSection("players");
		this.serverConfigManager.onTick();
		this.theProfiler.endStartSection("tickables");

		for (var1 = 0; var1 < this.playersOnline.size(); ++var1) {
			((IUpdatePlayerListBox) this.playersOnline.get(var1)).update();
		}

		this.theProfiler.endSection();
	}

	public boolean getAllowNether() {
		return true;
	}

	public void func_82010_a(IUpdatePlayerListBox par1IUpdatePlayerListBox) {
		this.playersOnline.add(par1IUpdatePlayerListBox);
	}
	
	/**
	 * Returns a File object from the specified string.
	 */
	public VFile getFile(String par1Str) {
		return new VFile(folderName, par1Str);
	}

	/**
	 * Logs the message with a level of INFO.
	 */
	public void logInfo(String par1Str) {
		this.getLogAgent().func_98233_a(par1Str);
	}

	/**
	 * Logs the message with a level of WARN.
	 */
	public void logWarning(String par1Str) {
		this.getLogAgent().func_98236_b(par1Str);
	}

	/**
	 * Gets the worldServer by the given dimension.
	 */
	public WorldServer worldServerForDimension(int par1) {
		return par1 == -1 ? this.worldServers[1] : (par1 == 1 ? this.worldServers[2] : this.worldServers[0]);
	}

	/**
	 * Returns the server's hostname.
	 */
	public String getHostname() {
		return this.getServerHostname();
	}

	/**
	 * Never used, but "getServerPort" is already taken.
	 */
	public int getPort() {
		return this.getServerPort();
	}

	/**
	 * Returns the server message of the day
	 */
	public String getMotd() {
		return this.motd;
	}

	/**
	 * Returns the server's Minecraft version as string.
	 */
	public String getMinecraftVersion() {
		return "1.5.2";
	}

	/**
	 * Returns the number of players currently on the server.
	 */
	public int getCurrentPlayerCount() {
		return this.serverConfigManager.getCurrentPlayerCount();
	}

	/**
	 * Returns the maximum number of players allowed on the server.
	 */
	public int getMaxPlayers() {
		return this.serverConfigManager.getMaxPlayers();
	}

	/**
	 * Returns an array of the usernames of all the connected players.
	 */
	public String[] getAllUsernames() {
		return this.serverConfigManager.getAllUsernames();
	}

	/**
	 * Used by RCon's Query in the form of "MajorServerMod 1.2.3: MyPlugin 1.3;
	 * AnotherPlugin 2.1; AndSoForth 1.0".
	 */
	public String getPlugins() {
		return "";
	}

	/**
	 * Handle a command received by an RCon instance
	 */
	public String handleRConCommand(String par1Str) {
		return "fuck off";
	}

	/**
	 * Returns true if debugging is enabled, false otherwise.
	 */
	public boolean isDebuggingEnabled() {
		return true;
	}

	/**
	 * Logs the error message with a level of SEVERE.
	 */
	public void logSevere(String par1Str) {
		this.getLogAgent().logSevere(par1Str);
	}

	/**
	 * If isDebuggingEnabled(), logs the message with a level of INFO.
	 */
	public void logDebug(String par1Str) {
		if (this.isDebuggingEnabled()) {
			this.getLogAgent().func_98233_a(par1Str);
		}
	}

	public String getServerModName() {
		return "eaglercraft";
	}

	/**
	 * If par2Str begins with /, then it searches for commands, otherwise it returns
	 * players.
	 */
	public List getPossibleCompletions(ICommandSender par1ICommandSender, String par2Str) {
		ArrayList var3 = new ArrayList();

		if (par2Str.startsWith("/")) {
			par2Str = par2Str.substring(1);
			boolean var10 = !par2Str.contains(" ");
			List var11 = this.commandManager.getPossibleCommands(par1ICommandSender, par2Str);

			if (var11 != null) {
				Iterator var12 = var11.iterator();

				while (var12.hasNext()) {
					String var13 = (String) var12.next();

					if (var10) {
						var3.add("/" + var13);
					} else {
						var3.add(var13);
					}
				}
			}

			return var3;
		} else {
			String[] var4 = par2Str.split(" ", -1);
			String var5 = var4[var4.length - 1];
			String[] var6 = this.serverConfigManager.getAllUsernames();
			int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				String var9 = var6[var8];

				if (CommandBase.doesStringStartWith(var5, var9)) {
					var3.add(var9);
				}
			}

			return var3;
		}
	}

	/**
	 * Gets mcServer.
	 */
	public static MinecraftServer getServer() {
		return mcServer;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly "Rcon")
	 */
	public String getCommandSenderName() {
		return "Host";
	}

	public void sendChatToPlayer(String par1Str) {
		this.getLogAgent().func_98233_a(StringUtils.stripControlCodes(par1Str));
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 */
	public boolean canCommandSenderUseCommand(int par1, String par2Str) {
		return par2Str.equals(this.getServerOwner());
	}

	/**
	 * Translates and formats the given string key with the given arguments.
	 */
	public String translateString(String par1Str, Object... par2ArrayOfObj) {
		return StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj);
	}

	public ICommandManager getCommandManager() {
		return this.commandManager;
	}

	/**
	 * Gets serverPort.
	 */
	public int getServerPort() {
		return 1;
	}

	public void setServerPort(int par1) {
		throw new IllegalArgumentException("variable removed");
	}

	/**
	 * Returns the username of the server owner (for integrated servers)
	 */
	public String getServerOwner() {
		return this.serverOwner;
	}

	/**
	 * Sets the username of the owner of this server (in the case of an integrated
	 * server)
	 */
	public void setServerOwner(String par1Str) {
		this.serverOwner = par1Str;
	}

	public boolean isSinglePlayer() {
		return this.serverOwner != null;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String par1Str) {
		this.folderName = par1Str;
	}

	public void setDifficultyForAllWorlds(int par1) {
		for (int var2 = 0; var2 < this.worldServers.length; ++var2) {
			WorldServer var3 = this.worldServers[var2];

			if (var3 != null) {
				if (var3.getWorldInfo().isHardcoreModeEnabled()) {
					var3.difficultySetting = 3;
					var3.setAllowedSpawnTypes(true, true);
				} else if (this.isSinglePlayer()) {
					var3.difficultySetting = par1;
					var3.setAllowedSpawnTypes(var3.difficultySetting > 0, true);
				} else {
					var3.difficultySetting = par1;
					var3.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
				}
			}
		}
	}

	protected boolean allowSpawnMonsters() {
		return true;
	}

	/**
	 * Gets whether this is a demo or not.
	 */
	public boolean isDemo() {
		return false;
	}

	/**
	 * Sets whether this is a demo or not.
	 */
	public void setDemo(boolean par1) {
		throw new IllegalArgumentException("variable removed");
	}

	public void canCreateBonusChest(boolean par1) {
		throw new IllegalArgumentException("variable removed");
	}

	/**
	 * WARNING : directly calls
	 * getActiveAnvilConverter().deleteWorldDirectory(theWorldServer[0].getSaveHandler().getWorldDirectoryName());
	 */
	public void deleteWorldAndStopServer() {
		this.worldIsBeingDeleted = true;

		for (int var1 = 0; var1 < this.worldServers.length; ++var1) {
			WorldServer var2 = this.worldServers[var1];

			if (var2 != null) {
				var2.flush();
			}
		}
		
		String dir = this.worldServers[0].getSaveHandler().getWorldDirectoryName();
		SYS.VFS.deleteFiles(dir);
		String[] worldsTxt = SYS.VFS.getFile("worlds.txt").getAllLines();
		if(worldsTxt != null) {
			LinkedList<String> newWorlds = new LinkedList();
			for(String str : worldsTxt) {
				if(!str.equalsIgnoreCase(dir)) {
					newWorlds.add(str);
				}
			}
			SYS.VFS.getFile("worlds.txt").setAllChars(String.join("\n", newWorlds));
		}
		
		this.initiateShutdown();
	}

	public String getTexturePack() {
		return null;
	}

	public void setTexturePack(String par1Str) {
		throw new IllegalArgumentException("variable removed");
	}

	/**
	 * This is checked to be 16 upon receiving the packet, otherwise the packet is
	 * ignored.
	 */
	public int textureSize() {
		return 16;
	}

	public abstract boolean isDedicatedServer();

	public boolean isServerInOnlineMode() {
		return false;
	}

	public void setOnlineMode(boolean par1) {
		throw new IllegalArgumentException("variable removed");
	}

	public boolean getCanSpawnAnimals() {
		return this.canSpawnAnimals;
	}

	public void setCanSpawnAnimals(boolean par1) {
		this.canSpawnAnimals = par1;
	}

	public boolean getCanSpawnNPCs() {
		return this.canSpawnNPCs;
	}

	public void setCanSpawnNPCs(boolean par1) {
		this.canSpawnNPCs = par1;
	}

	public boolean isPVPEnabled() {
		return this.pvpEnabled;
	}

	public void setAllowPvp(boolean par1) {
		this.pvpEnabled = par1;
	}

	public boolean isFlightAllowed() {
		return this.allowFlight;
	}

	public void setAllowFlight(boolean par1) {
		this.allowFlight = par1;
	}

	/**
	 * Return whether command blocks are enabled.
	 */
	public abstract boolean isCommandBlockEnabled();

	public String getMOTD() {
		return this.motd;
	}

	public void setMOTD(String par1Str) {
		this.motd = par1Str;
	}

	public int getBuildLimit() {
		return 256;
	}

	public void setBuildLimit(int par1) {
		throw new IllegalArgumentException("variable removed");
	}

	public boolean isServerStopped() {
		return this.serverStopped;
	}

	public ServerConfigurationManager getConfigurationManager() {
		return this.serverConfigManager;
	}

	public void setConfigurationManager(ServerConfigurationManager par1ServerConfigurationManager) {
		this.serverConfigManager = par1ServerConfigurationManager;
	}

	/**
	 * Sets the game type for all worlds.
	 */
	public void setGameType(EnumGameType par1EnumGameType) {
		for (int var2 = 0; var2 < this.worldServers.length; ++var2) {
			getServer().worldServers[var2].getWorldInfo().setGameType(par1EnumGameType);
		}
	}

	public abstract WorkerListenThread getNetworkThread();

	public boolean getGuiEnabled() {
		return false;
	}

	/**
	 * On dedicated does nothing. On integrated, sets commandsAllowedForAll,
	 * gameType and allows external connections.
	 */
	public abstract String shareToLAN(EnumGameType var1, boolean var2);

	public int getTickCounter() {
		return this.tickCounter;
	}

	public void enableProfiling() {
		this.startProfiling = true;
	}

	/**
	 * Return the position for this command sender.
	 */
	public ChunkCoordinates getCommandSenderPosition() {
		return new ChunkCoordinates(0, 0, 0);
	}

	/**
	 * Return the spawn protection area's size.
	 */
	public int getSpawnProtectionSize() {
		return 0;
	}

	public boolean func_96290_a(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
		return false;
	}

	public abstract ILogAgent getLogAgent();

	public void func_104055_i(boolean par1) {
		this.field_104057_T = par1;
	}

	public boolean func_104056_am() {
		return this.field_104057_T;
	}

	/**
	 * Gets the current player count, maximum player count, and player entity list.
	 */
	public static ServerConfigurationManager getServerConfigurationManager(MinecraftServer par0MinecraftServer) {
		return par0MinecraftServer.serverConfigManager;
	}
}
