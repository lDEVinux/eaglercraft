package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.lax1dude.eaglercraft.EaglerInflater;

public class Packet56MapChunks extends Packet {
	private int[] chunkPostX;
	private int[] chunkPosZ;
	public int[] field_73590_a;
	public int[] field_73588_b;

	/** The compressed chunk data buffer */
	private byte[] chunkDataBuffer;
	private byte[][] field_73584_f;

	/** total size of the compressed data */
	private int dataLength;

	/**
	 * Whether or not the chunk data contains a light nibble array. This is true in
	 * the main world, false in the end + nether.
	 */
	private boolean skyLightSent;
	private static byte[] chunkDataNotCompressed = new byte[0];

	public Packet56MapChunks() {
	}

	public Packet56MapChunks(List par1List) {
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		short var2 = par1DataInputStream.readShort();
		this.dataLength = par1DataInputStream.readInt();
		this.skyLightSent = par1DataInputStream.readBoolean();
		this.chunkPostX = new int[var2];
		this.chunkPosZ = new int[var2];
		this.field_73590_a = new int[var2];
		this.field_73588_b = new int[var2];
		this.field_73584_f = new byte[var2][];

		byte[] var3;
		if((this.dataLength & 0x10000000) == 0x10000000) {
			this.dataLength &= 0xFFFFFFF;
			var3 = new byte[this.dataLength];
			par1DataInputStream.readFully(var3, 0, this.dataLength);
		}else {
			if (chunkDataNotCompressed.length < this.dataLength) {
				chunkDataNotCompressed = new byte[this.dataLength];
			}
			par1DataInputStream.readFully(chunkDataNotCompressed, 0, this.dataLength);
			var3 = EaglerInflater.uncompress(chunkDataNotCompressed);
		}

		int var5 = 0;

		for (int var6 = 0; var6 < var2; ++var6) {
			this.chunkPostX[var6] = par1DataInputStream.readInt();
			this.chunkPosZ[var6] = par1DataInputStream.readInt();
			this.field_73590_a[var6] = par1DataInputStream.readShort();
			this.field_73588_b[var6] = par1DataInputStream.readShort();
			int var7 = 0;
			int var8 = 0;
			int var9;

			for (var9 = 0; var9 < 16; ++var9) {
				var7 += this.field_73590_a[var6] >> var9 & 1;
				var8 += this.field_73588_b[var6] >> var9 & 1;
			}

			var9 = 2048 * 4 * var7 + 256;
			var9 += 2048 * var8;

			if (this.skyLightSent) {
				var9 += 2048 * var7;
			}

			this.field_73584_f[var6] = new byte[var9];
			System.arraycopy(var3, var5, this.field_73584_f[var6], 0, var9);
			var5 += var9;
		}
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeShort(this.chunkPostX.length);
		par1DataOutputStream.writeInt(this.dataLength);
		par1DataOutputStream.writeBoolean(this.skyLightSent);
		par1DataOutputStream.write(this.chunkDataBuffer, 0, this.dataLength);

		for (int var2 = 0; var2 < this.chunkPostX.length; ++var2) {
			par1DataOutputStream.writeInt(this.chunkPostX[var2]);
			par1DataOutputStream.writeInt(this.chunkPosZ[var2]);
			par1DataOutputStream.writeShort((short) (this.field_73590_a[var2] & 65535));
			par1DataOutputStream.writeShort((short) (this.field_73588_b[var2] & 65535));
		}
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleMapChunks(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 6 + this.dataLength + 12 * this.getNumberOfChunkInPacket();
	}

	public int getChunkPosX(int par1) {
		return this.chunkPostX[par1];
	}

	public int getChunkPosZ(int par1) {
		return this.chunkPosZ[par1];
	}

	public int getNumberOfChunkInPacket() {
		return this.chunkPostX.length;
	}

	public byte[] getChunkCompressedData(int par1) {
		return this.field_73584_f[par1];
	}
}
