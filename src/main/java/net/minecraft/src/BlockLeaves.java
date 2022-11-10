package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockLeaves extends BlockLeavesBase {
	public static final String[] LEAF_TYPES = new String[] { "oak", "spruce", "birch", "jungle" };
	public static final String[][] field_94396_b = new String[][] { { "leaves", "leaves_spruce", "leaves", "leaves_jungle" }, { "leaves_opaque", "leaves_spruce_opaque", "leaves_opaque", "leaves_jungle_opaque" } };
	private int field_94394_cP;
	private Icon[][] iconArray = new Icon[2][];
	int[] adjacentTreeBlocks;

	protected BlockLeaves(int par1) {
		super(par1, Material.leaves, false);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	public int getBlockColor() {
		double var1 = 0.5D;
		double var3 = 1.0D;
		return ColorizerFoliage.getFoliageColor(var1, var3);
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1) {
		return (par1 & 3) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((par1 & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic());
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against
	 * the blocks color. Note only called when first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

		if ((var5 & 3) == 1) {
			return ColorizerFoliage.getFoliageColorPine();
		} else if ((var5 & 3) == 2) {
			return ColorizerFoliage.getFoliageColorBirch();
		} else {
			int var6 = 0;
			int var7 = 0;
			int var8 = 0;

			for (int var9 = -1; var9 <= 1; ++var9) {
				for (int var10 = -1; var10 <= 1; ++var10) {
					int var11 = par1IBlockAccess.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor();
					var6 += (var11 & 16711680) >> 16;
					var7 += (var11 & 65280) >> 8;
					var8 += var11 & 255;
				}
			}
			
			initNoiseField(par2 >> 4, par4 >> 4);
			float noise = (float)(grassNoiseArray[(par4 & 15) + (par2 & 15) * 16]) * 0.3F + 1.0F;
			
			var7 = (int)((var7 / 9) * noise);
			
			if(var7 > 255) var7 = 255;
			if(var7 < 0) var7 = 0;
			
			return (var6 / 9 & 255) << 16 | (var7 & 255) << 8 | var8 / 9 & 255;
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		byte var7 = 1;
		int var8 = var7 + 1;

		if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
			for (int var9 = -var7; var9 <= var7; ++var9) {
				for (int var10 = -var7; var10 <= var7; ++var10) {
					for (int var11 = -var7; var11 <= var7; ++var11) {
						int var12 = par1World.getBlockId(par2 + var9, par3 + var10, par4 + var11);

						if (var12 == Block.leaves.blockID) {
							int var13 = par1World.getBlockMetadata(par2 + var9, par3 + var10, par4 + var11);
							par1World.setBlockMetadataWithNotify(par2 + var9, par3 + var10, par4 + var11, var13 | 8, 4);
						}
					}
				}
			}
		}
	}

	/**
	 * A randomly called display update to be able to add particles or other items
	 * for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (par1World.canLightningStrikeAt(par2, par3 + 1, par4) && !par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && par5Random.nextInt(15) == 1) {
			double var6 = (double) ((float) par2 + par5Random.nextFloat());
			double var8 = (double) par3 - 0.05D;
			double var10 = (double) ((float) par4 + par5Random.nextFloat());
			par1World.spawnParticle("dripWater", var6, var8, var10, 0.0D, 0.0D, 0.0D);
		}
	}

	private void removeLeaves(World par1World, int par2, int par3, int par4) {
		this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
		par1World.setBlockToAir(par2, par3, par4);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return par1Random.nextInt(20) == 0 ? 1 : 0;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.sapling.blockID;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1 & 3;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether or
	 * not to render the shared face of two adjacent blocks and also whether the
	 * player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return (par2 & 3) == 1 ? this.iconArray[this.field_94394_cP][1] : ((par2 & 3) == 3 ? this.iconArray[this.field_94394_cP][3] : this.iconArray[this.field_94394_cP][0]);
	}

	/**
	 * Pass true to draw this block using fancy graphics, or false for fast
	 * graphics.
	 */
	public void setGraphicsLevel(boolean par1) {
		this.graphicsLevel = par1;
		this.field_94394_cP = par1 ? 0 : 1;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
	}

	/**
	 * Returns an item stack containing a single instance of the current block type.
	 * 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(this.blockID, 1, par1 & 3);
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		for (int var2 = 0; var2 < field_94396_b.length; ++var2) {
			this.iconArray[var2] = new Icon[field_94396_b[var2].length];

			for (int var3 = 0; var3 < field_94396_b[var2].length; ++var3) {
				this.iconArray[var2][var3] = par1IconRegister.registerIcon(field_94396_b[var2][var3]);
			}
		}
	}
}
