package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class ItemRecord extends Item {
	/** List of all record items and their names. */
	private static final Map records = new HashMap();

	/** The name of the record. */
	public final String recordName;

	protected ItemRecord(int par1, String par2Str) {
		super(par1);
		this.recordName = par2Str;
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabMisc);
		records.put(par2Str, this);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
			int par5, int par6, int par7, float par8, float par9, float par10) {
		if (par3World.getBlockId(par4, par5, par6) == Block.jukebox.blockID
				&& par3World.getBlockMetadata(par4, par5, par6) == 0) {
			if (par3World.isRemote) {
				return true;
			} else {
				((BlockJukeBox) Block.jukebox).insertRecord(par3World, par4, par5, par6, par1ItemStack);
				par3World.playAuxSFXAtEntity((EntityPlayer) null, 1005, par4, par5, par6, this.itemID);
				--par1ItemStack.stackSize;
				return true;
			}
		} else {
			return false;
		}
	}
}
