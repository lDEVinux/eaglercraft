// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol;

import java.lang.reflect.Constructor;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.skip.PacketReader;

public interface Protocol {
	PacketReader getSkipper();

	DefinedPacket read(final short p0, final ByteBuf p1);

	OpCode[][] getOpCodes();

	Class<? extends DefinedPacket>[] getClasses();

	Constructor<? extends DefinedPacket>[] getConstructors();
}
