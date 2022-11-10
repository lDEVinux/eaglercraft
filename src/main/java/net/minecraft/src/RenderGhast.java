package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderGhast extends RenderLiving {
	public RenderGhast() {
		super(new ModelGhast(), 0.5F);
	}

	/**
	 * Pre-Renders the Ghast.
	 */
	protected void preRenderGhast(EntityGhast par1EntityGhast, float par2) {
		float var4 = ((float) par1EntityGhast.prevAttackCounter + (float) (par1EntityGhast.attackCounter - par1EntityGhast.prevAttackCounter) * par2) / 20.0F;

		if (var4 < 0.0F) {
			var4 = 0.0F;
		}

		var4 = 1.0F / (var4 * var4 * var4 * var4 * var4 * 2.0F + 1.0F);
		float var5 = (8.0F + var4) / 2.0F;
		float var6 = (8.0F + 1.0F / var4) / 2.0F;
		EaglerAdapter.glScalef(var6, var5, var6);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.preRenderGhast((EntityGhast) par1EntityLiving, par2);
	}

	private static final TextureLocation entityTexture0 = new TextureLocation("/mob/ghast.png");
	private static final TextureLocation entityTexture1 = new TextureLocation("/mob/ghast_fire.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		if(par1EntityLiving.getDataWatcher().getWatchableObjectByte(16) == (byte)1) {
			entityTexture1.bindTexture();
		}else {
			entityTexture0.bindTexture();
		}
	}
}
