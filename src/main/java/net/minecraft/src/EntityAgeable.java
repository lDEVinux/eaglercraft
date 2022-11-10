package net.minecraft.src;

public abstract class EntityAgeable extends EntityCreature {
	private float field_98056_d = -1.0F;
	private float field_98057_e;

	public abstract EntityAgeable createChild(EntityAgeable var1);

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(12, new Integer(0));
	}

	/**
	 * The age value may be negative or positive or zero. If it's negative, it get's
	 * incremented on each tick, if it's positive, it get's decremented each tick.
	 * Don't confuse this with EntityLiving.getAge. With a negative value the Entity
	 * is considered a child.
	 */
	public int getGrowingAge() {
		return this.dataWatcher.getWatchableObjectInt(12);
	}

	/**
	 * The age value may be negative or positive or zero. If it's negative, it get's
	 * incremented on each tick, if it's positive, it get's decremented each tick.
	 * With a negative value the Entity is considered a child.
	 */
	public void setGrowingAge(int par1) {
		this.dataWatcher.updateObject(12, Integer.valueOf(par1));
		this.func_98054_a(this.isChild());
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Age", this.getGrowingAge());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setGrowingAge(par1NBTTagCompound.getInteger("Age"));
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.func_98054_a(this.isChild());
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild() {
		return this.getGrowingAge() < 0;
	}

	public void func_98054_a(boolean par1) {
		this.func_98055_j(par1 ? 0.5F : 1.0F);
	}

	/**
	 * Sets the width and height of the entity. Args: width, height
	 */
	protected final void setSize(float par1, float par2) {
		boolean var3 = this.field_98056_d > 0.0F;
		this.field_98056_d = par1;
		this.field_98057_e = par2;

		if (!var3) {
			this.func_98055_j(1.0F);
		}
	}

	private void func_98055_j(float par1) {
		super.setSize(this.field_98056_d * par1, this.field_98057_e * par1);
	}
}
