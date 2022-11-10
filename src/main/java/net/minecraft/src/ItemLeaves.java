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
	 * Gets an icon index based on an item's damage value
	 */
	public Icon getIconFromDamage(int par1) {
		return Block.leaves.getIcon(0, par1);
	}

	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		int var3 = par1ItemStack.getItemDamage();
		return (var3 & 1) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((var3 & 2) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic());
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
