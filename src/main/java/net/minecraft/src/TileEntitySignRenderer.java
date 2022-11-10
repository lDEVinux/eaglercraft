package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer {
	/** The ModelSign instance used by the TileEntitySignRenderer */
	private ModelSign modelSign = new ModelSign();
	private static final TextureLocation tex_sign = new TextureLocation("/item/sign.png");

	public void renderTileEntitySignAt(TileEntitySign par1TileEntitySign, double par2, double par4, double par6, float par8) {
		Block var9 = par1TileEntitySign.getBlockType();
		EaglerAdapter.glPushMatrix();
		float var10 = 0.6666667F;
		float var12;

		if (var9 == Block.signPost) {
			EaglerAdapter.glTranslatef((float) par2 + 0.5F, (float) par4 + 0.75F * var10, (float) par6 + 0.5F);
			float var11 = (float) (par1TileEntitySign.getBlockMetadata() * 360) / 16.0F;
			EaglerAdapter.glRotatef(-var11, 0.0F, 1.0F, 0.0F);
			this.modelSign.signStick.showModel = true;
		} else {
			int var16 = par1TileEntitySign.getBlockMetadata();
			var12 = 0.0F;

			if (var16 == 2) {
				var12 = 180.0F;
			}

			if (var16 == 4) {
				var12 = 90.0F;
			}

			if (var16 == 5) {
				var12 = -90.0F;
			}

			EaglerAdapter.glTranslatef((float) par2 + 0.5F, (float) par4 + 0.75F * var10, (float) par6 + 0.5F);
			EaglerAdapter.glRotatef(-var12, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glTranslatef(0.0F, -0.3125F, -0.4375F);
			this.modelSign.signStick.showModel = false;
		}

		tex_sign.bindTexture();
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glScalef(var10, -var10, -var10);
		this.modelSign.renderSign();
		EaglerAdapter.glPopMatrix();
		FontRenderer var17 = this.getFontRenderer();
		var12 = 0.016666668F * var10;
		EaglerAdapter.glTranslatef(0.0F, 0.5F * var10, 0.07F * var10);
		EaglerAdapter.glScalef(var12, -var12, var12);
		EaglerAdapter.glNormal3f(0.0F, 0.0F, -1.0F * var12);
		EaglerAdapter.glDepthMask(false);
		byte var13 = 0;

		for (int var14 = 0; var14 < par1TileEntitySign.signText.length; ++var14) {
			String var15 = par1TileEntitySign.signText[var14];

			if (var14 == par1TileEntitySign.lineBeingEdited) {
				var15 = "> " + var15 + " <";
				var17.drawString(var15, -var17.getStringWidth(var15) / 2, var14 * 10 - par1TileEntitySign.signText.length * 5, var13);
			} else {
				var17.drawString(var15, -var17.getStringWidth(var15) / 2, var14 * 10 - par1TileEntitySign.signText.length * 5, var13);
			}
		}

		EaglerAdapter.glDepthMask(true);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderTileEntitySignAt((TileEntitySign) par1TileEntity, par2, par4, par6, par8);
	}
}
