package net.minecraft.src;

public class BlockSand extends Block {
	/** Do blocks fall instantly to where they stop or do they fall over time */
	public static boolean fallInstantly = false;

	public BlockSand(int par1) {
		super(par1, Material.sand);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public BlockSand(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
	}

	/**
	 * If there is space to fall below will start this block falling
	 */
	private void tryToFall(World par1World, int par2, int par3, int par4) {
		if (canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0) {
			byte var8 = 32;

			if (!(!fallInstantly && par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8))) {
				par1World.setBlockToAir(par2, par3, par4);

				while (canFallBelow(par1World, par2, par3 - 1, par4) && par3 > 0) {
					--par3;
				}

				if (par3 > 0) {
					par1World.setBlock(par2, par3, par4, this.blockID);
				}
			}
		}
	}

	/**
	 * Called when the falling block entity for this block is created
	 */
	protected void onStartFalling(EntityFallingSand par1EntityFallingSand) {
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World par1World) {
		return 2;
	}

	/**
	 * Checks to see if the sand can fall into the block below it
	 */
	public static boolean canFallBelow(World par0World, int par1, int par2, int par3) {
		int var4 = par0World.getBlockId(par1, par2, par3);

		if (var4 == 0) {
			return true;
		} else if (var4 == Block.fire.blockID) {
			return true;
		} else {
			Material var5 = Block.blocksList[var4].blockMaterial;
			return var5 == Material.water ? true : var5 == Material.lava;
		}
	}

	/**
	 * Called when the falling block entity for this block hits the ground and turns
	 * back into a block
	 */
	public void onFinishFalling(World par1World, int par2, int par3, int par4, int par5) {
	}
}
