package net.minecraft.src;

public class ItemSnow extends ItemBlockWithMetadata {
	public ItemSnow(int par1, Block par2Block) {
		super(par1, par2Block);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if (par1ItemStack.stackSize == 0) {
			return false;
		} else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
			return false;
		} else {
			int var11 = par3World.getBlockId(par4, par5, par6);

			if (var11 == Block.snow.blockID) {
				Block var12 = Block.blocksList[this.getBlockID()];
				int var13 = par3World.getBlockMetadata(par4, par5, par6);
				int var14 = var13 & 7;

				if (var14 <= 6 && par3World.checkNoEntityCollision(var12.getCollisionBoundingBoxFromPool(par3World, par4, par5, par6)) && par3World.setBlockMetadataWithNotify(par4, par5, par6, var14 + 1 | var13 & -8, 2)) {
					par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F,
							var12.stepSound.getPitch() * 0.8F);
					--par1ItemStack.stackSize;
					return true;
				}
			}

			return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
		}
	}
}
