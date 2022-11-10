package net.minecraft.src;

public interface Icon {
	/**
	 * Returns the X position of this icon on its texture sheet, in pixels.
	 */
	int getOriginX();

	/**
	 * Returns the Y position of this icon on its texture sheet, in pixels.
	 */
	int getOriginY();

	/**
	 * Returns the minimum U coordinate to use when rendering with this icon.
	 */
	float getMinU();

	/**
	 * Returns the maximum U coordinate to use when rendering with this icon.
	 */
	float getMaxU();

	/**
	 * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other
	 * arguments return in-between values.
	 */
	float getInterpolatedU(double var1);

	/**
	 * Returns the minimum V coordinate to use when rendering with this icon.
	 */
	float getMinV();

	/**
	 * Returns the maximum V coordinate to use when rendering with this icon.
	 */
	float getMaxV();

	/**
	 * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other
	 * arguments return in-between values.
	 */
	float getInterpolatedV(double var1);

	String getIconName();

	/**
	 * Returns the width of the texture sheet this icon is on, in pixels.
	 */
	int getSheetWidth();

	/**
	 * Returns the height of the texture sheet this icon is on, in pixels.
	 */
	int getSheetHeight();
}
