package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket09RequestResponse implements IPCPacketBase {
	
	public static final int ID = 0x09;

	public byte[] response;
	
	public IPCPacket09RequestResponse() {
	}
	
	public IPCPacket09RequestResponse(byte[] dat) {
		this.response = dat;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		response = new byte[bin.readInt()];
		bin.readFully(response);
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeInt(response.length);
		bin.write(response);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return 4 + response.length;
	}

}
