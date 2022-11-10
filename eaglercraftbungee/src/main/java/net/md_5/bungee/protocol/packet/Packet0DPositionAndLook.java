package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;

public class Packet0DPositionAndLook extends DefinedPacket {
	private double x;
	private double y;
	private double stance;
	private double z;
	private float yaw;
	private float pitch;
	private boolean onGround;

	private Packet0DPositionAndLook() {
		super(13);
	}

	public Packet0DPositionAndLook(final int x, final int y, final double stance, final double z, final float yaw,
			final float pitch, final boolean onGround) {
		this();
		this.x = x;
		this.y = y;
		this.stance = stance;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	@Override
	public void read(final ByteBuf buf) {
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.stance = buf.readDouble();
		this.z = buf.readDouble();
		this.yaw = buf.readFloat();
		this.pitch = buf.readFloat();
		this.onGround = buf.readBoolean();
	}

	@Override
	public void write(final ByteBuf buf) {
		buf.writeDouble(this.x);
		buf.writeDouble(this.y);
		buf.writeDouble(this.stance);
		buf.writeDouble(this.z);
		buf.writeFloat(this.yaw);
		buf.writeFloat(this.pitch);
		buf.writeBoolean(this.onGround);
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return "Packet0DPositionAndLook(tooLazyToFillThisInLOL)";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Packet0DPositionAndLook)) {
			return false;
		}
		final Packet0DPositionAndLook other = (Packet0DPositionAndLook) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		if (this.stance != other.stance) {
			return false;
		}
		if (this.z != other.z) {
			return false;
		}
		if (this.yaw != other.yaw) {
			return false;
		}
		if (this.pitch != other.pitch) {
			return false;
		}
		if (this.onGround != other.onGround) {
			return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + Double.hashCode(this.x);
		result = result * 31 + Double.hashCode(this.y);
		result = result * 31 + Double.hashCode(this.stance);
		result = result * 31 + Double.hashCode(this.z);
		result = result * 31 + Float.hashCode(this.yaw);
		result = result * 31 + Float.hashCode(this.pitch);
		result = result * 31 + Boolean.hashCode(this.onGround);
		return result;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Packet0DPositionAndLook;
	}
}
