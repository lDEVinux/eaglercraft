package net.minecraft.src;

public class EntityOcelot extends EntityTameable {

	public EntityOcelot() {
		super();
		this.setSize(0.6F, 0.8F);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	public void updateAITick() {
		if (this.getMoveHelper().isUpdating()) {
			float var1 = this.getMoveHelper().getSpeed();

			if (var1 == 0.18F) {
				this.setSneaking(true);
				this.setSprinting(false);
			} else if (var1 == 0.4F) {
				this.setSneaking(false);
				this.setSprinting(true);
			} else {
				this.setSneaking(false);
				this.setSprinting(false);
			}
		} else {
			this.setSneaking(false);
			this.setSprinting(false);
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return !this.isTamed();
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
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("CatType", this.getTameSkin());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setTameSkin(par1NBTTagCompound.getInteger("CatType"));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return this.isTamed() ? (this.isInLove() ? "mob.cat.purr" : (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.cat.hitt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.cat.hitt";
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

	public boolean attackEntityAsMob(Entity par1Entity) {
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed to
	 * generate the new baby animal.
	 */
	public EntityOcelot spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		EntityOcelot var2 = new EntityOcelot();
		var2.setWorld(worldObj);

		if (this.isTamed()) {
			var2.setOwner(this.getOwnerName());
			var2.setTamed(true);
			var2.setTameSkin(this.getTameSkin());
		}

		return var2;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it
	 * (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		return par1ItemStack != null && par1ItemStack.itemID == Item.fishRaw.itemID;
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	public boolean canMateWith(EntityAnimal par1EntityAnimal) {
		if (par1EntityAnimal == this) {
			return false;
		} else if (!this.isTamed()) {
			return false;
		} else if (!(par1EntityAnimal instanceof EntityOcelot)) {
			return false;
		} else {
			EntityOcelot var2 = (EntityOcelot) par1EntityAnimal;
			return !var2.isTamed() ? false : this.isInLove() && var2.isInLove();
		}
	}

	public int getTameSkin() {
		return this.dataWatcher.getWatchableObjectByte(18);
	}

	public void setTameSkin(int par1) {
		this.dataWatcher.updateObject(18, Byte.valueOf((byte) par1));
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	public boolean getCanSpawnHere() {
		if (this.worldObj.rand.nextInt(3) == 0) {
			return false;
		} else {
			if (this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox)) {
				int var1 = MathHelper.floor_double(this.posX);
				int var2 = MathHelper.floor_double(this.boundingBox.minY);
				int var3 = MathHelper.floor_double(this.posZ);

				if (var2 < 63) {
					return false;
				}

				int var4 = this.worldObj.getBlockId(var1, var2 - 1, var3);

				if (var4 == Block.grass.blockID || var4 == Block.leaves.blockID) {
					return true;
				}
			}

			return false;
		}
	}

	/**
	 * Gets the username of the entity.
	 */
	public String getEntityName() {
		return this.func_94056_bM() ? this.func_94057_bL() : (this.isTamed() ? "entity.Cat.name" : super.getEntityName());
	}

	/**
	 * Initialize this creature.
	 */
	public void initCreature() {
		if (this.worldObj.rand.nextInt(7) == 0) {
			for (int var1 = 0; var1 < 2; ++var1) {
				EntityOcelot var2 = new EntityOcelot();
				var2.setWorld(worldObj);
				var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				var2.setGrowingAge(-24000);
				this.worldObj.spawnEntityInWorld(var2);
			}
		}
	}

	public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
}
