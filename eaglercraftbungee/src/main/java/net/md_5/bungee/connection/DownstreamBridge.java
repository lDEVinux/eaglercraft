// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.connection;

import java.beans.ConstructorProperties;
import java.net.InetAddress;
import java.util.Objects;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EntityMap;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.score.Objective;
import net.md_5.bungee.api.score.Position;
import net.md_5.bungee.api.score.Score;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.score.Team;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.packet.Packet0KeepAlive;
import net.md_5.bungee.protocol.packet.PacketC9PlayerListItem;
import net.md_5.bungee.protocol.packet.PacketCEScoreboardObjective;
import net.md_5.bungee.protocol.packet.PacketCFScoreboardScore;
import net.md_5.bungee.protocol.packet.PacketD0DisplayScoreboard;
import net.md_5.bungee.protocol.packet.PacketD1Team;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.PacketFFKick;

public class DownstreamBridge extends PacketHandler {
	private final ProxyServer bungee;
	private final UserConnection con;
	private final ServerConnection server;

	@Override
	public void exception(final Throwable t) throws Exception {
		final ServerInfo def = this.bungee.getServerInfo(this.con.getPendingConnection().getListener().getFallbackServer());
		if (this.server.getInfo() != def) {
			this.con.connectNow(def);
			this.con.sendMessage(ChatColor.RED + "The server you were previously on went down, you have been connected to the lobby");
		} else {
			this.con.disconnect(Util.exception(t));
		}
	}

	@Override
	public void disconnected(final ChannelWrapper channel) throws Exception {
		this.server.getInfo().removePlayer(this.con);
		this.bungee.getReconnectHandler().setServer(this.con);
		if (!this.server.isObsolete()) {
			this.con.disconnect(this.bungee.getTranslation("lost_connection"));
		}
	}

	@Override
	public void handle(final byte[] buf) throws Exception {
		if (!this.server.isObsolete()) {
			EntityMap.rewrite(buf, this.con.getServerEntityId(), this.con.getClientEntityId());
			this.con.sendPacket(buf);
		}
	}

	@Override
	public void handle(final Packet0KeepAlive alive) throws Exception {
		this.con.setSentPingId(alive.getRandomId());
		this.con.setSentPingTime(System.currentTimeMillis());
	}

	@Override
	public void handle(final PacketC9PlayerListItem playerList) throws Exception {
		if (!this.con.getTabList().onListUpdate(playerList.getUsername(), playerList.isOnline(), playerList.getPing())) {
			throw new CancelSendSignal();
		}
	}

	@Override
	public void handle(final PacketCEScoreboardObjective objective) throws Exception {
		final Scoreboard serverScoreboard = this.con.getServerSentScoreboard();
		switch (objective.getAction()) {
		case 0: {
			serverScoreboard.addObjective(new Objective(objective.getName(), objective.getText()));
			break;
		}
		case 1: {
			serverScoreboard.removeObjective(objective.getName());
			break;
		}
		}
	}

	@Override
	public void handle(final PacketCFScoreboardScore score) throws Exception {
		final Scoreboard serverScoreboard = this.con.getServerSentScoreboard();
		switch (score.getAction()) {
		case 0: {
			final Score s = new Score(score.getItemName(), score.getScoreName(), score.getValue());
			serverScoreboard.removeScore(score.getItemName());
			serverScoreboard.addScore(s);
			break;
		}
		case 1: {
			serverScoreboard.removeScore(score.getItemName());
			break;
		}
		}
	}

	@Override
	public void handle(final PacketD0DisplayScoreboard displayScoreboard) throws Exception {
		final Scoreboard serverScoreboard = this.con.getServerSentScoreboard();
		serverScoreboard.setName(displayScoreboard.getName());
		serverScoreboard.setPosition(Position.values()[displayScoreboard.getPosition()]);
	}

	@Override
	public void handle(final PacketD1Team team) throws Exception {
		final Scoreboard serverScoreboard = this.con.getServerSentScoreboard();
		if (team.getMode() == 1) {
			serverScoreboard.removeTeam(team.getName());
			return;
		}
		Team t;
		if (team.getMode() == 0) {
			t = new Team(team.getName());
			serverScoreboard.addTeam(t);
		} else {
			t = serverScoreboard.getTeam(team.getName());
		}
		if (t != null) {
			if (team.getMode() == 0 || team.getMode() == 2) {
				t.setDisplayName(team.getDisplayName());
				t.setPrefix(team.getPrefix());
				t.setSuffix(team.getSuffix());
				t.setFriendlyFire(team.isFriendlyFire());
			}
			if (team.getPlayers() != null) {
				for (final String s : team.getPlayers()) {
					if (team.getMode() == 0 || team.getMode() == 3) {
						t.addPlayer(s);
					} else {
						t.removePlayer(s);
					}
				}
			}
		}
	}

	@Override
	public void handle(final PacketFAPluginMessage pluginMessage) throws Exception {
		final ByteArrayDataInput in = ByteStreams.newDataInput(pluginMessage.getData());
		final PluginMessageEvent event = new PluginMessageEvent(this.con.getServer(), this.con, pluginMessage.getTag(), pluginMessage.getData().clone());
		if (this.bungee.getPluginManager().callEvent(event).isCancelled()) {
			throw new CancelSendSignal();
		}
		if (pluginMessage.getTag().equals("MC|TPack") && this.con.getPendingConnection().getListener().getTexturePack() != null) {
			throw new CancelSendSignal();
		}
		if (pluginMessage.getTag().equals("BungeeCord")) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			final String subChannel = in.readUTF();
			if (subChannel.equals("Forward")) {
				final String target = in.readUTF();
				final String channel = in.readUTF();
				final short len = in.readShort();
				final byte[] data = new byte[len];
				in.readFully(data);
				out.writeUTF(channel);
				out.writeShort(data.length);
				out.write(data);
				final byte[] payload = out.toByteArray();
				out = null;
				if (target.equals("ALL")) {
					for (final ServerInfo server : this.bungee.getServers().values()) {
						if (server != this.con.getServer().getInfo()) {
							server.sendData("BungeeCord", payload);
						}
					}
				} else {
					final ServerInfo server2 = this.bungee.getServerInfo(target);
					if (server2 != null) {
						server2.sendData("BungeeCord", payload);
					}
				}
			}
			if (subChannel.equals("Connect")) {
				final ServerInfo server3 = this.bungee.getServerInfo(in.readUTF());
				if (server3 != null) {
					this.con.connect(server3);
				}
			}
			if (subChannel.equals("IP")) {
				out.writeUTF("IP");
				Object ob = this.con.getAttachment().get("remoteAddr");
				if(ob != null && (ob instanceof InetAddress)) {
					out.writeUTF(((InetAddress)ob).getHostAddress());
					out.writeInt(this.con.getAddress().getPort());
				}else {
					out.writeUTF(this.con.getAddress().getHostString());
					out.writeInt(this.con.getAddress().getPort());
				}
			}
			if (subChannel.equals("PlayerCount")) {
				final String target = in.readUTF();
				out.writeUTF("PlayerCount");
				if (target.equals("ALL")) {
					out.writeUTF("ALL");
					out.writeInt(this.bungee.getOnlineCount());
				} else {
					final ServerInfo server4 = this.bungee.getServerInfo(target);
					if (server4 != null) {
						out.writeUTF(server4.getName());
						out.writeInt(server4.getPlayers().size());
					}
				}
			}
			if (subChannel.equals("PlayerList")) {
				final String target = in.readUTF();
				out.writeUTF("PlayerList");
				if (target.equals("ALL")) {
					out.writeUTF("ALL");
					out.writeUTF(Util.csv(this.bungee.getPlayers()));
				} else {
					final ServerInfo server4 = this.bungee.getServerInfo(target);
					if (server4 != null) {
						out.writeUTF(server4.getName());
						out.writeUTF(Util.csv(server4.getPlayers()));
					}
				}
			}
			if (subChannel.equals("GetServers")) {
				out.writeUTF("GetServers");
				out.writeUTF(Util.csv(this.bungee.getServers().keySet()));
			}
			if (subChannel.equals("Message")) {
				final ProxiedPlayer target2 = this.bungee.getPlayer(in.readUTF());
				if (target2 != null) {
					target2.sendMessage(in.readUTF());
				}
			}
			if (subChannel.equals("GetServer")) {
				out.writeUTF("GetServer");
				out.writeUTF(this.server.getInfo().getName());
			}
			if (subChannel.equals("EAG|GetDomain")) {
				out.writeUTF("EAG|GetDomain");
				Object ob = this.con.getAttachment().get("origin");
				if(ob != null && (ob instanceof String)) {
					out.writeBoolean(true);
					out.writeUTF((String)ob);
				}else {
					out.writeBoolean(false);
					out.writeUTF("");
				}
			}
			if (subChannel.equals("EAG|ConsoleCommand")) {
				if(BungeeCord.getInstance().config.shouldAcceptBukkitConsoleCommandPacket()) {
					String cmd = in.readUTF();
					bungee.getLogger().info("Connection [" + this.con.getName() + "] <-> [" + this.server.getInfo().getName() + "] executed bungee console command: " + cmd);
					bungee.getPluginManager().dispatchCommand(bungee.getConsole(), cmd);
				}else {
					bungee.getLogger().info("Connection [" + this.con.getName() + "] <-> [" + this.server.getInfo().getName() + "] tried executing a bungee console command but \"accept_bukkit_console_command_packets\" is set to false in config.yml");
				}
			}
			if (out != null) {
				final byte[] b = out.toByteArray();
				if (b.length != 0) {
					this.con.getServer().sendData("BungeeCord", b);
				}
			}
		}
	}

	@Override
	public void handle(final PacketFFKick kick) throws Exception {
		ServerInfo def = this.bungee.getServerInfo(this.con.getPendingConnection().getListener().getFallbackServer());
		if (Objects.equals(this.server.getInfo(), def)) {
			def = null;
		}
		final ServerKickEvent event = this.bungee.getPluginManager().callEvent(new ServerKickEvent(this.con, kick.getMessage(), def));
		if (event.isCancelled() && event.getCancelServer() != null) {
			this.con.connectNow(event.getCancelServer());
		} else {
			this.con.disconnect(this.bungee.getTranslation("server_kick") + event.getKickReason());
		}
		this.server.setObsolete(true);
		throw new CancelSendSignal();
	}

	@Override
	public String toString() {
		return "[" + this.con.getName() + "] <-> DownstreamBridge <-> [" + this.server.getInfo().getName() + "]";
	}

	@ConstructorProperties({ "bungee", "con", "server" })
	public DownstreamBridge(final ProxyServer bungee, final UserConnection con, final ServerConnection server) {
		this.bungee = bungee;
		this.con = con;
		this.server = server;
	}
}
