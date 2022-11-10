package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockCommandBlock extends BlockContainer {
	public BlockCommandBlock(int par1) {
		super(par1, Material.iron);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityCommandBlock();
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		TileEntity var6 = par1World.getBlockTileEntity(par2, par3, par4);

		if (var6 != null && var6 instanceof TileEntityCommandBlock) {
			TileEntityCommandBlock var7 = (TileEntityCommandBlock) var6;
			var7.func_96102_a(var7.executeCommandOnPowered(par1World));
			par1World.func_96440_m(par2, par3, par4, this.blockID);
		}
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World par1World) {
		return 1;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntityCommandBlock var10 = (TileEntityCommandBlock) par1World.getBlockTileEntity(par2, par3, par4);

		if (var10 != null) {
			par5EntityPlayer.displayGUIEditSign(var10);
		}

		return true;
	}

	/**
	 * If this returns true, then comparators facing away from this block will use
	 * the value from getComparatorInputOverride instead of the actual redstone
	 * signal strength.
	 */
	public boolean hasComparatorInputOverride() {
		return true;
	}

	/**
	 * If hasComparatorInputOverride returns true, the return value from this is
	 * used instead of the redstone signal strength when this block inputs to a
	 * comparator.
	 */
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		TileEntity var6 = par1World.getBlockTileEntity(par2, par3, par4);
		return var6 != null && var6 instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock) var6).func_96103_d() : 0;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		TileEntityCommandBlock var7 = (TileEntityCommandBlock) par1World.getBlockTileEntity(par2, par3, par4);

		if (par6ItemStack.hasDisplayName()) {
			var7.setCommandSenderName(par6ItemStack.getDisplayName());
		}
	}
}
