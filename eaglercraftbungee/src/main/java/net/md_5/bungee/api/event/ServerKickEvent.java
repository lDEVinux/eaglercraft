// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class ServerKickEvent extends Event implements Cancellable {
	private boolean cancelled;
	private final ProxiedPlayer player;
	private String kickReason;
	private ServerInfo cancelServer;

	public ServerKickEvent(final ProxiedPlayer player, final String kickReason, final ServerInfo cancelServer) {
		this.player = player;
		this.kickReason = kickReason;
		this.cancelServer = cancelServer;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public ProxiedPlayer getPlayer() {
		return this.player;
	}

	public String getKickReason() {
		return this.kickReason;
	}

	public ServerInfo getCancelServer() {
		return this.cancelServer;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setKickReason(final String kickReason) {
		this.kickReason = kickReason;
	}

	public void setCancelServer(final ServerInfo cancelServer) {
		this.cancelServer = cancelServer;
	}

	@Override
	public String toString() {
		return "ServerKickEvent(cancelled=" + this.isCancelled() + ", player=" + this.getPlayer() + ", kickReason=" + this.getKickReason() + ", cancelServer=" + this.getCancelServer() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ServerKickEvent)) {
			return false;
		}
		final ServerKickEvent other = (ServerKickEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.isCancelled() != other.isCancelled()) {
			return false;
		}
		final Object this$player = this.getPlayer();
		final Object other$player = other.getPlayer();
		Label_0078: {
			if (this$player == null) {
				if (other$player == null) {
					break Label_0078;
				}
			} else if (this$player.equals(other$player)) {
				break Label_0078;
			}
			return false;
		}
		final Object this$kickReason = this.getKickReason();
		final Object other$kickReason = other.getKickReason();
		Label_0115: {
			if (this$kickReason == null) {
				if (other$kickReason == null) {
					break Label_0115;
				}
			} else if (this$kickReason.equals(other$kickReason)) {
				break Label_0115;
			}
			return false;
		}
		final Object this$cancelServer = this.getCancelServer();
		final Object other$cancelServer = other.getCancelServer();
		if (this$cancelServer == null) {
			if (other$cancelServer == null) {
				return true;
			}
		} else if (this$cancelServer.equals(other$cancelServer)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof ServerKickEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + (this.isCancelled() ? 1231 : 1237);
		final Object $player = this.getPlayer();
		result = result * 31 + (($player == null) ? 0 : $player.hashCode());
		final Object $kickReason = this.getKickReason();
		result = result * 31 + (($kickReason == null) ? 0 : $kickReason.hashCode());
		final Object $cancelServer = this.getCancelServer();
		result = result * 31 + (($cancelServer == null) ? 0 : $cancelServer.hashCode());
		return result;
	}
}
