package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket12FileWrite implements IPCPacketBase {
	
	public static final int ID = 0x12;

	public String path;
	public byte[] data;
	
	public IPCPacket12FileWrite() {
	}
	
	public IPCPacket12FileWrite(String path, byte[] data) {
		this.path = path;
		this.data = data;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		path = bin.readUTF();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(path);
		bin.writeInt(data.length);
		bin.write(data);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(path) + 4 + data.length;
	}

}
