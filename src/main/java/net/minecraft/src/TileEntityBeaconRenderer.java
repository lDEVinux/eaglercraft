package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer {
	
	private static final TextureLocation tex_beam = new TextureLocation("/misc/beam.png");
	
	/**
	 * Render a beacon tile entity.
	 */
	public void renderTileEntityBeaconAt(TileEntityBeacon par1TileEntityBeacon, double par2, double par4, double par6, float par8) {
		float var9 = par1TileEntityBeacon.func_82125_v_();
		
		if (var9 > 0.0F) {
			Tessellator var10 = Tessellator.instance;
			tex_beam.bindTexture();
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_REPEAT);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_REPEAT);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glDepthMask(true);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE);
			float var11 = ((System.currentTimeMillis() % 200000) / 50.0f);//par1TileEntityBeacon.getWorldObj().getWorldTime() + par8;
			float var12 = -var11 * 0.2F - (float) MathHelper.floor_float(-var11 * 0.1F);
			byte var13 = 1;
			double var14 = (double) var11 * 0.025D * (1.0D - (double) (var13 & 1) * 2.5D);
			var10.startDrawingQuads();
			var10.setColorRGBA(255, 255, 255, 32);
			double var16 = (double) var13 * 0.2D;
			double var18 = 0.5D + Math.cos(var14 + 2.356194490192345D) * var16;
			double var20 = 0.5D + Math.sin(var14 + 2.356194490192345D) * var16;
			double var22 = 0.5D + Math.cos(var14 + (Math.PI / 4D)) * var16;
			double var24 = 0.5D + Math.sin(var14 + (Math.PI / 4D)) * var16;
			double var26 = 0.5D + Math.cos(var14 + 3.9269908169872414D) * var16;
			double var28 = 0.5D + Math.sin(var14 + 3.9269908169872414D) * var16;
			double var30 = 0.5D + Math.cos(var14 + 5.497787143782138D) * var16;
			double var32 = 0.5D + Math.sin(var14 + 5.497787143782138D) * var16;
			double var34 = (double) (256.0F * var9);
			double var36 = 0.0D;
			double var38 = 1.0D;
			double var40 = (double) (-1.0F + var12);
			double var42 = (double) (256.0F * var9) * (0.5D / var16) + var40;
			var10.addVertexWithUV(par2 + var18, par4 + var34, par6 + var20, var38, var42);
			var10.addVertexWithUV(par2 + var18, par4, par6 + var20, var38, var40);
			var10.addVertexWithUV(par2 + var22, par4, par6 + var24, var36, var40);
			var10.addVertexWithUV(par2 + var22, par4 + var34, par6 + var24, var36, var42);
			var10.addVertexWithUV(par2 + var30, par4 + var34, par6 + var32, var38, var42);
			var10.addVertexWithUV(par2 + var30, par4, par6 + var32, var38, var40);
			var10.addVertexWithUV(par2 + var26, par4, par6 + var28, var36, var40);
			var10.addVertexWithUV(par2 + var26, par4 + var34, par6 + var28, var36, var42);
			var10.addVertexWithUV(par2 + var22, par4 + var34, par6 + var24, var38, var42);
			var10.addVertexWithUV(par2 + var22, par4, par6 + var24, var38, var40);
			var10.addVertexWithUV(par2 + var30, par4, par6 + var32, var36, var40);
			var10.addVertexWithUV(par2 + var30, par4 + var34, par6 + var32, var36, var42);
			var10.addVertexWithUV(par2 + var26, par4 + var34, par6 + var28, var38, var42);
			var10.addVertexWithUV(par2 + var26, par4, par6 + var28, var38, var40);
			var10.addVertexWithUV(par2 + var18, par4, par6 + var20, var36, var40);
			var10.addVertexWithUV(par2 + var18, par4 + var34, par6 + var20, var36, var42);
			var10.draw();
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			EaglerAdapter.glDepthMask(false);
			var10.startDrawingQuads();
			var10.setColorRGBA(255, 255, 255, 32);
			double var44 = 0.2D;
			double var15 = 0.2D;
			double var17 = 0.8D;
			double var19 = 0.2D;
			double var21 = 0.2D;
			double var23 = 0.8D;
			double var25 = 0.8D;
			double var27 = 0.8D;
			double var29 = (double) (256.0F * var9);
			double var31 = 0.0D;
			double var33 = 1.0D;
			double var35 = (double) (-1.0F + var12);
			double var37 = (double) (256.0F * var9) + var35;
			var10.addVertexWithUV(par2 + var44, par4 + var29, par6 + var15, var33, var37);
			var10.addVertexWithUV(par2 + var44, par4, par6 + var15, var33, var35);
			var10.addVertexWithUV(par2 + var17, par4, par6 + var19, var31, var35);
			var10.addVertexWithUV(par2 + var17, par4 + var29, par6 + var19, var31, var37);
			var10.addVertexWithUV(par2 + var25, par4 + var29, par6 + var27, var33, var37);
			var10.addVertexWithUV(par2 + var25, par4, par6 + var27, var33, var35);
			var10.addVertexWithUV(par2 + var21, par4, par6 + var23, var31, var35);
			var10.addVertexWithUV(par2 + var21, par4 + var29, par6 + var23, var31, var37);
			var10.addVertexWithUV(par2 + var17, par4 + var29, par6 + var19, var33, var37);
			var10.addVertexWithUV(par2 + var17, par4, par6 + var19, var33, var35);
			var10.addVertexWithUV(par2 + var25, par4, par6 + var27, var31, var35);
			var10.addVertexWithUV(par2 + var25, par4 + var29, par6 + var27, var31, var37);
			var10.addVertexWithUV(par2 + var21, par4 + var29, par6 + var23, var33, var37);
			var10.addVertexWithUV(par2 + var21, par4, par6 + var23, var33, var35);
			var10.addVertexWithUV(par2 + var44, par4, par6 + var15, var31, var35);
			var10.addVertexWithUV(par2 + var44, par4 + var29, par6 + var15, var31, var37);
			var10.draw();
			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glDepthMask(true);
		}
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderTileEntityBeaconAt((TileEntityBeacon) par1TileEntity, par2, par4, par6, par8);
	}
}
