package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;
import net.minecraft.client.Minecraft;

public class TextureManager {
	private static TextureManager instance;
	private int nextTextureID = 0;
	private final HashMap texturesMap = new HashMap();
	private final HashMap mapNameToId = new HashMap();

	public static void init() {
		instance = new TextureManager();
	}

	public static TextureManager instance() {
		return instance;
	}

	public int getNextTextureId() {
		return this.nextTextureID++;
	}

	public void registerTexture(String par1Str, Texture par2Texture) {
		this.mapNameToId.put(par1Str, Integer.valueOf(par2Texture.getTextureId()));

		if (!this.texturesMap.containsKey(Integer.valueOf(par2Texture.getTextureId()))) {
			this.texturesMap.put(Integer.valueOf(par2Texture.getTextureId()), par2Texture);
		}
	}

	public void registerTexture(Texture par1Texture) {
		if (this.texturesMap.containsValue(par1Texture)) {
			System.out.println("TextureManager.registerTexture called, but this texture has already been registered. ignoring.");
		} else {
			this.texturesMap.put(Integer.valueOf(par1Texture.getTextureId()), par1Texture);
		}
	}

	public Stitcher createStitcher(String par1Str) {
		int var2 = Minecraft.getGLMaximumTextureSize();
		return new Stitcher(par1Str, var2, var2, true);
	}

	public List createTexture(String par1Str) {
		ArrayList var2 = new ArrayList();
		ITexturePack var3 = Minecraft.getMinecraft().texturePackList.getSelectedTexturePack();
		byte[] b = var3.getResourceAsBytes("/" + par1Str);
		if(b != null) {
			//EaglerImage var9 = EaglerImage.loadImage(b);
			EaglerImage var9 = EaglerAdapter.loadPNG(b);
			int var10 = var9.w;
			int var11 = var9.h;
			String var12 = this.getBasename(par1Str);
		
			if (this.hasAnimationTxt(par1Str, var3)) {
				int var13 = var10;
				int var15 = var11 / var10;
		
				for (int var16 = 0; var16 < var15; ++var16) {
					Texture var17 = this.makeTexture(var12, 2, var13, var13, EaglerAdapter.GL_CLAMP, EaglerAdapter.GL_RGBA, EaglerAdapter.GL_NEAREST, EaglerAdapter.GL_NEAREST, false, var9.getSubImage(0, var13 * var16, var13, var13));
					var2.add(var17);
				}
			} else if (var10 == var11) {
				var2.add(this.makeTexture(var12, 2, var10, var10, EaglerAdapter.GL_CLAMP, EaglerAdapter.GL_RGBA, EaglerAdapter.GL_NEAREST, EaglerAdapter.GL_NEAREST, false, var9));
			} else {
				System.out.println("TextureManager.createTexture: Skipping " + par1Str + " because of broken aspect ratio and not animation");
			}
		}

		return var2;
	}

	/**
	 * Strips directory and file extension from the specified path, returning only
	 * the filename
	 */
	private String getBasename(String par1Str) {
		String name = par1Str.substring(par1Str.lastIndexOf('/') + 1);
		return name.substring(0, name.lastIndexOf(46));
	}

	/**
	 * Returns true if specified texture pack contains animation data for the
	 * specified texture file
	 */
	private boolean hasAnimationTxt(String par1Str, ITexturePack par2ITexturePack) {
		String var3 = "/" + par1Str.substring(0, par1Str.lastIndexOf(46)) + ".txt";
		boolean var4 = par2ITexturePack.func_98138_b("/" + par1Str, false);
		return Minecraft.getMinecraft().texturePackList.getSelectedTexturePack().func_98138_b(var3, !var4);
	}

	public Texture makeTexture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, boolean par9, EaglerImage par10BufferedImage) {
		Texture var11 = new Texture(par1Str, par2, par3, par4, par5, par6, par7, par8, par10BufferedImage);
		this.registerTexture(var11);
		return var11;
	}

	public Texture createEmptyTexture(String par1Str, int par2, int par3, int par4, int par5) {
		return this.makeTexture(par1Str, par2, par3, par4, EaglerAdapter.GL_CLAMP, par5, EaglerAdapter.GL_NEAREST, EaglerAdapter.GL_NEAREST, false, (EaglerImage) null);
	}
}
