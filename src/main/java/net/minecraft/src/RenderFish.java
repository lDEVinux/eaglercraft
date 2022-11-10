package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class RenderFish extends Render {
	
	private static final TextureLocation tex = new TextureLocation("/particles.png");
	
	/**
	 * Actually renders the fishing line and hook
	 */
	public void doRenderFishHook(EntityFishHook par1EntityFishHook, double par2, double par4, double par6, float par8, float par9) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glScalef(0.5F, 0.5F, 0.5F);
		byte var10 = 1;
		byte var11 = 2;
		tex.bindTexture();
		Tessellator var12 = Tessellator.instance;
		float var13 = (float) (var10 * 8 + 0 + 0.15f) / 128.0F;
		float var14 = (float) (var10 * 8 + 8 - 0.15f) / 128.0F;
		float var15 = (float) (var11 * 8 + 0 + 0.15f) / 128.0F;
		float var16 = (float) (var11 * 8 + 8 - 0.15f) / 128.0F;
		float var17 = 1.0F;
		float var18 = 0.5F;
		float var19 = 0.5F;
		EaglerAdapter.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		var12.startDrawingQuads();
		var12.setNormal(0.0F, -1.0F, 0.0F);
		var12.addVertexWithUV((double) (0.0F - var18), (double) (0.0F - var19), 0.0D, (double) var13, (double) var16);
		var12.addVertexWithUV((double) (var17 - var18), (double) (0.0F - var19), 0.0D, (double) var14, (double) var16);
		var12.addVertexWithUV((double) (var17 - var18), (double) (1.0F - var19), 0.0D, (double) var14, (double) var15);
		var12.addVertexWithUV((double) (0.0F - var18), (double) (1.0F - var19), 0.0D, (double) var13, (double) var15);
		var12.draw();
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glPopMatrix();

		if (par1EntityFishHook.angler != null) {
			float var20 = par1EntityFishHook.angler.getSwingProgress(par9);
			float var21 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float) Math.PI);
			Vec3 var22 = par1EntityFishHook.worldObj.getWorldVec3Pool().getVecFromPool(-0.5D, 0.03D, 0.8D);
			var22.rotateAroundX(-(par1EntityFishHook.angler.prevRotationPitch + (par1EntityFishHook.angler.rotationPitch - par1EntityFishHook.angler.prevRotationPitch) * par9) * (float) Math.PI / 180.0F);
			var22.rotateAroundY(-(par1EntityFishHook.angler.prevRotationYaw + (par1EntityFishHook.angler.rotationYaw - par1EntityFishHook.angler.prevRotationYaw) * par9) * (float) Math.PI / 180.0F);
			var22.rotateAroundY(var21 * 0.5F);
			var22.rotateAroundX(-var21 * 0.7F);
			double var23 = par1EntityFishHook.angler.prevPosX + (par1EntityFishHook.angler.posX - par1EntityFishHook.angler.prevPosX) * (double) par9 + var22.xCoord;
			double var25 = par1EntityFishHook.angler.prevPosY + (par1EntityFishHook.angler.posY - par1EntityFishHook.angler.prevPosY) * (double) par9 + var22.yCoord;
			double var27 = par1EntityFishHook.angler.prevPosZ + (par1EntityFishHook.angler.posZ - par1EntityFishHook.angler.prevPosZ) * (double) par9 + var22.zCoord;
			double var29 = par1EntityFishHook.angler != Minecraft.getMinecraft().thePlayer ? (double) par1EntityFishHook.angler.getEyeHeight() : 0.0D;

			if (this.renderManager.options.thirdPersonView > 0 || par1EntityFishHook.angler != Minecraft.getMinecraft().thePlayer) {
				float var31 = (par1EntityFishHook.angler.prevRenderYawOffset + (par1EntityFishHook.angler.renderYawOffset - par1EntityFishHook.angler.prevRenderYawOffset) * par9) * (float) Math.PI / 180.0F;
				double var32 = (double) MathHelper.sin(var31);
				double var34 = (double) MathHelper.cos(var31);
				var23 = par1EntityFishHook.angler.prevPosX + (par1EntityFishHook.angler.posX - par1EntityFishHook.angler.prevPosX) * (double) par9 - var34 * 0.35D - var32 * 0.85D;
				var25 = par1EntityFishHook.angler.prevPosY + var29 + (par1EntityFishHook.angler.posY - par1EntityFishHook.angler.prevPosY) * (double) par9 - 0.45D;
				var27 = par1EntityFishHook.angler.prevPosZ + (par1EntityFishHook.angler.posZ - par1EntityFishHook.angler.prevPosZ) * (double) par9 - var32 * 0.35D + var34 * 0.85D;
			}

			double var46 = par1EntityFishHook.prevPosX + (par1EntityFishHook.posX - par1EntityFishHook.prevPosX) * (double) par9;
			double var33 = par1EntityFishHook.prevPosY + (par1EntityFishHook.posY - par1EntityFishHook.prevPosY) * (double) par9 + 0.25D;
			double var35 = par1EntityFishHook.prevPosZ + (par1EntityFishHook.posZ - par1EntityFishHook.prevPosZ) * (double) par9;
			double var37 = (double) ((float) (var23 - var46));
			double var39 = (double) ((float) (var25 - var33));
			double var41 = (double) ((float) (var27 - var35));
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
			var12.startDrawing(EaglerAdapter.GL_LINE_STRIP);
			var12.setColorOpaque_I(0);
			byte var43 = 16;

			for (int var44 = 0; var44 <= var43; ++var44) {
				float var45 = (float) var44 / (float) var43;
				var12.addVertex(par2 + var37 * (double) var45, par4 + var39 * (double) (var45 * var45 + var45) * 0.5D + 0.25D, par6 + var41 * (double) var45);
			}

			var12.draw();
			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		}
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
		this.doRenderFishHook((EntityFishHook) par1Entity, par2, par4, par6, par8, par9);
	}
}
