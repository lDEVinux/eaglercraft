package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket0FListFiles implements IPCPacketBase {
	
	public static final int ID = 0x0F;

	public String path;
	
	public IPCPacket0FListFiles() {
	}
	
	public IPCPacket0FListFiles(String path) {
		this.path = path;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		this.path = bin.readUTF();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(path);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(path);
	}

}
