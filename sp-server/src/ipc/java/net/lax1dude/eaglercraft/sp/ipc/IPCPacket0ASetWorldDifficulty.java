package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket0ASetWorldDifficulty implements IPCPacketBase {
	
	public static final int ID = 0x0A;
	
	public byte difficulty;
	
	public IPCPacket0ASetWorldDifficulty() {
	}
	
	public IPCPacket0ASetWorldDifficulty(byte difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		difficulty = bin.readByte();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeByte(difficulty);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return 1;
	}

}
