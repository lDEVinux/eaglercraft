package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2ClientProtocol extends Packet {
	private int protocolVersion;
	private String username;
	private String serverHost;
	private int viewDistance;

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.protocolVersion = par1DataInputStream.readByte();
		this.username = readString(par1DataInputStream, 16);
		this.serverHost = readString(par1DataInputStream, 255);
		this.viewDistance = par1DataInputStream.readInt();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeByte(this.protocolVersion);
		writeString(this.username, par1DataOutputStream);
		writeString(this.serverHost, par1DataOutputStream);
		par1DataOutputStream.writeInt(this.viewDistance);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleClientProtocol(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 3 + 2 * this.username.length();
	}

	/**
	 * Returns the protocol version.
	 */
	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	/**
	 * Returns the username.
	 */
	public String getUsername() {
		return this.username;
	}
	
	public int getViewDistance() {
		return this.viewDistance;
	}
	
}
