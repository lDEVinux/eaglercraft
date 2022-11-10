package net.minecraft.src;

import net.lax1dude.eaglercraft.TextureLocation;

public class RenderPig extends RenderLiving {
	public RenderPig(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}
	
	private static final TextureLocation saddle = new TextureLocation("/mob/saddle.png");

	protected int renderSaddledPig(EntityPig par1EntityPig, int par2, float par3) {
		if (par2 == 0 && par1EntityPig.getSaddled()) {
			saddle.bindTexture();
			return 1;
		} else {
			return -1;
		}
	}

	public void renderLivingPig(EntityPig par1EntityPig, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityPig, par2, par4, par6, par8, par9);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.renderSaddledPig((EntityPig) par1EntityLiving, par2, par3);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderLivingPig((EntityPig) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.renderLivingPig((EntityPig) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/pig.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
