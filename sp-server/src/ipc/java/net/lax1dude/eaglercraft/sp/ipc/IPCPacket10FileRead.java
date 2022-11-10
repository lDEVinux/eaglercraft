package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket10FileRead implements IPCPacketBase {
	
	public static final int ID = 0x10;

	public String file;
	
	public IPCPacket10FileRead() {
	}
	
	public IPCPacket10FileRead(String file) {
		this.file = file;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		file = bin.readUTF();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(file);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(file);
	}

}
