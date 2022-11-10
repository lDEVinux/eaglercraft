package net.minecraft.src;

import java.util.Calendar;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer {
	/** The normal small chest model. */
	private ModelChest chestModel = new ModelChest();

	/** The large double chest model. */
	private ModelChest largeChestModel = new ModelLargeChest();

	/** If true, chests will be rendered with the Christmas present textures. */
	private boolean isChristmas;

	public TileEntityChestRenderer() {
		Calendar var1 = Calendar.getInstance();

		if (var1.get(2) + 1 == 12 && var1.get(5) >= 24 && var1.get(5) <= 26) {
			this.isChristmas = true;
		}
	}
	
	private static final TextureLocation tex_trap_small = new TextureLocation("/item/chests/trap_small.png");
	private static final TextureLocation tex_xmaschest = new TextureLocation("/item/xmaschest.png");
	private static final TextureLocation tex_chest = new TextureLocation("/item/chest.png");
	private static final TextureLocation tex_trap_large = new TextureLocation("/item/chests/trap_large.png");
	private static final TextureLocation tex_largexmaschest = new TextureLocation("/item/largexmaschest.png");
	private static final TextureLocation tex_largechest = new TextureLocation("/item/largechest.png");

	/**
	 * Renders the TileEntity for the chest at a position.
	 */
	public void renderTileEntityChestAt(TileEntityChest par1TileEntityChest, double par2, double par4, double par6, float par8) {
		int var9;

		if (!par1TileEntityChest.func_70309_m()) {
			var9 = 0;
		} else {
			Block var10 = par1TileEntityChest.getBlockType();
			var9 = par1TileEntityChest.getBlockMetadata();

			if (var10 instanceof BlockChest && var9 == 0) {
				((BlockChest) var10).unifyAdjacentChests(par1TileEntityChest.getWorldObj(), par1TileEntityChest.xCoord, par1TileEntityChest.yCoord, par1TileEntityChest.zCoord);
				var9 = par1TileEntityChest.getBlockMetadata();
			}

			par1TileEntityChest.checkForAdjacentChests();
		}

		if (par1TileEntityChest.adjacentChestZNeg == null && par1TileEntityChest.adjacentChestXNeg == null) {
			ModelChest var14;

			if (par1TileEntityChest.adjacentChestXPos == null && par1TileEntityChest.adjacentChestZPosition == null) {
				var14 = this.chestModel;

				if (par1TileEntityChest.func_98041_l() == 1) {
					tex_trap_small.bindTexture();
				} else if (this.isChristmas) {
					tex_xmaschest.bindTexture();
				} else {
					tex_chest.bindTexture();
				}
			} else {
				var14 = this.largeChestModel;

				if (par1TileEntityChest.func_98041_l() == 1) {
					tex_trap_large.bindTexture();
				} else if (this.isChristmas) {
					tex_largexmaschest.bindTexture();
				} else {
					tex_largechest.bindTexture();
				}
			}

			EaglerAdapter.glPushMatrix();
			//EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glTranslatef((float) par2, (float) par4 + 1.0f, (float) par6 + 1.0f);
			EaglerAdapter.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glTranslatef(0.5F, 0.5F, 0.5F);
			
			short var11 = 0;

			if (var9 == 2) {
				var11 = 180;
			}

			if (var9 == 3) {
				var11 = 0;
			}

			if (var9 == 4) {
				var11 = 90;
			}

			if (var9 == 5) {
				var11 = -90;
			}

			if (var9 == 2 && par1TileEntityChest.adjacentChestXPos != null) {
				EaglerAdapter.glTranslatef(1.0F, 0.0F, 0.0F);
			}

			if (var9 == 5 && par1TileEntityChest.adjacentChestZPosition != null) {
				EaglerAdapter.glTranslatef(0.0F, 0.0F, -1.0F);
			}

			EaglerAdapter.glRotatef((float) var11, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glTranslatef(-0.5F, -0.5F, -0.5F);
			float var12 = par1TileEntityChest.prevLidAngle + (par1TileEntityChest.lidAngle - par1TileEntityChest.prevLidAngle) * par8;
			float var13;

			if (par1TileEntityChest.adjacentChestZNeg != null) {
				var13 = par1TileEntityChest.adjacentChestZNeg.prevLidAngle + (par1TileEntityChest.adjacentChestZNeg.lidAngle - par1TileEntityChest.adjacentChestZNeg.prevLidAngle) * par8;

				if (var13 > var12) {
					var12 = var13;
				}
			}

			if (par1TileEntityChest.adjacentChestXNeg != null) {
				var13 = par1TileEntityChest.adjacentChestXNeg.prevLidAngle + (par1TileEntityChest.adjacentChestXNeg.lidAngle - par1TileEntityChest.adjacentChestXNeg.prevLidAngle) * par8;

				if (var13 > var12) {
					var12 = var13;
				}
			}

			var12 = 1.0F - var12;
			var12 = 1.0F - var12 * var12 * var12;
			var14.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 2.0F);
			var14.renderAll();
			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderTileEntityChestAt((TileEntityChest) par1TileEntity, par2, par4, par6, par8);
	}
}
