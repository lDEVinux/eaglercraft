package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet9Respawn extends Packet {
	public int respawnDimension;

	/**
	 * The difficulty setting. 0 through 3 for peaceful, easy, normal, hard. The
	 * client always sends 1.
	 */
	public int difficulty;

	/** Defaults to 128 */
	public int worldHeight;
	public EnumGameType gameType;
	public WorldType terrainType;

	public Packet9Respawn() {
	}

	public Packet9Respawn(int par1, byte par2, WorldType par3WorldType, int par4, EnumGameType par5EnumGameType) {
		this.respawnDimension = par1;
		this.difficulty = par2;
		this.worldHeight = par4;
		this.gameType = par5EnumGameType;
		this.terrainType = par3WorldType;
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleRespawn(this);
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.respawnDimension = par1DataInputStream.readInt();
		this.difficulty = par1DataInputStream.readByte();
		this.gameType = EnumGameType.getByID(par1DataInputStream.readByte());
		this.worldHeight = par1DataInputStream.readShort();
		String var2 = readString(par1DataInputStream, 16);
		this.terrainType = WorldType.parseWorldType(var2);

		if (this.terrainType == null) {
			this.terrainType = WorldType.DEFAULT;
		}
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.respawnDimension);
		par1DataOutputStream.writeByte(this.difficulty);
		par1DataOutputStream.writeByte(this.gameType.getID());
		par1DataOutputStream.writeShort(this.worldHeight);
		writeString(this.terrainType.getWorldTypeName(), par1DataOutputStream);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 8 + (this.terrainType == null ? 0 : this.terrainType.getWorldTypeName().length());
	}
}
