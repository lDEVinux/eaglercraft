package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderMagmaCube extends RenderLiving {
	private int field_77120_a;

	public RenderMagmaCube() {
		super(new ModelMagmaCube(), 0.25F);
		this.field_77120_a = ((ModelMagmaCube) this.mainModel).func_78107_a();
	}

	public void renderMagmaCube(EntityMagmaCube par1EntityMagmaCube, double par2, double par4, double par6, float par8, float par9) {
		int var10 = ((ModelMagmaCube) this.mainModel).func_78107_a();

		if (var10 != this.field_77120_a) {
			this.field_77120_a = var10;
			this.mainModel = new ModelMagmaCube();
			System.out.println("Loaded new lava slime model");
		}

		super.doRenderLiving(par1EntityMagmaCube, par2, par4, par6, par8, par9);
	}

	protected void scaleMagmaCube(EntityMagmaCube par1EntityMagmaCube, float par2) {
		int var3 = par1EntityMagmaCube.getSlimeSize();
		float var4 = (par1EntityMagmaCube.field_70812_c + (par1EntityMagmaCube.field_70811_b - par1EntityMagmaCube.field_70812_c) * par2) / ((float) var3 * 0.5F + 1.0F);
		float var5 = 1.0F / (var4 + 1.0F);
		float var6 = (float) var3;
		EaglerAdapter.glScalef(var5 * var6, 1.0F / var5 * var6, var5 * var6);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.scaleMagmaCube((EntityMagmaCube) par1EntityLiving, par2);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderMagmaCube((EntityMagmaCube) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.renderMagmaCube((EntityMagmaCube) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/lava.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
