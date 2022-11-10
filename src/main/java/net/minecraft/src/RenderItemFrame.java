package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class RenderItemFrame extends Render {
	private final RenderBlocks renderBlocksInstance = new RenderBlocks();
	private Icon field_94147_f;

	public void updateIcons(IconRegister par1IconRegister) {
		this.field_94147_f = par1IconRegister.registerIcon("itemframe_back");
	}

	public void func_82404_a(EntityItemFrame par1EntityItemFrame, double par2, double par4, double par6, float par8, float par9) {
		EaglerAdapter.glPushMatrix();
		float var10 = (float) (par1EntityItemFrame.posX - par2) - 0.5F;
		float var11 = (float) (par1EntityItemFrame.posY - par4) - 0.5F;
		float var12 = (float) (par1EntityItemFrame.posZ - par6) - 0.5F;
		int var13 = par1EntityItemFrame.xPosition + Direction.offsetX[par1EntityItemFrame.hangingDirection];
		int var14 = par1EntityItemFrame.yPosition;
		int var15 = par1EntityItemFrame.zPosition + Direction.offsetZ[par1EntityItemFrame.hangingDirection];
		EaglerAdapter.glTranslatef((float) var13 - var10, (float) var14 - var11, (float) var15 - var12);
		if (par1EntityItemFrame.getDisplayedItem() == null || par1EntityItemFrame.getDisplayedItem().getItem() != Item.map) {
			this.renderFrameItemAsBlock(par1EntityItemFrame);
		}
		this.func_82402_b(par1EntityItemFrame);
		EaglerAdapter.glPopMatrix();
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");

	/**
	 * Render the item frame's item as a block.
	 */
	private void renderFrameItemAsBlock(EntityItemFrame par1EntityItemFrame) {
		EaglerAdapter.glPushMatrix();
		terrain.bindTexture();
		EaglerAdapter.glRotatef(par1EntityItemFrame.rotationYaw, 0.0F, 1.0F, 0.0F);
		Block var2 = Block.planks;
		float var3 = 0.0625F;
		float var4 = 0.75F;
		float var5 = var4 / 2.0F;
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.flipLightMatrix();
		this.renderBlocksInstance.overrideBlockBounds(0.0D, (double) (0.5F - var5 + 0.0625F), (double) (0.5F - var5 + 0.0625F), (double) (var3 * 0.5F), (double) (0.5F + var5 - 0.0625F), (double) (0.5F + var5 - 0.0625F));
		this.renderBlocksInstance.setOverrideBlockTexture(this.field_94147_f);
		//RenderHelper.enableStandardItemLighting2();
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		//EaglerAdapter.revertLightMatrix();
		this.renderBlocksInstance.clearOverrideBlockTexture();
		this.renderBlocksInstance.unlockBlockBounds();
		EaglerAdapter.glPopMatrix();
		this.renderBlocksInstance.setOverrideBlockTexture(Block.planks.getIcon(1, 2));
		EaglerAdapter.glPushMatrix();
		this.renderBlocksInstance.overrideBlockBounds(0.0D, (double) (0.5F - var5), (double) (0.5F - var5), (double) (var3 + 1.0E-4F), (double) (var3 + 0.5F - var5), (double) (0.5F + var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glPushMatrix();
		this.renderBlocksInstance.overrideBlockBounds(0.0D, (double) (0.5F + var5 - var3), (double) (0.5F - var5), (double) (var3 + 1.0E-4F), (double) (0.5F + var5), (double) (0.5F + var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glPushMatrix();
		this.renderBlocksInstance.overrideBlockBounds(0.0D, (double) (0.5F - var5), (double) (0.5F - var5), (double) var3, (double) (0.5F + var5), (double) (var3 + 0.5F - var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glPushMatrix();
		this.renderBlocksInstance.overrideBlockBounds(0.0D, (double) (0.5F - var5), (double) (0.5F + var5 - var3), (double) var3, (double) (0.5F + var5), (double) (0.5F + var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		EaglerAdapter.glPopMatrix();
		this.renderBlocksInstance.unlockBlockBounds();
		this.renderBlocksInstance.clearOverrideBlockTexture();
		EaglerAdapter.flipLightMatrix();
		EaglerAdapter.glPopMatrix();
	}
	
	private static final TextureLocation tex_mapbg = new TextureLocation("/misc/mapbg.png");

	private void func_82402_b(EntityItemFrame par1EntityItemFrame) {
		ItemStack var2 = par1EntityItemFrame.getDisplayedItem();

		if (var2 != null) {
			EntityItem var3 = new EntityItem(par1EntityItemFrame.worldObj, 0.0D, 0.0D, 0.0D, var2);
			var3.getEntityItem().stackSize = 1;
			var3.hoverStart = 0.0F;
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef(-0.453125F * (float) Direction.offsetX[par1EntityItemFrame.hangingDirection], -0.18F, -0.453125F * (float) Direction.offsetZ[par1EntityItemFrame.hangingDirection]);
			EaglerAdapter.glRotatef(180.0F + par1EntityItemFrame.rotationYaw, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef((float) (-90 * par1EntityItemFrame.getRotation()), 0.0F, 0.0F, 1.0F);

			switch (par1EntityItemFrame.getRotation()) {
			case 1:
				EaglerAdapter.glTranslatef(-0.16F, -0.16F, 0.0F);
				break;

			case 2:
				EaglerAdapter.glTranslatef(0.0F, -0.32F, 0.0F);
				break;

			case 3:
				EaglerAdapter.glTranslatef(0.16F, -0.16F, 0.0F);
			}

			if (var3.getEntityItem().getItem() == Item.map) {
				tex_mapbg.bindTexture();
				Tessellator var4 = Tessellator.instance;
				EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
				//EaglerAdapter.glScalef(0.00390625F, 0.00390625F, 0.00390625F);
				EaglerAdapter.glScalef(0.0078125F, 0.0078125F, 0.0078125F);
				EaglerAdapter.glTranslatef(-65.0F, -85.0F, 1.0F);
				EaglerAdapter.glNormal3f(0.0F, 0.0F, -1.0F);
				EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
				var4.startDrawingQuads();
				byte var5 = 7;
				double d = 0.001d;
				var4.addVertexWithUV((double) (0 - var5), (double) (128 + var5), 0.0D, 0.0D + d, 1.0D - d);
				var4.addVertexWithUV((double) (128 + var5), (double) (128 + var5), 0.0D, 1.0D - d, 1.0D - d);
				var4.addVertexWithUV((double) (128 + var5), (double) (0 - var5), 0.0D, 1.0D - d, 0.0D + d);
				var4.addVertexWithUV((double) (0 - var5), (double) (0 - var5), 0.0D, 0.0D + d, 0.0D + d);
				var4.draw();
				EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
				EaglerAdapter.glTranslatef(0.0F, 0.0F, -2.0F);
				MapData var6 = Item.map.getMapData(var3.getEntityItem(), par1EntityItemFrame.worldObj);
				EaglerAdapter.glTranslatef(0.0F, 0.0F, -1.0F);

				if (var6 != null) {
					this.renderManager.itemRenderer.mapItemRenderer.renderMap((EntityPlayer) null, this.renderManager.renderEngine, var6);
				}
			} else {
				TextureCompass var9;

				if (var3.getEntityItem().getItem() == Item.compass) {
					var9 = TextureCompass.compassTexture;
					double var10 = var9.currentAngle;
					double var7 = var9.angleDelta;
					var9.currentAngle = 0.0D;
					var9.angleDelta = 0.0D;
					var9.updateCompass(par1EntityItemFrame.worldObj, par1EntityItemFrame.posX, par1EntityItemFrame.posZ, (double) MathHelper.wrapAngleTo180_float((float) (180 + par1EntityItemFrame.hangingDirection * 90)), false, true);
					var9.currentAngle = var10;
					var9.angleDelta = var7;
				}

				RenderItem.renderInFrame = true;
				EaglerAdapter.flipLightMatrix();
				RenderManager.instance.renderEntityWithPosYaw(var3, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
				EaglerAdapter.flipLightMatrix();
				RenderItem.renderInFrame = false;

				if (var3.getEntityItem().getItem() == Item.compass) {
					var9 = TextureCompass.compassTexture;
					var9.updateAnimation();
				}
			}

			EaglerAdapter.glPopMatrix();
		}
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
		this.func_82404_a((EntityItemFrame) par1Entity, par2, par4, par6, par8, par9);
	}
}
