package net.minecraft.src;

public class EntityLightningBolt extends EntityWeatherEffect {
	/**
	 * Declares which state the lightning bolt is in. Whether it's in the air, hit
	 * the ground, etc.
	 */
	private int lightningState;

	/**
	 * A random long that is used to change the vertex of the lightning rendered in
	 * RenderLightningBolt
	 */
	public long boltVertex = 0L;

	/**
	 * Determines the time before the EntityLightningBolt is destroyed. It is a
	 * random integer decremented over time.
	 */
	private int boltLivingTime;

	public EntityLightningBolt(World par1World, double par2, double par4, double par6) {
		super(par1World);
		this.setLocationAndAngles(par2, par4, par6, 0.0F, 0.0F);
		this.lightningState = 2;
		this.boltVertex = this.rand.nextLong();
		this.boltLivingTime = this.rand.nextInt(3) + 1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (this.lightningState == 2) {
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
		}

		--this.lightningState;

		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.setDead();
			} else if (this.lightningState < -this.rand.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = this.rand.nextLong();
			}
		}

		if (this.lightningState >= 0) {
			this.worldObj.lastLightningBolt = 2;
		}
	}

	protected void entityInit() {
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
	}

	/**
	 * Checks using a Vec3d to determine if this entity is within range of that
	 * vector to be rendered. Args: vec3D
	 */
	public boolean isInRangeToRenderVec3D(Vec3 par1Vec3) {
		return this.lightningState >= 0;
	}
}
