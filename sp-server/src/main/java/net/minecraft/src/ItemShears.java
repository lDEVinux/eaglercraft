package net.minecraft.src;

public class ItemShears extends Item {
	public ItemShears(int par1) {
		super(par1);
		this.setMaxStackSize(1);
		this.setMaxDamage(238);
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6,
			EntityLiving par7EntityLiving) {
		if (par3 != Block.leaves.blockID && par3 != Block.web.blockID && par3 != Block.tallGrass.blockID
				&& par3 != Block.vine.blockID && par3 != Block.tripWire.blockID) {
			return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLiving);
		} else {
			par1ItemStack.damageItem(1, par7EntityLiving);
			return true;
		}
	}

	/**
	 * Returns if the item (tool) can harvest results from the block type.
	 */
	public boolean canHarvestBlock(Block par1Block) {
		return par1Block.blockID == Block.web.blockID || par1Block.blockID == Block.redstoneWire.blockID
				|| par1Block.blockID == Block.tripWire.blockID;
	}

	/**
	 * Returns the strength of the stack against a given block. 1.0F base,
	 * (Quality+1)*2 if correct blocktype, 1.5F if sword
	 */
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		return par2Block.blockID != Block.web.blockID && par2Block.blockID != Block.leaves.blockID
				? (par2Block.blockID == Block.cloth.blockID ? 5.0F : super.getStrVsBlock(par1ItemStack, par2Block))
				: 15.0F;
	}
}
