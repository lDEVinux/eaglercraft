package net.minecraft.src;

import java.nio.FloatBuffer;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class RenderEndPortal extends TileEntitySpecialRenderer {
	FloatBuffer field_76908_a = GLAllocation.createDirectFloatBuffer(16);
	
	private static final TextureLocation tex_tunnel = new TextureLocation("/misc/tunnel.png");
	private static final TextureLocation tex_particlefield = new TextureLocation("/misc/particlefield.png");

	/**
	 * Renders the End Portal.
	 */
	public void renderEndPortalTileEntity(TileEntityEndPortal par1TileEntityEndPortal, double par2, double par4, double par6, float par8) {
		float var9 = (float) this.tileEntityRenderer.playerX;
		float var10 = (float) this.tileEntityRenderer.playerY;
		float var11 = (float) this.tileEntityRenderer.playerZ;
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		EaglercraftRandom var12 = new EaglercraftRandom(31100L);
		float var13 = 0.75F;

		for (int var14 = 0; var14 < 16; ++var14) {
			EaglerAdapter.glPushMatrix();
			float var15 = (float) (16 - var14);
			float var16 = 0.0625F;
			float var17 = 1.0F / (var15 + 1.0F);

			if (var14 == 0) {
				tex_tunnel.bindTexture();
				var17 = 0.1F;
				var15 = 65.0F;
				var16 = 0.125F;
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			}

			if (var14 == 1) {
				tex_particlefield.bindTexture();
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE);
				var16 = 0.5F;
			}

			float var18 = (float) (-(par4 + (double) var13));
			float var19 = var18 + ActiveRenderInfo.objectY;
			float var20 = var18 + var15 + ActiveRenderInfo.objectY;
			float var21 = var19 / var20;
			var21 += (float) (par4 + (double) var13);
			EaglerAdapter.glTranslatef(var9, var21, var11);
			EaglerAdapter.glTexGeni(EaglerAdapter.GL_S, EaglerAdapter.GL_TEXTURE_GEN_MODE, EaglerAdapter.GL_OBJECT_LINEAR);
			EaglerAdapter.glTexGeni(EaglerAdapter.GL_T, EaglerAdapter.GL_TEXTURE_GEN_MODE, EaglerAdapter.GL_OBJECT_LINEAR);
			EaglerAdapter.glTexGeni(EaglerAdapter.GL_R, EaglerAdapter.GL_TEXTURE_GEN_MODE, EaglerAdapter.GL_OBJECT_LINEAR);
			EaglerAdapter.glTexGeni(EaglerAdapter.GL_Q, EaglerAdapter.GL_TEXTURE_GEN_MODE, EaglerAdapter.GL_EYE_LINEAR);
			EaglerAdapter.glTexGen(EaglerAdapter.GL_S, EaglerAdapter.GL_OBJECT_PLANE, this.func_76907_a(1.0F, 0.0F, 0.0F, 0.0F));
			EaglerAdapter.glTexGen(EaglerAdapter.GL_T, EaglerAdapter.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 1.0F, 0.0F));
			EaglerAdapter.glTexGen(EaglerAdapter.GL_R, EaglerAdapter.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 0.0F, 1.0F));
			EaglerAdapter.glTexGen(EaglerAdapter.GL_Q, EaglerAdapter.GL_EYE_PLANE, this.func_76907_a(0.0F, 1.0F, 0.0F, 0.0F));
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_GEN_S);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_GEN_T);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_GEN_R);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_GEN_Q);
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glLoadIdentity();
			EaglerAdapter.glTranslatef(0.0F, (float) (Minecraft.getSystemTime() % 400000L) / 400000.0F, 0.0F);
			EaglerAdapter.glScalef(var16, var16, var16);
			EaglerAdapter.glTranslatef(0.5F, 0.5F, 0.0F);
			EaglerAdapter.glRotatef((float) (var14 * var14 * 4321 + var14 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glTranslatef(-0.5F, -0.5F, 0.0F);
			EaglerAdapter.glTranslatef(-var9, -var11, -var10);
			var19 = var18 + ActiveRenderInfo.objectY;
			EaglerAdapter.glTranslatef(ActiveRenderInfo.objectX * var15 / var19, ActiveRenderInfo.objectZ * var15 / var19, -var10);
			Tessellator var24 = Tessellator.instance;
			var24.startDrawingQuads();
			var21 = var12.nextFloat() * 0.5F + 0.1F;
			float var22 = var12.nextFloat() * 0.5F + 0.4F;
			float var23 = var12.nextFloat() * 0.5F + 0.5F;

			if (var14 == 0) {
				var23 = 1.0F;
				var22 = 1.0F;
				var21 = 1.0F;
			}

			var24.setColorRGBA_F(var21 * var17, var22 * var17, var23 * var17, 1.0F);
			var24.addVertex(par2, par4 + (double) var13, par6);
			var24.addVertex(par2, par4 + (double) var13, par6 + 1.0D);
			var24.addVertex(par2 + 1.0D, par4 + (double) var13, par6 + 1.0D);
			var24.addVertex(par2 + 1.0D, par4 + (double) var13, par6);
			var24.draw();
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		}

		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_GEN_S);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_GEN_T);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_GEN_R);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_GEN_Q);
		EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
	}

	private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4) {
		this.field_76908_a.clear();
		this.field_76908_a.put(par1).put(par2).put(par3).put(par4);
		this.field_76908_a.flip();
		return this.field_76908_a;
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderEndPortalTileEntity((TileEntityEndPortal) par1TileEntity, par2, par4, par6, par8);
	}
}
