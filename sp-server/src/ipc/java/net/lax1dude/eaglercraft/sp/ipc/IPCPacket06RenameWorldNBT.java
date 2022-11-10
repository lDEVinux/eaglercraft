package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket06RenameWorldNBT implements IPCPacketBase {
	
	public static final int ID = 0x06;

	public String worldName;
	public String displayName;
	
	public IPCPacket06RenameWorldNBT() {
	}
	
	public IPCPacket06RenameWorldNBT(String worldName, String displayName) {
		this.worldName = worldName;
		this.displayName = displayName;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		this.worldName = bin.readUTF();
		this.displayName = bin.readUTF();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeUTF(displayName);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + IPCPacketBase.strLen(displayName);
	}

}
