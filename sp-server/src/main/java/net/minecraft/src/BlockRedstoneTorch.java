package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockRedstoneTorch extends BlockTorch {
	/** Whether the redstone torch is currently active or not. */
	private boolean torchActive = false;

	/** Map of ArrayLists of RedstoneUpdateInfo. Key of map is World. */
	private static Map redstoneUpdateInfoCache = new HashMap();

	private boolean checkForBurnout(World par1World, int par2, int par3, int par4, boolean par5) {
		if (!redstoneUpdateInfoCache.containsKey(par1World)) {
			redstoneUpdateInfoCache.put(par1World, new ArrayList());
		}

		List var6 = (List) redstoneUpdateInfoCache.get(par1World);

		if (par5) {
			var6.add(new RedstoneUpdateInfo(par2, par3, par4, par1World.getTotalWorldTime()));
		}

		int var7 = 0;

		for (int var8 = 0; var8 < var6.size(); ++var8) {
			RedstoneUpdateInfo var9 = (RedstoneUpdateInfo) var6.get(var8);

			if (var9.x == par2 && var9.y == par3 && var9.z == par4) {
				++var7;

				if (var7 >= 8) {
					return true;
				}
			}
		}

		return false;
	}

	protected BlockRedstoneTorch(int par1, boolean par2) {
		super(par1);
		this.torchActive = par2;
		this.setTickRandomly(true);
		this.setCreativeTab((CreativeTabs) null);
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World par1World) {
		return 2;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		if (par1World.getBlockMetadata(par2, par3, par4) == 0) {
			super.onBlockAdded(par1World, par2, par3, par4);
		}

		if (this.torchActive) {
			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		if (this.torchActive) {
			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
		}
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the
	 * specified side. If isBlockNormalCube returns true, standard redstone
	 * propagation rules will apply instead and this will not be called. Args:
	 * World, X, Y, Z, side. Note that the side is reversed - eg it is 1 (up) when
	 * checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (!this.torchActive) {
			return 0;
		} else {
			int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
			return var6 == 5 && par5 == 1 ? 0
					: (var6 == 3 && par5 == 3 ? 0
							: (var6 == 4 && par5 == 2 ? 0
									: (var6 == 1 && par5 == 5 ? 0 : (var6 == 2 && par5 == 4 ? 0 : 15))));
		}
	}

	/**
	 * Returns true or false based on whether the block the torch is attached to is
	 * providing indirect power.
	 */
	private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockMetadata(par2, par3, par4);
		return var5 == 5 && par1World.getIndirectPowerOutput(par2, par3 - 1, par4, 0) ? true
				: (var5 == 3 && par1World.getIndirectPowerOutput(par2, par3, par4 - 1, 2) ? true
						: (var5 == 4 && par1World.getIndirectPowerOutput(par2, par3, par4 + 1, 3) ? true
								: (var5 == 1 && par1World.getIndirectPowerOutput(par2 - 1, par3, par4, 4) ? true
										: var5 == 2 && par1World.getIndirectPowerOutput(par2 + 1, par3, par4, 5))));
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		boolean var6 = this.isIndirectlyPowered(par1World, par2, par3, par4);
		List var7 = (List) redstoneUpdateInfoCache.get(par1World);

		while (var7 != null && !var7.isEmpty()
				&& par1World.getTotalWorldTime() - ((RedstoneUpdateInfo) var7.get(0)).updateTime > 60L) {
			var7.remove(0);
		}

		if (this.torchActive) {
			if (var6) {
				par1World.setBlock(par2, par3, par4, Block.torchRedstoneIdle.blockID,
						par1World.getBlockMetadata(par2, par3, par4), 3);

				if (this.checkForBurnout(par1World, par2, par3, par4, true)) {
					par1World.playSoundEffect((double) ((float) par2 + 0.5F), (double) ((float) par3 + 0.5F),
							(double) ((float) par4 + 0.5F), "random.fizz", 0.5F,
							2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

					for (int var8 = 0; var8 < 5; ++var8) {
						double var9 = (double) par2 + par5Random.nextDouble() * 0.6D + 0.2D;
						double var11 = (double) par3 + par5Random.nextDouble() * 0.6D + 0.2D;
						double var13 = (double) par4 + par5Random.nextDouble() * 0.6D + 0.2D;
						par1World.spawnParticle("smoke", var9, var11, var13, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		} else if (!var6 && !this.checkForBurnout(par1World, par2, par3, par4, false)) {
			par1World.setBlock(par2, par3, par4, Block.torchRedstoneActive.blockID,
					par1World.getBlockMetadata(par2, par3, par4), 3);
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!this.func_94397_d(par1World, par2, par3, par4, par5)) {
			boolean var6 = this.isIndirectlyPowered(par1World, par2, par3, par4);

			if (this.torchActive && var6 || !this.torchActive && !var6) {
				par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
			}
		}
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the
	 * specified side. Args: World, X, Y, Z, side. Note that the side is reversed -
	 * eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 == 0 ? this.isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5) : 0;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.torchRedstoneActive.blockID;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change
	 * based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**
	 * Returns true if the given block ID is equivalent to this one. Example:
	 * redstoneTorchOn matches itself and redstoneTorchOff, and vice versa. Most
	 * blocks only match themselves.
	 */
	public boolean isAssociatedBlockID(int par1) {
		return par1 == Block.torchRedstoneIdle.blockID || par1 == Block.torchRedstoneActive.blockID;
	}
}
