package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet29DestroyEntity extends Packet {
	/** ID of the entity to be destroyed on the client. */
	public int[] entityId;

	public Packet29DestroyEntity() {
	}

	public Packet29DestroyEntity(int... par1ArrayOfInteger) {
		this.entityId = par1ArrayOfInteger;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.entityId = new int[par1DataInputStream.readByte()];

		for (int var2 = 0; var2 < this.entityId.length; ++var2) {
			this.entityId[var2] = par1DataInputStream.readInt();
		}
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeByte(this.entityId.length);

		for (int var2 = 0; var2 < this.entityId.length; ++var2) {
			par1DataOutputStream.writeInt(this.entityId[var2]);
		}
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleDestroyEntity(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 1 + this.entityId.length * 4;
	}
}
