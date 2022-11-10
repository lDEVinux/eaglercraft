// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

class MetaData extends Instruction {
	@Override
	void read(final ByteBuf in) {
		for (int x = in.readUnsignedByte(); x != 127; x = in.readUnsignedByte()) {
			final int type = x >> 5;
			switch (type) {
			case 0: {
				MetaData.BYTE.read(in);
				break;
			}
			case 1: {
				MetaData.SHORT.read(in);
				break;
			}
			case 2: {
				MetaData.INT.read(in);
				break;
			}
			case 3: {
				MetaData.FLOAT.read(in);
				break;
			}
			case 4: {
				MetaData.STRING.read(in);
				break;
			}
			case 5: {
				MetaData.ITEM.read(in);
				break;
			}
			case 6: {
				in.skipBytes(12);
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown metadata type " + type);
			}
			}
		}
	}
}
