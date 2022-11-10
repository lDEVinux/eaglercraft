package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet132TileEntityData extends Packet {
	/** The X position of the tile entity to update. */
	public int xPosition;

	/** The Y position of the tile entity to update. */
	public int yPosition;

	/** The Z position of the tile entity to update. */
	public int zPosition;

	/** The type of update to perform on the tile entity. */
	public int actionType;

	/** Custom parameter 1 passed to the tile entity on update. */
	public NBTTagCompound customParam1;

	public Packet132TileEntityData() {
		this.isChunkDataPacket = true;
	}

	public Packet132TileEntityData(int par1, int par2, int par3, int par4, NBTTagCompound par5NBTTagCompound) {
		this.isChunkDataPacket = true;
		this.xPosition = par1;
		this.yPosition = par2;
		this.zPosition = par3;
		this.actionType = par4;
		this.customParam1 = par5NBTTagCompound;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.xPosition = par1DataInputStream.readInt();
		this.yPosition = par1DataInputStream.readShort();
		this.zPosition = par1DataInputStream.readInt();
		this.actionType = par1DataInputStream.readByte();
		this.customParam1 = readNBTTagCompound(par1DataInputStream);
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.xPosition);
		par1DataOutputStream.writeShort(this.yPosition);
		par1DataOutputStream.writeInt(this.zPosition);
		par1DataOutputStream.writeByte((byte) this.actionType);
		writeNBTTagCompound(this.customParam1, par1DataOutputStream);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleTileEntityData(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 25;
	}
}
