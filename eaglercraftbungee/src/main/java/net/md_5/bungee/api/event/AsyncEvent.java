// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import java.beans.ConstructorProperties;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;

public class AsyncEvent<T> extends Event {
	private final Callback<T> done;
	private final Set<Plugin> intents;
	private final AtomicBoolean fired;
	private final AtomicInteger latch;

	@Override
	public void postCall() {
		this.fired.set(true);
		if (this.latch.get() == 0) {
			this.done.done((T) this, null);
		}
	}

	public void registerIntent(final Plugin plugin) {
		Preconditions.checkState(!this.fired.get(), "Event %s has already been fired", new Object[] { this });
		Preconditions.checkState(!this.intents.contains(plugin), "Plugin %s already registered intent for event %s", new Object[] { plugin, this });
		this.intents.add(plugin);
		this.latch.incrementAndGet();
	}

	public void completeIntent(final Plugin plugin) {
		Preconditions.checkState(this.intents.contains(plugin), "Plugin %s has not registered intent for event %s", new Object[] { plugin, this });
		this.intents.remove(plugin);
		if (this.latch.decrementAndGet() == 0 && this.fired.get()) {
			this.done.done((T) this, null);
		}
	}

	@ConstructorProperties({ "done" })
	public AsyncEvent(final Callback<T> done) {
		this.intents = Collections.newSetFromMap(new ConcurrentHashMap<Plugin, Boolean>());
		this.fired = new AtomicBoolean();
		this.latch = new AtomicInteger();
		this.done = done;
	}

	public Callback<T> getDone() {
		return this.done;
	}

	public Set<Plugin> getIntents() {
		return this.intents;
	}

	public AtomicBoolean getFired() {
		return this.fired;
	}

	public AtomicInteger getLatch() {
		return this.latch;
	}

	@Override
	public String toString() {
		return "AsyncEvent(super=" + super.toString() + ", done=" + this.getDone() + ", intents=" + this.getIntents() + ", fired=" + this.getFired() + ", latch=" + this.getLatch() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AsyncEvent)) {
			return false;
		}
		final AsyncEvent<?> other = (AsyncEvent<?>) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		final Object this$done = this.getDone();
		final Object other$done = other.getDone();
		Label_0075: {
			if (this$done == null) {
				if (other$done == null) {
					break Label_0075;
				}
			} else if (this$done.equals(other$done)) {
				break Label_0075;
			}
			return false;
		}
		final Object this$intents = this.getIntents();
		final Object other$intents = other.getIntents();
		Label_0112: {
			if (this$intents == null) {
				if (other$intents == null) {
					break Label_0112;
				}
			} else if (this$intents.equals(other$intents)) {
				break Label_0112;
			}
			return false;
		}
		final Object this$fired = this.getFired();
		final Object other$fired = other.getFired();
		Label_0149: {
			if (this$fired == null) {
				if (other$fired == null) {
					break Label_0149;
				}
			} else if (this$fired.equals(other$fired)) {
				break Label_0149;
			}
			return false;
		}
		final Object this$latch = this.getLatch();
		final Object other$latch = other.getLatch();
		if (this$latch == null) {
			if (other$latch == null) {
				return true;
			}
		} else if (this$latch.equals(other$latch)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof AsyncEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + super.hashCode();
		final Object $done = this.getDone();
		result = result * 31 + (($done == null) ? 0 : $done.hashCode());
		final Object $intents = this.getIntents();
		result = result * 31 + (($intents == null) ? 0 : $intents.hashCode());
		final Object $fired = this.getFired();
		result = result * 31 + (($fired == null) ? 0 : $fired.hashCode());
		final Object $latch = this.getLatch();
		result = result * 31 + (($latch == null) ? 0 : $latch.hashCode());
		return result;
	}
}
