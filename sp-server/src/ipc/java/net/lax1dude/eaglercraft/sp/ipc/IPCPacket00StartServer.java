package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket00StartServer implements IPCPacketBase {
	
	public static final int ID = 0x00;

	public String worldName;
	public String ownerName;
	public int initialDifficulty;
	
	public IPCPacket00StartServer() {
	}
	
	public IPCPacket00StartServer(String worldName, String ownerName, int initialDifficulty) {
		this.worldName = worldName;
		this.ownerName = ownerName;
		this.initialDifficulty = initialDifficulty;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		ownerName = bin.readUTF();
		initialDifficulty = bin.readByte();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeUTF(ownerName);
		bin.writeByte(initialDifficulty);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + IPCPacketBase.strLen(ownerName) + 1;
	}

}
