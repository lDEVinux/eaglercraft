package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet0KeepAlive extends Packet {
	public int randomId;

	public Packet0KeepAlive() {
	}

	public Packet0KeepAlive(int par1) {
		this.randomId = par1;
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleKeepAlive(this);
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.randomId = par1DataInputStream.readInt();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.randomId);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 4;
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
		return true;
	}

	/**
	 * If this returns true, the packet may be processed on any thread; otherwise it
	 * is queued for the main thread to handle.
	 */
	public boolean canProcessAsync() {
		return true;
	}
}
