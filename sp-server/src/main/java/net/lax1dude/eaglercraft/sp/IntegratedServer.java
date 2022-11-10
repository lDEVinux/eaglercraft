package net.lax1dude.eaglercraft.sp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.sp.ipc.*;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AchievementMap;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ILogAgent;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public class IntegratedServer {
	
	private static final LinkedList<PKT> messageQueue = new LinkedList();
	
	protected static class PKT {
		protected final String channel;
		protected final byte[] data;
		protected PKT(String channel, byte[] data) {
			this.channel = channel;
			this.data = data;
		}
	}
	
	private static EAGMinecraftServer currentProcess = null;
	private static WorldSettings newWorldSettings = null;
	
	public static EAGMinecraftServer getServer() {
		return currentProcess;
	}
	
	public static final ILogAgent logger = new EAGLogAgent();
	
	@JSFunctor
	private static interface WorkerBinaryPacketHandler extends JSObject {
		public void onMessage(String channel, ArrayBuffer buf);
	}
	
	private static class WorkerBinaryPacketHandlerImpl implements WorkerBinaryPacketHandler {
		
		public void onMessage(String channel, ArrayBuffer buf) {
			if(channel == null) {
				System.err.println("Recieved IPC packet with null channel");
				return;
			}
			
			if(buf == null) {
				System.err.println("Recieved IPC packet with null buffer");
				return;
			}
			
			Uint8Array a = Uint8Array.create(buf);
			byte[] pkt = new byte[a.getLength()];
			for(int i = 0; i < pkt.length; ++i) {
				pkt[i] = (byte) a.get(i);
			}
			
			synchronized(messageQueue) {
				messageQueue.add(new PKT(channel, pkt));
			}
		}
		
	}
	
	private static void tryStopServer() {
		if(currentProcess != null) {
			try {
				currentProcess.stopServer();
			}catch(Throwable t) {
				System.err.println("Failed to stop server!");
				throwExceptionToClient("Failed to stop server!", t);
			}
			currentProcess = null;
		}
	}
	
	public static void updateStatusString(String stat, float prog) {
		sendIPCPacket(new IPCPacket0DProgressUpdate(stat, prog));
	}
	
	private static boolean isServerStopped() {
		return currentProcess == null || !currentProcess.isServerRunning();
	}
	
	public static void throwExceptionToClient(String msg, Throwable t) {
		String str = t.toString();
		System.err.println("Exception was raised to client: " + str);
		t.printStackTrace();
		List<String> arr = new LinkedList();
		for(StackTraceElement e : t.getStackTrace()) {
			String st = e.toString();
			arr.add(st);
		}
		sendIPCPacket(new IPCPacket15ThrowException(str, arr));
	}
	
	public static void sendTaskFailed() {
		sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacketFFProcessKeepAlive.FAILURE));
	}
	
	private static void processAsyncMessageQueue() {
		ArrayList<PKT> cur;
		synchronized(messageQueue) {
			if(messageQueue.size() <= 0) {
				return;
			}
			cur = new ArrayList<PKT>(messageQueue);
			messageQueue.clear();
		}
		Iterator<PKT> itr = cur.iterator();
		while(itr.hasNext()) {
			PKT msg = itr.next();
			if(msg.channel.equals("IPC")) {
				
				IPCPacketBase packet;
				try {
					packet = IPCPacketManager.IPCDeserialize(msg.data);
				}catch(IOException e) {
					System.err.print("Failed to deserialize IPC packet: ");
					e.printStackTrace();
					continue;
				}
				
				int id = packet.id();
				
				try {
					switch(id) {
					case IPCPacket00StartServer.ID: {
							IPCPacket00StartServer pkt = (IPCPacket00StartServer)packet;
							
							if(!isServerStopped()) {
								currentProcess.stopServer();
							}
							
							currentProcess = new EAGMinecraftServer(pkt.worldName, pkt.ownerName, newWorldSettings);
							currentProcess.setBaseServerProperties(pkt.initialDifficulty, newWorldSettings == null ? EnumGameType.SURVIVAL : newWorldSettings.getGameType());
							currentProcess.startServer();
							
							String[] worlds = SYS.VFS.getFile("worlds.txt").getAllLines();
							if(worlds == null || (worlds.length == 1 && worlds[0].trim().length() <= 0)) {
								worlds = null;
							}
							if(worlds == null) {
								SYS.VFS.getFile("worlds.txt").setAllChars(pkt.worldName);
							}else {
								boolean found = false;
								for(String s : worlds) {
									if(s.equals(pkt.worldName)) {
										found = true;
										break;
									}
								}
								if(!found) {
									String[] s = new String[worlds.length + 1];
									s[0] = pkt.worldName;
									System.arraycopy(worlds, 0, s, 1, worlds.length);
									SYS.VFS.getFile("worlds.txt").setAllChars(String.join("\n", s));
								}
							}
							
							sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket00StartServer.ID));
						}
						break;
					case IPCPacket01StopServer.ID: {
							if(!isServerStopped()) {
								try {
									currentProcess.stopServer();
									currentProcess = null;
								}catch(Throwable t) {
									throwExceptionToClient("Failed to stop server!", t);
								}
							}else {
								System.err.println("Client tried to stop server while it wasn't running for some reason");
							}
							sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket01StopServer.ID));
						}
						break;
					case IPCPacket02InitWorld.ID: {
							tryStopServer();
							IPCPacket02InitWorld pkt = (IPCPacket02InitWorld)packet;
							newWorldSettings = new WorldSettings(pkt.seed, pkt.gamemode == 1 ? EnumGameType.CREATIVE : EnumGameType.SURVIVAL, pkt.structures,
									pkt.gamemode == 2, pkt.worldType == 1 ? WorldType.FLAT : (pkt.worldType == 2 ? WorldType.LARGE_BIOMES : WorldType.DEFAULT_1_1));
							newWorldSettings.func_82750_a(pkt.worldArgs);
							if(pkt.bonusChest) {
								newWorldSettings.enableBonusChest();
							}
							if(pkt.cheats) {
								newWorldSettings.enableCheats();
							}
						}
						break;
					case IPCPacket03DeleteWorld.ID: {
							tryStopServer();
							IPCPacket03DeleteWorld pkt = (IPCPacket03DeleteWorld)packet;
							if(SYS.VFS.deleteFiles("worlds/" + pkt.worldName + "/") <= 0) {
								throwExceptionToClient("Failed to delete world!", new RuntimeException("VFS did not delete directory 'worlds/" + pkt.worldName + "' correctly"));
								sendTaskFailed();
								break;
							}
							String[] worldsTxt = SYS.VFS.getFile("worlds.txt").getAllLines();
							if(worldsTxt != null) {
								LinkedList<String> newWorlds = new LinkedList();
								for(String str : worldsTxt) {
									if(!str.equalsIgnoreCase(pkt.worldName)) {
										newWorlds.add(str);
									}
								}
								SYS.VFS.getFile("worlds.txt").setAllChars(String.join("\n", newWorlds));
							}
							sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket03DeleteWorld.ID));
						}
						break;
					case IPCPacket04RenameWorld.ID: {
							tryStopServer();
							IPCPacket04RenameWorld pkt = (IPCPacket04RenameWorld)packet;
							if(SYS.VFS.renameFiles("worlds/" + pkt.worldOldName + "/", "worlds/" + pkt.worldNewName + "/", pkt.copy) <= 0) {
								throwExceptionToClient("Failed to copy/rename server!", new RuntimeException("VFS did not copy/rename directory 'worlds/" + pkt.worldOldName + "' correctly"));
								sendTaskFailed();
								break;
							}else {
								String[] worldsTxt = SYS.VFS.getFile("worlds.txt").getAllLines();
								LinkedList<String> newWorlds = new LinkedList();
								if(worldsTxt != null) {
									for(String str : worldsTxt) {
										if(pkt.copy || !str.equalsIgnoreCase(pkt.worldOldName)) {
											newWorlds.add(str);
										}
									}
								}
								newWorlds.add(pkt.worldNewName);
								SYS.VFS.getFile("worlds.txt").setAllChars(String.join("\n", newWorlds));
								VFile worldDat = new VFile("worlds", pkt.worldNewName, "level.dat");
								if(worldDat.canRead()) {
									NBTTagCompound worldDatNBT = CompressedStreamTools.decompress(worldDat.getAllBytes());
									worldDatNBT.getCompoundTag("Data").setString("LevelName", pkt.displayName);
									worldDat.setAllBytes(CompressedStreamTools.compress(worldDatNBT));
								}else {
									throwExceptionToClient("Failed to copy/rename world!", new RuntimeException("Failed to change level.dat world '" + pkt.worldNewName + "' display name to '" + pkt.displayName + "' because level.dat was missing"));
									sendTaskFailed();
									break;
								}
							}
							sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket04RenameWorld.ID));
						}
						break;
					case IPCPacket05RequestData.ID: {
							IPCPacket05RequestData pkt = (IPCPacket05RequestData)packet;
							if(pkt.request == IPCPacket05RequestData.REQUEST_LEVEL_EAG) {String realWorldName = pkt.worldName;
								String worldOwner = "UNKNOWN";
								String splitter = new String(new char[] { (char)253, (char)233, (char)233 });
								if(realWorldName.contains(splitter)) {
									int i = realWorldName.lastIndexOf(splitter);
									worldOwner = realWorldName.substring(i + 3);
									realWorldName = realWorldName.substring(0, i);
								}
								try {
									
									final int[] bytesWritten = new int[1];
									final int[] lastUpdate = new int[1];
									String pfx = "worlds/" + realWorldName + "/";
									EPK2Compiler c = new EPK2Compiler(realWorldName, worldOwner, "epk/world152");
									SYS.VFS.iterateFiles(pfx, false, (i) -> {
										byte[] b = i.getAllBytes();
										c.append(i.path.substring(pfx.length()), b);
										bytesWritten[0] += b.length;
										if (bytesWritten[0] - lastUpdate[0] > 10000) {
											lastUpdate[0] = bytesWritten[0];
											updateStatusString("selectWorld.progress.exporting." + pkt.request, bytesWritten[0]);
										}
									});
									sendIPCPacket(new IPCPacket09RequestResponse(c.complete()));
								} catch (Throwable t) {
									throwExceptionToClient("Failed to export world '" + realWorldName + "' as EPK", t);
									sendTaskFailed();
								}
							}else if(pkt.request == IPCPacket05RequestData.REQUEST_LEVEL_MCA) {
								try {
									final int[] bytesWritten = new int[1];
									final int[] lastUpdate = new int[1];
									String shortpfx = pkt.worldName + "/";
									String pfx = "worlds/" + shortpfx;
									ByteArrayOutputStream baos = new ByteArrayOutputStream();
									ZipOutputStream c = new ZipOutputStream(baos);
									c.setComment("contains backup of world '" + pkt.worldName + "'");
									Map<ChunkCoordIntPair, byte[]> regions = new HashMap<>();
									Map<ChunkCoordIntPair, byte[]> regions1 = new HashMap<>();
									Map<ChunkCoordIntPair, byte[]> regionsn1 = new HashMap<>();
									SYS.VFS.iterateFiles(pfx, false, (i) -> {
										String currPath = i.path.substring(pfx.length());
										try {
											byte[] b = i.getAllBytes();
											if(currPath.equals("level.dat")) {
												NBTTagCompound worldDatNBT = CompressedStreamTools.decompress(b);
												worldDatNBT.getCompoundTag("Data").setInteger("version", 19133);
												b = CompressedStreamTools.compress(worldDatNBT);
											}
											if (currPath.startsWith("level0/")) {
												regions.put(VFSChunkLoader.getChunkCoords(currPath.substring(7, currPath.length() - 4)), b);
											} else if (currPath.startsWith("level1/")) {
												regions1.put(VFSChunkLoader.getChunkCoords(currPath.substring(7, currPath.length() - 4)), b);
											} else if (currPath.startsWith("level-1/")) {
												regionsn1.put(VFSChunkLoader.getChunkCoords(currPath.substring(8, currPath.length() - 4)), b);
											} else {
												ZipEntry zipEntry = new ZipEntry(shortpfx + currPath);
												c.putNextEntry(zipEntry);
												c.write(b);
												c.closeEntry();
												bytesWritten[0] += b.length;
												if (bytesWritten[0] - lastUpdate[0] > 10000) {
													lastUpdate[0] = bytesWritten[0];
													updateStatusString("selectWorld.progress.exporting." + pkt.request, bytesWritten[0]);
												}
											}
										} catch (Throwable t) {
											throwExceptionToClient("Failed to export file '" + currPath + "'", t);
											sendTaskFailed();
										}
									});
									Map<String, byte[]> regionsOut = MCAConverter.convertToMCA(regions);
									for (String path : regionsOut.keySet()) {
										byte[] b = regionsOut.get(path);
										ZipEntry zipEntry = new ZipEntry(shortpfx + "region/" + path + ".mca");
										c.putNextEntry(zipEntry);
										c.write(b);
										c.closeEntry();
										bytesWritten[0] += b.length;
										if (bytesWritten[0] - lastUpdate[0] > 10000) {
											lastUpdate[0] = bytesWritten[0];
											updateStatusString("selectWorld.progress.exporting." + pkt.request, bytesWritten[0]);
										}
									}
									Map<String, byte[]> regions1Out = MCAConverter.convertToMCA(regions1);
									for (String path : regions1Out.keySet()) {
										byte[] b = regions1Out.get(path);
										ZipEntry zipEntry = new ZipEntry(shortpfx + "DIM1/region/" + path + ".mca");
										c.putNextEntry(zipEntry);
										c.write(b);
										c.closeEntry();
										bytesWritten[0] += b.length;
										if (bytesWritten[0] - lastUpdate[0] > 10000) {
											lastUpdate[0] = bytesWritten[0];
											updateStatusString("selectWorld.progress.exporting." + pkt.request, bytesWritten[0]);
										}
									}
									Map<String, byte[]> regionsn1Out = MCAConverter.convertToMCA(regionsn1);
									for (String path : regionsn1Out.keySet()) {
										byte[] b = regionsn1Out.get(path);
										ZipEntry zipEntry = new ZipEntry(shortpfx + "DIM-1/region/" + path + ".mca");
										c.putNextEntry(zipEntry);
										c.write(b);
										c.closeEntry();
										bytesWritten[0] += b.length;
										if (bytesWritten[0] - lastUpdate[0] > 10000) {
											lastUpdate[0] = bytesWritten[0];
											updateStatusString("selectWorld.progress.exporting." + pkt.request, bytesWritten[0]);
										}
									}
									c.close();
									sendIPCPacket(new IPCPacket09RequestResponse(baos.toByteArray()));
								} catch (Throwable t) {
									throwExceptionToClient("Failed to export world '" + pkt.worldName + "' as MCA", t);
									sendTaskFailed();
								}
							}else {
								System.err.println("Unknown IPCPacket05RequestData type '" + pkt.request + "'");
								sendTaskFailed();
							}
						}
						break;
					case IPCPacket06RenameWorldNBT.ID: {
							IPCPacket06RenameWorldNBT pkt = (IPCPacket06RenameWorldNBT)packet;
							if(isServerStopped()) {
								VFile worldDat = new VFile("worlds", pkt.worldName, "level.dat");
								if(worldDat.canRead()) {
									NBTTagCompound worldDatNBT = CompressedStreamTools.decompress(worldDat.getAllBytes());
									worldDatNBT.getCompoundTag("Data").setString("LevelName", pkt.displayName);
									worldDat.setAllBytes(CompressedStreamTools.compress(worldDatNBT));
								}else {
									throwExceptionToClient("Failed to rename world!", new RuntimeException("Failed to change level.dat world '" + pkt.worldName + "' display name to '" + pkt.displayName + "' because level.dat was missing"));
								}
							}else {
								System.err.println("Client tried to rename a world '" + pkt.worldName + "' to have name '" + pkt.displayName + "' while the server is running");
								sendTaskFailed();
							}
						}
						break;
					case IPCPacket07ImportWorld.ID: {
							IPCPacket07ImportWorld pkt = (IPCPacket07ImportWorld)packet;
							if(isServerStopped()) {
								if(pkt.worldFormat == IPCPacket07ImportWorld.WORLD_FORMAT_EAG) {
									String folder = VFSSaveHandler.worldNameToFolderName(pkt.worldName);
									try {
										VFile dir = new VFile("worlds", folder);
										EPKDecompiler dc = new EPKDecompiler(pkt.worldData);
										EPKDecompiler.FileEntry f = null;
										int lastProgUpdate = 0;
										int prog = 0;
										boolean hasReadType = dc.isOld();
										while((f = dc.readFile()) != null) {
											byte[] b = f.data;
											if(!hasReadType) {
												if(f.type.equals("HEAD") && f.name.equals("file-type") && EPKDecompiler.readASCII(f.data).equals("epk/world152")) {
													hasReadType = true;
													continue;
												}else {
													throw new IOException("file does not contain a singleplayer 1.5.2 world!");
												}
											}
											if(f.type.equals("FILE")) {
												if(f.name.equals("level.dat")) {
													NBTTagCompound worldDatNBT = CompressedStreamTools.decompress(b);
													worldDatNBT.getCompoundTag("Data").setString("LevelName", pkt.worldName);
													worldDatNBT.getCompoundTag("Data").setLong("LastPlayed", System.currentTimeMillis());
													b = CompressedStreamTools.compress(worldDatNBT);
												}
												VFile ff = new VFile(dir, f.name);
												ff.setAllBytes(b);
												prog += b.length;
												if(prog - lastProgUpdate > 10000) {
													lastProgUpdate = prog;
													updateStatusString("selectWorld.progress.importing." + pkt.worldFormat, prog);
												}
											}
										}
										String[] worldsTxt = SYS.VFS.getFile("worlds.txt").getAllLines();
										if(worldsTxt == null || worldsTxt.length <= 0) {
											worldsTxt = new String[] { folder };
										}else {
											String[] tmp = worldsTxt;
											worldsTxt = new String[worldsTxt.length + 1];
											System.arraycopy(tmp, 0, worldsTxt, 0, tmp.length);
											worldsTxt[worldsTxt.length - 1] = folder;
										}
										SYS.VFS.getFile("worlds.txt").setAllChars(String.join("\n", worldsTxt));
										sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket07ImportWorld.ID));
									}catch(Throwable t) {
										SYS.VFS.deleteFiles("worlds/" + folder + "/");
										throwExceptionToClient("Failed to import world '" + pkt.worldName + "' as EPK", t);
										sendTaskFailed();
									}
								}else if(pkt.worldFormat == IPCPacket07ImportWorld.WORLD_FORMAT_MCA) {
									String folder = VFSSaveHandler.worldNameToFolderName(pkt.worldName);
									try {
										VFile dir = new VFile("worlds", folder);
										ZipInputStream folderNames = new ZipInputStream(new ByteArrayInputStream(pkt.worldData));
										ZipEntry folderNameFile = null;
										List<char[]> fileNames = new ArrayList<>();
										while((folderNameFile = folderNames.getNextEntry()) != null) {
											if (folderNameFile.getName().contains("__MACOSX/")) continue;
											if (folderNameFile.isDirectory()) continue;
											String lowerName = folderNameFile.getName().toLowerCase();
											if (!(lowerName.endsWith(".dat") || lowerName.endsWith(".mca") || lowerName.endsWith(".mcr"))) continue;
											fileNames.add(folderNameFile.getName().toCharArray());
										}
										final int[] i = new int[] { 0 };
										while(fileNames.get(0).length > i[0] && fileNames.stream().allMatch(w -> w[i[0]] == fileNames.get(0)[i[0]])) i[0]++;
										int folderPrefixOffset = i[0];
										ZipInputStream dc = new ZipInputStream(new ByteArrayInputStream(pkt.worldData));
										ZipEntry f = null;
										int lastProgUpdate = 0;
										int prog = 0;
										byte[] bb = new byte[16000];
										while ((f = dc.getNextEntry()) != null) {
											if (f.getName().contains("__MACOSX/")) continue;
											if (f.isDirectory()) continue;
											String lowerName = f.getName().toLowerCase();
											if (!(lowerName.endsWith(".dat") || lowerName.endsWith(".mca") || lowerName.endsWith(".mcr"))) continue;
											ByteArrayOutputStream baos = new ByteArrayOutputStream();
											int len;
											while ((len = dc.read(bb)) != -1) {
												baos.write(bb, 0, len);
											}
											baos.close();
											byte[] b = baos.toByteArray();
											String fileName = f.getName().substring(folderPrefixOffset);
											if (fileName.equals("level.dat")) {
												NBTTagCompound worldDatNBT = CompressedStreamTools.decompress(b);
												worldDatNBT.getCompoundTag("Data").setString("LevelName", pkt.worldName);
												worldDatNBT.getCompoundTag("Data").setLong("LastPlayed", System.currentTimeMillis());
												b = CompressedStreamTools.compress(worldDatNBT);
											}
											if (fileName.endsWith(".mcr") || fileName.endsWith(".mca")) {
												MCAConverter.convertFromMCA(dir, b, fileName);
											} else {
												VFile ff = new VFile(dir, fileName);
												ff.setAllBytes(b);
											}
											prog += b.length;
											if (prog - lastProgUpdate > 10000) {
												lastProgUpdate = prog;
												updateStatusString("selectWorld.progress.importing." + pkt.worldFormat, prog);
											}
										}
										String[] worldsTxt = SYS.VFS.getFile("worlds.txt").getAllLines();
										if(worldsTxt == null || worldsTxt.length <= 0) {
											worldsTxt = new String[] { folder };
										}else {
											String[] tmp = worldsTxt;
											worldsTxt = new String[worldsTxt.length + 1];
											System.arraycopy(tmp, 0, worldsTxt, 0, tmp.length);
											worldsTxt[worldsTxt.length - 1] = folder;
										}
										SYS.VFS.getFile("worlds.txt").setAllChars(String.join("\n", worldsTxt));
										sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket07ImportWorld.ID));
									}catch(Throwable t) {
										SYS.VFS.deleteFiles("worlds/" + folder + "/");
										throwExceptionToClient("Failed to import world '" + pkt.worldName + "' as MCA", t);
										sendTaskFailed();
									}
								}else {
									System.err.println("Client tried to import a world in an unknown format: 0x" + Integer.toHexString(pkt.worldFormat));
									sendTaskFailed();
								}
							}else {
								System.err.println("Client tried to import a world '" + pkt.worldName + "' while the server is running");
								sendTaskFailed();
							}
						}
						break;
					case IPCPacket09RequestResponse.ID:
						
						break;
					case IPCPacket0ASetWorldDifficulty.ID: {
							IPCPacket0ASetWorldDifficulty pkt = (IPCPacket0ASetWorldDifficulty)packet;
							if(!isServerStopped()) {
								currentProcess.setDifficultyForAllWorlds(pkt.difficulty);
							}else {
								System.err.println("Client tried to set difficulty '" + pkt.difficulty + "' while server was stopped");
								sendTaskFailed();
							}
						}
						break;
					case IPCPacket0BPause.ID: {
							IPCPacket0BPause pkt = (IPCPacket0BPause)packet;
							if(!isServerStopped()) {
								if(!pkt.pause && !currentProcess.getPaused()) {
									currentProcess.saveAllWorlds(true);
								}else {
									currentProcess.setPaused(pkt.pause);
									if(pkt.pause) {
										currentProcess.saveAllWorlds(true);
									}
								}
								sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket0BPause.ID));
							}else {
								System.err.println("Client tried to " + (pkt.pause ? "pause" : "unpause") + " while server was stopped");
							}
						}
						break;
					case IPCPacket0CPlayerChannel.ID: {
							IPCPacket0CPlayerChannel pkt = (IPCPacket0CPlayerChannel)packet;
							if(!isServerStopped()) {
								if(pkt.open) {
									if(!currentProcess.getNetworkThread().openChannel(pkt.channel)) {
										System.err.println("Client tried to open a duplicate channel '" + pkt.channel + "'");
									}
								}else {
									if(!currentProcess.getNetworkThread().closeChannel(pkt.channel)) {
										System.err.println("Client tried to close a null channel '" + pkt.channel + "'");
									}
								}
							}else {
								System.err.println("Client tried to " + (pkt.open ? "open" : "close") + " channel '" + pkt.channel + "' while server was stopped");
							}
						}
						break;
					case IPCPacket0EListWorlds.ID: {
							IPCPacket0EListWorlds pkt = (IPCPacket0EListWorlds)packet;
							if(isServerStopped()) {
								String[] worlds = SYS.VFS.getFile("worlds.txt").getAllLines();
								if(worlds == null || (worlds.length == 1 && worlds[0].trim().length() <= 0)) {
									worlds = null;
								}
								if(worlds == null) {
									sendIPCPacket(new IPCPacket16NBTList(IPCPacket16NBTList.WORLD_LIST, new LinkedList<NBTTagCompound>()));
									break;
								}
								LinkedList<String> updatedList = new LinkedList();
								LinkedList<NBTTagCompound> sendListNBT = new LinkedList();
								boolean rewrite = false;
								for(String w : worlds) {
									byte[] dat = (new VFile("worlds", w, "level.dat")).getAllBytes();
									if(dat != null) {
										NBTTagCompound worldDatNBT;
										try {
											worldDatNBT = CompressedStreamTools.decompress(dat);
											worldDatNBT.setString("folderName", w);
											sendListNBT.add(worldDatNBT);
											updatedList.add(w);
											continue;
										}catch(IOException e) {
											// shit fuck
										}
										
									}
									rewrite = true;
									System.err.println("World level.dat for '" + w + "' was not found, attempting to delete 'worlds/" + w + "/*'");
									if(SYS.VFS.deleteFiles("worlds/" + w) <= 0) {
										System.err.println("No files were deleted in 'worlds/" + w + "/*', this may be corruption but '" + w + "' will still be removed from worlds.txt");
									}
								}
								if(rewrite) {
									SYS.VFS.getFile("worlds.txt").setAllChars(String.join("\n", updatedList));
								}
								sendIPCPacket(new IPCPacket16NBTList(IPCPacket16NBTList.WORLD_LIST, sendListNBT));
							}else {
								System.err.println("Client tried to list worlds while server was running");
								sendTaskFailed();
							}
						}
						break;
					case IPCPacket0FListFiles.ID:
						
						break;
					case IPCPacket10FileRead.ID:
						
						break;
					case IPCPacket12FileWrite.ID:
						
						break;
					case IPCPacket13FileCopyMove.ID:
						
						break;
					case IPCPacket14StringList.ID: {
							IPCPacket14StringList pkt = (IPCPacket14StringList)packet;
							switch(pkt.opCode) {
							case IPCPacket14StringList.LOCALE:
								StringTranslate.init(pkt.stringList);
								break;
							case IPCPacket14StringList.STAT_GUID:
								AchievementMap.init(pkt.stringList);
								AchievementList.init();
								break;
							default:
								System.err.println("Strange string list 0x" + Integer.toHexString(pkt.opCode) + " with length " + pkt.stringList.size() + " recieved");
								break;
							}
						}
						break;
					case IPCPacket17ConfigureLAN.ID: {
						IPCPacket17ConfigureLAN pkt = (IPCPacket17ConfigureLAN)packet;
						currentProcess.getConfigurationManager().configureLAN(pkt.gamemode, pkt.cheats, pkt.iceServers);
					}
					break;
					case IPCPacket18ClearPlayers.ID: {
						SYS.VFS.deleteFiles("worlds/" + ((IPCPacket18ClearPlayers)packet).worldName + "/player");
						sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket18ClearPlayers.ID));
					}
					break;
					default:
						System.err.println("IPC packet type 0x" + Integer.toHexString(id) + " class '" + packet.getClass().getSimpleName() + "' was not handled");
						sendTaskFailed();
						break;
					}
				}catch(Throwable t) {
					String str = "IPC packet 0x" + Integer.toHexString(id) + " class '" + packet.getClass().getSimpleName() + "' was not processed correctly";
					System.err.println(str);
					throwExceptionToClient(str, t);
					sendTaskFailed();
				}
				
				continue;
			}
		}
		long watchDog = System.currentTimeMillis();
		itr = cur.iterator();
		int overflow = 0;
		while(itr.hasNext()) {
			PKT msg = itr.next();
			if(!msg.channel.equals("IPC")) {
				if(System.currentTimeMillis() - watchDog > 500l) {
					++overflow;
					continue;
				}
				if(!msg.channel.startsWith("NET|") || currentProcess == null) {
					//System.err.println("Unknown ICP channel: '" + msg.channel + "' passed " + msg.data.length + " bytes");
					continue;
				}
				String u = msg.channel.substring(4);
				currentProcess.getNetworkThread().recievePacket(u, msg.data);
			}
		}
		if(overflow > 0) {
			System.err.println("Async ICP queue is overloaded, server dropped " + overflow + " player packets");
		}
	}
	
	@JSBody(params = { "ch", "dat" }, script = "postMessage({ ch: ch, dat : dat });")
	private static native void sendWorkerPacket(String channel, ArrayBuffer arr);
	
	public static void sendIPCPacket(IPCPacketBase pkt) {
		byte[] serialized;
		
		try {
			serialized = IPCPacketManager.IPCSerialize(pkt);
		} catch (IOException e) {
			System.err.println("Could not serialize IPC packet 0x" + Integer.toHexString(pkt.id()) + " class '" + pkt.getClass().getSimpleName() + "'");
			e.printStackTrace();
			return;
		}
		
		ArrayBuffer arb = ArrayBuffer.create(serialized.length);
		Uint8Array ar = Uint8Array.create(arb);
		ar.set(serialized);
		sendWorkerPacket("IPC", arb);
	}
	
	public static void sendPlayerPacket(String channel, byte[] buf) {
		//System.out.println("[Server][SEND][" + channel + "]: " + buf.length);
		ArrayBuffer arb = ArrayBuffer.create(buf.length);
		Uint8Array ar = Uint8Array.create(arb);
		ar.set(buf);
		sendWorkerPacket("NET|" + channel, arb);
	}
	
	private static boolean isRunning = false;
	
	public static void halt() {
		isRunning = false;
	}
	
	private static void mainLoop() {
		processAsyncMessageQueue();
		
		if(currentProcess != null) {
			currentProcess.mainLoop();
			if(currentProcess.isServerStopped()) {
				sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket01StopServer.ID));
				currentProcess = null;
			}
		}
	}
	
	@JSBody(params = { "wb" }, script = "onmessage = function(o) { wb(o.data.ch, o.data.dat); };")
	private static native void registerPacketHandler(WorkerBinaryPacketHandler wb);
	
	public static void main(String[] args) {
		
		registerPacketHandler(new WorkerBinaryPacketHandlerImpl());
		
		isRunning = true;
		
		sendIPCPacket(new IPCPacketFFProcessKeepAlive(0xFF));
		
		while(isRunning) {
			
			mainLoop();
			
			try {
				Thread.sleep(1l); // allow some async to occur
			}catch(InterruptedException e) {
				System.err.println("you eagler");
			}
		}
		
		// yee
	}

}
