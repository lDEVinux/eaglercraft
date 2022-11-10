package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class ItemEnchantedBook extends Item {
	public ItemEnchantedBook(int par1) {
		super(par1);
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	public boolean isItemTool(ItemStack par1ItemStack) {
		return false;
	}

	public NBTTagList func_92110_g(ItemStack par1ItemStack) {
		return par1ItemStack.stackTagCompound != null && par1ItemStack.stackTagCompound.hasKey("StoredEnchantments")
				? (NBTTagList) par1ItemStack.stackTagCompound.getTag("StoredEnchantments")
				: new NBTTagList();
	}

	public void func_92115_a(ItemStack par1ItemStack, EnchantmentData par2EnchantmentData) {
		NBTTagList var3 = this.func_92110_g(par1ItemStack);
		boolean var4 = true;

		for (int var5 = 0; var5 < var3.tagCount(); ++var5) {
			NBTTagCompound var6 = (NBTTagCompound) var3.tagAt(var5);

			if (var6.getShort("id") == par2EnchantmentData.enchantmentobj.effectId) {
				if (var6.getShort("lvl") < par2EnchantmentData.enchantmentLevel) {
					var6.setShort("lvl", (short) par2EnchantmentData.enchantmentLevel);
				}

				var4 = false;
				break;
			}
		}

		if (var4) {
			NBTTagCompound var7 = new NBTTagCompound();
			var7.setShort("id", (short) par2EnchantmentData.enchantmentobj.effectId);
			var7.setShort("lvl", (short) par2EnchantmentData.enchantmentLevel);
			var3.appendTag(var7);
		}

		if (!par1ItemStack.hasTagCompound()) {
			par1ItemStack.setTagCompound(new NBTTagCompound());
		}

		par1ItemStack.getTagCompound().setTag("StoredEnchantments", var3);
	}

	public ItemStack func_92111_a(EnchantmentData par1EnchantmentData) {
		ItemStack var2 = new ItemStack(this);
		this.func_92115_a(var2, par1EnchantmentData);
		return var2;
	}

	public ItemStack func_92109_a(EaglercraftRandom par1Random) {
		Enchantment var2 = Enchantment.field_92090_c[par1Random.nextInt(Enchantment.field_92090_c.length)];
		ItemStack var3 = new ItemStack(this.itemID, 1, 0);
		int var4 = MathHelper.getRandomIntegerInRange(par1Random, var2.getMinLevel(), var2.getMaxLevel());
		this.func_92115_a(var3, new EnchantmentData(var2, var4));
		return var3;
	}

	public WeightedRandomChestContent func_92114_b(EaglercraftRandom par1Random) {
		return this.func_92112_a(par1Random, 1, 1, 1);
	}

	public WeightedRandomChestContent func_92112_a(EaglercraftRandom par1Random, int par2, int par3, int par4) {
		Enchantment var5 = Enchantment.field_92090_c[par1Random.nextInt(Enchantment.field_92090_c.length)];
		ItemStack var6 = new ItemStack(this.itemID, 1, 0);
		int var7 = MathHelper.getRandomIntegerInRange(par1Random, var5.getMinLevel(), var5.getMaxLevel());
		this.func_92115_a(var6, new EnchantmentData(var5, var7));
		return new WeightedRandomChestContent(var6, par2, par3, par4);
	}
}
