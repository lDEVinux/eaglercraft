package net.minecraft.src;

import java.io.File;

public class AnvilSaveHandler extends SaveHandler {
	public AnvilSaveHandler(File par1File, String par2Str, boolean par3) {
		super(par1File, par2Str, par3);
	}

	/**
	 * initializes and returns the chunk loader for the specified world provider
	 */
	public IChunkLoader getChunkLoader(WorldProvider par1WorldProvider) {
		File var2 = this.getWorldDirectory();
		File var3;

		if (par1WorldProvider instanceof WorldProviderHell) {
			var3 = new File(var2, "DIM-1");
			var3.mkdirs();
			return new AnvilChunkLoader(var3);
		} else if (par1WorldProvider instanceof WorldProviderEnd) {
			var3 = new File(var2, "DIM1");
			var3.mkdirs();
			return new AnvilChunkLoader(var3);
		} else {
			return new AnvilChunkLoader(var2);
		}
	}

	/**
	 * Saves the given World Info with the given NBTTagCompound as the Player.
	 */
	public void saveWorldInfoWithPlayer(WorldInfo par1WorldInfo, NBTTagCompound par2NBTTagCompound) {
		par1WorldInfo.setSaveVersion(19133);
		super.saveWorldInfoWithPlayer(par1WorldInfo, par2NBTTagCompound);
	}

	/**
	 * Called to flush all changes to disk, waiting for them to complete.
	 */
	public void flush() {
		try {
			ThreadedFileIOBase.threadedIOInstance.waitForFinish();
		} catch (InterruptedException var2) {
			var2.printStackTrace();
		}

		RegionFileCache.clearRegionFileReferences();
	}
}
