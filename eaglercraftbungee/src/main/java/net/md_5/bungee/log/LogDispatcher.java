// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

public class LogDispatcher extends Thread {
	private final BungeeLogger logger;
	private final BlockingQueue<LogRecord> queue;

	public LogDispatcher(final BungeeLogger logger) {
		super("BungeeCord Logger Thread - " + logger);
		this.queue = new LinkedBlockingQueue<LogRecord>();
		this.logger = logger;
	}

	@Override
	public void run() {
		while (!this.isInterrupted()) {
			LogRecord record;
			try {
				record = this.queue.take();
			} catch (InterruptedException ex) {
				continue;
			}
			this.logger.doLog(record);
		}
		for (final LogRecord record2 : this.queue) {
			this.logger.doLog(record2);
		}
	}

	public void queue(final LogRecord record) {
		if (!this.isInterrupted()) {
			this.queue.add(record);
		}
	}
}
