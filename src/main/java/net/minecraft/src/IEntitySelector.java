package net.minecraft.src;

public interface IEntitySelector {
	IEntitySelector selectAnything = new EntitySelectorAlive();
	IEntitySelector selectInventories = new EntitySelectorInventory();

	/**
	 * Return whether the specified entity is applicable to this filter.
	 */
	boolean isEntityApplicable(Entity var1);
}
