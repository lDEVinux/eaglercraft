package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderSkeleton extends RenderBiped {
	public RenderSkeleton() {
		super(new ModelSkeleton(), 0.5F);
	}

	protected void func_82438_a(EntitySkeleton par1EntitySkeleton, float par2) {
		if (par1EntitySkeleton.getSkeletonType() == 1) {
			EaglerAdapter.glScalef(1.2F, 1.2F, 1.2F);
		}
	}

	protected void func_82422_c() {
		EaglerAdapter.glTranslatef(0.09375F, 0.1875F, 0.0F);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.func_82438_a((EntitySkeleton) par1EntityLiving, par2);
	}

	private static final TextureLocation entityTexture0 = new TextureLocation("/mob/skeleton.png");
	private static final TextureLocation entityTexture1 = new TextureLocation("/mob/skeleton_wither.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		if(((EntitySkeleton)par1EntityLiving).getSkeletonType() == 1) {
			entityTexture1.bindTexture();
		}else {
			entityTexture0.bindTexture();
		}
	}
}
