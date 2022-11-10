// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class Jump extends Instruction {
	final int len;

	Jump(final int len) {
		if (len < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.len = len;
	}

	@Override
	void read(final ByteBuf in) {
		in.skipBytes(this.len);
	}
}
