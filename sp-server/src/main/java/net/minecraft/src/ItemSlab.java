package net.minecraft.src;

public class ItemSlab extends ItemBlock {
	private final boolean isFullBlock;

	/** Instance of BlockHalfSlab. */
	private final BlockHalfSlab theHalfSlab;

	/** The double-slab block corresponding to this item. */
	private final BlockHalfSlab doubleSlab;

	public ItemSlab(int par1, BlockHalfSlab par2BlockHalfSlab, BlockHalfSlab par3BlockHalfSlab, boolean par4) {
		super(par1);
		this.theHalfSlab = par2BlockHalfSlab;
		this.doubleSlab = par3BlockHalfSlab;
		this.isFullBlock = par4;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack
	 * so different stacks can have different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.theHalfSlab.getFullSlabName(par1ItemStack.getItemDamage());
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
			int par5, int par6, int par7, float par8, float par9, float par10) {
		if (this.isFullBlock) {
			return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9,
					par10);
		} else if (par1ItemStack.stackSize == 0) {
			return false;
		} else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
			return false;
		} else {
			int var11 = par3World.getBlockId(par4, par5, par6);
			int var12 = par3World.getBlockMetadata(par4, par5, par6);
			int var13 = var12 & 7;
			boolean var14 = (var12 & 8) != 0;

			if ((par7 == 1 && !var14 || par7 == 0 && var14) && var11 == this.theHalfSlab.blockID
					&& var13 == par1ItemStack.getItemDamage()) {
				if (par3World.checkNoEntityCollision(
						this.doubleSlab.getCollisionBoundingBoxFromPool(par3World, par4, par5, par6))
						&& par3World.setBlock(par4, par5, par6, this.doubleSlab.blockID, var13, 3)) {
					par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F),
							(double) ((float) par6 + 0.5F), this.doubleSlab.stepSound.getPlaceSound(),
							(this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F,
							this.doubleSlab.stepSound.getPitch() * 0.8F);
					--par1ItemStack.stackSize;
				}

				return true;
			} else {
				return this.func_77888_a(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7) ? true
						: super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8,
								par9, par10);
			}
		}
	}

	private boolean func_77888_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
			int par5, int par6, int par7) {
		if (par7 == 0) {
			--par5;
		}

		if (par7 == 1) {
			++par5;
		}

		if (par7 == 2) {
			--par6;
		}

		if (par7 == 3) {
			++par6;
		}

		if (par7 == 4) {
			--par4;
		}

		if (par7 == 5) {
			++par4;
		}

		int var8 = par3World.getBlockId(par4, par5, par6);
		int var9 = par3World.getBlockMetadata(par4, par5, par6);
		int var10 = var9 & 7;

		if (var8 == this.theHalfSlab.blockID && var10 == par1ItemStack.getItemDamage()) {
			if (par3World.checkNoEntityCollision(
					this.doubleSlab.getCollisionBoundingBoxFromPool(par3World, par4, par5, par6))
					&& par3World.setBlock(par4, par5, par6, this.doubleSlab.blockID, var10, 3)) {
				par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F),
						(double) ((float) par6 + 0.5F), this.doubleSlab.stepSound.getPlaceSound(),
						(this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F,
						this.doubleSlab.stepSound.getPitch() * 0.8F);
				--par1ItemStack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}
}
