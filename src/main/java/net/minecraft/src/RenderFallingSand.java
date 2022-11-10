package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class RenderFallingSand extends Render {
	private RenderBlocks sandRenderBlocks = new RenderBlocks();

	public RenderFallingSand() {
		this.shadowSize = 0.5F;
	}
	
	private static final TextureLocation tex = new TextureLocation("/terrain.png");

	/**
	 * The actual render method that is used in doRender
	 */
	public void doRenderFallingSand(EntityFallingSand par1EntityFallingSand, double par2, double par4, double par6, float par8, float par9) {
		World var10 = par1EntityFallingSand.getWorld();
		Block var11 = Block.blocksList[par1EntityFallingSand.blockID];

		if (var10.getBlockId(MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY), MathHelper.floor_double(par1EntityFallingSand.posZ)) != par1EntityFallingSand.blockID) {
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
			tex.bindTexture();
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
			Tessellator var12;

			if (var11 instanceof BlockAnvil && var11.getRenderType() == 35) {
				this.sandRenderBlocks.blockAccess = var10;
				var12 = Tessellator.instance;
				var12.startDrawingQuads();
				var12.setTranslation((double) ((float) (-MathHelper.floor_double(par1EntityFallingSand.posX)) - 0.5F), (double) ((float) (-MathHelper.floor_double(par1EntityFallingSand.posY)) - 0.5F),
						(double) ((float) (-MathHelper.floor_double(par1EntityFallingSand.posZ)) - 0.5F));
				this.sandRenderBlocks.renderBlockAnvilMetadata((BlockAnvil) var11, MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY),
						MathHelper.floor_double(par1EntityFallingSand.posZ), par1EntityFallingSand.metadata);
				var12.setTranslation(0.0D, 0.0D, 0.0D);
				var12.draw();
			} else if (var11.getRenderType() == 27) {
				this.sandRenderBlocks.blockAccess = var10;
				var12 = Tessellator.instance;
				var12.startDrawingQuads();
				var12.setTranslation((double) ((float) (-MathHelper.floor_double(par1EntityFallingSand.posX)) - 0.5F), (double) ((float) (-MathHelper.floor_double(par1EntityFallingSand.posY)) - 0.5F),
						(double) ((float) (-MathHelper.floor_double(par1EntityFallingSand.posZ)) - 0.5F));
				this.sandRenderBlocks.renderBlockDragonEgg((BlockDragonEgg) var11, MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY),
						MathHelper.floor_double(par1EntityFallingSand.posZ));
				var12.setTranslation(0.0D, 0.0D, 0.0D);
				var12.draw();
			} else if (var11 != null) {
				this.sandRenderBlocks.setRenderBoundsFromBlock(var11);
				this.sandRenderBlocks.renderBlockSandFalling(var11, var10, MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY), MathHelper.floor_double(par1EntityFallingSand.posZ),
						par1EntityFallingSand.metadata);
			}

			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glPopMatrix();
		}
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker function
	 * which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void
	 * doRender(T entity, double d, double d1, double d2, float f, float f1). But
	 * JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderFallingSand((EntityFallingSand) par1Entity, par2, par4, par6, par8, par9);
	}
}
