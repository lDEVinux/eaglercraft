package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet62LevelSound extends Packet {
	/** e.g. step.grass */
	private String soundName;

	/** Effect X multiplied by 8 */
	private int effectX;

	/** Effect Y multiplied by 8 */
	private int effectY = Integer.MAX_VALUE;

	/** Effect Z multiplied by 8 */
	private int effectZ;

	/** 1 is 100%. Can be more. */
	private float volume;

	/** 63 is 100%. Can be more. */
	private int pitch;

	public Packet62LevelSound() {
	}

	public Packet62LevelSound(String par1Str, double par2, double par4, double par6, float par8, float par9) {
		this.soundName = par1Str;
		this.effectX = (int) (par2 * 8.0D);
		this.effectY = (int) (par4 * 8.0D);
		this.effectZ = (int) (par6 * 8.0D);
		this.volume = par8;
		this.pitch = (int) (par9 * 63.0F);

		if (this.pitch < 0) {
			this.pitch = 0;
		}

		if (this.pitch > 255) {
			this.pitch = 255;
		}
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.soundName = readString(par1DataInputStream, 32);
		this.effectX = par1DataInputStream.readInt();
		this.effectY = par1DataInputStream.readInt();
		this.effectZ = par1DataInputStream.readInt();
		this.volume = par1DataInputStream.readFloat();
		this.pitch = par1DataInputStream.readUnsignedByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeString(this.soundName, par1DataOutputStream);
		par1DataOutputStream.writeInt(this.effectX);
		par1DataOutputStream.writeInt(this.effectY);
		par1DataOutputStream.writeInt(this.effectZ);
		par1DataOutputStream.writeFloat(this.volume);
		par1DataOutputStream.writeByte(this.pitch);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleLevelSound(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 24;
	}
}
