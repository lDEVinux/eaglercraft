package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket04RenameWorld implements IPCPacketBase {
	
	public static final int ID = 0x04;

	public String worldOldName;
	public String worldNewName;
	public String displayName;
	public boolean copy;
	
	public IPCPacket04RenameWorld() {
	}
	
	public IPCPacket04RenameWorld(String worldOldName, String worldNewName, String displayName, boolean copy) {
		this.worldOldName = worldOldName;
		this.worldNewName = worldNewName;
		this.displayName = displayName;
		this.copy = copy;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldOldName = bin.readUTF();
		worldNewName = bin.readUTF();
		displayName = bin.readUTF();
		copy = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldOldName);
		bin.writeUTF(worldNewName);
		bin.writeUTF(displayName);
		bin.writeBoolean(copy);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldOldName) + IPCPacketBase.strLen(worldNewName) + IPCPacketBase.strLen(displayName) + 1;
	}

}
