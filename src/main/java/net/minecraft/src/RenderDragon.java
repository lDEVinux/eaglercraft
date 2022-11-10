package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class RenderDragon extends RenderLiving {
	/**
	 * Reloads the dragon model if not equal to 4. Presumably a leftover debugging
	 * field.
	 */
	private static int updateModelState = 0;

	/** An instance of the dragon model in RenderDragon */
	protected ModelDragon modelDragon;

	public RenderDragon() {
		super(new ModelDragon(0.0F), 0.5F);
		this.modelDragon = (ModelDragon) this.mainModel;
		this.setRenderPassModel(this.mainModel);
	}

	/**
	 * Used to rotate the dragon as a whole in RenderDragon. It's called in the
	 * rotateCorpse method.
	 */
	protected void rotateDragonBody(EntityDragon par1EntityDragon, float par2, float par3, float par4) {
		float var5 = (float) par1EntityDragon.getMovementOffsets(7, par4)[0];
		float var6 = (float) (par1EntityDragon.getMovementOffsets(5, par4)[1] - par1EntityDragon.getMovementOffsets(10, par4)[1]);
		EaglerAdapter.glRotatef(-var5, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(var6 * 10.0F, 1.0F, 0.0F, 0.0F);
		EaglerAdapter.glTranslatef(0.0F, 0.0F, 1.0F);

		if (par1EntityDragon.deathTime > 0) {
			float var7 = ((float) par1EntityDragon.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
			var7 = MathHelper.sqrt_float(var7);

			if (var7 > 1.0F) {
				var7 = 1.0F;
			}

			EaglerAdapter.glRotatef(var7 * this.getDeathMaxRotation(par1EntityDragon), 0.0F, 0.0F, 1.0F);
		}
	}
	
	private static final TextureLocation tex_shuffle = new TextureLocation("/mob/enderdragon/shuffle.png");
	private static final TextureLocation tex_beam = new TextureLocation("/mob/enderdragon/beam.png");
	private static final TextureLocation tex_eyes = new TextureLocation("/mob/enderdragon/ender_eyes.png");

	private static final TextureLocation entityTexture = new TextureLocation("/mob/enderdragon/ender.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		entityTexture.bindTexture();
	}

	/**
	 * Renders the dragon model. Called by renderModel.
	 */
	protected void renderDragonModel(EntityDragon par1EntityDragon, float par2, float par3, float par4, float par5, float par6, float par7) {
		if (par1EntityDragon.deathTicks > 0) {
			float var8 = (float) par1EntityDragon.deathTicks / 200.0F;
			EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
			EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, var8);
			tex_shuffle.bindTexture();
			this.mainModel.render(par1EntityDragon, par2, par3, par4, par5, par6, par7);
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.1F);
			EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);
		}

		this.bindTexture(par1EntityDragon);
		this.mainModel.render(par1EntityDragon, par2, par3, par4, par5, par6, par7);

		if (par1EntityDragon.hurtTime > 0) {
			EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			EaglerAdapter.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
			this.mainModel.render(par1EntityDragon, par2, par3, par4, par5, par6, par7);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
		}
	}

	/**
	 * Renders the dragon, along with its dying animation
	 */
	public void renderDragon(EntityDragon par1EntityDragon, double par2, double par4, double par6, float par8, float par9) {
		BossStatus.func_82824_a(par1EntityDragon, false);

		if (updateModelState != 4) {
			this.mainModel = new ModelDragon(0.0F);
			updateModelState = 4;
		}

		super.doRenderLiving(par1EntityDragon, par2, par4, par6, par8, par9);

		if (par1EntityDragon.healingEnderCrystal != null) {
			float var10 = (float) par1EntityDragon.healingEnderCrystal.innerRotation + par9;
			float var11 = MathHelper.sin(var10 * 0.2F) / 2.0F + 0.5F;
			var11 = (var11 * var11 + var11) * 0.2F;
			float var12 = (float) (par1EntityDragon.healingEnderCrystal.posX - par1EntityDragon.posX - (par1EntityDragon.prevPosX - par1EntityDragon.posX) * (double) (1.0F - par9));
			float var13 = (float) ((double) var11 + par1EntityDragon.healingEnderCrystal.posY - 1.0D - par1EntityDragon.posY - (par1EntityDragon.prevPosY - par1EntityDragon.posY) * (double) (1.0F - par9));
			float var14 = (float) (par1EntityDragon.healingEnderCrystal.posZ - par1EntityDragon.posZ - (par1EntityDragon.prevPosZ - par1EntityDragon.posZ) * (double) (1.0F - par9));
			float var15 = MathHelper.sqrt_float(var12 * var12 + var14 * var14);
			float var16 = MathHelper.sqrt_float(var12 * var12 + var13 * var13 + var14 * var14);
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef((float) par2, (float) par4 + 2.0F, (float) par6);
			EaglerAdapter.glRotatef((float) (-Math.atan2((double) var14, (double) var12)) * 180.0F / (float) Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef((float) (-Math.atan2((double) var15, (double) var13)) * 180.0F / (float) Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
			Tessellator var17 = Tessellator.instance;
			RenderHelper.disableStandardItemLighting();
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
			tex_beam.bindTexture();
			EaglerAdapter.glShadeModel(EaglerAdapter.GL_SMOOTH);
			float var18 = 0.0F - ((float) par1EntityDragon.ticksExisted + par9) * 0.01F;
			float var19 = MathHelper.sqrt_float(var12 * var12 + var13 * var13 + var14 * var14) / 32.0F - ((float) par1EntityDragon.ticksExisted + par9) * 0.01F;
			var17.startDrawing(EaglerAdapter.GL_TRIANGLE_STRIP);
			byte var20 = 8;

			for (int var21 = 0; var21 <= var20; ++var21) {
				float var22 = MathHelper.sin((float) (var21 % var20) * (float) Math.PI * 2.0F / (float) var20) * 0.75F;
				float var23 = MathHelper.cos((float) (var21 % var20) * (float) Math.PI * 2.0F / (float) var20) * 0.75F;
				float var24 = (float) (var21 % var20) * 1.0F / (float) var20;
				var17.setColorOpaque_I(0);
				var17.addVertexWithUV((double) (var22 * 0.2F), (double) (var23 * 0.2F), 0.0D, (double) var24, (double) var19);
				var17.setColorOpaque_I(16777215);
				var17.addVertexWithUV((double) var22, (double) var23, (double) var16, (double) var24, (double) var18);
			}

			var17.draw();
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glShadeModel(EaglerAdapter.GL_FLAT);
			RenderHelper.enableStandardItemLighting();
			EaglerAdapter.glPopMatrix();
		}
	}

	/**
	 * Renders the animation for when an enderdragon dies
	 */
	protected void renderDragonDying(EntityDragon par1EntityDragon, float par2) {
		super.renderEquippedItems(par1EntityDragon, par2);
		Tessellator var3 = Tessellator.instance;

		if (par1EntityDragon.deathTicks > 0) {
			RenderHelper.disableStandardItemLighting();
			float var4 = ((float) par1EntityDragon.deathTicks + par2) / 200.0F;
			float var5 = 0.0F;

			if (var4 > 0.8F) {
				var5 = (var4 - 0.8F) / 0.2F;
			}

			EaglercraftRandom var6 = new EaglercraftRandom(432L);
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glShadeModel(EaglerAdapter.GL_SMOOTH);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glDepthMask(false);
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef(0.0F, -1.0F, -2.0F);

			for (int var7 = 0; (float) var7 < (var4 + var4 * var4) / 2.0F * 60.0F; ++var7) {
				EaglerAdapter.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				EaglerAdapter.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glRotatef(var6.nextFloat() * 360.0F + var4 * 90.0F, 0.0F, 0.0F, 1.0F);
				var3.startDrawing(EaglerAdapter.GL_TRIANGLE_FAN);
				float var8 = var6.nextFloat() * 20.0F + 5.0F + var5 * 10.0F;
				float var9 = var6.nextFloat() * 2.0F + 1.0F + var5 * 2.0F;
				var3.setColorRGBA_I(16777215, (int) (255.0F * (1.0F - var5)));
				var3.addVertex(0.0D, 0.0D, 0.0D);
				var3.setColorRGBA_I(16711935, 0);
				var3.addVertex(-0.866D * (double) var9, (double) var8, (double) (-0.5F * var9));
				var3.addVertex(0.866D * (double) var9, (double) var8, (double) (-0.5F * var9));
				var3.addVertex(0.0D, (double) var8, (double) (1.0F * var9));
				var3.addVertex(-0.866D * (double) var9, (double) var8, (double) (-0.5F * var9));
				var3.draw();
			}

			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glDepthMask(true);
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glShadeModel(EaglerAdapter.GL_FLAT);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
			RenderHelper.enableStandardItemLighting();
		}
	}

	/**
	 * Renders the overlay for glowing eyes and the mouth. Called by
	 * shouldRenderPass.
	 */
	protected int renderGlow(EntityDragon par1EntityDragon, int par2, float par3) {
		if (par2 == 1) {
			EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
		}

		if (par2 != 0) {
			return -1;
		} else {
			tex_eyes.bindTexture();
			float var4 = 1.0F;
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);
			char var5 = 61680;
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return 1;
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.renderGlow((EntityDragon) par1EntityLiving, par2, par3);
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderDragonDying((EntityDragon) par1EntityLiving, par2);
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		this.rotateDragonBody((EntityDragon) par1EntityLiving, par2, par3, par4);
	}

	/**
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
		this.renderDragonModel((EntityDragon) par1EntityLiving, par2, par3, par4, par5, par6, par7);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderDragon((EntityDragon) par1EntityLiving, par2, par4, par6, par8, par9);
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
		this.renderDragon((EntityDragon) par1Entity, par2, par4, par6, par8, par9);
	}
}
