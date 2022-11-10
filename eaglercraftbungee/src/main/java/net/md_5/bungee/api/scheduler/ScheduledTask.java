// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.scheduler;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.plugin.Plugin;

public interface ScheduledTask {
	int getId();

	Plugin getOwner();

	Runnable getTask();

	long getDelay(final TimeUnit p0);
}
