package net.minecraft.src;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
	public EntitySnowman(World par1World) {
		super(par1World);
		this.texture = "/mob/snowman.png";
		this.setSize(0.4F, 1.8F);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAIArrowAttack(this, 0.25F, 20, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 0.2F));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1,
				new EntityAINearestAttackableTarget(this, EntityLiving.class, 16.0F, 0, true, false, IMob.mobSelector));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 4;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.isWet()) {
			this.attackEntityFrom(DamageSource.drown, 1);
		}

		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.posZ);

		if (this.worldObj.getBiomeGenForCoords(var1, var2).getFloatTemperature() > 1.0F) {
			this.attackEntityFrom(DamageSource.onFire, 1);
		}

		for (var1 = 0; var1 < 4; ++var1) {
			var2 = MathHelper.floor_double(this.posX + (double) ((float) (var1 % 2 * 2 - 1) * 0.25F));
			int var3 = MathHelper.floor_double(this.posY);
			int var4 = MathHelper.floor_double(this.posZ + (double) ((float) (var1 / 2 % 2 * 2 - 1) * 0.25F));

			if (this.worldObj.getBlockId(var2, var3, var4) == 0
					&& this.worldObj.getBiomeGenForCoords(var2, var4).getFloatTemperature() < 0.8F
					&& Block.snow.canPlaceBlockAt(this.worldObj, var2, var3, var4)) {
				this.worldObj.setBlock(var2, var3, var4, Block.snow.blockID);
			}
		}
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.snowball.itemID;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(16);

		for (int var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.snowball.itemID, 1);
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
		EntitySnowball var3 = new EntitySnowball(this.worldObj, this);
		double var4 = par1EntityLiving.posX - this.posX;
		double var6 = par1EntityLiving.posY + (double) par1EntityLiving.getEyeHeight() - 1.100000023841858D - var3.posY;
		double var8 = par1EntityLiving.posZ - this.posZ;
		float var10 = MathHelper.sqrt_double(var4 * var4 + var8 * var8) * 0.2F;
		var3.setThrowableHeading(var4, var6 + (double) var10, var8, 1.6F, 12.0F);
		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(var3);
	}
}
