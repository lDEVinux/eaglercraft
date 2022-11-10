package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MobSpawnerBaseLogic {
	/** The delay to spawn. */
	public int spawnDelay = 20;
	private String mobID = "Pig";

	/** List of minecart to spawn. */
	private List minecartToSpawn = null;
	private WeightedRandomMinecart randomMinecart = null;
	public double field_98287_c;
	public double field_98284_d = 0.0D;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 800;
	private int spawnCount = 4;
	private Entity field_98291_j;
	private int maxNearbyEntities = 6;

	/** The distance from which a player activates the spawner. */
	private int activatingRangeFromPlayer = 16;

	/** The range coefficient for spawning entities around. */
	private int spawnRange = 4;

	/**
	 * Gets the entity name that should be spawned.
	 */
	public String getEntityNameToSpawn() {
		if (this.getRandomMinecart() == null) {
			if (this.mobID.equals("Minecart")) {
				this.mobID = "MinecartRideable";
			}

			return this.mobID;
		} else {
			return this.getRandomMinecart().minecartName;
		}
	}

	public void setMobID(String par1Str) {
		this.mobID = par1Str;
	}

	/**
	 * Returns true if there's a player close enough to this mob spawner to activate
	 * it.
	 */
	public boolean canRun() {
		return this.getSpawnerWorld().getClosestPlayer((double) this.getSpawnerX() + 0.5D,
				(double) this.getSpawnerY() + 0.5D, (double) this.getSpawnerZ() + 0.5D,
				(double) this.activatingRangeFromPlayer) != null;
	}

	public void updateSpawner() {
		if (this.canRun()) {
			double var5;

			if (this.getSpawnerWorld().isRemote) {
				double var1 = (double) ((float) this.getSpawnerX() + this.getSpawnerWorld().rand.nextFloat());
				double var3 = (double) ((float) this.getSpawnerY() + this.getSpawnerWorld().rand.nextFloat());
				var5 = (double) ((float) this.getSpawnerZ() + this.getSpawnerWorld().rand.nextFloat());
				this.getSpawnerWorld().spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
				this.getSpawnerWorld().spawnParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);

				if (this.spawnDelay > 0) {
					--this.spawnDelay;
				}

				this.field_98284_d = this.field_98287_c;
				this.field_98287_c = (this.field_98287_c + (double) (1000.0F / ((float) this.spawnDelay + 200.0F)))
						% 360.0D;
			} else {
				if (this.spawnDelay == -1) {
					this.func_98273_j();
				}

				if (this.spawnDelay > 0) {
					--this.spawnDelay;
					return;
				}

				boolean var12 = false;

				for (int var2 = 0; var2 < this.spawnCount; ++var2) {
					Entity var13 = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());

					if (var13 == null) {
						return;
					}

					int var4 = this.getSpawnerWorld()
							.getEntitiesWithinAABB(var13.getClass(), AxisAlignedBB.getAABBPool()
									.getAABB((double) this.getSpawnerX(), (double) this.getSpawnerY(),
											(double) this.getSpawnerZ(), (double) (this.getSpawnerX() + 1),
											(double) (this.getSpawnerY() + 1), (double) (this.getSpawnerZ() + 1))
									.expand((double) (this.spawnRange * 2), 4.0D, (double) (this.spawnRange * 2)))
							.size();

					if (var4 >= this.maxNearbyEntities) {
						this.func_98273_j();
						return;
					}

					var5 = (double) this.getSpawnerX()
							+ (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble())
									* (double) this.spawnRange;
					double var7 = (double) (this.getSpawnerY() + this.getSpawnerWorld().rand.nextInt(3) - 1);
					double var9 = (double) this.getSpawnerZ()
							+ (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble())
									* (double) this.spawnRange;
					EntityLiving var11 = var13 instanceof EntityLiving ? (EntityLiving) var13 : null;
					var13.setLocationAndAngles(var5, var7, var9, this.getSpawnerWorld().rand.nextFloat() * 360.0F,
							0.0F);

					if (var11 == null || var11.getCanSpawnHere()) {
						this.func_98265_a(var13);
						this.getSpawnerWorld().playAuxSFX(2004, this.getSpawnerX(), this.getSpawnerY(),
								this.getSpawnerZ(), 0);

						if (var11 != null) {
							var11.spawnExplosionParticle();
						}

						var12 = true;
					}
				}

				if (var12) {
					this.func_98273_j();
				}
			}
		}
	}

	public Entity func_98265_a(Entity par1Entity) {
		if (this.getRandomMinecart() != null) {
			NBTTagCompound var2 = new NBTTagCompound();
			par1Entity.addEntityID(var2);
			Iterator var3 = this.getRandomMinecart().field_98222_b.getTags().iterator();

			while (var3.hasNext()) {
				NBTBase var4 = (NBTBase) var3.next();
				var2.setTag(var4.getName(), var4.copy());
			}

			par1Entity.readFromNBT(var2);

			if (par1Entity.worldObj != null) {
				par1Entity.worldObj.spawnEntityInWorld(par1Entity);
			}

			NBTTagCompound var10;

			for (Entity var9 = par1Entity; var2.hasKey("Riding"); var2 = var10) {
				var10 = var2.getCompoundTag("Riding");
				Entity var5 = EntityList.createEntityByName(var10.getString("id"), this.getSpawnerWorld());

				if (var5 != null) {
					NBTTagCompound var6 = new NBTTagCompound();
					var5.addEntityID(var6);
					Iterator var7 = var10.getTags().iterator();

					while (var7.hasNext()) {
						NBTBase var8 = (NBTBase) var7.next();
						var6.setTag(var8.getName(), var8.copy());
					}

					var5.readFromNBT(var6);
					var5.setLocationAndAngles(var9.posX, var9.posY, var9.posZ, var9.rotationYaw, var9.rotationPitch);
					this.getSpawnerWorld().spawnEntityInWorld(var5);
					var9.mountEntity(var5);
				}

				var9 = var5;
			}
		} else if (par1Entity instanceof EntityLiving && par1Entity.worldObj != null) {
			((EntityLiving) par1Entity).initCreature();
			this.getSpawnerWorld().spawnEntityInWorld(par1Entity);
		}

		return par1Entity;
	}

	private void func_98273_j() {
		if (this.maxSpawnDelay <= this.minSpawnDelay) {
			this.spawnDelay = this.minSpawnDelay;
		} else {
			int var10003 = this.maxSpawnDelay - this.minSpawnDelay;
			this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(var10003);
		}

		if (this.minecartToSpawn != null && this.minecartToSpawn.size() > 0) {
			this.setRandomMinecart((WeightedRandomMinecart) WeightedRandom.getRandomItem(this.getSpawnerWorld().rand,
					this.minecartToSpawn));
		}

		this.func_98267_a(1);
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.mobID = par1NBTTagCompound.getString("EntityId");
		this.spawnDelay = par1NBTTagCompound.getShort("Delay");

		if (par1NBTTagCompound.hasKey("SpawnPotentials")) {
			this.minecartToSpawn = new ArrayList();
			NBTTagList var2 = par1NBTTagCompound.getTagList("SpawnPotentials");

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				this.minecartToSpawn.add(new WeightedRandomMinecart(this, (NBTTagCompound) var2.tagAt(var3)));
			}
		} else {
			this.minecartToSpawn = null;
		}

		if (par1NBTTagCompound.hasKey("SpawnData")) {
			this.setRandomMinecart(
					new WeightedRandomMinecart(this, par1NBTTagCompound.getCompoundTag("SpawnData"), this.mobID));
		} else {
			this.setRandomMinecart((WeightedRandomMinecart) null);
		}

		if (par1NBTTagCompound.hasKey("MinSpawnDelay")) {
			this.minSpawnDelay = par1NBTTagCompound.getShort("MinSpawnDelay");
			this.maxSpawnDelay = par1NBTTagCompound.getShort("MaxSpawnDelay");
			this.spawnCount = par1NBTTagCompound.getShort("SpawnCount");
		}

		if (par1NBTTagCompound.hasKey("MaxNearbyEntities")) {
			this.maxNearbyEntities = par1NBTTagCompound.getShort("MaxNearbyEntities");
			this.activatingRangeFromPlayer = par1NBTTagCompound.getShort("RequiredPlayerRange");
		}

		if (par1NBTTagCompound.hasKey("SpawnRange")) {
			this.spawnRange = par1NBTTagCompound.getShort("SpawnRange");
		}

		if (this.getSpawnerWorld() != null && this.getSpawnerWorld().isRemote) {
			this.field_98291_j = null;
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setString("EntityId", this.getEntityNameToSpawn());
		par1NBTTagCompound.setShort("Delay", (short) this.spawnDelay);
		par1NBTTagCompound.setShort("MinSpawnDelay", (short) this.minSpawnDelay);
		par1NBTTagCompound.setShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
		par1NBTTagCompound.setShort("SpawnCount", (short) this.spawnCount);
		par1NBTTagCompound.setShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
		par1NBTTagCompound.setShort("RequiredPlayerRange", (short) this.activatingRangeFromPlayer);
		par1NBTTagCompound.setShort("SpawnRange", (short) this.spawnRange);

		if (this.getRandomMinecart() != null) {
			par1NBTTagCompound.setCompoundTag("SpawnData",
					(NBTTagCompound) this.getRandomMinecart().field_98222_b.copy());
		}

		if (this.getRandomMinecart() != null || this.minecartToSpawn != null && this.minecartToSpawn.size() > 0) {
			NBTTagList var2 = new NBTTagList();

			if (this.minecartToSpawn != null && this.minecartToSpawn.size() > 0) {
				Iterator var3 = this.minecartToSpawn.iterator();

				while (var3.hasNext()) {
					WeightedRandomMinecart var4 = (WeightedRandomMinecart) var3.next();
					var2.appendTag(var4.func_98220_a());
				}
			} else {
				var2.appendTag(this.getRandomMinecart().func_98220_a());
			}

			par1NBTTagCompound.setTag("SpawnPotentials", var2);
		}
	}

	/**
	 * Sets the delay to minDelay if parameter given is 1, else return false.
	 */
	public boolean setDelayToMin(int par1) {
		if (par1 == 1 && this.getSpawnerWorld().isRemote) {
			this.spawnDelay = this.minSpawnDelay;
			return true;
		} else {
			return false;
		}
	}

	public WeightedRandomMinecart getRandomMinecart() {
		return this.randomMinecart;
	}

	public void setRandomMinecart(WeightedRandomMinecart par1WeightedRandomMinecart) {
		this.randomMinecart = par1WeightedRandomMinecart;
	}

	public abstract void func_98267_a(int var1);

	public abstract World getSpawnerWorld();

	public abstract int getSpawnerX();

	public abstract int getSpawnerY();

	public abstract int getSpawnerZ();
}
