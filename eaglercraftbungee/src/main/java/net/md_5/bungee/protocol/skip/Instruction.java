// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import io.netty.buffer.ByteBuf;

abstract class Instruction {
	static final Instruction BOOLEAN;
	static final Instruction BULK_CHUNK;
	static final Instruction BYTE;
	static final Instruction DOUBLE;
	static final Instruction FLOAT;
	static final Instruction INT;
	static final Instruction INT_3;
	static final Instruction INT_BYTE;
	static final Instruction ITEM;
	static final Instruction LONG;
	static final Instruction METADATA;
	static final Instruction OPTIONAL_MOTION;
	static final Instruction SHORT;
	static final Instruction SHORT_BYTE;
	static final Instruction SHORT_ITEM;
	static final Instruction STRING;
	static final Instruction USHORT_BYTE;
	static final Instruction BYTE_INT;
	static final Instruction STRING_ARRAY;

	abstract void read(final ByteBuf p0);

	static {
		BOOLEAN = new Jump(1);
		BULK_CHUNK = new BulkChunk();
		BYTE = new Jump(1);
		DOUBLE = new Jump(8);
		FLOAT = new Jump(4);
		INT = new Jump(4);
		INT_3 = new IntHeader(new Jump(3));
		INT_BYTE = new IntHeader(Instruction.BYTE);
		ITEM = new Item();
		LONG = new Jump(8);
		METADATA = new MetaData();
		OPTIONAL_MOTION = new OptionalMotion();
		SHORT = new Jump(2);
		SHORT_BYTE = new ShortHeader(Instruction.BYTE);
		SHORT_ITEM = new ShortHeader(Instruction.ITEM);
		STRING = new ShortHeader(new Jump(2));
		USHORT_BYTE = new UnsignedShortByte();
		BYTE_INT = new ByteHeader(Instruction.INT);
		STRING_ARRAY = new ShortHeader(Instruction.STRING);
	}
}
