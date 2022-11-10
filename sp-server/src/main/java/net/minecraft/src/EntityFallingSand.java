package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityFallingSand extends Entity {
	public int blockID;
	public int metadata;

	/** How long the block has been falling for. */
	public int fallTime;
	public boolean shouldDropItem;
	private boolean isBreakingAnvil;
	private boolean isAnvil;

	/** Maximum amount of damage dealt to entities hit by falling block */
	private int fallHurtMax;

	/** Actual damage dealt to entities hit by falling block */
	private float fallHurtAmount;
	public NBTTagCompound fallingBlockTileEntityData;

	public EntityFallingSand(World par1World) {
		super(par1World);
		this.fallTime = 0;
		this.shouldDropItem = true;
		this.isBreakingAnvil = false;
		this.isAnvil = false;
		this.fallHurtMax = 40;
		this.fallHurtAmount = 2.0F;
		this.fallingBlockTileEntityData = null;
	}

	public EntityFallingSand(World par1World, double par2, double par4, double par6, int par8) {
		this(par1World, par2, par4, par6, par8, 0);
	}

	public EntityFallingSand(World par1World, double par2, double par4, double par6, int par8, int par9) {
		super(par1World);
		this.fallTime = 0;
		this.shouldDropItem = true;
		this.isBreakingAnvil = false;
		this.isAnvil = false;
		this.fallHurtMax = 40;
		this.fallHurtAmount = 2.0F;
		this.fallingBlockTileEntityData = null;
		this.blockID = par8;
		this.metadata = par9;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(par2, par4, par6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk
	 * on. used for spiders and wolves to prevent them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this
	 * Entity.
	 */
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		if (this.blockID == 0) {
			this.setDead();
		} else {
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			++this.fallTime;
			this.motionY -= 0.03999999910593033D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;

			if (!this.worldObj.isRemote) {
				int var1 = MathHelper.floor_double(this.posX);
				int var2 = MathHelper.floor_double(this.posY);
				int var3 = MathHelper.floor_double(this.posZ);

				if (this.fallTime == 1) {
					if (this.worldObj.getBlockId(var1, var2, var3) != this.blockID) {
						this.setDead();
						return;
					}

					this.worldObj.setBlockToAir(var1, var2, var3);
				}

				if (this.onGround) {
					this.motionX *= 0.699999988079071D;
					this.motionZ *= 0.699999988079071D;
					this.motionY *= -0.5D;

					if (this.worldObj.getBlockId(var1, var2, var3) != Block.pistonMoving.blockID) {
						this.setDead();

						if (!this.isBreakingAnvil
								&& this.worldObj.canPlaceEntityOnSide(this.blockID, var1, var2, var3, true, 1,
										(Entity) null, (ItemStack) null)
								&& !BlockSand.canFallBelow(this.worldObj, var1, var2 - 1, var3)
								&& this.worldObj.setBlock(var1, var2, var3, this.blockID, this.metadata, 3)) {
							if (Block.blocksList[this.blockID] instanceof BlockSand) {
								((BlockSand) Block.blocksList[this.blockID]).onFinishFalling(this.worldObj, var1, var2,
										var3, this.metadata);
							}

							if (this.fallingBlockTileEntityData != null
									&& Block.blocksList[this.blockID] instanceof ITileEntityProvider) {
								TileEntity var4 = this.worldObj.getBlockTileEntity(var1, var2, var3);

								if (var4 != null) {
									NBTTagCompound var5 = new NBTTagCompound();
									var4.writeToNBT(var5);
									Iterator var6 = this.fallingBlockTileEntityData.getTags().iterator();

									while (var6.hasNext()) {
										NBTBase var7 = (NBTBase) var6.next();

										if (!var7.getName().equals("x") && !var7.getName().equals("y")
												&& !var7.getName().equals("z")) {
											var5.setTag(var7.getName(), var7.copy());
										}
									}

									var4.readFromNBT(var5);
									var4.onInventoryChanged();
								}
							}
						} else if (this.shouldDropItem && !this.isBreakingAnvil) {
							this.entityDropItem(new ItemStack(this.blockID, 1,
									Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
						}
					}
				} else if (this.fallTime > 100 && !this.worldObj.isRemote && (var2 < 1 || var2 > 256)
						|| this.fallTime > 600) {
					if (this.shouldDropItem) {
						this.entityDropItem(new ItemStack(this.blockID, 1,
								Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
					}

					this.setDead();
				}
			}
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
		if (this.isAnvil) {
			int var2 = MathHelper.ceiling_float_int(par1 - 1.0F);

			if (var2 > 0) {
				ArrayList var3 = new ArrayList(
						this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
				DamageSource var4 = this.blockID == Block.anvil.blockID ? DamageSource.anvil
						: DamageSource.fallingBlock;
				Iterator var5 = var3.iterator();

				while (var5.hasNext()) {
					Entity var6 = (Entity) var5.next();
					var6.attackEntityFrom(var4,
							Math.min(MathHelper.floor_float((float) var2 * this.fallHurtAmount), this.fallHurtMax));
				}

				if (this.blockID == Block.anvil.blockID
						&& (double) this.rand.nextFloat() < 0.05000000074505806D + (double) var2 * 0.05D) {
					int var7 = this.metadata >> 2;
					int var8 = this.metadata & 3;
					++var7;

					if (var7 > 2) {
						this.isBreakingAnvil = true;
					} else {
						this.metadata = var8 | var7 << 2;
					}
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setByte("Tile", (byte) this.blockID);
		par1NBTTagCompound.setInteger("TileID", this.blockID);
		par1NBTTagCompound.setByte("Data", (byte) this.metadata);
		par1NBTTagCompound.setByte("Time", (byte) this.fallTime);
		par1NBTTagCompound.setBoolean("DropItem", this.shouldDropItem);
		par1NBTTagCompound.setBoolean("HurtEntities", this.isAnvil);
		par1NBTTagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
		par1NBTTagCompound.setInteger("FallHurtMax", this.fallHurtMax);

		if (this.fallingBlockTileEntityData != null) {
			par1NBTTagCompound.setCompoundTag("TileEntityData", this.fallingBlockTileEntityData);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		if (par1NBTTagCompound.hasKey("TileID")) {
			this.blockID = par1NBTTagCompound.getInteger("TileID");
		} else {
			this.blockID = par1NBTTagCompound.getByte("Tile") & 255;
		}

		this.metadata = par1NBTTagCompound.getByte("Data") & 255;
		this.fallTime = par1NBTTagCompound.getByte("Time") & 255;

		if (par1NBTTagCompound.hasKey("HurtEntities")) {
			this.isAnvil = par1NBTTagCompound.getBoolean("HurtEntities");
			this.fallHurtAmount = par1NBTTagCompound.getFloat("FallHurtAmount");
			this.fallHurtMax = par1NBTTagCompound.getInteger("FallHurtMax");
		} else if (this.blockID == Block.anvil.blockID) {
			this.isAnvil = true;
		}

		if (par1NBTTagCompound.hasKey("DropItem")) {
			this.shouldDropItem = par1NBTTagCompound.getBoolean("DropItem");
		}

		if (par1NBTTagCompound.hasKey("TileEntityData")) {
			this.fallingBlockTileEntityData = par1NBTTagCompound.getCompoundTag("TileEntityData");
		}

		if (this.blockID == 0) {
			this.blockID = Block.sand.blockID;
		}
	}

	public void setIsAnvil(boolean par1) {
		this.isAnvil = par1;
	}
}
