package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet253ServerAuthData extends Packet {
	private String serverId;
	private byte[] verifyToken = new byte[0];

	public Packet253ServerAuthData() {
	}

	public Packet253ServerAuthData(String par1Str, byte[] par3ArrayOfByte) {
		this.serverId = par1Str;
		this.verifyToken = par3ArrayOfByte;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.serverId = readString(par1DataInputStream, 20);
		readBytesFromStream(par1DataInputStream);
		this.verifyToken = readBytesFromStream(par1DataInputStream);
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeString(this.serverId, par1DataOutputStream);
		writeByteArray(par1DataOutputStream, new byte[0]);
		writeByteArray(par1DataOutputStream, this.verifyToken);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleServerAuthData(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 2 + this.serverId.length() * 2 + 2 + 0 + 2 + this.verifyToken.length;
	}

	public String getServerId() {
		return this.serverId;
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}
}
