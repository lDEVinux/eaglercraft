package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class RenderItem extends Render {
	private RenderBlocks itemRenderBlocks = new RenderBlocks();

	/** The RNG used in RenderItem (for bobbing itemstacks on the ground) */
	private EaglercraftRandom random = new EaglercraftRandom();
	public boolean renderWithColor = true;

	/** Defines the zLevel of rendering of item on GUI. */
	public float zLevel = 0.0F;
	public static boolean renderInFrame = false;

	public RenderItem() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}
	
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");
	private static final TextureLocation items = new TextureLocation("/gui/items.png");
	
	public static boolean isRenderInProgress = false;

	/**
	 * Renders the item
	 */
	public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
		isRenderInProgress = true;
		this.random.setSeed(187L);
		ItemStack var10 = par1EntityItem.getEntityItem();
		
		if (var10.getItem() != null) {
			EaglerAdapter.glPushMatrix();
			float var11 = MathHelper.sin(((float) par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
			float var12 = (((float) par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI);
			byte var13 = 1;

			if (par1EntityItem.getEntityItem().stackSize > 1) {
				var13 = 2;
			}

			if (par1EntityItem.getEntityItem().stackSize > 5) {
				var13 = 3;
			}

			if (par1EntityItem.getEntityItem().stackSize > 20) {
				var13 = 4;
			}

			if (par1EntityItem.getEntityItem().stackSize > 40) {
				var13 = 5;
			}

			EaglerAdapter.glTranslatef((float) par2, (float) par4 + var11, (float) par6);
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			
			int var17;
			float var18;
			float var19;
			float var20;

			if (var10.getItemSpriteNumber() == 0 && Block.blocksList[var10.itemID] != null && RenderBlocks.renderItemIn3d(Block.blocksList[var10.itemID].getRenderType())) {
				Block var22 = Block.blocksList[var10.itemID];
				EaglerAdapter.glRotatef(var12, 0.0F, 1.0F, 0.0F);
				//RenderHelper.enableStandardItemLighting();

				if (renderInFrame) {
					EaglerAdapter.glScalef(1.25F, 1.25F, 1.25F);
					EaglerAdapter.glTranslatef(0.0F, 0.05F, 0.0F);
					EaglerAdapter.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				terrain.bindTexture();
				float var24 = 0.25F;
				int var25 = var22.getRenderType();

				if (var25 == 1 || var25 == 19 || var25 == 12 || var25 == 2) {
					var24 = 0.5F;
				}

				EaglerAdapter.glScalef(var24, var24, var24);

				for (var17 = 0; var17 < var13; ++var17) {
					EaglerAdapter.glPushMatrix();

					if (var17 > 0) {
						var18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
						var19 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
						var20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
						EaglerAdapter.glTranslatef(var18, var19, var20);
					}

					var18 = 1.0F;
					if (!renderInFrame) EaglerAdapter.flipLightMatrix();
					this.itemRenderBlocks.renderBlockAsItem(var22, var10.getItemDamage(), var18);
					if (!renderInFrame) EaglerAdapter.flipLightMatrix();
					EaglerAdapter.glPopMatrix();
				}
			} else {
				float var16;

				if (var10.getItem().requiresMultipleRenderPasses()) {
					if (renderInFrame) {
						EaglerAdapter.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
						EaglerAdapter.glTranslatef(0.0F, -0.05F, 0.0F);
					} else {
						EaglerAdapter.glScalef(0.5F, 0.5F, 0.5F);
					}

					items.bindTexture();

					for (int var14 = 0; var14 <= 1; ++var14) {
						this.random.setSeed(187L);
						Icon var15 = var10.getItem().getIconFromDamageForRenderPass(var10.getItemDamage(), var14);
						var16 = 1.0F;

						if (this.renderWithColor) {
							var17 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, var14);
							var18 = (float) (var17 >> 16 & 255) / 255.0F;
							var19 = (float) (var17 >> 8 & 255) / 255.0F;
							var20 = (float) (var17 & 255) / 255.0F;
							EaglerAdapter.glColor4f(var18 * var16, var19 * var16, var20 * var16, 1.0F);
							this.renderDroppedItem(par1EntityItem, var15, var13, par9, var18 * var16, var19 * var16, var20 * var16);
						} else {
							this.renderDroppedItem(par1EntityItem, var15, var13, par9, 1.0F, 1.0F, 1.0F);
						}
					}
				} else {
					if (renderInFrame) {
						EaglerAdapter.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
						EaglerAdapter.glTranslatef(0.0F, -0.05F, 0.0F);
					} else {
						EaglerAdapter.glScalef(0.5F, 0.5F, 0.5F);
					}

					Icon var21 = var10.getIconIndex();

					if (var10.getItemSpriteNumber() == 0) {
						terrain.bindTexture();
					} else {
						items.bindTexture();
					}

					if (this.renderWithColor) {
						int var23 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, 0);
						var16 = (float) (var23 >> 16 & 255) / 255.0F;
						float var26 = (float) (var23 >> 8 & 255) / 255.0F;
						var18 = (float) (var23 & 255) / 255.0F;
						var19 = 1.0F;
						this.renderDroppedItem(par1EntityItem, var21, var13, par9, var16 * var19, var26 * var19, var18 * var19);
					} else {
						this.renderDroppedItem(par1EntityItem, var21, var13, par9, 1.0F, 1.0F, 1.0F);
					}
				}
			}

			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glPopMatrix();
		}
		isRenderInProgress = false;
	}
	
	private static final TextureLocation glint = new TextureLocation("%blur%/misc/glint.png");

	/**
	 * Renders a dropped item
	 */
	private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7) {
		Tessellator var8 = Tessellator.instance;

		if (par2Icon == null) {
			par2Icon = this.renderManager.renderEngine.getMissingIcon(par1EntityItem.getEntityItem().getItemSpriteNumber());
		}

		float var9 = par2Icon.getMinU();
		float var10 = par2Icon.getMaxU();
		float var11 = par2Icon.getMinV();
		float var12 = par2Icon.getMaxV();
		float var13 = 1.0F;
		float var14 = 0.5F;
		float var15 = 0.25F;
		float var17;

		if (this.renderManager.options.fancyGraphics) {
			EaglerAdapter.glPushMatrix();

			if (renderInFrame) {
				EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			} else {
				EaglerAdapter.glRotatef((((float) par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
			}

			float var16 = 0.0625F;
			var17 = 0.021875F;
			ItemStack var18 = par1EntityItem.getEntityItem();
			int var19 = var18.stackSize;
			byte var24;

			if (var19 < 2) {
				var24 = 1;
			} else if (var19 < 16) {
				var24 = 2;
			} else if (var19 < 32) {
				var24 = 3;
			} else {
				var24 = 4;
			}

			EaglerAdapter.glTranslatef(-var14, -var15, -((var16 + var17) * (float) var24 / 2.0F));

			for (int var20 = 0; var20 < var24; ++var20) {
				EaglerAdapter.glTranslatef(0.0F, 0.0F, var16 + var17);

				if (var18.getItemSpriteNumber() == 0 && Block.blocksList[var18.itemID] != null) {
					terrain.bindTexture();
				} else {
					items.bindTexture();
				}

				EaglerAdapter.glColor4f(par5, par6, par7, 1.0F);
				EaglerAdapter.flipLightMatrix();
				ItemRenderer.renderItemIn2D(var8, var10, var11, var9, var12, par2Icon.getSheetWidth(), par2Icon.getSheetHeight(), var16);

				if (var18 != null && var18.hasEffect()) {
					EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);
					EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
					glint.bindTexture();
					EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_COLOR, EaglerAdapter.GL_ONE);
					float var21 = 0.76F;
					EaglerAdapter.glColor4f(0.5F * var21, 0.25F * var21, 0.8F * var21, 1.0F);
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
					EaglerAdapter.glPushMatrix();
					float var22 = 0.125F;
					EaglerAdapter.glScalef(var22, var22, var22);
					float var23 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
					EaglerAdapter.glTranslatef(var23, 0.0F, 0.0F);
					EaglerAdapter.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
					ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
					EaglerAdapter.glPopMatrix();
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glScalef(var22, var22, var22);
					var23 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
					EaglerAdapter.glTranslatef(-var23, 0.0F, 0.0F);
					EaglerAdapter.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
					ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
					EaglerAdapter.glPopMatrix();
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
					EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
					EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
				}
				EaglerAdapter.flipLightMatrix();
			}

			EaglerAdapter.glPopMatrix();
		} else {
			for (int var25 = 0; var25 < par3; ++var25) {
				EaglerAdapter.glPushMatrix();

				if (var25 > 0) {
					var17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var27 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					EaglerAdapter.glTranslatef(var17, var26, var27);
				}

				if (!renderInFrame) {
					EaglerAdapter.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				}

				if (!renderInFrame) EaglerAdapter.flipLightMatrix();
				EaglerAdapter.glColor4f(par5, par6, par7, 1.0F);
				var8.startDrawingQuads();
				var8.setNormal(0.0F, 1.0F, 0.0F);
				var8.addVertexWithUV((double) (0.0F - var14), (double) (0.0F - var15), 0.0D, (double) var9, (double) var12);
				var8.addVertexWithUV((double) (var13 - var14), (double) (0.0F - var15), 0.0D, (double) var10, (double) var12);
				var8.addVertexWithUV((double) (var13 - var14), (double) (1.0F - var15), 0.0D, (double) var10, (double) var11);
				var8.addVertexWithUV((double) (0.0F - var14), (double) (1.0F - var15), 0.0D, (double) var9, (double) var11);
				var8.draw();
				if (!renderInFrame) EaglerAdapter.flipLightMatrix();
				EaglerAdapter.glPopMatrix();
			}
		}
	}

	/**
	 * Renders the item's icon or block into the UI at the specified position.
	 */
	public void renderItemIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		int var6 = par3ItemStack.itemID;
		int var7 = par3ItemStack.getItemDamage();
		Icon var8 = par3ItemStack.getIconIndex();
		float var12;
		float var13;
		float var18;
		
		EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
		
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		if (par3ItemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.blocksList[var6].getRenderType())) {
			terrain.bindTexture();
			Block var15 = Block.blocksList[var6];
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef((float) (par4 - 2), (float) (par5 + 3), -3.0F + this.zLevel);
			EaglerAdapter.glScalef(10.0F, 10.0F, 10.0F);
			EaglerAdapter.glTranslatef(1.0F, 0.5F, 1.0F);
			EaglerAdapter.glScalef(1.0F, 1.0F, -1.0F);
			EaglerAdapter.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int var17 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
			var18 = (float) (var17 >> 16 & 255) / 255.0F;
			var12 = (float) (var17 >> 8 & 255) / 255.0F;
			var13 = (float) (var17 & 255) / 255.0F;

			if (this.renderWithColor) {
				EaglerAdapter.glColor4f(var18, var12, var13, 1.0F);
			}

			EaglerAdapter.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.itemRenderBlocks.useInventoryTint = this.renderWithColor;
			this.itemRenderBlocks.renderBlockAsItem(var15, var7, 1.0F);
			this.itemRenderBlocks.useInventoryTint = true;
			EaglerAdapter.glPopMatrix();
		} else {
			int var9;

			if (Item.itemsList[var6].requiresMultipleRenderPasses()) {
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				items.bindTexture();

				for (var9 = 0; var9 <= 1; ++var9) {
					Icon var10 = Item.itemsList[var6].getIconFromDamageForRenderPass(var7, var9);
					int var11 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, var9);
					var12 = (float) (var11 >> 16 & 255) / 255.0F;
					var13 = (float) (var11 >> 8 & 255) / 255.0F;
					float var14 = (float) (var11 & 255) / 255.0F;

					if (this.renderWithColor) {
						EaglerAdapter.glColor4f(var12, var13, var14, 1.0F);
					}

					this.renderIcon(par4, par5, var10, 16, 16);
				}

				EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			} else {
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);

				if (par3ItemStack.getItemSpriteNumber() == 0) {
					terrain.bindTexture();
				} else {
					items.bindTexture();
				}

				if (var8 == null) {
					var8 = par2RenderEngine.getMissingIcon(par3ItemStack.getItemSpriteNumber());
				}

				var9 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
				float var16 = (float) (var9 >> 16 & 255) / 255.0F;
				var18 = (float) (var9 >> 8 & 255) / 255.0F;
				var12 = (float) (var9 & 255) / 255.0F;

				if (this.renderWithColor) {
					EaglerAdapter.glColor4f(var16, var18, var12, 1.0F);
				}

				this.renderIcon(par4, par5, var8, 16, 16);
				EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			}
		}
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		
		EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
	}

	/**
	 * Render the item's icon or block into the GUI, including the glint effect.
	 */
	public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		if (par3ItemStack != null) {
			this.renderItemIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5);

			if (par3ItemStack.hasEffect()) {
				EaglerAdapter.glDepthFunc(EaglerAdapter.GL_GREATER);
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glDepthMask(false);
				glint.bindTexture();
				this.zLevel -= 50.0F;
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_DST_COLOR, EaglerAdapter.GL_DST_COLOR);
				EaglerAdapter.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
				this.renderGlint(par4 * 431278612 + par5 * 32178161, par4 - 2, par5 - 2, 20, 20);
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glDepthMask(true);
				this.zLevel += 50.0F;
				EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
				EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
			}
		}
	}

	private void renderGlint(int par1, int par2, int par3, int par4, int par5) {
		for (int var6 = 0; var6 < 2; ++var6) {
			if (var6 == 0) {
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_COLOR, EaglerAdapter.GL_ONE);
			}

			if (var6 == 1) {
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_COLOR, EaglerAdapter.GL_ONE);
			}

			float var7 = 0.00390625F;
			float var8 = 0.00390625F;
			float var9 = (float) (Minecraft.getSystemTime() % (long) (3000 + var6 * 1873)) / (3000.0F + (float) (var6 * 1873)) * 256.0F;
			float var10 = 0.0F;
			Tessellator var11 = Tessellator.instance;
			float var12 = 4.0F;

			if (var6 == 1) {
				var12 = -1.0F;
			}

			var11.startDrawingQuads();
			var11.addVertexWithUV((double) (par2 + 0), (double) (par3 + par5), (double) this.zLevel, (double) ((var9 + (float) par5 * var12) * var7), (double) ((var10 + (float) par5) * var8));
			var11.addVertexWithUV((double) (par2 + par4), (double) (par3 + par5), (double) this.zLevel, (double) ((var9 + (float) par4 + (float) par5 * var12) * var7), (double) ((var10 + (float) par5) * var8));
			var11.addVertexWithUV((double) (par2 + par4), (double) (par3 + 0), (double) this.zLevel, (double) ((var9 + (float) par4) * var7), (double) ((var10 + 0.0F) * var8));
			var11.addVertexWithUV((double) (par2 + 0), (double) (par3 + 0), (double) this.zLevel, (double) ((var9 + 0.0F) * var7), (double) ((var10 + 0.0F) * var8));
			var11.draw();
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_COLOR, EaglerAdapter.GL_ONE_MINUS_SRC_COLOR);
		}
	}

	/**
	 * Renders the item's overlay information. Examples being stack count or damage
	 * on top of the item's image at the specified position.
	 */
	public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		this.renderItemOverlayIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5, (String) null);
	}

	public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5, String par6Str) {
		if (par3ItemStack != null) {
			if (par3ItemStack.stackSize > 1 || par6Str != null) {
				String var7 = par6Str == null ? String.valueOf(par3ItemStack.stackSize) : par6Str;
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
				par1FontRenderer.drawStringWithShadow(var7, par4 + 19 - 2 - par1FontRenderer.getStringWidth(var7), par5 + 6 + 3, 16777215);
				EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
			}

			if (par3ItemStack.isItemDamaged()) {
				int var12 = (int) Math.round(13.0D - (double) par3ItemStack.getItemDamageForDisplay() * 13.0D / (double) par3ItemStack.getMaxDamage());
				int var8 = (int) Math.round(255.0D - (double) par3ItemStack.getItemDamageForDisplay() * 255.0D / (double) par3ItemStack.getMaxDamage());
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
				EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
				Tessellator var9 = Tessellator.instance;
				int var10 = 255 - var8 << 16 | var8 << 8;
				int var11 = (255 - var8) / 4 << 16 | 16128;
				this.renderQuad(var9, par4 + 2, par5 + 13, 13, 2, 0);
				this.renderQuad(var9, par4 + 2, par5 + 13, 12, 1, var11);
				this.renderQuad(var9, par4 + 2, par5 + 13, var12, 1, var10);
				EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
				EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
				EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Adds a quad to the tesselator at the specified position with the set width
	 * and height and color. Args: tessellator, x, y, width, height, color
	 */
	private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setColorOpaque_I(par6);
		par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + 0), 0.0D);
		par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + par5), 0.0D);
		par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + par5), 0.0D);
		par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + 0), 0.0D);
		par1Tessellator.draw();
	}

	public void renderIcon(int par1, int par2, Icon par3Icon, int par4, int par5) {
		Tessellator var6 = Tessellator.instance;
		var6.startDrawingQuads();
		var6.addVertexWithUV((double) (par1 + 0), (double) (par2 + par5), (double) this.zLevel, (double) par3Icon.getMinU(), (double) par3Icon.getMaxV());
		var6.addVertexWithUV((double) (par1 + par4), (double) (par2 + par5), (double) this.zLevel, (double) par3Icon.getMaxU(), (double) par3Icon.getMaxV());
		var6.addVertexWithUV((double) (par1 + par4), (double) (par2 + 0), (double) this.zLevel, (double) par3Icon.getMaxU(), (double) par3Icon.getMinV());
		var6.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), (double) this.zLevel, (double) par3Icon.getMinU(), (double) par3Icon.getMinV());
		var6.draw();
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
		this.doRenderItem((EntityItem) par1Entity, par2, par4, par6, par8, par9);
	}
}
