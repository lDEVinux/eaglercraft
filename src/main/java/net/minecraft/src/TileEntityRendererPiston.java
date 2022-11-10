package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class TileEntityRendererPiston extends TileEntitySpecialRenderer {
	/** instance of RenderBlocks used to draw the piston base and extension. */
	private RenderBlocks blockRenderer;
	
	private static final TextureLocation tex_terrain = new TextureLocation("/terrain.png");
	
	public void renderPiston(TileEntityPiston par1TileEntityPiston, double par2, double par4, double par6, float par8) {
		Block var9 = Block.blocksList[par1TileEntityPiston.getStoredBlockID()];

		if (var9 != null && par1TileEntityPiston.getProgress(par8) < 1.0F) {
			Tessellator var10 = Tessellator.instance;
			tex_terrain.bindTexture();
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
			RenderHelper.disableStandardItemLighting();
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);

			if (Minecraft.isAmbientOcclusionEnabled()) {
				EaglerAdapter.glShadeModel(EaglerAdapter.GL_SMOOTH);
			} else {
				EaglerAdapter.glShadeModel(EaglerAdapter.GL_FLAT);
			}

			var10.startDrawingQuads();
			var10.setTranslation((double) ((float) par2 - (float) par1TileEntityPiston.xCoord + par1TileEntityPiston.getOffsetX(par8)), (double) ((float) par4 - (float) par1TileEntityPiston.yCoord + par1TileEntityPiston.getOffsetY(par8)),
					(double) ((float) par6 - (float) par1TileEntityPiston.zCoord + par1TileEntityPiston.getOffsetZ(par8)));
			var10.setColorOpaque(1, 1, 1);

			if (var9 == Block.pistonExtension && par1TileEntityPiston.getProgress(par8) < 0.5F) {
				this.blockRenderer.renderPistonExtensionAllFaces(var9, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord, false);
			} else if (par1TileEntityPiston.shouldRenderHead() && !par1TileEntityPiston.isExtending()) {
				Block.pistonExtension.setHeadTexture(((BlockPistonBase) var9).getPistonExtensionTexture());
				this.blockRenderer.renderPistonExtensionAllFaces(Block.pistonExtension, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord, par1TileEntityPiston.getProgress(par8) < 0.5F);
				Block.pistonExtension.clearHeadTexture();
				var10.setTranslation((double) ((float) par2 - (float) par1TileEntityPiston.xCoord), (double) ((float) par4 - (float) par1TileEntityPiston.yCoord), (double) ((float) par6 - (float) par1TileEntityPiston.zCoord));
				this.blockRenderer.renderPistonBaseAllFaces(var9, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord);
			} else {
				this.blockRenderer.renderBlockAllFaces(var9, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord);
			}

			var10.setTranslation(0.0D, 0.0D, 0.0D);
			var10.draw();
			RenderHelper.enableStandardItemLighting();
		}
	}

	/**
	 * Called when the ingame world being rendered changes (e.g. on world -> nether
	 * travel) due to using one renderer per tile entity type, rather than instance
	 */
	public void onWorldChange(World par1World) {
		this.blockRenderer = new RenderBlocks(par1World);
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderPiston((TileEntityPiston) par1TileEntity, par2, par4, par6, par8);
	}
}
