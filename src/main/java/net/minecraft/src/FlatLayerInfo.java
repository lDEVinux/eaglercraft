package net.minecraft.src;

public class FlatLayerInfo {
	/** Amount of layers for this set of layers. */
	private int layerCount;

	/** Block type used on this set of layers. */
	private int layerFillBlock;

	/** Block metadata used on this set of laeyrs. */
	private int layerFillBlockMeta;
	private int layerMinimumY;

	public FlatLayerInfo(int par1, int par2) {
		this.layerCount = 1;
		this.layerFillBlock = 0;
		this.layerFillBlockMeta = 0;
		this.layerMinimumY = 0;
		this.layerCount = par1;
		this.layerFillBlock = par2;
	}

	public FlatLayerInfo(int par1, int par2, int par3) {
		this(par1, par2);
		this.layerFillBlockMeta = par3;
	}

	/**
	 * Return the amount of layers for this set of layers.
	 */
	public int getLayerCount() {
		return this.layerCount;
	}

	/**
	 * Return the block type used on this set of layers.
	 */
	public int getFillBlock() {
		return this.layerFillBlock;
	}

	/**
	 * Return the block metadata used on this set of layers.
	 */
	public int getFillBlockMeta() {
		return this.layerFillBlockMeta;
	}

	/**
	 * Return the minimum Y coordinate for this layer, set during generation.
	 */
	public int getMinY() {
		return this.layerMinimumY;
	}

	/**
	 * Set the minimum Y coordinate for this layer.
	 */
	public void setMinY(int par1) {
		this.layerMinimumY = par1;
	}

	public String toString() {
		String var1 = Integer.toString(this.layerFillBlock);

		if (this.layerCount > 1) {
			var1 = this.layerCount + "x" + var1;
		}

		if (this.layerFillBlockMeta > 0) {
			var1 = var1 + ":" + this.layerFillBlockMeta;
		}

		return var1;
	}
}
