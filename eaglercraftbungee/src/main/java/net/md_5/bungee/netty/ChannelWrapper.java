// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ChannelWrapper {
	private final Channel ch;
	private volatile boolean closed;

	public ChannelWrapper(final ChannelHandlerContext ctx) {
		this.ch = ctx.channel();
	}

	public synchronized void write(final Object packet) {
		if (!this.closed) {
			this.ch.write(packet);
		}
	}

	public synchronized void close() {
		if (!this.closed) {
			this.closed = true;
			this.ch.close();
		}
	}

	public Channel getHandle() {
		return this.ch;
	}

	public boolean isClosed() {
		return this.closed;
	}
}
