package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockRedstoneLight extends Block {
	/** Whether this lamp block is the powered version. */
	private final boolean powered;

	public BlockRedstoneLight(int par1, boolean par2) {
		super(par1, Material.redstoneLight);
		this.powered = par2;

		if (par2) {
			this.setLightValue(1.0F);
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		if (!par1World.isRemote) {
			if (this.powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
				par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
			} else if (!this.powered && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
				par1World.setBlock(par2, par3, par4, Block.redstoneLampActive.blockID, 0, 2);
			}
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isRemote) {
			if (this.powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
				par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
			} else if (!this.powered && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
				par1World.setBlock(par2, par3, par4, Block.redstoneLampActive.blockID, 0, 2);
			}
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (!par1World.isRemote && this.powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
			par1World.setBlock(par2, par3, par4, Block.redstoneLampIdle.blockID, 0, 2);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.redstoneLampIdle.blockID;
	}
}
