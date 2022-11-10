package net.minecraft.src;

public class EntityPotion extends EntityThrowable {
	/**
	 * The damage value of the thrown potion that this EntityPotion represents.
	 */
	private ItemStack potionDamage;

	public EntityPotion() {
		super();
	}

	public EntityPotion(World par1World, EntityLiving par2EntityLiving, int par3) {
		this(par1World, par2EntityLiving, new ItemStack(Item.potion, 1, par3));
	}

	public EntityPotion(World par1World, EntityLiving par2EntityLiving, ItemStack par3ItemStack) {
		super(par1World, par2EntityLiving);
		this.potionDamage = par3ItemStack;
	}

	public EntityPotion(World par1World, double par2, double par4, double par6, int par8) {
		this(par1World, par2, par4, par6, new ItemStack(Item.potion, 1, par8));
	}

	public EntityPotion(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
		super(par1World, par2, par4, par6);
		this.potionDamage = par8ItemStack;
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity() {
		return 0.05F;
	}

	protected float func_70182_d() {
		return 0.5F;
	}

	protected float func_70183_g() {
		return -20.0F;
	}

	public void setPotionDamage(int par1) {
		if (this.potionDamage == null) {
			this.potionDamage = new ItemStack(Item.potion, 1, 0);
		}

		this.potionDamage.setItemDamage(par1);
	}

	/**
	 * Returns the damage value of the thrown potion that this EntityPotion
	 * represents.
	 */
	public int getPotionDamage() {
		if (this.potionDamage == null) {
			this.potionDamage = new ItemStack(Item.potion, 1, 0);
		}

		return this.potionDamage.getItemDamage();
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.hasKey("Potion")) {
			this.potionDamage = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("Potion"));
		} else {
			this.setPotionDamage(par1NBTTagCompound.getInteger("potionValue"));
		}

		if (this.potionDamage == null) {
			this.setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);

		if (this.potionDamage != null) {
			par1NBTTagCompound.setCompoundTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
		}
	}
}
