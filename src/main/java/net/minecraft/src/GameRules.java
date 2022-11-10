package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class GameRules {
	private TreeMap theGameRules = new TreeMap();

	public GameRules() {
		this.addGameRule("doFireTick", "true");
		this.addGameRule("mobGriefing", "true");
		this.addGameRule("keepInventory", "false");
		this.addGameRule("doMobSpawning", "true");
		this.addGameRule("doMobLoot", "true");
		this.addGameRule("doTileDrops", "true");
		this.addGameRule("commandBlockOutput", "true");
	}

	/**
	 * Define a game rule and its default value.
	 */
	public void addGameRule(String par1Str, String par2Str) {
		this.theGameRules.put(par1Str, new GameRuleValue(par2Str));
	}

	public void setOrCreateGameRule(String par1Str, String par2Str) {
		GameRuleValue var3 = (GameRuleValue) this.theGameRules.get(par1Str);

		if (var3 != null) {
			var3.setValue(par2Str);
		} else {
			this.addGameRule(par1Str, par2Str);
		}
	}

	/**
	 * Gets the string Game Rule value.
	 */
	public String getGameRuleStringValue(String par1Str) {
		GameRuleValue var2 = (GameRuleValue) this.theGameRules.get(par1Str);
		return var2 != null ? var2.getGameRuleStringValue() : "";
	}

	/**
	 * Gets the boolean Game Rule value.
	 */
	public boolean getGameRuleBooleanValue(String par1Str) {
		GameRuleValue var2 = (GameRuleValue) this.theGameRules.get(par1Str);
		return var2 != null ? var2.getGameRuleBooleanValue() : false;
	}

	/**
	 * Return the defined game rules as NBT.
	 */
	public NBTTagCompound writeGameRulesToNBT() {
		NBTTagCompound var1 = new NBTTagCompound("GameRules");
		Iterator var2 = this.theGameRules.keySet().iterator();

		while (var2.hasNext()) {
			String var3 = (String) var2.next();
			GameRuleValue var4 = (GameRuleValue) this.theGameRules.get(var3);
			var1.setString(var3, var4.getGameRuleStringValue());
		}

		return var1;
	}

	/**
	 * Set defined game rules from NBT.
	 */
	public void readGameRulesFromNBT(NBTTagCompound par1NBTTagCompound) {
		Collection var2 = par1NBTTagCompound.getTags();
		Iterator var3 = var2.iterator();

		while (var3.hasNext()) {
			NBTBase var4 = (NBTBase) var3.next();
			String var5 = var4.getName();
			String var6 = par1NBTTagCompound.getString(var4.getName());
			this.setOrCreateGameRule(var5, var6);
		}
	}

	/**
	 * Return the defined game rules.
	 */
	public String[] getRules() {
		return (String[]) this.theGameRules.keySet().toArray(new String[0]);
	}

	/**
	 * Return whether the specified game rule is defined.
	 */
	public boolean hasRule(String par1Str) {
		return this.theGameRules.containsKey(par1Str);
	}
}
