package net.minecraft.src;

public class TileEntityDropper extends TileEntityDispenser {
	/**
	 * Returns the name of the inventory.
	 */
	public String getInvName() {
		return this.isInvNameLocalized() ? this.customName : "container.dropper";
	}
}
