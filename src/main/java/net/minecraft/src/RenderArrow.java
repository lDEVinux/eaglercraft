package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class RenderArrow extends Render {
	
	private static final TextureLocation tex = new TextureLocation("/item/arrows.png");
	
	public void renderArrow(EntityArrow par1EntityArrow, double par2, double par4, double par6, float par8, float par9) {
		tex.bindTexture();
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
		EaglerAdapter.glRotatef(par1EntityArrow.prevRotationYaw + (par1EntityArrow.rotationYaw - par1EntityArrow.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(par1EntityArrow.prevRotationPitch + (par1EntityArrow.rotationPitch - par1EntityArrow.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
		Tessellator var10 = Tessellator.instance;
		byte var11 = 0;
		float fix = 0.002f;
		float var12 = 0.0F + fix;
		float var13 = 0.5F - fix;
		float var14 = (float) (0 + var11 * 10) / 32.0F + fix;
		float var15 = (float) (5 + var11 * 10) / 32.0F - fix;
		float var16 = 0.0F + fix;
		float var17 = 0.15625F - fix;
		float var18 = (float) (5 + var11 * 10) / 32.0F + fix;
		float var19 = (float) (10 + var11 * 10) / 32.0F - fix;
		float var20 = 0.05625F + fix;
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		float var21 = (float) par1EntityArrow.arrowShake - par9;

		if (var21 > 0.0F) {
			float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
			EaglerAdapter.glRotatef(var22, 0.0F, 0.0F, 1.0F);
		}

		EaglerAdapter.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		EaglerAdapter.glScalef(var20, var20, var20);
		EaglerAdapter.glTranslatef(-4.0F, 0.0F, 0.0F);
		EaglerAdapter.glNormal3f(var20, 0.0F, 0.0F);
		var10.startDrawingQuads();
		var10.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double) var16, (double) var18);
		var10.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double) var17, (double) var18);
		var10.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double) var17, (double) var19);
		var10.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double) var16, (double) var19);
		var10.draw();
		EaglerAdapter.glNormal3f(-var20, 0.0F, 0.0F);
		var10.startDrawingQuads();
		var10.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double) var16, (double) var18);
		var10.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double) var17, (double) var18);
		var10.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double) var17, (double) var19);
		var10.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double) var16, (double) var19);
		var10.draw();

		for (int var23 = 0; var23 < 4; ++var23) {
			EaglerAdapter.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glNormal3f(0.0F, 0.0F, -var20);
			var10.startDrawingQuads();
			var10.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double) var12, (double) var14);
			var10.addVertexWithUV(8.0D, -2.0D, 0.0D, (double) var13, (double) var14);
			var10.addVertexWithUV(8.0D, 2.0D, 0.0D, (double) var13, (double) var15);
			var10.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double) var12, (double) var15);
			var10.draw();
		}

		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glPopMatrix();
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker function
	 * which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void
	 * doRender(T entity, double d, double d1, double d2, float f, float f1). But
	 * JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderArrow((EntityArrow) par1Entity, par2, par4, par6, par8, par9);
	}
}
