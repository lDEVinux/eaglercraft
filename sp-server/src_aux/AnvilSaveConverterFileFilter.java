package net.minecraft.src;

import java.io.File;
import java.io.FilenameFilter;

class AnvilSaveConverterFileFilter implements FilenameFilter {
	final AnvilSaveConverter parent;

	AnvilSaveConverterFileFilter(AnvilSaveConverter par1AnvilSaveConverter) {
		this.parent = par1AnvilSaveConverter;
	}

	public boolean accept(File par1File, String par2Str) {
		return par2Str.endsWith(".mcr");
	}
}
