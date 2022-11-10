package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet205ClientCommand extends Packet {
	/**
	 * 0 sent to a netLoginHandler starts the server, 1 sent to NetServerHandler
	 * forces a respawn
	 */
	public int forceRespawn;

	public Packet205ClientCommand() {
	}

	public Packet205ClientCommand(int par1) {
		this.forceRespawn = par1;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.forceRespawn = par1DataInputStream.readByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeByte(this.forceRespawn & 255);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleClientCommand(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 1;
	}
}
