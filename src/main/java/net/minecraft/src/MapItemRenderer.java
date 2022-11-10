package net.minecraft.src;

import java.util.Iterator;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class MapItemRenderer {
	private int[] intArray = new int[16384];
	private int bufferedImage;
	private GameSettings gameSettings;
	private FontRenderer fontRenderer;

	public MapItemRenderer(FontRenderer par1FontRenderer, GameSettings par2GameSettings, RenderEngine par3RenderEngine) {
		this.gameSettings = par2GameSettings;
		this.fontRenderer = par1FontRenderer;
		this.bufferedImage = par3RenderEngine.allocateAndSetupTexture(new EaglerImage(128, 128, false));

		for (int var4 = 0; var4 < 16384; ++var4) {
			this.intArray[var4] = 0;
		}
	}
	
	private static final TextureLocation mapicons = new TextureLocation("/misc/mapicons.png");

	public void renderMap(EntityPlayer par1EntityPlayer, RenderEngine par2RenderEngine, MapData par3MapData) {
		float texX1 = 0.0f;
		float texX2 = 1.0f;
		float texY1 = 0.0f;
		float texY2 = 1.0f;
		boolean isVideoOrImageMode = EaglerAdapter.isVideoSupported() && par3MapData.enableVideoPlayback;
		boolean isVideoMode = isVideoOrImageMode && EaglerAdapter.isVideoLoaded();
		boolean isImageMode = isVideoOrImageMode && EaglerAdapter.isImageLoaded();
		if(isVideoMode) {
			EaglerAdapter.glEnable(EaglerAdapter.EAG_SWAP_RB);
			EaglerAdapter.updateVideoTexture();
			EaglerAdapter.bindVideoTexture();
			texX1 = par3MapData.videoX1;
			texY1 = par3MapData.videoY1;
			texX2 = par3MapData.videoX2;
			texY2 = par3MapData.videoY2;
		}else if(isImageMode) {
			EaglerAdapter.glEnable(EaglerAdapter.EAG_SWAP_RB);
			EaglerAdapter.updateImageTexture();
			EaglerAdapter.bindImageTexture();
			texX1 = par3MapData.videoX1;
			texY1 = par3MapData.videoY1;
			texX2 = par3MapData.videoX2;
			texY2 = par3MapData.videoY2;
		}else {
			if(par3MapData.enableAyunami) {
				System.arraycopy(par3MapData.ayunamiPixels, 0, intArray, 0, intArray.length);
			}else {
				for (int var4 = 0; var4 < 16384; ++var4) {
					byte var5 = par3MapData.colors[var4];

					if (var5 / 4 == 0) {
						this.intArray[var4] = (var4 + var4 / 128 & 1) * 8 + 16 << 24;
					} else {
						int var6 = MapColor.mapColorArray[var5 / 4].colorValue;
						int var7 = var5 & 3;
						short var8 = 220;

						if (var7 == 2) {
							var8 = 255;
						}

						if (var7 == 0) {
							var8 = 180;
						}

						int var9 = (var6 >> 16 & 255) * var8 / 255;
						int var10 = (var6 >> 8 & 255) * var8 / 255;
						int var11 = (var6 & 255) * var8 / 255;

						if (this.gameSettings.anaglyph) {
							int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
							int var13 = (var9 * 30 + var10 * 70) / 100;
							int var14 = (var9 * 30 + var11 * 70) / 100;
							var9 = var12;
							var10 = var13;
							var11 = var14;
						}

						this.intArray[var4] = -16777216 | var9 << 16 | var10 << 8 | var11;
					}
				}
			}
			par2RenderEngine.createTextureFromBytes(this.intArray, 128, 128, this.bufferedImage);
		}

		byte var15 = 0;
		byte var16 = 0;
		Tessellator var17 = Tessellator.instance;
		float var18 = 0.0F;
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		var17.startDrawingQuads();
		var17.addVertexWithUV((double) ((float) (var15 + 0) + var18), (double) ((float) (var16 + 128) - var18), -0.009999999776482582D, texX1, texY2);
		var17.addVertexWithUV((double) ((float) (var15 + 128) - var18), (double) ((float) (var16 + 128) - var18), -0.009999999776482582D, texX2, texY2);
		var17.addVertexWithUV((double) ((float) (var15 + 128) - var18), (double) ((float) (var16 + 0) + var18), -0.009999999776482582D, texX2, texY1);
		var17.addVertexWithUV((double) ((float) (var15 + 0) + var18), (double) ((float) (var16 + 0) + var18), -0.009999999776482582D, texX1, texY1);
		var17.draw();
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		par2RenderEngine.resetBoundTexture();

		if(isVideoMode || isImageMode) {
			EaglerAdapter.glDisable(EaglerAdapter.EAG_SWAP_RB);
		}

		if(!par3MapData.enableAyunami && !(isVideoMode || isImageMode)) {
			mapicons.bindTexture();
			int var19 = 0;

			for (Iterator var20 = par3MapData.playersVisibleOnMap.values().iterator(); var20.hasNext(); ++var19) {
				MapCoord var21 = (MapCoord) var20.next();
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef((float) var15 + (float) var21.centerX / 2.0F + 64.0F, (float) var16 + (float) var21.centerZ / 2.0F + 64.0F, -0.02F);
				EaglerAdapter.glRotatef((float) (var21.iconRotation * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
				EaglerAdapter.glScalef(4.0F, 4.0F, 3.0F);
				EaglerAdapter.glTranslatef(-0.125F, 0.125F, 0.0F);
				float var22 = (float) (var21.iconSize % 4 + 0) / 4.0F;
				float var23 = (float) (var21.iconSize / 4 + 0) / 4.0F;
				float var24 = (float) (var21.iconSize % 4 + 1) / 4.0F;
				float var25 = (float) (var21.iconSize / 4 + 1) / 4.0F;
				var17.startDrawingQuads();
				var17.addVertexWithUV(-1.0D, 1.0D, (double) ((float) var19 * 0.001F), (double) var22, (double) var23);
				var17.addVertexWithUV(1.0D, 1.0D, (double) ((float) var19 * 0.001F), (double) var24, (double) var23);
				var17.addVertexWithUV(1.0D, -1.0D, (double) ((float) var19 * 0.001F), (double) var24, (double) var25);
				var17.addVertexWithUV(-1.0D, -1.0D, (double) ((float) var19 * 0.001F), (double) var22, (double) var25);
				var17.draw();
				EaglerAdapter.glPopMatrix();
			}
		}
	}
}
