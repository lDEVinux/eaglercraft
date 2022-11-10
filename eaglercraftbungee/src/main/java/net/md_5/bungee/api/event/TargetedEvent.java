// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import java.beans.ConstructorProperties;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.plugin.Event;

public abstract class TargetedEvent extends Event {
	private final Connection sender;
	private final Connection receiver;

	public Connection getSender() {
		return this.sender;
	}

	public Connection getReceiver() {
		return this.receiver;
	}

	@Override
	public String toString() {
		return "TargetedEvent(sender=" + this.getSender() + ", receiver=" + this.getReceiver() + ")";
	}

	@ConstructorProperties({ "sender", "receiver" })
	public TargetedEvent(final Connection sender, final Connection receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof TargetedEvent)) {
			return false;
		}
		final TargetedEvent other = (TargetedEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$sender = this.getSender();
		final Object other$sender = other.getSender();
		Label_0065: {
			if (this$sender == null) {
				if (other$sender == null) {
					break Label_0065;
				}
			} else if (this$sender.equals(other$sender)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$receiver = this.getReceiver();
		final Object other$receiver = other.getReceiver();
		if (this$receiver == null) {
			if (other$receiver == null) {
				return true;
			}
		} else if (this$receiver.equals(other$receiver)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof TargetedEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $sender = this.getSender();
		result = result * 31 + (($sender == null) ? 0 : $sender.hashCode());
		final Object $receiver = this.getReceiver();
		result = result * 31 + (($receiver == null) ? 0 : $receiver.hashCode());
		return result;
	}
}
