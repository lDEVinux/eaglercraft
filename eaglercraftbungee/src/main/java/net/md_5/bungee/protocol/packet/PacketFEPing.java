// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketFEPing extends DefinedPacket {
	private byte version;

	private PacketFEPing() {
		super(254);
	}

	@Override
	public void read(final ByteBuf buf) {
		this.version = buf.readByte();
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeByte((int) this.version);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return "PacketFEPing(version=" + this.version + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketFEPing)) {
			return false;
		}
		final PacketFEPing other = (PacketFEPing) o;
		return other.canEqual(this) && this.version == other.version;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketFEPing;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.version;
		return result;
	}
}
