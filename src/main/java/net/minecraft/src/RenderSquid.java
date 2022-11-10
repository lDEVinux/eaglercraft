package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderSquid extends RenderLiving {
	public RenderSquid(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}

	/**
	 * Renders the Living Squid.
	 */
	public void renderLivingSquid(EntitySquid par1EntitySquid, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntitySquid, par2, par4, par6, par8, par9);
	}

	/**
	 * Rotates the Squid's corpse.
	 */
	protected void rotateSquidsCorpse(EntitySquid par1EntitySquid, float par2, float par3, float par4) {
		float var5 = par1EntitySquid.prevSquidPitch + (par1EntitySquid.squidPitch - par1EntitySquid.prevSquidPitch) * par4;
		float var6 = par1EntitySquid.prevSquidYaw + (par1EntitySquid.squidYaw - par1EntitySquid.prevSquidYaw) * par4;
		EaglerAdapter.glTranslatef(0.0F, 0.5F, 0.0F);
		EaglerAdapter.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(var5, 1.0F, 0.0F, 0.0F);
		EaglerAdapter.glRotatef(var6, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glTranslatef(0.0F, -1.2F, 0.0F);
	}

	protected float handleRotationFloat(EntitySquid par1EntitySquid, float par2) {
		return par1EntitySquid.prevTentacleAngle + (par1EntitySquid.tentacleAngle - par1EntitySquid.prevTentacleAngle) * par2;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
		return this.handleRotationFloat((EntitySquid) par1EntityLiving, par2);
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		this.rotateSquidsCorpse((EntitySquid) par1EntityLiving, par2, par3, par4);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderLivingSquid((EntitySquid) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.renderLivingSquid((EntitySquid) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/squid.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
