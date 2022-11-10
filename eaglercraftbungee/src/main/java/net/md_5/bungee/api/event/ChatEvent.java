// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.plugin.Cancellable;

public class ChatEvent extends TargetedEvent implements Cancellable {
	private boolean cancelled;
	private String message;

	public ChatEvent(final Connection sender, final Connection receiver, final String message) {
		super(sender, receiver);
		this.message = message;
	}

	public boolean isCommand() {
		return this.message.length() > 0 && this.message.charAt(0) == '/';
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ChatEvent(super=" + super.toString() + ", cancelled=" + this.isCancelled() + ", message=" + this.getMessage() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ChatEvent)) {
			return false;
		}
		final ChatEvent other = (ChatEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.isCancelled() != other.isCancelled()) {
			return false;
		}
		final Object this$message = this.getMessage();
		final Object other$message = other.getMessage();
		if (this$message == null) {
			if (other$message == null) {
				return true;
			}
		} else if (this$message.equals(other$message)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEqual(final Object other) {
		return other instanceof ChatEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + super.hashCode();
		result = result * 31 + (this.isCancelled() ? 1231 : 1237);
		final Object $message = this.getMessage();
		result = result * 31 + (($message == null) ? 0 : $message.hashCode());
		return result;
	}
}
