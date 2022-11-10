// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import net.md_5.bungee.protocol.packet.DefinedPacket;

public class PacketWrapper {
	DefinedPacket packet;
	byte[] buf;

	public PacketWrapper(final DefinedPacket packet, final byte[] buf) {
		this.packet = packet;
		this.buf = buf;
	}
}
