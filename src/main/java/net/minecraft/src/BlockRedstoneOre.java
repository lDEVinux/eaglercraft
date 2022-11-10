package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockRedstoneOre extends Block {
	private boolean glowing;

	public BlockRedstoneOre(int par1, boolean par2) {
		super(par1, Material.rock);

		if (par2) {
			this.setTickRandomly(true);
		}

		this.glowing = par2;
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World par1World) {
		return 30;
	}

	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
		this.glow(par1World, par2, par3, par4);
		super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
	}

	/**
	 * Called whenever an entity is walking on top of this block. Args: world, x, y,
	 * z, entity
	 */
	public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		this.glow(par1World, par2, par3, par4);
		super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		this.glow(par1World, par2, par3, par4);
		return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
	}

	/**
	 * The redstone ore glows.
	 */
	private void glow(World par1World, int par2, int par3, int par4) {
		this.sparkle(par1World, par2, par3, par4);

		if (this.blockID == Block.oreRedstone.blockID) {
			par1World.setBlock(par2, par3, par4, Block.oreRedstoneGlowing.blockID);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (this.blockID == Block.oreRedstoneGlowing.blockID) {
			par1World.setBlock(par2, par3, par4, Block.oreRedstone.blockID);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.redstone.itemID;
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i'
	 * (inclusive).
	 */
	public int quantityDroppedWithBonus(int par1, EaglercraftRandom par2Random) {
		return this.quantityDropped(par2Random) + par2Random.nextInt(par1 + 1);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 4 + par1Random.nextInt(2);
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

		if (this.idDropped(par5, par1World.rand, par7) != this.blockID) {
			int var8 = 1 + par1World.rand.nextInt(5);
			this.dropXpOnBlockBreak(par1World, par2, par3, par4, var8);
		}
	}

	/**
	 * A randomly called display update to be able to add particles or other items
	 * for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (this.glowing) {
			this.sparkle(par1World, par2, par3, par4);
		}
	}

	/**
	 * The redstone ore sparkles.
	 */
	private void sparkle(World par1World, int par2, int par3, int par4) {
		EaglercraftRandom var5 = par1World.rand;
		double var6 = 0.0625D;

		for (int var8 = 0; var8 < 6; ++var8) {
			double var9 = (double) ((float) par2 + var5.nextFloat());
			double var11 = (double) ((float) par3 + var5.nextFloat());
			double var13 = (double) ((float) par4 + var5.nextFloat());

			if (var8 == 0 && !par1World.isBlockOpaqueCube(par2, par3 + 1, par4)) {
				var11 = (double) (par3 + 1) + var6;
			}

			if (var8 == 1 && !par1World.isBlockOpaqueCube(par2, par3 - 1, par4)) {
				var11 = (double) (par3 + 0) - var6;
			}

			if (var8 == 2 && !par1World.isBlockOpaqueCube(par2, par3, par4 + 1)) {
				var13 = (double) (par4 + 1) + var6;
			}

			if (var8 == 3 && !par1World.isBlockOpaqueCube(par2, par3, par4 - 1)) {
				var13 = (double) (par4 + 0) - var6;
			}

			if (var8 == 4 && !par1World.isBlockOpaqueCube(par2 + 1, par3, par4)) {
				var9 = (double) (par2 + 1) + var6;
			}

			if (var8 == 5 && !par1World.isBlockOpaqueCube(par2 - 1, par3, par4)) {
				var9 = (double) (par2 + 0) - var6;
			}

			if (var9 < (double) par2 || var9 > (double) (par2 + 1) || var11 < 0.0D || var11 > (double) (par3 + 1) || var13 < (double) par4 || var13 > (double) (par4 + 1)) {
				par1World.spawnParticle("reddust", var9, var11, var13, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * Returns an item stack containing a single instance of the current block type.
	 * 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(Block.oreRedstone);
	}
}
