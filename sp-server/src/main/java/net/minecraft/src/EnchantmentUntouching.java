package net.minecraft.src;

public class EnchantmentUntouching extends Enchantment {
	protected EnchantmentUntouching(int par1, int par2) {
		super(par1, par2, EnumEnchantmentType.digger);
		this.setName("untouching");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level
	 * passed.
	 */
	public int getMinEnchantability(int par1) {
		return 15;
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
		return 1;
	}

	/**
	 * Determines if the enchantment passed can be applyied together with this
	 * enchantment.
	 */
	public boolean canApplyTogether(Enchantment par1Enchantment) {
		return super.canApplyTogether(par1Enchantment) && par1Enchantment.effectId != fortune.effectId;
	}

	public boolean func_92089_a(ItemStack par1ItemStack) {
		return par1ItemStack.getItem().itemID == Item.shears.itemID ? true : super.func_92089_a(par1ItemStack);
	}
}
