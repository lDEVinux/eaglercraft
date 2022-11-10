package net.minecraft.src;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {

	public EntitySkeleton() {
		super();
		this.moveSpeed = 0.25F;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(13, new Byte((byte) 0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 20;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.skeleton.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.skeleton.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.skeleton.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		if (super.attackEntityAsMob(par1Entity)) {
			if (this.getSkeletonType() == 1 && par1Entity instanceof EntityLiving) {
				((EntityLiving) par1Entity).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public int getAttackStrength(Entity par1Entity) {
		if (this.getSkeletonType() == 1) {
			ItemStack var2 = this.getHeldItem();
			int var3 = 4;

			if (var2 != null) {
				var3 += var2.getDamageVsEntity(this);
			}

			return var3;
		} else {
			return super.getAttackStrength(par1Entity);
		}
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		if (this.getSkeletonType() == 1) {
			this.setSize(0.72F, 2.34F);
		}

		super.onLivingUpdate();
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);

		if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow && par1DamageSource.getEntity() instanceof EntityPlayer) {
			EntityPlayer var2 = (EntityPlayer) par1DamageSource.getEntity();
			double var3 = var2.posX - this.posX;
			double var5 = var2.posZ - this.posZ;

			if (var3 * var3 + var5 * var5 >= 2500.0D) {
				var2.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.arrow.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3;
		int var4;

		if (this.getSkeletonType() == 1) {
			var3 = this.rand.nextInt(3 + par2) - 1;

			for (var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Item.coal.itemID, 1);
			}
		} else {
			var3 = this.rand.nextInt(3 + par2);

			for (var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Item.arrow.itemID, 1);
			}
		}

		var3 = this.rand.nextInt(3 + par2);

		for (var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.bone.itemID, 1);
		}
	}

	protected void dropRareDrop(int par1) {
		if (this.getSkeletonType() == 1) {
			this.entityDropItem(new ItemStack(Item.skull.itemID, 1, 1), 0.0F);
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	protected void addRandomArmor() {
		super.addRandomArmor();
		this.setCurrentItemOrArmor(0, new ItemStack(Item.bow));
	}

	/**
	 * Initialize this creature.
	 */
	public void initCreature() {
		
	}

	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask() {
		
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
		EntityArrow var3 = new EntityArrow(this.worldObj, this, par1EntityLiving, 1.6F, (float) (14 - this.worldObj.difficultySetting * 4));
		int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int var5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
		var3.setDamage((double) (par2 * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.difficultySetting * 0.11F));

		if (var4 > 0) {
			var3.setDamage(var3.getDamage() + (double) var4 * 0.5D + 0.5D);
		}

		if (var5 > 0) {
			var3.setKnockbackStrength(var5);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1) {
			var3.setFire(100);
		}

		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(var3);
	}

	/**
	 * Return this skeleton's type.
	 */
	public int getSkeletonType() {
		return this.dataWatcher.getWatchableObjectByte(13);
	}

	/**
	 * Set this skeleton's type.
	 */
	public void setSkeletonType(int par1) {
		this.dataWatcher.updateObject(13, Byte.valueOf((byte) par1));
		this.isImmuneToFire = par1 == 1;

		if (par1 == 1) {
			this.setSize(0.72F, 2.34F);
		} else {
			this.setSize(0.6F, 1.8F);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.hasKey("SkeletonType")) {
			byte var2 = par1NBTTagCompound.getByte("SkeletonType");
			this.setSkeletonType(var2);
		}

		this.setCombatTask();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setByte("SkeletonType", (byte) this.getSkeletonType());
	}
}
