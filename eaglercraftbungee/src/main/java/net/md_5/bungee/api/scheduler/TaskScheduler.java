// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.scheduler;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.plugin.Plugin;

public interface TaskScheduler {
	void cancel(final int p0);

	void cancel(final ScheduledTask p0);

	int cancel(final Plugin p0);

	ScheduledTask runAsync(final Plugin p0, final Runnable p1);

	ScheduledTask schedule(final Plugin p0, final Runnable p1, final long p2, final TimeUnit p3);

	ScheduledTask schedule(final Plugin p0, final Runnable p1, final long p2, final long p3, final TimeUnit p4);
}
