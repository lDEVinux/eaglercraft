package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.minecraft.client.Minecraft;

class GuiBeaconButton extends GuiButton {
	/** Texture for this button. */
	private final TextureLocation buttonTexture;
	private final int field_82257_l;
	private final int field_82258_m;
	private boolean field_82256_n;

	protected GuiBeaconButton(int par1, int par2, int par3, String par4Str, int par5, int par6) {
		super(par1, par2, par3, 22, 22, "");
		this.buttonTexture = new TextureLocation(par4Str);
		this.field_82257_l = par5;
		this.field_82258_m = par6;
	}
	
	private static final TextureLocation tex_beacon = new TextureLocation("/gui/beacon.png");

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (this.drawButton) {
			tex_beacon.bindTexture();
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			short var4 = 219;
			int var5 = 0;

			if (!this.enabled) {
				var5 += this.width * 2;
			} else if (this.field_82256_n) {
				var5 += this.width * 1;
			} else if (this.field_82253_i) {
				var5 += this.width * 3;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, var5, var4, this.width, this.height);
			
			this.buttonTexture.bindTexture();
			this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_82257_l, this.field_82258_m, 18, 18);
		}
	}

	public boolean func_82255_b() {
		return this.field_82256_n;
	}

	public void func_82254_b(boolean par1) {
		this.field_82256_n = par1;
	}
}
