package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class EnchantmentDurability extends Enchantment {
	protected EnchantmentDurability(int par1, int par2) {
		super(par1, par2, EnumEnchantmentType.digger);
		this.setName("durability");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level
	 * passed.
	 */
	public int getMinEnchantability(int par1) {
		return 5 + (par1 - 1) * 8;
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment level
	 * passed.
	 */
	public int getMaxEnchantability(int par1) {
		return super.getMinEnchantability(par1) + 50;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	public int getMaxLevel() {
		return 3;
	}

	public boolean func_92089_a(ItemStack par1ItemStack) {
		return par1ItemStack.isItemStackDamageable() ? true : super.func_92089_a(par1ItemStack);
	}

	public static boolean func_92097_a(ItemStack par0ItemStack, int par1, EaglercraftRandom par2Random) {
		return par0ItemStack.getItem() instanceof ItemArmor && par2Random.nextFloat() < 0.6F ? false
				: par2Random.nextInt(par1 + 1) > 0;
	}
}
