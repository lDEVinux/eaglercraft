// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.scheduler;

import java.beans.ConstructorProperties;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeTask implements ScheduledTask {
	private final int id;
	private final Plugin owner;
	private final Runnable task;
	private ScheduledFuture<?> future;

	@Override
	public long getDelay(final TimeUnit unit) {
		return this.future.getDelay(unit);
	}

	BungeeTask setFuture(final ScheduledFuture<?> future) {
		this.future = future;
		return this;
	}

	@ConstructorProperties({ "id", "owner", "task" })
	public BungeeTask(final int id, final Plugin owner, final Runnable task) {
		this.id = id;
		this.owner = owner;
		this.task = task;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Plugin getOwner() {
		return this.owner;
	}

	@Override
	public Runnable getTask() {
		return this.task;
	}

	public ScheduledFuture<?> getFuture() {
		return this.future;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BungeeTask)) {
			return false;
		}
		final BungeeTask other = (BungeeTask) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getId() != other.getId()) {
			return false;
		}
		final Object this$owner = this.getOwner();
		final Object other$owner = other.getOwner();
		Label_0078: {
			if (this$owner == null) {
				if (other$owner == null) {
					break Label_0078;
				}
			} else if (this$owner.equals(other$owner)) {
				break Label_0078;
			}
			return false;
		}
		final Object this$task = this.getTask();
		final Object other$task = other.getTask();
		Label_0115: {
			if (this$task == null) {
				if (other$task == null) {
					break Label_0115;
				}
			} else if (this$task.equals(other$task)) {
				break Label_0115;
			}
			return false;
		}
		final Object this$future = this.getFuture();
		final Object other$future = other.getFuture();
		if (this$future == null) {
			if (other$future == null) {
				return true;
			}
		} else if (this$future.equals(other$future)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof BungeeTask;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.getId();
		final Object $owner = this.getOwner();
		result = result * 31 + (($owner == null) ? 0 : $owner.hashCode());
		final Object $task = this.getTask();
		result = result * 31 + (($task == null) ? 0 : $task.hashCode());
		final Object $future = this.getFuture();
		result = result * 31 + (($future == null) ? 0 : $future.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "BungeeTask(id=" + this.getId() + ", owner=" + this.getOwner() + ", task=" + this.getTask() + ", future=" + this.getFuture() + ")";
	}
}
