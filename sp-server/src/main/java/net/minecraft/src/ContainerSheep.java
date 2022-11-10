package net.minecraft.src;

class ContainerSheep extends Container {
	final EntitySheep field_90034_a;

	ContainerSheep(EntitySheep par1EntitySheep) {
		this.field_90034_a = par1EntitySheep;
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return false;
	}
}
