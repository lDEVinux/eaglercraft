package net.minecraft.src;

public class BlockEventData {
	private int coordX;
	private int coordY;
	private int coordZ;
	private int blockID;

	/** Different for each blockID */
	private int eventID;

	/** Different for each blockID, eventID */
	private int eventParameter;

	public BlockEventData(int par1, int par2, int par3, int par4, int par5, int par6) {
		this.coordX = par1;
		this.coordY = par2;
		this.coordZ = par3;
		this.eventID = par5;
		this.eventParameter = par6;
		this.blockID = par4;
	}

	/**
	 * Get the X coordinate.
	 */
	public int getX() {
		return this.coordX;
	}

	/**
	 * Get the Y coordinate.
	 */
	public int getY() {
		return this.coordY;
	}

	/**
	 * Get the Z coordinate.
	 */
	public int getZ() {
		return this.coordZ;
	}

	/**
	 * Get the Event ID (different for each BlockID)
	 */
	public int getEventID() {
		return this.eventID;
	}

	/**
	 * Get the Event Parameter (different for each BlockID,EventID)
	 */
	public int getEventParameter() {
		return this.eventParameter;
	}

	/**
	 * Gets the BlockID for this BlockEventData
	 */
	public int getBlockID() {
		return this.blockID;
	}

	public boolean equals(Object par1Obj) {
		if (!(par1Obj instanceof BlockEventData)) {
			return false;
		} else {
			BlockEventData var2 = (BlockEventData) par1Obj;
			return this.coordX == var2.coordX && this.coordY == var2.coordY && this.coordZ == var2.coordZ
					&& this.eventID == var2.eventID && this.eventParameter == var2.eventParameter
					&& this.blockID == var2.blockID;
		}
	}

	public String toString() {
		return "TE(" + this.coordX + "," + this.coordY + "," + this.coordZ + ")," + this.eventID + ","
				+ this.eventParameter + "," + this.blockID;
	}
}
