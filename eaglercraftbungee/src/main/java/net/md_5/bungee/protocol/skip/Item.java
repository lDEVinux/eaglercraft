// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class Item extends Instruction {
	@Override
	void read(final ByteBuf in) {
		final short type = in.readShort();
		if (type >= 0) {
			in.skipBytes(3);
			Item.SHORT_BYTE.read(in);
		}
	}
}
