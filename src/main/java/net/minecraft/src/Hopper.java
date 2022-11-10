package net.minecraft.src;

public interface Hopper extends IInventory {
	/**
	 * Returns the worldObj for this tileEntity.
	 */
	World getWorldObj();

	/**
	 * Gets the world X position for this hopper entity.
	 */
	double getXPos();

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	double getYPos();

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	double getZPos();
}
