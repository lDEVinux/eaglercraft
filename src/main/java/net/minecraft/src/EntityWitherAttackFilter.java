package net.minecraft.src;

final class EntityWitherAttackFilter implements IEntitySelector {
	/**
	 * Return whether the specified entity is applicable to this filter.
	 */
	public boolean isEntityApplicable(Entity par1Entity) {
		return par1Entity instanceof EntityLiving && ((EntityLiving) par1Entity).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
	}
}
