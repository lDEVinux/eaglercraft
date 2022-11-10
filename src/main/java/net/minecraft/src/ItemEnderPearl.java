package net.minecraft.src;

import net.lax1dude.eaglercraft.WebsocketNetworkManager;
import net.minecraft.client.Minecraft;

public class ItemEnderPearl extends Item {
	public ItemEnderPearl(int par1) {
		super(par1);
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par3EntityPlayer.capabilities.isCreativeMode && Minecraft.getMinecraft().getNetHandler().getNetManager() instanceof WebsocketNetworkManager) {
			return par1ItemStack;
		} else if (par3EntityPlayer.ridingEntity != null) {
			return par1ItemStack;
		} else {
			if (!par3EntityPlayer.capabilities.isCreativeMode) --par1ItemStack.stackSize;
			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			return par1ItemStack;
		}
	}
}
