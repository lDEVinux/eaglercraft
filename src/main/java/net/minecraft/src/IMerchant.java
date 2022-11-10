package net.minecraft.src;

public interface IMerchant {
	void setCustomer(EntityPlayer var1);

	EntityPlayer getCustomer();

	MerchantRecipeList getRecipes(EntityPlayer var1);

	void setRecipes(MerchantRecipeList var1);

	void useRecipe(MerchantRecipe var1);
}
