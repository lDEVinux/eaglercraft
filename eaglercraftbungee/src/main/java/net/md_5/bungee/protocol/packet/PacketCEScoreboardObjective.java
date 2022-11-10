// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketCEScoreboardObjective extends DefinedPacket {
	private String name;
	private String text;
	private byte action;

	private PacketCEScoreboardObjective() {
		super(206);
	}

	public PacketCEScoreboardObjective(final String name, final String text, final byte action) {
		this();
		this.name = name;
		this.text = text;
		this.action = action;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.name = this.readString(buf);
		this.text = this.readString(buf);
		this.action = buf.readByte();
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.name, buf);
		this.writeString(this.text, buf);
		buf.writeByte((int) this.action);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public String getName() {
		return this.name;
	}

	public String getText() {
		return this.text;
	}

	public byte getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "PacketCEScoreboardObjective(name=" + this.getName() + ", text=" + this.getText() + ", action=" + this.getAction() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketCEScoreboardObjective)) {
			return false;
		}
		final PacketCEScoreboardObjective other = (PacketCEScoreboardObjective) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		Label_0065: {
			if (this$name == null) {
				if (other$name == null) {
					break Label_0065;
				}
			} else if (this$name.equals(other$name)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$text = this.getText();
		final Object other$text = other.getText();
		if (this$text == null) {
			if (other$text == null) {
				return this.getAction() == other.getAction();
			}
		} else if (this$text.equals(other$text)) {
			return this.getAction() == other.getAction();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketCEScoreboardObjective;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		final Object $text = this.getText();
		result = result * 31 + (($text == null) ? 0 : $text.hashCode());
		result = result * 31 + this.getAction();
		return result;
	}
}
