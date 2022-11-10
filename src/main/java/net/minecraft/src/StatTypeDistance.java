package net.minecraft.src;

final class StatTypeDistance implements IStatType {
	/**
	 * Formats a given stat for human consumption.
	 */
	public String format(int par1) {
		double var2 = (double) par1 / 100.0D;
		double var4 = var2 / 1000.0D;
		return var4 > 0.5D ? StatBase.getDecimalFormat().format(var4) + " km" : (var2 > 0.5D ? StatBase.getDecimalFormat().format(var2) + " m" : par1 + " cm");
	}
}
