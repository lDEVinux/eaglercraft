package net.minecraft.src;

public class EntityMinecartMobSpawner extends EntityMinecart {
	/** Mob spawner logic for this spawner minecart. */
	public EntityMinecartMobSpawner(World par1World) {
		super();
		setWorld(par1World);
	}
	public EntityMinecartMobSpawner(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	public EntityMinecartMobSpawner() {
	}

	public int getMinecartType() {
		return 4;
	}

	public Block getDefaultDisplayTile() {
		return Block.mobSpawner;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
	}
}
