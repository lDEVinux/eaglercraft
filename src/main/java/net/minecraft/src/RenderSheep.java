package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderSheep extends RenderLiving {
	public RenderSheep(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}
	
	private static final TextureLocation tex_fur = new TextureLocation("/mob/sheep_fur.png");

	protected int setWoolColorAndRender(EntitySheep par1EntitySheep, int par2, float par3) {
		if (par2 == 0 && !par1EntitySheep.getSheared()) {
			tex_fur.bindTexture();
			float var4 = 1.0F;
			int var5 = par1EntitySheep.getFleeceColor();
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
		return this.setWoolColorAndRender((EntitySheep) par1EntityLiving, par2, par3);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/sheep.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
