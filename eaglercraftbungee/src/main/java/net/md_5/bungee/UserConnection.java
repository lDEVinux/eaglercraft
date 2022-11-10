// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee;

import java.beans.ConstructorProperties;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.logging.Level;

import com.google.common.base.Preconditions;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.PlatformDependent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.config.TexturePackInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.tab.TabListHandler;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.eaglercraft.RedirectServerInfo;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.packet.Packet3Chat;
import net.md_5.bungee.protocol.packet.PacketCCSettings;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.PacketFFKick;
import net.md_5.bungee.util.CaseInsensitiveSet;

public final class UserConnection implements ProxiedPlayer {
	private final ProxyServer bungee;
	private final ChannelWrapper ch;
	private final String name;
	private final InitialHandler pendingConnection;
	private ServerConnection server;
	private final Object switchMutex;
	private final Collection<ServerInfo> pendingConnects;
	private TabListHandler tabList;
	private int sentPingId;
	private long sentPingTime;
	private int ping;
	private final Collection<String> groups;
	private final Collection<String> permissions;
	private int clientEntityId;
	private int serverEntityId;
	private PacketCCSettings settings;
	private final Scoreboard serverSentScoreboard;
	private String displayName;
	private final Connection.Unsafe unsafe;
	private final Map<String, Object> attachment = new WeakHashMap();

	public void init() {
		this.displayName = this.name;
		try {
			this.tabList = (TabListHandler) this.getPendingConnection().getListener().getTabList().getDeclaredConstructor((Class<?>[]) new Class[0]).newInstance(new Object[0]);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
		this.tabList.init(this);
		final Collection<String> g = this.bungee.getConfigurationAdapter().getGroups(this.name);
		for (final String s : g) {
			this.addGroups(s);
		}
	}

	@Override
	public void setTabList(final TabListHandler tabList) {
		tabList.init(this);
		this.tabList = tabList;
	}

	public void sendPacket(final byte[] b) {
		this.ch.write(b);
	}

	@Deprecated
	public boolean isActive() {
		return !this.ch.isClosed();
	}

	@Override
	public void setDisplayName(final String name) {
		Preconditions.checkNotNull((Object) name, (Object) "displayName");
		Preconditions.checkArgument(name.length() <= 16, (Object) "Display name cannot be longer than 16 characters");
		this.getTabList().onDisconnect();
		this.displayName = name;
		this.getTabList().onConnect();
	}

	@Override
	public void connect(final ServerInfo target) {
		this.connect(target, false);
	}

	void sendDimensionSwitch() {
		this.unsafe().sendPacket(PacketConstants.DIM1_SWITCH);
		this.unsafe().sendPacket(PacketConstants.DIM2_SWITCH);
	}

	public void connectNow(final ServerInfo target) {
		this.sendDimensionSwitch();
		this.connect(target);
	}

	public void connect(final ServerInfo info, final boolean retry) {
		if(info instanceof RedirectServerInfo) {
			sendData("EAG|Reconnect", ((RedirectServerInfo)info).getRedirect().getBytes(StandardCharsets.UTF_8));
			return;
		}
		final ServerConnectEvent event = new ServerConnectEvent(this, info);
		if (this.bungee.getPluginManager().callEvent(event).isCancelled()) {
			return;
		}
		final BungeeServerInfo target = (BungeeServerInfo) event.getTarget();
		if (this.getServer() != null && this.getServer().getInfo() != null && Objects.equals(this.getServer().getInfo(), target)) {
			//this.sendMessage(ChatColor.RED + "Cannot connect to server you are already on!");
			return;
		}
		if (this.pendingConnects.contains(target)) {
			this.sendMessage(ChatColor.RED + "Already connecting to this server!");
			return;
		}
		this.pendingConnects.add(target);
		final ChannelInitializer initializer = new ChannelInitializer() {
			protected void initChannel(final Channel ch) throws Exception {
				PipelineUtils.BASE.initChannel(ch);
				((HandlerBoss) ch.pipeline().get((Class) HandlerBoss.class)).setHandler(new ServerConnector(UserConnection.this.bungee, UserConnection.this, target));
			}
		};
		final ChannelFutureListener listener = (ChannelFutureListener) new ChannelFutureListener() {
			public void operationComplete(final ChannelFuture future) throws Exception {
				if (!future.isSuccess()) {
					future.channel().close();
					UserConnection.this.pendingConnects.remove(target);
					final ServerInfo def = ProxyServer.getInstance().getServers().get(UserConnection.this.getPendingConnection().getListener().getFallbackServer());
					if ((retry & target != def) && ((UserConnection.this.getServer() == null || UserConnection.this.getServer().getInfo() == null) || def != UserConnection.this.getServer().getInfo())) {
						UserConnection.this.sendMessage(UserConnection.this.bungee.getTranslation("fallback_lobby"));
						UserConnection.this.connect(def, false);
					} else if (UserConnection.this.getServer() == null || UserConnection.this.getServer().getInfo() == null) {
						UserConnection.this.disconnect(UserConnection.this.bungee.getTranslation("fallback_kick") + future.cause().getClass().getName());
					} else {
						UserConnection.this.sendMessage(UserConnection.this.bungee.getTranslation("fallback_kick") + future.cause().getClass().getName());
					}
				}
			}
		};
		final Bootstrap b = ((Bootstrap) ((Bootstrap) ((Bootstrap) ((Bootstrap) new Bootstrap().channel((Class) NioSocketChannel.class)).group((EventLoopGroup) BungeeCord.getInstance().eventLoops)).handler((ChannelHandler) initializer))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(5000))).remoteAddress((SocketAddress) target.getAddress());
		if (!PlatformDependent.isWindows()) {
			b.localAddress(this.getPendingConnection().getListener().getHost().getHostString(), 0);
		}
		b.connect().addListener((GenericFutureListener) listener);
	}

	@Override
	public synchronized void disconnect(final String reason) {
		if (this.ch.getHandle().isActive()) {
			this.bungee.getLogger().log(Level.INFO, "[" + this.getName() + "] disconnected with: " + reason);
			this.unsafe().sendPacket(new PacketFFKick(reason));
			this.ch.close();
			if (this.server != null && this.server.getInfo() != null) {
				this.server.disconnect("Quitting");
			}
		}
	}

	@Override
	public void chat(final String message) {
		Preconditions.checkState(this.server != null, (Object) "Not connected to server");
		this.server.getCh().write(new Packet3Chat(message));
	}

	@Override
	public void sendMessage(final String message) {
		this.unsafe().sendPacket(new Packet3Chat(message));
	}

	@Override
	public void sendMessages(final String... messages) {
		for (final String message : messages) {
			this.sendMessage(message);
		}
	}

	@Override
	public void sendData(final String channel, final byte[] data) {
		this.unsafe().sendPacket(new PacketFAPluginMessage(channel, data));
	}

	@Override
	public InetSocketAddress getAddress() {
		return (InetSocketAddress) this.ch.getHandle().remoteAddress();
	}

	@Override
	public Collection<String> getGroups() {
		return Collections.unmodifiableCollection((Collection<? extends String>) this.groups);
	}

	@Override
	public void addGroups(final String... groups) {
		for (final String group : groups) {
			this.groups.add(group);
			for (final String permission : this.bungee.getConfigurationAdapter().getPermissions(group)) {
				this.setPermission(permission, true);
			}
		}
	}

	@Override
	public void removeGroups(final String... groups) {
		for (final String group : groups) {
			this.groups.remove(group);
			for (final String permission : this.bungee.getConfigurationAdapter().getPermissions(group)) {
				this.setPermission(permission, false);
			}
		}
	}

	@Override
	public boolean hasPermission(final String permission) {
		return this.bungee.getPluginManager().callEvent(new PermissionCheckEvent(this, permission, this.permissions.contains(permission))).hasPermission();
	}

	@Override
	public void setPermission(final String permission, final boolean value) {
		if (value) {
			this.permissions.add(permission);
		} else {
			this.permissions.remove(permission);
		}
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public void setTexturePack(final TexturePackInfo pack) {
		this.unsafe().sendPacket(new PacketFAPluginMessage("MC|TPack", (pack.getUrl() + "\u0000" + pack.getSize()).getBytes()));
	}

	@Override
	public Connection.Unsafe unsafe() {
		return this.unsafe;
	}

	@ConstructorProperties({ "bungee", "ch", "name", "pendingConnection" })
	public UserConnection(final ProxyServer bungee, final ChannelWrapper ch, final String name, final InitialHandler pendingConnection) {
		this.switchMutex = new Object();
		this.pendingConnects = new HashSet<ServerInfo>();
		this.ping = 100;
		this.groups = (Collection<String>) new CaseInsensitiveSet();
		this.permissions = (Collection<String>) new CaseInsensitiveSet();
		this.serverSentScoreboard = new Scoreboard();
		this.unsafe = new Connection.Unsafe() {
			@Override
			public void sendPacket(final DefinedPacket packet) {
				UserConnection.this.ch.write(packet);
			}
		};
		if (bungee == null) {
			throw new NullPointerException("bungee");
		}
		if (ch == null) {
			throw new NullPointerException("ch");
		}
		if (name == null) {
			throw new NullPointerException("name");
		}
		this.bungee = bungee;
		this.ch = ch;
		this.name = name;
		this.pendingConnection = pendingConnection;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public InitialHandler getPendingConnection() {
		return this.pendingConnection;
	}

	@Override
	public ServerConnection getServer() {
		return this.server;
	}

	public void setServer(final ServerConnection server) {
		this.server = server;
	}

	public Object getSwitchMutex() {
		return this.switchMutex;
	}

	public Collection<ServerInfo> getPendingConnects() {
		return this.pendingConnects;
	}

	@Override
	public TabListHandler getTabList() {
		return this.tabList;
	}

	public int getSentPingId() {
		return this.sentPingId;
	}

	public void setSentPingId(final int sentPingId) {
		this.sentPingId = sentPingId;
	}

	public long getSentPingTime() {
		return this.sentPingTime;
	}

	public void setSentPingTime(final long sentPingTime) {
		this.sentPingTime = sentPingTime;
	}

	@Override
	public int getPing() {
		return this.ping;
	}

	public void setPing(final int ping) {
		this.ping = ping;
	}

	public int getClientEntityId() {
		return this.clientEntityId;
	}

	public void setClientEntityId(final int clientEntityId) {
		this.clientEntityId = clientEntityId;
	}

	public int getServerEntityId() {
		return this.serverEntityId;
	}

	public void setServerEntityId(final int serverEntityId) {
		this.serverEntityId = serverEntityId;
	}

	public PacketCCSettings getSettings() {
		return this.settings;
	}

	public void setSettings(final PacketCCSettings settings) {
		this.settings = settings;
	}

	public Scoreboard getServerSentScoreboard() {
		return this.serverSentScoreboard;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public Map<String, Object> getAttachment() {
		return attachment;
	}
}
