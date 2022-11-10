package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.EnumBrowser;
import net.lax1dude.eaglercraft.IntegratedServer;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.lax1dude.eaglercraft.glemu.EffectPipeline;
import net.minecraft.client.Minecraft;

public class GuiIngame extends Gui {
	private static RenderItem itemRenderer = null;
	private final EaglercraftRandom rand = new EaglercraftRandom();
	private final Minecraft mc;

	/** ChatGUI instance that retains all previous chat data */
	private final GuiNewChat persistantChatGUI;
	private int updateCounter = 0;

	/** The string specifying which record music is playing */
	private String recordPlaying = "";

	/** How many ticks the record playing message will be displayed */
	private int recordPlayingUpFor = 0;
	private boolean recordIsPlaying = false;

	/** Previous frame vignette brightness (slowly changes by 1% each frame) */
	public float prevVignetteBrightness = 1.0F;

	/** Remaining ticks the item highlight should be visible */
	private int remainingHighlightTicks;

	/** The ItemStack that is currently being highlighted */
	private ItemStack highlightingItemStack;

	public GuiIngame(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
		this.persistantChatGUI = new GuiNewChat(par1Minecraft);
	}
	
	private static final TextureLocation tex_gui = new TextureLocation("/gui/gui.png");
	private static final TextureLocation tex_icons = new TextureLocation("/gui/icons.png");
	private static final TextureLocation tex_pumpkin = new TextureLocation("%blur%/misc/pumpkinblur.png");
	private static final TextureLocation tex_vigg = new TextureLocation("%blur%/misc/vignette.png");
	private static final TextureLocation terrain = new TextureLocation("/terrain.png");

	/**
	 * Render the ingame overlay with quick icon bar, ...
	 */
	public void renderGameOverlay(float par1, boolean par2, int par3, int par4) {
		ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int var6 = var5.getScaledWidth();
		int var7 = var5.getScaledHeight();
		FontRenderer var8 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);

		if (Minecraft.isFancyGraphicsEnabled()) {
			this.renderVignette(this.mc.thePlayer.getBrightness(par1), var6, var7);
		} else {
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		}

		ItemStack var9 = this.mc.thePlayer.inventory.armorItemInSlot(3);

		if (this.mc.gameSettings.thirdPersonView == 0 && var9 != null && var9.itemID == Block.pumpkin.blockID) {
			this.renderPumpkinBlur(var6, var7);
		}

		if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
			float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;

			if (var10 > 0.0F) {
				this.renderPortalOverlay(var10, var6, var7);
			}
		}

		boolean var11;
		int var12;
		int var13;
		int var16;
		int var17;
		int var18;
		int var20;
		int var22;
		int var23;
		int var24;
		int var26;
		byte var27;
		int var47;
		int var52;

		if (!this.mc.playerController.enableEverythingIsScrewedUpMode()) {
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tex_gui.bindTexture();
			InventoryPlayer var31 = this.mc.thePlayer.inventory;
			this.zLevel = -90.0F;
			this.drawTexturedModalRect(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
			this.drawTexturedModalRect(var6 / 2 - 91 - 1 + var31.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
			tex_icons.bindTexture();
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE_MINUS_DST_COLOR, EaglerAdapter.GL_ONE_MINUS_SRC_COLOR);

			float i = mc.entityRenderer.startup / 900.0f - 0.5f;
			if(i > 1.0f) i = 1.0f;
			if(i < 0.0f) i = 0.0f;
			float i2 = i * i;
			if(i2 > 0.0f) {
				float f = (float)((System.currentTimeMillis() % 1000000l) * 0.0002);
				f += MathHelper.sin(f * 5.0f) * 0.2f;
				i2 *= MathHelper.sin(f) + MathHelper.sin(f * 1.5f + 0.6f) + MathHelper.sin(f * 0.7f + 1.7f) +
						MathHelper.sin(f * 3.0f + 3.0f);
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(var6 / 2, var7 / 2, 0.0f);
				EaglerAdapter.glRotatef(i2 * 5.0f, 0.0f, 0.0f, 1.0f);
				this.drawTexturedModalRect(-7, -7, 0, 0, 16, 16);
				EaglerAdapter.glPopMatrix();
			}else {
				this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
			}
			
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			var11 = this.mc.thePlayer.hurtResistantTime / 3 % 2 == 1;

			if (this.mc.thePlayer.hurtResistantTime < 10) {
				var11 = false;
			}

			var12 = this.mc.thePlayer.getHealth();
			var13 = this.mc.thePlayer.prevHealth;
			this.rand.setSeed((long) (this.updateCounter * 312871));
			boolean var14 = false;
			FoodStats var15 = this.mc.thePlayer.getFoodStats();
			var16 = var15.getFoodLevel();
			var17 = var15.getPrevFoodLevel();
			this.mc.mcProfiler.startSection("bossHealth");
			this.renderBossHealth();
			this.mc.mcProfiler.endSection();
			int var19;

			if (this.mc.playerController.shouldDrawHUD()) {
				var18 = var6 / 2 - 91;
				var19 = var6 / 2 + 91;
				this.mc.mcProfiler.startSection("expBar");
				var20 = this.mc.thePlayer.xpBarCap();

				if (var20 > 0) {
					short var21 = 182;
					var22 = (int) (this.mc.thePlayer.experience * (float) (var21 + 1));
					var23 = var7 - 32 + 3;
					this.drawTexturedModalRect(var18, var23, 0, 64, var21, 5);

					if (var22 > 0) {
						this.drawTexturedModalRect(var18, var23, 0, 69, var22, 5);
					}
				}

				var47 = var7 - 39;
				var22 = var47 - 10;
				var23 = this.mc.thePlayer.getTotalArmorValue();
				var24 = -1;

				if (this.mc.thePlayer.isPotionActive(Potion.regeneration)) {
					var24 = this.updateCounter % 25;
				}

				this.mc.mcProfiler.endStartSection("healthArmor");
				int var25;
				int var28;
				int var29;

				for (var25 = 0; var25 < 10; ++var25) {
					if (var23 > 0) {
						var26 = var18 + var25 * 8;

						if (var25 * 2 + 1 < var23) {
							this.drawTexturedModalRect(var26, var22, 34, 9, 9, 9);
						}

						if (var25 * 2 + 1 == var23) {
							this.drawTexturedModalRect(var26, var22, 25, 9, 9, 9);
						}

						if (var25 * 2 + 1 > var23) {
							this.drawTexturedModalRect(var26, var22, 16, 9, 9, 9);
						}
					}

					var26 = 16;

					if (this.mc.thePlayer.isPotionActive(Potion.poison)) {
						var26 += 36;
					} else if (this.mc.thePlayer.isPotionActive(Potion.wither)) {
						var26 += 72;
					}

					var27 = 0;

					if (var11) {
						var27 = 1;
					}

					var28 = var18 + var25 * 8;
					var29 = var47;

					if (var12 <= 4) {
						var29 = var47 + this.rand.nextInt(2);
					}

					if (var25 == var24) {
						var29 -= 2;
					}

					byte var30 = 0;

					if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
						var30 = 5;
					}

					this.drawTexturedModalRect(var28, var29, 16 + var27 * 9, 9 * var30, 9, 9);

					if (var11) {
						if (var25 * 2 + 1 < var13) {
							this.drawTexturedModalRect(var28, var29, var26 + 54, 9 * var30, 9, 9);
						}

						if (var25 * 2 + 1 == var13) {
							this.drawTexturedModalRect(var28, var29, var26 + 63, 9 * var30, 9, 9);
						}
					}

					if (var25 * 2 + 1 < var12) {
						this.drawTexturedModalRect(var28, var29, var26 + 36, 9 * var30, 9, 9);
					}

					if (var25 * 2 + 1 == var12) {
						this.drawTexturedModalRect(var28, var29, var26 + 45, 9 * var30, 9, 9);
					}
				}

				this.mc.mcProfiler.endStartSection("food");

				for (var25 = 0; var25 < 10; ++var25) {
					var26 = var47;
					var52 = 16;
					byte var53 = 0;

					if (this.mc.thePlayer.isPotionActive(Potion.hunger)) {
						var52 += 36;
						var53 = 13;
					}

					if (this.mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (var16 * 3 + 1) == 0) {
						var26 = var47 + (this.rand.nextInt(3) - 1);
					}

					if (var14) {
						var53 = 1;
					}

					var29 = var19 - var25 * 8 - 9;
					this.drawTexturedModalRect(var29, var26, 16 + var53 * 9, 27, 9, 9);

					if (var14) {
						if (var25 * 2 + 1 < var17) {
							this.drawTexturedModalRect(var29, var26, var52 + 54, 27, 9, 9);
						}

						if (var25 * 2 + 1 == var17) {
							this.drawTexturedModalRect(var29, var26, var52 + 63, 27, 9, 9);
						}
					}

					if (var25 * 2 + 1 < var16) {
						this.drawTexturedModalRect(var29, var26, var52 + 36, 27, 9, 9);
					}

					if (var25 * 2 + 1 == var16) {
						this.drawTexturedModalRect(var29, var26, var52 + 45, 27, 9, 9);
					}
				}

				this.mc.mcProfiler.endStartSection("air");

				if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
					var25 = this.mc.thePlayer.getAir();
					var26 = MathHelper.ceiling_double_int((double) (var25 - 2) * 10.0D / 300.0D);
					var52 = MathHelper.ceiling_double_int((double) var25 * 10.0D / 300.0D) - var26;

					for (var28 = 0; var28 < var26 + var52; ++var28) {
						if (var28 < var26) {
							this.drawTexturedModalRect(var19 - var28 * 8 - 9, var22, 16, 18, 9, 9);
						} else {
							this.drawTexturedModalRect(var19 - var28 * 8 - 9, var22, 25, 18, 9, 9);
						}
					}
				}

				this.mc.mcProfiler.endSection();
			}

			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			this.mc.mcProfiler.startSection("actionBar");
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			RenderHelper.enableGUIStandardItemLighting2();

			for (var18 = 0; var18 < 9; ++var18) {
				var19 = var6 / 2 - 90 + var18 * 20 + 2;
				var20 = var7 - 16 - 3;
				this.renderInventorySlot(var18, var19, var20, par1);
			}

			RenderHelper.disableStandardItemLighting();
			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
			this.mc.mcProfiler.endSection();
		}

		float var33;

		if (this.mc.thePlayer.getSleepTimer() > 0) {
			this.mc.mcProfiler.startSection("sleep");
			EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
			EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
			int var32 = this.mc.thePlayer.getSleepTimer();
			var33 = (float) var32 / 100.0F;

			if (var33 > 1.0F) {
				var33 = 1.0F - (float) (var32 - 100) / 10.0F;
			}

			var12 = (int) (220.0F * var33) << 24 | 1052704;
			drawRect(0, 0, var6, var7, var12);
			EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
			EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
			this.mc.mcProfiler.endSection();
		}

		int var36;
		int var40;

		if (this.mc.playerController.func_78763_f() && this.mc.thePlayer.experienceLevel > 0) {
			this.mc.mcProfiler.startSection("expLevel");
			var11 = false;
			var12 = var11 ? 16777215 : 8453920;
			String var34 = "" + this.mc.thePlayer.experienceLevel;
			var36 = (var6 - var8.getStringWidth(var34)) / 2;
			var40 = var7 - 31 - 4;
			var8.drawString(var34, var36 + 1, var40, 0);
			var8.drawString(var34, var36 - 1, var40, 0);
			var8.drawString(var34, var36, var40 + 1, 0);
			var8.drawString(var34, var36, var40 - 1, 0);
			var8.drawString(var34, var36, var40, var12);
			this.mc.mcProfiler.endSection();
		}

		String var35;

		if (this.mc.gameSettings.heldItemTooltips) {
			this.mc.mcProfiler.startSection("toolHighlight");

			if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
				var35 = this.highlightingItemStack.getDisplayName();
				var12 = (var6 - var8.getStringWidth(var35)) / 2;
				var13 = var7 - 59;

				if (!this.mc.playerController.shouldDrawHUD()) {
					var13 += 14;
				}

				var36 = (int) ((float) this.remainingHighlightTicks * 256.0F / 10.0F);

				if (var36 > 255) {
					var36 = 255;
				}

				if (var36 > 0) {
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
					var8.drawStringWithShadow(var35, var12, var13, 16777215 + (var36 << 24));
					EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glPopMatrix();
				}
			}

			this.mc.mcProfiler.endSection();
		}

		if (this.mc.isDemo()) {
			this.mc.mcProfiler.startSection("demo");
			var35 = "";

			if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
				var35 = StatCollector.translateToLocal("demo.demoExpired");
			} else {
				var35 = String.format(StatCollector.translateToLocal("demo.remainingTime"), new Object[] { StringUtils.ticksToElapsedTime((int) (120500L - this.mc.theWorld.getTotalWorldTime())) });
			}

			var12 = var8.getStringWidth(var35);
			var8.drawStringWithShadow(var35, var6 - var12 - 10, 5, 16777215);
			this.mc.mcProfiler.endSection();
		}
		
		this.mc.debug = "" + Minecraft.debugFPS + " fps, " + Minecraft.debugChunkUpdates + " chunk updates";

		if (this.mc.gameSettings.showDebugInfo) {
			this.mc.mcProfiler.startSection("debug");
			EaglerAdapter.glPushMatrix();
			var8.drawStringWithShadow("minecraft 1.5.2 (" + this.mc.debug + ")", 2, 2, 16777215);
			var8.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 16777215);
			var8.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 16777215);
			var8.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 16777215);
			var8.drawStringWithShadow(this.mc.getWorldProviderName(), 2, 42, 16777215);
			String var45;
			int offset = 0;
			if(EaglerAdapter._wisWebGL()) {
				var45 = "Game is running in a browser (" + EnumBrowser.getBrowser().toString() + ")";
				this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, 2, 14737632);
				offset = -10;
			}else {
				long var39;
				long var37;
				long var41;
				var39 = EaglerAdapter.maxMemory();
				var37 = EaglerAdapter.totalMemory();
				var41 = EaglerAdapter.freeMemory();
				long var44 = var37 - var41;
				var45 = "Used memory: " + var44 * 100L / var39 + "% (" + var44 / 1024L / 1024L + "MB) of " + var39 / 1024L / 1024L + "MB";
				this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, 2, 14737632);
				var45 = "Allocated memory: " + var37 * 100L / var39 + "% (" + var37 / 1024L / 1024L + "MB)";
				this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, 12, 14737632);
				offset = 0;
			}
			
			this.drawString(var8, "Player is not using an xbox live account", 2, 64, 0xFFBBBB);
			this.drawString(var8, "Using cracked server @ " + mc.getServerURI(), 2, 73, 0xFFBBBB);
			var47 = MathHelper.floor_double(this.mc.thePlayer.posX);
			var22 = MathHelper.floor_double(this.mc.thePlayer.posY);
			var23 = MathHelper.floor_double(this.mc.thePlayer.posZ);
			this.drawString(var8, "x: "+doubleToShorterString(this.mc.thePlayer.posX)+" ("+var47+") // c: "+(var47 >> 4)+" ("+(var47 & 15)+")", 2, 92, 14737632);
			this.drawString(var8, "y: "+doubleToShorterString(this.mc.thePlayer.posY)+" ("+var22+") (feet pos)", 2, 100, 14737632);
			this.drawString(var8, "z: "+doubleToShorterString(this.mc.thePlayer.posZ)+" ("+var23+") // c: "+(var23 >> 4)+" ("+(var23 & 15)+")", 2, 108, 14737632);
			var24 = MathHelper.floor_double((double) (this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			this.drawString(var8, "f: " + var24 + " (" + Direction.directions[var24] + ") / " + MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw), 2, 116, 14737632);
			this.drawString(var8, "Press F+6 to " + (mc.gameSettings.showCoordinates ? "disable" : "enable") + " showing coords in ingame GUI", 2, 135, 0xFFFFFFFF);

			if(IntegratedServer.isWorldRunning()) {
				this.drawString(var8, "IntegratedServer is running", 2, 154, 14737632);
				List<String> info = IntegratedServer.getTPS();
				for(int i = 0, size = info.size(); i < size; ++i) {
					this.drawString(var8, info.get(i), 2, 173 + i * 9, 14737632);
				}
			}
			
			//this.drawString(var8, String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", new Object[] { Float.valueOf(this.mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(this.mc.thePlayer.capabilities.getFlySpeed()),
			//		Boolean.valueOf(this.mc.thePlayer.onGround), Integer.valueOf(this.mc.theWorld.getHeightValue(var47, var23)) }), 2, 104, 14737632);
			var45 = "opengl emulator status - v1.0";
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 36, 14737632);
			var45 = "lists: "+EaglerAdapter.getDisplayListCount()+", upload: "+(EaglerAdapter.getBitsPerSecond() / 1000000)+"mbps";
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 46, 14737632);
			var45 = "verts: "+(EaglerAdapter.getVertexesPerSecond() / 1000)+"k, triangles: "+(EaglerAdapter.getTrianglesPerSecond() / 1000)+"k";
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 56, 14737632);
			var45 = "rendering backend: "+(EaglerAdapter.isWebGL ? "webgl20" : "opengl30");
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 66, 14737632);
			var45 = "glsl "+(EaglerAdapter._wgetShaderHeader() +", 32 bytes/vert");
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 76, 14737632);
			var45 = "Bound Shaders:";
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 100, 14737632);
			var45 = "/glsl/core.glsl";
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 110, 14737632);
			var45 = "/glsl/occl.glsl";
			this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, offset + 120, 14737632);
			EaglerAdapter.glPopMatrix();
			this.mc.mcProfiler.endSection();
		}else {
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
			var8.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoShort(), 2, 2, 16777215);

			if(mc.gameSettings.showCoordinates) {
				var8.drawStringWithShadow("x: "+MathHelper.floor_double(this.mc.thePlayer.posX)+", y: "+MathHelper.floor_double(this.mc.thePlayer.posY)+", z: "+MathHelper.floor_double(this.mc.thePlayer.posZ), 2, 12, 16777215);
			}
			
			if(IntegratedServer.isWorldRunning()) {
				String strr = "Playing Singleplayer";
				var8.drawStringWithShadow(strr, (int)(var6 / 0.75f) - var8.getStringWidth(strr) - 2, 2, 0xFFFFAA);
				List<String> info = IntegratedServer.getTPS();
				for(int i = 0, size = info.size(); i < size; ++i) {
					var8.drawStringWithShadow(info.get(i), (int)(var6 / 0.75f) - var8.getStringWidth(info.get(i)) - 2, 12 + i * 10, 14737632);
				}
			}
			/*
			if(mc.currentScreen == null || !(mc.currentScreen instanceof GuiIngameMenu)) {
				int offset = this.mc.isChatOpen() ? 135 : 0;
				int offset2 = this.mc.isChatOpen() ? 4 : 0;
				String str = "not using an xbox live account";
				var8.drawStringWithShadow(str, (var6 * 4 / 3) - 2 - var8.getStringWidth(str) - offset, 2 + offset2, 0xFFDDDD);
				str = "eaglercraft: " + ConfigConstants.version;
				var8.drawStringWithShadow(str, (var6 * 4 / 3) - 2 - var8.getStringWidth(str) - offset, 12 + offset2, 14737632);
			}
			*/
			EaglerAdapter.glPopMatrix();
		}

		if (this.recordPlayingUpFor > 0) {
			this.mc.mcProfiler.startSection("overlayMessage");
			var33 = (float) this.recordPlayingUpFor - par1;
			var12 = (int) (var33 * 256.0F / 20.0F);

			if (var12 > 255) {
				var12 = 255;
			}

			if (var12 > 0) {
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef((float) (var6 / 2), (float) (var7 - 48), 0.0F);
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
				var13 = 16777215;

				if (this.recordIsPlaying) {
					var13 = BiomeGenBase.HSBtoRGB(var33 / 50.0F, 0.7F, 0.6F) & 16777215;
				}
				
				String str = this.recordPlaying + " (jukebox not enabled)";
				
				var8.drawString(this.recordPlaying, -var8.getStringWidth(this.recordPlaying) / 2, -4, var13 + (var12 << 24));
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glPopMatrix();
			}

			this.mc.mcProfiler.endSection();
		}

		ScoreObjective var42 = this.mc.theWorld.getScoreboard().func_96539_a(1);

		if (var42 != null) {
			this.func_96136_a(var42, var7, var6, var8);
		}

		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef(0.0F, (float) (var7 - 48), 0.0F);
		this.mc.mcProfiler.startSection("chat");
		this.persistantChatGUI.drawChat(this.updateCounter);
		this.mc.mcProfiler.endSection();
		EaglerAdapter.glPopMatrix();
		var42 = this.mc.theWorld.getScoreboard().func_96539_a(0);

		if (this.mc.gameSettings.keyBindPlayerList.pressed && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.playerInfoList.size() > 1 || var42 != null)) {
			this.mc.mcProfiler.startSection("playerList");
			NetClientHandler var38 = this.mc.thePlayer.sendQueue;
			List var43 = var38.playerInfoList;
			var36 = var38.currentServerMaxPlayers;
			var40 = var36;

			for (var16 = 1; var40 > 20; var40 = (var36 + var16 - 1) / var16) {
				++var16;
			}

			var17 = 300 / var16;

			if (var17 > 150) {
				var17 = 150;
			}

			var18 = (var6 - var16 * var17) / 2;
			byte var46 = 10;
			drawRect(var18 - 1, var46 - 1, var18 + var17 * var16, var46 + 9 * var40, Integer.MIN_VALUE);

			for (var20 = 0; var20 < var36; ++var20) {
				var47 = var18 + var20 % var16 * var17;
				var22 = var46 + var20 / var16 * 9;
				drawRect(var47, var22, var47 + var17 - 1, var22 + 8, 553648127);
				EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);

				if (var20 < var43.size()) {
					GuiPlayerInfo var48 = (GuiPlayerInfo) var43.get(var20);
					ScorePlayerTeam var49 = this.mc.theWorld.getScoreboard().getPlayersTeam(var48.name);
					String var51 = ScorePlayerTeam.func_96667_a(var49, var48.name);
					var8.drawStringWithShadow(var51, var47, var22, 16777215);

					if (var42 != null) {
						var26 = var47 + var8.getStringWidth(var51) + 5;
						var52 = var47 + var17 - 12 - 5;

						if (var52 - var26 > 5) {
							Score var55 = var42.getScoreboard().func_96529_a(var48.name, var42);
							String var57 = EnumChatFormatting.YELLOW + "" + var55.func_96652_c();
							var8.drawStringWithShadow(var57, var52 - var8.getStringWidth(var57), var22, 16777215);
						}
					}

					EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					this.mc.renderEngine.bindTexture("/gui/icons.png");
					byte var54 = 0;
					boolean var56 = false;

					if (var48.responseTime < 0) {
						var27 = 5;
					} else if (var48.responseTime < 150) {
						var27 = 0;
					} else if (var48.responseTime < 300) {
						var27 = 1;
					} else if (var48.responseTime < 600) {
						var27 = 2;
					} else if (var48.responseTime < 1000) {
						var27 = 3;
					} else {
						var27 = 4;
					}

					this.zLevel += 100.0F;
					this.drawTexturedModalRect(var47 + var17 - 12, var22, 0 + var54 * 10, 176 + var27 * 8, 10, 8);
					this.zLevel -= 100.0F;
				}
			}
		}

		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
	}
	
	private static String doubleToShorterString(double d) {
		String s = Double.toString(d);
		int i = s.indexOf('.');
		if(s.length() - i > 6) {
			s = s.substring(0, i + 6);
		}
		return s;
	}

	private void func_96136_a(ScoreObjective par1ScoreObjective, int par2, int par3, FontRenderer par4FontRenderer) {
		Scoreboard var5 = par1ScoreObjective.getScoreboard();
		Collection var6 = var5.func_96534_i(par1ScoreObjective);

		if (var6.size() <= 15) {
			int var7 = par4FontRenderer.getStringWidth(par1ScoreObjective.getDisplayName());
			String var11;

			for (Iterator var8 = var6.iterator(); var8.hasNext(); var7 = Math.max(var7, par4FontRenderer.getStringWidth(var11))) {
				Score var9 = (Score) var8.next();
				ScorePlayerTeam var10 = var5.getPlayersTeam(var9.func_96653_e());
				var11 = ScorePlayerTeam.func_96667_a(var10, var9.func_96653_e()) + ": " + EnumChatFormatting.RED + var9.func_96652_c();
			}

			int var22 = var6.size() * par4FontRenderer.FONT_HEIGHT;
			int var23 = par2 / 2 + var22 / 3;
			byte var24 = 3;
			int var25 = par3 - var7 - var24;
			int var12 = 0;
			Iterator var13 = var6.iterator();

			while (var13.hasNext()) {
				Score var14 = (Score) var13.next();
				++var12;
				ScorePlayerTeam var15 = var5.getPlayersTeam(var14.func_96653_e());
				String var16 = ScorePlayerTeam.func_96667_a(var15, var14.func_96653_e());
				String var17 = EnumChatFormatting.RED + "" + var14.func_96652_c();
				int var19 = var23 - var12 * par4FontRenderer.FONT_HEIGHT;
				int var20 = par3 - var24 + 2;
				drawRect(var25 - 2, var19, var20, var19 + par4FontRenderer.FONT_HEIGHT, 1342177280);
				par4FontRenderer.drawString(var16, var25, var19, 553648127);
				par4FontRenderer.drawString(var17, var20 - par4FontRenderer.getStringWidth(var17), var19, 553648127);

				if (var12 == var6.size()) {
					String var21 = par1ScoreObjective.getDisplayName();
					drawRect(var25 - 2, var19 - par4FontRenderer.FONT_HEIGHT - 1, var20, var19 - 1, 1610612736);
					drawRect(var25 - 2, var19 - 1, var20, var19, 1342177280);
					par4FontRenderer.drawString(var21, var25 + var7 / 2 - par4FontRenderer.getStringWidth(var21) / 2, var19 - par4FontRenderer.FONT_HEIGHT, 553648127);
				}
			}
		}
	}

	/**
	 * Renders dragon's (boss) health on the HUD
	 */
	private void renderBossHealth() {
		if (BossStatus.bossName != null && BossStatus.statusBarLength > 0) {
			--BossStatus.statusBarLength;
			FontRenderer var1 = this.mc.fontRenderer;
			ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int var3 = var2.getScaledWidth();
			short var4 = 182;
			int var5 = var3 / 2 - var4 / 2;
			int var6 = (int) (BossStatus.healthScale * (float) (var4 + 1));
			byte var7 = 12;
			this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
			this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);

			if (var6 > 0) {
				this.drawTexturedModalRect(var5, var7, 0, 79, var6, 5);
			}

			String var8 = BossStatus.bossName;
			var1.drawStringWithShadow(var8, var3 / 2 - var1.getStringWidth(var8) / 2, var7 - 10, 16777215);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tex_icons.bindTexture();
		}
	}

	private void renderPumpkinBlur(int par1, int par2) {
		EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
		EaglerAdapter.glDepthMask(false);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		tex_pumpkin.bindTexture();
		Tessellator var3 = Tessellator.instance;
		var3.startDrawingQuads();
		var3.addVertexWithUV(0.0D, (double) par2, -90.0D, 0.0D, 1.0D);
		var3.addVertexWithUV((double) par1, (double) par2, -90.0D, 1.0D, 1.0D);
		var3.addVertexWithUV((double) par1, 0.0D, -90.0D, 1.0D, 0.0D);
		var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var3.draw();
		EaglerAdapter.glDepthMask(true);
		EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders the vignette. Args: vignetteBrightness, width, height
	 */
	private void renderVignette(float par1, int par2, int par3) {
		par1 = 1.0F - par1 * 0.5f;

		if (par1 < 0.0F) {
			par1 = 0.0F;
		}

		if (par1 > 1.0F) {
			par1 = 1.0F;
		}

		this.prevVignetteBrightness = (float) ((double) this.prevVignetteBrightness + (double) (par1 - this.prevVignetteBrightness) * 0.01D);
		EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
		EaglerAdapter.glDepthMask(false);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ZERO, EaglerAdapter.GL_ONE_MINUS_SRC_COLOR);
		EaglerAdapter.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
		tex_vigg.bindTexture();
		Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		var4.addVertexWithUV(0.0D, (double) par3, -90.0D, 0.0D, 1.0D);
		var4.addVertexWithUV((double) par2, (double) par3, -90.0D, 1.0D, 1.0D);
		var4.addVertexWithUV((double) par2, 0.0D, -90.0D, 1.0D, 0.0D);
		var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var4.draw();
		EaglerAdapter.glDepthMask(true);
		EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Renders the portal overlay. Args: portalStrength, width, height
	 */
	private void renderPortalOverlay(float par1, int par2, int par3) {
		if (par1 < 1.0F) {
			par1 *= par1;
			par1 *= par1;
			par1 = par1 * 0.8F + 0.2F;
		}

		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
		EaglerAdapter.glDepthMask(false);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, par1);
		terrain.bindTexture();
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
		Icon var4 = Block.portal.getBlockTextureFromSide(1);
		float var5 = var4.getMinU();
		float var6 = var4.getMinV();
		float var7 = var4.getMaxU();
		float var8 = var4.getMaxV();
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(0.0D, (double) par3, -90.0D, (double) var5, (double) var8);
		var9.addVertexWithUV((double) par2, (double) par3, -90.0D, (double) var7, (double) var8);
		var9.addVertexWithUV((double) par2, 0.0D, -90.0D, (double) var7, (double) var6);
		var9.addVertexWithUV(0.0D, 0.0D, -90.0D, (double) var5, (double) var6);
		var9.draw();
		EaglerAdapter.glDepthMask(true);
		EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders the specified item of the inventory slot at the specified location.
	 * Args: slot, x, y, partialTick
	 */
	private void renderInventorySlot(int par1, int par2, int par3, float par4) {
		ItemStack var5 = this.mc.thePlayer.inventory.mainInventory[par1];

		if (var5 != null) {
			float var6 = (float) var5.animationsToGo - par4;

			if (var6 > 0.0F) {
				EaglerAdapter.glPushMatrix();
				float var7 = 1.0F + var6 / 5.0F;
				EaglerAdapter.glTranslatef((float) (par2 + 8), (float) (par3 + 12), 0.0F);
				EaglerAdapter.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
				EaglerAdapter.glTranslatef((float) (-(par2 + 8)), (float) (-(par3 + 12)), 0.0F);
			}
			
			if(itemRenderer == null) itemRenderer = new RenderItem();

			itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, par2, par3);

			if (var6 > 0.0F) {
				EaglerAdapter.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, par2, par3);
		}
	}

	/**
	 * The update tick for the ingame UI
	 */
	public void updateTick() {
		if (this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		++this.updateCounter;

		if (this.mc.thePlayer != null) {
			ItemStack var1 = this.mc.thePlayer.inventory.getCurrentItem();

			if (var1 == null) {
				this.remainingHighlightTicks = 0;
			} else if (this.highlightingItemStack != null && var1.itemID == this.highlightingItemStack.itemID && ItemStack.areItemStackTagsEqual(var1, this.highlightingItemStack)
					&& (var1.isItemStackDamageable() || var1.getItemDamage() == this.highlightingItemStack.getItemDamage())) {
				if (this.remainingHighlightTicks > 0) {
					--this.remainingHighlightTicks;
				}
			} else {
				this.remainingHighlightTicks = 40;
			}

			this.highlightingItemStack = var1;
		}
	}

	public void setRecordPlayingMessage(String par1Str) {
		this.recordPlaying = "Now playing: " + par1Str;
		this.recordPlayingUpFor = 60;
		this.recordIsPlaying = true;
	}

	/**
	 * returns a pointer to the persistant Chat GUI, containing all previous chat
	 * messages and such
	 */
	public GuiNewChat getChatGUI() {
		return this.persistantChatGUI;
	}

	public int getUpdateCounter() {
		return this.updateCounter;
	}
}
