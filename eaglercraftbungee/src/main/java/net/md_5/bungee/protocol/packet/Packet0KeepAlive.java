// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class Packet0KeepAlive extends DefinedPacket {
	private int randomId;

	private Packet0KeepAlive() {
		super(0);
	}

	@Override
	public void read(final ByteBuf buf) {
		this.randomId = buf.readInt();
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeInt(this.randomId);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public int getRandomId() {
		return this.randomId;
	}

	@Override
	public String toString() {
		return "Packet0KeepAlive(randomId=" + this.getRandomId() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Packet0KeepAlive)) {
			return false;
		}
		final Packet0KeepAlive other = (Packet0KeepAlive) o;
		return other.canEqual(this) && this.getRandomId() == other.getRandomId();
	}

	public boolean canEqual(final Object other) {
		return other instanceof Packet0KeepAlive;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.getRandomId();
		return result;
	}
}
