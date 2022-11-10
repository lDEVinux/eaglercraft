package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;
import net.minecraft.client.Minecraft;

public class TextureMap implements IconRegister {
	/** 0 = terrain.png, 1 = items.png */
	private final int textureType;
	private final String textureName;
	private final String basePath;
	private final String textureExt;
	private final HashMap mapTexturesStiched = new HashMap();
	private EaglerImage missingImage;
	private TextureStitched missingTextureStiched;
	private Texture atlasTexture;
	private final List listTextureStiched = new ArrayList();
	private final Map textureStichedMap = new HashMap();

	public TextureMap(int par1, String par2, String par3Str, EaglerImage par4BufferedImage) {
		this.textureType = par1;
		this.textureName = par2;
		this.basePath = par3Str;
		this.textureExt = ".png";
		this.missingImage = par4BufferedImage;
	}

	public void refreshTextures() {
		this.textureStichedMap.clear();
		int var2;
		int var3;

		if (this.textureType == 0) {
			Block[] var1 = Block.blocksList;
			var2 = var1.length;

			for (var3 = 0; var3 < var2; ++var3) {
				Block var4 = var1[var3];

				if (var4 != null) {
					var4.registerIcons(this);
				}
			}

			Minecraft.getMinecraft().renderGlobal.registerDestroyBlockIcons(this);
			RenderManager.instance.updateIcons(this);
		}

		Item[] var19 = Item.itemsList;
		var2 = var19.length;

		for (var3 = 0; var3 < var2; ++var3) {
			Item var23 = var19[var3];

			if (var23 != null && var23.getSpriteNumber() == this.textureType) {
				var23.registerIcons(this);
			}
		}

		HashMap var20 = new HashMap();
		Stitcher var21 = TextureManager.instance().createStitcher(this.textureName);
		this.mapTexturesStiched.clear();
		this.listTextureStiched.clear();
		Texture var22 = TextureManager.instance().makeTexture("missingno", 2, this.missingImage.w, this.missingImage.h, EaglerAdapter.GL_CLAMP, EaglerAdapter.GL_RGBA, EaglerAdapter.GL_NEAREST, EaglerAdapter.GL_NEAREST, false, this.missingImage);
		StitchHolder var24 = new StitchHolder(var22);
		var21.addStitchHolder(var24);
		var20.put(var24, Arrays.asList(new Texture[] { var22 }));
		Iterator var5 = this.textureStichedMap.keySet().iterator();

		while (var5.hasNext()) {
			String var6 = (String) var5.next();
			String var7 = this.basePath + var6 + this.textureExt;
			List var8 = TextureManager.instance().createTexture(var7);

			if (!var8.isEmpty()) {
				StitchHolder var9 = new StitchHolder((Texture) var8.get(0));
				var21.addStitchHolder(var9);
				var20.put(var9, var8);
			}
		}

		try {
			var21.doStitch();
		} catch (StitcherException var18) {
			throw var18;
		}

		this.atlasTexture = var21.getTexture();
		var5 = var21.getStichSlots().iterator();

		while (var5.hasNext()) {
			StitchSlot var25 = (StitchSlot) var5.next();
			StitchHolder var27 = var25.getStitchHolder();
			Texture var28 = var27.func_98150_a();
			String var29 = var28.getTextureName();
			List var10 = (List) var20.get(var27);
			TextureStitched var11 = (TextureStitched) this.textureStichedMap.get(var29);
			boolean var12 = false;

			if (var11 == null) {
				var12 = true;
				var11 = TextureStitched.makeTextureStitched(var29);

				if (!var29.equals("missingno")) {
					System.out.println("Couldn\'t find premade icon for " + var29 + " doing " + this.textureName);
				}
			}

			var11.init(this.atlasTexture, var10, var25.getOriginX(), var25.getOriginY(), var27.func_98150_a().getWidth(), var27.func_98150_a().getHeight(), var27.isRotated());
			this.mapTexturesStiched.put(var29, var11);

			if (!var12) {
				this.textureStichedMap.remove(var29);
			}

			if (var10.size() > 1) {
				this.listTextureStiched.add(var11);
				String var13 = this.basePath + var29 + ".txt";
				ITexturePack var14 = Minecraft.getMinecraft().texturePackList.getSelectedTexturePack();
				boolean var15 = !var14.func_98138_b("/" + this.basePath + var29 + ".png", false);
				
				if(var14.func_98138_b("/" + var13, false)) {
					System.out.println("Found animation info for: " + var13);
					var11.readAnimationInfo("/" + var13);
				}
			}
		}

		this.missingTextureStiched = (TextureStitched) this.mapTexturesStiched.get("missingno");
		var5 = this.textureStichedMap.values().iterator();

		while (var5.hasNext()) {
			TextureStitched var26 = (TextureStitched) var5.next();
			var26.copyFrom(this.missingTextureStiched);
		}

		this.atlasTexture.writeImage("debug.stitched_" + this.textureName + ".png");
		this.atlasTexture.uploadTexture();
	}

	public void updateAnimations() {
		Iterator var1 = this.listTextureStiched.iterator();

		while (var1.hasNext()) {
			TextureStitched var2 = (TextureStitched) var1.next();
			var2.updateAnimation();
		}
	}

	public Texture getTexture() {
		return this.atlasTexture;
	}

	public Icon registerIcon(String par1Str, int w) {
		if(w != 1) {
			System.err.println("Error, texture '" + par1Str + "' was registered with size " + w + ", the item texure map only supports size 1 (16px)");
			return missingTextureStiched;
		}else {
			if (par1Str == null) {
				(new RuntimeException("Don\'t register null!")).printStackTrace();
			}
	
			TextureStitched var2 = (TextureStitched) this.textureStichedMap.get(par1Str);
	
			if (var2 == null) {
				var2 = TextureStitched.makeTextureStitched(par1Str);
				this.textureStichedMap.put(par1Str, var2);
			}
	
			return var2;
		}
	}

	public Icon getMissingIcon() {
		return this.missingTextureStiched;
	}
}
