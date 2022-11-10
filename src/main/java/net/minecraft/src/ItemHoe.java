package net.minecraft.src;

public class ItemHoe extends Item {
	protected EnumToolMaterial theToolMaterial;

	public ItemHoe(int par1, EnumToolMaterial par2EnumToolMaterial) {
		super(par1);
		this.theToolMaterial = par2EnumToolMaterial;
		this.maxStackSize = 1;
		this.setMaxDamage(par2EnumToolMaterial.getMaxUses());
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
			return false;
		} else {
			int var11 = par3World.getBlockId(par4, par5, par6);
			int var12 = par3World.getBlockId(par4, par5 + 1, par6);

			if ((par7 == 0 || var12 != 0 || var11 != Block.grass.blockID) && var11 != Block.dirt.blockID) {
				return false;
			} else {
				Block var13 = Block.tilledField;
				par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F), var13.stepSound.getStepSound(), (var13.stepSound.getVolume() + 1.0F) / 2.0F,
						var13.stepSound.getPitch() * 0.8F);

				return true;
			}
		}
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Returns the name of the material this tool is made from as it is declared in
	 * EnumToolMaterial (meaning diamond would return "EMERALD")
	 */
	public String getMaterialName() {
		return this.theToolMaterial.toString();
	}
}
