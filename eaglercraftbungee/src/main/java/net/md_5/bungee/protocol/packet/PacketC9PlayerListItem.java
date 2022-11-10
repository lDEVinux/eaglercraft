// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class PacketC9PlayerListItem extends DefinedPacket {
	private String username;
	private boolean online;
	private short ping;

	private PacketC9PlayerListItem() {
		super(201);
	}

	public PacketC9PlayerListItem(final String username, final boolean online, final short ping) {
		super(201);
		this.username = username;
		this.online = online;
		this.ping = ping;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.username = this.readString(buf);
		this.online = buf.readBoolean();
		this.ping = buf.readShort();
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.username, buf);
		buf.writeBoolean(this.online);
		buf.writeShort((int) this.ping);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public String getUsername() {
		return this.username;
	}

	public boolean isOnline() {
		return this.online;
	}

	public short getPing() {
		return this.ping;
	}

	@Override
	public String toString() {
		return "PacketC9PlayerListItem(username=" + this.getUsername() + ", online=" + this.isOnline() + ", ping=" + this.getPing() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketC9PlayerListItem)) {
			return false;
		}
		final PacketC9PlayerListItem other = (PacketC9PlayerListItem) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$username = this.getUsername();
		final Object other$username = other.getUsername();
		if (this$username == null) {
			if (other$username == null) {
				return this.isOnline() == other.isOnline() && this.getPing() == other.getPing();
			}
		} else if (this$username.equals(other$username)) {
			return this.isOnline() == other.isOnline() && this.getPing() == other.getPing();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketC9PlayerListItem;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $username = this.getUsername();
		result = result * 31 + (($username == null) ? 0 : $username.hashCode());
		result = result * 31 + (this.isOnline() ? 1231 : 1237);
		result = result * 31 + this.getPing();
		return result;
	}
}
