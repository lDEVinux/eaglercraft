package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockTNT extends Block {
	public BlockTNT(int par1) {
		super(par1, Material.tnt);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);

		if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
			this.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
			par1World.setBlockToAir(par2, par3, par4);
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
			this.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
			par1World.setBlockToAir(par2, par3, par4);
		}
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 1;
	}

	/**
	 * Called upon the block being destroyed by an explosion
	 */
	public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion) {
		if (!par1World.isRemote) {
			EntityTNTPrimed var6 = new EntityTNTPrimed(par1World, (double) ((float) par2 + 0.5F),
					(double) ((float) par3 + 0.5F), (double) ((float) par4 + 0.5F), par5Explosion.func_94613_c());
			var6.fuse = par1World.rand.nextInt(var6.fuse / 4) + var6.fuse / 8;
			par1World.spawnEntityInWorld(var6);
		}
	}

	/**
	 * Called right before the block is destroyed by a player. Args: world, x, y, z,
	 * metaData
	 */
	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
		this.func_94391_a(par1World, par2, par3, par4, par5, (EntityLiving) null);
	}

	public void func_94391_a(World par1World, int par2, int par3, int par4, int par5, EntityLiving par6EntityLiving) {
		if (!par1World.isRemote) {
			if ((par5 & 1) == 1) {
				EntityTNTPrimed var7 = new EntityTNTPrimed(par1World, (double) ((float) par2 + 0.5F),
						(double) ((float) par3 + 0.5F), (double) ((float) par4 + 0.5F), par6EntityLiving);
				par1World.spawnEntityInWorld(var7);
				par1World.playSoundAtEntity(var7, "random.fuse", 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		if (par5EntityPlayer.getCurrentEquippedItem() != null
				&& par5EntityPlayer.getCurrentEquippedItem().itemID == Item.flintAndSteel.itemID) {
			this.func_94391_a(par1World, par2, par3, par4, 1, par5EntityPlayer);
			par1World.setBlockToAir(par2, par3, par4);
			return true;
		} else {
			return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
		}
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the
	 * block). Args: world, x, y, z, entity
	 */
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if (par5Entity instanceof EntityArrow && !par1World.isRemote) {
			EntityArrow var6 = (EntityArrow) par5Entity;

			if (var6.isBurning()) {
				this.func_94391_a(par1World, par2, par3, par4, 1,
						var6.shootingEntity instanceof EntityLiving ? (EntityLiving) var6.shootingEntity : null);
				par1World.setBlockToAir(par2, par3, par4);
			}
		}
	}

	/**
	 * Return whether this block can drop from an explosion.
	 */
	public boolean canDropFromExplosion(Explosion par1Explosion) {
		return false;
	}
}
