package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket18ClearPlayers implements IPCPacketBase {
	
	public static final int ID = 0x18;
	
	public String worldName = null;
	
	public IPCPacket18ClearPlayers(String worldName) {
		this.worldName = worldName;
	}
	
	public IPCPacket18ClearPlayers() {
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName);
	}

}
