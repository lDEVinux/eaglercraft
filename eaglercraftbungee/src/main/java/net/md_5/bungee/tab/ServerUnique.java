// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.tab;

import java.util.Collection;
import java.util.HashSet;

import net.md_5.bungee.api.tab.TabListAdapter;
import net.md_5.bungee.protocol.packet.PacketC9PlayerListItem;

public class ServerUnique extends TabListAdapter {
	private final Collection<String> usernames;

	public ServerUnique() {
		this.usernames = new HashSet<String>();
	}

	@Override
	public void onServerChange() {
		synchronized (this.usernames) {
			for (final String username : this.usernames) {
				this.getPlayer().unsafe().sendPacket(new PacketC9PlayerListItem(username, false, (short) 9999));
			}
			this.usernames.clear();
		}
	}

	@Override
	public boolean onListUpdate(final String name, final boolean online, final int ping) {
		if (online) {
			this.usernames.add(name);
		} else {
			this.usernames.remove(name);
		}
		return true;
	}
}
