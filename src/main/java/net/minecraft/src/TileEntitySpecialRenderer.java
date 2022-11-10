package net.minecraft.src;

public abstract class TileEntitySpecialRenderer {
	/**
	 * The TileEntityRenderer instance associated with this
	 * TileEntitySpecialRenderer
	 */
	protected TileEntityRenderer tileEntityRenderer;

	public abstract void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8);

	/**
	 * Binds a texture to the renderEngine given a filename from the JAR.
	 */
	protected void bindTextureByName(String par1Str) {
		RenderEngine var2 = this.tileEntityRenderer.renderEngine;

		if (var2 != null) {
			var2.bindTexture(par1Str);
		}
	}

	/**
	 * Associate a TileEntityRenderer with this TileEntitySpecialRenderer
	 */
	public void setTileEntityRenderer(TileEntityRenderer par1TileEntityRenderer) {
		this.tileEntityRenderer = par1TileEntityRenderer;
	}

	/**
	 * Called when the ingame world being rendered changes (e.g. on world -> nether
	 * travel) due to using one renderer per tile entity type, rather than instance
	 */
	public void onWorldChange(World par1World) {
	}

	public FontRenderer getFontRenderer() {
		return this.tileEntityRenderer.getFontRenderer();
	}
}
