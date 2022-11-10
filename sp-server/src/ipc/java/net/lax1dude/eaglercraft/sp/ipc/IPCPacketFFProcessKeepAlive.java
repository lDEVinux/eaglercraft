package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacketFFProcessKeepAlive implements IPCPacketBase {
	
	public static final int ID = 0xFF;

	public static final int KEEPALIVE = 0;
	public static final int FAILURE = 0xFE;
	
	public int ack;
	
	public IPCPacketFFProcessKeepAlive() {
	}
	
	public IPCPacketFFProcessKeepAlive(int ack) {
		this.ack = ack;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		ack = bin.readUnsignedByte();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeByte(ack);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return 1;
	}

}
