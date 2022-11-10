package net.minecraft.src;

public class ItemColored extends ItemBlock {
	private final Block blockRef;
	private String[] blockNames;

	public ItemColored(int par1, boolean par2) {
		super(par1);
		this.blockRef = Block.blocksList[this.getBlockID()];

		if (par2) {
			this.setMaxDamage(0);
			this.setHasSubtypes(true);
		}
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}

	/**
	 * Sets the array of strings to be used for name lookups from item damage to
	 * metadata
	 */
	public ItemColored setBlockNames(String[] par1ArrayOfStr) {
		this.blockNames = par1ArrayOfStr;
		return this;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack
	 * so different stacks can have different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		if (this.blockNames == null) {
			return super.getUnlocalizedName(par1ItemStack);
		} else {
			int var2 = par1ItemStack.getItemDamage();
			return var2 >= 0 && var2 < this.blockNames.length
					? super.getUnlocalizedName(par1ItemStack) + "." + this.blockNames[var2]
					: super.getUnlocalizedName(par1ItemStack);
		}
	}
}
