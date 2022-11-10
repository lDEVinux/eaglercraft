package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70GameEvent extends Packet {
	/**
	 * The client prints clientMessage[eventType] to chat when this packet is
	 * received.
	 */
	public static final String[] clientMessage = new String[] { "tile.bed.notValid", null, null, "gameMode.changed" };

	/** 0: Invalid bed, 1: Rain starts, 2: Rain stops, 3: Game mode changed. */
	public int eventType;

	/**
	 * When reason==3, the game mode to set. See EnumGameType for a list of values.
	 */
	public int gameMode;

	public Packet70GameEvent() {
	}

	public Packet70GameEvent(int par1, int par2) {
		this.eventType = par1;
		this.gameMode = par2;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.eventType = par1DataInputStream.readByte();
		this.gameMode = par1DataInputStream.readByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeByte(this.eventType);
		par1DataOutputStream.writeByte(this.gameMode);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleGameEvent(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 2;
	}
}
