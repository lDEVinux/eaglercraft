package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderTNTPrimed extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks();

	public RenderTNTPrimed() {
		this.shadowSize = 0.5F;
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");

	public void renderPrimedTNT(EntityTNTPrimed par1EntityTNTPrimed, double par2, double par4, double par6, float par8, float par9) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
		float var10;

		if ((float) par1EntityTNTPrimed.fuse - par9 + 1.0F < 10.0F) {
			var10 = 1.0F - ((float) par1EntityTNTPrimed.fuse - par9 + 1.0F) / 10.0F;

			if (var10 < 0.0F) {
				var10 = 0.0F;
			}

			if (var10 > 1.0F) {
				var10 = 1.0F;
			}

			var10 *= var10;
			var10 *= var10;
			float var11 = 1.0F + var10 * 0.3F;
			EaglerAdapter.glScalef(var11, var11, var11);
		}

		var10 = (1.0F - ((float) par1EntityTNTPrimed.fuse - par9 + 1.0F) / 100.0F) * 0.8F;
		terrain.bindTexture();
		EaglerAdapter.flipLightMatrix();
		this.blockRenderer.renderBlockAsItem(Block.tnt, 0, par1EntityTNTPrimed.getBrightness(par9));

		if (par1EntityTNTPrimed.fuse / 5 % 2 == 0) {
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_DST_ALPHA);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, var10);
			this.blockRenderer.renderBlockAsItem(Block.tnt, 0, 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		}
		EaglerAdapter.flipLightMatrix();

		EaglerAdapter.glPopMatrix();
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
		this.renderPrimedTNT((EntityTNTPrimed) par1Entity, par2, par4, par6, par8, par9);
	}
}
