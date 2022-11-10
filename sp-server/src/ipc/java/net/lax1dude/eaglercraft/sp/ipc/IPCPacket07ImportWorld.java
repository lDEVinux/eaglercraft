package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket07ImportWorld implements IPCPacketBase {
	
	public static final int ID = 0x07;

	public String worldName;
	public byte[] worldData;
	public byte worldFormat;
	
	public static final byte WORLD_FORMAT_EAG = 0x00;
	public static final byte WORLD_FORMAT_MCA = 0x01;
	
	public IPCPacket07ImportWorld() {
	}
	
	public IPCPacket07ImportWorld(String worldName, byte[] worldData, byte worldFormat) {
		this.worldName = worldName;
		this.worldData = worldData;
		this.worldFormat = worldFormat;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		worldData = new byte[bin.readInt()];
		worldFormat = bin.readByte();
		bin.readFully(worldData);
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeInt(worldData.length);
		bin.writeByte(worldFormat);
		bin.write(worldData);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + worldData.length + 5;
	}

}
