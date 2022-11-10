package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockMycelium extends Block {
	private Icon field_94422_a;
	private Icon field_94421_b;

	protected BlockMycelium(int par1) {
		super(par1, Material.grass);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? this.field_94422_a : (par1 == 0 ? Block.dirt.getBlockTextureFromSide(par1) : this.blockIcon);
	}

	/**
	 * Retrieves the block texture to use based on the display side. Args:
	 * iBlockAccess, x, y, z, side
	 */
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (par5 == 1) {
			return this.field_94422_a;
		} else if (par5 == 0) {
			return Block.dirt.getBlockTextureFromSide(par5);
		} else {
			Material var6 = par1IBlockAccess.getBlockMaterial(par2, par3 + 1, par4);
			return var6 != Material.snow && var6 != Material.craftedSnow ? this.blockIcon : this.field_94421_b;
		}
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("mycel_side");
		this.field_94422_a = par1IconRegister.registerIcon("mycel_top");
		this.field_94421_b = par1IconRegister.registerIcon("snow_side");
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
	}

	/**
	 * A randomly called display update to be able to add particles or other items
	 * for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		super.randomDisplayTick(par1World, par2, par3, par4, par5Random);

		if (par5Random.nextInt(10) == 0) {
			par1World.spawnParticle("townaura", (double) ((float) par2 + par5Random.nextFloat()), (double) ((float) par3 + 1.1F), (double) ((float) par4 + par5Random.nextFloat()), 0.0D, 0.0D, 0.0D);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.dirt.idDropped(0, par2Random, par3);
	}
}
