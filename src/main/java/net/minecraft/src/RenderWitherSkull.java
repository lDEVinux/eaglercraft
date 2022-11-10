package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderWitherSkull extends Render {
	/** The Skeleton's head model. */
	ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();

	private float func_82400_a(float par1, float par2, float par3) {
		float var4;

		for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F) {
			;
		}

		while (var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		return par1 + par3 * var4;
	}
	
	private static final TextureLocation tex_wither = new TextureLocation("/mob/wither.png");
	private static final TextureLocation tex_wither_invul = new TextureLocation("/mob/wither_invul.png");

	public void func_82399_a(EntityWitherSkull par1EntityWitherSkull, double par2, double par4, double par6, float par8, float par9) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
		float var10 = this.func_82400_a(par1EntityWitherSkull.prevRotationYaw, par1EntityWitherSkull.rotationYaw, par9);
		float var11 = par1EntityWitherSkull.prevRotationPitch + (par1EntityWitherSkull.rotationPitch - par1EntityWitherSkull.prevRotationPitch) * par9;
		EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
		float var12 = 0.0625F;
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glScalef(-1.0F, -1.0F, 1.0F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);

		if (par1EntityWitherSkull.isInvulnerable()) {
			tex_wither_invul.bindTexture();
		} else {
			tex_wither.bindTexture();
		}

		this.skeletonHeadModel.render(par1EntityWitherSkull, 0.0F, 0.0F, 0.0F, var10, var11, var12);
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
		this.func_82399_a((EntityWitherSkull) par1Entity, par2, par4, par6, par8, par9);
	}
}
