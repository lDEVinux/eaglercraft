// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.plugin.Cancellable;

public class LoginEvent extends AsyncEvent<LoginEvent> implements Cancellable {
	private boolean cancelled;
	private String cancelReason;
	private final PendingConnection connection;

	public LoginEvent(final PendingConnection connection, final Callback<LoginEvent> done) {
		super(done);
		this.connection = connection;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public String getCancelReason() {
		return this.cancelReason;
	}

	public PendingConnection getConnection() {
		return this.connection;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setCancelReason(final String cancelReason) {
		this.cancelReason = cancelReason;
	}

	@Override
	public String toString() {
		return "LoginEvent(cancelled=" + this.isCancelled() + ", cancelReason=" + this.getCancelReason() + ", connection=" + this.getConnection() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LoginEvent)) {
			return false;
		}
		final LoginEvent other = (LoginEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.isCancelled() != other.isCancelled()) {
			return false;
		}
		final Object this$cancelReason = this.getCancelReason();
		final Object other$cancelReason = other.getCancelReason();
		Label_0078: {
			if (this$cancelReason == null) {
				if (other$cancelReason == null) {
					break Label_0078;
				}
			} else if (this$cancelReason.equals(other$cancelReason)) {
				break Label_0078;
			}
			return false;
		}
		final Object this$connection = this.getConnection();
		final Object other$connection = other.getConnection();
		if (this$connection == null) {
			if (other$connection == null) {
				return true;
			}
		} else if (this$connection.equals(other$connection)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEqual(final Object other) {
		return other instanceof LoginEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + (this.isCancelled() ? 1231 : 1237);
		final Object $cancelReason = this.getCancelReason();
		result = result * 31 + (($cancelReason == null) ? 0 : $cancelReason.hashCode());
		final Object $connection = this.getConnection();
		result = result * 31 + (($connection == null) ? 0 : $connection.hashCode());
		return result;
	}
}
