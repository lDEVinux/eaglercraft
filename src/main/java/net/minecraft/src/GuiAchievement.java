package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.minecraft.client.Minecraft;

public class GuiAchievement extends Gui {
	/** Holds the instance of the game (Minecraft) */
	private Minecraft theGame;

	/** Holds the latest width scaled to fit the game window. */
	private int achievementWindowWidth;

	/** Holds the latest height scaled to fit the game window. */
	private int achievementWindowHeight;
	private String achievementGetLocalText;
	private String achievementStatName;

	/** Holds the achievement that will be displayed on the GUI. */
	private Achievement theAchievement;
	private long achievementTime;

	/**
	 * Holds a instance of RenderItem, used to draw the achievement icons on screen
	 * (is based on ItemStack)
	 */
	private RenderItem itemRender;
	private boolean haveAchiement;

	public GuiAchievement(Minecraft par1Minecraft) {
		this.theGame = par1Minecraft;
		this.itemRender = new RenderItem();
	}

	/**
	 * Queue a taken achievement to be displayed.
	 */
	public void queueTakenAchievement(Achievement par1Achievement) {
		this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
		this.achievementStatName = StatCollector.translateToLocal(par1Achievement.getName());
		this.achievementTime = Minecraft.getSystemTime();
		this.theAchievement = par1Achievement;
		this.haveAchiement = false;
	}

	/**
	 * Queue a information about a achievement to be displayed.
	 */
	public void queueAchievementInformation(Achievement par1Achievement) {
		this.achievementGetLocalText = StatCollector.translateToLocal(par1Achievement.getName());
		this.achievementStatName = par1Achievement.getDescription();
		this.achievementTime = Minecraft.getSystemTime() - 2500L;
		this.theAchievement = par1Achievement;
		this.haveAchiement = true;
	}

	/**
	 * Update the display of the achievement window to match the game window.
	 */
	private void updateAchievementWindowScale() {
		EaglerAdapter.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glLoadIdentity();
		this.achievementWindowWidth = this.theGame.displayWidth;
		this.achievementWindowHeight = this.theGame.displayHeight;
		ScaledResolution var1 = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
		this.achievementWindowWidth = var1.getScaledWidth();
		this.achievementWindowHeight = var1.getScaledHeight();
		EaglerAdapter.glClear(EaglerAdapter.GL_DEPTH_BUFFER_BIT);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glOrtho(0.0F, this.achievementWindowWidth, this.achievementWindowHeight, 0.0F, 1000.0F, 3000.0F);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glTranslatef(0.0F, 0.0F, -2000.0F);
	}
	
	private static final TextureLocation bg = new TextureLocation("/achievement/bg.png");

	/**
	 * Updates the small achievement tooltip window, showing a queued achievement if
	 * is needed.
	 */
	public void updateAchievementWindow() {
		if (this.theAchievement != null && this.achievementTime != 0L) {
			double var1 = (double) (Minecraft.getSystemTime() - this.achievementTime) / 3000.0D;

			if (!this.haveAchiement && (var1 < 0.0D || var1 > 1.0D)) {
				this.achievementTime = 0L;
			} else {
				this.updateAchievementWindowScale();
				EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
				EaglerAdapter.glDepthMask(false);
				double var3 = var1 * 2.0D;

				if (var3 > 1.0D) {
					var3 = 2.0D - var3;
				}

				var3 *= 4.0D;
				var3 = 1.0D - var3;

				if (var3 < 0.0D) {
					var3 = 0.0D;
				}

				var3 *= var3;
				var3 *= var3;
				int var5 = this.achievementWindowWidth - 160;
				int var6 = 0 - (int) (var3 * 36.0D);
				EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
				bg.bindTexture();
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);

				if (this.haveAchiement) {
					this.theGame.fontRenderer.drawSplitString(this.achievementStatName, var5 + 30, var6 + 7, 120, -1);
				} else {
					this.theGame.fontRenderer.drawString(this.achievementGetLocalText, var5 + 30, var6 + 7, -256);
					this.theGame.fontRenderer.drawString(this.achievementStatName, var5 + 30, var6 + 18, -1);
				}

				RenderHelper.enableGUIStandardItemLighting2();
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
				EaglerAdapter.glEnable(EaglerAdapter.GL_COLOR_MATERIAL);
				this.itemRender.renderItemAndEffectIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, this.theAchievement.theItemStack, var5 + 8, var6 + 8);
				RenderHelper.disableStandardItemLighting();
				EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
				EaglerAdapter.glDepthMask(true);
				EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
			}
		}
	}
}
