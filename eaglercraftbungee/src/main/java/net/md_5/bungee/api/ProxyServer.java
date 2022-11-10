// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.api.tab.CustomTabList;

public abstract class ProxyServer {
	private static ProxyServer instance;

	public static void setInstance(final ProxyServer instance) {
		Preconditions.checkNotNull((Object) instance, (Object) "instance");
		Preconditions.checkArgument(ProxyServer.instance == null, (Object) "Instance already set");
		ProxyServer.instance = instance;
	}

	public abstract String getName();

	public abstract String getVersion();

	public abstract String getTranslation(final String p0);

	public abstract Logger getLogger();

	public abstract Collection<ProxiedPlayer> getPlayers();

	public abstract ProxiedPlayer getPlayer(final String p0);

	public abstract Map<String, ServerInfo> getServers();

	public abstract ServerInfo getServerInfo(final String p0);

	public abstract PluginManager getPluginManager();

	public abstract ConfigurationAdapter getConfigurationAdapter();

	public abstract void setConfigurationAdapter(final ConfigurationAdapter p0);

	public abstract ReconnectHandler getReconnectHandler();

	public abstract void setReconnectHandler(final ReconnectHandler p0);

	public abstract void stop();

	public abstract void start() throws Exception;

	public abstract void registerChannel(final String p0);

	public abstract void unregisterChannel(final String p0);

	public abstract Collection<String> getChannels();

	public abstract String getGameVersion();

	public abstract byte getProtocolVersion();

	public abstract ServerInfo constructServerInfo(final String p0, final InetSocketAddress p1, final boolean p2);

	public abstract CommandSender getConsole();

	public abstract File getPluginsFolder();

	public abstract TaskScheduler getScheduler();

	public abstract int getOnlineCount();

	public abstract void broadcast(final String p0);

	public abstract CustomTabList customTabList(final ProxiedPlayer p0);

	public static ProxyServer getInstance() {
		return ProxyServer.instance;
	}
}
