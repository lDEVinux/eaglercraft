// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class Packet2Handshake extends DefinedPacket {
	private byte procolVersion;
	private String username;
	private String host;
	private int port;

	private Packet2Handshake() {
		super(2);
	}

	@Override
	public void read(final ByteBuf buf) {
		this.procolVersion = buf.readByte();
		this.username = this.readString(buf);
		this.host = this.readString(buf);
		this.port = buf.readInt();
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeByte((int) this.procolVersion);
		this.writeString(this.username, buf);
		this.writeString(this.host, buf);
		buf.writeInt(this.port);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public byte getProcolVersion() {
		return this.procolVersion;
	}

	public void swapProtocol(byte b) {
		this.procolVersion = (byte)b;
	}

	public void setHost(String h) {
		this.host = h;
	}

	public void setPort(int p) {
		this.port = p;
	}

	public String getUsername() {
		return this.username;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	@Override
	public String toString() {
		return "Packet2Handshake(procolVersion=" + this.getProcolVersion() + ", username=" + this.getUsername() + ", host=" + this.getHost() + ", port=" + this.getPort() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Packet2Handshake)) {
			return false;
		}
		final Packet2Handshake other = (Packet2Handshake) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getProcolVersion() != other.getProcolVersion()) {
			return false;
		}
		final Object this$username = this.getUsername();
		final Object other$username = other.getUsername();
		Label_0078: {
			if (this$username == null) {
				if (other$username == null) {
					break Label_0078;
				}
			} else if (this$username.equals(other$username)) {
				break Label_0078;
			}
			return false;
		}
		final Object this$host = this.getHost();
		final Object other$host = other.getHost();
		if (this$host == null) {
			if (other$host == null) {
				return this.getPort() == other.getPort();
			}
		} else if (this$host.equals(other$host)) {
			return this.getPort() == other.getPort();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Packet2Handshake;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.getProcolVersion();
		final Object $username = this.getUsername();
		result = result * 31 + (($username == null) ? 0 : $username.hashCode());
		final Object $host = this.getHost();
		result = result * 31 + (($host == null) ? 0 : $host.hashCode());
		result = result * 31 + this.getPort();
		return result;
	}
}
