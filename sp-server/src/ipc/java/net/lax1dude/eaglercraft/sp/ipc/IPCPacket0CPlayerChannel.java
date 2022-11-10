package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket0CPlayerChannel implements IPCPacketBase {
	
	public static final int ID = 0x0C;

	public String channel;
	public boolean open;
	
	public IPCPacket0CPlayerChannel() {
	}
	
	public IPCPacket0CPlayerChannel(String channel, boolean open) {
		this.channel = channel;
		this.open = open;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		channel = bin.readUTF();
		open = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(channel);
		bin.writeBoolean(open);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(channel) + 1;
	}

}
