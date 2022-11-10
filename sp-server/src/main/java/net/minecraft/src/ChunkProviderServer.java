package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChunkProviderServer implements IChunkProvider {
	private Set droppedChunksSet = new HashSet();

	/** a dummy chunk, returned in place of an actual chunk. */
	private Chunk dummyChunk;

	/**
	 * chunk generator object. Calls to load nonexistent chunks are forwarded to
	 * this object.
	 */
	private IChunkProvider serverChunkGenerator;
	private IChunkLoader chunkLoader;

	/**
	 * if set, this flag forces a request to load a chunk to load the chunk rather
	 * than defaulting to the dummy if possible
	 */
	public boolean chunkLoadOverride = true;

	/** map of chunk Id's to Chunk instances */
	private LongHashMap id2ChunkMap = new LongHashMap();
	private List loadedChunks = new ArrayList();
	private WorldServer worldObj;

	public ChunkProviderServer(WorldServer par1WorldServer, IChunkLoader par2IChunkLoader,
			IChunkProvider par3IChunkProvider) {
		this.dummyChunk = new EmptyChunk(par1WorldServer, 0, 0);
		this.worldObj = par1WorldServer;
		this.chunkLoader = par2IChunkLoader;
		this.serverChunkGenerator = par3IChunkProvider;
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	public boolean chunkExists(int par1, int par2) {
		return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
	}

	public void dropChunk(int par1, int par2) {
		/*
		if (this.worldObj.provider.canRespawnHere()) {
			ChunkCoordinates var3 = this.worldObj.getSpawnPoint();
			int var4 = par1 * 16 + 8 - var3.posX;
			int var5 = par2 * 16 + 8 - var3.posZ;
			short var6 = 128;

			if (var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6) {
				this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par1, par2)));
			}
		} else {
		*/
			this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par1, par2)));
		//}
	}

	/**
	 * marks all chunks for unload, ignoring those near the spawn
	 */
	public void unloadAllChunks() {
		Iterator var1 = this.loadedChunks.iterator();

		while (var1.hasNext()) {
			Chunk var2 = (Chunk) var1.next();
			this.dropChunk(var2.xPosition, var2.zPosition);
		}
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int par1, int par2) {
		long var3 = ChunkCoordIntPair.chunkXZ2Int(par1, par2);
		this.droppedChunksSet.remove(Long.valueOf(var3));
		Chunk var5 = (Chunk) this.id2ChunkMap.getValueByKey(var3);

		if (var5 == null) {
			var5 = this.loadChunkFromFile(par1, par2);

			if (var5 == null) {
				if (this.serverChunkGenerator == null) {
					var5 = this.dummyChunk;
				} else {
					++_g;
					var5 = this.serverChunkGenerator.provideChunk(par1, par2);
				}
			}else {
				++_r;
			}

			this.id2ChunkMap.add(var3, var5);
			this.loadedChunks.add(var5);

			if (var5 != null) {
				var5.onChunkLoad();
			}

			var5.populateChunk(this, this, par1, par2);
		}

		return var5;
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it will
	 * generates all the blocks for the specified chunk from the map seed and chunk
	 * seed
	 */
	public Chunk provideChunk(int par1, int par2) {
		Chunk var3 = (Chunk) this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
		return var3 == null ? (!this.worldObj.findingSpawnPoint && !this.chunkLoadOverride ? this.dummyChunk
				: this.loadChunk(par1, par2)) : var3;
	}

	private Chunk loadChunkFromFile(int par1, int par2) {
		if (this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, par1, par2);

				if (var3 != null) {
					var3.lastSaveTime = this.worldObj.getTotalWorldTime();

					if (this.serverChunkGenerator != null) {
						this.serverChunkGenerator.recreateStructures(par1, par2);
					}
				}

				return var3;
			} catch (Exception var4) {
				var4.printStackTrace();
				return null;
			}
		}
	}

	private void saveChunkExtraData(Chunk par1Chunk) {
		if (this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.worldObj, par1Chunk);
			} catch (Exception var3) {
				var3.printStackTrace();
			}
		}
	}

	private void saveChunkData(Chunk par1Chunk) {
		if (this.chunkLoader != null) {
			++_w;
			try {
				par1Chunk.lastSaveTime = this.worldObj.getTotalWorldTime();
				this.chunkLoader.saveChunk(this.worldObj, par1Chunk);
			} catch (IOException var3) {
				var3.printStackTrace();
			} catch (MinecraftException var4) {
				var4.printStackTrace();
			}
		}
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
		Chunk var4 = this.provideChunk(par2, par3);

		if (!var4.isTerrainPopulated) {
			var4.isTerrainPopulated = true;

			if (this.serverChunkGenerator != null) {
				this.serverChunkGenerator.populate(par1IChunkProvider, par2, par3);
				var4.setChunkModified();
			}
		}
	}

	/**
	 * Two modes of operation: if passed true, save all Chunks in one go. If passed
	 * false, save up to two chunks. Return true if all chunks have been saved.
	 */
	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		int var3 = 0;

		for (int var4 = 0; var4 < this.loadedChunks.size(); ++var4) {
			Chunk var5 = (Chunk) this.loadedChunks.get(var4);

			if (par1) {
				this.saveChunkExtraData(var5);
			}

			if (var5.needsSaving(par1)) {
				this.saveChunkData(var5);
				var5.isModified = false;
				++var3;

				if (var3 == 24 && !par1) {
					return false;
				}
			}
		}

		return true;
	}

	public void func_104112_b() {
		if (this.chunkLoader != null) {
			this.chunkLoader.saveExtraData();
		}
	}
	
	private long fixTheFuckingMemoryLeak = 0l;

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to
	 * unload every such chunk.
	 */
	public boolean unloadQueuedChunks() {
		if (!this.worldObj.levelSaving) {
			
			long millis = System.currentTimeMillis();
			if(millis - fixTheFuckingMemoryLeak > 10000l) {  // FUCK OFF SUCK MY FUCKING COCK
				fixTheFuckingMemoryLeak = millis;
				this.id2ChunkMap.iterate((l,o) -> {
					Chunk id = (Chunk) o;
					PlayerInstance ii = this.worldObj.getPlayerManager().getPlayerInstance(id.xPosition, id.zPosition, false);
					if((ii == null || ii.isEmpty()) && !droppedChunksSet.contains(l)) {
						this.droppedChunksSet.add(l);
						//System.out.println("Leaked: " + id);
					}
				});
			}
			
			for (int var1 = 0; var1 < 100; ++var1) {
				if (!this.droppedChunksSet.isEmpty()) {
					Long var2 = (Long) this.droppedChunksSet.iterator().next();
					Chunk var3 = (Chunk) this.id2ChunkMap.getValueByKey(var2.longValue());
					var3.onChunkUnload();
					this.saveChunkData(var3);
					this.saveChunkExtraData(var3);
					this.worldObj.getPlayerManager().freePlayerInstance(var2);
					this.droppedChunksSet.remove(var2);
					this.id2ChunkMap.remove(var2.longValue());
					this.loadedChunks.remove(var3);
					//System.out.println("" + this.droppedChunksSet.size() + ", " + this.id2ChunkMap.getNumHashElements());
				}
			}

			if (this.chunkLoader != null) {
				this.chunkLoader.chunkTick();
			}
		}

		return this.serverChunkGenerator.unloadQueuedChunks();
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave() {
		return !this.worldObj.levelSaving;
	}

	/**
	 * Converts the instance data to a readable string.
	 */
	public String makeString() {
		return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the given
	 * location.
	 */
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		return this.serverChunkGenerator.getPossibleCreatures(par1EnumCreatureType, par2, par3, par4);
	}

	/**
	 * Returns the location of the closest structure of the specified type. If not
	 * found returns null.
	 */
	public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5) {
		return this.serverChunkGenerator.findClosestStructure(par1World, par2Str, par3, par4, par5);
	}

	public int getLoadedChunkCount() {
		return this.id2ChunkMap.getNumHashElements();
	}

	public void recreateStructures(int par1, int par2) {
	}
	
	private int _r = 0;
	private int _w = 0;
	private int _g = 0;
	
	public int statR() {
		int r = _r;
		_r = 0;
		return r;
	}
	
	public int statW() {
		int w = _w;
		_w = 0;
		return w;
	}
	
	public int statG() {
		int g = _g;
		_g = 0;
		return g;
	}
}
