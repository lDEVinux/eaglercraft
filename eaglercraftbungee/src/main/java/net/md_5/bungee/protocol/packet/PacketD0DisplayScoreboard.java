// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketD0DisplayScoreboard extends DefinedPacket {
	private byte position;
	private String name;

	private PacketD0DisplayScoreboard() {
		super(208);
	}

	@Override
	public void read(final ByteBuf buf) {
		this.position = buf.readByte();
		this.name = this.readString(buf);
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeByte((int) this.position);
		this.writeString(this.name, buf);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public byte getPosition() {
		return this.position;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "PacketD0DisplayScoreboard(position=" + this.getPosition() + ", name=" + this.getName() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketD0DisplayScoreboard)) {
			return false;
		}
		final PacketD0DisplayScoreboard other = (PacketD0DisplayScoreboard) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getPosition() != other.getPosition()) {
			return false;
		}
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		if (this$name == null) {
			if (other$name == null) {
				return true;
			}
		} else if (this$name.equals(other$name)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketD0DisplayScoreboard;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.getPosition();
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		return result;
	}
}
