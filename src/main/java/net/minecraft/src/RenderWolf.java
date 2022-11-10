package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderWolf extends RenderLiving {
	public RenderWolf(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}

	protected float getTailRotation(EntityWolf par1EntityWolf, float par2) {
		return par1EntityWolf.getTailRotation();
	}
	
	private static final TextureLocation tex_collar = new TextureLocation("/mob/wolf_collar.png");

	protected int func_82447_a(EntityWolf par1EntityWolf, int par2, float par3) {
		float var4;

		if (par2 == 0 && par1EntityWolf.getWolfShaking()) {
			var4 = par1EntityWolf.getBrightness(par3) * par1EntityWolf.getShadingWhileShaking(par3);
			this.bindTexture(par1EntityWolf);
			EaglerAdapter.glColor3f(var4, var4, var4);
			return 1;
		} else if (par2 == 1 && par1EntityWolf.isTamed()) {
			tex_collar.bindTexture();
			var4 = 1.0F;
			int var5 = par1EntityWolf.getCollarColor();
			EaglerAdapter.glColor3f(var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]);
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.func_82447_a((EntityWolf) par1EntityLiving, par2, par3);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
		return this.getTailRotation((EntityWolf) par1EntityLiving, par2);
	}

	private static final TextureLocation entityTexture0 = new TextureLocation("/mob/wolf.png");
	private static final TextureLocation entityTexture1 = new TextureLocation("/mob/wolf_angry.png");
	private static final TextureLocation entityTexture2 = new TextureLocation("/mob/wolf_tame.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		if(((EntityWolf)par1EntityLiving).isTamed()) {
			entityTexture2.bindTexture();
		}else if(((EntityWolf)par1EntityLiving).isAngry()) {
			entityTexture1.bindTexture();
		}else {
			entityTexture0.bindTexture();
		}
	}
}
