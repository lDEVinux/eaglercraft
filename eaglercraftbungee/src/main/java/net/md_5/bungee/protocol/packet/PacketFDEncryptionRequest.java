// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

public class PacketFDEncryptionRequest extends DefinedPacket {
	private String serverId;
	private byte[] publicKey;
	private byte[] verifyToken;

	private PacketFDEncryptionRequest() {
		super(253);
	}

	public PacketFDEncryptionRequest(final String serverId, final byte[] publicKey, final byte[] verifyToken) {
		this();
		this.serverId = serverId;
		this.publicKey = publicKey;
		this.verifyToken = verifyToken;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.serverId = this.readString(buf);
		this.publicKey = this.readArray(buf);
		this.verifyToken = this.readArray(buf);
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeString(this.serverId, buf);
		this.writeArray(this.publicKey, buf);
		this.writeArray(this.verifyToken, buf);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public String getServerId() {
		return this.serverId;
	}

	public byte[] getPublicKey() {
		return this.publicKey;
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}

	@Override
	public String toString() {
		return "PacketFDEncryptionRequest(serverId=" + this.getServerId() + ", publicKey=" + Arrays.toString(this.getPublicKey()) + ", verifyToken=" + Arrays.toString(this.getVerifyToken()) + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketFDEncryptionRequest)) {
			return false;
		}
		final PacketFDEncryptionRequest other = (PacketFDEncryptionRequest) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$serverId = this.getServerId();
		final Object other$serverId = other.getServerId();
		if (this$serverId == null) {
			if (other$serverId == null) {
				return Arrays.equals(this.getPublicKey(), other.getPublicKey()) && Arrays.equals(this.getVerifyToken(), other.getVerifyToken());
			}
		} else if (this$serverId.equals(other$serverId)) {
			return Arrays.equals(this.getPublicKey(), other.getPublicKey()) && Arrays.equals(this.getVerifyToken(), other.getVerifyToken());
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketFDEncryptionRequest;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $serverId = this.getServerId();
		result = result * 31 + (($serverId == null) ? 0 : $serverId.hashCode());
		result = result * 31 + Arrays.hashCode(this.getPublicKey());
		result = result * 31 + Arrays.hashCode(this.getVerifyToken());
		return result;
	}
}
