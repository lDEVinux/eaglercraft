// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.reconnect;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class AbstractReconnectManager implements ReconnectHandler {
	@Override
	public ServerInfo getServer(final ProxiedPlayer player) {
		final ListenerInfo listener = player.getPendingConnection().getListener();
		String forced = listener.getForcedHosts().get(player.getPendingConnection().getVirtualHost().getHostString());
		if (forced == null && listener.isForceDefault()) {
			forced = listener.getDefaultServer();
		}
		final String server = (forced == null) ? this.getStoredServer(player) : forced;
		final String name = (server != null) ? server : listener.getDefaultServer();
		ServerInfo info = ProxyServer.getInstance().getServerInfo(name);
		if (info == null) {
			info = ProxyServer.getInstance().getServerInfo(listener.getDefaultServer());
		}
		Preconditions.checkState(info != null, (Object) "Default server not defined");
		return info;
	}

	protected abstract String getStoredServer(final ProxiedPlayer p0);
}
