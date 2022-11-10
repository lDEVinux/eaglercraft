// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class Packet9Respawn extends DefinedPacket {
	private int dimension;
	private byte difficulty;
	private byte gameMode;
	private short worldHeight;
	private String levelType;

	private Packet9Respawn() {
		super(9);
	}

	public Packet9Respawn(final int dimension, final byte difficulty, final byte gameMode, final short worldHeight, final String levelType) {
		this();
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gameMode = gameMode;
		this.worldHeight = worldHeight;
		this.levelType = levelType;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.dimension = buf.readInt();
		this.difficulty = buf.readByte();
		this.gameMode = buf.readByte();
		this.worldHeight = buf.readShort();
		this.levelType = this.readString(buf);
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeInt(this.dimension);
		buf.writeByte((int) this.difficulty);
		buf.writeByte((int) this.gameMode);
		buf.writeShort((int) this.worldHeight);
		this.writeString(this.levelType, buf);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return "Packet9Respawn(dimension=" + this.dimension + ", difficulty=" + this.difficulty + ", gameMode=" + this.gameMode + ", worldHeight=" + this.worldHeight + ", levelType=" + this.levelType + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Packet9Respawn)) {
			return false;
		}
		final Packet9Respawn other = (Packet9Respawn) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.dimension != other.dimension) {
			return false;
		}
		if (this.difficulty != other.difficulty) {
			return false;
		}
		if (this.gameMode != other.gameMode) {
			return false;
		}
		if (this.worldHeight != other.worldHeight) {
			return false;
		}
		final Object this$levelType = this.levelType;
		final Object other$levelType = other.levelType;
		if (this$levelType == null) {
			if (other$levelType == null) {
				return true;
			}
		} else if (this$levelType.equals(other$levelType)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Packet9Respawn;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.dimension;
		result = result * 31 + this.difficulty;
		result = result * 31 + this.gameMode;
		result = result * 31 + this.worldHeight;
		final Object $levelType = this.levelType;
		result = result * 31 + (($levelType == null) ? 0 : $levelType.hashCode());
		return result;
	}
}
