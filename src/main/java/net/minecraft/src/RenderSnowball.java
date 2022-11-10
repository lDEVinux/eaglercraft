package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class RenderSnowball extends Render {
	private Item field_94151_a;
	private int field_94150_f;

	public RenderSnowball(Item par1, int par2) {
		this.field_94151_a = par1;
		this.field_94150_f = par2;
	}

	public RenderSnowball(Item par1) {
		this(par1, 0);
	}
	
	private static final TextureLocation tex = new TextureLocation("/gui/items.png");

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker function
	 * which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void
	 * doRender(T entity, double d, double d1, double d2, float f, float f1). But
	 * JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		Icon var10 = this.field_94151_a.getIconFromDamage(this.field_94150_f);

		if (var10 != null) {
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glScalef(0.5F, 0.5F, 0.5F);
			tex.bindTexture();
			Tessellator var11 = Tessellator.instance;

			if (var10 == ItemPotion.func_94589_d("potion_splash")) {
				int var12 = PotionHelper.func_77915_a(((EntityPotion) par1Entity).getPotionDamage(), false);
				float var13 = (float) (var12 >> 16 & 255) / 255.0F;
				float var14 = (float) (var12 >> 8 & 255) / 255.0F;
				float var15 = (float) (var12 & 255) / 255.0F;
				EaglerAdapter.glColor3f(var13, var14, var15);
				EaglerAdapter.glPushMatrix();
				this.func_77026_a(var11, ItemPotion.func_94589_d("potion_contents"));
				EaglerAdapter.glPopMatrix();
				EaglerAdapter.glColor3f(1.0F, 1.0F, 1.0F);
			}

			this.func_77026_a(var11, var10);
			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glPopMatrix();
		}
	}

	private void func_77026_a(Tessellator par1Tessellator, Icon par2Icon) {
		float var3 = par2Icon.getMinU();
		float var4 = par2Icon.getMaxU();
		float var5 = par2Icon.getMinV();
		float var6 = par2Icon.getMaxV();
		float var7 = 1.0F;
		float var8 = 0.5F;
		float var9 = 0.25F;
		EaglerAdapter.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
		par1Tessellator.addVertexWithUV((double) (0.0F - var8), (double) (0.0F - var9), 0.0D, (double) var3, (double) var6);
		par1Tessellator.addVertexWithUV((double) (var7 - var8), (double) (0.0F - var9), 0.0D, (double) var4, (double) var6);
		par1Tessellator.addVertexWithUV((double) (var7 - var8), (double) (var7 - var9), 0.0D, (double) var4, (double) var5);
		par1Tessellator.addVertexWithUV((double) (0.0F - var8), (double) (var7 - var9), 0.0D, (double) var3, (double) var5);
		par1Tessellator.draw();
	}
}
