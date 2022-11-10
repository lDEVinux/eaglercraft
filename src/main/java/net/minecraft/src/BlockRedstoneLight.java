package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

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
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		if (this.powered) {
			this.blockIcon = par1IconRegister.registerIcon("redstoneLight_lit");
		} else {
			this.blockIcon = par1IconRegister.registerIcon("redstoneLight");
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.redstoneLampIdle.blockID;
	}

	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return Block.redstoneLampIdle.blockID;
	}
}
