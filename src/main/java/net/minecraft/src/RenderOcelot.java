package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderOcelot extends RenderLiving {
	public RenderOcelot(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}

	public void renderLivingOcelot(EntityOcelot par1EntityOcelot, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityOcelot, par2, par4, par6, par8, par9);
	}

	/**
	 * Pre-Renders the Ocelot.
	 */
	protected void preRenderOcelot(EntityOcelot par1EntityOcelot, float par2) {
		super.preRenderCallback(par1EntityOcelot, par2);

		if (par1EntityOcelot.isTamed()) {
			EaglerAdapter.glScalef(0.8F, 0.8F, 0.8F);
		}
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.preRenderOcelot((EntityOcelot) par1EntityLiving, par2);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderLivingOcelot((EntityOcelot) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.renderLivingOcelot((EntityOcelot) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture0 = new TextureLocation("/mob/ozelot.png");
	private static final TextureLocation entityTexture1 = new TextureLocation("/mob/cat_black.png");
	private static final TextureLocation entityTexture2 = new TextureLocation("/mob/cat_red.png");
	private static final TextureLocation entityTexture3 = new TextureLocation("/mob/cat_siamese.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		switch(((EntityOcelot)par1EntityLiving).getTameSkin()) {
		case 0:
		default:
			entityTexture0.bindTexture();
			break;
		case 1:
			entityTexture1.bindTexture();
			break;
		case 2:
			entityTexture2.bindTexture();
			break;
		case 3:
			entityTexture3.bindTexture();
			break;
		}
	}
}
