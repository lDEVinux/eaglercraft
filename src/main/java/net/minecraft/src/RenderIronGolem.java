package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderIronGolem extends RenderLiving {
	/** Iron Golem's Model. */
	private ModelIronGolem ironGolemModel;

	public RenderIronGolem() {
		super(new ModelIronGolem(), 0.5F);
		this.ironGolemModel = (ModelIronGolem) this.mainModel;
	}

	/**
	 * Renders the Iron Golem.
	 */
	public void doRenderIronGolem(EntityIronGolem par1EntityIronGolem, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityIronGolem, par2, par4, par6, par8, par9);
	}

	/**
	 * Rotates Iron Golem corpse.
	 */
	protected void rotateIronGolemCorpse(EntityIronGolem par1EntityIronGolem, float par2, float par3, float par4) {
		super.rotateCorpse(par1EntityIronGolem, par2, par3, par4);

		if ((double) par1EntityIronGolem.limbYaw >= 0.01D) {
			float var5 = 13.0F;
			float var6 = par1EntityIronGolem.limbSwing - par1EntityIronGolem.limbYaw * (1.0F - par4) + 6.0F;
			float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
			EaglerAdapter.glRotatef(6.5F * var7, 0.0F, 0.0F, 1.0F);
		}
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");

	/**
	 * Renders Iron Golem Equipped items.
	 */
	protected void renderIronGolemEquippedItems(EntityIronGolem par1EntityIronGolem, float par2) {
		super.renderEquippedItems(par1EntityIronGolem, par2);

		if (par1EntityIronGolem.getHoldRoseTick() != 0) {
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glRotatef(5.0F + 180.0F * this.ironGolemModel.ironGolemRightArm.rotateAngleX / (float) Math.PI, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glTranslatef(-0.6875F, 1.25F, -0.9375F);
			EaglerAdapter.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			float var3 = 0.8F;
			EaglerAdapter.glScalef(var3, -var3, var3);
			int var4 = par1EntityIronGolem.getBrightnessForRender(par2);
			int var5 = var4 % 65536;
			int var6 = var4 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var5 / 1.0F, (float) var6 / 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			terrain.bindTexture();
			EaglerAdapter.flipLightMatrix();
			this.renderBlocks.renderBlockAsItem(Block.plantRed, 0, 1.0F);
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		}
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderIronGolemEquippedItems((EntityIronGolem) par1EntityLiving, par2);
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		this.rotateIronGolemCorpse((EntityIronGolem) par1EntityLiving, par2, par3, par4);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderIronGolem((EntityIronGolem) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.doRenderIronGolem((EntityIronGolem) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/villager_golem.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
