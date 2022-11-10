package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
	private final WorldServer theWorldServer;

	/** players in the current instance */
	private final List players = new ArrayList();

	/** the hash of all playerInstances created */
	private final LongHashMap playerInstances = new LongHashMap();

	/** the playerInstances(chunks) that need to be updated */
	private final List playerInstancesToUpdate = new ArrayList();

	/** x, z direction vectors: east, south, west, north */
	private final int[][] xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

	public PlayerManager(WorldServer par1WorldServer, int par2) {
		if (par2 > 15) {
			throw new IllegalArgumentException("Too big view radius!");
		} else if (par2 < 3) {
			throw new IllegalArgumentException("Too small view radius!");
		} else {
			//this.playerViewRadius = par2;
			this.theWorldServer = par1WorldServer;
		}
	}

	/**
	 * Returns the MinecraftServer associated with the PlayerManager.
	 */
	public WorldServer getMinecraftServer() {
		return this.theWorldServer;
	}

	/**
	 * updates all the player instances that need to be updated
	 */
	public void updatePlayerInstances() {
		for (int var1 = 0; var1 < this.playerInstancesToUpdate.size(); ++var1) {
			((PlayerInstance) this.playerInstancesToUpdate.get(var1)).onUpdate();
		}

		this.playerInstancesToUpdate.clear();

		if (this.players.isEmpty()) {
			WorldProvider var2 = this.theWorldServer.provider;

			if (!var2.canRespawnHere()) {
				this.theWorldServer.theChunkProviderServer.unloadAllChunks();
			}
		}
	}

	/**
	 * passi n the chunk x and y and a flag as to whether or not the instance should
	 * be made if it doesnt exist
	 */
	public PlayerInstance getPlayerInstance(int par1, int par2, boolean par3) {
		long var4 = ((long) par1 + 2147483647L) | (((long) par2 + 2147483647L) << 32);
		PlayerInstance var6 = (PlayerInstance) this.playerInstances.getValueByKey(var4);

		if (var6 == null && par3) {
			var6 = new PlayerInstance(this, par1, par2);
			this.playerInstances.add(var4, var6);
		}

		return var6;
	}
	
	public void freePlayerInstance(long l) {
		this.playerInstances.remove(l);
		this.playerInstancesToUpdate.remove(l);
	}

	public void markBlockNeedsUpdate(int par1, int par2, int par3) {
		int var4 = par1 >> 4;
		int var5 = par3 >> 4;
		PlayerInstance var6 = this.getPlayerInstance(var4, var5, false);

		if (var6 != null) {
			var6.markBlockNeedsUpdate(par1 & 15, par2, par3 & 15);
		}
	}
	
	public void cycleRenderDistance(EntityPlayerMP player) {
		if(player.lastRenderDistance != player.renderDistance) {
			//player.lastRenderDistance = player.renderDistance;
			player.mcServer.getConfigurationManager().updateOnRenderDistanceChange(player);
		}
		if(player.mcServer.getServerOwner().equals(player.username)) {
			cycleAllRenderDistance(player);
		}
	}
	
	public void cycleAllRenderDistance(EntityPlayerMP player) {
		player.mcServer.getConfigurationManager().viewDistance = player.renderDistance;
		player.lastRenderDistance = player.renderDistance;
		List curList = new ArrayList();
		curList.addAll(player.mcServer.getConfigurationManager().playerEntityList);
		curList.remove(player);
		int limited = player.renderDistance > 10 ? 10 : player.renderDistance;
		for(int i = 0, l = curList.size(); i < l; ++i) {
			EntityPlayerMP playerReload = (EntityPlayerMP)curList.get(i);
			if(!player.mcServer.getServerOwner().equals(playerReload.username)) {
				int targetRenderDist = player.renderDistance > limited ? limited : player.renderDistance;
				if (playerReload.renderDistance != targetRenderDist) {
					//playerReload.lastRenderDistance = playerReload.renderDistance = targetRenderDist;
					playerReload.mcServer.getConfigurationManager().updateOnRenderDistanceChange(playerReload);
				}
			}
		}
	}

	/**
	 * Adds an EntityPlayerMP to the PlayerManager.
	 */
	public void addPlayer(EntityPlayerMP par1EntityPlayerMP) {
		int var2 = (int) par1EntityPlayerMP.posX >> 4;
		int var3 = (int) par1EntityPlayerMP.posZ >> 4;
		par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
		par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;
		
		int rd = par1EntityPlayerMP.lastRenderDistance = par1EntityPlayerMP.renderDistance;
		
		for (int var4 = var2 - rd; var4 <= var2 + rd; ++var4) {
			for (int var5 = var3 - rd; var5 <= var3 + rd; ++var5) {
				PlayerInstance pi = this.getPlayerInstance(var4, var5, true);
				if (!pi.hasPlayer(par1EntityPlayerMP)) {
					pi.addPlayer(par1EntityPlayerMP);
				}
				if(!playerInstancesToUpdate.contains(pi)) {
					playerInstancesToUpdate.add(pi);
				}
			}
		}

		this.players.add(par1EntityPlayerMP);
		this.filterChunkLoadQueue(par1EntityPlayerMP);
	}

	/**
	 * Removes all chunks from the given player's chunk load queue that are not in
	 * viewing range of the player.
	 */
	public void filterChunkLoadQueue(EntityPlayerMP par1EntityPlayerMP) {
		if(par1EntityPlayerMP.lastRenderDistance != par1EntityPlayerMP.renderDistance) {
			cycleRenderDistance(par1EntityPlayerMP);
		}
		ArrayList var2 = new ArrayList(par1EntityPlayerMP.loadedChunks);
		int var3 = 0;
		int var4 = par1EntityPlayerMP.renderDistance;
		int var5 = (int) par1EntityPlayerMP.posX >> 4;
		int var6 = (int) par1EntityPlayerMP.posZ >> 4;
		int var7 = 0;
		int var8 = 0;
		ChunkCoordIntPair var9 = PlayerInstance.getChunkLocation(this.getPlayerInstance(var5, var6, true));
		par1EntityPlayerMP.loadedChunks.clear();

		if (var2.contains(var9)) {
			par1EntityPlayerMP.loadedChunks.add(var9);
		}

		int var10;

		for (var10 = 1; var10 <= var4 * 2; ++var10) {
			for (int var11 = 0; var11 < 2; ++var11) {
				int[] var12 = this.xzDirectionsConst[var3++ % 4];

				for (int var13 = 0; var13 < var10; ++var13) {
					var7 += var12[0];
					var8 += var12[1];
					var9 = PlayerInstance.getChunkLocation(this.getPlayerInstance(var5 + var7, var6 + var8, true));

					if (var2.contains(var9)) {
						par1EntityPlayerMP.loadedChunks.add(var9);
					}
				}
			}
		}

		var3 %= 4;

		for (var10 = 0; var10 < var4 * 2; ++var10) {
			var7 += this.xzDirectionsConst[var3][0];
			var8 += this.xzDirectionsConst[var3][1];
			var9 = PlayerInstance.getChunkLocation(this.getPlayerInstance(var5 + var7, var6 + var8, true));

			if (var2.contains(var9)) {
				par1EntityPlayerMP.loadedChunks.add(var9);
			}
		}
	}

	/**
	 * Removes an EntityPlayerMP from the PlayerManager.
	 */
	public void removePlayer(EntityPlayerMP par1EntityPlayerMP) {
		int var2 = (int) par1EntityPlayerMP.managedPosX >> 4;
		int var3 = (int) par1EntityPlayerMP.managedPosZ >> 4;
		
		int rd = par1EntityPlayerMP.lastRenderDistance;
		for (int var4 = var2 - rd; var4 <= var2 + rd; ++var4) {
			for (int var5 = var3 - rd; var5 <= var3 + rd; ++var5) {
				PlayerInstance var6 = this.getPlayerInstance(var4, var5, false);

				if (var6 != null) {
					var6.removePlayer(par1EntityPlayerMP);
					// long var7 = ((long) var4 + 2147483647L) | (((long) var5 + 2147483647L) << 32);
					// this.freePlayerInstance(var7);
				}
			}
		}
		par1EntityPlayerMP.lastRenderDistance = par1EntityPlayerMP.renderDistance;

		this.players.remove(par1EntityPlayerMP);
	}

	private boolean func_72684_a(int par1, int par2, int par3, int par4, int par5) {
		int var6 = par1 - par3;
		int var7 = par2 - par4;
		return var6 >= -par5 && var6 <= par5 ? var7 >= -par5 && var7 <= par5 : false;
	}

	/**
	 * update chunks around a player being moved by server logic (e.g. cart, boat)
	 */
	public void updateMountedMovingPlayer(EntityPlayerMP par1EntityPlayerMP) {
		if(par1EntityPlayerMP.renderDistance != par1EntityPlayerMP.lastRenderDistance) {
			cycleRenderDistance(par1EntityPlayerMP);
		}
		int var2 = (int) par1EntityPlayerMP.posX >> 4;
		int var3 = (int) par1EntityPlayerMP.posZ >> 4;
		double var4 = par1EntityPlayerMP.managedPosX - par1EntityPlayerMP.posX;
		double var6 = par1EntityPlayerMP.managedPosZ - par1EntityPlayerMP.posZ;
		double var8 = var4 * var4 + var6 * var6;

		if (var8 >= 64.0D) {
			int var10 = (int) par1EntityPlayerMP.managedPosX >> 4;
			int var11 = (int) par1EntityPlayerMP.managedPosZ >> 4;
			int var12 = par1EntityPlayerMP.renderDistance;
			int var13 = var2 - var10;
			int var14 = var3 - var11;

			if (var13 != 0 || var14 != 0) {
				for (int var15 = var2 - var12; var15 <= var2 + var12; ++var15) {
					for (int var16 = var3 - var12; var16 <= var3 + var12; ++var16) {
						if (!this.func_72684_a(var15, var16, var10, var11, var12)) {
							this.getPlayerInstance(var15, var16, true).addPlayer(par1EntityPlayerMP);
						}

						if (!this.func_72684_a(var15 - var13, var16 - var14, var2, var3, var12)) {
							PlayerInstance var17 = this.getPlayerInstance(var15 - var13, var16 - var14, false);

							if (var17 != null) {
								var17.removePlayer(par1EntityPlayerMP);
							}
						}
					}
				}

				this.filterChunkLoadQueue(par1EntityPlayerMP);
				par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
				par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;
			}
		}
	}

	public boolean isPlayerWatchingChunk(EntityPlayerMP par1EntityPlayerMP, int par2, int par3) {
		PlayerInstance var4 = this.getPlayerInstance(par2, par3, false);
		return var4 == null ? false
				: PlayerInstance.getPlayersInChunk(var4).contains(par1EntityPlayerMP)
						&& !par1EntityPlayerMP.loadedChunks.contains(PlayerInstance.getChunkLocation(var4));
	}

	/**
	 * Get the furthest viewable block given player's view distance
	 */
	public static int getFurthestViewableBlock(int par0) {
		return par0 * 16 - 16;
	}

	static WorldServer getWorldServer(PlayerManager par0PlayerManager) {
		return par0PlayerManager.theWorldServer;
	}

	static LongHashMap getChunkWatchers(PlayerManager par0PlayerManager) {
		return par0PlayerManager.playerInstances;
	}

	static List getChunkWatchersWithPlayers(PlayerManager par0PlayerManager) {
		return par0PlayerManager.playerInstancesToUpdate;
	}
}
