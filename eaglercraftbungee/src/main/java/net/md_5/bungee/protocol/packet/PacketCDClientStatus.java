// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketCDClientStatus extends DefinedPacket {
	private byte payload;

	private PacketCDClientStatus() {
		super(205);
	}

	public PacketCDClientStatus(final byte payload) {
		this();
		this.payload = payload;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.payload = buf.readByte();
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeByte((int) this.payload);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return "PacketCDClientStatus(payload=" + this.payload + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketCDClientStatus)) {
			return false;
		}
		final PacketCDClientStatus other = (PacketCDClientStatus) o;
		return other.canEqual(this) && this.payload == other.payload;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketCDClientStatus;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.payload;
		return result;
	}
}
