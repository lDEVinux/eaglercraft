package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet204ClientInfo extends Packet {
	private String language;
	private int renderDistance;
	private int chatVisisble;
	private boolean chatColours;
	private int gameDifficulty;
	private boolean showCape;

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.language = readString(par1DataInputStream, 7);
		this.renderDistance = par1DataInputStream.readByte();
		byte var2 = par1DataInputStream.readByte();
		this.chatVisisble = var2 & 7;
		this.chatColours = (var2 & 8) == 8;
		this.gameDifficulty = par1DataInputStream.readByte();
		this.showCape = par1DataInputStream.readBoolean();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeString(this.language, par1DataOutputStream);
		par1DataOutputStream.writeByte(this.renderDistance);
		par1DataOutputStream.writeByte(this.chatVisisble | (this.chatColours ? 1 : 0) << 3);
		par1DataOutputStream.writeByte(this.gameDifficulty);
		par1DataOutputStream.writeBoolean(this.showCape);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleClientInfo(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 7;
	}

	public String getLanguage() {
		return this.language;
	}

	public int getRenderDistance() {
		return this.renderDistance;
	}

	public int getChatVisibility() {
		return this.chatVisisble;
	}

	public boolean getChatColours() {
		return this.chatColours;
	}

	public int getDifficulty() {
		return this.gameDifficulty;
	}

	public boolean getShowCape() {
		return this.showCape;
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
}
