// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketCCSettings extends DefinedPacket {
	private String locale;
	private byte viewDistance;
	private byte chatFlags;
	private byte difficulty;
	private boolean showCape;

	private PacketCCSettings() {
		super(204);
	}

	@Override
	public void read(final ByteBuf buf) {
		this.locale = this.readString(buf);
		this.viewDistance = buf.readByte();
		this.chatFlags = buf.readByte();
		this.difficulty = buf.readByte();
		this.showCape = buf.readBoolean();
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.locale, buf);
		buf.writeByte((int) this.viewDistance);
		buf.writeByte((int) this.chatFlags);
		buf.writeByte((int) this.difficulty);
		buf.writeBoolean(this.showCape);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return "PacketCCSettings(locale=" + this.locale + ", viewDistance=" + this.viewDistance + ", chatFlags=" + this.chatFlags + ", difficulty=" + this.difficulty + ", showCape=" + this.showCape + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketCCSettings)) {
			return false;
		}
		final PacketCCSettings other = (PacketCCSettings) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$locale = this.locale;
		final Object other$locale = other.locale;
		if (this$locale == null) {
			if (other$locale == null) {
				return this.viewDistance == other.viewDistance && this.chatFlags == other.chatFlags && this.difficulty == other.difficulty && this.showCape == other.showCape;
			}
		} else if (this$locale.equals(other$locale)) {
			return this.viewDistance == other.viewDistance && this.chatFlags == other.chatFlags && this.difficulty == other.difficulty && this.showCape == other.showCape;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketCCSettings;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $locale = this.locale;
		result = result * 31 + (($locale == null) ? 0 : $locale.hashCode());
		result = result * 31 + this.viewDistance;
		result = result * 31 + this.chatFlags;
		result = result * 31 + this.difficulty;
		result = result * 31 + (this.showCape ? 1231 : 1237);
		return result;
	}
}
