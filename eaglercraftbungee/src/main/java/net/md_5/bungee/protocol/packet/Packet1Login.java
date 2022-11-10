// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class Packet1Login extends DefinedPacket {
	protected int entityId;
	protected String levelType;
	protected byte gameMode;
	protected int dimension;
	protected byte difficulty;
	protected byte unused;
	protected byte maxPlayers;

	protected Packet1Login() {
		super(1);
	}

	public Packet1Login(final int entityId, final String levelType, final byte gameMode, final byte dimension, final byte difficulty, final byte unused, final byte maxPlayers) {
		this(entityId, levelType, gameMode, (int) dimension, difficulty, unused, maxPlayers);
	}

	public Packet1Login(final int entityId, final String levelType, final byte gameMode, final int dimension, final byte difficulty, final byte unused, final byte maxPlayers) {
		this();
		this.entityId = entityId;
		this.levelType = levelType;
		this.gameMode = gameMode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.unused = unused;
		this.maxPlayers = maxPlayers;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.entityId = buf.readInt();
		this.levelType = this.readString(buf);
		this.gameMode = buf.readByte();
		this.dimension = buf.readByte();
		this.difficulty = buf.readByte();
		this.unused = buf.readByte();
		this.maxPlayers = buf.readByte();
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeInt(this.entityId);
		this.writeString(this.levelType, buf);
		buf.writeByte((int) this.gameMode);
		buf.writeByte(this.dimension);
		buf.writeByte((int) this.difficulty);
		buf.writeByte((int) this.unused);
		buf.writeByte((int) this.maxPlayers);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public String getLevelType() {
		return this.levelType;
	}

	public byte getGameMode() {
		return this.gameMode;
	}

	public int getDimension() {
		return this.dimension;
	}

	public byte getDifficulty() {
		return this.difficulty;
	}

	public byte getUnused() {
		return this.unused;
	}

	public byte getMaxPlayers() {
		return this.maxPlayers;
	}

	@Override
	public String toString() {
		return "Packet1Login(entityId=" + this.getEntityId() + ", levelType=" + this.getLevelType() + ", gameMode=" + this.getGameMode() + ", dimension=" + this.getDimension() + ", difficulty=" + this.getDifficulty() + ", unused="
				+ this.getUnused() + ", maxPlayers=" + this.getMaxPlayers() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Packet1Login)) {
			return false;
		}
		final Packet1Login other = (Packet1Login) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getEntityId() != other.getEntityId()) {
			return false;
		}
		final Object this$levelType = this.getLevelType();
		final Object other$levelType = other.getLevelType();
		if (this$levelType == null) {
			if (other$levelType == null) {
				return this.getGameMode() == other.getGameMode() && this.getDimension() == other.getDimension() && this.getDifficulty() == other.getDifficulty() && this.getUnused() == other.getUnused()
						&& this.getMaxPlayers() == other.getMaxPlayers();
			}
		} else if (this$levelType.equals(other$levelType)) {
			return this.getGameMode() == other.getGameMode() && this.getDimension() == other.getDimension() && this.getDifficulty() == other.getDifficulty() && this.getUnused() == other.getUnused()
					&& this.getMaxPlayers() == other.getMaxPlayers();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Packet1Login;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.getEntityId();
		final Object $levelType = this.getLevelType();
		result = result * 31 + (($levelType == null) ? 0 : $levelType.hashCode());
		result = result * 31 + this.getGameMode();
		result = result * 31 + this.getDimension();
		result = result * 31 + this.getDifficulty();
		result = result * 31 + this.getUnused();
		result = result * 31 + this.getMaxPlayers();
		return result;
	}
}
