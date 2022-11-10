// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class ServerConnectEvent extends Event implements Cancellable {
	private final ProxiedPlayer player;
	private ServerInfo target;
	private boolean cancelled;

	public ServerConnectEvent(final ProxiedPlayer player, final ServerInfo target) {
		this.player = player;
		this.target = target;
	}

	public ProxiedPlayer getPlayer() {
		return this.player;
	}

	public ServerInfo getTarget() {
		return this.target;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setTarget(final ServerInfo target) {
		this.target = target;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public String toString() {
		return "ServerConnectEvent(player=" + this.getPlayer() + ", target=" + this.getTarget() + ", cancelled=" + this.isCancelled() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ServerConnectEvent)) {
			return false;
		}
		final ServerConnectEvent other = (ServerConnectEvent) o;
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
		final Object this$target = this.getTarget();
		final Object other$target = other.getTarget();
		if (this$target == null) {
			if (other$target == null) {
				return this.isCancelled() == other.isCancelled();
			}
		} else if (this$target.equals(other$target)) {
			return this.isCancelled() == other.isCancelled();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof ServerConnectEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $player = this.getPlayer();
		result = result * 31 + (($player == null) ? 0 : $player.hashCode());
		final Object $target = this.getTarget();
		result = result * 31 + (($target == null) ? 0 : $target.hashCode());
		result = result * 31 + (this.isCancelled() ? 1231 : 1237);
		return result;
	}
}
