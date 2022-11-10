// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.tab;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface TabListHandler {
	void init(final ProxiedPlayer p0);

	void onConnect();

	void onServerChange();

	void onPingChange(final int p0);

	void onDisconnect();

	boolean onListUpdate(final String p0, final boolean p1, final int p2);
}
