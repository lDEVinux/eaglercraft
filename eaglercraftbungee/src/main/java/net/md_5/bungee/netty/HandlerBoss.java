// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import java.io.IOException;
import java.util.logging.Level;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.handler.timeout.ReadTimeoutException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.CancelSendSignal;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.PingHandler;

public class HandlerBoss extends ChannelInboundHandlerAdapter {
	private ChannelWrapper channel;
	private PacketHandler handler;

	public void setHandler(final PacketHandler handler) {
		Preconditions.checkArgument(handler != null, (Object) "handler");
		this.handler = handler;
	}

	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		if (this.handler != null) {
			this.channel = new ChannelWrapper(ctx);
			this.handler.connected(this.channel);
			if (!(this.handler instanceof InitialHandler) && !(this.handler instanceof PingHandler)) {
				ProxyServer.getInstance().getLogger().log(Level.INFO, "{0} has connected", this.handler);
			}
		}
	}

	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		if (this.handler != null) {
			this.handler.disconnected(this.channel);
			if (!(this.handler instanceof InitialHandler) && !(this.handler instanceof PingHandler)) {
				ProxyServer.getInstance().getLogger().log(Level.INFO, "{0} has disconnected", this.handler);
			}
		}
	}

	public void messageReceived(final ChannelHandlerContext ctx, final MessageList<Object> msgs) throws Exception {
		for (final Object msg : msgs) {
			if (this.handler != null && ctx.channel().isActive()) {
				if (msg instanceof PacketWrapper) {
					boolean sendPacket = true;
					try {
						((PacketWrapper) msg).packet.handle(this.handler);
					} catch (CancelSendSignal ex) {
						sendPacket = false;
					}
					if (!sendPacket) {
						continue;
					}
					this.handler.handle(((PacketWrapper) msg).buf);
				} else {
					this.handler.handle((byte[]) msg);
				}
			}
		}
	}

	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		if (ctx.channel().isActive()) {
			if (cause instanceof ReadTimeoutException) {
				ProxyServer.getInstance().getLogger().log(Level.WARNING, this.handler + " - read timed out");
			} else if (cause instanceof IOException) {
				ProxyServer.getInstance().getLogger().log(Level.WARNING, this.handler + " - IOException: " + cause.getMessage());
			} else {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, this.handler + " - encountered exception", cause);
			}
			if (this.handler != null) {
				try {
					this.handler.exception(cause);
				} catch (Exception ex) {
					ProxyServer.getInstance().getLogger().log(Level.SEVERE, this.handler + " - exception processing exception", ex);
				}
			}
			ctx.close();
		}
	}
}
