package net.minecraft.src;

public class EntityPig extends EntityAnimal {

	public EntityPig() {
		super();
		this.setSize(0.9F, 0.9F);
		float var2 = 0.25F;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 10;
	}

	protected void updateAITasks() {
		super.updateAITasks();
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For pigs,
	 * this is true if it is being ridden by a player and the player is holding a
	 * carrot-on-a-stick
	 */
	public boolean canBeSteered() {
		ItemStack var1 = ((EntityPlayer) this.riddenByEntity).getHeldItem();
		return var1 != null && var1.itemID == Item.carrotOnAStick.itemID;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Saddle", this.getSaddled());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setSaddled(par1NBTTagCompound.getBoolean("Saddle"));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.pig.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.pig.say";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.pig.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.playSound("mob.pig.step", 0.15F, 1.0F);
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return this.isBurning() ? Item.porkCooked.itemID : Item.porkRaw.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

		for (int var4 = 0; var4 < var3; ++var4) {
			if (this.isBurning()) {
				this.dropItem(Item.porkCooked.itemID, 1);
			} else {
				this.dropItem(Item.porkRaw.itemID, 1);
			}
		}

		if (this.getSaddled()) {
			this.dropItem(Item.saddle.itemID, 1);
		}
	}

	/**
	 * Returns true if the pig is saddled.
	 */
	public boolean getSaddled() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * Set or remove the saddle of the pig.
	 */
	public void setSaddled(boolean par1) {
		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
		}
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
		
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
		super.fall(par1);

		if (par1 > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer) this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed to
	 * generate the new baby animal.
	 */
	public EntityPig spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		EntityPig p = new EntityPig();
		p.setWorld(worldObj);
		return p;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it
	 * (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		return par1ItemStack != null && par1ItemStack.itemID == Item.carrot.itemID;
	}

	public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
}
