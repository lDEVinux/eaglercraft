package net.minecraft.src;

public class EntityMinecartEmpty extends EntityMinecart {
	
	public EntityMinecartEmpty(World par1) {
		super();
		setWorld(par1);
	}

	public EntityMinecartEmpty(World par1, double par2, double par4, double par6) {
		super(par1, par2, par4, par6);
	}

	public EntityMinecartEmpty() {
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
	 * into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer) {
			return true;
		} else if (this.riddenByEntity != null && this.riddenByEntity != par1EntityPlayer) {
			return false;
		} else {
			return true;
		}
	}

	public int getMinecartType() {
		return 0;
	}
}
