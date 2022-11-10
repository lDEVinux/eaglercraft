package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket0BPause implements IPCPacketBase {
	
	public static final int ID = 0x0B;

	public boolean pause;
	
	public IPCPacket0BPause() {
	}
	
	public IPCPacket0BPause(boolean pause) {
		this.pause = pause;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		pause = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeBoolean(pause);
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
