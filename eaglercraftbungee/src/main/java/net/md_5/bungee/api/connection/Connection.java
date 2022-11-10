// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.connection;

import java.net.InetSocketAddress;

import net.md_5.bungee.protocol.packet.DefinedPacket;

public interface Connection {
	InetSocketAddress getAddress();

	void disconnect(final String p0);

	Unsafe unsafe();

	public interface Unsafe {
		void sendPacket(final DefinedPacket p0);
	}
}
