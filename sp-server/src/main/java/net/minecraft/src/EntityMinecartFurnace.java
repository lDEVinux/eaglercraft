package net.minecraft.src;

public class EntityMinecartFurnace extends EntityMinecart {
	private int fuel = 0;
	public double pushX;
	public double pushZ;

	public EntityMinecartFurnace(World par1World) {
		super(par1World);
	}

	public EntityMinecartFurnace(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	public int getMinecartType() {
		return 2;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte) 0));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (this.fuel > 0) {
			--this.fuel;
		}

		if (this.fuel <= 0) {
			this.pushX = this.pushZ = 0.0D;
		}

		this.setMinecartPowered(this.fuel > 0);

		if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
			this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
		}
	}

	public void killMinecart(DamageSource par1DamageSource) {
		super.killMinecart(par1DamageSource);

		if (!par1DamageSource.isExplosion()) {
			this.entityDropItem(new ItemStack(Block.furnaceIdle, 1), 0.0F);
		}
	}

	protected void updateOnTrack(int par1, int par2, int par3, double par4, double par6, int par8, int par9) {
		super.updateOnTrack(par1, par2, par3, par4, par6, par8, par9);
		double var10 = this.pushX * this.pushX + this.pushZ * this.pushZ;

		if (var10 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
			var10 = (double) MathHelper.sqrt_double(var10);
			this.pushX /= var10;
			this.pushZ /= var10;

			if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
				this.pushX = 0.0D;
				this.pushZ = 0.0D;
			} else {
				this.pushX = this.motionX;
				this.pushZ = this.motionZ;
			}
		}
	}

	protected void applyDrag() {
		double var1 = this.pushX * this.pushX + this.pushZ * this.pushZ;

		if (var1 > 1.0E-4D) {
			var1 = (double) MathHelper.sqrt_double(var1);
			this.pushX /= var1;
			this.pushZ /= var1;
			double var3 = 0.05D;
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.0D;
			this.motionZ *= 0.800000011920929D;
			this.motionX += this.pushX * var3;
			this.motionZ += this.pushZ * var3;
		} else {
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.0D;
			this.motionZ *= 0.9800000190734863D;
		}

		super.applyDrag();
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
	 * into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

		if (var2 != null && var2.itemID == Item.coal.itemID) {
			if (--var2.stackSize == 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem,
						(ItemStack) null);
			}

			this.fuel += 3600;
		}

		this.pushX = this.posX - par1EntityPlayer.posX;
		this.pushZ = this.posZ - par1EntityPlayer.posZ;
		return true;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("PushX", this.pushX);
		par1NBTTagCompound.setDouble("PushZ", this.pushZ);
		par1NBTTagCompound.setShort("Fuel", (short) this.fuel);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.pushX = par1NBTTagCompound.getDouble("PushX");
		this.pushZ = par1NBTTagCompound.getDouble("PushZ");
		this.fuel = par1NBTTagCompound.getShort("Fuel");
	}

	protected boolean isMinecartPowered() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	protected void setMinecartPowered(boolean par1) {
		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (this.dataWatcher.getWatchableObjectByte(16) | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (this.dataWatcher.getWatchableObjectByte(16) & -2)));
		}
	}

	public Block getDefaultDisplayTile() {
		return Block.furnaceBurning;
	}

	public int getDefaultDisplayTileData() {
		return 2;
	}
}
