// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import java.util.Arrays;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.plugin.Cancellable;

public class PluginMessageEvent extends TargetedEvent implements Cancellable {
	private boolean cancelled;
	private final String tag;
	private final byte[] data;

	public PluginMessageEvent(final Connection sender, final Connection receiver, final String tag, final byte[] data) {
		super(sender, receiver);
		this.tag = tag;
		this.data = data;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public String getTag() {
		return this.tag;
	}

	public byte[] getData() {
		return this.data;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public String toString() {
		return "PluginMessageEvent(super=" + super.toString() + ", cancelled=" + this.isCancelled() + ", tag=" + this.getTag() + ", data=" + Arrays.toString(this.getData()) + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PluginMessageEvent)) {
			return false;
		}
		final PluginMessageEvent other = (PluginMessageEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.isCancelled() != other.isCancelled()) {
			return false;
		}
		final Object this$tag = this.getTag();
		final Object other$tag = other.getTag();
		if (this$tag == null) {
			if (other$tag == null) {
				return Arrays.equals(this.getData(), other.getData());
			}
		} else if (this$tag.equals(other$tag)) {
			return Arrays.equals(this.getData(), other.getData());
		}
		return false;
	}

	@Override
	public boolean canEqual(final Object other) {
		return other instanceof PluginMessageEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + super.hashCode();
		result = result * 31 + (this.isCancelled() ? 1231 : 1237);
		final Object $tag = this.getTag();
		result = result * 31 + (($tag == null) ? 0 : $tag.hashCode());
		result = result * 31 + Arrays.hashCode(this.getData());
		return result;
	}
}
