package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class EnchantmentThorns extends Enchantment {
	public EnchantmentThorns(int par1, int par2) {
		super(par1, par2, EnumEnchantmentType.armor_torso);
		this.setName("thorns");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level
	 * passed.
	 */
	public int getMinEnchantability(int par1) {
		return 10 + 20 * (par1 - 1);
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
		return par1ItemStack.getItem() instanceof ItemArmor ? true : super.func_92089_a(par1ItemStack);
	}

	public static boolean func_92094_a(int par0, EaglercraftRandom par1Random) {
		return par0 <= 0 ? false : par1Random.nextFloat() < 0.15F * (float) par0;
	}

	public static int func_92095_b(int par0, EaglercraftRandom par1Random) {
		return par0 > 10 ? par0 - 10 : 1 + par1Random.nextInt(4);
	}

	public static void func_92096_a(Entity par0Entity, EntityLiving par1EntityLiving, EaglercraftRandom par2Random) {
		int var3 = EnchantmentHelper.func_92098_i(par1EntityLiving);
		ItemStack var4 = EnchantmentHelper.func_92099_a(Enchantment.thorns, par1EntityLiving);

		if (func_92094_a(var3, par2Random)) {
			par0Entity.attackEntityFrom(DamageSource.causeThornsDamage(par1EntityLiving),
					func_92095_b(var3, par2Random));
			par0Entity.playSound("damage.thorns", 0.5F, 1.0F);

			if (var4 != null) {
				var4.damageItem(3, par1EntityLiving);
			}
		} else if (var4 != null) {
			var4.damageItem(1, par1EntityLiving);
		}
	}
}
