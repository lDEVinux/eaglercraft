package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket01StopServer implements IPCPacketBase {
	
	public static final int ID = 0x01;
	
	public IPCPacket01StopServer() {
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return 0;
	}

}
