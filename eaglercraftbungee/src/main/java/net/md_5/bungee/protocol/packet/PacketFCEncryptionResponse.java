// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.packet;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

public class PacketFCEncryptionResponse extends DefinedPacket {
	private byte[] sharedSecret;
	private byte[] verifyToken;

	private PacketFCEncryptionResponse() {
		super(252);
	}

	public PacketFCEncryptionResponse(final byte[] sharedSecret, final byte[] verifyToken) {
		this();
		this.sharedSecret = sharedSecret;
		this.verifyToken = verifyToken;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.sharedSecret = this.readArray(buf);
		this.verifyToken = this.readArray(buf);
	}

	@Override
	public void write(final ByteBuf buf) {
		this.writeArray(this.sharedSecret, buf);
		this.writeArray(this.verifyToken, buf);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	public byte[] getSharedSecret() {
		return this.sharedSecret;
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}

	@Override
	public String toString() {
		return "PacketFCEncryptionResponse(sharedSecret=" + Arrays.toString(this.getSharedSecret()) + ", verifyToken=" + Arrays.toString(this.getVerifyToken()) + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PacketFCEncryptionResponse)) {
			return false;
		}
		final PacketFCEncryptionResponse other = (PacketFCEncryptionResponse) o;
		return other.canEqual(this) && Arrays.equals(this.getSharedSecret(), other.getSharedSecret()) && Arrays.equals(this.getVerifyToken(), other.getVerifyToken());
	}

	public boolean canEqual(final Object other) {
		return other instanceof PacketFCEncryptionResponse;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + Arrays.hashCode(this.getSharedSecret());
		result = result * 31 + Arrays.hashCode(this.getVerifyToken());
		return result;
	}
}
