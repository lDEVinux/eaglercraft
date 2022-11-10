package net.lax1dude.eaglercraft.sp;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.ExtendedBlockStorage;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NextTickListEntry;
import net.minecraft.src.NibbleArray;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class VFSChunkLoader implements IChunkLoader {
	
	public final VFile chunkDirectory;
	
	private static final String hex = "0123456789ABCDEF";
	
	public static String getChunkPath(int x, int z) {
		int unsignedX = x + 1900000;
		int unsignedZ = z + 1900000;
		
		char[] path = new char[12];
		for(int i = 5; i >= 0; --i) {
			path[i] = hex.charAt((unsignedX >> (i * 4)) & 0xF);
			path[i + 6] = hex.charAt((unsignedZ >> (i * 4)) & 0xF);
		}
		
		return new String(path);
	}

	public static ChunkCoordIntPair getChunkCoords(String filename) {
		String strX = filename.substring(0, 6);
		String strZ = filename.substring(6);

		int retX = 0;
		int retZ = 0;

		for(int i = 0; i < 6; ++i) {
			retX |= hex.indexOf(strX.charAt(i)) << (i << 2);
			retZ |= hex.indexOf(strZ.charAt(i)) << (i << 2);
		}

		return new ChunkCoordIntPair(retX - 1900000, retZ - 1900000);
	}
	
	public VFSChunkLoader(VFile chunkDirectory) {
		this.chunkDirectory = chunkDirectory;
	}

	@Override
	public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
		VFile file = new VFile(chunkDirectory, getChunkPath(var2, var3) + ".dat");
		byte[] bytes = file.getAllBytes();
		
		if(bytes == null) {
			return null;
		}
		
		try {
			NBTTagCompound nbt = CompressedStreamTools.decompress(bytes);
			nbt = nbt.getCompoundTag("Level");
			return readChunkFromNBT(var1, nbt, var2, var3);
		}catch(Throwable t) {
			file.delete();
			System.err.println("Corrupted chunk has been deleted: [" + var2 + ", " + var3 + "]");
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveChunk(World var1, Chunk var2) throws MinecraftException, IOException {
		
		NBTTagCompound chunkFile = new NBTTagCompound();
		this.writeChunkToNBT(var2, var1, chunkFile);
		
		byte[] save;
		
		try {
			NBTTagCompound chunkFileSave = new NBTTagCompound();
			chunkFileSave.setCompoundTag("Level", chunkFile);
			save = CompressedStreamTools.compressChunk(chunkFileSave);
		}catch(IOException e) {
			System.err.println("Corrupted chunk could not be serialized: [" + var2.xPosition + ", " + var2.zPosition + "]");
			return;
		}
		
		VFile file = new VFile(chunkDirectory, getChunkPath(var2.xPosition, var2.zPosition) + ".dat");
		
		if(!file.setAllBytes(save)) {
			System.err.println("Corrupted chunk could not be written: [" + var2.xPosition + ", " + var2.zPosition + "] to file \"" + file.toString() + "\")");
		}
		
	}

	@Override
	public void saveExtraChunkData(World var1, Chunk var2) {
		// ?
	}

	@Override
	public void chunkTick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveExtraData() {
		// unused
	}
	
	private Chunk readChunkFromNBT(World par1World, NBTTagCompound par2NBTTagCompound, int x, int z) {
		int var3 = x; //par2NBTTagCompound.getInteger("xPos");
		int var4 = z; //par2NBTTagCompound.getInteger("zPos");
		Chunk var5 = new Chunk(par1World, var3, var4);
		var5.heightMap = par2NBTTagCompound.getIntArray("HeightMap");
		var5.isTerrainPopulated = par2NBTTagCompound.getBoolean("TerrainPopulated");
		NBTTagList var6 = par2NBTTagCompound.getTagList("Sections");
		byte var7 = 16;
		ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];
		boolean var9 = !par1World.provider.hasNoSky;

		for (int var10 = 0; var10 < var6.tagCount(); ++var10) {
			NBTTagCompound var11 = (NBTTagCompound) var6.tagAt(var10);
			byte var12 = var11.getByte("Y");
			ExtendedBlockStorage var13 = new ExtendedBlockStorage(var12 << 4, var9);
			var13.setBlockLSBArray(var11.getByteArray("Blocks"));

			if (var11.hasKey("Add")) {
				var13.setBlockMSBArray(new NibbleArray(var11.getByteArray("Add"), 4));
			}

			var13.setBlockMetadataArray(new NibbleArray(var11.getByteArray("Data"), 4));
			var13.setBlocklightArray(new NibbleArray(var11.getByteArray("BlockLight"), 4));

			if (var9) {
				var13.setSkylightArray(new NibbleArray(var11.getByteArray("SkyLight"), 4));
			}

			var13.removeInvalidBlocks();
			var8[var12] = var13;
		}

		var5.setStorageArrays(var8);

		if (par2NBTTagCompound.hasKey("Biomes")) {
			var5.setBiomeArray(par2NBTTagCompound.getByteArray("Biomes"));
		}

		NBTTagList var17 = par2NBTTagCompound.getTagList("Entities");

		if (var17 != null) {
			for (int var18 = 0; var18 < var17.tagCount(); ++var18) {
				NBTTagCompound var20 = (NBTTagCompound) var17.tagAt(var18);
				Entity var22 = EntityList.createEntityFromNBT(var20, par1World);
				var5.hasEntities = true;

				if (var22 != null) {
					var5.addEntity(var22);
					Entity var14 = var22;

					for (NBTTagCompound var15 = var20; var15.hasKey("Riding"); var15 = var15.getCompoundTag("Riding")) {
						Entity var16 = EntityList.createEntityFromNBT(var15.getCompoundTag("Riding"), par1World);

						if (var16 != null) {
							var5.addEntity(var16);
							var14.mountEntity(var16);
						}

						var14 = var16;
					}
				}
			}
		}

		NBTTagList var19 = par2NBTTagCompound.getTagList("TileEntities");

		if (var19 != null) {
			for (int var21 = 0; var21 < var19.tagCount(); ++var21) {
				NBTTagCompound var24 = (NBTTagCompound) var19.tagAt(var21);
				TileEntity var26 = TileEntity.createAndLoadEntity(var24);

				if (var26 != null) {
					var5.addTileEntity(var26);
				}
			}
		}

		if (par2NBTTagCompound.hasKey("TileTicks")) {
			NBTTagList var23 = par2NBTTagCompound.getTagList("TileTicks");

			if (var23 != null) {
				for (int var25 = 0; var25 < var23.tagCount(); ++var25) {
					NBTTagCompound var27 = (NBTTagCompound) var23.tagAt(var25);
					par1World.scheduleBlockUpdateFromLoad(var27.getInteger("x"), var27.getInteger("y"),
							var27.getInteger("z"), var27.getInteger("i"), var27.getInteger("t"), var27.getInteger("p"));
				}
			}
		}

		return var5;
	}
	
	private void writeChunkToNBT(Chunk par1Chunk, World par2World, NBTTagCompound par3NBTTagCompound) {
		par3NBTTagCompound.setInteger("xPos", par1Chunk.xPosition);
		par3NBTTagCompound.setInteger("zPos", par1Chunk.zPosition);
		par3NBTTagCompound.setLong("LastUpdate", par2World.getTotalWorldTime());
		par3NBTTagCompound.setIntArray("HeightMap", par1Chunk.heightMap);
		par3NBTTagCompound.setBoolean("TerrainPopulated", par1Chunk.isTerrainPopulated);
		ExtendedBlockStorage[] var4 = par1Chunk.getBlockStorageArray();
		NBTTagList var5 = new NBTTagList("Sections");
		boolean var6 = !par2World.provider.hasNoSky;
		ExtendedBlockStorage[] var7 = var4;
		int var8 = var4.length;
		NBTTagCompound var11;

		for (int var9 = 0; var9 < var8; ++var9) {
			ExtendedBlockStorage var10 = var7[var9];

			if (var10 != null) {
				var11 = new NBTTagCompound();
				var11.setByte("Y", (byte) (var10.getYLocation() >> 4 & 255));
				var11.setByteArray("Blocks", var10.getBlockLSBArray());

				if (var10.getBlockMSBArray() != null) {
					var11.setByteArray("Add", var10.getBlockMSBArray().data);
				}

				var11.setByteArray("Data", var10.getMetadataArray().data);
				var11.setByteArray("BlockLight", var10.getBlocklightArray().data);

				if (var6) {
					var11.setByteArray("SkyLight", var10.getSkylightArray().data);
				} else {
					var11.setByteArray("SkyLight", new byte[var10.getBlocklightArray().data.length]);
				}

				var5.appendTag(var11);
			}
		}

		par3NBTTagCompound.setTag("Sections", var5);
		par3NBTTagCompound.setByteArray("Biomes", par1Chunk.getBiomeArray());
		par1Chunk.hasEntities = false;
		NBTTagList var16 = new NBTTagList();
		Iterator var18;

		for (var8 = 0; var8 < par1Chunk.entityLists.length; ++var8) {
			var18 = par1Chunk.entityLists[var8].iterator();

			while (var18.hasNext()) {
				Entity var20 = (Entity) var18.next();
				var11 = new NBTTagCompound();

				if (var20.addEntityID(var11)) {
					par1Chunk.hasEntities = true;
					var16.appendTag(var11);
				}
			}
		}

		par3NBTTagCompound.setTag("Entities", var16);
		NBTTagList var17 = new NBTTagList();
		var18 = par1Chunk.chunkTileEntityMap.values().iterator();

		while (var18.hasNext()) {
			TileEntity var21 = (TileEntity) var18.next();
			var11 = new NBTTagCompound();
			var21.writeToNBT(var11);
			var17.appendTag(var11);
		}

		par3NBTTagCompound.setTag("TileEntities", var17);
		List var19 = par2World.getPendingBlockUpdates(par1Chunk, false);

		if (var19 != null) {
			long var22 = par2World.getTotalWorldTime();
			NBTTagList var12 = new NBTTagList();
			Iterator var13 = var19.iterator();

			while (var13.hasNext()) {
				NextTickListEntry var14 = (NextTickListEntry) var13.next();
				NBTTagCompound var15 = new NBTTagCompound();
				var15.setInteger("i", var14.blockID);
				var15.setInteger("x", var14.xCoord);
				var15.setInteger("y", var14.yCoord);
				var15.setInteger("z", var14.zCoord);
				var15.setInteger("t", (int) (var14.scheduledTime - var22));
				var15.setInteger("p", var14.field_82754_f);
				var12.appendTag(var15);
			}

			par3NBTTagCompound.setTag("TileTicks", var12);
		}
	}
	
}
