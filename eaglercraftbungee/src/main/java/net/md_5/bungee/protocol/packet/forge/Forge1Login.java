// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet.forge;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.Packet1Login;

public class Forge1Login extends Packet1Login {
	private Forge1Login() {
	}

	public Forge1Login(final int entityId, final String levelType, final byte gameMode, final int dimension, final byte difficulty, final byte unused, final byte maxPlayers) {
		super(entityId, levelType, gameMode, dimension, difficulty, unused, maxPlayers);
	}

	@Override
	public void read(final ByteBuf buf) {
		this.entityId = buf.readInt();
		this.levelType = this.readString(buf);
		this.gameMode = buf.readByte();
		this.dimension = buf.readInt();
		this.difficulty = buf.readByte();
		this.unused = buf.readByte();
		this.maxPlayers = buf.readByte();
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeInt(this.entityId);
		this.writeString(this.levelType, buf);
		buf.writeByte((int) this.gameMode);
		buf.writeInt(this.dimension);
		buf.writeByte((int) this.difficulty);
		buf.writeByte((int) this.unused);
		buf.writeByte((int) this.maxPlayers);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return "Forge1Login()";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Forge1Login)) {
			return false;
		}
		final Forge1Login other = (Forge1Login) o;
		return other.canEqual(this);
	}

	@Override
	public boolean canEqual(final Object other) {
		return other instanceof Forge1Login;
	}

	@Override
	public int hashCode() {
		final int result = 1;
		return result;
	}
}
