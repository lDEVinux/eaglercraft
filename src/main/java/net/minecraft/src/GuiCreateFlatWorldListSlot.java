package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.adapter.Tessellator;

class GuiCreateFlatWorldListSlot extends GuiSlot {
	public int field_82454_a;

	final GuiCreateFlatWorld createFlatWorldGui;

	public GuiCreateFlatWorldListSlot(GuiCreateFlatWorld par1GuiCreateFlatWorld) {
		super(par1GuiCreateFlatWorld.mc, par1GuiCreateFlatWorld.width, par1GuiCreateFlatWorld.height, 43,
				par1GuiCreateFlatWorld.height - 60, 24);
		this.createFlatWorldGui = par1GuiCreateFlatWorld;
		this.field_82454_a = -1;
	}

	private void func_82452_a(int par1, int par2, ItemStack par3ItemStack) {
		this.func_82451_d(par1 + 1, par2 + 1);
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);

		if (par3ItemStack != null) {
			RenderHelper.enableGUIStandardItemLighting();
			EaglerAdapter.flipLightMatrix();
			GuiCreateFlatWorld.getRenderItem().renderItemIntoGUI(this.createFlatWorldGui.fontRenderer,
					this.createFlatWorldGui.mc.renderEngine, par3ItemStack, par1 + 2, par2 + 2);
			EaglerAdapter.flipLightMatrix();
			RenderHelper.disableStandardItemLighting();
		}

		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
	}

	private void func_82451_d(int par1, int par2) {
		this.func_82450_b(par1, par2, 0, 0);
	}

	private void func_82450_b(int par1, int par2, int par3, int par4) {
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.createFlatWorldGui.mc.renderEngine.bindTexture("/gui/slot.png");
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + 18), (double) this.createFlatWorldGui.zLevel,
				(double) ((float) (par3 + 0) * 0.0078125F), (double) ((float) (par4 + 18) * 0.0078125F));
		var9.addVertexWithUV((double) (par1 + 18), (double) (par2 + 18), (double) this.createFlatWorldGui.zLevel,
				(double) ((float) (par3 + 18) * 0.0078125F), (double) ((float) (par4 + 18) * 0.0078125F));
		var9.addVertexWithUV((double) (par1 + 18), (double) (par2 + 0), (double) this.createFlatWorldGui.zLevel,
				(double) ((float) (par3 + 18) * 0.0078125F), (double) ((float) (par4 + 0) * 0.0078125F));
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), (double) this.createFlatWorldGui.zLevel,
				(double) ((float) (par3 + 0) * 0.0078125F), (double) ((float) (par4 + 0) * 0.0078125F));
		var9.draw();
	}

	/**
	 * Gets the size of the current slot list.
	 */
	protected int getSize() {
		return GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers().size();
	}

	/**
	 * the element in the slot that was clicked, boolean for wether it was double
	 * clicked or not
	 */
	protected void elementClicked(int par1, boolean par2) {
		this.field_82454_a = par1;
		this.createFlatWorldGui.func_82270_g();
	}

	/**
	 * returns true if the element passed in is currently selected
	 */
	protected boolean isSelected(int par1) {
		return par1 == this.field_82454_a;
	}

	protected void drawBackground() {
	}

	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator) {
		FlatLayerInfo var6 = (FlatLayerInfo) GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers()
				.get(GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers().size() - par1 - 1);
		ItemStack var7 = var6.getFillBlock() == 0 ? null
				: new ItemStack(var6.getFillBlock(), 1, var6.getFillBlockMeta());
		String var8 = var7 == null ? "Air" : Item.itemsList[var6.getFillBlock()].func_77653_i(var7);
		this.func_82452_a(par2, par3, var7);
		this.createFlatWorldGui.fontRenderer.drawString(var8, par2 + 18 + 5, par3 + 3, 16777215);
		String var9;

		if (par1 == 0) {
			var9 = StatCollector.translateToLocalFormatted("createWorld.customize.flat.layer.top",
					new Object[] { Integer.valueOf(var6.getLayerCount()) });
		} else if (par1 == GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers().size() - 1) {
			var9 = StatCollector.translateToLocalFormatted("createWorld.customize.flat.layer.bottom",
					new Object[] { Integer.valueOf(var6.getLayerCount()) });
		} else {
			var9 = StatCollector.translateToLocalFormatted("createWorld.customize.flat.layer",
					new Object[] { Integer.valueOf(var6.getLayerCount()) });
		}

		this.createFlatWorldGui.fontRenderer.drawString(var9,
				par2 + 2 + 213 - this.createFlatWorldGui.fontRenderer.getStringWidth(var9), par3 + 3, 16777215);
	}

	protected int getScrollBarX() {
		return this.createFlatWorldGui.width - 70;
	}
}
