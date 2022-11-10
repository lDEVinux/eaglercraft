package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.NoCatchParse;

class GameRuleValue {
	private String valueString;
	private boolean valueBoolean;
	private int valueInteger;
	private double valueDouble;

	public GameRuleValue(String par1Str) {
		this.setValue(par1Str);
	}

	/**
	 * Set this game rule value.
	 */
	public void setValue(String par1Str) {
		this.valueString = par1Str;
		this.valueBoolean = Boolean.parseBoolean(par1Str);
		
		this.valueInteger = NoCatchParse.parseInt(par1Str);
		
		if(this.valueInteger == NoCatchParse.INT_EXCEPTION) {
			this.valueInteger = 0;
		}
		
		this.valueDouble = NoCatchParse.parseDouble(par1Str);
		
		if(this.valueDouble == NoCatchParse.DOUBLE_EXCEPTION) {
			this.valueDouble = 0.0d;
		}
	}

	/**
	 * Gets the GameRule's value as String.
	 */
	public String getGameRuleStringValue() {
		return this.valueString;
	}

	/**
	 * Gets the GameRule's value as boolean.
	 */
	public boolean getGameRuleBooleanValue() {
		return this.valueBoolean;
	}
}
