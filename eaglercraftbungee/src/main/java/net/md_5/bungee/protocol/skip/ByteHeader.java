// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class ByteHeader extends Instruction {
	private final Instruction child;

	ByteHeader(final Instruction child) {
		this.child = child;
	}

	@Override
	void read(final ByteBuf in) {
		for (byte size = in.readByte(), b = 0; b < size; ++b) {
			this.child.read(in);
		}
	}
}
