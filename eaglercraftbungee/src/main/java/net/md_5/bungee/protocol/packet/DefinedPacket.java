// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import java.beans.ConstructorProperties;

import io.netty.buffer.ByteBuf;

public abstract class DefinedPacket {
	private final int id;

	public final int getId() {
		return this.id;
	}

	public void writeString(final String s, final ByteBuf buf) {
		buf.writeShort(s.length());
		for (final char c : s.toCharArray()) {
			buf.writeChar((int) c);
		}
	}

	public String readString(final ByteBuf buf) {
		final short len = buf.readShort();
		final char[] chars = new char[len];
		for (int i = 0; i < len; ++i) {
			chars[i] = buf.readChar();
		}
		return new String(chars);
	}

	public void writeArray(final byte[] b, final ByteBuf buf) {
		buf.writeShort(b.length);
		buf.writeBytes(b);
	}

	public byte[] readArray(final ByteBuf buf) {
		final short len = buf.readShort();
		final byte[] ret = new byte[len];
		buf.readBytes(ret);
		return ret;
	}

	public abstract void read(final ByteBuf p0);

	public abstract void write(final ByteBuf p0);

	public abstract void handle(final AbstractPacketHandler p0) throws Exception;

	@Override
	public abstract boolean equals(final Object p0);

	@Override
	public abstract int hashCode();

	@Override
	public abstract String toString();

	@ConstructorProperties({ "id" })
	public DefinedPacket(final int id) {
		this.id = id;
	}
}
