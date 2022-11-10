package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpider par1EntitySpider) {
		return 180.0F;
	}
	
	private static final TextureLocation tex_eyes = new TextureLocation("/mob/spider_eyes.png");

	/**
	 * Sets the spider's glowing eyes
	 */
	protected int setSpiderEyeBrightness(EntitySpider par1EntitySpider, int par2, float par3) {
		if (par2 != 0) {
			return -1;
		} else {
			tex_eyes.bindTexture();
			float var4 = 1.0F;
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE);

			if (par1EntitySpider.isInvisible()) {
				EaglerAdapter.glDepthMask(false);
			} else {
				EaglerAdapter.glDepthMask(true);
			}

			char var5 = 61680;
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return 1;
		}
	}

	protected void scaleSpider(EntitySpider par1EntitySpider, float par2) {
		float var3 = par1EntitySpider.spiderScaleAmount();
		EaglerAdapter.glScalef(var3, var3, var3);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.scaleSpider((EntitySpider) par1EntityLiving, par2);
	}

	protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
		return this.setSpiderDeathMaxRotation((EntitySpider) par1EntityLiving);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.setSpiderEyeBrightness((EntitySpider) par1EntityLiving, par2, par3);
	}

	private static final TextureLocation entityTexture0 = new TextureLocation("/mob/spider.png");
	private static final TextureLocation entityTexture1 = new TextureLocation("/mob/cavespider.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		if(par1EntityLiving instanceof EntityCaveSpider) {
			entityTexture1.bindTexture();
		}else {
			entityTexture0.bindTexture();
		}
	}
}
