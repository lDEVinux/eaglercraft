package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ConvertingProgressUpdate implements IProgressUpdate {
	private long field_96245_b;

	/** Reference to the MinecraftServer object. */
	final MinecraftServer mcServer;

	public ConvertingProgressUpdate(MinecraftServer par1) {
		this.mcServer = par1;
		this.field_96245_b = System.currentTimeMillis();
	}

	/**
	 * Shows the 'Saving level' string.
	 */
	public void displaySavingString(String par1Str) {
	}

	/**
	 * Updates the progress bar on the loading screen to the specified amount. Args:
	 * loadProgress
	 */
	public void setLoadingProgress(int par1) {
		if (System.currentTimeMillis() - this.field_96245_b >= 1000L) {
			this.field_96245_b = System.currentTimeMillis();
			this.mcServer.getLogAgent().func_98233_a("Converting... " + par1 + "%");
		}
	}

	/**
	 * Displays a string on the loading screen supposed to indicate what is being
	 * done currently.
	 */
	public void displayLoadingString(String par1Str) {
	}
}
