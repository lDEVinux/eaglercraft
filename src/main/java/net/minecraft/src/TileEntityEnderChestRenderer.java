package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer {
	/** The Ender Chest Chest's model. */
	private ModelChest theEnderChestModel = new ModelChest();
	
	private static final TextureLocation tex_enderchest = new TextureLocation("/item/enderchest.png");
	
	/**
	 * Helps to render Ender Chest.
	 */
	public void renderEnderChest(TileEntityEnderChest par1TileEntityEnderChest, double par2, double par4, double par6, float par8) {
		int var9 = 0;

		if (par1TileEntityEnderChest.func_70309_m()) {
			var9 = par1TileEntityEnderChest.getBlockMetadata();
		}

		tex_enderchest.bindTexture();
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
		EaglerAdapter.glScalef(1.0F, -1.0F, -1.0F);
		EaglerAdapter.glTranslatef(0.5F, 0.5F, 0.5F);
		short var10 = 0;

		if (var9 == 2) {
			var10 = 180;
		}

		if (var9 == 3) {
			var10 = 0;
		}

		if (var9 == 4) {
			var10 = 90;
		}

		if (var9 == 5) {
			var10 = -90;
		}

		EaglerAdapter.glRotatef((float) var10, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glTranslatef(-0.5F, -0.5F, -0.5F);
		float var11 = par1TileEntityEnderChest.prevLidAngle + (par1TileEntityEnderChest.lidAngle - par1TileEntityEnderChest.prevLidAngle) * par8;
		var11 = 1.0F - var11;
		var11 = 1.0F - var11 * var11 * var11;
		this.theEnderChestModel.chestLid.rotateAngleX = -(var11 * (float) Math.PI / 2.0F);
		this.theEnderChestModel.renderAll();
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderEnderChest((TileEntityEnderChest) par1TileEntity, par2, par4, par6, par8);
	}
}
