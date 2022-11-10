package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet63WorldParticles extends Packet {
	/**
	 * The name of the particle to create. A list can be found at
	 * https://gist.github.com/thinkofdeath/5110835
	 */
	private String particleName;

	/** X position of the particle. */
	private float posX;

	/** Y position of the particle. */
	private float posY;

	/** Z position of the particle. */
	private float posZ;

	/**
	 * This is added to the X position after being multiplied by
	 * random.nextGaussian()
	 */
	private float offsetX;

	/**
	 * This is added to the Y position after being multiplied by
	 * random.nextGaussian()
	 */
	private float offsetY;

	/**
	 * This is added to the Z position after being multiplied by
	 * random.nextGaussian()
	 */
	private float offsetZ;

	/** The speed of each particle. */
	private float speed;

	/** The number of particles to create. */
	private int quantity;

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.particleName = readString(par1DataInputStream, 64);
		this.posX = par1DataInputStream.readFloat();
		this.posY = par1DataInputStream.readFloat();
		this.posZ = par1DataInputStream.readFloat();
		this.offsetX = par1DataInputStream.readFloat();
		this.offsetY = par1DataInputStream.readFloat();
		this.offsetZ = par1DataInputStream.readFloat();
		this.speed = par1DataInputStream.readFloat();
		this.quantity = par1DataInputStream.readInt();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeString(this.particleName, par1DataOutputStream);
		par1DataOutputStream.writeFloat(this.posX);
		par1DataOutputStream.writeFloat(this.posY);
		par1DataOutputStream.writeFloat(this.posZ);
		par1DataOutputStream.writeFloat(this.offsetX);
		par1DataOutputStream.writeFloat(this.offsetY);
		par1DataOutputStream.writeFloat(this.offsetZ);
		par1DataOutputStream.writeFloat(this.speed);
		par1DataOutputStream.writeInt(this.quantity);
	}

	public String getParticleName() {
		return this.particleName;
	}

	/**
	 * Gets the X position of the particle.
	 */
	public double getPositionX() {
		return (double) this.posX;
	}

	/**
	 * Gets the Y position of the particle.
	 */
	public double getPositionY() {
		return (double) this.posY;
	}

	/**
	 * Gets the Z position of the particle.
	 */
	public double getPositionZ() {
		return (double) this.posZ;
	}

	/**
	 * This is added to the X position after being multiplied by
	 * random.nextGaussian()
	 */
	public float getOffsetX() {
		return this.offsetX;
	}

	/**
	 * This is added to the Y position after being multiplied by
	 * random.nextGaussian()
	 */
	public float getOffsetY() {
		return this.offsetY;
	}

	/**
	 * This is added to the Z position after being multiplied by
	 * random.nextGaussian()
	 */
	public float getOffsetZ() {
		return this.offsetZ;
	}

	/**
	 * Gets the speed of the particles.
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * Gets the number of particles to create.
	 */
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleWorldParticles(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 64;
	}
}
