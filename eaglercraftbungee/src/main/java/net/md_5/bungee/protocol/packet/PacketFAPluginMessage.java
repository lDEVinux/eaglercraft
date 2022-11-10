// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

public class PacketFAPluginMessage extends DefinedPacket {
	private String tag;
	private byte[] data;

	private PacketFAPluginMessage() {
		super(250);
	}

	public PacketFAPluginMessage(final String tag, final byte[] data) {
		this();
		this.tag = tag;
		this.data = data;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.tag = this.readString(buf);
		this.data = this.readArray(buf);
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.tag, buf);
		this.writeArray(this.data, buf);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public String getTag() {
		return this.tag;
	}

	public byte[] getData() {
		return this.data;
	}

	@Override
	public String toString() {
		return "PacketFAPluginMessage(tag=" + this.getTag() + ", data=" + Arrays.toString(this.getData()) + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketFAPluginMessage)) {
			return false;
		}
		final PacketFAPluginMessage other = (PacketFAPluginMessage) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$tag = this.getTag();
		final Object other$tag = other.getTag();
		if (this$tag == null) {
			if (other$tag == null) {
				return Arrays.equals(this.getData(), other.getData());
			}
		} else if (this$tag.equals(other$tag)) {
			return Arrays.equals(this.getData(), other.getData());
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketFAPluginMessage;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $tag = this.getTag();
		result = result * 31 + (($tag == null) ? 0 : $tag.hashCode());
		result = result * 31 + Arrays.hashCode(this.getData());
		return result;
	}
}
