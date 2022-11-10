// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketFFKick extends DefinedPacket {
	private String message;

	private PacketFFKick() {
		super(255);
	}

	public PacketFFKick(final String message) {
		this();
		this.message = message;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.message = this.readString(buf);
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.message, buf);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public String toString() {
		return "PacketFFKick(message=" + this.getMessage() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketFFKick)) {
			return false;
		}
		final PacketFFKick other = (PacketFFKick) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$message = this.getMessage();
		final Object other$message = other.getMessage();
		if (this$message == null) {
			if (other$message == null) {
				return true;
			}
		} else if (this$message.equals(other$message)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketFFKick;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $message = this.getMessage();
		result = result * 31 + (($message == null) ? 0 : $message.hashCode());
		return result;
	}
}
