package net.lax1dude.eaglercraft.sp;

import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;

public class VFSSaveFormat implements ISaveFormat {
	
	private VFSSaveHandler folder;
	
	public VFSSaveFormat(VFSSaveHandler dir) {
		folder = dir;
	}

	@Override
	public ISaveHandler getSaveLoader(String var1, boolean var2) {
		return folder;
	}

	@Override
	public void flushCache() {
	}

	@Override
	public boolean deleteWorldDirectory(String var1) {
		return true;
	}

	@Override
	public boolean isOldMapFormat(String var1) {
		return false;
	}

	@Override
	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		return false;
	}

}
