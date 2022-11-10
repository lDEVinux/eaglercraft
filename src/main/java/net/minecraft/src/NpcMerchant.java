package net.minecraft.src;

public class NpcMerchant implements IMerchant {
	/** Instance of Merchants Inventory. */
	private InventoryMerchant theMerchantInventory;

	/** This merchant's current player customer. */
	private EntityPlayer customer;

	/** The MerchantRecipeList instance. */
	private MerchantRecipeList recipeList;

	public NpcMerchant(EntityPlayer par1EntityPlayer) {
		this.customer = par1EntityPlayer;
		this.theMerchantInventory = new InventoryMerchant(par1EntityPlayer, this);
	}

	public EntityPlayer getCustomer() {
		return this.customer;
	}

	public void setCustomer(EntityPlayer par1EntityPlayer) {
	}

	public MerchantRecipeList getRecipes(EntityPlayer par1EntityPlayer) {
		return this.recipeList;
	}

	public void setRecipes(MerchantRecipeList par1MerchantRecipeList) {
		this.recipeList = par1MerchantRecipeList;
	}

	public void useRecipe(MerchantRecipe par1MerchantRecipe) {
	}
}
