package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

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
		int var2 = par1List.size();
		this.chunkPostX = new int[var2];
		this.chunkPosZ = new int[var2];
		this.field_73590_a = new int[var2];
		this.field_73588_b = new int[var2];
		this.field_73584_f = new byte[var2][];
		this.skyLightSent = !par1List.isEmpty() && !((Chunk) par1List.get(0)).worldObj.provider.hasNoSky;
		int var3 = 0;

		for (int var4 = 0; var4 < var2; ++var4) {
			Chunk var5 = (Chunk) par1List.get(var4);
			Packet51MapChunkData var6 = Packet51MapChunk.getMapChunkData(var5, true, 65535);

			if (chunkDataNotCompressed.length < var3 + var6.compressedData.length) {
				byte[] var7 = new byte[var3 + var6.compressedData.length];
				System.arraycopy(chunkDataNotCompressed, 0, var7, 0, chunkDataNotCompressed.length);
				chunkDataNotCompressed = var7;
			}

			System.arraycopy(var6.compressedData, 0, chunkDataNotCompressed, var3, var6.compressedData.length);
			var3 += var6.compressedData.length;
			this.chunkPostX[var4] = var5.xPosition;
			this.chunkPosZ[var4] = var5.zPosition;
			this.field_73590_a[var4] = var6.chunkExistFlag;
			this.field_73588_b[var4] = var6.chunkHasAddSectionFlag;
			this.field_73584_f[var4] = var6.compressedData;
		}

		this.chunkDataBuffer = new byte[var3];
		System.arraycopy(chunkDataNotCompressed, 0, this.chunkDataBuffer, 0, var3);
		this.dataLength = this.chunkDataBuffer.length;

	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		// not used in server
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeShort(this.chunkPostX.length);
		par1DataOutputStream.writeInt(this.dataLength | 0x10000000);
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

	public int getNumberOfChunkInPacket() {
		return this.chunkPostX.length;
	}
}
