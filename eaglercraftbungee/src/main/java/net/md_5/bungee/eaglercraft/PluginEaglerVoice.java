package net.md_5.bungee.eaglercraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.event.EventHandler;

public class PluginEaglerVoice extends Plugin implements Listener {

    private final boolean voiceEnabled;

    private final Map<String, UserConnection> voicePlayers = new HashMap<>();
    private final Map<String, ExpiringSet<String>> voiceRequests = new HashMap<>();
    private final Set<String[]> voicePairs = new HashSet<>();

    private static final int VOICE_SIGNAL_ALLOWED = 0;
    private static final int VOICE_SIGNAL_REQUEST = 0;
    private static final int VOICE_SIGNAL_CONNECT = 1;
    private static final int VOICE_SIGNAL_DISCONNECT = 2;
    private static final int VOICE_SIGNAL_ICE = 3;
    private static final int VOICE_SIGNAL_DESC = 4;
    private static final int VOICE_SIGNAL_GLOBAL = 5;

    public PluginEaglerVoice(boolean voiceEnabled) {
        super(new PluginDescription("EaglerVoice", PluginEaglerVoice.class.getName(), "1.0.0", "ayunami2000", Collections.emptySet(), null));
        this.voiceEnabled = voiceEnabled;
    }

    public void onLoad() {

    }

    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
    }

    public void onDisable() {

    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        synchronized (voicePlayers) {
            if (!voiceEnabled) return;
            if (event.getSender() instanceof UserConnection && event.getData().length > 0) {
                UserConnection connection = (UserConnection) event.getSender();
                String user = connection.getName();
                byte[] msg = event.getData();
                try {
                    if (!("EAG|Voice".equals(event.getTag()))) return;
                    event.setCancelled(true);
                    DataInputStream streamIn = new DataInputStream(new ByteArrayInputStream(msg));
                    int sig = streamIn.read();
                    switch (sig) {
                        case VOICE_SIGNAL_CONNECT:
                            if (voicePlayers.containsKey(user)) return; // user is already using voice chat
                            // send out packet for player joined voice
                            // notice: everyone on the server can see this packet!! however, it doesn't do anything but let clients know that the player has turned on voice chat
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            DataOutputStream dos = new DataOutputStream(baos);
                            dos.write(VOICE_SIGNAL_CONNECT);
                            dos.writeUTF(user);
                            byte[] out = baos.toByteArray();
                            for (UserConnection conn : voicePlayers.values()) conn.sendData("EAG|Voice", out);
                            voicePlayers.put(user, connection);
                            for (String username : voicePlayers.keySet()) sendVoicePlayers(username);
                            break;
                        case VOICE_SIGNAL_DISCONNECT:
                            if (!voicePlayers.containsKey(user)) return; // user is not using voice chat
                            try {
                                String user2 = streamIn.readUTF();
                                if (!voicePlayers.containsKey(user2)) return;
                                if (voicePairs.removeIf(pair -> (pair[0].equals(user) && pair[1].equals(user2)) || (pair[0].equals(user2) && pair[1].equals(user)))) {
                                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                                    DataOutputStream dos2 = new DataOutputStream(baos2);
                                    dos2.write(VOICE_SIGNAL_DISCONNECT);
                                    dos2.writeUTF(user);
                                    voicePlayers.get(user2).sendData("EAG|Voice", baos2.toByteArray());
                                    baos2 = new ByteArrayOutputStream();
                                    dos2 = new DataOutputStream(baos2);
                                    dos2.write(VOICE_SIGNAL_DISCONNECT);
                                    dos2.writeUTF(user2);
                                    connection.sendData("EAG|Voice", baos2.toByteArray());
                                }
                            } catch (EOFException e) {
                                removeUser(user);
                            }
                            break;
                        case VOICE_SIGNAL_REQUEST:
                            if (!voicePlayers.containsKey(user)) return; // user is not using voice chat
                            String targetUser = streamIn.readUTF();
                            if (user.equals(targetUser)) return; // prevent duplicates
                            if (checkVoicePair(user, targetUser)) return; // already paired
                            if (!voicePlayers.containsKey(targetUser)) return; // target user is not using voice chat
                            if (!voiceRequests.containsKey(user)) voiceRequests.put(user, new ExpiringSet<>(2000));
                            if (voiceRequests.get(user).contains(targetUser)) return;
                            voiceRequests.get(user).add(targetUser);

                            // check if other has requested earlier
                            if (voiceRequests.containsKey(targetUser) && voiceRequests.get(targetUser).contains(user)) {
                                if (voiceRequests.containsKey(targetUser)) {
                                    voiceRequests.get(targetUser).remove(user);
                                    if (voiceRequests.get(targetUser).isEmpty()) voiceRequests.remove(targetUser);
                                }
                                if (voiceRequests.containsKey(user)) {
                                    voiceRequests.get(user).remove(targetUser);
                                    if (voiceRequests.get(user).isEmpty()) voiceRequests.remove(user);
                                }
                                // send each other add data
                                voicePairs.add(new String[]{user, targetUser});
                                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                                DataOutputStream dos2 = new DataOutputStream(baos2);
                                dos2.write(VOICE_SIGNAL_CONNECT);
                                dos2.writeUTF(user);
                                dos2.writeBoolean(false);
                                voicePlayers.get(targetUser).sendData("EAG|Voice", baos2.toByteArray());
                                baos2 = new ByteArrayOutputStream();
                                dos2 = new DataOutputStream(baos2);
                                dos2.write(VOICE_SIGNAL_CONNECT);
                                dos2.writeUTF(targetUser);
                                dos2.writeBoolean(true);
                                connection.sendData("EAG|Voice", baos2.toByteArray());
                            }
                            break;
                        case VOICE_SIGNAL_ICE:
                        case VOICE_SIGNAL_DESC:
                            if (!voicePlayers.containsKey(user)) return; // user is not using voice chat
                            String targetUser2 = streamIn.readUTF();
                            if (checkVoicePair(user, targetUser2)) {
                                String data = streamIn.readUTF();
                                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                                DataOutputStream dos2 = new DataOutputStream(baos2);
                                dos2.write(sig);
                                dos2.writeUTF(user);
                                dos2.writeUTF(data);
                                voicePlayers.get(targetUser2).sendData("EAG|Voice", baos2.toByteArray());
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
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.write(VOICE_SIGNAL_ALLOWED);
            dos.writeBoolean(voiceEnabled);
            Collection<String> servs = BungeeCord.getInstance().config.getICEServers();
            dos.write(servs.size());
            for(String str : servs) {
            	dos.writeUTF(str);
            }
            event.getPlayer().sendData("EAG|Voice", baos.toByteArray());
            sendVoicePlayers(event.getPlayer().getName());
        } catch (IOException ignored) {  }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        String nm = event.getPlayer().getName();
        removeUser(nm);
    }

    public void sendVoicePlayers(String name) {
        synchronized (voicePlayers) {
            if (!voiceEnabled) return;
            if (!voicePlayers.containsKey(name)) return;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.write(VOICE_SIGNAL_GLOBAL);
                Set<String> mostlyGlobalPlayers = new HashSet<>();
                for (String username : voicePlayers.keySet()) {
                    if (username.equals(name)) continue;
                    if (voicePairs.stream().anyMatch(pair -> (pair[0].equals(name) && pair[1].equals(username)) || (pair[0].equals(username) && pair[1].equals(name))))
                        continue;
                    mostlyGlobalPlayers.add(username);
                }
                if (mostlyGlobalPlayers.size() > 0) {
                    dos.writeInt(mostlyGlobalPlayers.size());
                    for (String username : mostlyGlobalPlayers) dos.writeUTF(username);
                    voicePlayers.get(name).sendData("EAG|Voice", baos.toByteArray());
                }
            } catch (IOException ignored) {
            }
        }
    }

    public void removeUser(String name) {
        synchronized (voicePlayers) {
            voicePlayers.remove(name);
            for (String username : voicePlayers.keySet()) {
                if (!name.equals(username)) sendVoicePlayers(username);
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
                        voicePlayers.get(target).sendData("EAG|Voice", baos.toByteArray());
                    } catch (IOException ignored) {
                    }
                }
            }
            voicePairs.removeIf(pair -> pair[0].equals(name) || pair[1].equals(name));
        }
    }

    private boolean checkVoicePair(String user1, String user2) {
        return voicePairs.stream().anyMatch(pair -> (pair[0].equals(user1) && pair[1].equals(user2)) || (pair[0].equals(user2) && pair[1].equals(user1)));
    }
}
