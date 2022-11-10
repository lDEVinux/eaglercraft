package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockTallGrass extends BlockFlower {
	private static final String[] grassTypes = new String[] { "deadbush", "tallgrass", "fern" };
	private Icon[] iconArray;

	protected BlockTallGrass(int par1) {
		super(par1, Material.vine);
		float var2 = 0.4F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.8F, 0.5F + var2);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		if (par2 >= this.iconArray.length) {
			par2 = 0;
		}

		return this.iconArray[par2];
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
		return par1 == 0 ? 16777215 : ColorizerFoliage.getFoliageColorBasic();
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against
	 * the blocks color. Note only called when first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		if(var5 == 0) return 16777215;
		var5 = par1IBlockAccess.getBiomeGenForCoords(par2, par4).getBiomeGrassColor();

		initNoiseField(par2 >> 4, par4 >> 4);
		float noise = (float)(grassNoiseArray[(par4 & 15) + (par2 & 15) * 16]) * 0.25F + 1.0F;
		int var6 = (int)(((var5 >> 8) & 255) * noise);
		if(var6 > 255) var6 = 255;
		if(var6 < 0) var6 = 0;
		
		return (var5 & 0xff00ff) | (var6 << 8);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return par2Random.nextInt(8) == 0 ? Item.seeds.itemID : -1;
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i'
	 * (inclusive).
	 */
	public int quantityDroppedWithBonus(int par1, EaglercraftRandom par2Random) {
		return 1 + par2Random.nextInt(par1 * 2 + 1);
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		return par1World.getBlockMetadata(par2, par3, par4);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int var4 = 1; var4 < 3; ++var4) {
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.iconArray = new Icon[grassTypes.length];

		for (int var2 = 0; var2 < this.iconArray.length; ++var2) {
			this.iconArray[var2] = par1IconRegister.registerIcon(grassTypes[var2]);
		}
	}
}
