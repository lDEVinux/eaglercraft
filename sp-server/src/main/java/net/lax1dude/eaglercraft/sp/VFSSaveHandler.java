package net.lax1dude.eaglercraft.sp;

import java.io.IOException;
import java.util.HashMap;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IPlayerFileData;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;

public class VFSSaveHandler implements ISaveHandler, IPlayerFileData {
	
	public final VFile worldDirectory;
	
	private final HashMap<Integer, VFSChunkLoader> chunkLoaders = new HashMap();
	
	public VFSSaveHandler(VFile worldDirectory) {
		this.worldDirectory = worldDirectory;
	}

	@Override
	public WorldInfo loadWorldInfo() {
		
		byte[] level_dat_bin = (new VFile(worldDirectory, "level.dat")).getAllBytes();
		
		if(level_dat_bin == null) {
			return null;
		}
		
		try {
			NBTTagCompound level_dat = CompressedStreamTools.decompress(level_dat_bin);
			return new WorldInfo(level_dat.getCompoundTag("Data"));
		}catch(Throwable t) {
			System.err.println("Could not parse level.dat!");
			t.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void checkSessionLock() throws MinecraftException {
		// no
	}

	@Override
	public IChunkLoader getChunkLoader(WorldProvider var1) {
		VFSChunkLoader loader = chunkLoaders.get(var1.dimensionId);
		
		if(loader == null) {
			loader = new VFSChunkLoader(new VFile(worldDirectory, "level" + var1.dimensionId));
			chunkLoaders.put(var1.dimensionId, loader);
		}
		
		return loader;
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
		NBTTagCompound var3 = var2 != null ? var1.cloneNBTCompound(var2) : var1.getNBTTagCompound();
		NBTTagCompound var4 = new NBTTagCompound();
		var4.setTag("Data", var3);
		
		VFile level_dat = new VFile(worldDirectory, "level.dat");
		
		byte[] compressed;
		
		try {
			compressed = CompressedStreamTools.compress(var4);
		}catch(IOException e) {
			System.err.println("Could not serialize \"" + level_dat + "\"");
			e.printStackTrace();
			return;
		}
		
		if(!level_dat.setAllBytes(compressed)) {
			System.err.println("Could not save \"" + level_dat + "\" to filesystem");
		}
	}

	@Override
	public void saveWorldInfo(WorldInfo var1) {
		saveWorldInfoWithPlayer(var1, null);
	}

	@Override
	public IPlayerFileData getPlayerNBTManager() {
		return this;
	}

	@Override
	public void flush() {
		
	}

	@Override
	public VFile getMapFileFromName(String var1) {
		return new VFile(worldDirectory, "data", var1 + ".dat");
	}

	@Override
	public String getWorldDirectoryName() {
		return worldDirectory.toString();
	}

	@Override
	public void writePlayerData(EntityPlayer var1) {
		NBTTagCompound var2 = new NBTTagCompound();
		var1.writeToNBT(var2);
		
		byte[] bin;
		
		try {
			bin = CompressedStreamTools.compress(var2);
		}catch(Throwable t) {
			System.err.println("Could not serialize player data for \"" + var1.username + "\"");
			t.printStackTrace();
			return;
		}
		
		VFile playerData = new VFile(worldDirectory, "player", var1.username.toLowerCase() + ".dat");
		
		if(!playerData.setAllBytes(bin)) {
			System.err.println("Could not write player data for \"" + var1.username + "\" to file \"" + playerData.toString() + "\"");
		}
	}

	@Override
	public NBTTagCompound readPlayerData(EntityPlayer var1) {
		VFile playerData = new VFile(worldDirectory, "player", var1.username.toLowerCase() + ".dat");
		
		NBTTagCompound ret = null;
		
		byte[] playerBin = playerData.getAllBytes();
		if(playerBin != null) {
			try {
				ret = CompressedStreamTools.decompress(playerBin);
				var1.readFromNBT(ret);
			}catch(IOException e) {
				System.err.println("Could not deserialize player data for \"" + var1.username + "\"");
				e.printStackTrace();
			}
		}
		
		return ret;
	}

	@Override
	public String[] getAvailablePlayerDat() {
		return null;
	}
	
	public static String worldNameToFolderName(String par1Str) {
		par1Str = par1Str.replaceAll("[\\./\"]", "_");
		
		boolean shit = true;
		while(shit) {
			shit = (new VFile("worlds", par1Str, "level.dat")).exists();
			if(shit) {
				par1Str = par1Str + "_";
			}
		}

		return par1Str;
	}

}
