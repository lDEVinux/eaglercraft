package net.lax1dude.eaglercraft.sp.ipc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;

public class IPCPacket16NBTList implements IPCPacketBase {

	public static final int ID = 0x16;
	
	public static final int WORLD_LIST = 0x0;

	public int opCode;
	public final List<byte[]> tagList;
	public final List<NBTTagCompound> nbtTagList;
	
	public IPCPacket16NBTList() {
		tagList = new LinkedList();
		nbtTagList = new LinkedList();
	}
	
	public IPCPacket16NBTList(int opcode, NBTTagCompound[] list) {
		this(opcode, Arrays.asList(list));
	}
	
	public IPCPacket16NBTList(int opcode, List<NBTTagCompound> list) {
		tagList = new LinkedList();
		nbtTagList = list;
		for(int i = 0, size = list.size(); i < size; ++i) {
			NBTTagCompound tag = list.get(i);
			try {
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				CompressedStreamTools.write(tag, new DataOutputStream(bao));
				tagList.add(bao.toByteArray());
			}catch(IOException e) {
				System.err.println("Failed to write tag '" + tag.getName() + "' (#" + i + ") in IPCPacket16NBTList");
			}
		}
		opCode = opcode;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		tagList.clear();
		nbtTagList.clear();
		opCode = bin.readInt();
		int count = bin.readInt();
		for(int i = 0; i < count; ++i) {
			byte[] toRead = new byte[bin.readInt()];
			bin.readFully(toRead);
			tagList.add(toRead);
			try {
				nbtTagList.add(CompressedStreamTools.read(new DataInputStream(new ByteArrayInputStream(toRead))));
			}catch(IOException e) {
				System.err.println("Failed to read tag #" + i + " in IPCPacket16NBTList");
			}
		}
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeInt(opCode);
		bin.writeInt(tagList.size());
		for(byte[] str : tagList) {
			bin.writeInt(str.length);
			bin.write(str);
		}
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		int len = 8;
		for(byte[] str : tagList) {
			len += 4;
			len += str.length;
		}
		return len;
	}

}
