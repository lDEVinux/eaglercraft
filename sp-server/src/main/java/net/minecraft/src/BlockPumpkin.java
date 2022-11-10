package net.minecraft.src;

public class BlockPumpkin extends BlockDirectional {
	/** Boolean used to seperate different states of blocks */
	private boolean blockType;

	protected BlockPumpkin(int par1, boolean par2) {
		super(par1, Material.pumpkin);
		this.setTickRandomly(true);
		this.blockType = par2;
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);

		if (par1World.getBlockId(par2, par3 - 1, par4) == Block.blockSnow.blockID
				&& par1World.getBlockId(par2, par3 - 2, par4) == Block.blockSnow.blockID) {
			if (!par1World.isRemote) {
				par1World.setBlock(par2, par3, par4, 0, 0, 2);
				par1World.setBlock(par2, par3 - 1, par4, 0, 0, 2);
				par1World.setBlock(par2, par3 - 2, par4, 0, 0, 2);
				EntitySnowman var9 = new EntitySnowman(par1World);
				var9.setLocationAndAngles((double) par2 + 0.5D, (double) par3 - 1.95D, (double) par4 + 0.5D, 0.0F,
						0.0F);
				par1World.spawnEntityInWorld(var9);
				par1World.notifyBlockChange(par2, par3, par4, 0);
				par1World.notifyBlockChange(par2, par3 - 1, par4, 0);
				par1World.notifyBlockChange(par2, par3 - 2, par4, 0);
			}

			for (int var10 = 0; var10 < 120; ++var10) {
				par1World.spawnParticle("snowshovel", (double) par2 + par1World.rand.nextDouble(),
						(double) (par3 - 2) + par1World.rand.nextDouble() * 2.5D,
						(double) par4 + par1World.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		} else if (par1World.getBlockId(par2, par3 - 1, par4) == Block.blockIron.blockID
				&& par1World.getBlockId(par2, par3 - 2, par4) == Block.blockIron.blockID) {
			boolean var5 = par1World.getBlockId(par2 - 1, par3 - 1, par4) == Block.blockIron.blockID
					&& par1World.getBlockId(par2 + 1, par3 - 1, par4) == Block.blockIron.blockID;
			boolean var6 = par1World.getBlockId(par2, par3 - 1, par4 - 1) == Block.blockIron.blockID
					&& par1World.getBlockId(par2, par3 - 1, par4 + 1) == Block.blockIron.blockID;

			if (var5 || var6) {
				par1World.setBlock(par2, par3, par4, 0, 0, 2);
				par1World.setBlock(par2, par3 - 1, par4, 0, 0, 2);
				par1World.setBlock(par2, par3 - 2, par4, 0, 0, 2);

				if (var5) {
					par1World.setBlock(par2 - 1, par3 - 1, par4, 0, 0, 2);
					par1World.setBlock(par2 + 1, par3 - 1, par4, 0, 0, 2);
				} else {
					par1World.setBlock(par2, par3 - 1, par4 - 1, 0, 0, 2);
					par1World.setBlock(par2, par3 - 1, par4 + 1, 0, 0, 2);
				}

				EntityIronGolem var7 = new EntityIronGolem(par1World);
				var7.setPlayerCreated(true);
				var7.setLocationAndAngles((double) par2 + 0.5D, (double) par3 - 1.95D, (double) par4 + 0.5D, 0.0F,
						0.0F);
				par1World.spawnEntityInWorld(var7);

				for (int var8 = 0; var8 < 120; ++var8) {
					par1World.spawnParticle("snowballpoof", (double) par2 + par1World.rand.nextDouble(),
							(double) (par3 - 2) + par1World.rand.nextDouble() * 3.9D,
							(double) par4 + par1World.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
				}

				par1World.notifyBlockChange(par2, par3, par4, 0);
				par1World.notifyBlockChange(par2, par3 - 1, par4, 0);
				par1World.notifyBlockChange(par2, par3 - 2, par4, 0);

				if (var5) {
					par1World.notifyBlockChange(par2 - 1, par3 - 1, par4, 0);
					par1World.notifyBlockChange(par2 + 1, par3 - 1, par4, 0);
				} else {
					par1World.notifyBlockChange(par2, par3 - 1, par4 - 1, 0);
					par1World.notifyBlockChange(par2, par3 - 1, par4 + 1, 0);
				}
			}
		}
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockId(par2, par3, par4);
		return (var5 == 0 || Block.blocksList[var5].blockMaterial.isReplaceable())
				&& par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		int var7 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		par1World.setBlockMetadata(par2, par3, par4, var7, 2);
	}
}
