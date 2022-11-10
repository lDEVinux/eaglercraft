package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Packet60Explosion extends Packet {
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	public float explosionSize;
	public List chunkPositionRecords;

	/** X velocity of the player being pushed by the explosion */
	private float playerVelocityX;

	/** Y velocity of the player being pushed by the explosion */
	private float playerVelocityY;

	/** Z velocity of the player being pushed by the explosion */
	private float playerVelocityZ;

	public Packet60Explosion() {
	}

	public Packet60Explosion(double par1, double par3, double par5, float par7, List par8List, Vec3 par9Vec3) {
		this.explosionX = par1;
		this.explosionY = par3;
		this.explosionZ = par5;
		this.explosionSize = par7;
		this.chunkPositionRecords = new ArrayList(par8List);

		if (par9Vec3 != null) {
			this.playerVelocityX = (float) par9Vec3.xCoord;
			this.playerVelocityY = (float) par9Vec3.yCoord;
			this.playerVelocityZ = (float) par9Vec3.zCoord;
		}
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.explosionX = par1DataInputStream.readDouble();
		this.explosionY = par1DataInputStream.readDouble();
		this.explosionZ = par1DataInputStream.readDouble();
		this.explosionSize = par1DataInputStream.readFloat();
		int var2 = par1DataInputStream.readInt();
		this.chunkPositionRecords = new ArrayList(var2);
		int var3 = (int) this.explosionX;
		int var4 = (int) this.explosionY;
		int var5 = (int) this.explosionZ;

		for (int var6 = 0; var6 < var2; ++var6) {
			int var7 = par1DataInputStream.readByte() + var3;
			int var8 = par1DataInputStream.readByte() + var4;
			int var9 = par1DataInputStream.readByte() + var5;
			this.chunkPositionRecords.add(new ChunkPosition(var7, var8, var9));
		}

		this.playerVelocityX = par1DataInputStream.readFloat();
		this.playerVelocityY = par1DataInputStream.readFloat();
		this.playerVelocityZ = par1DataInputStream.readFloat();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeDouble(this.explosionX);
		par1DataOutputStream.writeDouble(this.explosionY);
		par1DataOutputStream.writeDouble(this.explosionZ);
		par1DataOutputStream.writeFloat(this.explosionSize);
		par1DataOutputStream.writeInt(this.chunkPositionRecords.size());
		int var2 = (int) this.explosionX;
		int var3 = (int) this.explosionY;
		int var4 = (int) this.explosionZ;
		Iterator var5 = this.chunkPositionRecords.iterator();

		while (var5.hasNext()) {
			ChunkPosition var6 = (ChunkPosition) var5.next();
			int var7 = var6.x - var2;
			int var8 = var6.y - var3;
			int var9 = var6.z - var4;
			par1DataOutputStream.writeByte(var7);
			par1DataOutputStream.writeByte(var8);
			par1DataOutputStream.writeByte(var9);
		}

		par1DataOutputStream.writeFloat(this.playerVelocityX);
		par1DataOutputStream.writeFloat(this.playerVelocityY);
		par1DataOutputStream.writeFloat(this.playerVelocityZ);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleExplosion(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 32 + this.chunkPositionRecords.size() * 3 + 3;
	}
}
