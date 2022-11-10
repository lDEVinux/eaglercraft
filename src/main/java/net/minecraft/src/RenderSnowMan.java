package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderSnowMan extends RenderLiving {
	/** A reference to the Snowman model in RenderSnowMan. */
	private ModelSnowMan snowmanModel;

	public RenderSnowMan() {
		super(new ModelSnowMan(), 0.5F);
		this.snowmanModel = (ModelSnowMan) super.mainModel;
		this.setRenderPassModel(this.snowmanModel);
	}

	/**
	 * Renders this snowman's pumpkin.
	 */
	protected void renderSnowmanPumpkin(EntitySnowman par1EntitySnowman, float par2) {
		super.renderEquippedItems(par1EntitySnowman, par2);
		ItemStack var3 = new ItemStack(Block.pumpkin, 1);

		if (var3 != null && var3.getItem().itemID < 256) {
			EaglerAdapter.glPushMatrix();
			this.snowmanModel.head.postRender(0.0625F);

			if (RenderBlocks.renderItemIn3d(Block.blocksList[var3.itemID].getRenderType())) {
				float var4 = 0.625F;
				EaglerAdapter.glTranslatef(0.0F, -0.34375F, 0.0F);
				EaglerAdapter.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glScalef(var4, -var4, var4);
			}

			this.renderManager.itemRenderer.renderItem(par1EntitySnowman, var3, 0);
			EaglerAdapter.glPopMatrix();
		}
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderSnowmanPumpkin((EntitySnowman) par1EntityLiving, par2);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/snowman.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
