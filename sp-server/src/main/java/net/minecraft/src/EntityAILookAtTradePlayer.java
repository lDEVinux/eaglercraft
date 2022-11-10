package net.minecraft.src;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest {
	private final EntityVillager theMerchant;

	public EntityAILookAtTradePlayer(EntityVillager par1EntityVillager) {
		super(par1EntityVillager, EntityPlayer.class, 8.0F);
		this.theMerchant = par1EntityVillager;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.theMerchant.isTrading()) {
			this.closestEntity = this.theMerchant.getCustomer();
			return true;
		} else {
			return false;
		}
	}
}
