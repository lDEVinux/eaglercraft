package net.lax1dude.eaglercraft.sp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet250CustomPayload;

public class VoiceChatPlugin {
	
	private static final Map<String, EntityPlayerMP> voicePlayers = new HashMap<>();
	private static final Map<String, ExpiringSet<String>> voiceRequests = new HashMap<>();
	private static final Set<String[]> voicePairs = new HashSet<>();
	
	private static final List<String> iceServers = new ArrayList();

	private static final int VOICE_SIGNAL_ALLOWED = 0;
	private static final int VOICE_SIGNAL_REQUEST = 0;
	private static final int VOICE_SIGNAL_CONNECT = 1;
	private static final int VOICE_SIGNAL_DISCONNECT = 2;
	private static final int VOICE_SIGNAL_ICE = 3;
	private static final int VOICE_SIGNAL_DESC = 4;
	private static final int VOICE_SIGNAL_GLOBAL = 5;
	
	public static boolean handleMessage(EntityPlayerMP player, Packet250CustomPayload payload) {
		if ("EAG|Voice".equals(payload.channel) && payload.data.length > 0) {
			deev: {
				String user = player.username;
				byte[] msg = payload.data;
				try {
					DataInputStream streamIn = new DataInputStream(new ByteArrayInputStream(msg));
					int sig = streamIn.read();
					switch (sig) {
					case VOICE_SIGNAL_CONNECT:
						if (voicePlayers.containsKey(user))
							break deev; // user is already using voice chat
						// send out packet for player joined voice
						// notice: everyone on the server can see this packet!! however, it doesn't do
						// anything but let clients know that the player has turned on voice chat
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						DataOutputStream dos = new DataOutputStream(baos);
						dos.write(VOICE_SIGNAL_CONNECT);
						dos.writeUTF(user);
						byte[] out = baos.toByteArray();
						for (EntityPlayerMP conn : voicePlayers.values())
							conn.playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|Voice", out));
						voicePlayers.put(user, player);
						for (String username : voicePlayers.keySet())
							sendVoicePlayers(username);
						break;
					case VOICE_SIGNAL_DISCONNECT:
						if (!voicePlayers.containsKey(user))
							break deev; // user is not using voice chat
						try {
							String user2 = streamIn.readUTF();
							if (!voicePlayers.containsKey(user2))
								break deev;
							if (removeIf(voicePairs, pair -> (pair[0].equals(user) && pair[1].equals(user2))
									|| (pair[0].equals(user2) && pair[1].equals(user)))) {
								ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
								DataOutputStream dos2 = new DataOutputStream(baos2);
								dos2.write(VOICE_SIGNAL_DISCONNECT);
								dos2.writeUTF(user);
								voicePlayers.get(user2).playerNetServerHandler.sendPacket(
										new Packet250CustomPayload("EAG|Voice", baos2.toByteArray()));
								baos2 = new ByteArrayOutputStream();
								dos2 = new DataOutputStream(baos2);
								dos2.write(VOICE_SIGNAL_DISCONNECT);
								dos2.writeUTF(user2);
								player.playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|Voice", baos2.toByteArray()));
							}
						} catch (EOFException e) {
							removeUser(user);
						}
						break;
					case VOICE_SIGNAL_REQUEST:
						if (!voicePlayers.containsKey(user))
							break deev; // user is not using voice chat
						String targetUser = streamIn.readUTF();
						if (user.equals(targetUser))
							break deev; // prevent duplicates
						if (checkVoicePair(user, targetUser))
							break deev; // already paired
						if (!voicePlayers.containsKey(targetUser))
							break deev; // target user is not using voice chat
						if (!voiceRequests.containsKey(user))
							voiceRequests.put(user, new ExpiringSet<>(2000));
						if (voiceRequests.get(user).contains(targetUser))
							break deev;
						voiceRequests.get(user).add(targetUser);
	
						// check if other has requested earlier
						if (voiceRequests.containsKey(targetUser) && voiceRequests.get(targetUser).contains(user)) {
							if (voiceRequests.containsKey(targetUser)) {
								voiceRequests.get(targetUser).remove(user);
								if (voiceRequests.get(targetUser).isEmpty())
									voiceRequests.remove(targetUser);
							}
							if (voiceRequests.containsKey(user)) {
								voiceRequests.get(user).remove(targetUser);
								if (voiceRequests.get(user).isEmpty())
									voiceRequests.remove(user);
							}
							// send each other add data
							voicePairs.add(new String[] { user, targetUser });
							ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
							DataOutputStream dos2 = new DataOutputStream(baos2);
							dos2.write(VOICE_SIGNAL_CONNECT);
							dos2.writeUTF(user);
							dos2.writeBoolean(false);
							voicePlayers.get(targetUser).playerNetServerHandler.sendPacket(
									new Packet250CustomPayload("EAG|Voice", baos2.toByteArray()));
							baos2 = new ByteArrayOutputStream();
							dos2 = new DataOutputStream(baos2);
							dos2.write(VOICE_SIGNAL_CONNECT);
							dos2.writeUTF(targetUser);
							dos2.writeBoolean(true);
							player.playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|Voice", baos2.toByteArray()));
						}
						break;
					case VOICE_SIGNAL_ICE:
					case VOICE_SIGNAL_DESC:
						if (!voicePlayers.containsKey(user))
							break deev; // user is not using voice chat
						String targetUser2 = streamIn.readUTF();
						if (checkVoicePair(user, targetUser2)) {
							String data = streamIn.readUTF();
							ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
							DataOutputStream dos2 = new DataOutputStream(baos2);
							dos2.write(sig);
							dos2.writeUTF(user);
							dos2.writeUTF(data);
							voicePlayers.get(targetUser2).playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|Voice", baos2.toByteArray()));
						}
						break;
					default:
						break;
					}
				} catch (Throwable t) {
					// hacker
					// t.printStackTrace(); // todo: remove in production
					removeUser(user);
				}
			}
			return true;
		}
		return false;
	}
	
	public static void activate(List<String> ice) {
		if(iceServers.size() == 0) {
			iceServers.addAll(ice);
			for(Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
				handleConnect((EntityPlayerMP) o);
			}
		}
	}

	public static void handleConnect(EntityPlayerMP player) {
		if(iceServers.size() > 0) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				dos.write(VOICE_SIGNAL_ALLOWED);
				dos.writeBoolean(true);
				dos.write(iceServers.size());
				for (String str : iceServers) {
					dos.writeUTF(str);
				}
				player.playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|Voice", baos.toByteArray()));
				sendVoicePlayers(player.username);
			} catch (IOException ignored) {
			}
		}
	}

	public static void handleDisconnect(EntityPlayerMP player) {
		removeUser(player.username);
	}
	
	private static void removeUser(String name) {
		voicePlayers.remove(name);
		for (String username : voicePlayers.keySet()) {
			if (!name.equals(username))
				sendVoicePlayers(username);
		}
		for (String[] voicePair : voicePairs) {
			String target = null;
			if (voicePair[0].equals(name)) {
				target = voicePair[1];
			} else if (voicePair[1].equals(name)) {
				target = voicePair[0];
			}
			if (target != null && voicePlayers.containsKey(target)) {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					DataOutputStream dos = new DataOutputStream(baos);
					dos.write(VOICE_SIGNAL_DISCONNECT);
					dos.writeUTF(name);
					voicePlayers.get(target).playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|Voice", baos.toByteArray()));
				} catch (IOException ignored) {
				}
			}
		}
		removeIf(voicePairs, pair -> pair[0].equals(name) || pair[1].equals(name));
	}
	
	public static void reset() {
		voicePlayers.clear();
		voiceRequests.clear();
		voicePairs.clear();
		iceServers.clear();
	}
	
	private static void sendVoicePlayers(String name) {
		if (!voicePlayers.containsKey(name))
			return;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(VOICE_SIGNAL_GLOBAL);
			Set<String> mostlyGlobalPlayers = new HashSet<>();
			for (String username : voicePlayers.keySet()) {
				if (username.equals(name))
					continue;
				if (anyMatch(voicePairs, pair -> (pair[0].equals(name) && pair[1].equals(username))
						|| (pair[0].equals(username) && pair[1].equals(name))))
					continue;
				mostlyGlobalPlayers.add(username);
			}
			if (mostlyGlobalPlayers.size() > 0) {
				dos.writeInt(mostlyGlobalPlayers.size());
				for (String username : mostlyGlobalPlayers)
					dos.writeUTF(username);
				voicePlayers.get(name).playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|Voice", baos.toByteArray()));
			}
		} catch (IOException ignored) {
		}
	}

	private static boolean checkVoicePair(String user1, String user2) {
		return anyMatch(voicePairs, pair -> (pair[0].equals(user1) && pair[1].equals(user2))
				|| (pair[0].equals(user2) && pair[1].equals(user1)));
	}
	
	/**
	 * JDK 8 function not available in TeaVM
	 */
	private static <T> boolean removeIf(Collection<T> collection, Predicate<T> pre) {
		boolean ret = false;
		Iterator<T> itr = collection.iterator();
		while(itr.hasNext()) {
			if(pre.test(itr.next())) {
				itr.remove();
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * JDK 8 function not available in TeaVM
	 */
	private static <T> boolean anyMatch(Collection<T> collection, Predicate<T> pre) {
		boolean ret = false;
		Iterator<T> itr = collection.iterator();
		while(itr.hasNext()) {
			if(pre.test(itr.next())) {
				return true;
			}
		}
		return false;
	}
	
}
