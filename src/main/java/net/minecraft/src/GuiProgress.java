package net.minecraft.src;

public class GuiProgress extends GuiScreen implements IProgressUpdate {
	private String progressMessage = "";
	private String workingMessage = "";
	private int currentProgress = 0;
	private boolean noMoreProgress;

	/**
	 * "Saving level", or the loading,or downloading equivelent
	 */
	public void displayProgressMessage(String par1Str) {
		this.resetProgressAndMessage(par1Str);
	}

	/**
	 * this string, followed by "working..." and then the "% complete" are the 3
	 * lines shown. This resets progress to 0, and the WorkingString to
	 * "working...".
	 */
	public void resetProgressAndMessage(String par1Str) {
		this.progressMessage = par1Str;
		this.resetProgresAndWorkingMessage("Working...");
	}

	/**
	 * This is called with "Working..." by resetProgressAndMessage
	 */
	public void resetProgresAndWorkingMessage(String par1Str) {
		this.workingMessage = par1Str;
		this.setLoadingProgress(0);
	}

	/**
	 * Updates the progress bar on the loading screen to the specified amount. Args:
	 * loadProgress
	 */
	public void setLoadingProgress(int par1) {
		this.currentProgress = par1;
	}

	/**
	 * called when there is no more progress to be had, both on completion and
	 * failure
	 */
	public void onNoMoreProgress() {
		this.noMoreProgress = true;
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		if (this.noMoreProgress) {
			this.mc.displayGuiScreen((GuiScreen) null);
		} else {
			this.drawDefaultBackground();
			this.drawCenteredString(this.fontRenderer, this.progressMessage, this.width / 2, 70, 16777215);
			this.drawCenteredString(this.fontRenderer, this.workingMessage + " " + this.currentProgress + "%", this.width / 2, 90, 16777215);
			super.drawScreen(par1, par2, par3);
		}
	}
}
