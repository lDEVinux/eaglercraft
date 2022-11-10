package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IPCPacket14StringList implements IPCPacketBase {
	
	public static final int ID = 0x14;

	public static final int FILE_LIST = 0x0;
	public static final int LOCALE = 0x1;
	public static final int STAT_GUID = 0x2;
	public static final int SERVER_TPS = 0x3;

	public int opCode;
	public final List<String> stringList;
	
	public IPCPacket14StringList() {
		stringList = new ArrayList();
	}
	
	public IPCPacket14StringList(int opcode, String[] list) {
		stringList = new ArrayList();
		for(String s : list) {
			s = s.trim();
			if(s.length() > 0) {
				stringList.add(s);
			}
		}
		this.opCode = opcode;
	}
	
	public IPCPacket14StringList(int opcode, List<String> list) {
		stringList = new ArrayList();
		for(String s : list) {
			s = s.trim();
			if(s.length() > 0) {
				stringList.add(s);
			}
		}
		this.opCode = opcode;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		stringList.clear();
		opCode = bin.readByte();
		int len = bin.readInt();
		for(int i = 0; i < len; ++i) {
			stringList.add(bin.readUTF());
		}
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeByte(opCode);
		bin.writeInt(stringList.size());
		for(String str : stringList) {
			bin.writeUTF(str);
		}
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		int len = 5;
		for(String str : stringList) {
			len += IPCPacketBase.strLen(str);
		}
		return len;
	}

}
