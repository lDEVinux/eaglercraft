package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockComparator extends BlockRedstoneLogic implements ITileEntityProvider {
	public BlockComparator(int par1, boolean par2) {
		super(par1, par2);
		this.isBlockContainer = true;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.comparator.itemID;
	}

	protected int func_94481_j_(int par1) {
		return 2;
	}

	protected BlockRedstoneLogic func_94485_e() {
		return Block.redstoneComparatorActive;
	}

	protected BlockRedstoneLogic func_94484_i() {
		return Block.redstoneComparatorIdle;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 37;
	}

	protected boolean func_96470_c(int par1) {
		return this.isRepeaterPowered || (par1 & 8) != 0;
	}

	protected int func_94480_d(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return this.getTileEntityComparator(par1IBlockAccess, par2, par3, par4).func_96100_a();
	}

	private int func_94491_m(World par1World, int par2, int par3, int par4, int par5) {
		return !this.func_94490_c(par5) ? this.getInputStrength(par1World, par2, par3, par4, par5)
				: Math.max(this.getInputStrength(par1World, par2, par3, par4, par5)
						- this.func_94482_f(par1World, par2, par3, par4, par5), 0);
	}

	public boolean func_94490_c(int par1) {
		return (par1 & 4) == 4;
	}

	protected boolean func_94478_d(World par1World, int par2, int par3, int par4, int par5) {
		int var6 = this.getInputStrength(par1World, par2, par3, par4, par5);

		if (var6 >= 15) {
			return true;
		} else if (var6 == 0) {
			return false;
		} else {
			int var7 = this.func_94482_f(par1World, par2, par3, par4, par5);
			return var7 == 0 ? true : var6 >= var7;
		}
	}

	/**
	 * Returns the signal strength at one input of the block. Args: world, X, Y, Z,
	 * side
	 */
	protected int getInputStrength(World par1World, int par2, int par3, int par4, int par5) {
		int var6 = super.getInputStrength(par1World, par2, par3, par4, par5);
		int var7 = getDirection(par5);
		int var8 = par2 + Direction.offsetX[var7];
		int var9 = par4 + Direction.offsetZ[var7];
		int var10 = par1World.getBlockId(var8, par3, var9);

		if (var10 > 0) {
			if (Block.blocksList[var10].hasComparatorInputOverride()) {
				var6 = Block.blocksList[var10].getComparatorInputOverride(par1World, var8, par3, var9,
						Direction.footInvisibleFaceRemap[var7]);
			} else if (var6 < 15 && Block.isNormalCube(var10)) {
				var8 += Direction.offsetX[var7];
				var9 += Direction.offsetZ[var7];
				var10 = par1World.getBlockId(var8, par3, var9);

				if (var10 > 0 && Block.blocksList[var10].hasComparatorInputOverride()) {
					var6 = Block.blocksList[var10].getComparatorInputOverride(par1World, var8, par3, var9,
							Direction.footInvisibleFaceRemap[var7]);
				}
			}
		}

		return var6;
	}

	/**
	 * Returns the blockTileEntity at given coordinates.
	 */
	public TileEntityComparator getTileEntityComparator(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return (TileEntityComparator) par1IBlockAccess.getBlockTileEntity(par2, par3, par4);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		int var10 = par1World.getBlockMetadata(par2, par3, par4);
		boolean var11 = this.isRepeaterPowered | (var10 & 8) != 0;
		boolean var12 = !this.func_94490_c(var10);
		int var13 = var12 ? 4 : 0;
		var13 |= var11 ? 8 : 0;
		par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.5D, (double) par4 + 0.5D, "random.click",
				0.3F, var12 ? 0.55F : 0.5F);
		par1World.setBlockMetadata(par2, par3, par4, var13 | var10 & 3, 2);
		this.func_96476_c(par1World, par2, par3, par4, par1World.rand);
		return true;
	}

	protected void func_94479_f(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isBlockTickScheduled(par2, par3, par4, this.blockID)) {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);
			int var7 = this.func_94491_m(par1World, par2, par3, par4, var6);
			int var8 = this.getTileEntityComparator(par1World, par2, par3, par4).func_96100_a();

			if (var7 != var8 || this.func_96470_c(var6) != this.func_94478_d(par1World, par2, par3, par4, var6)) {
				if (this.func_83011_d(par1World, par2, par3, par4, var6)) {
					par1World.func_82740_a(par2, par3, par4, this.blockID, this.func_94481_j_(0), -1);
				} else {
					par1World.func_82740_a(par2, par3, par4, this.blockID, this.func_94481_j_(0), 0);
				}
			}
		}
	}

	private void func_96476_c(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		int var6 = par1World.getBlockMetadata(par2, par3, par4);
		int var7 = this.func_94491_m(par1World, par2, par3, par4, var6);
		int var8 = this.getTileEntityComparator(par1World, par2, par3, par4).func_96100_a();
		this.getTileEntityComparator(par1World, par2, par3, par4).func_96099_a(var7);

		if (var8 != var7 || !this.func_94490_c(var6)) {
			boolean var9 = this.func_94478_d(par1World, par2, par3, par4, var6);
			boolean var10 = this.isRepeaterPowered || (var6 & 8) != 0;

			if (var10 && !var9) {
				par1World.setBlockMetadata(par2, par3, par4, var6 & -9, 2);
			} else if (!var10 && var9) {
				par1World.setBlockMetadata(par2, par3, par4, var6 | 8, 2);
			}

			this.func_94483_i_(par1World, par2, par3, par4);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (this.isRepeaterPowered) {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);
			par1World.setBlock(par2, par3, par4, this.func_94484_i().blockID, var6 | 8, 4);
		}

		this.func_96476_c(par1World, par2, par3, par4, par5Random);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		par1World.setBlockTileEntity(par2, par3, par4, this.createNewTileEntity(par1World));
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
		par1World.removeBlockTileEntity(par2, par3, par4);
		this.func_94483_i_(par1World, par2, par3, par4);
	}

	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By
	 * default, passes it on to the tile entity at this location. Args: world, x, y,
	 * z, blockID, EventID, event parameter
	 */
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);
		return var7 != null ? var7.receiveClientEvent(par5, par6) : false;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityComparator();
	}
}
