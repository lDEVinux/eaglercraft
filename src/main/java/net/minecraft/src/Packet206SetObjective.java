package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet206SetObjective extends Packet {
	public String objectiveName;
	public String objectiveDisplayName;

	/**
	 * 0 to create scoreboard, 1 to remove scoreboard, 2 to update display text.
	 */
	public int change;

	public Packet206SetObjective() {
	}

	public Packet206SetObjective(ScoreObjective par1, int par2) {
		this.objectiveName = par1.getName();
		this.objectiveDisplayName = par1.getDisplayName();
		this.change = par2;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.objectiveName = readString(par1DataInputStream, 16);
		this.objectiveDisplayName = readString(par1DataInputStream, 32);
		this.change = par1DataInputStream.readByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeString(this.objectiveName, par1DataOutputStream);
		writeString(this.objectiveDisplayName, par1DataOutputStream);
		par1DataOutputStream.writeByte(this.change);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleSetObjective(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 2 + this.objectiveName.length() + 2 + this.objectiveDisplayName.length() + 1;
	}
}
