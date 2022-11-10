package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderBat extends RenderLiving {
	/**
	 * not actually sure this is size, is not used as of now, but the model would be
	 * recreated if the value changed and it seems a good match for a bats size
	 */
	private int renderedBatSize;

	public RenderBat() {
		super(new ModelBat(), 0.25F);
		this.renderedBatSize = ((ModelBat) this.mainModel).getBatSize();
	}

	public void func_82443_a(EntityBat par1EntityBat, double par2, double par4, double par6, float par8, float par9) {
		int var10 = ((ModelBat) this.mainModel).getBatSize();

		if (var10 != this.renderedBatSize) {
			this.renderedBatSize = var10;
			this.mainModel = new ModelBat();
		}

		super.doRenderLiving(par1EntityBat, par2, par4, par6, par8, par9);
	}

	protected void func_82442_a(EntityBat par1EntityBat, float par2) {
		EaglerAdapter.glScalef(0.35F, 0.35F, 0.35F);
	}

	protected void func_82445_a(EntityBat par1EntityBat, double par2, double par4, double par6) {
		super.renderLivingAt(par1EntityBat, par2, par4, par6);
	}

	protected void func_82444_a(EntityBat par1EntityBat, float par2, float par3, float par4) {
		if (!par1EntityBat.getIsBatHanging()) {
			EaglerAdapter.glTranslatef(0.0F, MathHelper.cos(par2 * 0.3F) * 0.1F, 0.0F);
		} else {
			EaglerAdapter.glTranslatef(0.0F, -0.1F, 0.0F);
		}

		super.rotateCorpse(par1EntityBat, par2, par3, par4);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.func_82442_a((EntityBat) par1EntityLiving, par2);
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		this.func_82444_a((EntityBat) par1EntityLiving, par2, par3, par4);
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		this.func_82445_a((EntityBat) par1EntityLiving, par2, par4, par6);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.func_82443_a((EntityBat) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.func_82443_a((EntityBat) par1Entity, par2, par4, par6, par8, par9);
	}
	
	private static final TextureLocation entityTexture = new TextureLocation("/mob/bat.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
