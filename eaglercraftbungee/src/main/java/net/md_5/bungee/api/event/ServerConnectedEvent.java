// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import java.beans.ConstructorProperties;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Event;

public class ServerConnectedEvent extends Event {
	private final ProxiedPlayer player;
	private final Server server;

	@ConstructorProperties({ "player", "server" })
	public ServerConnectedEvent(final ProxiedPlayer player, final Server server) {
		this.player = player;
		this.server = server;
	}

	public ProxiedPlayer getPlayer() {
		return this.player;
	}

	public Server getServer() {
		return this.server;
	}

	@Override
	public String toString() {
		return "ServerConnectedEvent(player=" + this.getPlayer() + ", server=" + this.getServer() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ServerConnectedEvent)) {
			return false;
		}
		final ServerConnectedEvent other = (ServerConnectedEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$player = this.getPlayer();
		final Object other$player = other.getPlayer();
		Label_0065: {
			if (this$player == null) {
				if (other$player == null) {
					break Label_0065;
				}
			} else if (this$player.equals(other$player)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$server = this.getServer();
		final Object other$server = other.getServer();
		if (this$server == null) {
			if (other$server == null) {
				return true;
			}
		} else if (this$server.equals(other$server)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof ServerConnectedEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $player = this.getPlayer();
		result = result * 31 + (($player == null) ? 0 : $player.hashCode());
		final Object $server = this.getServer();
		result = result * 31 + (($server == null) ? 0 : $server.hashCode());
		return result;
	}
}
