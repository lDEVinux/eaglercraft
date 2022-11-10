// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import java.beans.ConstructorProperties;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class PlayerDisconnectEvent extends Event {
	private final ProxiedPlayer player;

	@ConstructorProperties({ "player" })
	public PlayerDisconnectEvent(final ProxiedPlayer player) {
		this.player = player;
	}

	public ProxiedPlayer getPlayer() {
		return this.player;
	}

	@Override
	public String toString() {
		return "PlayerDisconnectEvent(player=" + this.getPlayer() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PlayerDisconnectEvent)) {
			return false;
		}
		final PlayerDisconnectEvent other = (PlayerDisconnectEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$player = this.getPlayer();
		final Object other$player = other.getPlayer();
		if (this$player == null) {
			if (other$player == null) {
				return true;
			}
		} else if (this$player.equals(other$player)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PlayerDisconnectEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $player = this.getPlayer();
		result = result * 31 + (($player == null) ? 0 : $player.hashCode());
		return result;
	}
}
