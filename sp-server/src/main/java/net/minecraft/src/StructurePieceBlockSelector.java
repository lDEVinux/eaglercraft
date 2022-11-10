package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public abstract class StructurePieceBlockSelector {
	protected int selectedBlockId;
	protected int selectedBlockMetaData;

	/**
	 * picks Block Ids and Metadata (Silverfish)
	 */
	public abstract void selectBlocks(EaglercraftRandom var1, int var2, int var3, int var4, boolean var5);

	public int getSelectedBlockId() {
		return this.selectedBlockId;
	}

	public int getSelectedBlockMetaData() {
		return this.selectedBlockMetaData;
	}
}
