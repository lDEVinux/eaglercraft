package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket0DProgressUpdate implements IPCPacketBase {
	
	public static final int ID = 0x0D;

	public String updateMessage;
	public float updateProgress;
	
	public IPCPacket0DProgressUpdate() {
	}
	
	public IPCPacket0DProgressUpdate(String updateMessage, float updateProgress) {
		this.updateMessage = updateMessage == null ? "" : updateMessage;
		this.updateProgress = updateProgress;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		updateMessage = bin.readUTF();
		updateProgress = bin.readFloat();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(updateMessage);
		bin.writeFloat(updateProgress);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(updateMessage) + 4;
	}

}
