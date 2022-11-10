package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class RenderTntMinecart extends RenderMinecart {
	protected void func_94146_a(EntityMinecartTNT par1EntityMinecartTNT, float par2, Block par3Block, int par4) {
		int var5 = par1EntityMinecartTNT.func_94104_d();

		if (var5 > -1 && (float) var5 - par2 + 1.0F < 10.0F) {
			float var6 = 1.0F - ((float) var5 - par2 + 1.0F) / 10.0F;

			if (var6 < 0.0F) {
				var6 = 0.0F;
			}

			if (var6 > 1.0F) {
				var6 = 1.0F;
			}

			var6 *= var6;
			var6 *= var6;
			float var7 = 1.0F + var6 * 0.3F;
			EaglerAdapter.glScalef(var7, var7, var7);
		}

		super.renderBlockInMinecart(par1EntityMinecartTNT, par2, par3Block, par4);

		if (var5 > -1 && var5 / 5 % 2 == 0) {
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_DST_ALPHA);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - ((float) var5 - par2 + 1.0F) / 100.0F) * 0.8F);
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.flipLightMatrix();
			this.field_94145_f.renderBlockAsItem(Block.tnt, 0, 1.0F);
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		}
	}

	/**
	 * Renders the block that is inside the minecart.
	 */
	protected void renderBlockInMinecart(EntityMinecart par1EntityMinecart, float par2, Block par3Block, int par4) {
		this.func_94146_a((EntityMinecartTNT) par1EntityMinecart, par2, par3Block, par4);
	}
}
