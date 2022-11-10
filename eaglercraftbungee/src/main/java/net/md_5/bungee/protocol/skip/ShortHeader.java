// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class ShortHeader extends Instruction {
	private final Instruction child;

	ShortHeader(final Instruction child) {
		this.child = child;
	}

	@Override
	void read(final ByteBuf in) {
		for (short size = in.readShort(), s = 0; s < size; ++s) {
			this.child.read(in);
		}
	}
}
