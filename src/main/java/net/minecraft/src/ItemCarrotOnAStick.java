package net.minecraft.src;

public class ItemCarrotOnAStick extends Item {
	public ItemCarrotOnAStick(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabTransport);
		this.setMaxStackSize(1);
		this.setMaxDamage(25);
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Returns true if this item should be rotated by 180 degrees around the Y axis
	 * when being held in an entities hands.
	 */
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}
}
