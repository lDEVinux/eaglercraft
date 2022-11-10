package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class GuiFurnace extends GuiContainer {
	private TileEntityFurnace furnaceInventory;

	public GuiFurnace(InventoryPlayer par1InventoryPlayer, TileEntityFurnace par2TileEntityFurnace) {
		super(new ContainerFurnace(par1InventoryPlayer, par2TileEntityFurnace));
		this.furnaceInventory = par2TileEntityFurnace;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String var3 = this.furnaceInventory.isInvNameLocalized() ? this.furnaceInventory.getInvName() : StatCollector.translateToLocal(this.furnaceInventory.getInvName());
		this.fontRenderer.drawString(var3, this.xSize / 2 - this.fontRenderer.getStringWidth(var3) / 2, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	private static final TextureLocation tex = new TextureLocation("/gui/furnace.png");

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tex.bindTexture();
		int var4 = (this.width - this.xSize) / 2;
		int var5 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
		int var6;

		if (this.furnaceInventory.isBurning()) {
			var6 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(var4 + 56, var5 + 36 + 12 - var6, 176, 12 - var6, 14, var6 + 2);
		}

		var6 = this.furnaceInventory.getCookProgressScaled(24);
		this.drawTexturedModalRect(var4 + 79, var5 + 34, 176, 14, var6 + 1, 16);
	}
}
