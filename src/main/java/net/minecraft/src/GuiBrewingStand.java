package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class GuiBrewingStand extends GuiContainer {
	private TileEntityBrewingStand brewingStand;

	public GuiBrewingStand(InventoryPlayer par1InventoryPlayer, TileEntityBrewingStand par2TileEntityBrewingStand) {
		super(new ContainerBrewingStand(par1InventoryPlayer, par2TileEntityBrewingStand));
		this.brewingStand = par2TileEntityBrewingStand;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String var3 = this.brewingStand.isInvNameLocalized() ? this.brewingStand.getInvName() : StatCollector.translateToLocal(this.brewingStand.getInvName());
		this.fontRenderer.drawString(var3, this.xSize / 2 - this.fontRenderer.getStringWidth(var3) / 2, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	private static final TextureLocation tex = new TextureLocation("/gui/alchemy.png");

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tex.bindTexture();
		int var4 = (this.width - this.xSize) / 2;
		int var5 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
		int var6 = this.brewingStand.getBrewTime();

		if (var6 > 0) {
			int var7 = (int) (28.0F * (1.0F - (float) var6 / 400.0F));

			if (var7 > 0) {
				this.drawTexturedModalRect(var4 + 97, var5 + 16, 176, 0, 9, var7);
			}

			int var8 = var6 / 2 % 7;

			switch (var8) {
			case 0:
				var7 = 29;
				break;

			case 1:
				var7 = 24;
				break;

			case 2:
				var7 = 20;
				break;

			case 3:
				var7 = 16;
				break;

			case 4:
				var7 = 11;
				break;

			case 5:
				var7 = 6;
				break;

			case 6:
				var7 = 0;
			}

			if (var7 > 0) {
				this.drawTexturedModalRect(var4 + 65, var5 + 14 + 29 - var7, 185, 29 - var7, 12, var7);
			}
		}
	}
}
