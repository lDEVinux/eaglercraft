package net.minecraft.src;

public class BlockPumpkin extends BlockDirectional {
	/** Boolean used to seperate different states of blocks */
	private boolean blockType;
	private Icon field_94474_b;
	private Icon field_94475_c;

	protected BlockPumpkin(int par1, boolean par2) {
		super(par1, Material.pumpkin);
		this.setTickRandomly(true);
		this.blockType = par2;
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? this.field_94474_b
				: (par1 == 0 ? this.field_94474_b
						: (par2 == 2 && par1 == 2 ? this.field_94475_c : (par2 == 3 && par1 == 5 ? this.field_94475_c : (par2 == 0 && par1 == 3 ? this.field_94475_c : (par2 == 1 && par1 == 4 ? this.field_94475_c : this.blockIcon)))));
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);

		if (par1World.getBlockId(par2, par3 - 1, par4) == Block.blockSnow.blockID && par1World.getBlockId(par2, par3 - 2, par4) == Block.blockSnow.blockID) {
			for (int var10 = 0; var10 < 120; ++var10) {
				par1World.spawnParticle("snowshovel", (double) par2 + par1World.rand.nextDouble(), (double) (par3 - 2) + par1World.rand.nextDouble() * 2.5D, (double) par4 + par1World.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		} else if (par1World.getBlockId(par2, par3 - 1, par4) == Block.blockIron.blockID && par1World.getBlockId(par2, par3 - 2, par4) == Block.blockIron.blockID) {
			boolean var5 = par1World.getBlockId(par2 - 1, par3 - 1, par4) == Block.blockIron.blockID && par1World.getBlockId(par2 + 1, par3 - 1, par4) == Block.blockIron.blockID;
			boolean var6 = par1World.getBlockId(par2, par3 - 1, par4 - 1) == Block.blockIron.blockID && par1World.getBlockId(par2, par3 - 1, par4 + 1) == Block.blockIron.blockID;

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

				EntityIronGolem var7 = new EntityIronGolem();
				var7.setWorld(par1World);
				var7.setPlayerCreated(true);
				var7.setLocationAndAngles((double) par2 + 0.5D, (double) par3 - 1.95D, (double) par4 + 0.5D, 0.0F, 0.0F);
				par1World.spawnEntityInWorld(var7);

				for (int var8 = 0; var8 < 120; ++var8) {
					par1World.spawnParticle("snowballpoof", (double) par2 + par1World.rand.nextDouble(), (double) (par3 - 2) + par1World.rand.nextDouble() * 3.9D, (double) par4 + par1World.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
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
		return (var5 == 0 || Block.blocksList[var5].blockMaterial.isReplaceable()) && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		int var7 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		par1World.setBlockMetadataWithNotify(par2, par3, par4, var7, 2);
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.field_94475_c = par1IconRegister.registerIcon(this.blockType ? "pumpkin_jack" : "pumpkin_face");
		this.field_94474_b = par1IconRegister.registerIcon("pumpkin_top");
		this.blockIcon = par1IconRegister.registerIcon("pumpkin_side");
	}
}
