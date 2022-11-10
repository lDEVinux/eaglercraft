package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet252SharedKey extends Packet {

	public Packet252SharedKey() {
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		readBytesFromStream(par1DataInputStream);
		readBytesFromStream(par1DataInputStream);
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeByteArray(par1DataOutputStream, new byte[162]);
		writeByteArray(par1DataOutputStream, new byte[4]);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleSharedKey(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 2 + 162 + 2 + 4;
	}
}
