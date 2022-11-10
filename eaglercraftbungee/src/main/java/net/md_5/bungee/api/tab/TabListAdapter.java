// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.tab;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class TabListAdapter implements TabListHandler {
	private ProxiedPlayer player;

	@Override
	public void init(final ProxiedPlayer player) {
		this.player = player;
	}

	@Override
	public void onConnect() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public void onServerChange() {
	}

	@Override
	public void onPingChange(final int ping) {
	}

	public ProxiedPlayer getPlayer() {
		return this.player;
	}
}
