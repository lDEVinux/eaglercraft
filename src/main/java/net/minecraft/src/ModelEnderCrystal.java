package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class ModelEnderCrystal extends ModelBase {
	/** The cube model for the Ender Crystal. */
	private ModelRenderer cube;

	/** The glass model for the Ender Crystal. */
	private ModelRenderer glass = new ModelRenderer(this, "glass");

	/** The base model for the Ender Crystal. */
	private ModelRenderer base;

	public ModelEnderCrystal(float par1, boolean par2) {
		this.glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		this.cube = new ModelRenderer(this, "cube");
		this.cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);

		if (par2) {
			this.base = new ModelRenderer(this, "base");
			this.base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
		}
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glScalef(2.0F, 2.0F, 2.0F);
		EaglerAdapter.glTranslatef(0.0F, -0.5F, 0.0F);

		if (this.base != null) {
			this.base.render(par7);
		}

		EaglerAdapter.glRotatef(par3, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glTranslatef(0.0F, 0.8F + par4, 0.0F);
		EaglerAdapter.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		this.glass.render(par7);
		float var8 = 0.875F;
		EaglerAdapter.glScalef(var8, var8, var8);
		EaglerAdapter.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		EaglerAdapter.glRotatef(par3, 0.0F, 1.0F, 0.0F);
		this.glass.render(par7);
		EaglerAdapter.glScalef(var8, var8, var8);
		EaglerAdapter.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		EaglerAdapter.glRotatef(par3, 0.0F, 1.0F, 0.0F);
		this.cube.render(par7);
		EaglerAdapter.glPopMatrix();
	}
}
