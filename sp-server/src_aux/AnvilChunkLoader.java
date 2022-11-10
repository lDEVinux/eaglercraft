package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO {
	private List chunksToRemove = new ArrayList();
	private Set pendingAnvilChunksCoordinates = new HashSet();
	private Object syncLockObject = new Object();

	/** Save directory for chunks using the Anvil format */
	private final File chunkSaveLocation;

	public AnvilChunkLoader(File par1File) {
		this.chunkSaveLocation = par1File;
	}

	/**
	 * Loads the specified(XZ) chunk into the specified world.
	 */
	public Chunk loadChunk(World par1World, int par2, int par3) throws IOException {
		NBTTagCompound var4 = null;
		ChunkCoordIntPair var5 = new ChunkCoordIntPair(par2, par3);
		Object var6 = this.syncLockObject;

		synchronized (this.syncLockObject) {
			if (this.pendingAnvilChunksCoordinates.contains(var5)) {
				for (int var7 = 0; var7 < this.chunksToRemove.size(); ++var7) {
					if (((AnvilChunkLoaderPending) this.chunksToRemove.get(var7)).chunkCoordinate.equals(var5)) {
						var4 = ((AnvilChunkLoaderPending) this.chunksToRemove.get(var7)).nbtTags;
						break;
					}
				}
			}
		}

		if (var4 == null) {
			DataInputStream var10 = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, par2, par3);

			if (var10 == null) {
				return null;
			}

			var4 = CompressedStreamTools.read(var10);
		}

		return this.checkedReadChunkFromNBT(par1World, par2, par3, var4);
	}

	/**
	 * Wraps readChunkFromNBT. Checks the coordinates and several NBT tags.
	 */
	protected Chunk checkedReadChunkFromNBT(World par1World, int par2, int par3, NBTTagCompound par4NBTTagCompound) {
		if (!par4NBTTagCompound.hasKey("Level")) {
			par1World.getWorldLogAgent()
					.logSevere("Chunk file at " + par2 + "," + par3 + " is missing level data, skipping");
			return null;
		} else if (!par4NBTTagCompound.getCompoundTag("Level").hasKey("Sections")) {
			par1World.getWorldLogAgent()
					.logSevere("Chunk file at " + par2 + "," + par3 + " is missing block data, skipping");
			return null;
		} else {
			Chunk var5 = this.readChunkFromNBT(par1World, par4NBTTagCompound.getCompoundTag("Level"));

			if (!var5.isAtLocation(par2, par3)) {
				par1World.getWorldLogAgent()
						.logSevere("Chunk file at " + par2 + "," + par3
								+ " is in the wrong location; relocating. (Expected " + par2 + ", " + par3 + ", got "
								+ var5.xPosition + ", " + var5.zPosition + ")");
				par4NBTTagCompound.setInteger("xPos", par2);
				par4NBTTagCompound.setInteger("zPos", par3);
				var5 = this.readChunkFromNBT(par1World, par4NBTTagCompound.getCompoundTag("Level"));
			}

			return var5;
		}
	}

	public void saveChunk(World par1World, Chunk par2Chunk) throws MinecraftException, IOException {
		par1World.checkSessionLock();

		try {
			NBTTagCompound var3 = new NBTTagCompound();
			NBTTagCompound var4 = new NBTTagCompound();
			var3.setTag("Level", var4);
			this.writeChunkToNBT(par2Chunk, par1World, var4);
			this.addChunkToPending(par2Chunk.getChunkCoordIntPair(), var3);
		} catch (Exception var5) {
			var5.printStackTrace();
		}
	}

	protected void addChunkToPending(ChunkCoordIntPair par1ChunkCoordIntPair, NBTTagCompound par2NBTTagCompound) {
		Object var3 = this.syncLockObject;

		synchronized (this.syncLockObject) {
			if (this.pendingAnvilChunksCoordinates.contains(par1ChunkCoordIntPair)) {
				for (int var4 = 0; var4 < this.chunksToRemove.size(); ++var4) {
					if (((AnvilChunkLoaderPending) this.chunksToRemove.get(var4)).chunkCoordinate
							.equals(par1ChunkCoordIntPair)) {
						this.chunksToRemove.set(var4,
								new AnvilChunkLoaderPending(par1ChunkCoordIntPair, par2NBTTagCompound));
						return;
					}
				}
			}

			this.chunksToRemove.add(new AnvilChunkLoaderPending(par1ChunkCoordIntPair, par2NBTTagCompound));
			this.pendingAnvilChunksCoordinates.add(par1ChunkCoordIntPair);
			ThreadedFileIOBase.threadedIOInstance.queueIO(this);
		}
	}

	/**
	 * Returns a boolean stating if the write was unsuccessful.
	 */
	public boolean writeNextIO() {
		AnvilChunkLoaderPending var1 = null;
		Object var2 = this.syncLockObject;

		synchronized (this.syncLockObject) {
			if (this.chunksToRemove.isEmpty()) {
				return false;
			}

			var1 = (AnvilChunkLoaderPending) this.chunksToRemove.remove(0);
			this.pendingAnvilChunksCoordinates.remove(var1.chunkCoordinate);
		}

		if (var1 != null) {
			try {
				this.writeChunkNBTTags(var1);
			} catch (Exception var4) {
				var4.printStackTrace();
			}
		}

		return true;
	}

	private void writeChunkNBTTags(AnvilChunkLoaderPending par1AnvilChunkLoaderPending) throws IOException {
		DataOutputStream var2 = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation,
				par1AnvilChunkLoaderPending.chunkCoordinate.chunkXPos,
				par1AnvilChunkLoaderPending.chunkCoordinate.chunkZPos);
		CompressedStreamTools.write(par1AnvilChunkLoaderPending.nbtTags, var2);
		var2.close();
	}

	/**
	 * Save extra data associated with this Chunk not normally saved during
	 * autosave, only during chunk unload. Currently unused.
	 */
	public void saveExtraChunkData(World par1World, Chunk par2Chunk) {
	}

	/**
	 * Called every World.tick()
	 */
	public void chunkTick() {
	}

	/**
	 * Save extra data not associated with any Chunk. Not saved during autosave,
	 * only during world unload. Currently unused.
	 */
	public void saveExtraData() {
		while (this.writeNextIO()) {
			;
		}
	}

	/**
	 * Writes the Chunk passed as an argument to the NBTTagCompound also passed,
	 * using the World argument to retrieve the Chunk's last update time.
	 */
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

	/**
	 * Reads the data stored in the passed NBTTagCompound and creates a Chunk with
	 * that data in the passed World. Returns the created Chunk.
	 */
	private Chunk readChunkFromNBT(World par1World, NBTTagCompound par2NBTTagCompound) {
		int var3 = par2NBTTagCompound.getInteger("xPos");
		int var4 = par2NBTTagCompound.getInteger("zPos");
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
}
