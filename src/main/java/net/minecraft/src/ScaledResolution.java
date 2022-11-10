package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;
	private double scaledWidthD;
	private double scaledHeightD;
	private int scaleFactor;

	public ScaledResolution(GameSettings par1GameSettings, int par2, int par3) {
		float f = EaglerAdapter.getContentScaling();
		this.scaledWidth = (int)(par2 / f);
		this.scaledHeight = (int)(par3 / f);
		this.scaleFactor = 1;
		int var4 = par1GameSettings.guiScale;

		if (var4 == 0) {
			var4 = 1000;
		}

		while (this.scaleFactor < var4 && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
			++this.scaleFactor;
		}

		this.scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
		this.scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
		this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
		this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
	}

	public int getScaledWidth() {
		return this.scaledWidth;
	}

	public int getScaledHeight() {
		return this.scaledHeight;
	}

	public double getScaledWidth_double() {
		return this.scaledWidthD;
	}

	public double getScaledHeight_double() {
		return this.scaledHeightD;
	}

	public int getScaleFactor() {
		return this.scaleFactor;
	}
}
