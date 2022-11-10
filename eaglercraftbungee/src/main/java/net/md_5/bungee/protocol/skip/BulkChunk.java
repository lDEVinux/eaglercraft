// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

public class BulkChunk extends Instruction {
	@Override
	void read(final ByteBuf in) {
		final short count = in.readShort();
		final int size = in.readInt();
		in.readBoolean();
		in.skipBytes(size + count * 12);
	}
}
