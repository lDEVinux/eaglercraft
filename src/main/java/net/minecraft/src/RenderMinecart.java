package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderMinecart extends Render {
	/** instance of ModelMinecart for rendering */
	protected ModelBase modelMinecart;
	protected final RenderBlocks field_94145_f;

	public RenderMinecart() {
		this.shadowSize = 0.5F;
		this.modelMinecart = new ModelMinecart();
		this.field_94145_f = new RenderBlocks();
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");
	private static final TextureLocation cart = new TextureLocation("/item/cart.png");

	/**
	 * Renders the Minecart.
	 */
	public void renderTheMinecart(EntityMinecart par1EntityMinecart, double par2, double par4, double par6, float par8, float par9) {
		EaglerAdapter.glPushMatrix();
		long var10 = (long) par1EntityMinecart.entityId * 493286711L;
		var10 = var10 * var10 * 4392167121L + var10 * 98761L;
		float var12 = (((float) (var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float var13 = (((float) (var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float var14 = (((float) (var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		EaglerAdapter.glTranslatef(var12, var13, var14);
		double var15 = par1EntityMinecart.lastTickPosX + (par1EntityMinecart.posX - par1EntityMinecart.lastTickPosX) * (double) par9;
		double var17 = par1EntityMinecart.lastTickPosY + (par1EntityMinecart.posY - par1EntityMinecart.lastTickPosY) * (double) par9;
		double var19 = par1EntityMinecart.lastTickPosZ + (par1EntityMinecart.posZ - par1EntityMinecart.lastTickPosZ) * (double) par9;
		double var21 = 0.30000001192092896D;
		Vec3 var23 = par1EntityMinecart.func_70489_a(var15, var17, var19);
		float var24 = par1EntityMinecart.prevRotationPitch + (par1EntityMinecart.rotationPitch - par1EntityMinecart.prevRotationPitch) * par9;

		if (var23 != null) {
			Vec3 var25 = par1EntityMinecart.func_70495_a(var15, var17, var19, var21);
			Vec3 var26 = par1EntityMinecart.func_70495_a(var15, var17, var19, -var21);

			if (var25 == null) {
				var25 = var23;
			}

			if (var26 == null) {
				var26 = var23;
			}

			par2 += var23.xCoord - var15;
			par4 += (var25.yCoord + var26.yCoord) / 2.0D - var17;
			par6 += var23.zCoord - var19;
			Vec3 var27 = var26.addVector(-var25.xCoord, -var25.yCoord, -var25.zCoord);

			if (var27.lengthVector() != 0.0D) {
				var27 = var27.normalize();
				par8 = (float) (Math.atan2(var27.zCoord, var27.xCoord) * 180.0D / Math.PI);
				var24 = (float) (Math.atan(var27.yCoord) * 73.0D);
			}
		}

		EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
		EaglerAdapter.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
		float var31 = (float) par1EntityMinecart.getRollingAmplitude() - par9;
		float var32 = (float) par1EntityMinecart.getDamage() - par9;

		if (var32 < 0.0F) {
			var32 = 0.0F;
		}

		if (var31 > 0.0F) {
			EaglerAdapter.glRotatef(MathHelper.sin(var31) * var31 * var32 / 10.0F * (float) par1EntityMinecart.getRollingDirection(), 1.0F, 0.0F, 0.0F);
		}

		int var33 = par1EntityMinecart.getDisplayTileOffset();
		Block var28 = par1EntityMinecart.getDisplayTile();
		int var29 = par1EntityMinecart.getDisplayTileData();

		if (var28 != null) {
			EaglerAdapter.glPushMatrix();
			terrain.bindTexture();
			float var30 = 0.75F;
			EaglerAdapter.glScalef(var30, var30, var30);
			EaglerAdapter.glTranslatef(0.0F, (float) var33 / 16.0F, 0.0F);
			this.renderBlockInMinecart(par1EntityMinecart, par9, var28, var29);
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		cart.bindTexture();
		EaglerAdapter.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelMinecart.render(par1EntityMinecart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		EaglerAdapter.glPopMatrix();
	}

	/**
	 * Renders the block that is inside the minecart.
	 */
	protected void renderBlockInMinecart(EntityMinecart par1EntityMinecart, float par2, Block par3Block, int par4) {
		float var5 = par1EntityMinecart.getBrightness(par2);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.flipLightMatrix();
		this.field_94145_f.renderBlockAsItem(par3Block, par4, var5);
		EaglerAdapter.flipLightMatrix();
		EaglerAdapter.glPopMatrix();
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
		this.renderTheMinecart((EntityMinecart) par1Entity, par2, par4, par6, par8, par9);
	}
}
