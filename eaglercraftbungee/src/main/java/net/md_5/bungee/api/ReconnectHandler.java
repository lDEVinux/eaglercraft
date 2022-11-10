// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface ReconnectHandler {
	ServerInfo getServer(final ProxiedPlayer p0);

	void setServer(final ProxiedPlayer p0);

	void save();

	void close();
}
