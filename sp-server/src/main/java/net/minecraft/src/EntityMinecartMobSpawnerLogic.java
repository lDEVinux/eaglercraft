package net.minecraft.src;

class EntityMinecartMobSpawnerLogic extends MobSpawnerBaseLogic {
	/** The spawner minecart using this mob spawner logic. */
	final EntityMinecartMobSpawner spawnerMinecart;

	EntityMinecartMobSpawnerLogic(EntityMinecartMobSpawner par1EntityMinecartMobSpawner) {
		this.spawnerMinecart = par1EntityMinecartMobSpawner;
	}

	public void func_98267_a(int par1) {
		this.spawnerMinecart.worldObj.setEntityState(this.spawnerMinecart, (byte) par1);
	}

	public World getSpawnerWorld() {
		return this.spawnerMinecart.worldObj;
	}

	public int getSpawnerX() {
		return MathHelper.floor_double(this.spawnerMinecart.posX);
	}

	public int getSpawnerY() {
		return MathHelper.floor_double(this.spawnerMinecart.posY);
	}

	public int getSpawnerZ() {
		return MathHelper.floor_double(this.spawnerMinecart.posZ);
	}
}
