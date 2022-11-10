package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderSlime extends RenderLiving {
	private ModelBase scaleAmount;

	public RenderSlime(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.scaleAmount = par2ModelBase;
	}

	/**
	 * Determines whether Slime Render should pass or not.
	 */
	protected int shouldSlimeRenderPass(EntitySlime par1EntitySlime, int par2, float par3) {
		if (par1EntitySlime.isInvisible()) {
			return 0;
		} else if (par2 == 0) {
			this.setRenderPassModel(this.scaleAmount);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			return 1;
		} else {
			if (par2 == 1) {
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			return -1;
		}
	}

	/**
	 * sets the scale for the slime based on getSlimeSize in EntitySlime
	 */
	protected void scaleSlime(EntitySlime par1EntitySlime, float par2) {
		float var3 = (float) par1EntitySlime.getSlimeSize();
		float var4 = (par1EntitySlime.field_70812_c + (par1EntitySlime.field_70811_b - par1EntitySlime.field_70812_c) * par2) / (var3 * 0.5F + 1.0F);
		float var5 = 1.0F / (var4 + 1.0F);
		EaglerAdapter.glScalef(var5 * var3, 1.0F / var5 * var3, var5 * var3);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.scaleSlime((EntitySlime) par1EntityLiving, par2);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.shouldSlimeRenderPass((EntitySlime) par1EntityLiving, par2, par3);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/slime.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
