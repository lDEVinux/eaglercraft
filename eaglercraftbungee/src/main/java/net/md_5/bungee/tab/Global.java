// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.tab;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.TabListAdapter;
import net.md_5.bungee.protocol.packet.PacketC9PlayerListItem;

public class Global extends TabListAdapter {
	private boolean sentPing;

	@Override
	public void onConnect() {
		for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			this.getPlayer().unsafe().sendPacket(new PacketC9PlayerListItem(p.getDisplayName(), true, (short) p.getPing()));
		}
		BungeeCord.getInstance().broadcast(new PacketC9PlayerListItem(this.getPlayer().getDisplayName(), true, (short) this.getPlayer().getPing()));
	}

	@Override
	public void onPingChange(final int ping) {
		if (!this.sentPing) {
			this.sentPing = true;
			BungeeCord.getInstance().broadcast(new PacketC9PlayerListItem(this.getPlayer().getDisplayName(), true, (short) this.getPlayer().getPing()));
		}
	}

	@Override
	public void onDisconnect() {
		BungeeCord.getInstance().broadcast(new PacketC9PlayerListItem(this.getPlayer().getDisplayName(), false, (short) 9999));
	}

	@Override
	public boolean onListUpdate(final String name, final boolean online, final int ping) {
		return false;
	}
}
