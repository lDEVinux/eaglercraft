package net.minecraft.src;

public class ChatLine {
	/** GUI Update Counter value this Line was created at */
	private final int updateCounterCreated;
	private final String lineString;

	/**
	 * int value to refer to existing Chat Lines, can be 0 which means unreferrable
	 */
	private final int chatLineID;

	public ChatLine(int par1, String par2Str, int par3) {
		this.lineString = par2Str;
		this.updateCounterCreated = par1;
		this.chatLineID = par3;
	}

	public String getChatLineString() {
		return this.lineString;
	}

	public int getUpdatedCounter() {
		return this.updateCounterCreated;
	}

	public int getChatLineID() {
		return this.chatLineID;
	}
}
