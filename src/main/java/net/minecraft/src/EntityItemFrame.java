package net.minecraft.src;

public class EntityItemFrame extends EntityHanging {
	/** Chance for this item frame's item to drop from the frame. */
	private float itemDropChance = 1.0F;
	private boolean hasMapItem = false;

	public EntityItemFrame() {
		super();
	}

	public EntityItemFrame(World par1World, int par2, int par3, int par4, int par5) {
		super(par1World, par2, par3, par4, par5);
		this.setDirection(par5);
	}

	protected void entityInit() {
		this.getDataWatcher().addObjectByDataType(2, 5);
		this.getDataWatcher().addObject(3, Byte.valueOf((byte) 0));
	}

	public int func_82329_d() {
		return hasMapItem ? 16 : 10;
	}

	public int func_82330_g() {
		return hasMapItem ? 16 : 10;
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance and
	 * comparing it to its average edge length * 64 * renderDistanceWeight Args:
	 * distance
	 */
	public boolean isInRangeToRenderDist(double par1) {
		double var3 = 16.0D;
		var3 *= 64.0D * this.renderDistanceWeight;
		return par1 < var3 * var3;
	}

	/**
	 * Drop the item currently on this item frame.
	 */
	public void dropItemStack() {
		this.entityDropItem(new ItemStack(Item.itemFrame), 0.0F);
		ItemStack var1 = this.getDisplayedItem();

		if (var1 != null && this.rand.nextFloat() < this.itemDropChance) {
			var1 = var1.copy();
			var1.setItemFrame((EntityItemFrame) null);
			this.entityDropItem(var1, 0.0F);
		}
	}

	public ItemStack getDisplayedItem() {
		ItemStack stack = this.getDataWatcher().getWatchableObjectItemStack(2);
		boolean isStackMap = stack != null && stack.getItem() == Item.map;
		if(isStackMap != hasMapItem) {
			hasMapItem = isStackMap;
			this.setDirection(this.hangingDirection);
		}
		return stack;
	}

	public void setDisplayedItem(ItemStack par1ItemStack) {
		par1ItemStack = par1ItemStack.copy();
		par1ItemStack.stackSize = 1;
		par1ItemStack.setItemFrame(this);
		this.getDataWatcher().updateObject(2, par1ItemStack);
		this.getDataWatcher().setObjectWatched(2);
	}

	/**
	 * Return the rotation of the item currently on this frame.
	 */
	public int getRotation() {
		return this.getDataWatcher().getWatchableObjectByte(3);
	}

	public void setItemRotation(int par1) {
		this.getDataWatcher().updateObject(3, Byte.valueOf((byte) (par1 % 4)));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		if (this.getDisplayedItem() != null) {
			par1NBTTagCompound.setCompoundTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
			par1NBTTagCompound.setByte("ItemRotation", (byte) this.getRotation());
			par1NBTTagCompound.setFloat("ItemDropChance", this.itemDropChance);
		}

		super.writeEntityToNBT(par1NBTTagCompound);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Item");

		if (var2 != null && !var2.hasNoTags()) {
			this.setDisplayedItem(ItemStack.loadItemStackFromNBT(var2));
			this.setItemRotation(par1NBTTagCompound.getByte("ItemRotation"));

			if (par1NBTTagCompound.hasKey("ItemDropChance")) {
				this.itemDropChance = par1NBTTagCompound.getFloat("ItemDropChance");
			}
		}

		super.readEntityFromNBT(par1NBTTagCompound);
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
	 * into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		return true;
	}
}
