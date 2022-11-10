package net.minecraft.src;

class GuiFlatPresetsItem {
	/** ID for the item used as icon for this preset. */
	public int iconId;

	/** Name for this preset. */
	public String presetName;

	/** Data for this preset. */
	public String presetData;

	public GuiFlatPresetsItem(int par1, String par2Str, String par3Str) {
		this.iconId = par1;
		this.presetName = par2Str;
		this.presetData = par3Str;
	}
}
