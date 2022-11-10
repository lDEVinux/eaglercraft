package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;



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
	public boolean ghost = false;

	public EntityFallingSand() {
		super();
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
		super();
		this.setWorld(par1World);
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
			
			if(ghost) {
				if(ticksExisted > 600) {
					setDead();
					return;
				}else {
					EntityLiving lv = Minecraft.getMinecraft().renderViewEntity;
					float dst = lv.getDistanceToEntity(this);
					if(dst < 3.0f || dst > 24.0f) {
						setDead();
						return;
					}else if(dst < 3.5f) {
						dst *= dst;
						dst *= 2.0f;
						this.motionX -= (float)(lv.posX - posX) / dst;
						this.motionY -= (float)(lv.posY - posY) / dst;
						this.motionZ -= (float)(lv.posZ - posZ) / dst;
					}
				}
			}
			
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			++this.fallTime;
			this.motionY -= 0.03999999910593033D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
		if (this.isAnvil) {
			int var2 = MathHelper.ceiling_float_int(par1 - 1.0F);

			if (var2 > 0) {
				ArrayList var3 = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
				DamageSource var4 = this.blockID == Block.anvil.blockID ? DamageSource.anvil : DamageSource.fallingBlock;
				Iterator var5 = var3.iterator();

				while (var5.hasNext()) {
					Entity var6 = (Entity) var5.next();
					var6.attackEntityFrom(var4, Math.min(MathHelper.floor_float((float) var2 * this.fallHurtAmount), this.fallHurtMax));
				}

				if (this.blockID == Block.anvil.blockID && (double) this.rand.nextFloat() < 0.05000000074505806D + (double) var2 * 0.05D) {
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

	public float getShadowSize() {
		return 0.0F;
	}

	public World getWorld() {
		return this.worldObj;
	}

	public void setIsAnvil(boolean par1) {
		this.isAnvil = par1;
	}

	/**
	 * Return whether this entity should be rendered as on fire.
	 */
	public boolean canRenderOnFire() {
		return false;
	}
}
