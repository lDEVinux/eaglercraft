package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderCreeper extends RenderLiving {
	/** The creeper model. */
	private ModelBase creeperModel = new ModelCreeper(2.0F);

	public RenderCreeper() {
		super(new ModelCreeper(), 0.5F);
	}

	/**
	 * Updates creeper scale in prerender callback
	 */
	protected void updateCreeperScale(EntityCreeper par1EntityCreeper, float par2) {
		float var4 = par1EntityCreeper.getCreeperFlashIntensity(par2);
		float var5 = 1.0F + MathHelper.sin(var4 * 100.0F) * var4 * 0.01F;

		if (var4 < 0.0F) {
			var4 = 0.0F;
		}

		if (var4 > 1.0F) {
			var4 = 1.0F;
		}

		var4 *= var4;
		var4 *= var4;
		float var6 = (1.0F + var4 * 0.4F) * var5;
		float var7 = (1.0F + var4 * 0.1F) / var5;
		EaglerAdapter.glScalef(var6, var7, var6);
	}

	/**
	 * Updates color multiplier based on creeper state called by getColorMultiplier
	 */
	protected int updateCreeperColorMultiplier(EntityCreeper par1EntityCreeper, float par2, float par3) {
		float var5 = par1EntityCreeper.getCreeperFlashIntensity(par3);

		if ((int) (var5 * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int var6 = (int) (var5 * 0.2F * 255.0F);

			if (var6 < 0) {
				var6 = 0;
			}

			if (var6 > 255) {
				var6 = 255;
			}

			short var7 = 255;
			short var8 = 255;
			short var9 = 255;
			return var6 << 24 | var7 << 16 | var8 << 8 | var9;
		}
	}
	
	private static final TextureLocation tex_power = new TextureLocation("/armor/power.png");

	/**
	 * A method used to render a creeper's powered form as a pass model.
	 */
	protected int renderCreeperPassModel(EntityCreeper par1EntityCreeper, int par2, float par3) {
		if (par1EntityCreeper.getPowered()) {
			if (par1EntityCreeper.isInvisible()) {
				EaglerAdapter.glDepthMask(false);
			} else {
				EaglerAdapter.glDepthMask(true);
			}

			if (par2 == 1) {
				float var4 = (float) par1EntityCreeper.ticksExisted + par3;
				tex_power.bindTexture();
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
				EaglerAdapter.glLoadIdentity();
				float var5 = var4 * 0.01F;
				float var6 = var4 * 0.01F;
				EaglerAdapter.glTranslatef(var5, var6, 0.0F);
				this.setRenderPassModel(this.creeperModel);
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				float var7 = 0.5F;
				EaglerAdapter.glColor4f(var7, var7, var7, 1.0F);
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE);
				return 1;
			}

			if (par2 == 2) {
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
				EaglerAdapter.glLoadIdentity();
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			}
		}

		return -1;
	}

	protected int func_77061_b(EntityCreeper par1EntityCreeper, int par2, float par3) {
		return -1;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.updateCreeperScale((EntityCreeper) par1EntityLiving, par2);
	}

	/**
	 * Returns an ARGB int color back. Args: entityLiving, lightBrightness,
	 * partialTickTime
	 */
	protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3) {
		return this.updateCreeperColorMultiplier((EntityCreeper) par1EntityLiving, par2, par3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.renderCreeperPassModel((EntityCreeper) par1EntityLiving, par2, par3);
	}

	protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.func_77061_b((EntityCreeper) par1EntityLiving, par2, par3);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/creeper.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
