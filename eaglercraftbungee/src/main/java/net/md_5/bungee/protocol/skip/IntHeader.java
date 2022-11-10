// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class IntHeader extends Instruction {
	private final Instruction child;

	IntHeader(final Instruction child) {
		this.child = child;
	}

	@Override
	void read(final ByteBuf in) {
		for (int size = in.readInt(), i = 0; i < size; ++i) {
			this.child.read(in);
		}
	}
}
