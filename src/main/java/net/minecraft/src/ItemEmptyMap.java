package net.minecraft.src;



public class ItemEmptyMap extends ItemMapBase {
	protected ItemEmptyMap(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		ItemStack var4 = new ItemStack(Item.map, 1, par2World.getUniqueDataId("map"));
		String var5 = "map_" + var4.getItemDamage();
		MapData var6 = new MapData(var5);
		par2World.setItemData(var5, var6);
		var6.scale = 0;
		int var7 = 128 * (1 << var6.scale);
		var6.xCenter = (int) (Math.round(par3EntityPlayer.posX / (double) var7) * (long) var7);
		var6.zCenter = (int) (Math.round(par3EntityPlayer.posZ / (double) var7) * (long) var7);
		var6.dimension = (byte) par2World.provider.dimensionId;
		var6.markDirty();
		--par1ItemStack.stackSize;

		if (par1ItemStack.stackSize <= 0) {
			return var4;
		} else {
			if (!par3EntityPlayer.inventory.addItemStackToInventory(var4.copy())) {
				par3EntityPlayer.dropPlayerItem(var4);
			}

			return par1ItemStack;
		}
	}
}
