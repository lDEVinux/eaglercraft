// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.md_5.bungee.api.ProxyServer;

public class BungeeThreadPool extends ScheduledThreadPoolExecutor {
	public BungeeThreadPool(final ThreadFactory threadFactory) {
		super(Integer.MAX_VALUE, threadFactory);
		this.setKeepAliveTime(5L, TimeUnit.MINUTES);
		this.allowCoreThreadTimeOut(true);
	}

	@Override
	protected void afterExecute(final Runnable r, final Throwable t) {
		super.afterExecute(r, t);
		if (t != null) {
			ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Task caused exception whilst running", t);
		}
	}
}
