package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IPCPacket15ThrowException implements IPCPacketBase {
	
	public static final int ID = 0x15;
	
	public String errorMessage;
	public final List<String> stackTrace;
	
	public IPCPacket15ThrowException() {
		stackTrace = new ArrayList();
	}
	
	public IPCPacket15ThrowException(String errorMessage, String[] list) {
		stackTrace = new ArrayList(Arrays.asList(list));
		this.errorMessage = errorMessage;
	}
	
	public IPCPacket15ThrowException(String errorMessage, List<String> list) {
		stackTrace = list;
		this.errorMessage = errorMessage;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		stackTrace.clear();
		errorMessage = bin.readUTF();
		int len = bin.readInt();
		for(int i = 0; i < len; ++i) {
			stackTrace.add(bin.readUTF());
		}
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(errorMessage);
		bin.writeInt(stackTrace.size());
		for(String str : stackTrace) {
			bin.writeUTF(str);
		}
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		int len = 4 + IPCPacketBase.strLen(errorMessage);
		for(String str : stackTrace) {
			len += IPCPacketBase.strLen(str);
		}
		return len;
	}
	
	public void log() {
		System.err.println("Integrated server exception: " + errorMessage);
		for(String s : stackTrace) {
			System.err.println("    at " + s);
		}
	}

}
