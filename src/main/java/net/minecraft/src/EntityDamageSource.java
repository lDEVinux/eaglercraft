package net.minecraft.src;

public class EntityDamageSource extends DamageSource {
	protected Entity damageSourceEntity;

	public EntityDamageSource(String par1Str, Entity par2Entity) {
		super(par1Str);
		this.damageSourceEntity = par2Entity;
	}

	public Entity getEntity() {
		return this.damageSourceEntity;
	}

	/**
	 * Returns the message to be displayed on player death.
	 */
	public String getDeathMessage(EntityLiving par1EntityLiving) {
		ItemStack var2 = this.damageSourceEntity instanceof EntityLiving ? ((EntityLiving) this.damageSourceEntity).getHeldItem() : null;
		String var3 = "death.attack." + this.damageType;
		String var4 = var3 + ".item";
		return var2 != null && var2.hasDisplayName() && StatCollector.func_94522_b(var4)
				? StatCollector.translateToLocalFormatted(var4, new Object[] { par1EntityLiving.getTranslatedEntityName(), this.damageSourceEntity.getTranslatedEntityName(), var2.getDisplayName() })
				: StatCollector.translateToLocalFormatted(var3, new Object[] { par1EntityLiving.getTranslatedEntityName(), this.damageSourceEntity.getTranslatedEntityName() });
	}

	/**
	 * Return whether this damage source will have its damage amount scaled based on
	 * the current difficulty.
	 */
	public boolean isDifficultyScaled() {
		return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLiving && !(this.damageSourceEntity instanceof EntityPlayer);
	}
}
