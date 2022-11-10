// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

public class PacketD1Team extends DefinedPacket {
	private String name;
	private byte mode;
	private String displayName;
	private String prefix;
	private String suffix;
	private boolean friendlyFire;
	private short playerCount;
	private String[] players;

	private PacketD1Team() {
		super(209);
	}

	public PacketD1Team(final String name) {
		this();
		this.name = name;
		this.mode = 1;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.name = this.readString(buf);
		this.mode = buf.readByte();
		if (this.mode == 0 || this.mode == 2) {
			this.displayName = this.readString(buf);
			this.prefix = this.readString(buf);
			this.suffix = this.readString(buf);
			this.friendlyFire = buf.readBoolean();
		}
		if (this.mode == 0 || this.mode == 3 || this.mode == 4) {
			this.players = new String[buf.readShort()];
			for (int i = 0; i < this.getPlayers().length; ++i) {
				this.players[i] = this.readString(buf);
			}
		}
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.name, buf);
		buf.writeByte((int) this.mode);
		if (this.mode == 0 || this.mode == 2) {
			this.writeString(this.displayName, buf);
			this.writeString(this.prefix, buf);
			this.writeString(this.suffix, buf);
			buf.writeBoolean(this.friendlyFire);
		}
		if (this.mode == 0 || this.mode == 3 || this.mode == 4) {
			buf.writeShort(this.players.length);
			for (int i = 0; i < this.players.length; ++i) {
				this.writeString(this.players[i], buf);
			}
		}
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public String getName() {
		return this.name;
	}

	public byte getMode() {
		return this.mode;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public boolean isFriendlyFire() {
		return this.friendlyFire;
	}

	public short getPlayerCount() {
		return this.playerCount;
	}

	public String[] getPlayers() {
		return this.players;
	}

	@Override
	public String toString() {
		return "PacketD1Team(name=" + this.getName() + ", mode=" + this.getMode() + ", displayName=" + this.getDisplayName() + ", prefix=" + this.getPrefix() + ", suffix=" + this.getSuffix() + ", friendlyFire=" + this.isFriendlyFire()
				+ ", playerCount=" + this.getPlayerCount() + ", players=" + Arrays.deepToString(this.getPlayers()) + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketD1Team)) {
			return false;
		}
		final PacketD1Team other = (PacketD1Team) o;
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
		if (this.getMode() != other.getMode()) {
			return false;
		}
		final Object this$displayName = this.getDisplayName();
		final Object other$displayName = other.getDisplayName();
		Label_0115: {
			if (this$displayName == null) {
				if (other$displayName == null) {
					break Label_0115;
				}
			} else if (this$displayName.equals(other$displayName)) {
				break Label_0115;
			}
			return false;
		}
		final Object this$prefix = this.getPrefix();
		final Object other$prefix = other.getPrefix();
		Label_0152: {
			if (this$prefix == null) {
				if (other$prefix == null) {
					break Label_0152;
				}
			} else if (this$prefix.equals(other$prefix)) {
				break Label_0152;
			}
			return false;
		}
		final Object this$suffix = this.getSuffix();
		final Object other$suffix = other.getSuffix();
		if (this$suffix == null) {
			if (other$suffix == null) {
				return this.isFriendlyFire() == other.isFriendlyFire() && this.getPlayerCount() == other.getPlayerCount() && Arrays.deepEquals(this.getPlayers(), other.getPlayers());
			}
		} else if (this$suffix.equals(other$suffix)) {
			return this.isFriendlyFire() == other.isFriendlyFire() && this.getPlayerCount() == other.getPlayerCount() && Arrays.deepEquals(this.getPlayers(), other.getPlayers());
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketD1Team;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		result = result * 31 + this.getMode();
		final Object $displayName = this.getDisplayName();
		result = result * 31 + (($displayName == null) ? 0 : $displayName.hashCode());
		final Object $prefix = this.getPrefix();
		result = result * 31 + (($prefix == null) ? 0 : $prefix.hashCode());
		final Object $suffix = this.getSuffix();
		result = result * 31 + (($suffix == null) ? 0 : $suffix.hashCode());
		result = result * 31 + (this.isFriendlyFire() ? 1231 : 1237);
		result = result * 31 + this.getPlayerCount();
		result = result * 31 + Arrays.deepHashCode(this.getPlayers());
		return result;
	}
}
