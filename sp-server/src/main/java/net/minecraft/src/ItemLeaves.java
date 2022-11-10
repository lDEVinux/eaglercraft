package net.minecraft.src;

public class ItemLeaves extends ItemBlock {
	public ItemLeaves(int par1) {
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1 | 4;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack
	 * so different stacks can have different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int var2 = par1ItemStack.getItemDamage();

		if (var2 < 0 || var2 >= BlockLeaves.LEAF_TYPES.length) {
			var2 = 0;
		}

		return super.getUnlocalizedName() + "." + BlockLeaves.LEAF_TYPES[var2];
	}
}
