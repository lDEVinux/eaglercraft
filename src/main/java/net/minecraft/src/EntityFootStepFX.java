package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class EntityFootStepFX extends EntityFX {
	private int field_70576_a = 0;
	private int field_70578_aq = 0;
	private RenderEngine currentFootSteps;

	public EntityFootStepFX(RenderEngine par1RenderEngine, World par2World, double par3, double par5, double par7) {
		super(par2World, par3, par5, par7, 0.0D, 0.0D, 0.0D);
		this.currentFootSteps = par1RenderEngine;
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.field_70578_aq = 200;
	}
	
	private static final TextureLocation fp = new TextureLocation("/misc/footprint.png");

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
		float var8 = ((float) this.field_70576_a + par2) / (float) this.field_70578_aq;
		var8 *= var8;
		float var9 = 2.0F - var8 * 2.0F;

		if (var9 > 1.0F) {
			var9 = 1.0F;
		}

		var9 *= 0.2F;
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		float var10 = 0.125F;
		float var11 = (float) (this.posX - interpPosX);
		float var12 = (float) (this.posY - interpPosY);
		float var13 = (float) (this.posZ - interpPosZ);
		float var14 = this.worldObj.getLightBrightness(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
		fp.bindTexture();
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setColorRGBA_F(var14, var14, var14, var9);
		par1Tessellator.addVertexWithUV((double) (var11 - var10), (double) var12, (double) (var13 + var10), 0.0D, 1.0D);
		par1Tessellator.addVertexWithUV((double) (var11 + var10), (double) var12, (double) (var13 + var10), 1.0D, 1.0D);
		par1Tessellator.addVertexWithUV((double) (var11 + var10), (double) var12, (double) (var13 - var10), 1.0D, 0.0D);
		par1Tessellator.addVertexWithUV((double) (var11 - var10), (double) var12, (double) (var13 - var10), 0.0D, 0.0D);
		par1Tessellator.draw();
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		++this.field_70576_a;

		if (this.field_70576_a == this.field_70578_aq) {
			this.setDead();
		}
	}

	public int getFXLayer() {
		return 3;
	}
}
