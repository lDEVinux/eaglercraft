package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class RenderXPOrb extends Render {
	public RenderXPOrb() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}
	
	private static final TextureLocation tex_xporb = new TextureLocation("/item/xporb.png");

	/**
	 * Renders the XP Orb.
	 */
	public void renderTheXPOrb(EntityXPOrb par1EntityXPOrb, double par2, double par4, double par6, float par8, float par9) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glAlphaFunc(0, 0.003921569f);
		int var10 = par1EntityXPOrb.getTextureByXP();
		tex_xporb.bindTexture();
		Tessellator var11 = Tessellator.instance;
		float var12 = (float) (var10 % 4 * 16 + 0 + 0.2f) / 64.0F;
		float var13 = (float) (var10 % 4 * 16 + 16 - 0.2f) / 64.0F;
		float var14 = (float) (var10 / 4 * 16 + 0 + 0.2f) / 64.0F;
		float var15 = (float) (var10 / 4 * 16 + 16 - 0.2f) / 64.0F;
		float var16 = 1.0F;
		float var17 = 0.5F;
		float var18 = 0.25F;
		int var19 = par1EntityXPOrb.getBrightnessForRender(par9);
		int var20 = var19 % 65536;
		int var21 = var19 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var20 / 1.0F, (float) var21 / 1.0F);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var26 = 255.0F;
		float var27 = ((float) par1EntityXPOrb.xpColor + par9) / 2.0F;
		var21 = (int) ((MathHelper.sin(var27 + 0.0F) + 1.0F) * 0.5F * var26);
		int var22 = (int) var26;
		int var23 = (int) ((MathHelper.sin(var27 + 4.1887903F) + 1.0F) * 0.1F * var26);
		int var24 = var21 << 16 | var22 << 8 | var23;
		EaglerAdapter.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float var25 = 0.3F;
		EaglerAdapter.glScalef(var25, var25, var25);
		var11.startDrawingQuads();
		var11.setColorRGBA_I(var24, 128);
		var11.setNormal(0.0F, 1.0F, 0.0F);
		var11.addVertexWithUV((double) (0.0F - var17), (double) (0.0F - var18), 0.0D, (double) var12, (double) var15);
		var11.addVertexWithUV((double) (var16 - var17), (double) (0.0F - var18), 0.0D, (double) var13, (double) var15);
		var11.addVertexWithUV((double) (var16 - var17), (double) (1.0F - var18), 0.0D, (double) var13, (double) var14);
		var11.addVertexWithUV((double) (0.0F - var17), (double) (1.0F - var18), 0.0D, (double) var12, (double) var14);
		var11.draw();
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
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
		this.renderTheXPOrb((EntityXPOrb) par1Entity, par2, par4, par6, par8, par9);
	}
}
