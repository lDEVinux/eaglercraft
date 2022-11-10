package net.minecraft.src;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.TextureTerrainMap;
import net.minecraft.client.Minecraft;

public class RenderEngine {
	private HashMap textureMap = new HashMap();

	/** Texture contents map (key: texture name, value: int[] contents) */
	private HashMap textureContentsMap = new HashMap();

	/** A mapping from GL texture names (integers) to BufferedImage instances */
	private IntHashMap textureNameToImageMap = new IntHashMap();

	/** Stores the image data for the texture. */
	private IntBuffer imageData;

	/** A mapping from image URLs to ThreadDownloadImageData instances */
	private Map urlToImageDataMap = new HashMap();

	/** Reference to the GameSettings object */
	private GameSettings options;

	/** Texture pack */
	private TexturePackList texturePack;

	/** Missing texture image */
	private EaglerImage missingTextureImage;
	private final TextureTerrainMap textureMapBlocks;
	private final TextureMap textureMapItems;
	private int boundTexture;

	public RenderEngine(TexturePackList par1TexturePackList, GameSettings par2GameSettings) {
		this.imageData = GLAllocation.createDirectIntBuffer(16777216 >> 2);
		this.texturePack = par1TexturePackList;
		this.options = par2GameSettings;
		int[] missingTex = new int[256];
		for(int i = 0; i < 256; ++i) {
			missingTex[i] = ((i / 16 + (i % 16)) % 2 == 0) ? 0xffff00ff : 0xff000000;
		}
		this.missingTextureImage = new EaglerImage(missingTex, 16, 16, true);
		this.textureMapBlocks = new TextureTerrainMap(1024, "terrain", "textures/blocks/", this.missingTextureImage);
		this.textureMapItems = new TextureMap(1, "items", "textures/items/", this.missingTextureImage);
	}

	public int[] getTextureContents(String par1Str) {
		ITexturePack var2 = this.texturePack.getSelectedTexturePack();
		int[] var3 = (int[]) this.textureContentsMap.get(par1Str);

		if (var3 != null) {
			return var3;
		} else {
			byte[] var7 = var2.getResourceAsBytes(par1Str);
			int[] var4;

			if (var7 == null) {
				var4 = this.missingTextureImage.data;
			} else {
				//var4 = EaglerImage.loadImage(var7).data;
				var4 = EaglerAdapter.loadPNG(var7).data;
			}

			this.textureContentsMap.put(par1Str, var4);
			return var4;
		}
	}


	public void bindTexture(String par1Str) {
		this.bindTexture(this.getTexture(par1Str));
	}

	public void bindTexture(int par1) {
		if (par1 != this.boundTexture) {
			EaglerAdapter.glBindTexture(EaglerAdapter.GL_TEXTURE_2D, par1);
			this.boundTexture = par1;
		}
	}

	public void resetBoundTexture() {
		this.boundTexture = -1;
	}

	public int getTexture(String par1Str) {
		if (par1Str.equals("/terrain.png")) {
			return this.textureMapBlocks.texture;
		} else if (par1Str.equals("/gui/items.png")) {
			this.textureMapItems.getTexture().bindTexture(0);
			return this.textureMapItems.getTexture().getGlTextureId();
		} else {
			Integer var2 = (Integer) this.textureMap.get(par1Str);

			if (var2 != null) {
				return var2.intValue();
			} else {
				String var8 = par1Str;

				try {
					int var3 = GLAllocation.generateTextureNames();
					boolean var9 = par1Str.startsWith("%blur%");

					if (var9) {
						par1Str = par1Str.substring(6);
					}

					boolean var5 = par1Str.startsWith("%clamp%");

					if (var5) {
						par1Str = par1Str.substring(7);
					}

					byte[] var6 = this.texturePack.getSelectedTexturePack().getResourceAsBytes(par1Str);

					if (var6 == null) {
						this.setupTextureExt(this.missingTextureImage, var3, var9, var5);
					} else {
						this.setupTextureExt(EaglerImage.loadImage(var6), var3, var9, var5);
					}

					this.textureMap.put(var8, Integer.valueOf(var3));
					return var3;
				} catch (Exception var7) {
					var7.printStackTrace();
					int var4 = GLAllocation.generateTextureNames();
					this.setupTexture(this.missingTextureImage, var4);
					this.textureMap.put(par1Str, Integer.valueOf(var4));
					return var4;
				}
			}
		}
	}

	/**
	 * Copy the supplied image onto a newly-allocated OpenGL texture, returning the
	 * allocated texture name
	 */
	public int allocateAndSetupTexture(EaglerImage par1BufferedImage) {
		int var2 = GLAllocation.generateTextureNames();
		this.setupTexture(par1BufferedImage, var2);
		this.textureNameToImageMap.addKey(var2, par1BufferedImage);
		return var2;
	}

	/**
	 * Copy the supplied image onto the specified OpenGL texture
	 */
	public void setupTexture(EaglerImage par1BufferedImage, int par2) {
		this.setupTextureExt(par1BufferedImage, par2, false, false);
	}
	
	public int makeViewportTexture(int w, int h) {
		int t = EaglerAdapter.glGenTextures();
		this.bindTexture(t);
		this.imageData.position(0).limit(w * h);
		EaglerAdapter.glTexImage2D_2(EaglerAdapter.GL_TEXTURE_2D, 0, EaglerAdapter.GL_RGBA, w, h, 0, EaglerAdapter.GL_BGRA, EaglerAdapter.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
		return t;
	}

	public void setupTextureExt(EaglerImage par1BufferedImage, int par2, boolean par3, boolean par4) {
		this.bindTexture(par2);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);

		if (par3) {
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_LINEAR);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_LINEAR);
		}

		if (par4) {
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_CLAMP);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_CLAMP);
		} else {
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_REPEAT);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_REPEAT);
		}

		int var5 = par1BufferedImage.w;
		int var6 = par1BufferedImage.h;
		int[] var7 = par1BufferedImage.data;

		if (this.options != null && this.options.anaglyph) {
			var7 = this.colorToAnaglyph(var7);
		}

		this.imageData.clear();
		this.imageData.put(var7);
		this.imageData.position(0).limit(var7.length);
		EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 0, EaglerAdapter.GL_RGBA, var5, var6, 0, EaglerAdapter.GL_BGRA, EaglerAdapter.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
	}
	
	public int setupTextureRaw(byte[] data, int w, int h) {
		int e = GLAllocation.generateTextureNames();
		this.bindTexture(e);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_CLAMP);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_CLAMP);
		this.imageData.clear();
		int[] upload = new int[data.length / 4];
		for(int i = 0; i < upload.length; ++i) {
			upload[i] = ((data[i*4+3] & 255) << 24) | ((data[i*4+2] & 255) << 16) | ((data[i*4+1] & 255) << 8) | (data[i*4] & 255);
		}
		this.imageData.put(upload).flip();
		EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 0, EaglerAdapter.GL_RGBA, w, h, 0, EaglerAdapter.GL_BGRA, EaglerAdapter.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
		return e;
	}

	private int[] colorToAnaglyph(int[] par1ArrayOfInteger) {
		int[] var2 = new int[par1ArrayOfInteger.length];

		for (int var3 = 0; var3 < par1ArrayOfInteger.length; ++var3) {
			int var4 = par1ArrayOfInteger[var3] >> 24 & 255;
			int var5 = par1ArrayOfInteger[var3] >> 16 & 255;
			int var6 = par1ArrayOfInteger[var3] >> 8 & 255;
			int var7 = par1ArrayOfInteger[var3] & 255;
			int var8 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
			int var9 = (var5 * 30 + var6 * 70) / 100;
			int var10 = (var5 * 30 + var7 * 70) / 100;
			var2[var3] = var4 << 24 | var8 << 16 | var9 << 8 | var10;
		}

		return var2;
	}

	public void createTextureFromBytes(int[] par1ArrayOfInteger, int par2, int par3, int par4) {
		this.bindTexture(par4);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_REPEAT);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_REPEAT);

		if (this.options != null && this.options.anaglyph) {
			par1ArrayOfInteger = this.colorToAnaglyph(par1ArrayOfInteger);
		}

		this.imageData.clear();
		this.imageData.put(par1ArrayOfInteger);
		this.imageData.position(0).limit(par1ArrayOfInteger.length);
		EaglerAdapter.glTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, 0, 0, 0, par2, par3, EaglerAdapter.GL_BGRA, EaglerAdapter.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
	}

	/**
	 * Deletes a single GL texture
	 */
	public void deleteTexture(int par1) {
		this.textureNameToImageMap.removeObject(par1);
		EaglerAdapter.glDeleteTextures(par1);
	}

	public void updateDynamicTextures() {
		this.textureMapBlocks.updateAnimations();
		this.textureMapItems.updateAnimations();
	}

	/**
	 * Call setupTexture on all currently-loaded textures again to account for
	 * changes in rendering options
	 */
	public void refreshTextures() {
		TextureLocation.freeTextures();
		ITexturePack var1 = this.texturePack.getSelectedTexturePack();
		this.refreshTextureMaps();
		Iterator var2 = this.textureNameToImageMap.getKeySet().iterator();
		EaglerImage var4;

		while (var2.hasNext()) {
			int var3 = ((Integer) var2.next()).intValue();
			var4 = (EaglerImage) this.textureNameToImageMap.lookup(var3);
			this.setupTexture(var4, var3);
		}

		var2 = this.textureMap.keySet().iterator();
		String var11;

		while (var2.hasNext()) {
			var11 = (String) var2.next();

			try {
				int var12 = ((Integer) this.textureMap.get(var11)).intValue();
				boolean var6 = var11.startsWith("%blur%");

				if (var6) {
					var11 = var11.substring(6);
				}

				boolean var7 = var11.startsWith("%clamp%");

				if (var7) {
					var11 = var11.substring(7);
				}
				
				//byte[] b = var1.getResourceAsBytes(var11); //what the fuck
				//if(b != null) {
					EaglerImage var5 = EaglerAdapter.loadPNG(var1.getResourceAsBytes(var11));
					if(var5 == null) {
						throw new IOException("Could not load PNG");
					}
					this.setupTextureExt(var5, var12, var6, var7);
				//}else {
				//	System.err.println("could not reload: "+var11);
				//}
			} catch (IOException var9) {
				var9.printStackTrace();
			}
		}

		var2 = this.textureContentsMap.keySet().iterator();

		while (var2.hasNext()) {
			var11 = (String) var2.next();

			try {
				var4 = EaglerAdapter.loadPNG(var1.getResourceAsBytes(var11));
				if(var4 == null) {
					throw new IOException("Could not load PNG");
				}
				System.arraycopy(var4.data, 0, (int[]) this.textureContentsMap.get(var11), 0, var4.data.length);
			} catch (IOException var8) {
				var8.printStackTrace();
			}
		}

		Minecraft.getMinecraft().fontRenderer.readFontData();
		Minecraft.getMinecraft().standardGalacticFontRenderer.readFontData();
	}

	/**
	 * Returns a BufferedImage read off the provided input stream. Args: inputStream
	 */
	private EaglerImage readTextureImage(byte[] par1InputStream) throws IOException {
		return null;//EaglerImage.loadImage(par1InputStream);
		//return EaglerAdapter.loadPNG(par1InputStream);
	}

	public void refreshTextureMaps() {
		this.textureMapBlocks.refreshTextures();
		this.textureMapItems.refreshTextures();
	}

	public Icon getMissingIcon(int par1) {
		switch (par1) {
		case 0:
			return this.textureMapBlocks.getMissingIcon();

		case 1:
		default:
			return this.textureMapItems.getMissingIcon();
		}
	}
}
