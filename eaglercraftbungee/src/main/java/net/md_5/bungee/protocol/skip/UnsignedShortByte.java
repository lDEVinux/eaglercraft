// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class UnsignedShortByte extends Instruction {
	@Override
	void read(final ByteBuf in) {
		final int size = in.readUnsignedShort();
		in.skipBytes(size);
	}
}
