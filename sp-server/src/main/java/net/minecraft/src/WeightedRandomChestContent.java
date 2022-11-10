package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class WeightedRandomChestContent extends WeightedRandomItem {
	/** The Item/Block ID to generate in the Chest. */
	private ItemStack theItemId = null;

	/** The minimum chance of item generating. */
	private int theMinimumChanceToGenerateItem;

	/** The maximum chance of item generating. */
	private int theMaximumChanceToGenerateItem;

	public WeightedRandomChestContent(int par1, int par2, int par3, int par4, int par5) {
		super(par5);
		this.theItemId = new ItemStack(par1, 1, par2);
		this.theMinimumChanceToGenerateItem = par3;
		this.theMaximumChanceToGenerateItem = par4;
	}

	public WeightedRandomChestContent(ItemStack par1ItemStack, int par2, int par3, int par4) {
		super(par4);
		this.theItemId = par1ItemStack;
		this.theMinimumChanceToGenerateItem = par2;
		this.theMaximumChanceToGenerateItem = par3;
	}

	/**
	 * Generates the Chest contents.
	 */
	public static void generateChestContents(EaglercraftRandom par0Random,
			WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, IInventory par2IInventory, int par3) {
		for (int var4 = 0; var4 < par3; ++var4) {
			WeightedRandomChestContent var5 = (WeightedRandomChestContent) WeightedRandom.getRandomItem(par0Random,
					par1ArrayOfWeightedRandomChestContent);
			int var6 = var5.theMinimumChanceToGenerateItem
					+ par0Random.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);

			if (var5.theItemId.getMaxStackSize() >= var6) {
				ItemStack var7 = var5.theItemId.copy();
				var7.stackSize = var6;
				par2IInventory.setInventorySlotContents(par0Random.nextInt(par2IInventory.getSizeInventory()), var7);
			} else {
				for (int var9 = 0; var9 < var6; ++var9) {
					ItemStack var8 = var5.theItemId.copy();
					var8.stackSize = 1;
					par2IInventory.setInventorySlotContents(par0Random.nextInt(par2IInventory.getSizeInventory()),
							var8);
				}
			}
		}
	}

	/**
	 * Generates the Dispenser contents.
	 */
	public static void generateDispenserContents(EaglercraftRandom par0Random,
			WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent,
			TileEntityDispenser par2TileEntityDispenser, int par3) {
		for (int var4 = 0; var4 < par3; ++var4) {
			WeightedRandomChestContent var5 = (WeightedRandomChestContent) WeightedRandom.getRandomItem(par0Random,
					par1ArrayOfWeightedRandomChestContent);
			int var6 = var5.theMinimumChanceToGenerateItem
					+ par0Random.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);

			if (var5.theItemId.getMaxStackSize() >= var6) {
				ItemStack var7 = var5.theItemId.copy();
				var7.stackSize = var6;
				par2TileEntityDispenser
						.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), var7);
			} else {
				for (int var9 = 0; var9 < var6; ++var9) {
					ItemStack var8 = var5.theItemId.copy();
					var8.stackSize = 1;
					par2TileEntityDispenser.setInventorySlotContents(
							par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), var8);
				}
			}
		}
	}

	public static WeightedRandomChestContent[] func_92080_a(
			WeightedRandomChestContent[] par0ArrayOfWeightedRandomChestContent,
			WeightedRandomChestContent... par1ArrayOfWeightedRandomChestContent) {
		WeightedRandomChestContent[] var2 = new WeightedRandomChestContent[par0ArrayOfWeightedRandomChestContent.length
				+ par1ArrayOfWeightedRandomChestContent.length];
		int var3 = 0;

		for (int var4 = 0; var4 < par0ArrayOfWeightedRandomChestContent.length; ++var4) {
			var2[var3++] = par0ArrayOfWeightedRandomChestContent[var4];
		}

		WeightedRandomChestContent[] var8 = par1ArrayOfWeightedRandomChestContent;
		int var5 = par1ArrayOfWeightedRandomChestContent.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			WeightedRandomChestContent var7 = var8[var6];
			var2[var3++] = var7;
		}

		return var2;
	}
}
