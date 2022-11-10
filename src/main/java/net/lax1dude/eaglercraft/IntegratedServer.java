package net.lax1dude.eaglercraft;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.sp.ipc.*;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NetHandler;
import net.minecraft.src.WorldSettings;

public class IntegratedServer {
	
	public static boolean isWorkerAlive() {
		return !(statusState < 0 || !EaglerAdapter.isIntegratedServerAlive());
	}
	
	public static void killWorker() {
		openConnections.clear();
		exceptions.clear();
		statusState = IntegratedState.WORLD_WORKER_NOT_RUNNING;
		EaglerAdapter.terminateIntegratedServer();
	}

	private static String[] loadLocale = null;
	private static String[] loadStats = null;
	private static boolean isPaused = false;
	private static List<String> integratedServerTPS = new LinkedList();
	
	public static final int preferredRelayVersion = 1;
	
	public static final RelayManager relayManager = new RelayManager();
	
	public static List<String> getTPS() {
		return integratedServerTPS;
	}
	
	public static void clearTPS() { 
		integratedServerTPS.clear();
	}
	
	public static void begin() {
		if(!isWorkerAlive()) {
			begin(EaglerAdapter.fileContentsLines("lang/en_US.lang"), EaglerAdapter.fileContentsLines("achievement/map.txt"));
		}
	}
	
	public static void begin(String[] locale, String[] stats) {
		logException = true;
		if(!isWorkerAlive()) {
			openConnections.clear();
			exceptions.clear();
			statusState = IntegratedState.WORLD_WORKER_BOOTING;
			isPaused = false;
			loadLocale = locale;
			loadStats = stats;
			clearTPS();
			EaglerAdapter.beginLoadingIntegratedServer();
		}
	}

	public static boolean isReady() {
		return statusState == IntegratedState.WORLD_NONE;
	}

	public static boolean isWorldNotLoaded() {
		return statusState == IntegratedState.WORLD_NONE || statusState == IntegratedState.WORLD_WORKER_NOT_RUNNING ||
				statusState == IntegratedState.WORLD_WORKER_BOOTING;
	}
	
	public static boolean isWorldRunning() {
		return statusState == IntegratedState.WORLD_LOADED || statusState == IntegratedState.WORLD_PAUSED ||
				statusState == IntegratedState.WORLD_LOADING || statusState == IntegratedState.WORLD_SAVING;
	}
	
	public static boolean isWorldReady() {
		return statusState == IntegratedState.WORLD_LOADED || statusState == IntegratedState.WORLD_LOADING;
	}
	
	private static void ensureReady() {
		if(!isReady()) {
			String msg = "Server is in state " + statusState + " '" + IntegratedState.getStateName(statusState) + "' which is not the 'WORLD_NONE' state for the requested IPC operation";
			throw new IllegalStateException(msg);
		}
	}
	
	private static void ensureWorldReady() {
		if(!isWorldReady()) {
			String msg = "Server is in state " + statusState + " '" + IntegratedState.getStateName(statusState) + "' which is not the 'WORLD_LOADED' state for the requested IPC operation";
			throw new IllegalStateException(msg);
		}
	}
	
	public static boolean isAlive() {
		return isWorkerAlive();
	}
	
	public static void loadWorld(String name, int difficulty) {
		loadWorld(name, difficulty, null);
	}
	
	public static void loadWorld(String name, int difficulty, WorldSettings gen) {
		ensureReady();
		clearTPS();
		statusState = IntegratedState.WORLD_LOADING;
		isPaused = false;
		
		if(gen != null) {
			sendIPCPacket(new IPCPacket02InitWorld(name, gen.getIPCGamemode(), gen.getTerrainType().getWorldTypeID(),
			gen.func_82749_j(), gen.getSeed(), gen.areCommandsAllowed(), gen.isMapFeaturesEnabled(), gen.isBonusChestEnabled()));
		}
		
		sendIPCPacket(new IPCPacket00StartServer(name, EaglerProfile.username, difficulty));
	}
	
	public static void unloadWorld() {
		if(isWorldRunning()) {
			statusState = IntegratedState.WORLD_UNLOADING;
			sendIPCPacket(new IPCPacket01StopServer());
		}
		IntegratedServerLAN.closeLAN();
	}
	
	public static void autoSave() {
		if(!isPaused) {
			statusState = IntegratedState.WORLD_SAVING;
			sendIPCPacket(new IPCPacket0BPause(false));
		}
	}
	
	public static void setPaused(boolean pause) {
		if(statusState != IntegratedState.WORLD_LOADED && statusState != IntegratedState.WORLD_PAUSED) {
			return;
		}
		if(isPaused != pause) {
			if(pause) {
				statusState = IntegratedState.WORLD_PAUSED;
			}else {
				statusState = IntegratedState.WORLD_LOADED;
			}
			sendIPCPacket(new IPCPacket0BPause(pause));
			isPaused = pause;
		}
		
	}
	
	public static void requestWorldList() {
		ensureReady();
		statusState = IntegratedState.WORLD_LISTING;
		worlds.clear();
		sendIPCPacket(new IPCPacket0EListWorlds());
	}
	
	public static List<NBTTagCompound> getWorldList() {
		return statusState == IntegratedState.WORLD_LISTING ? null : worlds;
	}
	
	public static NBTTagCompound getWorld(String folderName) {
		for(NBTTagCompound nbt : worlds) {
			if(folderName.equals(nbt.getString("folderName"))) {
				return nbt;
			}
		}
		return null;
	}
	
	public static void deleteWorld(String name) {
		ensureReady();
		statusState = IntegratedState.WORLD_DELETING;
		sendIPCPacket(new IPCPacket03DeleteWorld(name));
	}
	
	public static void setWorldName(String name, String displayName) {
		ensureReady();
		sendIPCPacket(new IPCPacket06RenameWorldNBT(name, displayName));
	}

	public static void copyMoveWorld(String oldName, String newName, String newDisplayName, boolean copyFilesNotRename) {
		ensureReady();
		statusState = copyFilesNotRename ? IntegratedState.WORLD_DUPLICATING : IntegratedState.WORLD_RENAMING;
		sendIPCPacket(new IPCPacket04RenameWorld(oldName, newName, newDisplayName, copyFilesNotRename));
	}

	private static int statusState = IntegratedState.WORLD_WORKER_NOT_RUNNING;
	private static String worldStatusString = "";
	private static float worldStatusProgress = 0.0f;

	private static final LinkedList<IPCPacket15ThrowException> exceptions = new LinkedList();
	
	public static final LinkedList<NBTTagCompound> worlds = new LinkedList();
	
	public static int statusState() {
		return statusState;
	}
	
	public static String worldStatusString() {
		return worldStatusString;
	}
	
	public static float worldStatusProgress() {
		return worldStatusProgress;
	}
	
	public static IPCPacket15ThrowException worldStatusError() {
		return exceptions.size() > 0 ? exceptions.remove(0) : null;
	}
	
	public static IPCPacket15ThrowException[] worldStatusErrors() {
		if(exceptions.size() <= 0) {
			return null;
		}
		IPCPacket15ThrowException[] t = new IPCPacket15ThrowException[exceptions.size()];
		for(int i = 0; i < t.length; ++i) {
			t[i] = exceptions.get(i);
		}
		exceptions.clear();
		return t;
	}
	
	private static boolean logException = false;
	
	public static void enableExceptionLog(boolean f) {
		logException = f;
	}
	
	private static boolean callFailed = false;
	
	public static boolean didLastCallFail() {
		boolean c = callFailed;
		callFailed = false;
		return c;
	}
	
	public static void importWorld(String name, byte[] data, int format) {
		ensureReady();
		statusState = IntegratedState.WORLD_IMPORTING;
		sendIPCPacket(new IPCPacket07ImportWorld(name, data, (byte)format));
	}
	
	public static void exportWorld(String name, int format) {
		ensureReady();
		statusState = IntegratedState.WORLD_EXPORTING;
		if(format == IPCPacket05RequestData.REQUEST_LEVEL_EAG) {
			name = name + (new String(new char[] { (char)253, (char)233, (char)233 })) + EaglerProfile.username;
		}
		sendIPCPacket(new IPCPacket05RequestData(name, (byte)format));
	}
	
	private static byte[] exportResponse = null;

	public static byte[] getExportResponse() {
		byte[] dat = exportResponse;
		exportResponse = null;
		return dat;
	}
	
	public static void processICP() {
		
		if(!EaglerAdapter.isIntegratedServerAlive()) {
			if(IntegratedServerLAN.isLANOpen()) {
				IntegratedServerLAN.closeLAN();
			}
			return;
		}
		
		PKT pktBytes;
		while((pktBytes = EaglerAdapter.recieveFromIntegratedServer("IPC")) != null) {
			
			IPCPacketBase packet;
			try {
				packet = IPCPacketManager.IPCDeserialize(pktBytes.data);
			}catch(IOException e) {
				System.err.print("Failed to deserialize IPC packet: ");
				e.printStackTrace();
				continue;
			}
			
			int id = packet.id();
			
			try {
				switch(id) {
					case IPCPacketFFProcessKeepAlive.ID: {
						IPCPacketFFProcessKeepAlive pkt = (IPCPacketFFProcessKeepAlive)packet;
						IntegratedState.isACKValidInState(pkt.ack, statusState);
						switch(pkt.ack) {
							case 0xFF:
								System.out.println("Integrated server signaled a successful boot");
								sendIPCPacket(new IPCPacket14StringList(IPCPacket14StringList.LOCALE, loadLocale));
								sendIPCPacket(new IPCPacket14StringList(IPCPacket14StringList.STAT_GUID, loadStats));
								loadLocale = loadStats = null;
								statusState = IntegratedState.WORLD_NONE;
								break;
							case IPCPacket00StartServer.ID:
								statusState = IntegratedState.WORLD_LOADED;
								isPaused = false;
								break;
							case IPCPacket0BPause.ID:
								statusState = isPaused ? IntegratedState.WORLD_PAUSED : IntegratedState.WORLD_LOADED;
								break;
							case IPCPacketFFProcessKeepAlive.FAILURE:
								System.err.println("Server signaled 'FAILURE' response in state '" + IntegratedState.getStateName(statusState) + "'");
								statusState = IntegratedState.WORLD_NONE;
								callFailed = true;
								break;
							case IPCPacket01StopServer.ID:
								statusState = IntegratedState.WORLD_NONE;
								break;
							case IPCPacket03DeleteWorld.ID:
							case IPCPacket04RenameWorld.ID:
							case IPCPacket07ImportWorld.ID:
							case IPCPacket12FileWrite.ID:
							case IPCPacket13FileCopyMove.ID:
							case IPCPacket18ClearPlayers.ID:
								statusState = IntegratedState.WORLD_NONE;
								break;
							default:
								System.err.println("IPC acknowledge packet type 0x" + Integer.toHexString(id) + " class '" + packet.getClass().getSimpleName() + "' was not handled");
								break;
						}
						break;
					}
					case IPCPacket09RequestResponse.ID: {
						IPCPacket09RequestResponse pkt = (IPCPacket09RequestResponse)packet;
						if(statusState == IntegratedState.WORLD_EXPORTING) {
							statusState = IntegratedState.WORLD_NONE;
							exportResponse = pkt.response;
						}else {
							System.err.println("IPCPacket09RequestResponse was recieved but statusState was '" + IntegratedState.getStateName(statusState) + "' instead of 'WORLD_EXPORTING'");
						}
						break;
					}
					case IPCPacket0DProgressUpdate.ID: {
						IPCPacket0DProgressUpdate pkt = (IPCPacket0DProgressUpdate)packet;
						worldStatusString = pkt.updateMessage;
						worldStatusProgress = pkt.updateProgress;
						if(logException) {
							System.out.println("IntegratedServer: task \"" + pkt.updateMessage + "\"" + (pkt.updateProgress > 0.0f ? " is " + ((int)(pkt.updateProgress * 100.0f)) + "% complete" : ""));
						}
						break;
					}
					case IPCPacket14StringList.ID: {
						IPCPacket14StringList pkt = (IPCPacket14StringList)packet;
						
						if(pkt.opCode == IPCPacket14StringList.SERVER_TPS) {
							integratedServerTPS.clear();
							integratedServerTPS.addAll(pkt.stringList);
						}
						
						// file path list for file browser
						
						break;
					}
					case IPCPacket15ThrowException.ID: {
						exceptions.add((IPCPacket15ThrowException)packet);
						if(logException) {
							((IPCPacket15ThrowException)packet).log();
						}
						if(exceptions.size() > 64) {
							exceptions.remove(0);
						}
						break;
					}
					case IPCPacket16NBTList.ID: {
						IPCPacket16NBTList pkt = (IPCPacket16NBTList)packet;
						if(pkt.opCode == IPCPacket16NBTList.WORLD_LIST && statusState == IntegratedState.WORLD_LISTING) {
							statusState = IntegratedState.WORLD_NONE;
							worlds.clear();
							worlds.addAll(pkt.nbtTagList);
						}else {
							System.err.println("IPC packet type 0x" + Integer.toHexString(id) + " class '" + packet.getClass().getSimpleName() + "' contained invalid opCode " + pkt.opCode + " in state " + statusState + " '" + IntegratedState.getStateName(statusState) + "'");
						}
						break;
					}
					case IPCPacket0CPlayerChannel.ID: {
						IPCPacket0CPlayerChannel pkt = (IPCPacket0CPlayerChannel)packet;
						WorkerNetworkManager newConnection = openConnections.get(pkt.channel);
						if(newConnection == null) {
							return;
						}
						System.out.println("[Client][INIT][CLOSE][" + pkt.channel + "]");
						newConnection.closeConnections();
						openConnections.remove(pkt.channel);
						EaglerAdapter.disableChannel("NET|" + pkt.channel);
						break;
					}
					default:
						System.err.println("IPC packet type 0x" + Integer.toHexString(id) + " class '" + packet.getClass().getSimpleName() + "' was not handled");
						break;
				}
			}catch(Throwable t) {
				System.err.println("Failed to process IPC packet type 0x" + Integer.toHexString(id) + " class '" + packet.getClass().getSimpleName() + "'");
				t.printStackTrace();
			}
		}
		
		IntegratedServerLAN.updateLANServer();
	}
	
	public static void sendIPCPacket(IPCPacketBase pkt) {
		try {
			byte[] serialized = IPCPacketManager.IPCSerialize(pkt);
			EaglerAdapter.sendToIntegratedServer("IPC", serialized);
		} catch (IOException e) {
			System.err.println("Could not serialize IPC packet 0x" + Integer.toHexString(pkt.id()) + " class '" + pkt.getClass().getSimpleName() + "'");
			e.printStackTrace();
		}
	}
	
	private static final HashMap<String, WorkerNetworkManager> openConnections = new HashMap();
	
	public static final boolean doesChannelExist(String channel) {
		return openConnections.containsKey(channel);
	}
	
	public static final WorkerNetworkManager openConnection(String channel, NetHandler netHandler) {
		WorkerNetworkManager newConnection = openConnections.get(channel);
		if(newConnection != null) {
			return newConnection;
		}
		System.out.println("[Client][INIT][OPEN][" + channel + "]");
		EaglerAdapter.enableChannel("NET|" + channel);
		sendIPCPacket(new IPCPacket0CPlayerChannel(channel, true));
		newConnection = new WorkerNetworkManager(channel, netHandler);
		openConnections.put(channel, newConnection);
		return newConnection;
	}
	
	public static final void closeChannel(String channel) {
		WorkerNetworkManager newConnection = openConnections.get(channel);
		if(newConnection == null) {
			return;
		}
		System.out.println("[Client][INIT][CLOSE][" + channel + "]");
		newConnection.closeConnections();
		openConnections.remove(channel);
		EaglerAdapter.disableChannel("NET|" + channel);
		sendIPCPacket(new IPCPacket0CPlayerChannel(channel, false));
	}

	public static void configureLAN(EnumGameType enumGameType, boolean allowCommands) {
		sendIPCPacket(new IPCPacket17ConfigureLAN(enumGameType.getID(), allowCommands, IntegratedServerLAN.currentICEServers));
	}

	public static void clearPlayerData(String worldName) {
		ensureReady();
		statusState = IntegratedState.WORLD_CLEAR_PLAYERS;
		sendIPCPacket(new IPCPacket18ClearPlayers(worldName));
	}
	
}