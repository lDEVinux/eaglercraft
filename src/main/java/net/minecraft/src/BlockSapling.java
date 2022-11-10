package net.minecraft.src;

import java.util.List;

public class BlockSapling extends BlockFlower {
	public static final String[] WOOD_TYPES = new String[] { "oak", "spruce", "birch", "jungle" };
	private static final String[] field_94370_b = new String[] { "sapling", "sapling_spruce", "sapling_birch", "sapling_jungle" };
	private Icon[] saplingIcon;

	protected BlockSapling(int par1) {
		super(par1);
		float var2 = 0.4F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var2 * 2.0F, 0.5F + var2);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		par2 &= 3;
		return this.saplingIcon[par2];
	}

	/**
	 * Determines if the same sapling is present at the given location.
	 */
	public boolean isSameSapling(World par1World, int par2, int par3, int par4, int par5) {
		return par1World.getBlockId(par2, par3, par4) == this.blockID && (par1World.getBlockMetadata(par2, par3, par4) & 3) == par5;
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1 & 3;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.saplingIcon = new Icon[field_94370_b.length];

		for (int var2 = 0; var2 < this.saplingIcon.length; ++var2) {
			this.saplingIcon[var2] = par1IconRegister.registerIcon(field_94370_b[var2]);
		}
	}
}
