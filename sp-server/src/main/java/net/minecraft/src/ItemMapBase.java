package net.minecraft.src;

public class ItemMapBase extends Item {
	protected ItemMapBase(int par1) {
		super(par1);
	}

	/**
	 * false for all Items except sub-classes of ItemMapBase
	 */
	public boolean isMap() {
		return true;
	}

	public Packet getUpdatePacket(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return null;
	}
}
