package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket05RequestData implements IPCPacketBase {
	
	public static final int ID = 0x05;

	public static final byte REQUEST_LEVEL_DAT = 0x00;
	public static final byte REQUEST_LEVEL_EAG = 0x01;
	public static final byte REQUEST_LEVEL_MCA = 0x02;

	public String worldName;
	public byte request;
	
	public IPCPacket05RequestData() {
	}
	
	public IPCPacket05RequestData(String worldName, byte request) {
		this.worldName = worldName;
		this.request = request;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		request = bin.readByte();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeByte(request);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + 1;
	}

}
