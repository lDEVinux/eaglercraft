package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldServerMulti extends WorldServer {
	public WorldServerMulti(MinecraftServer par1MinecraftServer, ISaveHandler par2ISaveHandler, String par3Str,
			int par4, WorldSettings par5WorldSettings, WorldServer par6WorldServer, Profiler par7Profiler,
			ILogAgent par8ILogAgent) {
		super(par1MinecraftServer, par2ISaveHandler, par3Str, par4, par5WorldSettings, par7Profiler, par8ILogAgent);
		this.mapStorage = par6WorldServer.mapStorage;
		this.worldScoreboard = par6WorldServer.getScoreboard();
		this.worldInfo = new DerivedWorldInfo(par6WorldServer.getWorldInfo());
	}

	/**
	 * Saves the chunks to disk.
	 */
	protected void saveLevel() throws MinecraftException {
	}
}
