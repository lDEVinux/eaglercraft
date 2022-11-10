package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public abstract class SelectionListBase {
	private final Minecraft field_96622_a;
	private final int field_96619_e;
	private final int field_96616_f;
	private final int field_96617_g;
	private final int field_96627_h;
	protected final int field_96620_b;
	protected int field_96621_c;
	protected int field_96618_d;
	private float field_96628_i = -2.0F;
	private float field_96625_j;
	private float field_96626_k;
	private int field_96623_l = -1;
	private long field_96624_m = 0L;

	public SelectionListBase(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6) {
		this.field_96622_a = par1Minecraft;
		this.field_96616_f = par3;
		this.field_96627_h = par3 + par5;
		this.field_96620_b = par6;
		this.field_96619_e = par2;
		this.field_96617_g = par2 + par4;
	}

	protected abstract int func_96608_a();

	protected abstract void func_96615_a(int var1, boolean var2);

	protected abstract boolean func_96609_a(int var1);

	protected int func_96613_b() {
		return this.func_96608_a() * this.field_96620_b;
	}

	protected abstract void func_96611_c();

	protected abstract void func_96610_a(int var1, int var2, int var3, int var4, Tessellator var5);

	private void func_96614_f() {
		int var1 = this.func_96607_d();

		if (var1 < 0) {
			var1 = 0;
		}

		if (this.field_96626_k < 0.0F) {
			this.field_96626_k = 0.0F;
		}

		if (this.field_96626_k > (float) var1) {
			this.field_96626_k = (float) var1;
		}
	}

	public int func_96607_d() {
		return this.func_96613_b() - (this.field_96627_h - this.field_96616_f - 4);
	}

	public void func_96612_a(int par1, int par2, float par3) {
		this.field_96621_c = par1;
		this.field_96618_d = par2;
		this.func_96611_c();
		int var4 = this.func_96608_a();
		int var5 = this.func_96606_e();
		int var6 = var5 + 6;
		int var9;
		int var10;
		int var11;
		int var13;
		int var20;

		if (EaglerAdapter.mouseIsButtonDown(0)) {
			if (this.field_96628_i == -1.0F) {
				boolean var7 = true;

				if (par2 >= this.field_96616_f && par2 <= this.field_96627_h) {
					int var8 = this.field_96619_e + 2;
					var9 = this.field_96617_g - 2;
					var10 = par2 - this.field_96616_f + (int) this.field_96626_k - 4;
					var11 = var10 / this.field_96620_b;

					if (par1 >= var8 && par1 <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
						boolean var12 = var11 == this.field_96623_l && Minecraft.getSystemTime() - this.field_96624_m < 250L;
						this.func_96615_a(var11, var12);
						this.field_96623_l = var11;
						this.field_96624_m = Minecraft.getSystemTime();
					} else if (par1 >= var8 && par1 <= var9 && var10 < 0) {
						var7 = false;
					}

					if (par1 >= var5 && par1 <= var6) {
						this.field_96625_j = -1.0F;
						var20 = this.func_96607_d();

						if (var20 < 1) {
							var20 = 1;
						}

						var13 = (int) ((float) ((this.field_96627_h - this.field_96616_f) * (this.field_96627_h - this.field_96616_f)) / (float) this.func_96613_b());

						if (var13 < 32) {
							var13 = 32;
						}

						if (var13 > this.field_96627_h - this.field_96616_f - 8) {
							var13 = this.field_96627_h - this.field_96616_f - 8;
						}

						this.field_96625_j /= (float) (this.field_96627_h - this.field_96616_f - var13) / (float) var20;
					} else {
						this.field_96625_j = 1.0F;
					}

					if (var7) {
						this.field_96628_i = (float) par2;
					} else {
						this.field_96628_i = -2.0F;
					}
				} else {
					this.field_96628_i = -2.0F;
				}
			} else if (this.field_96628_i >= 0.0F) {
				this.field_96626_k -= ((float) par2 - this.field_96628_i) * this.field_96625_j;
				this.field_96628_i = (float) par2;
			}
		} else {
			while (!this.field_96622_a.gameSettings.touchscreen && EaglerAdapter.mouseNext()) {
				int var16 = EaglerAdapter.mouseGetEventDWheel();

				if (var16 != 0) {
					if (var16 > 0) {
						var16 = -1;
					} else if (var16 < 0) {
						var16 = 1;
					}

					this.field_96626_k += (float) (var16 * this.field_96620_b / 2);
				}
			}

			this.field_96628_i = -1.0F;
		}

		this.func_96614_f();
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);
		Tessellator var18 = Tessellator.instance;
		this.field_96622_a.renderEngine.bindTexture("/gui/background.png");
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var17 = 32.0F;
		var18.startDrawingQuads();
		var18.setColorOpaque_I(2105376);
		var18.addVertexWithUV((double) this.field_96619_e, (double) this.field_96627_h, 0.0D, (double) ((float) this.field_96619_e / var17), (double) ((float) (this.field_96627_h + (int) this.field_96626_k) / var17));
		var18.addVertexWithUV((double) this.field_96617_g, (double) this.field_96627_h, 0.0D, (double) ((float) this.field_96617_g / var17), (double) ((float) (this.field_96627_h + (int) this.field_96626_k) / var17));
		var18.addVertexWithUV((double) this.field_96617_g, (double) this.field_96616_f, 0.0D, (double) ((float) this.field_96617_g / var17), (double) ((float) (this.field_96616_f + (int) this.field_96626_k) / var17));
		var18.addVertexWithUV((double) this.field_96619_e, (double) this.field_96616_f, 0.0D, (double) ((float) this.field_96619_e / var17), (double) ((float) (this.field_96616_f + (int) this.field_96626_k) / var17));
		var18.draw();
		var9 = this.field_96619_e + 2;
		var10 = this.field_96616_f + 4 - (int) this.field_96626_k;
		int var14;

		for (var11 = 0; var11 < var4; ++var11) {
			var20 = var10 + var11 * this.field_96620_b;
			var13 = this.field_96620_b - 4;

			if (var20 + this.field_96620_b <= this.field_96627_h && var20 - 4 >= this.field_96616_f) {
				if (this.func_96609_a(var11)) {
					var14 = this.field_96619_e + 2;
					int var15 = this.field_96617_g - 2;
					EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
					var18.startDrawingQuads();
					var18.setColorOpaque_I(8421504);
					var18.addVertexWithUV((double) var14, (double) (var20 + var13 + 2), 0.0D, 0.0D, 1.0D);
					var18.addVertexWithUV((double) var15, (double) (var20 + var13 + 2), 0.0D, 1.0D, 1.0D);
					var18.addVertexWithUV((double) var15, (double) (var20 - 2), 0.0D, 1.0D, 0.0D);
					var18.addVertexWithUV((double) var14, (double) (var20 - 2), 0.0D, 0.0D, 0.0D);
					var18.setColorOpaque_I(0);
					var18.addVertexWithUV((double) (var14 + 1), (double) (var20 + var13 + 1), 0.0D, 0.0D, 1.0D);
					var18.addVertexWithUV((double) (var15 - 1), (double) (var20 + var13 + 1), 0.0D, 1.0D, 1.0D);
					var18.addVertexWithUV((double) (var15 - 1), (double) (var20 - 1), 0.0D, 1.0D, 0.0D);
					var18.addVertexWithUV((double) (var14 + 1), (double) (var20 - 1), 0.0D, 0.0D, 0.0D);
					var18.draw();
					EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
				}

				this.func_96610_a(var11, var9, var20, var13, var18);
			}
		}

		EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
		byte var19 = 4;
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glShadeModel(EaglerAdapter.GL_SMOOTH);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
		var18.startDrawingQuads();
		var18.setColorRGBA_I(0, 0);
		var18.addVertexWithUV((double) this.field_96619_e, (double) (this.field_96616_f + var19), 0.0D, 0.0D, 1.0D);
		var18.addVertexWithUV((double) this.field_96617_g, (double) (this.field_96616_f + var19), 0.0D, 1.0D, 1.0D);
		var18.setColorRGBA_I(0, 255);
		var18.addVertexWithUV((double) this.field_96617_g, (double) this.field_96616_f, 0.0D, 1.0D, 0.0D);
		var18.addVertexWithUV((double) this.field_96619_e, (double) this.field_96616_f, 0.0D, 0.0D, 0.0D);
		var18.draw();
		var18.startDrawingQuads();
		var18.setColorRGBA_I(0, 255);
		var18.addVertexWithUV((double) this.field_96619_e, (double) this.field_96627_h, 0.0D, 0.0D, 1.0D);
		var18.addVertexWithUV((double) this.field_96617_g, (double) this.field_96627_h, 0.0D, 1.0D, 1.0D);
		var18.setColorRGBA_I(0, 0);
		var18.addVertexWithUV((double) this.field_96617_g, (double) (this.field_96627_h - var19), 0.0D, 1.0D, 0.0D);
		var18.addVertexWithUV((double) this.field_96619_e, (double) (this.field_96627_h - var19), 0.0D, 0.0D, 0.0D);
		var18.draw();
		var20 = this.func_96607_d();

		if (var20 > 0) {
			var13 = (this.field_96627_h - this.field_96616_f) * (this.field_96627_h - this.field_96616_f) / this.func_96613_b();

			if (var13 < 32) {
				var13 = 32;
			}

			if (var13 > this.field_96627_h - this.field_96616_f - 8) {
				var13 = this.field_96627_h - this.field_96616_f - 8;
			}

			var14 = (int) this.field_96626_k * (this.field_96627_h - this.field_96616_f - var13) / var20 + this.field_96616_f;

			if (var14 < this.field_96616_f) {
				var14 = this.field_96616_f;
			}

			var18.startDrawingQuads();
			var18.setColorRGBA_I(0, 255);
			var18.addVertexWithUV((double) var5, (double) this.field_96627_h, 0.0D, 0.0D, 1.0D);
			var18.addVertexWithUV((double) var6, (double) this.field_96627_h, 0.0D, 1.0D, 1.0D);
			var18.addVertexWithUV((double) var6, (double) this.field_96616_f, 0.0D, 1.0D, 0.0D);
			var18.addVertexWithUV((double) var5, (double) this.field_96616_f, 0.0D, 0.0D, 0.0D);
			var18.draw();
			var18.startDrawingQuads();
			var18.setColorRGBA_I(8421504, 255);
			var18.addVertexWithUV((double) var5, (double) (var14 + var13), 0.0D, 0.0D, 1.0D);
			var18.addVertexWithUV((double) var6, (double) (var14 + var13), 0.0D, 1.0D, 1.0D);
			var18.addVertexWithUV((double) var6, (double) var14, 0.0D, 1.0D, 0.0D);
			var18.addVertexWithUV((double) var5, (double) var14, 0.0D, 0.0D, 0.0D);
			var18.draw();
			var18.startDrawingQuads();
			var18.setColorRGBA_I(12632256, 255);
			var18.addVertexWithUV((double) var5, (double) (var14 + var13 - 1), 0.0D, 0.0D, 1.0D);
			var18.addVertexWithUV((double) (var6 - 1), (double) (var14 + var13 - 1), 0.0D, 1.0D, 1.0D);
			var18.addVertexWithUV((double) (var6 - 1), (double) var14, 0.0D, 1.0D, 0.0D);
			var18.addVertexWithUV((double) var5, (double) var14, 0.0D, 0.0D, 0.0D);
			var18.draw();
		}

		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		EaglerAdapter.glShadeModel(EaglerAdapter.GL_FLAT);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
	}

	protected int func_96606_e() {
		return this.field_96617_g - 8;
	}
}
