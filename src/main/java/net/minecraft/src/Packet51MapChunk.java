package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.EaglerInflater;

public class Packet51MapChunk extends Packet {
	/** The x-position of the transmitted chunk, in chunk coordinates. */
	public int xCh;

	/** The z-position of the transmitted chunk, in chunk coordinates. */
	public int zCh;

	/**
	 * The y-position of the lowest chunk Section in the transmitted chunk, in chunk
	 * coordinates.
	 */
	public int yChMin;

	/**
	 * The y-position of the highest chunk Section in the transmitted chunk, in
	 * chunk coordinates.
	 */
	public int yChMax;

	/** The transmitted chunk data, decompressed. */
	private byte[] chunkData;

	/** The compressed chunk data */
	private byte[] compressedChunkData;

	/**
	 * Whether to initialize the Chunk before applying the effect of the
	 * Packet51MapChunk.
	 */
	public boolean includeInitialize;

	/** The length of the compressed chunk data byte array. */
	private int tempLength;

	/** A temporary storage for the compressed chunk data byte array. */
	private static byte[] temp = new byte[196864];

	public Packet51MapChunk() {
		this.isChunkDataPacket = true;
	}

	public Packet51MapChunk(Chunk par1Chunk, boolean par2, int par3) {
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.xCh = par1DataInputStream.readInt();
		this.zCh = par1DataInputStream.readInt();
		this.includeInitialize = par1DataInputStream.readBoolean();
		this.yChMin = par1DataInputStream.readShort();
		this.yChMax = par1DataInputStream.readShort();
		this.tempLength = par1DataInputStream.readInt();

		boolean compress = (this.tempLength & 0x10000000) != 0x10000000; // stupid
		
		if(compress) {
			if (temp.length < this.tempLength) {
				temp = new byte[this.tempLength];
			}

			par1DataInputStream.readFully(temp, 0, this.tempLength);
			int var2 = 0;
			int var3;

			for (var3 = 0; var3 < 16; ++var3) {
				var2 += this.yChMin >> var3 & 1;
			}

			var3 = 12288 * var2;

			if (this.includeInitialize) {
				var3 += 256;
			}

			this.compressedChunkData = EaglerInflater.uncompress(temp);
		}else {
			this.tempLength &= 0xFFFFFFF;
			this.compressedChunkData = new byte[this.tempLength];
			par1DataInputStream.readFully(this.compressedChunkData, 0, this.tempLength);
		}
		
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.xCh);
		par1DataOutputStream.writeInt(this.zCh);
		par1DataOutputStream.writeBoolean(this.includeInitialize);
		par1DataOutputStream.writeShort((short) (this.yChMin & 65535));
		par1DataOutputStream.writeShort((short) (this.yChMax & 65535));
		par1DataOutputStream.writeInt(this.tempLength);
		par1DataOutputStream.write(this.chunkData, 0, this.tempLength);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleMapChunk(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 17 + this.tempLength;
	}

	public byte[] getCompressedChunkData() {
		return this.compressedChunkData;
	}
	
}
