package net.minecraft.src;

public class EntityDamageSourceIndirect extends EntityDamageSource {
	private Entity indirectEntity;

	public EntityDamageSourceIndirect(String par1Str, Entity par2Entity, Entity par3Entity) {
		super(par1Str, par2Entity);
		this.indirectEntity = par3Entity;
	}

	public Entity getSourceOfDamage() {
		return this.damageSourceEntity;
	}

	public Entity getEntity() {
		return this.indirectEntity;
	}

	/**
	 * Returns the message to be displayed on player death.
	 */
	public String getDeathMessage(EntityLiving par1EntityLiving) {
		String var2 = this.indirectEntity == null ? this.damageSourceEntity.getTranslatedEntityName() : this.indirectEntity.getTranslatedEntityName();
		ItemStack var3 = this.indirectEntity instanceof EntityLiving ? ((EntityLiving) this.indirectEntity).getHeldItem() : null;
		String var4 = "death.attack." + this.damageType;
		String var5 = var4 + ".item";
		return var3 != null && var3.hasDisplayName() && StatCollector.func_94522_b(var5) ? StatCollector.translateToLocalFormatted(var5, new Object[] { par1EntityLiving.getTranslatedEntityName(), var2, var3.getDisplayName() })
				: StatCollector.translateToLocalFormatted(var4, new Object[] { par1EntityLiving.getTranslatedEntityName(), var2 });
	}
}
