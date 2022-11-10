package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderEnderman extends RenderLiving {
	/** The model of the enderman */
	private ModelEnderman endermanModel;
	private EaglercraftRandom rnd = new EaglercraftRandom();

	public RenderEnderman() {
		super(new ModelEnderman(), 0.5F);
		this.endermanModel = (ModelEnderman) super.mainModel;
		this.setRenderPassModel(this.endermanModel);
	}

	/**
	 * Renders the enderman
	 */
	public void renderEnderman(EntityEnderman par1EntityEnderman, double par2, double par4, double par6, float par8, float par9) {
		this.endermanModel.isCarrying = par1EntityEnderman.getCarried() > 0;
		this.endermanModel.isAttacking = par1EntityEnderman.isScreaming();

		if (par1EntityEnderman.isScreaming()) {
			double var10 = 0.02D;
			par2 += this.rnd.nextGaussian() * var10;
			par6 += this.rnd.nextGaussian() * var10;
		}

		super.doRenderLiving(par1EntityEnderman, par2, par4, par6, par8, par9);
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");

	/**
	 * Render the block an enderman is carrying
	 */
	protected void renderCarrying(EntityEnderman par1EntityEnderman, float par2) {
		super.renderEquippedItems(par1EntityEnderman, par2);

		if (par1EntityEnderman.getCarried() > 0) {
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glPushMatrix();
			float var3 = 0.5F;
			EaglerAdapter.glTranslatef(0.0F, 0.6875F, -0.75F);
			var3 *= 1.0F;
			EaglerAdapter.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glScalef(-var3, -var3, var3);
			int var4 = par1EntityEnderman.getBrightnessForRender(par2);
			int var5 = var4 % 65536;
			int var6 = var4 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var5 / 1.0F, (float) var6 / 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			terrain.bindTexture();
			EaglerAdapter.flipLightMatrix();
			this.renderBlocks.renderBlockAsItem(Block.blocksList[par1EntityEnderman.getCarried()], par1EntityEnderman.getCarryingData(), 1.0F);
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		}
	}
	
	public static final TextureLocation tex_eyes = new TextureLocation("/mob/enderman_eyes.png");

	/**
	 * Render the endermans eyes
	 */
	protected int renderEyes(EntityEnderman par1EntityEnderman, int par2, float par3) {
		if (par2 != 0) {
			return -1;
		} else {
			tex_eyes.bindTexture();
			float var4 = 1.0F;
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);

			if (par1EntityEnderman.isInvisible()) {
				EaglerAdapter.glDepthMask(false);
			} else {
				EaglerAdapter.glDepthMask(true);
			}

			char var5 = 61680;
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glColor4f(2.3F, 2.3F, 2.3F, var4);
			return 1;
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.renderEyes((EntityEnderman) par1EntityLiving, par2, par3);
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderCarrying((EntityEnderman) par1EntityLiving, par2);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderEnderman((EntityEnderman) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.renderEnderman((EntityEnderman) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/enderman.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
