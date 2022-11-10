package net.minecraft.src;

public class ItemTool extends Item {
	/** Array of blocks the tool has extra effect against. */
	private Block[] blocksEffectiveAgainst;
	protected float efficiencyOnProperMaterial = 4.0F;

	/** Damage versus entities. */
	private int damageVsEntity;

	/** The material this tool is made from. */
	protected EnumToolMaterial toolMaterial;

	protected ItemTool(int par1, int par2, EnumToolMaterial par3EnumToolMaterial, Block[] par4ArrayOfBlock) {
		super(par1);
		this.toolMaterial = par3EnumToolMaterial;
		this.blocksEffectiveAgainst = par4ArrayOfBlock;
		this.maxStackSize = 1;
		this.setMaxDamage(par3EnumToolMaterial.getMaxUses());
		this.efficiencyOnProperMaterial = par3EnumToolMaterial.getEfficiencyOnProperMaterial();
		this.damageVsEntity = par2 + par3EnumToolMaterial.getDamageVsEntity();
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	/**
	 * Returns the strength of the stack against a given block. 1.0F base,
	 * (Quality+1)*2 if correct blocktype, 1.5F if sword
	 */
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		for (int var3 = 0; var3 < this.blocksEffectiveAgainst.length; ++var3) {
			if (this.blocksEffectiveAgainst[var3] == par2Block) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	/**
	 * Current implementations of this method in child classes do not use the entry
	 * argument beside ev. They just raise the damage on the stack.
	 */
	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
		par1ItemStack.damageItem(2, par3EntityLiving);
		return true;
	}

	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) {
		if ((double) Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0D) {
			par1ItemStack.damageItem(1, par7EntityLiving);
		}

		return true;
	}

	/**
	 * Returns the damage against a given entity.
	 */
	public int getDamageVsEntity(Entity par1Entity) {
		return this.damageVsEntity;
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on
	 * material.
	 */
	public int getItemEnchantability() {
		return this.toolMaterial.getEnchantability();
	}

	/**
	 * Return the name for this tool's material.
	 */
	public String getToolMaterialName() {
		return this.toolMaterial.toString();
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return this.toolMaterial.getToolCraftingMaterial() == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}
}
