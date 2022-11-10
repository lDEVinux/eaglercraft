// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.jansi.AnsiConsole;

import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import jline.internal.Log;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.api.tab.CustomTabList;
import net.md_5.bungee.command.CommandAlert;
import net.md_5.bungee.command.CommandBungee;
import net.md_5.bungee.command.CommandChangePassword;
import net.md_5.bungee.command.CommandClearRatelimit;
import net.md_5.bungee.command.CommandConfirmCode;
import net.md_5.bungee.command.CommandDomain;
import net.md_5.bungee.command.CommandDomainBlock;
import net.md_5.bungee.command.CommandDomainBlockDomain;
import net.md_5.bungee.command.CommandDomainUnblock;
import net.md_5.bungee.command.CommandEnd;
import net.md_5.bungee.command.CommandFind;
import net.md_5.bungee.command.CommandGlobalBan;
import net.md_5.bungee.command.CommandGlobalBanIP;
import net.md_5.bungee.command.CommandGlobalBanRegex;
import net.md_5.bungee.command.CommandGlobalBanReload;
import net.md_5.bungee.command.CommandGlobalBanWildcard;
import net.md_5.bungee.command.CommandGlobalCheckBan;
import net.md_5.bungee.command.CommandGlobalListBan;
import net.md_5.bungee.command.CommandGlobalUnban;
import net.md_5.bungee.command.CommandIP;
import net.md_5.bungee.command.CommandList;
import net.md_5.bungee.command.CommandPerms;
import net.md_5.bungee.command.CommandReload;
import net.md_5.bungee.command.CommandSend;
import net.md_5.bungee.command.CommandServer;
import net.md_5.bungee.command.ConsoleCommandSender;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfig;
import net.md_5.bungee.eaglercraft.AuthHandler;
import net.md_5.bungee.eaglercraft.AuthSystem;
import net.md_5.bungee.eaglercraft.BanList;
import net.md_5.bungee.eaglercraft.DomainBlacklist;
import net.md_5.bungee.eaglercraft.PluginEaglerSkins;
import net.md_5.bungee.eaglercraft.PluginEaglerVoice;
import net.md_5.bungee.eaglercraft.WebSocketListener;
import net.md_5.bungee.log.BungeeLogger;
import net.md_5.bungee.log.LoggingOutputStream;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.packet.Packet3Chat;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.reconnect.SQLReconnectHandler;
import net.md_5.bungee.scheduler.BungeeScheduler;
import net.md_5.bungee.scheduler.BungeeThreadPool;
import net.md_5.bungee.tab.Custom;
import net.md_5.bungee.util.CaseInsensitiveMap;

public class BungeeCord extends ProxyServer {
	public volatile boolean isRunning;
	public final Configuration config;
	public final ResourceBundle bundle;
	public final ScheduledThreadPoolExecutor executors;
	public final MultithreadEventLoopGroup eventLoops;
	private final Timer saveThread;
	private final Timer reloadBanThread;
	private final Timer closeInactiveSockets;
	private final Timer authTimeoutTimer;
	private Collection<Channel> listeners;
	private Collection<WebSocketListener> wsListeners;
	private final Map<String, UserConnection> connections;
	private final ReadWriteLock connectionLock;
	public final PluginManager pluginManager;
	private ReconnectHandler reconnectHandler;
	private ConfigurationAdapter configurationAdapter;
	private final Collection<String> pluginChannels;
	private final File pluginsFolder;
	private final TaskScheduler scheduler;
	private ConsoleReader consoleReader;
	private final Logger logger;
	private Collection<Command> banCommands;
	public AuthSystem authSystem;
	public String tokenVerify;

	public static BungeeCord getInstance() {
		return (BungeeCord) ProxyServer.getInstance();
	}

	public BungeeCord() throws IOException {
		this.config = new Configuration();
		this.bundle = ResourceBundle.getBundle("messages_en");
		this.executors = new BungeeThreadPool(new ThreadFactoryBuilder().setNameFormat("Bungee Pool Thread #%1$d").build());
		this.eventLoops = (MultithreadEventLoopGroup) new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("Netty IO Thread #%1$d").build());
		this.saveThread = new Timer("Reconnect Saver");
		this.reloadBanThread = new Timer("Ban List Reload");
		this.closeInactiveSockets = new Timer("Close Inactive WebSockets");
		this.authTimeoutTimer = new Timer("Auth Timeout");
		this.listeners = new HashSet<Channel>();
		this.wsListeners = new HashSet<WebSocketListener>();
		this.connections = (Map<String, UserConnection>) new CaseInsensitiveMap();
		this.connectionLock = new ReentrantReadWriteLock();
		this.pluginManager = new PluginManager(this);
		this.configurationAdapter = new YamlConfig();
		this.pluginChannels = new HashSet<String>();
		this.pluginsFolder = new File("plugins");
		this.scheduler = new BungeeScheduler();
		this.banCommands = new ArrayList();
		this.getPluginManager().registerCommand(null, new CommandReload());
		this.getPluginManager().registerCommand(null, new CommandEnd());
		this.getPluginManager().registerCommand(null, new CommandList());
		this.getPluginManager().registerCommand(null, new CommandServer());
		this.getPluginManager().registerCommand(null, new CommandIP());
		this.getPluginManager().registerCommand(null, new CommandAlert());
		this.getPluginManager().registerCommand(null, new CommandBungee());
		this.getPluginManager().registerCommand(null, new CommandPerms());
		this.getPluginManager().registerCommand(null, new CommandSend());
		this.getPluginManager().registerCommand(null, new CommandFind());
		this.getPluginManager().registerCommand(null, new CommandClearRatelimit());
		this.getPluginManager().registerCommand(null, new CommandConfirmCode());
		this.getPluginManager().registerCommand(null, new CommandDomain());
		this.getPluginManager().registerCommand(null, new CommandDomainBlock());
		this.getPluginManager().registerCommand(null, new CommandDomainBlockDomain());
		this.getPluginManager().registerCommand(null, new CommandDomainUnblock());
		this.registerChannel("BungeeCord");
		Log.setOutput(new PrintStream(ByteStreams.nullOutputStream()));
		AnsiConsole.systemInstall();
		this.consoleReader = new ConsoleReader();
		this.logger = new BungeeLogger(this);
		System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
		System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));
		if (this.consoleReader.getTerminal() instanceof UnsupportedTerminal) {
			this.logger.info("Unable to initialize fancy terminal. To fix this on Windows, install the correct Microsoft Visual C++ 2008 Runtime");
			this.logger.info("NOTE: This error is non crucial, and BungeeCord will still function correctly! Do not bug the author about it unless you are still unable to get it working");
		}
	}
	
	public void reconfigureBanCommands(boolean replaceBukkit) {
		if(banCommands.size() > 0) {
			for(Command c : banCommands) {
				this.getPluginManager().unregisterCommand(c);
			}
			banCommands.clear();
		}
		
		Command cBan = new CommandGlobalBan(replaceBukkit);
		Command cUnban = new CommandGlobalUnban(replaceBukkit);
		Command cBanReload = new CommandGlobalBanReload(replaceBukkit);
		Command cBanIP = new CommandGlobalBanIP(replaceBukkit);
		Command cBanWildcard = new CommandGlobalBanWildcard(replaceBukkit);
		Command cBanRegex = new CommandGlobalBanRegex(replaceBukkit);
		Command cBanCheck = new CommandGlobalCheckBan(replaceBukkit);
		Command cBanList = new CommandGlobalListBan(replaceBukkit);

		banCommands.add(cBan);
		banCommands.add(cUnban);
		banCommands.add(cBanReload);
		banCommands.add(cBanIP);
		banCommands.add(cBanWildcard);
		banCommands.add(cBanRegex);
		banCommands.add(cBanCheck);
		banCommands.add(cBanList);

		this.getPluginManager().registerCommand(null, cBan);
		this.getPluginManager().registerCommand(null, cUnban);
		this.getPluginManager().registerCommand(null, cBanReload);
		this.getPluginManager().registerCommand(null, cBanIP);
		this.getPluginManager().registerCommand(null, cBanWildcard);
		this.getPluginManager().registerCommand(null, cBanRegex);
		this.getPluginManager().registerCommand(null, cBanCheck);
		this.getPluginManager().registerCommand(null, cBanList);
	}

	public static void main(final String[] args) throws Exception {
		final BungeeCord bungee = new BungeeCord();
		ProxyServer.setInstance(bungee);
		bungee.getLogger().info("Enabled BungeeCord version " + bungee.getVersion());
		bungee.start();
		while (bungee.isRunning) {
			final String line = bungee.getConsoleReader().readLine(">");
			if (line != null && !bungee.getPluginManager().dispatchCommand(ConsoleCommandSender.getInstance(), line)) {
				bungee.getConsole().sendMessage(ChatColor.RED + "Command not found");
			}
		}
	}

	@Override
	public void start() throws Exception {
		this.pluginsFolder.mkdir();
		this.config.load();
		this.pluginManager.detectPlugins(this.pluginsFolder);
		this.pluginManager.addInternalPlugin(new PluginEaglerSkins());
		this.pluginManager.addInternalPlugin(new PluginEaglerVoice(this.config.getVoiceEnabled()));
		if (this.config.getAuthInfo().isEnabled()) {
			this.authSystem = new AuthSystem(this.config.getAuthInfo());
			this.getPluginManager().registerCommand(null, new CommandChangePassword(this.authSystem));
		}
		this.tokenVerify = Optional.ofNullable(System.getenv("YEEISH_TOKEN")).orElse(this.config.getTokenVerify());
		if (this.reconnectHandler == null) {
			this.reconnectHandler = new SQLReconnectHandler();
		}
		this.isRunning = true;
		this.pluginManager.loadAndEnablePlugins();
		this.startListeners();
		this.saveThread.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				BungeeCord.this.getReconnectHandler().save();
			}
		}, 0L, TimeUnit.MINUTES.toMillis(5L));
		this.reloadBanThread.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				BanList.maybeReloadBans(null);
			}
		}, 0L, TimeUnit.SECONDS.toMillis(3L));
		DomainBlacklist.init(this);
		this.closeInactiveSockets.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				DomainBlacklist.update();
				for(WebSocketListener lst : BungeeCord.this.wsListeners) {
					lst.closeInactiveSockets();
					ListenerInfo info = lst.getInfo();
					if(info.getRateLimitIP() != null) info.getRateLimitIP().deleteClearLimiters();
					if(info.getRateLimitLogin() != null) info.getRateLimitLogin().deleteClearLimiters();
					if(info.getRateLimitMOTD() != null) info.getRateLimitMOTD().deleteClearLimiters();
					if(info.getRateLimitQuery() != null) info.getRateLimitQuery().deleteClearLimiters();
				}
			}
		}, 0L, TimeUnit.SECONDS.toMillis(10L));
		final int authTimeout = this.config.getAuthInfo().getLoginTimeout();
		this.authTimeoutTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				AuthHandler.closeInactive(authTimeout);
			}
		}, 0L, TimeUnit.SECONDS.toMillis(2L));
	}

	public void startListeners() {
		for (final ListenerInfo info : this.config.getListeners()) {
			InetSocketAddress sock = info.getHost();
			if(info.isWebsocket()) {
				sock = info.getJavaHost();
				if(sock == null) {
					try {
						ServerSocket s = new ServerSocket(0, 0, InetAddress.getByName("127.11.0.1"));
						sock = new InetSocketAddress("127.11.0.1", s.getLocalPort());
						s.close();
					} catch(IOException e) {
						sock = new InetSocketAddress("127.11.0.1",(int) (System.nanoTime() % 64000L + 1025L));
					}
				}
				try {
					this.wsListeners.add(new WebSocketListener(info, sock, this));
					BungeeCord.this.getLogger().info("Listening websockets on " + info.getHost());
				}catch(Throwable t) {
					BungeeCord.this.getLogger().log(Level.WARNING, "Could not bind websocket listener to host " + info.getHost(), t);
				}
			}
			final InetSocketAddress sock2 = sock;
			final ChannelFutureListener listener = (ChannelFutureListener) new ChannelFutureListener() {
				public void operationComplete(final ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						BungeeCord.this.listeners.add(future.channel());
						BungeeCord.this.getLogger().info("Listening on " + sock2);
					} else {
						BungeeCord.this.getLogger().log(Level.WARNING, "Could not bind to host " + sock2, future.cause());
					}
				}
			};
			((ServerBootstrap) ((ServerBootstrap) new ServerBootstrap().channel((Class) NioServerSocketChannel.class)).childAttr((AttributeKey) PipelineUtils.LISTENER, (Object) info).childHandler((ChannelHandler) PipelineUtils.SERVER_CHILD)
					.group((EventLoopGroup) this.eventLoops).localAddress((SocketAddress) sock)).bind().addListener((GenericFutureListener) listener);
		}
	}

	public void stopListeners() {
		for (final Channel listener : this.listeners) {
			this.getLogger().log(Level.INFO, "Closing listener {0}", listener);
			try {
				listener.close().syncUninterruptibly();
			} catch (ChannelException ex) {
				this.getLogger().severe("Could not close listen thread");
			}
		}
		for (final WebSocketListener listener : this.wsListeners) {
			this.getLogger().log(Level.INFO, "Closing websocket listener {0}", listener.getAddress());
			try {
				listener.stop();
			}catch (IOException e) {
				this.getLogger().severe("Could not close listen thread");
				e.printStackTrace();
			} catch (InterruptedException e) {
				this.getLogger().severe("Could not close listen thread");
				e.printStackTrace();
			}
		}
		this.listeners.clear();
	}

	@Override
	public void stop() {
		new Thread("Shutdown Thread") {
			@Override
			public void run() {
				BungeeCord.this.isRunning = false;
				BungeeCord.this.executors.shutdown();
				BungeeCord.this.stopListeners();
				BungeeCord.this.getLogger().info("Closing pending connections");
				BungeeCord.this.connectionLock.readLock().lock();
				try {
					BungeeCord.this.getLogger().info("Disconnecting " + BungeeCord.this.connections.size() + " connections");
					for (final UserConnection user : BungeeCord.this.connections.values()) {
						user.disconnect(BungeeCord.this.getTranslation("restart"));
					}
				} finally {
					BungeeCord.this.connectionLock.readLock().unlock();
				}
				BungeeCord.this.getLogger().info("Closing IO threads");
				BungeeCord.this.eventLoops.shutdownGracefully();
				try {
					BungeeCord.this.eventLoops.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				} catch (InterruptedException ex) {
				}
				BungeeCord.this.getLogger().info("Saving reconnect locations");
				BungeeCord.this.reconnectHandler.save();
				BungeeCord.this.reconnectHandler.close();
				BungeeCord.this.saveThread.cancel();
				BungeeCord.this.reloadBanThread.cancel();
				BungeeCord.this.closeInactiveSockets.cancel();
				BungeeCord.this.authTimeoutTimer.cancel();
				BungeeCord.this.getLogger().info("Disabling plugins");
				for (final Plugin plugin : BungeeCord.this.pluginManager.getPlugins()) {
					plugin.onDisable();
					BungeeCord.this.getScheduler().cancel(plugin);
				}
				BungeeCord.this.getLogger().info("Thankyou and goodbye");
				System.exit(0);
			}
		}.start();
	}

	public void broadcast(final DefinedPacket packet) {
		this.connectionLock.readLock().lock();
		try {
			for (final UserConnection con : this.connections.values()) {
				con.unsafe().sendPacket(packet);
			}
		} finally {
			this.connectionLock.readLock().unlock();
		}
	}

	@Override
	public String getName() {
		return "BungeeCord";
	}

	@Override
	public String getVersion() {
		return (BungeeCord.class.getPackage().getImplementationVersion() == null) ? "unknown" : BungeeCord.class.getPackage().getImplementationVersion();
	}

	@Override
	public String getTranslation(final String name) {
		String translation = "<translation '" + name + "' missing>";
		try {
			translation = this.bundle.getString(name);
		} catch (MissingResourceException ex) {
		}
		return translation;
	}

	@Override
	public Collection<ProxiedPlayer> getPlayers() {
		this.connectionLock.readLock().lock();
		try {
			return new HashSet<ProxiedPlayer>(this.connections.values());
		} finally {
			this.connectionLock.readLock().unlock();
		}
	}

	@Override
	public int getOnlineCount() {
		return this.connections.size();
	}

	@Override
	public ProxiedPlayer getPlayer(final String name) {
		this.connectionLock.readLock().lock();
		try {
			return this.connections.get(name);
		} finally {
			this.connectionLock.readLock().unlock();
		}
	}

	@Override
	public Map<String, ServerInfo> getServers() {
		return (Map<String, ServerInfo>) this.config.getServers();
	}

	@Override
	public ServerInfo getServerInfo(final String name) {
		return this.getServers().get(name);
	}

	@Override
	public void registerChannel(final String channel) {
		synchronized (this.pluginChannels) {
			this.pluginChannels.add(channel);
		}
	}

	@Override
	public void unregisterChannel(final String channel) {
		synchronized (this.pluginChannels) {
			this.pluginChannels.remove(channel);
		}
	}

	@Override
	public Collection<String> getChannels() {
		synchronized (this.pluginChannels) {
			return Collections.unmodifiableCollection((Collection<? extends String>) this.pluginChannels);
		}
	}

	public PacketFAPluginMessage registerChannels() {
		return new PacketFAPluginMessage("REGISTER", Util.format(this.pluginChannels, "\u0000").getBytes());
	}

	@Override
	public byte getProtocolVersion() {
		return 61;
	}

	@Override
	public String getGameVersion() {
		return "1.5.2";
	}

	@Override
	public ServerInfo constructServerInfo(final String name, final InetSocketAddress address, final boolean restricted) {
		return new BungeeServerInfo(name, address, restricted);
	}

	@Override
	public CommandSender getConsole() {
		return ConsoleCommandSender.getInstance();
	}

	@Override
	public void broadcast(final String message) {
		this.getConsole().sendMessage(message);
		this.broadcast(new Packet3Chat(message));
	}

	public void addConnection(final UserConnection con) {
		this.connectionLock.writeLock().lock();
		try {
			this.connections.put(con.getName(), con);
		} finally {
			this.connectionLock.writeLock().unlock();
		}
	}

	public void removeConnection(final UserConnection con) {
		this.connectionLock.writeLock().lock();
		try {
			this.connections.remove(con.getName());
		} finally {
			this.connectionLock.writeLock().unlock();
		}
	}

	@Override
	public CustomTabList customTabList(final ProxiedPlayer player) {
		return new Custom(player);
	}

	@Override
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	@Override
	public ReconnectHandler getReconnectHandler() {
		return this.reconnectHandler;
	}

	@Override
	public void setReconnectHandler(final ReconnectHandler reconnectHandler) {
		this.reconnectHandler = reconnectHandler;
	}

	@Override
	public ConfigurationAdapter getConfigurationAdapter() {
		return this.configurationAdapter;
	}

	@Override
	public void setConfigurationAdapter(final ConfigurationAdapter configurationAdapter) {
		this.configurationAdapter = configurationAdapter;
	}

	@Override
	public File getPluginsFolder() {
		return this.pluginsFolder;
	}

	@Override
	public TaskScheduler getScheduler() {
		return this.scheduler;
	}

	public ConsoleReader getConsoleReader() {
		return this.consoleReader;
	}

	@Override
	public Logger getLogger() {
		return this.logger;
	}
}
