// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class BungeeScheduler implements TaskScheduler {
	private final AtomicInteger taskCounter;
	private final TIntObjectMap<BungeeTask> tasks;
	private final Multimap<Plugin, BungeeTask> tasksByPlugin;

	public BungeeScheduler() {
		this.taskCounter = new AtomicInteger();
		this.tasks = (TIntObjectMap<BungeeTask>) TCollections.synchronizedMap((TIntObjectMap) new TIntObjectHashMap());
		this.tasksByPlugin = (Multimap<Plugin, BungeeTask>) Multimaps.synchronizedMultimap((Multimap) HashMultimap.create());
	}

	@Override
	public void cancel(final int id) {
		final BungeeTask task = (BungeeTask) this.tasks.remove(id);
		this.tasksByPlugin.values().remove(task);
		task.getFuture().cancel(false);
	}

	@Override
	public void cancel(final ScheduledTask task) {
		this.cancel(task.getId());
	}

	@Override
	public int cancel(final Plugin plugin) {
		final Set<ScheduledTask> toRemove = new HashSet<ScheduledTask>();
		for (final ScheduledTask task : this.tasksByPlugin.get((Plugin) plugin)) {
			toRemove.add(task);
		}
		for (final ScheduledTask task : toRemove) {
			this.cancel(task);
		}
		return toRemove.size();
	}

	@Override
	public ScheduledTask runAsync(final Plugin owner, final Runnable task) {
		return this.schedule(owner, task, 0L, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledTask schedule(final Plugin owner, final Runnable task, final long delay, final TimeUnit unit) {
		return this.prepare(owner, task).setFuture(BungeeCord.getInstance().executors.schedule(task, delay, unit));
	}

	@Override
	public ScheduledTask schedule(final Plugin owner, final Runnable task, final long delay, final long period, final TimeUnit unit) {
		return this.prepare(owner, task).setFuture(BungeeCord.getInstance().executors.scheduleWithFixedDelay(task, delay, period, unit));
	}

	private BungeeTask prepare(final Plugin owner, final Runnable task) {
		Preconditions.checkNotNull((Object) owner, (Object) "owner");
		Preconditions.checkNotNull((Object) task, (Object) "task");
		final BungeeTask prepared = new BungeeTask(this.taskCounter.getAndIncrement(), owner, task);
		this.tasks.put(prepared.getId(), (BungeeTask) prepared);
		return prepared;
	}
}
