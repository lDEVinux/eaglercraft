package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.minecraft.client.Minecraft;

class GuiButtonNextPage extends GuiButton {
	/**
	 * True for pointing right (next page), false for pointing left (previous page).
	 */
	private final boolean nextPage;

	public GuiButtonNextPage(int par1, int par2, int par3, boolean par4) {
		super(par1, par2, par3, 23, 13, "");
		this.nextPage = par4;
	}
	
	private static final TextureLocation tex = new TextureLocation("/gui/book.png");
	
	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (this.drawButton) {
			boolean var4 = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tex.bindTexture();
			int var5 = 0;
			int var6 = 192;

			if (var4) {
				var5 += 23;
			}

			if (!this.nextPage) {
				var6 += 13;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, var5, var6, 23, 13);
		}
	}
}
