package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderEnchantmentTable extends TileEntitySpecialRenderer {
	private ModelBook enchantmentBook = new ModelBook();
	
	private static final TextureLocation tex_book = new TextureLocation("/item/book.png");

	public void renderTileEntityEnchantmentTableAt(TileEntityEnchantmentTable par1TileEntityEnchantmentTable, double par2, double par4, double par6, float par8) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) par2 + 0.5F, (float) par4 + 0.75F, (float) par6 + 0.5F);
		float var9 = (float) par1TileEntityEnchantmentTable.tickCount + par8;
		EaglerAdapter.glTranslatef(0.0F, 0.1F + MathHelper.sin(var9 * 0.1F) * 0.01F, 0.0F);
		float var10;

		for (var10 = par1TileEntityEnchantmentTable.bookRotation2 - par1TileEntityEnchantmentTable.bookRotationPrev; var10 >= (float) Math.PI; var10 -= ((float) Math.PI * 2F)) {
			;
		}

		while (var10 < -(float) Math.PI) {
			var10 += ((float) Math.PI * 2F);
		}

		float var11 = par1TileEntityEnchantmentTable.bookRotationPrev + var10 * par8;
		EaglerAdapter.glRotatef(-var11 * 180.0F / (float) Math.PI, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(80.0F, 0.0F, 0.0F, 1.0F);
		tex_book.bindTexture();
		float var12 = par1TileEntityEnchantmentTable.pageFlipPrev + (par1TileEntityEnchantmentTable.pageFlip - par1TileEntityEnchantmentTable.pageFlipPrev) * par8 + 0.25F;
		float var13 = par1TileEntityEnchantmentTable.pageFlipPrev + (par1TileEntityEnchantmentTable.pageFlip - par1TileEntityEnchantmentTable.pageFlipPrev) * par8 + 0.75F;
		var12 = (var12 - (float) MathHelper.truncateDoubleToInt((double) var12)) * 1.6F - 0.3F;
		var13 = (var13 - (float) MathHelper.truncateDoubleToInt((double) var13)) * 1.6F - 0.3F;

		if (var12 < 0.0F) {
			var12 = 0.0F;
		}

		if (var13 < 0.0F) {
			var13 = 0.0F;
		}

		if (var12 > 1.0F) {
			var12 = 1.0F;
		}

		if (var13 > 1.0F) {
			var13 = 1.0F;
		}

		float var14 = par1TileEntityEnchantmentTable.bookSpreadPrev + (par1TileEntityEnchantmentTable.bookSpread - par1TileEntityEnchantmentTable.bookSpreadPrev) * par8;
		EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
		this.enchantmentBook.render((Entity) null, var9, var12, var13, var14, 0.0F, 0.0625F);
		EaglerAdapter.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderTileEntityEnchantmentTableAt((TileEntityEnchantmentTable) par1TileEntity, par2, par4, par6, par8);
	}
}
