package net.minecraft.src;

public class EntityCow extends EntityAnimal {
	public EntityCow() {
		super();
		this.setSize(0.9F, 1.3F);
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

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.cow.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.cow.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.cow.hurt";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.playSound("mob.cow.step", 0.15F, 1.0F);
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 0.4F;
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.leather.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.leather.itemID, 1);
		}

		var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

		for (var4 = 0; var4 < var3; ++var4) {
			if (this.isBurning()) {
				this.dropItem(Item.beefCooked.itemID, 1);
			} else {
				this.dropItem(Item.beefRaw.itemID, 1);
			}
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
	 * into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

		if (var2 != null && var2.itemID == Item.bucketEmpty.itemID) {
			if (--var2.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Item.bucketMilk));
			} else if (!par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.bucketMilk))) {
				par1EntityPlayer.dropPlayerItem(new ItemStack(Item.bucketMilk.itemID, 1, 0));
			}

			return true;
		} else {
			return super.interact(par1EntityPlayer);
		}
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed to
	 * generate the new baby animal.
	 */
	public EntityCow spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		EntityCow c = new EntityCow();
		c.setWorld(worldObj);
		return c;
	}

	public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
}
