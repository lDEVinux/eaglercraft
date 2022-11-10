package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;


public class ModelDragon extends ModelBase {
	/** The head Model renderer of the dragon */
	private ModelRenderer head;

	/** The neck Model renderer of the dragon */
	private ModelRenderer neck;

	/** The jaw Model renderer of the dragon */
	private ModelRenderer jaw;

	/** The body Model renderer of the dragon */
	private ModelRenderer body;

	/** The rear leg Model renderer of the dragon */
	private ModelRenderer rearLeg;

	/** The front leg Model renderer of the dragon */
	private ModelRenderer frontLeg;

	/** The rear leg tip Model renderer of the dragon */
	private ModelRenderer rearLegTip;

	/** The front leg tip Model renderer of the dragon */
	private ModelRenderer frontLegTip;

	/** The rear foot Model renderer of the dragon */
	private ModelRenderer rearFoot;

	/** The front foot Model renderer of the dragon */
	private ModelRenderer frontFoot;

	/** The wing Model renderer of the dragon */
	private ModelRenderer wing;

	/** The wing tip Model renderer of the dragon */
	private ModelRenderer wingTip;
	private float partialTicks;

	public ModelDragon(float par1) {
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.setTextureOffset("body.body", 0, 0);
		this.setTextureOffset("wing.skin", -56, 88);
		this.setTextureOffset("wingtip.skin", -56, 144);
		this.setTextureOffset("rearleg.main", 0, 0);
		this.setTextureOffset("rearfoot.main", 112, 0);
		this.setTextureOffset("rearlegtip.main", 196, 0);
		this.setTextureOffset("head.upperhead", 112, 30);
		this.setTextureOffset("wing.bone", 112, 88);
		this.setTextureOffset("head.upperlip", 176, 44);
		this.setTextureOffset("jaw.jaw", 176, 65);
		this.setTextureOffset("frontleg.main", 112, 104);
		this.setTextureOffset("wingtip.bone", 112, 136);
		this.setTextureOffset("frontfoot.main", 144, 104);
		this.setTextureOffset("neck.box", 192, 104);
		this.setTextureOffset("frontlegtip.main", 226, 138);
		this.setTextureOffset("body.scale", 220, 53);
		this.setTextureOffset("head.scale", 0, 0);
		this.setTextureOffset("neck.scale", 48, 0);
		this.setTextureOffset("head.nostril", 112, 0);
		float var2 = -16.0F;
		this.head = new ModelRenderer(this, "head");
		this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + var2, 12, 5, 16);
		this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + var2, 16, 16, 16);
		this.head.mirror = true;
		this.head.addBox("scale", -5.0F, -12.0F, 12.0F + var2, 2, 4, 6);
		this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + var2, 2, 2, 4);
		this.head.mirror = false;
		this.head.addBox("scale", 3.0F, -12.0F, 12.0F + var2, 2, 4, 6);
		this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + var2, 2, 2, 4);
		this.jaw = new ModelRenderer(this, "jaw");
		this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + var2);
		this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
		this.head.addChild(this.jaw);
		this.neck = new ModelRenderer(this, "neck");
		this.neck.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
		this.neck.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
		this.body = new ModelRenderer(this, "body");
		this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
		this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
		this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
		this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
		this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
		this.wing = new ModelRenderer(this, "wing");
		this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
		this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
		this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		this.wingTip = new ModelRenderer(this, "wingtip");
		this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
		this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
		this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		this.wing.addChild(this.wingTip);
		this.frontLeg = new ModelRenderer(this, "frontleg");
		this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
		this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
		this.frontLegTip = new ModelRenderer(this, "frontlegtip");
		this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
		this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
		this.frontLeg.addChild(this.frontLegTip);
		this.frontFoot = new ModelRenderer(this, "frontfoot");
		this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
		this.frontLegTip.addChild(this.frontFoot);
		this.rearLeg = new ModelRenderer(this, "rearleg");
		this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
		this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
		this.rearLegTip = new ModelRenderer(this, "rearlegtip");
		this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
		this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
		this.rearLeg.addChild(this.rearLegTip);
		this.rearFoot = new ModelRenderer(this, "rearfoot");
		this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
		this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
		this.rearLegTip.addChild(this.rearFoot);
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the setRotationAngles
	 * method.
	 */
	public void setLivingAnimations(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		this.partialTicks = par4;
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		EaglerAdapter.glPushMatrix();
		EntityDragon var8 = (EntityDragon) par1Entity;
		float var9 = var8.prevAnimTime + (var8.animTime - var8.prevAnimTime) * this.partialTicks;
		this.jaw.rotateAngleX = (float) (Math.sin((double) (var9 * (float) Math.PI * 2.0F)) + 1.0D) * 0.2F;
		float var10 = (float) (Math.sin((double) (var9 * (float) Math.PI * 2.0F - 1.0F)) + 1.0D);
		var10 = (var10 * var10 * 1.0F + var10 * 2.0F) * 0.05F;
		EaglerAdapter.glTranslatef(0.0F, var10 - 2.0F, -3.0F);
		EaglerAdapter.glRotatef(var10 * 2.0F, 1.0F, 0.0F, 0.0F);
		float var11 = -30.0F;
		float var13 = 0.0F;
		float var14 = 1.5F;
		double[] var15 = var8.getMovementOffsets(6, this.partialTicks);
		float var16 = this.updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] - var8.getMovementOffsets(10, this.partialTicks)[0]);
		float var17 = this.updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] + (double) (var16 / 2.0F));
		var11 += 2.0F;
		float var18 = var9 * (float) Math.PI * 2.0F;
		var11 = 20.0F;
		float var12 = -12.0F;
		float var21;

		for (int var19 = 0; var19 < 5; ++var19) {
			double[] var20 = var8.getMovementOffsets(5 - var19, this.partialTicks);
			var21 = (float) Math.cos((double) ((float) var19 * 0.45F + var18)) * 0.15F;
			this.neck.rotateAngleY = this.updateRotations(var20[0] - var15[0]) * (float) Math.PI / 180.0F * var14;
			this.neck.rotateAngleX = var21 + (float) (var20[1] - var15[1]) * (float) Math.PI / 180.0F * var14 * 5.0F;
			this.neck.rotateAngleZ = -this.updateRotations(var20[0] - (double) var17) * (float) Math.PI / 180.0F * var14;
			this.neck.rotationPointY = var11;
			this.neck.rotationPointZ = var12;
			this.neck.rotationPointX = var13;
			var11 = (float) ((double) var11 + Math.sin((double) this.neck.rotateAngleX) * 10.0D);
			var12 = (float) ((double) var12 - Math.cos((double) this.neck.rotateAngleY) * Math.cos((double) this.neck.rotateAngleX) * 10.0D);
			var13 = (float) ((double) var13 - Math.sin((double) this.neck.rotateAngleY) * Math.cos((double) this.neck.rotateAngleX) * 10.0D);
			this.neck.render(par7);
		}

		this.head.rotationPointY = var11;
		this.head.rotationPointZ = var12;
		this.head.rotationPointX = var13;
		double[] var22 = var8.getMovementOffsets(0, this.partialTicks);
		this.head.rotateAngleY = this.updateRotations(var22[0] - var15[0]) * (float) Math.PI / 180.0F * 1.0F;
		this.head.rotateAngleZ = -this.updateRotations(var22[0] - (double) var17) * (float) Math.PI / 180.0F * 1.0F;
		this.head.render(par7);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef(0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(-var16 * var14 * 1.0F, 0.0F, 0.0F, 1.0F);
		EaglerAdapter.glTranslatef(0.0F, -1.0F, 0.0F);
		this.body.rotateAngleZ = 0.0F;
		this.body.render(par7);

		for (int var23 = 0; var23 < 2; ++var23) {
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			var21 = var9 * (float) Math.PI * 2.0F;
			this.wing.rotateAngleX = 0.125F - (float) Math.cos((double) var21) * 0.2F;
			this.wing.rotateAngleY = 0.25F;
			this.wing.rotateAngleZ = (float) (Math.sin((double) var21) + 0.125D) * 0.8F;
			this.wingTip.rotateAngleZ = -((float) (Math.sin((double) (var21 + 2.0F)) + 0.5D)) * 0.75F;
			this.rearLeg.rotateAngleX = 1.0F + var10 * 0.1F;
			this.rearLegTip.rotateAngleX = 0.5F + var10 * 0.1F;
			this.rearFoot.rotateAngleX = 0.75F + var10 * 0.1F;
			this.frontLeg.rotateAngleX = 1.3F + var10 * 0.1F;
			this.frontLegTip.rotateAngleX = -0.5F - var10 * 0.1F;
			this.frontFoot.rotateAngleX = 0.75F + var10 * 0.1F;
			this.wing.render(par7);
			this.frontLeg.render(par7);
			this.rearLeg.render(par7);
			EaglerAdapter.glScalef(-1.0F, 1.0F, 1.0F);

			if (var23 == 0) {
				EaglerAdapter.glCullFace(EaglerAdapter.GL_FRONT);
			}
		}

		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glCullFace(EaglerAdapter.GL_BACK);
		EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
		float var24 = -((float) Math.sin((double) (var9 * (float) Math.PI * 2.0F))) * 0.0F;
		var18 = var9 * (float) Math.PI * 2.0F;
		var11 = 10.0F;
		var12 = 60.0F;
		var13 = 0.0F;
		var15 = var8.getMovementOffsets(11, this.partialTicks);

		for (int var25 = 0; var25 < 12; ++var25) {
			var22 = var8.getMovementOffsets(12 + var25, this.partialTicks);
			var24 = (float) ((double) var24 + Math.sin((double) ((float) var25 * 0.45F + var18)) * 0.05000000074505806D);
			this.neck.rotateAngleY = (this.updateRotations(var22[0] - var15[0]) * var14 + 180.0F) * (float) Math.PI / 180.0F;
			this.neck.rotateAngleX = var24 + (float) (var22[1] - var15[1]) * (float) Math.PI / 180.0F * var14 * 5.0F;
			this.neck.rotateAngleZ = this.updateRotations(var22[0] - (double) var17) * (float) Math.PI / 180.0F * var14;
			this.neck.rotationPointY = var11;
			this.neck.rotationPointZ = var12;
			this.neck.rotationPointX = var13;
			var11 = (float) ((double) var11 + Math.sin((double) this.neck.rotateAngleX) * 10.0D);
			var12 = (float) ((double) var12 - Math.cos((double) this.neck.rotateAngleY) * Math.cos((double) this.neck.rotateAngleX) * 10.0D);
			var13 = (float) ((double) var13 - Math.sin((double) this.neck.rotateAngleY) * Math.cos((double) this.neck.rotateAngleX) * 10.0D);
			this.neck.render(par7);
		}

		EaglerAdapter.glPopMatrix();
	}

	/**
	 * Updates the rotations in the parameters for rotations greater than 180
	 * degrees or less than -180 degrees. It adds or subtracts 360 degrees, so that
	 * the appearance is the same, although the numbers are then simplified to range
	 * -180 to 180
	 */
	private float updateRotations(double par1) {
		while (par1 >= 180.0D) {
			par1 -= 360.0D;
		}

		while (par1 < -180.0D) {
			par1 += 360.0D;
		}

		return (float) par1;
	}
}
