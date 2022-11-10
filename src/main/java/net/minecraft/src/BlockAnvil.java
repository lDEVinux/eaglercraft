package net.minecraft.src;

import java.util.List;

public class BlockAnvil extends BlockSand {
	/** List of types/statues the Anvil can be in. */
	public static final String[] statuses = new String[] { "intact", "slightlyDamaged", "veryDamaged" };
	private static final String[] anvilIconNames = new String[] { "anvil_top", "anvil_top_damaged_1", "anvil_top_damaged_2" };
	public int field_82521_b = 0;
	private Icon[] iconArray;

	protected BlockAnvil(int par1) {
		super(par1, Material.anvil);
		this.setLightOpacity(0);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether or
	 * not to render the shared face of two adjacent blocks and also whether the
	 * player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		if (this.field_82521_b == 3 && par1 == 1) {
			int var3 = (par2 >> 2) % this.iconArray.length;
			return this.iconArray[var3];
		} else {
			return this.blockIcon;
		}
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("anvil_base");
		this.iconArray = new Icon[anvilIconNames.length];

		for (int var2 = 0; var2 < this.iconArray.length; ++var2) {
			this.iconArray[var2] = par1IconRegister.registerIcon(anvilIconNames[var2]);
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		int var7 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int var8 = par1World.getBlockMetadata(par2, par3, par4) >> 2;
		++var7;
		var7 %= 4;

		if (var7 == 0) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | var8 << 2, 2);
		}

		if (var7 == 1) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | var8 << 2, 2);
		}

		if (var7 == 2) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | var8 << 2, 2);
		}

		if (var7 == 3) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | var8 << 2, 2);
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		return true;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 35;
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1 >> 2;
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 3;

		if (var5 != 3 && var5 != 1) {
			this.setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
	}

	/**
	 * Called when the falling block entity for this block is created
	 */
	protected void onStartFalling(EntityFallingSand par1EntityFallingSand) {
		par1EntityFallingSand.setIsAnvil(true);
	}

	/**
	 * Called when the falling block entity for this block hits the ground and turns
	 * back into a block
	 */
	public void onFinishFalling(World par1World, int par2, int par3, int par4, int par5) {
		par1World.playAuxSFX(1022, par2, par3, par4, 0);
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if the
	 * adjacent block is at the given coordinates. Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}
}
