package net.minecraft.src;

public class BlockRailPowered extends BlockRailBase {
	protected BlockRailPowered(int par1) {
		super(par1, true);
	}

	protected boolean func_94360_a(World par1World, int par2, int par3, int par4, int par5, boolean par6, int par7) {
		if (par7 >= 8) {
			return false;
		} else {
			int var8 = par5 & 7;
			boolean var9 = true;

			switch (var8) {
			case 0:
				if (par6) {
					++par4;
				} else {
					--par4;
				}

				break;

			case 1:
				if (par6) {
					--par2;
				} else {
					++par2;
				}

				break;

			case 2:
				if (par6) {
					--par2;
				} else {
					++par2;
					++par3;
					var9 = false;
				}

				var8 = 1;
				break;

			case 3:
				if (par6) {
					--par2;
					++par3;
					var9 = false;
				} else {
					++par2;
				}

				var8 = 1;
				break;

			case 4:
				if (par6) {
					++par4;
				} else {
					--par4;
					++par3;
					var9 = false;
				}

				var8 = 0;
				break;

			case 5:
				if (par6) {
					++par4;
					++par3;
					var9 = false;
				} else {
					--par4;
				}

				var8 = 0;
			}

			return this.func_94361_a(par1World, par2, par3, par4, par6, par7, var8) ? true
					: var9 && this.func_94361_a(par1World, par2, par3 - 1, par4, par6, par7, var8);
		}
	}

	protected boolean func_94361_a(World par1World, int par2, int par3, int par4, boolean par5, int par6, int par7) {
		int var8 = par1World.getBlockId(par2, par3, par4);

		if (var8 == this.blockID) {
			int var9 = par1World.getBlockMetadata(par2, par3, par4);
			int var10 = var9 & 7;

			if (par7 == 1 && (var10 == 0 || var10 == 4 || var10 == 5)) {
				return false;
			}

			if (par7 == 0 && (var10 == 1 || var10 == 2 || var10 == 3)) {
				return false;
			}

			if ((var9 & 8) != 0) {
				if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
					return true;
				}

				return this.func_94360_a(par1World, par2, par3, par4, var9, par5, par6 + 1);
			}
		}

		return false;
	}

	protected void func_94358_a(World par1World, int par2, int par3, int par4, int par5, int par6, int par7) {
		boolean var8 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
		var8 = var8 || this.func_94360_a(par1World, par2, par3, par4, par5, true, 0)
				|| this.func_94360_a(par1World, par2, par3, par4, par5, false, 0);
		boolean var9 = false;

		if (var8 && (par5 & 8) == 0) {
			par1World.setBlockMetadata(par2, par3, par4, par6 | 8, 3);
			var9 = true;
		} else if (!var8 && (par5 & 8) != 0) {
			par1World.setBlockMetadata(par2, par3, par4, par6, 3);
			var9 = true;
		}

		if (var9) {
			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);

			if (par6 == 2 || par6 == 3 || par6 == 4 || par6 == 5) {
				par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
			}
		}
	}
}
