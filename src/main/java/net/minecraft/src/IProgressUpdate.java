package net.minecraft.src;

public interface IProgressUpdate {
	/**
	 * "Saving level", or the loading,or downloading equivelent
	 */
	void displayProgressMessage(String var1);

	/**
	 * this string, followed by "working..." and then the "% complete" are the 3
	 * lines shown. This resets progress to 0, and the WorkingString to
	 * "working...".
	 */
	void resetProgressAndMessage(String var1);

	/**
	 * This is called with "Working..." by resetProgressAndMessage
	 */
	void resetProgresAndWorkingMessage(String var1);

	/**
	 * Updates the progress bar on the loading screen to the specified amount. Args:
	 * loadProgress
	 */
	void setLoadingProgress(int var1);

	/**
	 * called when there is no more progress to be had, both on completion and
	 * failure
	 */
	void onNoMoreProgress();
}
