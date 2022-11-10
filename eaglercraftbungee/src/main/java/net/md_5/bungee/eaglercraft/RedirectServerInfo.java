package net.md_5.bungee.eaglercraft;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RedirectServerInfo implements ServerInfo {

	private final String serverName;
	private final String serverRedirect;
	private final boolean restricted;
	
	@Override
	public String getName() {
		return serverName;
	}

	@Override
	public InetSocketAddress getAddress() {
		return null;
	}

	@Override
	public String getRedirect() {
		return serverRedirect;
	}

	@Override
	public Collection<ProxiedPlayer> getPlayers() {
		return Collections.emptyList();
	}

	@Override
	public boolean canAccess(CommandSender p0) {
		Preconditions.checkNotNull((Object) p0, (Object) "player");
		return !this.restricted || p0.hasPermission("bungeecord.server." + serverName);
	}

	@Override
	public void sendData(String p0, byte[] p1) {
		
	}

	@Override
	public void ping(Callback<ServerPing> p0) {
		p0.done(null, new UnsupportedOperationException("Cannot ping a redirect server!"));
	}

	public RedirectServerInfo(String serverName, String serverRedirect, boolean restricted) {
		this.serverName = serverName;
		this.serverRedirect = serverRedirect;
		this.restricted = restricted;
	}

}
