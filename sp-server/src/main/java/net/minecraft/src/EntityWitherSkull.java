package net.minecraft.src;

public class EntityWitherSkull extends EntityFireball {
	public EntityWitherSkull(World par1World) {
		super(par1World);
		this.setSize(0.3125F, 0.3125F);
	}

	public EntityWitherSkull(World par1World, EntityLiving par2EntityLiving, double par3, double par5, double par7) {
		super(par1World, par2EntityLiving, par3, par5, par7);
		this.setSize(0.3125F, 0.3125F);
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by the
	 * original motion.
	 */
	protected float getMotionFactor() {
		return this.isInvulnerable() ? 0.73F : super.getMotionFactor();
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire effect
	 * on rendering.
	 */
	public boolean isBurning() {
		return false;
	}

	public float func_82146_a(Explosion par1Explosion, World par2World, int par3, int par4, int par5, Block par6Block) {
		float var7 = super.func_82146_a(par1Explosion, par2World, par3, par4, par5, par6Block);

		if (this.isInvulnerable() && par6Block != Block.bedrock && par6Block != Block.endPortal
				&& par6Block != Block.endPortalFrame) {
			var7 = Math.min(0.8F, var7);
		}

		return var7;
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
		if (!this.worldObj.isRemote) {
			if (par1MovingObjectPosition.entityHit != null) {
				if (this.shootingEntity != null) {
					if (par1MovingObjectPosition.entityHit
							.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8)
							&& !par1MovingObjectPosition.entityHit.isEntityAlive()) {
						this.shootingEntity.heal(5);
					}
				} else {
					par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.magic, 5);
				}

				if (par1MovingObjectPosition.entityHit instanceof EntityLiving) {
					byte var2 = 0;

					if (this.worldObj.difficultySetting > 1) {
						if (this.worldObj.difficultySetting == 2) {
							var2 = 10;
						} else if (this.worldObj.difficultySetting == 3) {
							var2 = 40;
						}
					}

					if (var2 > 0) {
						((EntityLiving) par1MovingObjectPosition.entityHit)
								.addPotionEffect(new PotionEffect(Potion.wither.id, 20 * var2, 1));
					}
				}
			}

			this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false,
					this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
			this.setDead();
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this
	 * Entity.
	 */
	public boolean canBeCollidedWith() {
		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(10, Byte.valueOf((byte) 0));
	}

	/**
	 * Return whether this skull comes from an invulnerable (aura) wither boss.
	 */
	public boolean isInvulnerable() {
		return this.dataWatcher.getWatchableObjectByte(10) == 1;
	}

	/**
	 * Set whether this skull comes from an invulnerable (aura) wither boss.
	 */
	public void setInvulnerable(boolean par1) {
		this.dataWatcher.updateObject(10, Byte.valueOf((byte) (par1 ? 1 : 0)));
	}
}
