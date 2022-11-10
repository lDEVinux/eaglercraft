package net.minecraft.src;

public class EntityLargeFireball extends EntityFireball {
	public int field_92057_e = 1;

	public EntityLargeFireball() {
		super();
	}

	public EntityLargeFireball(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		super(par1World, par2, par4, par6, par8, par10, par12);
	}

	public EntityLargeFireball(World par1World, EntityLiving par2EntityLiving, double par3, double par5, double par7) {
		super(par1World, par2EntityLiving, par3, par5, par7);
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("ExplosionPower", this.field_92057_e);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.hasKey("ExplosionPower")) {
			this.field_92057_e = par1NBTTagCompound.getInteger("ExplosionPower");
		}
	}
}
