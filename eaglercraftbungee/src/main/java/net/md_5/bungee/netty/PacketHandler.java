// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;

public abstract class PacketHandler extends AbstractPacketHandler {
	@Override
	public abstract String toString();

	public void exception(final Throwable t) throws Exception {
	}

	public void handle(final byte[] buf) throws Exception {
	}

	public void connected(final ChannelWrapper channel) throws Exception {
	}

	public void disconnected(final ChannelWrapper channel) throws Exception {
	}
}
