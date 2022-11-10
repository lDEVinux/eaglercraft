package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class LoadingScreenRenderer implements IProgressUpdate {
	private String field_73727_a = "";

	/** A reference to the Minecraft object. */
	private Minecraft mc;

	/**
	 * The text currently displayed (i.e. the argument to the last call to printText
	 * or func_73722_d)
	 */
	private String currentlyDisplayedText = "";
	private long field_73723_d = Minecraft.getSystemTime();
	private boolean field_73724_e = false;

	public LoadingScreenRenderer(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
	}

	/**
	 * this string, followed by "working..." and then the "% complete" are the 3
	 * lines shown. This resets progress to 0, and the WorkingString to
	 * "working...".
	 */
	public void resetProgressAndMessage(String par1Str) {
		this.field_73724_e = false;
		this.func_73722_d(par1Str);
	}

	/**
	 * "Saving level", or the loading,or downloading equivelent
	 */
	public void displayProgressMessage(String par1Str) {
		this.field_73724_e = true;
		this.func_73722_d(par1Str);
	}

	public void func_73722_d(String par1Str) {
		this.currentlyDisplayedText = par1Str;

		if (!this.mc.running) {
			if (!this.field_73724_e) {
				throw new MinecraftError();
			}
		} else {
			ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			EaglerAdapter.glClear(EaglerAdapter.GL_DEPTH_BUFFER_BIT);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
			EaglerAdapter.glLoadIdentity();
			EaglerAdapter.glOrtho(0.0F, var2.getScaledWidth(), var2.getScaledHeight(), 0.0F, 100.0F, 300.0F);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
			EaglerAdapter.glLoadIdentity();
			EaglerAdapter.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	/**
	 * This is called with "Working..." by resetProgressAndMessage
	 */
	public void resetProgresAndWorkingMessage(String par1Str) {
		if (!this.mc.running) {
			if (!this.field_73724_e) {
				throw new MinecraftError();
			}
		} else {
			this.field_73723_d = 0L;
			this.field_73727_a = par1Str;
			this.setLoadingProgress(-1);
			this.field_73723_d = 0L;
		}
	}
	
	private static final TextureLocation background = new TextureLocation("/gui/background.png");

	/**
	 * Updates the progress bar on the loading screen to the specified amount. Args:
	 * loadProgress
	 */
	public void setLoadingProgress(int par1) {
		if (!this.mc.running) {
			if (!this.field_73724_e) {
				throw new MinecraftError();
			}
		} else {
			long var2 = Minecraft.getSystemTime();

			if (var2 - this.field_73723_d >= 100L) {
				this.field_73723_d = var2;
				ScaledResolution var4 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int var5 = var4.getScaledWidth();
				int var6 = var4.getScaledHeight();
				EaglerAdapter.glClear(EaglerAdapter.GL_DEPTH_BUFFER_BIT);
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
				EaglerAdapter.glLoadIdentity();
				EaglerAdapter.glOrtho(0.0F, var4.getScaledWidth(), var4.getScaledHeight(), 0.0F, 100.0F, 300.0F);
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				EaglerAdapter.glLoadIdentity();
				EaglerAdapter.glTranslatef(0.0F, 0.0F, -200.0F);
				EaglerAdapter.glClear(EaglerAdapter.GL_COLOR_BUFFER_BIT | EaglerAdapter.GL_DEPTH_BUFFER_BIT);
				Tessellator var7 = Tessellator.instance;
				background.bindTexture();
				float var8 = 32.0F;
				var7.startDrawingQuads();
				var7.setColorOpaque_I(4210752);
				var7.addVertexWithUV(0.0D, (double) var6, 0.0D, 0.0D, (double) ((float) var6 / var8));
				var7.addVertexWithUV((double) var5, (double) var6, 0.0D, (double) ((float) var5 / var8), (double) ((float) var6 / var8));
				var7.addVertexWithUV((double) var5, 0.0D, 0.0D, (double) ((float) var5 / var8), 0.0D);
				var7.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
				var7.draw();

				if (par1 >= 0) {
					byte var9 = 100;
					byte var10 = 2;
					int var11 = var5 / 2 - var9 / 2;
					int var12 = var6 / 2 + 16;
					EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
					var7.startDrawingQuads();
					var7.setColorOpaque_I(8421504);
					var7.addVertex((double) var11, (double) var12, 0.0D);
					var7.addVertex((double) var11, (double) (var12 + var10), 0.0D);
					var7.addVertex((double) (var11 + var9), (double) (var12 + var10), 0.0D);
					var7.addVertex((double) (var11 + var9), (double) var12, 0.0D);
					var7.setColorOpaque_I(8454016);
					var7.addVertex((double) var11, (double) var12, 0.0D);
					var7.addVertex((double) var11, (double) (var12 + var10), 0.0D);
					var7.addVertex((double) (var11 + par1), (double) (var12 + var10), 0.0D);
					var7.addVertex((double) (var11 + par1), (double) var12, 0.0D);
					var7.draw();
					EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
				}

				this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (var5 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, var6 / 2 - 4 - 16, 16777215);
				this.mc.fontRenderer.drawStringWithShadow(this.field_73727_a, (var5 - this.mc.fontRenderer.getStringWidth(this.field_73727_a)) / 2, var6 / 2 - 4 + 8, 16777215);
				EaglerAdapter.updateDisplay();
			}
		}
	}

	/**
	 * called when there is no more progress to be had, both on completion and
	 * failure
	 */
	public void onNoMoreProgress() {
	}
}
