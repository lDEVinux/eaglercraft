package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityWitch extends EntityMob implements IRangedAttackMob {
	/** List of items a witch should drop on death. */
	private static final int[] witchDrops = new int[] { Item.lightStoneDust.itemID, Item.sugar.itemID,
			Item.redstone.itemID, Item.spiderEye.itemID, Item.glassBottle.itemID, Item.gunpowder.itemID,
			Item.stick.itemID, Item.stick.itemID };

	/**
	 * Timer used as interval for a witch's attack, decremented every tick if
	 * aggressive and when reaches zero the witch will throw a potion at the target
	 * entity.
	 */
	private int witchAttackTimer = 0;

	public EntityWitch(World par1World) {
		super(par1World);
		this.texture = "/mob/villager/witch.png";
		this.moveSpeed = 0.25F;
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, this.moveSpeed, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(21, Byte.valueOf((byte) 0));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.witch.idle";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.witch.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.witch.death";
	}

	/**
	 * Set whether this witch is aggressive at an entity.
	 */
	public void setAggressive(boolean par1) {
		this.getDataWatcher().updateObject(21, Byte.valueOf((byte) (par1 ? 1 : 0)));
	}

	/**
	 * Return whether this witch is aggressive at an entity.
	 */
	public boolean getAggressive() {
		return this.getDataWatcher().getWatchableObjectByte(21) == 1;
	}

	public int getMaxHealth() {
		return 26;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		if (!this.worldObj.isRemote) {
			if (this.getAggressive()) {
				if (this.witchAttackTimer-- <= 0) {
					this.setAggressive(false);
					ItemStack var1 = this.getHeldItem();
					this.setCurrentItemOrArmor(0, (ItemStack) null);

					if (var1 != null && var1.itemID == Item.potion.itemID) {
						List var2 = Item.potion.getEffects(var1);

						if (var2 != null) {
							Iterator var3 = var2.iterator();

							while (var3.hasNext()) {
								PotionEffect var4 = (PotionEffect) var3.next();
								this.addPotionEffect(new PotionEffect(var4));
							}
						}
					}
				}
			} else {
				short var5 = -1;

				if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance)) {
					var5 = 16307;
				} else if (this.rand.nextFloat() < 0.05F && this.health < this.getMaxHealth()) {
					var5 = 16341;
				} else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null
						&& !this.isPotionActive(Potion.moveSpeed)
						&& this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					var5 = 16274;
				} else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null
						&& !this.isPotionActive(Potion.moveSpeed)
						&& this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					var5 = 16274;
				}

				if (var5 > -1) {
					this.setCurrentItemOrArmor(0, new ItemStack(Item.potion, 1, var5));
					this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
					this.setAggressive(true);
				}
			}

			if (this.rand.nextFloat() < 7.5E-4F) {
				this.worldObj.setEntityState(this, (byte) 15);
			}
		}

		super.onLivingUpdate();
	}

	/**
	 * Reduces damage, depending on potions
	 */
	protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2) {
		par2 = super.applyPotionDamageCalculations(par1DamageSource, par2);

		if (par1DamageSource.getEntity() == this) {
			par2 = 0;
		}

		if (par1DamageSource.isMagicDamage()) {
			par2 = (int) ((double) par2 * 0.15D);
		}

		return par2;
	}

	/**
	 * This method returns a value to be applied directly to entity speed, this
	 * factor is less than 1 when a slowdown potion effect is applied, more than 1
	 * when a haste potion effect is applied and 2 for fleeing entities.
	 */
	public float getSpeedModifier() {
		float var1 = super.getSpeedModifier();

		if (this.getAggressive()) {
			var1 *= 0.75F;
		}

		return var1;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(3) + 1;

		for (int var4 = 0; var4 < var3; ++var4) {
			int var5 = this.rand.nextInt(3);
			int var6 = witchDrops[this.rand.nextInt(witchDrops.length)];

			if (par2 > 0) {
				var5 += this.rand.nextInt(par2 + 1);
			}

			for (int var7 = 0; var7 < var5; ++var7) {
				this.dropItem(var6, 1);
			}
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
		if (!this.getAggressive()) {
			EntityPotion var3 = new EntityPotion(this.worldObj, this, 32732);
			var3.rotationPitch -= -20.0F;
			double var4 = par1EntityLiving.posX + par1EntityLiving.motionX - this.posX;
			double var6 = par1EntityLiving.posY + (double) par1EntityLiving.getEyeHeight() - 1.100000023841858D
					- this.posY;
			double var8 = par1EntityLiving.posZ + par1EntityLiving.motionZ - this.posZ;
			float var10 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);

			if (var10 >= 8.0F && !par1EntityLiving.isPotionActive(Potion.moveSlowdown)) {
				var3.setPotionDamage(32698);
			} else if (par1EntityLiving.getHealth() >= 8 && !par1EntityLiving.isPotionActive(Potion.poison)) {
				var3.setPotionDamage(32660);
			} else if (var10 <= 3.0F && !par1EntityLiving.isPotionActive(Potion.weakness)
					&& this.rand.nextFloat() < 0.25F) {
				var3.setPotionDamage(32696);
			}

			var3.setThrowableHeading(var4, var6 + (double) (var10 * 0.2F), var8, 0.75F, 8.0F);
			this.worldObj.spawnEntityInWorld(var3);
		}
	}
}
