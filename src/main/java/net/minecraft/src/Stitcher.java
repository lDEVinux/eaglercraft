package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.lax1dude.eaglercraft.EaglerAdapter;


public class Stitcher {
	private final Set setStitchHolders;
	private final List stitchSlots;
	private int currentWidth;
	private int currentHeight;
	private final int maxWidth;
	private final int maxHeight;
	private final boolean forcePowerOf2;

	/** Max size (width or height) of a single tile */
	private final int maxTileDimension;
	private Texture atlasTexture;
	private final String textureName;

	public Stitcher(String par1Str, int par2, int par3, boolean par4) {
		this(par1Str, par2, par3, par4, 0);
	}

	public Stitcher(String par1, int par2, int par3, boolean par4, int par5) {
		this.setStitchHolders = new HashSet(256);
		this.stitchSlots = new ArrayList(256);
		this.currentWidth = 0;
		this.currentHeight = 0;
		this.textureName = par1;
		this.maxWidth = par2;
		this.maxHeight = par3;
		this.forcePowerOf2 = par4;
		this.maxTileDimension = par5;
	}

	public void addStitchHolder(StitchHolder par1StitchHolder) {
		if (this.maxTileDimension > 0) {
			par1StitchHolder.setNewDimension(this.maxTileDimension);
		}

		this.setStitchHolders.add(par1StitchHolder);
	}

	public Texture getTexture() {
		if (this.forcePowerOf2) {
			this.currentWidth = this.getCeilPowerOf2(this.currentWidth);
			this.currentHeight = this.getCeilPowerOf2(this.currentHeight);
		}

		this.atlasTexture = TextureManager.instance().createEmptyTexture(this.textureName, 1, this.currentWidth, this.currentHeight, EaglerAdapter.GL_CLAMP);
		this.atlasTexture.fillRect(this.atlasTexture.getTextureRect(), -65536);
		List var1 = this.getStichSlots();

		for (int var2 = 0; var2 < var1.size(); ++var2) {
			StitchSlot var3 = (StitchSlot) var1.get(var2);
			StitchHolder var4 = var3.getStitchHolder();
			this.atlasTexture.copyFrom(var3.getOriginX(), var3.getOriginY(), var4.func_98150_a(), var4.isRotated());
		}

		TextureManager.instance().registerTexture(this.textureName, this.atlasTexture);
		return this.atlasTexture;
	}

	public void doStitch() {
		StitchHolder[] var1 = (StitchHolder[]) this.setStitchHolders.toArray(new StitchHolder[this.setStitchHolders.size()]);
		Arrays.sort(var1);
		this.atlasTexture = null;

		for (int var2 = 0; var2 < var1.length; ++var2) {
			StitchHolder var3 = var1[var2];

			if (!this.allocateSlot(var3)) {
				throw new StitcherException(var3);
			}
		}
	}

	public List getStichSlots() {
		ArrayList var1 = new ArrayList();
		Iterator var2 = this.stitchSlots.iterator();

		while (var2.hasNext()) {
			StitchSlot var3 = (StitchSlot) var2.next();
			var3.getAllStitchSlots(var1);
		}

		return var1;
	}

	/**
	 * Returns power of 2 >= the specified value
	 */
	private int getCeilPowerOf2(int par1) {
		int var2 = par1 - 1;
		var2 |= var2 >> 1;
		var2 |= var2 >> 2;
		var2 |= var2 >> 4;
		var2 |= var2 >> 8;
		var2 |= var2 >> 16;
		return var2 + 1;
	}

	/**
	 * Attempts to find space for specified tile
	 */
	private boolean allocateSlot(StitchHolder par1StitchHolder) {
		for (int var2 = 0; var2 < this.stitchSlots.size(); ++var2) {
			if (((StitchSlot) this.stitchSlots.get(var2)).func_94182_a(par1StitchHolder)) {
				return true;
			}

			par1StitchHolder.rotate();

			if (((StitchSlot) this.stitchSlots.get(var2)).func_94182_a(par1StitchHolder)) {
				return true;
			}

			par1StitchHolder.rotate();
		}

		return this.expandAndAllocateSlot(par1StitchHolder);
	}

	/**
	 * Expand stitched texture in order to make space for specified tile
	 */
	private boolean expandAndAllocateSlot(StitchHolder par1StitchHolder) {
		int var2 = Math.min(par1StitchHolder.getHeight(), par1StitchHolder.getWidth());
		boolean var3 = this.currentWidth == 0 && this.currentHeight == 0;
		boolean var4;

		if (this.forcePowerOf2) {
			int var5 = this.getCeilPowerOf2(this.currentWidth);
			int var6 = this.getCeilPowerOf2(this.currentHeight);
			int var7 = this.getCeilPowerOf2(this.currentWidth + var2);
			int var8 = this.getCeilPowerOf2(this.currentHeight + var2);
			boolean var9 = var7 <= this.maxWidth;
			boolean var10 = var8 <= this.maxHeight;

			if (!var9 && !var10) {
				return false;
			}

			int var11 = Math.max(par1StitchHolder.getHeight(), par1StitchHolder.getWidth());

			if (var3 && !var9 && this.getCeilPowerOf2(this.currentHeight + var11) > this.maxHeight) {
				return false;
			}

			boolean var12 = var5 != var7;
			boolean var13 = var6 != var8;

			if (var12 ^ var13) {
				var4 = var12 && var9;
			} else {
				var4 = var9 && var5 <= var6;
			}
		} else {
			boolean var14 = this.currentWidth + var2 <= this.maxWidth;
			boolean var16 = this.currentHeight + var2 <= this.maxHeight;

			if (!var14 && !var16) {
				return false;
			}

			var4 = (var3 || this.currentWidth <= this.currentHeight) && var14;
		}

		StitchSlot var15;

		if (var4) {
			if (par1StitchHolder.getWidth() > par1StitchHolder.getHeight()) {
				par1StitchHolder.rotate();
			}

			if (this.currentHeight == 0) {
				this.currentHeight = par1StitchHolder.getHeight();
			}

			var15 = new StitchSlot(this.currentWidth, 0, par1StitchHolder.getWidth(), this.currentHeight);
			this.currentWidth += par1StitchHolder.getWidth();
		} else {
			var15 = new StitchSlot(0, this.currentHeight, this.currentWidth, par1StitchHolder.getHeight());
			this.currentHeight += par1StitchHolder.getHeight();
		}

		var15.func_94182_a(par1StitchHolder);
		this.stitchSlots.add(var15);
		return true;
	}
}
