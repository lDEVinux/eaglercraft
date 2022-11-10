package net.minecraft.src;



public class EntityBlaze extends EntityMob {
	/** Random offset used in floating behaviour */
	private float heightOffset = 0.5F;

	/** ticks until heightOffset is randomized */
	private int heightOffsetUpdateTime;
	private int field_70846_g;

	public EntityBlaze() {
		super();
		this.isImmuneToFire = true;
		this.experienceValue = 10;
	}

	public int getMaxHealth() {
		return 20;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte) 0));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.blaze.breathe";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.blaze.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.blaze.death";
	}

	public int getBrightnessForRender(float par1) {
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float par1) {
		return 1.0F;
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by
	 * each mob to define their attack.
	 */
	protected void attackEntity(Entity par1Entity, float par2) {
		if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.attackEntityAsMob(par1Entity);
		} else if (par2 < 30.0F) {
			double var3 = par1Entity.posX - this.posX;
			double var5 = par1Entity.boundingBox.minY + (double) (par1Entity.height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
			double var7 = par1Entity.posZ - this.posZ;

			if (this.attackTime == 0) {
				++this.field_70846_g;

				if (this.field_70846_g == 1) {
					this.attackTime = 60;
					this.func_70844_e(true);
				} else if (this.field_70846_g <= 4) {
					this.attackTime = 6;
				} else {
					this.attackTime = 100;
					this.field_70846_g = 0;
					this.func_70844_e(false);
				}

				if (this.field_70846_g > 1) {
					float var9 = MathHelper.sqrt_float(par2) * 0.5F;
					this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1009, (int) this.posX, (int) this.posY, (int) this.posZ, 0);

					for (int var10 = 0; var10 < 1; ++var10) {
						EntitySmallFireball var11 = new EntitySmallFireball(this.worldObj, this, var3 + this.rand.nextGaussian() * (double) var9, var5, var7 + this.rand.nextGaussian() * (double) var9);
						var11.posY = this.posY + (double) (this.height / 2.0F) + 0.5D;
						this.worldObj.spawnEntityInWorld(var11);
					}
				}
			}

			this.rotationYaw = (float) (Math.atan2(var7, var3) * 180.0D / Math.PI) - 90.0F;
			this.hasAttacked = true;
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.blazeRod.itemID;
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire effect
	 * on rendering.
	 */
	public boolean isBurning() {
		return this.func_70845_n();
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		if (par1) {
			int var3 = this.rand.nextInt(2 + par2);

			for (int var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Item.blazeRod.itemID, 1);
			}
		}
	}

	public boolean func_70845_n() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_70844_e(boolean par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);

		if (par1) {
			var2 = (byte) (var2 | 1);
		} else {
			var2 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(var2));
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	protected boolean isValidLightLevel() {
		return true;
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public int getAttackStrength(Entity par1Entity) {
		return 6;
	}
}
