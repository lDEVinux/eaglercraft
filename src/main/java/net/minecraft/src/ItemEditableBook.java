package net.minecraft.src;

import java.util.List;

public class ItemEditableBook extends Item {
	public ItemEditableBook(int par1) {
		super(par1);
		this.setMaxStackSize(1);
	}

	public static boolean validBookTagContents(NBTTagCompound par0NBTTagCompound) {
		if (!ItemWritableBook.validBookTagPages(par0NBTTagCompound)) {
			return false;
		} else if (!par0NBTTagCompound.hasKey("title")) {
			return false;
		} else {
			String var1 = par0NBTTagCompound.getString("title");
			return var1 != null && var1.length() <= 16 ? par0NBTTagCompound.hasKey("author") : false;
		}
	}

	public String getItemDisplayName(ItemStack par1ItemStack) {
		if (par1ItemStack.hasTagCompound()) {
			NBTTagCompound var2 = par1ItemStack.getTagCompound();
			NBTTagString var3 = (NBTTagString) var2.getTag("title");

			if (var3 != null) {
				return var3.toString();
			}
		}

		return super.getItemDisplayName(par1ItemStack);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (par1ItemStack.hasTagCompound()) {
			NBTTagCompound var5 = par1ItemStack.getTagCompound();
			NBTTagString var6 = (NBTTagString) var5.getTag("author");

			if (var6 != null) {
				par3List.add(EnumChatFormatting.GRAY + String.format(StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] { var6.data }), new Object[0]));
			}
		}
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

	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}
}
