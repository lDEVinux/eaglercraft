// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class Packet3Chat extends DefinedPacket {
	private String message;

	private Packet3Chat() {
		super(3);
	}

	public Packet3Chat(final String message) {
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
		return "Packet3Chat(message=" + this.getMessage() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Packet3Chat)) {
			return false;
		}
		final Packet3Chat other = (Packet3Chat) o;
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
		return other instanceof Packet3Chat;
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
