package net.minecraft.src;



public class Rect2i {
	private int rectX;
	private int rectY;
	private int rectWidth;
	private int rectHeight;

	public Rect2i(int par1, int par2, int par3, int par4) {
		this.rectX = par1;
		this.rectY = par2;
		this.rectWidth = par3;
		this.rectHeight = par4;
	}

	public Rect2i intersection(Rect2i par1Rect2i) {
		int var2 = this.rectX;
		int var3 = this.rectY;
		int var4 = this.rectX + this.rectWidth;
		int var5 = this.rectY + this.rectHeight;
		int var6 = par1Rect2i.getRectX();
		int var7 = par1Rect2i.getRectY();
		int var8 = var6 + par1Rect2i.getRectWidth();
		int var9 = var7 + par1Rect2i.getRectHeight();
		this.rectX = Math.max(var2, var6);
		this.rectY = Math.max(var3, var7);
		this.rectWidth = Math.max(0, Math.min(var4, var8) - this.rectX);
		this.rectHeight = Math.max(0, Math.min(var5, var9) - this.rectY);
		return this;
	}

	public int getRectX() {
		return this.rectX;
	}

	public int getRectY() {
		return this.rectY;
	}

	public int getRectWidth() {
		return this.rectWidth;
	}

	public int getRectHeight() {
		return this.rectHeight;
	}
}
