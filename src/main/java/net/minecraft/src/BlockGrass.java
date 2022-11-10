package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockGrass extends Block {
	private Icon iconGrassTop;
	private Icon iconSnowSide;
	private Icon iconGrassSideOverlay;

	protected BlockGrass(int par1) {
		super(par1, Material.grass);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? this.iconGrassTop : (par1 == 0 ? Block.dirt.getBlockTextureFromSide(par1) : this.blockIcon);
	}

	/**
	 * Retrieves the block texture to use based on the display side. Args:
	 * iBlockAccess, x, y, z, side
	 */
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (par5 == 1) {
			return this.iconGrassTop;
		} else if (par5 == 0) {
			return Block.dirt.getBlockTextureFromSide(par5);
		} else {
			Material var6 = par1IBlockAccess.getBlockMaterial(par2, par3 + 1, par4);
			return var6 != Material.snow && var6 != Material.craftedSnow ? this.blockIcon : this.iconSnowSide;
		}
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("grass_side");
		this.iconGrassTop = par1IconRegister.registerIcon("grass_top");
		this.iconSnowSide = par1IconRegister.registerIcon("snow_side");
		this.iconGrassSideOverlay = par1IconRegister.registerIcon("grass_side_overlay");
	}

	public int getBlockColor() {
		double var1 = 0.5D;
		double var3 = 1.0D;
		return ColorizerGrass.getGrassColor(var1, var3);
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1) {
		return this.getBlockColor();
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against
	 * the blocks color. Note only called when first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = 0;
		int var6 = 0;
		int var7 = 0;

		for (int var8 = -1; var8 <= 1; ++var8) {
			for (int var9 = -1; var9 <= 1; ++var9) {
				int var10 = par1IBlockAccess.getBiomeGenForCoords(par2 + var9, par4 + var8).getBiomeGrassColor();
				var5 += (var10 & 16711680) >> 16;
				var6 += (var10 & 65280) >> 8;
				var7 += var10 & 255;
			}
		}
		
		initNoiseField(par2 >> 4, par4 >> 4);
		float noise = (float)(grassNoiseArray[(par4 & 15) + (par2 & 15) * 16]) * 0.15F + 1.0F;
		
		var6 = (int)((var6 / 9) * noise);
		
		if(var6 > 255) var6 = 255;
		if(var6 < 0) var6 = 0;
		
		return ((var5 / 9) & 255) << 16 | (var6 & 255) << 8 | (var7 / 9) & 255;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.dirt.idDropped(0, par2Random, par3);
	}

	public static Icon getIconSideOverlay() {
		return Block.grass.iconGrassSideOverlay;
	}
}
