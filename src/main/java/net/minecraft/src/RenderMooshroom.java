package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderMooshroom extends RenderLiving {
	public RenderMooshroom(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}

	public void renderLivingMooshroom(EntityMooshroom par1EntityMooshroom, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityMooshroom, par2, par4, par6, par8, par9);
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");

	protected void renderMooshroomEquippedItems(EntityMooshroom par1EntityMooshroom, float par2) {
		super.renderEquippedItems(par1EntityMooshroom, par2);

		if (!par1EntityMooshroom.isChild()) {
			terrain.bindTexture();
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glScalef(1.0F, -1.0F, 1.0F);
			EaglerAdapter.glTranslatef(0.2F, 0.4F, 0.5F);
			EaglerAdapter.glRotatef(42.0F, 0.0F, 1.0F, 0.0F);
			this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
			EaglerAdapter.glTranslatef(0.1F, 0.0F, -0.6F);
			EaglerAdapter.glRotatef(42.0F, 0.0F, 1.0F, 0.0F);
			this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glPushMatrix();
			((ModelQuadruped) this.mainModel).head.postRender(0.0625F);
			EaglerAdapter.glScalef(1.0F, -1.0F, 1.0F);
			EaglerAdapter.glTranslatef(0.0F, 0.75F, -0.2F);
			EaglerAdapter.glRotatef(12.0F, 0.0F, 1.0F, 0.0F);
			this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.flipLightMatrix();
		}
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderMooshroomEquippedItems((EntityMooshroom) par1EntityLiving, par2);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderLivingMooshroom((EntityMooshroom) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.renderLivingMooshroom((EntityMooshroom) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/redcow.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
