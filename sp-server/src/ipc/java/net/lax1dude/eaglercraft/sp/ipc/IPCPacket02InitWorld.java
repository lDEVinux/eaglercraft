package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket02InitWorld implements IPCPacketBase {
	
	public static final int ID = 0x02;

	public String worldName;
	public byte gamemode;
	public byte worldType;
	public String worldArgs;
	public long seed;
	public boolean cheats;
	public boolean structures;
	public boolean bonusChest;
	
	public IPCPacket02InitWorld() {
	}
	
	public IPCPacket02InitWorld(String worldName, int gamemode, int worldType, String worldArgs, long seed, boolean cheats, boolean structures, boolean bonusChest) {
		this.worldName = worldName;
		this.gamemode = (byte)gamemode;
		this.worldType = (byte)worldType;
		this.worldArgs = worldArgs;
		this.seed = seed;
		this.cheats = cheats;
		this.structures = structures;
		this.bonusChest = bonusChest;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		gamemode = bin.readByte();
		worldType = bin.readByte();
		worldArgs = bin.readUTF();
		seed = bin.readLong();
		cheats = bin.readBoolean();
		structures = bin.readBoolean();
		bonusChest = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeByte(gamemode);
		bin.writeByte(worldType);
		bin.writeUTF(worldArgs);
		bin.writeLong(seed);
		bin.writeBoolean(cheats);
		bin.writeBoolean(structures);
		bin.writeBoolean(bonusChest);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + 1 + 1 + IPCPacketBase.strLen(worldArgs) + 8 + 1 + 1 + 1;
	}

}
