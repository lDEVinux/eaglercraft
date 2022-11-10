package net.minecraft.src;

import net.lax1dude.eaglercraft.adapter.Tessellator;

public class EntityBreakingFX extends EntityFX {
	public EntityBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item, RenderEngine par9RenderEngine) {
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.setParticleIcon(par9RenderEngine, par8Item.getIconFromDamage(0));
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleGravity = Block.blockSnow.blockParticleGravity;
		this.particleScale /= 2.0F;
	}

	public EntityBreakingFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, Item par14Item, RenderEngine par15RenderEngine) {
		this(par1World, par2, par4, par6, par14Item, par15RenderEngine);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += par8;
		this.motionY += par10;
		this.motionZ += par12;
	}

	public int getFXLayer() {
		return 2;
	}

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
		float var8 = ((float) this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
		float var9 = var8 + 0.015609375F;
		float var10 = ((float) this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
		float var11 = var10 + 0.015609375F;
		float var12 = 0.1F * this.particleScale;

		if (this.particleIcon != null) {
			var8 = this.particleIcon.getInterpolatedU((double) (this.particleTextureJitterX / 4.0F * 16.0F));
			var9 = this.particleIcon.getInterpolatedU((double) ((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
			var10 = this.particleIcon.getInterpolatedV((double) (this.particleTextureJitterY / 4.0F * 16.0F));
			var11 = this.particleIcon.getInterpolatedV((double) ((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
		}

		float var13 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) par2 - interpPosX);
		float var14 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) par2 - interpPosY);
		float var15 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) par2 - interpPosZ);
		float var16 = 1.0F;
		par1Tessellator.setColorOpaque_F(var16 * this.particleRed, var16 * this.particleGreen, var16 * this.particleBlue);
		par1Tessellator.addVertexWithUV((double) (var13 - par3 * var12 - par6 * var12), (double) (var14 - par4 * var12), (double) (var15 - par5 * var12 - par7 * var12), (double) var8, (double) var11);
		par1Tessellator.addVertexWithUV((double) (var13 - par3 * var12 + par6 * var12), (double) (var14 + par4 * var12), (double) (var15 - par5 * var12 + par7 * var12), (double) var8, (double) var10);
		par1Tessellator.addVertexWithUV((double) (var13 + par3 * var12 + par6 * var12), (double) (var14 + par4 * var12), (double) (var15 + par5 * var12 + par7 * var12), (double) var9, (double) var10);
		par1Tessellator.addVertexWithUV((double) (var13 + par3 * var12 - par6 * var12), (double) (var14 - par4 * var12), (double) (var15 + par5 * var12 - par7 * var12), (double) var9, (double) var11);
	}
}
