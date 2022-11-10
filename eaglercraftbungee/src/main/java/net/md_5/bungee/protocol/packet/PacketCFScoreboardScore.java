// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketCFScoreboardScore extends DefinedPacket {
	private String itemName;
	private byte action;
	private String scoreName;
	private int value;

	private PacketCFScoreboardScore() {
		super(207);
	}

	@Override
	public void read(final ByteBuf buf) {
		this.itemName = this.readString(buf);
		this.action = buf.readByte();
		if (this.action != 1) {
			this.scoreName = this.readString(buf);
			this.value = buf.readInt();
		}
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.itemName, buf);
		buf.writeByte((int) this.action);
		if (this.action != 1) {
			this.writeString(this.scoreName, buf);
			buf.writeInt(this.value);
		}
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public String getItemName() {
		return this.itemName;
	}

	public byte getAction() {
		return this.action;
	}

	public String getScoreName() {
		return this.scoreName;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "PacketCFScoreboardScore(itemName=" + this.getItemName() + ", action=" + this.getAction() + ", scoreName=" + this.getScoreName() + ", value=" + this.getValue() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketCFScoreboardScore)) {
			return false;
		}
		final PacketCFScoreboardScore other = (PacketCFScoreboardScore) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$itemName = this.getItemName();
		final Object other$itemName = other.getItemName();
		Label_0065: {
			if (this$itemName == null) {
				if (other$itemName == null) {
					break Label_0065;
				}
			} else if (this$itemName.equals(other$itemName)) {
				break Label_0065;
			}
			return false;
		}
		if (this.getAction() != other.getAction()) {
			return false;
		}
		final Object this$scoreName = this.getScoreName();
		final Object other$scoreName = other.getScoreName();
		if (this$scoreName == null) {
			if (other$scoreName == null) {
				return this.getValue() == other.getValue();
			}
		} else if (this$scoreName.equals(other$scoreName)) {
			return this.getValue() == other.getValue();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketCFScoreboardScore;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $itemName = this.getItemName();
		result = result * 31 + (($itemName == null) ? 0 : $itemName.hashCode());
		result = result * 31 + this.getAction();
		final Object $scoreName = this.getScoreName();
		result = result * 31 + (($scoreName == null) ? 0 : $scoreName.hashCode());
		result = result * 31 + this.getValue();
		return result;
	}
}
