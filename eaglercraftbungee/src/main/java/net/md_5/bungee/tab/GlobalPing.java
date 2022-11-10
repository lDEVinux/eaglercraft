// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.tab;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.protocol.packet.PacketC9PlayerListItem;

public class GlobalPing extends Global {
	private static final int PING_THRESHOLD = 20;
	private int lastPing;

	@Override
	public void onPingChange(final int ping) {
		if (ping - 20 > this.lastPing && ping + 20 < this.lastPing) {
			this.lastPing = ping;
			BungeeCord.getInstance().broadcast(new PacketC9PlayerListItem(this.getPlayer().getDisplayName(), true, (short) ping));
		}
	}
}
