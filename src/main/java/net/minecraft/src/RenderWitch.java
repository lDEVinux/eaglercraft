package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderWitch extends RenderLiving {
	private ModelWitch field_82414_a;
	private int field_82413_f;

	public RenderWitch() {
		super(new ModelWitch(0.0F), 0.5F);
		this.field_82414_a = (ModelWitch) this.mainModel;
		this.field_82413_f = this.field_82414_a.func_82899_a();
	}

	public void func_82412_a(EntityWitch par1EntityWitch, double par2, double par4, double par6, float par8, float par9) {
		ItemStack var10 = par1EntityWitch.getHeldItem();

		if (this.field_82414_a.func_82899_a() != this.field_82413_f) {
			this.mainModel = this.field_82414_a = new ModelWitch(0.0F);
			this.field_82413_f = this.field_82414_a.func_82899_a();
		}

		this.field_82414_a.field_82900_g = var10 != null;
		super.doRenderLiving(par1EntityWitch, par2, par4, par6, par8, par9);
	}

	protected void func_82411_a(EntityWitch par1EntityWitch, float par2) {
		float var3 = 1.0F;
		EaglerAdapter.glColor3f(var3, var3, var3);
		super.renderEquippedItems(par1EntityWitch, par2);
		ItemStack var4 = par1EntityWitch.getHeldItem();

		if (var4 != null) {
			EaglerAdapter.glPushMatrix();
			float var5;

			if (this.mainModel.isChild) {
				var5 = 0.5F;
				EaglerAdapter.glTranslatef(0.0F, 0.625F, 0.0F);
				EaglerAdapter.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
				EaglerAdapter.glScalef(var5, var5, var5);
			}

			this.field_82414_a.field_82898_f.postRender(0.0625F);
			EaglerAdapter.glTranslatef(-0.0625F, 0.53125F, 0.21875F);

			if (var4.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var4.itemID].getRenderType())) {
				var5 = 0.5F;
				EaglerAdapter.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var5 *= 0.75F;
				EaglerAdapter.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glScalef(var5, -var5, var5);
			} else if (var4.itemID == Item.bow.itemID) {
				var5 = 0.625F;
				EaglerAdapter.glTranslatef(0.0F, 0.125F, 0.3125F);
				EaglerAdapter.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glScalef(var5, -var5, var5);
				EaglerAdapter.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (Item.itemsList[var4.itemID].isFull3D()) {
				var5 = 0.625F;

				if (Item.itemsList[var4.itemID].shouldRotateAroundWhenRendering()) {
					EaglerAdapter.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					EaglerAdapter.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				this.func_82410_b();
				EaglerAdapter.glScalef(var5, -var5, var5);
				EaglerAdapter.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var5 = 0.375F;
				EaglerAdapter.glTranslatef(0.25F, 0.1875F, -0.1875F);
				EaglerAdapter.glScalef(var5, var5, var5);
				EaglerAdapter.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				EaglerAdapter.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			EaglerAdapter.glRotatef(-15.0F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
			this.renderManager.itemRenderer.renderItem(par1EntityWitch, var4, 0);

			if (var4.getItem().requiresMultipleRenderPasses()) {
				this.renderManager.itemRenderer.renderItem(par1EntityWitch, var4, 1);
			}

			EaglerAdapter.glPopMatrix();
		}
	}

	protected void func_82410_b() {
		EaglerAdapter.glTranslatef(0.0F, 0.1875F, 0.0F);
	}

	protected void func_82409_b(EntityWitch par1EntityWitch, float par2) {
		float var3 = 0.9375F;
		EaglerAdapter.glScalef(var3, var3, var3);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.func_82409_b((EntityWitch) par1EntityLiving, par2);
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.func_82411_a((EntityWitch) par1EntityLiving, par2);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.func_82412_a((EntityWitch) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.func_82412_a((EntityWitch) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/villager/witch.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}
}
