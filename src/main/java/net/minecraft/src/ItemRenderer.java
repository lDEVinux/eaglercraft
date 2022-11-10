package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class ItemRenderer {
	/** A reference to the Minecraft object. */
	private Minecraft mc;
	private ItemStack itemToRender = null;

	/**
	 * How far the current item has been equipped (0 disequipped and 1 fully up)
	 */
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;

	/** Instance of RenderBlocks. */
	private RenderBlocks renderBlocksInstance = new RenderBlocks();
	public final MapItemRenderer mapItemRenderer;

	/** The index of the currently held item (0-8, or -1 if not yet updated) */
	private int equippedItemSlot = -1;

	public ItemRenderer(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
		this.mapItemRenderer = new MapItemRenderer(par1Minecraft.fontRenderer, par1Minecraft.gameSettings, par1Minecraft.renderEngine);
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");
	public static final TextureLocation items = new TextureLocation("/gui/items.png");
	private static final TextureLocation glint = new TextureLocation("%blur%/misc/glint.png");

	/**
	 * Renders the item stack for being in an entity's hand Args: itemStack
	 */
	public void renderItem(EntityLiving par1EntityLiving, ItemStack par2ItemStack, int par3) {
		EaglerAdapter.glPushMatrix();

		if (par2ItemStack.getItemSpriteNumber() == 0 && Block.blocksList[par2ItemStack.itemID] != null && RenderBlocks.renderItemIn3d(Block.blocksList[par2ItemStack.itemID].getRenderType())) {
			terrain.bindTexture();
			EaglerAdapter.flipLightMatrix();
			this.renderBlocksInstance.renderBlockAsItem(Block.blocksList[par2ItemStack.itemID], par2ItemStack.getItemDamage(), 1.0F);
			EaglerAdapter.flipLightMatrix();
		} else {
			Icon var4 = par1EntityLiving.getItemIcon(par2ItemStack, par3);

			if (var4 == null) {
				EaglerAdapter.glPopMatrix();
				return;
			}

			if (par2ItemStack.getItemSpriteNumber() == 0) {
				terrain.bindTexture();
				EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
				EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
			} else {
				items.bindTexture();
			}

			Tessellator var5 = Tessellator.instance;
			float var6 = var4.getMinU();
			float var7 = var4.getMaxU();
			float var8 = var4.getMinV();
			float var9 = var4.getMaxV();
			float var10 = 0.0F;
			float var11 = 0.3F;
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glTranslatef(-var10, -var11, 0.0F);
			float var12 = 1.5F;
			EaglerAdapter.glScalef(var12, var12, var12);
			EaglerAdapter.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			renderItemIn2D(var5, var7, var8, var6, var9, var4.getSheetWidth(), var4.getSheetHeight(), 0.0625F);

			if (par2ItemStack != null && par2ItemStack.hasEffect() && par3 == 0) {
				EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				glint.bindTexture();
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_COLOR, EaglerAdapter.GL_ONE);
				float var13 = 0.76F;
				EaglerAdapter.glColor4f(0.5F * var13, 0.25F * var13, 0.8F * var13, 1.0F);
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
				EaglerAdapter.glPushMatrix();
				float var14 = 0.125F;
				EaglerAdapter.glScalef(var14, var14, var14);
				float var15 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
				EaglerAdapter.glTranslatef(var15, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				EaglerAdapter.glPopMatrix();
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glScalef(var14, var14, var14);
				var15 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
				EaglerAdapter.glTranslatef(-var15, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				EaglerAdapter.glPopMatrix();
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
			}

			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
			
			EaglerAdapter.flipLightMatrix();
		}

		EaglerAdapter.glPopMatrix();
	}

	/**
	 * Renders an item held in hand as a 2D texture with thickness
	 */
	public static void renderItemIn2D(Tessellator par0Tessellator, float par1, float par2, float par3, float par4, int par5, int par6, float par7) {
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 0.0F, 1.0F);
		par0Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double) par1, (double) par4);
		par0Tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, (double) par3, (double) par4);
		par0Tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, (double) par3, (double) par2);
		par0Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double) par1, (double) par2);
		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 0.0F, -1.0F);
		par0Tessellator.addVertexWithUV(0.0D, 1.0D, (double) (0.0F - par7), (double) par1, (double) par2);
		par0Tessellator.addVertexWithUV(1.0D, 1.0D, (double) (0.0F - par7), (double) par3, (double) par2);
		par0Tessellator.addVertexWithUV(1.0D, 0.0D, (double) (0.0F - par7), (double) par3, (double) par4);
		par0Tessellator.addVertexWithUV(0.0D, 0.0D, (double) (0.0F - par7), (double) par1, (double) par4);
		par0Tessellator.draw();
		float var8 = (float) par5 * (par1 - par3);
		float var9 = (float) par6 * (par4 - par2);
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		int var10;
		float var11;
		float var12;

		for (var10 = 0; (float) var10 < var8; ++var10) {
			var11 = (float) var10 / var8;
			var12 = par1 + (par3 - par1) * var11 - 0.5F / (float) par5;
			par0Tessellator.addVertexWithUV((double) var11, 0.0D, (double) (0.0F - par7), (double) var12, (double) par4);
			par0Tessellator.addVertexWithUV((double) var11, 0.0D, 0.0D, (double) var12, (double) par4);
			par0Tessellator.addVertexWithUV((double) var11, 1.0D, 0.0D, (double) var12, (double) par2);
			par0Tessellator.addVertexWithUV((double) var11, 1.0D, (double) (0.0F - par7), (double) var12, (double) par2);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(1.0F, 0.0F, 0.0F);
		float var13;

		for (var10 = 0; (float) var10 < var8; ++var10) {
			var11 = (float) var10 / var8;
			var12 = par1 + (par3 - par1) * var11 - 0.5F / (float) par5;
			var13 = var11 + 1.0F / var8;
			par0Tessellator.addVertexWithUV((double) var13, 1.0D, (double) (0.0F - par7), (double) var12, (double) par2);
			par0Tessellator.addVertexWithUV((double) var13, 1.0D, 0.0D, (double) var12, (double) par2);
			par0Tessellator.addVertexWithUV((double) var13, 0.0D, 0.0D, (double) var12, (double) par4);
			par0Tessellator.addVertexWithUV((double) var13, 0.0D, (double) (0.0F - par7), (double) var12, (double) par4);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 1.0F, 0.0F);

		for (var10 = 0; (float) var10 < var9; ++var10) {
			var11 = (float) var10 / var9;
			var12 = par4 + (par2 - par4) * var11 - 0.5F / (float) par6;
			var13 = var11 + 1.0F / var9;
			par0Tessellator.addVertexWithUV(0.0D, (double) var13, 0.0D, (double) par1, (double) var12);
			par0Tessellator.addVertexWithUV(1.0D, (double) var13, 0.0D, (double) par3, (double) var12);
			par0Tessellator.addVertexWithUV(1.0D, (double) var13, (double) (0.0F - par7), (double) par3, (double) var12);
			par0Tessellator.addVertexWithUV(0.0D, (double) var13, (double) (0.0F - par7), (double) par1, (double) var12);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, -1.0F, 0.0F);

		for (var10 = 0; (float) var10 < var9; ++var10) {
			var11 = (float) var10 / var9;
			var12 = par4 + (par2 - par4) * var11 - 0.5F / (float) par6;
			par0Tessellator.addVertexWithUV(1.0D, (double) var11, 0.0D, (double) par3, (double) var12);
			par0Tessellator.addVertexWithUV(0.0D, (double) var11, 0.0D, (double) par1, (double) var12);
			par0Tessellator.addVertexWithUV(0.0D, (double) var11, (double) (0.0F - par7), (double) par1, (double) var12);
			par0Tessellator.addVertexWithUV(1.0D, (double) var11, (double) (0.0F - par7), (double) par3, (double) var12);
		}

		par0Tessellator.draw();
	}
	
	private static final TextureLocation mapbg = new TextureLocation("/misc/mapbg.png");

	/**
	 * Renders the active item in the player's hand when in first person mode. Args:
	 * partialTickTime
	 */
	public void renderItemInFirstPerson(float par1) {
		float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * par1;
		EntityClientPlayerMP var3 = this.mc.thePlayer;
		float var4 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * par1;
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glRotatef(var4, 1.0F, 0.0F, 0.0F);
		EaglerAdapter.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		EaglerAdapter.glPopMatrix();
		float var6;
		float var7;

		if (var3 instanceof EntityPlayerSP) {
			EntityPlayerSP var5 = (EntityPlayerSP) var3;
			var6 = var5.prevRenderArmPitch + (var5.renderArmPitch - var5.prevRenderArmPitch) * par1;
			var7 = var5.prevRenderArmYaw + (var5.renderArmYaw - var5.prevRenderArmYaw) * par1;
			EaglerAdapter.glRotatef((var3.rotationPitch - var6) * 0.1F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glRotatef((var3.rotationYaw - var7) * 0.1F, 0.0F, 1.0F, 0.0F);
		}

		ItemStack var17 = this.itemToRender;
		var6 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ));
		var6 = 1.0F;
		int var18 = this.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ), 0);
		int var8 = var18 % 65536;
		int var9 = var18 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var8 / 1.0F, (float) var9 / 1.0F);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var10;
		float var19;
		float var21;

		if (var17 != null) {
			var18 = Item.itemsList[var17.itemID].getColorFromItemStack(var17, 0);
			var19 = (float) (var18 >> 16 & 255) / 255.0F;
			var21 = (float) (var18 >> 8 & 255) / 255.0F;
			var10 = (float) (var18 & 255) / 255.0F;
			EaglerAdapter.glColor4f(var6 * var19, var6 * var21, var6 * var10, 1.0F);
		} else {
			EaglerAdapter.glColor4f(var6, var6, var6, 1.0F);
		}

		float var11;
		float var12;
		float var13;
		Render var24 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
		RenderPlayer var26 = (RenderPlayer) var24;

		if (var17 != null && var17.itemID == Item.map.itemID) {
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glPushMatrix();
			var7 = 0.8F;
			var19 = var3.getSwingProgress(par1);
			var21 = MathHelper.sin(var19 * (float) Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI);
			EaglerAdapter.glTranslatef(-var10 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI * 2.0F) * 0.2F, -var21 * 0.2F);
			var19 = 1.0F - var4 / 45.0F + 0.1F;

			if (var19 < 0.0F) {
				var19 = 0.0F;
			}

			if (var19 > 1.0F) {
				var19 = 1.0F;
			}

			var19 = -MathHelper.cos(var19 * (float) Math.PI) * 0.5F + 0.5F;
			EaglerAdapter.glTranslatef(0.0F, 0.0F * var7 - (1.0F - var2) * 1.2F - var19 * 0.5F + 0.04F, -0.9F * var7);
			EaglerAdapter.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(var19 * -85.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			
			var26.bindTexture(var3);

			for (var9 = 0; var9 < 2; ++var9) {
				int var22 = var9 * 2 - 1;
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(0.0F, -0.6F, 1.1F * (float) var22);
				EaglerAdapter.glRotatef((float) (-45 * var22), 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				EaglerAdapter.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				EaglerAdapter.glRotatef((float) (-65 * var22), 0.0F, 1.0F, 0.0F);
				var13 = 1.0F;
				EaglerAdapter.glScalef(var13, var13, var13);
				var26.renderFirstPersonArm(this.mc.thePlayer);
				EaglerAdapter.glPopMatrix();
			}

			var21 = var3.getSwingProgress(par1);
			var10 = MathHelper.sin(var21 * var21 * (float) Math.PI);
			var11 = MathHelper.sin(MathHelper.sqrt_float(var21) * (float) Math.PI);
			EaglerAdapter.glRotatef(-var10 * 20.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(-var11 * 20.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glRotatef(-var11 * 80.0F, 1.0F, 0.0F, 0.0F);
			var12 = 0.38F;
			EaglerAdapter.glScalef(var12, var12, var12);
			EaglerAdapter.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glTranslatef(-1.0F, -1.0F, 0.0F);
			var13 = 0.015625F;
			EaglerAdapter.glScalef(var13, var13, var13);
			mapbg.bindTexture();
			Tessellator var27 = Tessellator.instance;
			EaglerAdapter.glNormal3f(0.0F, 0.0F, -1.0F);
			var27.startDrawingQuads();
			byte var28 = 7;
			var27.addVertexWithUV((double) (0 - var28), (double) (128 + var28), 0.0D, 0.0D, 1.0D);
			var27.addVertexWithUV((double) (128 + var28), (double) (128 + var28), 0.0D, 1.0D, 1.0D);
			var27.addVertexWithUV((double) (128 + var28), (double) (0 - var28), 0.0D, 1.0D, 0.0D);
			var27.addVertexWithUV((double) (0 - var28), (double) (0 - var28), 0.0D, 0.0D, 0.0D);
			var27.draw();
			MapData var16 = Item.map.getMapData(var17, this.mc.theWorld);

			if (var16 != null) {
				this.mapItemRenderer.renderMap(this.mc.thePlayer, this.mc.renderEngine, var16);
			}

			EaglerAdapter.glPopMatrix();
			EaglerAdapter.flipLightMatrix();
		} else if (var17 != null) {
			EaglerAdapter.glPushMatrix();
			var7 = 0.8F;

			if (var3.getItemInUseCount() > 0) {
				EnumAction var20 = var17.getItemUseAction();

				if (var20 == EnumAction.eat || var20 == EnumAction.drink) {
					var21 = (float) var3.getItemInUseCount() - par1 + 1.0F;
					var10 = 1.0F - var21 / (float) var17.getMaxItemUseDuration();
					var11 = 1.0F - var10;
					var11 = var11 * var11 * var11;
					var11 = var11 * var11 * var11;
					var11 = var11 * var11 * var11;
					var12 = 1.0F - var11;
					EaglerAdapter.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(var21 / 4.0F * (float) Math.PI) * 0.1F) * (float) ((double) var10 > 0.2D ? 1 : 0), 0.0F);
					EaglerAdapter.glTranslatef(var12 * 0.6F, -var12 * 0.5F, 0.0F);
					EaglerAdapter.glRotatef(var12 * 90.0F, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(var12 * 10.0F, 1.0F, 0.0F, 0.0F);
					EaglerAdapter.glRotatef(var12 * 30.0F, 0.0F, 0.0F, 1.0F);
				}
			} else {
				var19 = var3.getSwingProgress(par1);
				var21 = MathHelper.sin(var19 * (float) Math.PI);
				var10 = MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI);
				EaglerAdapter.glTranslatef(-var10 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI * 2.0F) * 0.2F, -var21 * 0.2F);
			}

			EaglerAdapter.glTranslatef(0.7F * var7, -0.65F * var7 - (1.0F - var2) * 0.6F, -0.9F * var7);
			EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			var19 = var3.getSwingProgress(par1);
			var21 = MathHelper.sin(var19 * var19 * (float) Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI);
			EaglerAdapter.glRotatef(-var21 * 20.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(-var10 * 20.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glRotatef(-var10 * 80.0F, 1.0F, 0.0F, 0.0F);
			var11 = 0.4F;
			EaglerAdapter.glScalef(var11, var11, var11);
			float var14;
			float var15;

			if (var3.getItemInUseCount() > 0) {
				EnumAction var23 = var17.getItemUseAction();

				if (var23 == EnumAction.block) {
					EaglerAdapter.glTranslatef(-0.5F, 0.2F, 0.0F);
					EaglerAdapter.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
					EaglerAdapter.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
				} else if (var23 == EnumAction.bow) {
					EaglerAdapter.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
					EaglerAdapter.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
					EaglerAdapter.glTranslatef(-0.9F, 0.2F, 0.0F);
					var13 = (float) var17.getMaxItemUseDuration() - ((float) var3.getItemInUseCount() - par1 + 1.0F);
					var14 = var13 / 20.0F;
					var14 = (var14 * var14 + var14 * 2.0F) / 3.0F;

					if (var14 > 1.0F) {
						var14 = 1.0F;
					}

					if (var14 > 0.1F) {
						EaglerAdapter.glTranslatef(0.0F, MathHelper.sin((var13 - 0.1F) * 1.3F) * 0.01F * (var14 - 0.1F), 0.0F);
					}

					EaglerAdapter.glTranslatef(0.0F, 0.0F, var14 * 0.1F);
					EaglerAdapter.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
					EaglerAdapter.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glTranslatef(0.0F, 0.5F, 0.0F);
					var15 = 1.0F + var14 * 0.2F;
					EaglerAdapter.glScalef(1.0F, 1.0F, var15);
					EaglerAdapter.glTranslatef(0.0F, -0.5F, 0.0F);
					EaglerAdapter.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				}
			}

			if (var17.getItem().shouldRotateAroundWhenRendering()) {
				EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var17.getItem().requiresMultipleRenderPasses()) {
				this.renderItem(var3, var17, 0);
				int var25 = Item.itemsList[var17.itemID].getColorFromItemStack(var17, 1);
				var13 = (float) (var25 >> 16 & 255) / 255.0F;
				var14 = (float) (var25 >> 8 & 255) / 255.0F;
				var15 = (float) (var25 & 255) / 255.0F;
				EaglerAdapter.glColor4f(var6 * var13, var6 * var14, var6 * var15, 1.0F);
				this.renderItem(var3, var17, 1);
			} else {
				this.renderItem(var3, var17, 0);
			}

			EaglerAdapter.glPopMatrix();
		} else if (!var3.isInvisible()) {
			EaglerAdapter.glPushMatrix();
			var7 = 0.8F;
			var19 = var3.getSwingProgress(par1);
			var21 = MathHelper.sin(var19 * (float) Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI);
			EaglerAdapter.glTranslatef(-var10 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI * 2.0F) * 0.4F, -var21 * 0.4F);
			EaglerAdapter.glTranslatef(0.8F * var7, -0.75F * var7 - (1.0F - var2) * 0.6F, -0.9F * var7);
			EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			var19 = var3.getSwingProgress(par1);
			var21 = MathHelper.sin(var19 * var19 * (float) Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var19) * (float) Math.PI);
			EaglerAdapter.glRotatef(var10 * 70.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(-var21 * 20.0F, 0.0F, 0.0F, 1.0F);
			var26.bindTexture(var3);
			EaglerAdapter.glTranslatef(-1.0F, 3.6F, 3.5F);
			EaglerAdapter.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glScalef(1.0F, 1.0F, 1.0F);
			EaglerAdapter.glTranslatef(5.6F, 0.0F, 0.0F);
			var24 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			var26 = (RenderPlayer) var24;
			var13 = 1.0F;
			EaglerAdapter.glScalef(var13, var13, var13);
			var26.renderFirstPersonArm(this.mc.thePlayer);
			EaglerAdapter.glPopMatrix();
		}

		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}
	
	private static final TextureLocation water = new TextureLocation("/misc/water.png");

	/**
	 * Renders all the overlays that are in first person mode. Args: partialTickTime
	 */
	public void renderOverlays(float par1) {
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);

		if (this.mc.thePlayer.isBurning()) {
			terrain.bindTexture();
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
			this.renderFireInFirstPerson(par1);
		}

		if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
			int var2 = MathHelper.floor_double(this.mc.thePlayer.posX);
			int var3 = MathHelper.floor_double(this.mc.thePlayer.posY);
			int var4 = MathHelper.floor_double(this.mc.thePlayer.posZ);
			terrain.bindTexture();
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
			int var5 = this.mc.theWorld.getBlockId(var2, var3, var4);

			if (this.mc.theWorld.isBlockNormalCube(var2, var3, var4)) {
				this.renderInsideOfBlock(par1, Block.blocksList[var5].getBlockTextureFromSide(2));
			} else {
				for (int var6 = 0; var6 < 8; ++var6) {
					float var7 = ((float) ((var6 >> 0) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					float var8 = ((float) ((var6 >> 1) % 2) - 0.5F) * this.mc.thePlayer.height * 0.2F;
					float var9 = ((float) ((var6 >> 2) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					int var10 = MathHelper.floor_float((float) var2 + var7);
					int var11 = MathHelper.floor_float((float) var3 + var8);
					int var12 = MathHelper.floor_float((float) var4 + var9);

					if (this.mc.theWorld.isBlockNormalCube(var10, var11, var12)) {
						var5 = this.mc.theWorld.getBlockId(var10, var11, var12);
					}
				}
			}

			if (Block.blocksList[var5] != null) {
				this.renderInsideOfBlock(par1, Block.blocksList[var5].getBlockTextureFromSide(2));
			}
		}

		if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
			water.bindTexture();
			this.renderWarpedTextureOverlay(par1);
		}

		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
	}

	/**
	 * Renders the texture of the block the player is inside as an overlay. Args:
	 * partialTickTime, blockTextureIndex
	 */
	private void renderInsideOfBlock(float par1, Icon par2Icon) {
		if (par2Icon == null) return;
		Tessellator var3 = Tessellator.instance;
		float var4 = 0.1F;
		EaglerAdapter.glColor4f(var4, var4, var4, 0.5F);
		EaglerAdapter.glPushMatrix();
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = par2Icon.getMinU();
		float var11 = par2Icon.getMaxU();
		float var12 = par2Icon.getMinV();
		float var13 = par2Icon.getMaxV();
		var3.startDrawingQuads();
		var3.addVertexWithUV((double) var5, (double) var7, (double) var9, (double) var11, (double) var13);
		var3.addVertexWithUV((double) var6, (double) var7, (double) var9, (double) var10, (double) var13);
		var3.addVertexWithUV((double) var6, (double) var8, (double) var9, (double) var10, (double) var12);
		var3.addVertexWithUV((double) var5, (double) var8, (double) var9, (double) var11, (double) var12);
		var3.draw();
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders a texture that warps around based on the direction the player is
	 * looking. Texture needs to be bound before being called. Used for the water
	 * overlay. Args: parialTickTime
	 */
	private void renderWarpedTextureOverlay(float par1) {
		Tessellator var2 = Tessellator.instance;
		float var3 = this.mc.thePlayer.getBrightness(par1);
		EaglerAdapter.glColor4f(var3, var3, var3, 0.5F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glPushMatrix();
		float var4 = 4.0F;
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = -this.mc.thePlayer.rotationYaw / 64.0F;
		float var11 = this.mc.thePlayer.rotationPitch / 64.0F;
		var2.startDrawingQuads();
		var2.addVertexWithUV((double) var5, (double) var7, (double) var9, (double) (var4 + var10), (double) (var4 + var11));
		var2.addVertexWithUV((double) var6, (double) var7, (double) var9, (double) (0.0F + var10), (double) (var4 + var11));
		var2.addVertexWithUV((double) var6, (double) var8, (double) var9, (double) (0.0F + var10), (double) (0.0F + var11));
		var2.addVertexWithUV((double) var5, (double) var8, (double) var9, (double) (var4 + var10), (double) (0.0F + var11));
		var2.draw();
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
	}

	/**
	 * Renders the fire on the screen for first person mode. Arg: partialTickTime
	 */
	private void renderFireInFirstPerson(float par1) {
		Tessellator var2 = Tessellator.instance;
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		float var3 = 1.0F;

		for (int var4 = 0; var4 < 2; ++var4) {
			EaglerAdapter.glPushMatrix();
			Icon var5 = Block.fire.func_94438_c(1);
			float var6 = var5.getMinU();
			float var7 = var5.getMaxU();
			float var8 = var5.getMinV();
			float var9 = var5.getMaxV();
			float var10 = (0.0F - var3) / 2.0F;
			float var11 = var10 + var3;
			float var12 = 0.0F - var3 / 2.0F;
			float var13 = var12 + var3;
			float var14 = -0.5F;
			EaglerAdapter.glTranslatef((float) (-(var4 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
			EaglerAdapter.glRotatef((float) (var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			var2.startDrawingQuads();
			var2.addVertexWithUV((double) var10, (double) var12, (double) var14, (double) var7, (double) var9);
			var2.addVertexWithUV((double) var11, (double) var12, (double) var14, (double) var6, (double) var9);
			var2.addVertexWithUV((double) var11, (double) var13, (double) var14, (double) var6, (double) var8);
			var2.addVertexWithUV((double) var10, (double) var13, (double) var14, (double) var7, (double) var8);
			var2.draw();
			EaglerAdapter.glPopMatrix();
		}

		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
	}

	public void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		EntityClientPlayerMP var1 = this.mc.thePlayer;
		ItemStack var2 = var1.inventory.getCurrentItem();
		boolean var3 = this.equippedItemSlot == var1.inventory.currentItem && var2 == this.itemToRender;

		if (this.itemToRender == null && var2 == null) {
			var3 = true;
		}

		if (var2 != null && this.itemToRender != null && var2 != this.itemToRender && var2.itemID == this.itemToRender.itemID && var2.getItemDamage() == this.itemToRender.getItemDamage()) {
			this.itemToRender = var2;
			var3 = true;
		}

		float var4 = 0.4F;
		float var5 = var3 ? 1.0F : 0.0F;
		float var6 = var5 - this.equippedProgress;

		if (var6 < -var4) {
			var6 = -var4;
		}

		if (var6 > var4) {
			var6 = var4;
		}

		this.equippedProgress += var6;

		if (this.equippedProgress < 0.1F) {
			this.itemToRender = var2;
			this.equippedItemSlot = var1.inventory.currentItem;
		}
	}

	/**
	 * Resets equippedProgress
	 */
	public void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}

	/**
	 * Resets equippedProgress
	 */
	public void resetEquippedProgress2() {
		this.equippedProgress = 0.0F;
	}
}
