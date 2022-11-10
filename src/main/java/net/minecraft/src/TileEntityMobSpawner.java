package net.minecraft.src;

public class TileEntityMobSpawner extends TileEntity {
	
	public String mobID = "Pig";
	public Entity mobObject = null;
	public int rotateTicks = 0;

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.mobID = par1NBTTagCompound.getString("EntityId");
		this.mobObject = null;
		super.readFromNBT(par1NBTTagCompound);
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g.
	 * the mob spawner uses this to count ticks and creates a new spawn inside its
	 * implementation.
	 */
	public void updateEntity() {
		super.updateEntity();
		
		if(worldObj.rand.nextFloat() < 0.2f) {
			worldObj.spawnParticle("flame", this.xCoord + worldObj.rand.nextFloat(), this.yCoord + worldObj.rand.nextFloat(), this.zCoord + worldObj.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", this.xCoord + worldObj.rand.nextFloat(), this.yCoord + worldObj.rand.nextFloat(), this.zCoord + worldObj.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
		
		++rotateTicks;
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	public Packet getDescriptionPacket() {
		return null;
	}

}
