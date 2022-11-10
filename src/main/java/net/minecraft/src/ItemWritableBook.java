package net.minecraft.src;

public class ItemWritableBook extends Item {
	public ItemWritableBook(int par1) {
		super(par1);
		this.setMaxStackSize(1);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.displayGUIBook(par1ItemStack);
		return par1ItemStack;
	}

	/**
	 * If this function returns true (or the item is damageable), the ItemStack's
	 * NBT tag will be sent to the client.
	 */
	public boolean getShareTag() {
		return true;
	}

	public static boolean validBookTagPages(NBTTagCompound par0NBTTagCompound) {
		if (par0NBTTagCompound == null) {
			return false;
		} else if (!par0NBTTagCompound.hasKey("pages")) {
			return false;
		} else {
			NBTTagList var1 = (NBTTagList) par0NBTTagCompound.getTag("pages");

			for (int var2 = 0; var2 < var1.tagCount(); ++var2) {
				NBTTagString var3 = (NBTTagString) var1.tagAt(var2);

				if (var3.data == null) {
					return false;
				}

				if (var3.data.length() > 256) {
					return false;
				}
			}

			return true;
		}
	}
}
