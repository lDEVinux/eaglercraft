package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket13FileCopyMove implements IPCPacketBase {
	
	public static final int ID = 0x13;

	public String fileOldName;
	public String fileNewName;
	public boolean copy;
	
	public IPCPacket13FileCopyMove() {
	}
	
	public IPCPacket13FileCopyMove(String fileOldName, String fileNewName, boolean copy) {
		this.fileOldName = fileOldName;
		this.fileNewName = fileNewName;
		this.copy = copy;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		fileOldName = bin.readUTF();
		fileNewName = bin.readUTF();
		copy = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(fileOldName);
		bin.writeUTF(fileNewName);
		bin.writeBoolean(copy);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(fileOldName) + IPCPacketBase.strLen(fileNewName) + 1;
	}

}
