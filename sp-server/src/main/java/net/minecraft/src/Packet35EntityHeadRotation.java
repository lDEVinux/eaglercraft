package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet35EntityHeadRotation extends Packet {
	public int entityId;
	public byte headRotationYaw;

	public Packet35EntityHeadRotation() {
	}

	public Packet35EntityHeadRotation(int par1, byte par2) {
		this.entityId = par1;
		this.headRotationYaw = par2;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.entityId = par1DataInputStream.readInt();
		this.headRotationYaw = par1DataInputStream.readByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.entityId);
		par1DataOutputStream.writeByte(this.headRotationYaw);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleEntityHeadRotation(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 5;
	}

	/**
	 * only false for the abstract Packet class, all real packets return true
	 */
	public boolean isRealPacket() {
		return true;
	}

	/**
	 * eg return packet30entity.entityId == entityId; WARNING : will throw if you
	 * compare a packet to a different packet class
	 */
	public boolean containsSameEntityIDAs(Packet par1Packet) {
		Packet35EntityHeadRotation var2 = (Packet35EntityHeadRotation) par1Packet;
		return var2.entityId == this.entityId;
	}

	/**
	 * If this returns true, the packet may be processed on any thread; otherwise it
	 * is queued for the main thread to handle.
	 */
	public boolean canProcessAsync() {
		return true;
	}
}
