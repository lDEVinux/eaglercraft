package net.minecraft.src;

public class ItemEnderEye extends Item {
	public ItemEnderEye(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		int var11 = par3World.getBlockId(par4, par5, par6);
		int var12 = par3World.getBlockMetadata(par4, par5, par6);

		if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && var11 == Block.endPortalFrame.blockID && !BlockEndPortalFrame.isEnderEyeInserted(var12)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

		if (var4 != null && var4.typeOfHit == EnumMovingObjectType.TILE) {
			int var5 = par2World.getBlockId(var4.blockX, var4.blockY, var4.blockZ);

			if (var5 == Block.endPortalFrame.blockID) {
				return par1ItemStack;
			}
		}

		return par1ItemStack;
	}
}
