package net.minecraft.src;

import java.util.List;



public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
	private float[] field_82220_d = new float[2];
	private float[] field_82221_e = new float[2];
	private float[] field_82217_f = new float[2];
	private float[] field_82218_g = new float[2];
	private int[] field_82223_h = new int[2];
	private int[] field_82224_i = new int[2];
	private int field_82222_j;

	/** Selector used to determine the entities a wither boss should attack. */
	private static final IEntitySelector attackEntitySelector = new EntityWitherAttackFilter();

	public EntityWither() {
		super();
		this.setEntityHealth(this.getMaxHealth());
		this.setSize(0.9F, 4.0F);
		this.isImmuneToFire = true;
		this.moveSpeed = 0.6F;
		this.experienceValue = 50;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Integer(100));
		this.dataWatcher.addObject(17, new Integer(0));
		this.dataWatcher.addObject(18, new Integer(0));
		this.dataWatcher.addObject(19, new Integer(0));
		this.dataWatcher.addObject(20, new Integer(0));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Invul", this.func_82212_n());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.func_82215_s(par1NBTTagCompound.getInteger("Invul"));
		this.dataWatcher.updateObject(16, Integer.valueOf(this.health));
	}

	public float getShadowSize() {
		return this.height / 8.0F;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.wither.idle";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.wither.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.wither.death";
	}

	/**
	 * Returns the texture's file path as a String.
	 */
	public boolean isInvul() {
		int var1 = this.func_82212_n();
		return var1 > 0 && (var1 > 80 || var1 / 5 % 2 != 1);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		this.motionY *= 0.6000000238418579D;
		double var4;
		double var6;
		double var8;

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D) {
			this.rotationYaw = (float) Math.atan2(this.motionZ, this.motionX) * (180F / (float) Math.PI) - 90.0F;
		}

		super.onLivingUpdate();
		int var20;

		for (var20 = 0; var20 < 2; ++var20) {
			this.field_82218_g[var20] = this.field_82221_e[var20];
			this.field_82217_f[var20] = this.field_82220_d[var20];
		}

		int var21;

		for (var20 = 0; var20 < 2; ++var20) {
			var21 = this.getWatchedTargetId(var20 + 1);
			Entity var3 = null;

			if (var21 > 0) {
				var3 = this.worldObj.getEntityByID(var21);
			}

			if (var3 != null) {
				var4 = this.func_82214_u(var20 + 1);
				var6 = this.func_82208_v(var20 + 1);
				var8 = this.func_82213_w(var20 + 1);
				double var10 = var3.posX - var4;
				double var12 = var3.posY + (double) var3.getEyeHeight() - var6;
				double var14 = var3.posZ - var8;
				double var16 = (double) MathHelper.sqrt_double(var10 * var10 + var14 * var14);
				float var18 = (float) (Math.atan2(var14, var10) * 180.0D / Math.PI) - 90.0F;
				float var19 = (float) (-(Math.atan2(var12, var16) * 180.0D / Math.PI));
				this.field_82220_d[var20] = this.func_82204_b(this.field_82220_d[var20], var19, 40.0F);
				this.field_82221_e[var20] = this.func_82204_b(this.field_82221_e[var20], var18, 10.0F);
			} else {
				this.field_82221_e[var20] = this.func_82204_b(this.field_82221_e[var20], this.renderYawOffset, 10.0F);
			}
		}

		boolean var22 = this.isArmored();

		for (var21 = 0; var21 < 3; ++var21) {
			double var23 = this.func_82214_u(var21);
			double var5 = this.func_82208_v(var21);
			double var7 = this.func_82213_w(var21);
			this.worldObj.spawnParticle("smoke", var23 + this.rand.nextGaussian() * 0.30000001192092896D, var5 + this.rand.nextGaussian() * 0.30000001192092896D, var7 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);

			if (var22 && this.worldObj.rand.nextInt(4) == 0) {
				this.worldObj.spawnParticle("mobSpell", var23 + this.rand.nextGaussian() * 0.30000001192092896D, var5 + this.rand.nextGaussian() * 0.30000001192092896D, var7 + this.rand.nextGaussian() * 0.30000001192092896D,
						0.699999988079071D, 0.699999988079071D, 0.5D);
			}
		}

		if (this.func_82212_n() > 0) {
			for (var21 = 0; var21 < 3; ++var21) {
				this.worldObj.spawnParticle("mobSpell", this.posX + this.rand.nextGaussian() * 1.0D, this.posY + (double) (this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D,
						0.8999999761581421D);
			}
		}
	}

	protected void updateAITasks() {
		int var1;

		if (this.func_82212_n() > 0) {
			var1 = this.func_82212_n() - 1;

			if (var1 <= 0) {
				this.worldObj.newExplosion(this, this.posX, this.posY + (double) this.getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
				this.worldObj.func_82739_e(1013, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
			}

			this.func_82215_s(var1);

			if (this.ticksExisted % 10 == 0) {
				this.heal(10);
			}
		} else {
			super.updateAITasks();
			int var12;

			for (var1 = 1; var1 < 3; ++var1) {
				if (this.ticksExisted >= this.field_82223_h[var1 - 1]) {
					this.field_82223_h[var1 - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);

					if (this.worldObj.difficultySetting >= 2) {
						int var10001 = var1 - 1;
						int var10003 = this.field_82224_i[var1 - 1];
						this.field_82224_i[var10001] = this.field_82224_i[var1 - 1] + 1;

						if (var10003 > 15) {
							float var2 = 10.0F;
							float var3 = 5.0F;
							double var4 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - (double) var2, this.posX + (double) var2);
							double var6 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - (double) var3, this.posY + (double) var3);
							double var8 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - (double) var2, this.posZ + (double) var2);
							this.func_82209_a(var1 + 1, var4, var6, var8, true);
							this.field_82224_i[var1 - 1] = 0;
						}
					}

					var12 = this.getWatchedTargetId(var1);

					if (var12 > 0) {
						Entity var14 = this.worldObj.getEntityByID(var12);

						if (var14 != null && var14.isEntityAlive() && this.getDistanceSqToEntity(var14) <= 900.0D && this.canEntityBeSeen(var14)) {
							this.func_82216_a(var1 + 1, (EntityLiving) var14);
							this.field_82223_h[var1 - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
							this.field_82224_i[var1 - 1] = 0;
						} else {
							this.func_82211_c(var1, 0);
						}
					} else {
						List var13 = this.worldObj.selectEntitiesWithinAABB(EntityLiving.class, this.boundingBox.expand(20.0D, 8.0D, 20.0D), attackEntitySelector);

						for (int var16 = 0; var16 < 10 && !var13.isEmpty(); ++var16) {
							EntityLiving var5 = (EntityLiving) var13.get(this.rand.nextInt(var13.size()));

							if (var5 != this && var5.isEntityAlive() && this.canEntityBeSeen(var5)) {
								if (var5 instanceof EntityPlayer) {
									if (!((EntityPlayer) var5).capabilities.disableDamage) {
										this.func_82211_c(var1, var5.entityId);
									}
								} else {
									this.func_82211_c(var1, var5.entityId);
								}

								break;
							}

							var13.remove(var5);
						}
					}
				}
			}

			if (this.getAttackTarget() != null) {
				this.func_82211_c(0, this.getAttackTarget().entityId);
			} else {
				this.func_82211_c(0, 0);
			}

			if (this.field_82222_j > 0) {
				--this.field_82222_j;

				if (this.field_82222_j == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
					var1 = MathHelper.floor_double(this.posY);
					var12 = MathHelper.floor_double(this.posX);
					int var15 = MathHelper.floor_double(this.posZ);
					boolean var17 = false;

					for (int var18 = -1; var18 <= 1; ++var18) {
						for (int var19 = -1; var19 <= 1; ++var19) {
							for (int var7 = 0; var7 <= 3; ++var7) {
								int var20 = var12 + var18;
								int var9 = var1 + var7;
								int var10 = var15 + var19;
								int var11 = this.worldObj.getBlockId(var20, var9, var10);

								if (var11 > 0 && var11 != Block.bedrock.blockID && var11 != Block.endPortal.blockID && var11 != Block.endPortalFrame.blockID) {
									var17 = this.worldObj.destroyBlock(var20, var9, var10, true) || var17;
								}
							}
						}
					}

					if (var17) {
						this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1012, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
					}
				}
			}

			if (this.ticksExisted % 20 == 0) {
				this.heal(1);
			}
		}
	}

	public void func_82206_m() {
		this.func_82215_s(220);
		this.setEntityHealth(this.getMaxHealth() / 3);
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	public void setInWeb() {
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue() {
		return 4;
	}

	private double func_82214_u(int par1) {
		if (par1 <= 0) {
			return this.posX;
		} else {
			float var2 = (this.renderYawOffset + (float) (180 * (par1 - 1))) / 180.0F * (float) Math.PI;
			float var3 = MathHelper.cos(var2);
			return this.posX + (double) var3 * 1.3D;
		}
	}

	private double func_82208_v(int par1) {
		return par1 <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
	}

	private double func_82213_w(int par1) {
		if (par1 <= 0) {
			return this.posZ;
		} else {
			float var2 = (this.renderYawOffset + (float) (180 * (par1 - 1))) / 180.0F * (float) Math.PI;
			float var3 = MathHelper.sin(var2);
			return this.posZ + (double) var3 * 1.3D;
		}
	}

	private float func_82204_b(float par1, float par2, float par3) {
		float var4 = MathHelper.wrapAngleTo180_float(par2 - par1);

		if (var4 > par3) {
			var4 = par3;
		}

		if (var4 < -par3) {
			var4 = -par3;
		}

		return par1 + var4;
	}

	private void func_82216_a(int par1, EntityLiving par2EntityLiving) {
		this.func_82209_a(par1, par2EntityLiving.posX, par2EntityLiving.posY + (double) par2EntityLiving.getEyeHeight() * 0.5D, par2EntityLiving.posZ, par1 == 0 && this.rand.nextFloat() < 0.001F);
	}

	private void func_82209_a(int par1, double par2, double par4, double par6, boolean par8) {
		this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1014, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
		double var9 = this.func_82214_u(par1);
		double var11 = this.func_82208_v(par1);
		double var13 = this.func_82213_w(par1);
		double var15 = par2 - var9;
		double var17 = par4 - var11;
		double var19 = par6 - var13;
		EntityWitherSkull var21 = new EntityWitherSkull(this.worldObj, this, var15, var17, var19);

		if (par8) {
			var21.setInvulnerable(true);
		}

		var21.posY = var11;
		var21.posX = var9;
		var21.posZ = var13;
		this.worldObj.spawnEntityInWorld(var21);
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
		this.func_82216_a(0, par1EntityLiving);
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else if (par1DamageSource == DamageSource.drown) {
			return false;
		} else if (this.func_82212_n() > 0) {
			return false;
		} else {
			Entity var3;

			if (this.isArmored()) {
				var3 = par1DamageSource.getSourceOfDamage();

				if (var3 instanceof EntityArrow) {
					return false;
				}
			}

			var3 = par1DamageSource.getEntity();

			if (var3 != null && !(var3 instanceof EntityPlayer) && var3 instanceof EntityLiving && ((EntityLiving) var3).getCreatureAttribute() == this.getCreatureAttribute()) {
				return false;
			} else {
				if (this.field_82222_j <= 0) {
					this.field_82222_j = 20;
				}

				for (int var4 = 0; var4 < this.field_82224_i.length; ++var4) {
					this.field_82224_i[var4] += 3;
				}

				return super.attackEntityFrom(par1DamageSource, par2);
			}
		}
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		this.dropItem(Item.netherStar.itemID, 1);
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	protected void despawnEntity() {
		this.entityAge = 0;
	}

	public int getBrightnessForRender(float par1) {
		return 15728880;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this
	 * Entity.
	 */
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**
	 * Returns the health points of the dragon.
	 */
	public int getBossHealth() {
		return this.dataWatcher.getWatchableObjectInt(16);
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
	}

	/**
	 * adds a PotionEffect to the entity
	 */
	public void addPotionEffect(PotionEffect par1PotionEffect) {
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 300;
	}

	public float func_82207_a(int par1) {
		return this.field_82221_e[par1];
	}

	public float func_82210_r(int par1) {
		return this.field_82220_d[par1];
	}

	public int func_82212_n() {
		return this.dataWatcher.getWatchableObjectInt(20);
	}

	public void func_82215_s(int par1) {
		this.dataWatcher.updateObject(20, Integer.valueOf(par1));
	}

	/**
	 * Returns the target entity ID if present, or -1 if not @param par1 The target
	 * offset, should be from 0-2
	 */
	public int getWatchedTargetId(int par1) {
		return this.dataWatcher.getWatchableObjectInt(17 + par1);
	}

	public void func_82211_c(int par1, int par2) {
		this.dataWatcher.updateObject(17 + par1, Integer.valueOf(par2));
	}

	/**
	 * Returns whether the wither is armored with its boss armor or not by checking
	 * whether its health is below half of its maximum.
	 */
	public boolean isArmored() {
		return this.getBossHealth() <= this.getMaxHealth() / 2;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	public void mountEntity(Entity par1Entity) {
		this.ridingEntity = null;
	}
}
