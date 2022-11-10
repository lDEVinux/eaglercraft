package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EntityTrackerEntry {
	/** The entity that this EntityTrackerEntry tracks. */
	public Entity trackedEntity;
	public int trackingDistanceThreshold;

	/** check for sync when ticks % updateFrequency==0 */
	public int updateFrequency;

	/** The encoded entity X position. */
	public int encodedPosX;

	/** The encoded entity Y position. */
	public int encodedPosY;

	/** The encoded entity Z position. */
	public int encodedPosZ;

	/** The encoded entity yaw rotation. */
	public int encodedRotationYaw;

	/** The encoded entity pitch rotation. */
	public int encodedRotationPitch;
	public int lastHeadMotion;
	public double lastTrackedEntityMotionX;
	public double lastTrackedEntityMotionY;
	public double motionZ;
	public int updateCounter = 0;
	private double lastTrackedEntityPosX;
	private double lastTrackedEntityPosY;
	private double lastTrackedEntityPosZ;
	private boolean firstUpdateDone = false;
	private boolean sendVelocityUpdates;

	/**
	 * every 400 ticks a full teleport packet is sent, rather than just a "move me
	 * +x" command, so that position remains fully synced.
	 */
	private int ticksSinceLastForcedTeleport = 0;
	private Entity field_85178_v;
	private boolean ridingEntity = false;
	public boolean playerEntitiesUpdated = false;

	/**
	 * Holds references to all the players that are currently receiving position
	 * updates for this entity.
	 */
	public Set trackingPlayers = new HashSet();

	public EntityTrackerEntry(Entity par1Entity, int par2, int par3, boolean par4) {
		this.trackedEntity = par1Entity;
		this.trackingDistanceThreshold = par2;
		this.updateFrequency = par3;
		this.sendVelocityUpdates = par4;
		this.encodedPosX = MathHelper.floor_double(par1Entity.posX * 32.0D);
		this.encodedPosY = MathHelper.floor_double(par1Entity.posY * 32.0D);
		this.encodedPosZ = MathHelper.floor_double(par1Entity.posZ * 32.0D);
		this.encodedRotationYaw = MathHelper.floor_float(par1Entity.rotationYaw * 256.0F / 360.0F);
		this.encodedRotationPitch = MathHelper.floor_float(par1Entity.rotationPitch * 256.0F / 360.0F);
		this.lastHeadMotion = MathHelper.floor_float(par1Entity.getRotationYawHead() * 256.0F / 360.0F);
	}

	public boolean equals(Object par1Obj) {
		return par1Obj instanceof EntityTrackerEntry
				? ((EntityTrackerEntry) par1Obj).trackedEntity.entityId == this.trackedEntity.entityId
				: false;
	}

	public int hashCode() {
		return this.trackedEntity.entityId;
	}

	public void updatePlayerList(List par1List) {
		this.playerEntitiesUpdated = false;

		if (!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX,
				this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0D) {
			this.lastTrackedEntityPosX = this.trackedEntity.posX;
			this.lastTrackedEntityPosY = this.trackedEntity.posY;
			this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
			this.firstUpdateDone = true;
			this.playerEntitiesUpdated = true;
			this.updatePlayerEntities(par1List);
		}

		if (this.field_85178_v != this.trackedEntity.ridingEntity
				|| this.trackedEntity.ridingEntity != null && this.updateCounter % 60 == 0) {
			this.field_85178_v = this.trackedEntity.ridingEntity;
			this.sendPacketToTrackedPlayers(
					new Packet39AttachEntity(this.trackedEntity, this.trackedEntity.ridingEntity));
		}

		if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
			EntityItemFrame var23 = (EntityItemFrame) this.trackedEntity;
			ItemStack var24 = var23.getDisplayedItem();

			if (var24 != null && var24.getItem() instanceof ItemMap) {
				MapData var26 = Item.map.getMapData(var24, this.trackedEntity.worldObj);
				Iterator var29 = par1List.iterator();

				while (var29.hasNext()) {
					EntityPlayer var30 = (EntityPlayer) var29.next();
					EntityPlayerMP var31 = (EntityPlayerMP) var30;
					var26.updateVisiblePlayers(var31, var24);

					if (var31.playerNetServerHandler.getNumChunkDataPackets() <= 5) {
						Packet var32 = Item.map.getUpdatePacket(var24, this.trackedEntity.worldObj, var31);

						if (var32 != null) {
							var31.playerNetServerHandler.sendPacket(var32);
						}
					}
				}
			}

			DataWatcher var27 = this.trackedEntity.getDataWatcher();

			if (var27.hasObjectChanged()) {
				this.sendPacketToTrackedPlayersAndTrackedEntity(
						new Packet40EntityMetadata(this.trackedEntity.entityId, var27, false));
			}
		} else if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne
				|| this.trackedEntity.getDataWatcher().hasObjectChanged()) {
			int var2;
			int var3;

			if (this.trackedEntity.ridingEntity == null) {
				++this.ticksSinceLastForcedTeleport;
				var2 = this.trackedEntity.myEntitySize.multiplyBy32AndRound(this.trackedEntity.posX);
				var3 = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
				int var4 = this.trackedEntity.myEntitySize.multiplyBy32AndRound(this.trackedEntity.posZ);
				int var5 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
				int var6 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
				int var7 = var2 - this.encodedPosX;
				int var8 = var3 - this.encodedPosY;
				int var9 = var4 - this.encodedPosZ;
				Object var10 = null;
				boolean var11 = Math.abs(var7) >= 4 || Math.abs(var8) >= 4 || Math.abs(var9) >= 4
						|| this.updateCounter % 60 == 0;
				boolean var12 = Math.abs(var5 - this.encodedRotationYaw) >= 4
						|| Math.abs(var6 - this.encodedRotationPitch) >= 4;

				if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow) {
					if (var7 >= -128 && var7 < 128 && var8 >= -128 && var8 < 128 && var9 >= -128 && var9 < 128
							&& this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity) {
						if (var11 && var12) {
							var10 = new Packet33RelEntityMoveLook(this.trackedEntity.entityId, (byte) var7, (byte) var8,
									(byte) var9, (byte) var5, (byte) var6);
						} else if (var11) {
							var10 = new Packet31RelEntityMove(this.trackedEntity.entityId, (byte) var7, (byte) var8,
									(byte) var9);
						} else if (var12) {
							var10 = new Packet32EntityLook(this.trackedEntity.entityId, (byte) var5, (byte) var6);
						}
					} else {
						this.ticksSinceLastForcedTeleport = 0;
						var10 = new Packet34EntityTeleport(this.trackedEntity.entityId, var2, var3, var4, (byte) var5,
								(byte) var6);
					}
				}

				if (this.sendVelocityUpdates) {
					double var13 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
					double var15 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
					double var17 = this.trackedEntity.motionZ - this.motionZ;
					double var19 = 0.02D;
					double var21 = var13 * var13 + var15 * var15 + var17 * var17;

					if (var21 > var19 * var19 || var21 > 0.0D && this.trackedEntity.motionX == 0.0D
							&& this.trackedEntity.motionY == 0.0D && this.trackedEntity.motionZ == 0.0D) {
						this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
						this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
						this.motionZ = this.trackedEntity.motionZ;
						this.sendPacketToTrackedPlayers(new Packet28EntityVelocity(this.trackedEntity.entityId,
								this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
					}
				}

				if (var10 != null) {
					this.sendPacketToTrackedPlayers((Packet) var10);
				}

				DataWatcher var33 = this.trackedEntity.getDataWatcher();

				if (var33.hasObjectChanged()) {
					this.sendPacketToTrackedPlayersAndTrackedEntity(
							new Packet40EntityMetadata(this.trackedEntity.entityId, var33, false));
				}

				if (var11) {
					this.encodedPosX = var2;
					this.encodedPosY = var3;
					this.encodedPosZ = var4;
				}

				if (var12) {
					this.encodedRotationYaw = var5;
					this.encodedRotationPitch = var6;
				}

				this.ridingEntity = false;
			} else {
				var2 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
				var3 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
				boolean var25 = Math.abs(var2 - this.encodedRotationYaw) >= 4
						|| Math.abs(var3 - this.encodedRotationPitch) >= 4;

				if (var25) {
					this.sendPacketToTrackedPlayers(
							new Packet32EntityLook(this.trackedEntity.entityId, (byte) var2, (byte) var3));
					this.encodedRotationYaw = var2;
					this.encodedRotationPitch = var3;
				}

				this.encodedPosX = this.trackedEntity.myEntitySize.multiplyBy32AndRound(this.trackedEntity.posX);
				this.encodedPosY = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
				this.encodedPosZ = this.trackedEntity.myEntitySize.multiplyBy32AndRound(this.trackedEntity.posZ);
				DataWatcher var28 = this.trackedEntity.getDataWatcher();

				if (var28.hasObjectChanged()) {
					this.sendPacketToTrackedPlayersAndTrackedEntity(
							new Packet40EntityMetadata(this.trackedEntity.entityId, var28, false));
				}

				this.ridingEntity = true;
			}

			var2 = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);

			if (Math.abs(var2 - this.lastHeadMotion) >= 4) {
				this.sendPacketToTrackedPlayers(
						new Packet35EntityHeadRotation(this.trackedEntity.entityId, (byte) var2));
				this.lastHeadMotion = var2;
			}

			this.trackedEntity.isAirBorne = false;
		}

		++this.updateCounter;

		if (this.trackedEntity.velocityChanged) {
			this.sendPacketToTrackedPlayersAndTrackedEntity(new Packet28EntityVelocity(this.trackedEntity));
			this.trackedEntity.velocityChanged = false;
		}
	}

	public void sendPacketToTrackedPlayers(Packet par1Packet) {
		Iterator var2 = this.trackingPlayers.iterator();

		while (var2.hasNext()) {
			EntityPlayerMP var3 = (EntityPlayerMP) var2.next();
			var3.playerNetServerHandler.sendPacket(par1Packet);
		}
	}

	public void sendPacketToTrackedPlayersAndTrackedEntity(Packet par1Packet) {
		this.sendPacketToTrackedPlayers(par1Packet);

		if (this.trackedEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP) this.trackedEntity).playerNetServerHandler.sendPacket(par1Packet);
		}
	}

	public void sendDestroyEntityPacketToTrackedPlayers() {
		sendDestroyEntityPacketToTrackedPlayers(false);
	}

	public void sendDestroyEntityPacketToTrackedPlayers(boolean asap) {
		Iterator var1 = this.trackingPlayers.iterator();

		while (var1.hasNext()) {
			EntityPlayerMP var2 = (EntityPlayerMP) var1.next();
			//System.out.println(this.trackedEntity.getEntityName() + ": sendDestroyEntityPacketToTrackedPlayers");
			if (asap) {
				var2.playerNetServerHandler.sendPacket(new Packet29DestroyEntity(Integer.valueOf(this.trackedEntity.entityId)));
			} else {
				var2.destroyedItemsNetCache.add(Integer.valueOf(this.trackedEntity.entityId));
			}
		}
	}

	public void removeFromTrackedPlayers(EntityPlayerMP par1EntityPlayerMP) {
		removeFromTrackedPlayers(par1EntityPlayerMP, false);
	}
	public void removeFromTrackedPlayers(EntityPlayerMP par1EntityPlayerMP, boolean asap) {
		if (this.trackingPlayers.contains(par1EntityPlayerMP)) {
			//System.out.println(this.trackedEntity.getEntityName() + ": removeFromTrackedPlayers");
			if (asap) {
				par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet29DestroyEntity(Integer.valueOf(this.trackedEntity.entityId)));
			} else {
				par1EntityPlayerMP.destroyedItemsNetCache.add(Integer.valueOf(this.trackedEntity.entityId));
			}
			this.trackingPlayers.remove(par1EntityPlayerMP);
		}
	}

	public void updatePlayerEntity(EntityPlayerMP par1EntityPlayerMP) {
		if (par1EntityPlayerMP != this.trackedEntity) {
			double var2 = par1EntityPlayerMP.posX - (double) (this.encodedPosX / 32);
			double var4 = par1EntityPlayerMP.posZ - (double) (this.encodedPosZ / 32);

			if (var2 >= (double) (-this.trackingDistanceThreshold) && var2 <= (double) this.trackingDistanceThreshold
					&& var4 >= (double) (-this.trackingDistanceThreshold)
					&& var4 <= (double) this.trackingDistanceThreshold) {
				if (!this.trackingPlayers.contains(par1EntityPlayerMP)
						&& (this.isPlayerWatchingThisChunk(par1EntityPlayerMP) || this.trackedEntity.field_98038_p)) {
					this.trackingPlayers.add(par1EntityPlayerMP);
					Packet var6 = this.getSpawnPacket();
					par1EntityPlayerMP.playerNetServerHandler.sendPacket(var6);

					if (!this.trackedEntity.getDataWatcher().getIsBlank()) {
						par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet40EntityMetadata(
								this.trackedEntity.entityId, this.trackedEntity.getDataWatcher(), true));
					}

					this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
					this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
					this.motionZ = this.trackedEntity.motionZ;

					if (this.sendVelocityUpdates && !(var6 instanceof Packet24MobSpawn)) {
						par1EntityPlayerMP.playerNetServerHandler.sendPacket(
								new Packet28EntityVelocity(this.trackedEntity.entityId, this.trackedEntity.motionX,
										this.trackedEntity.motionY, this.trackedEntity.motionZ));
					}

					if (this.trackedEntity.ridingEntity != null) {
						par1EntityPlayerMP.playerNetServerHandler.sendPacket(
								new Packet39AttachEntity(this.trackedEntity, this.trackedEntity.ridingEntity));
					}

					if (this.trackedEntity instanceof EntityLiving) {
						for (int var7 = 0; var7 < 5; ++var7) {
							ItemStack var8 = ((EntityLiving) this.trackedEntity).getEquipmentInSlot(var7);

							if (var8 != null) {
								par1EntityPlayerMP.playerNetServerHandler.sendPacket(
										new Packet5PlayerInventory(this.trackedEntity.entityId, var7, var8));
							}
						}
					}

					if (this.trackedEntity instanceof EntityPlayer) {
						EntityPlayer var10 = (EntityPlayer) this.trackedEntity;

						if (var10.isPlayerSleeping()) {
							par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet17Sleep(this.trackedEntity,
									0, MathHelper.floor_double(this.trackedEntity.posX),
									MathHelper.floor_double(this.trackedEntity.posY),
									MathHelper.floor_double(this.trackedEntity.posZ)));
						}
					}

					if (this.trackedEntity instanceof EntityLiving) {
						EntityLiving var11 = (EntityLiving) this.trackedEntity;
						Iterator var12 = var11.getActivePotionEffects().iterator();

						while (var12.hasNext()) {
							PotionEffect var9 = (PotionEffect) var12.next();
							par1EntityPlayerMP.playerNetServerHandler
									.sendPacket(new Packet41EntityEffect(this.trackedEntity.entityId, var9));
						}
					}
				}
			} else if (this.trackingPlayers.contains(par1EntityPlayerMP)) {
				this.trackingPlayers.remove(par1EntityPlayerMP);
				//System.out.println(this.trackedEntity.getEntityName() + ": updatePlayerEntity");
				par1EntityPlayerMP.destroyedItemsNetCache.add(Integer.valueOf(this.trackedEntity.entityId));
			}
		}
	}

	private boolean isPlayerWatchingThisChunk(EntityPlayerMP par1EntityPlayerMP) {
		return par1EntityPlayerMP.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(par1EntityPlayerMP,
				this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
	}

	public void updatePlayerEntities(List par1List) {
		for (int var2 = 0; var2 < par1List.size(); ++var2) {
			this.updatePlayerEntity((EntityPlayerMP) par1List.get(var2));
		}
	}

	private Packet getSpawnPacket() {
		if (this.trackedEntity.isDead) {
			this.trackedEntity.worldObj.getWorldLogAgent().func_98236_b("Fetching addPacket for removed entity");
		}

		if (this.trackedEntity instanceof EntityItem) {
			return new Packet23VehicleSpawn(this.trackedEntity, 2, 1);
		} else if (this.trackedEntity instanceof EntityPlayerMP) {
			return new Packet20NamedEntitySpawn((EntityPlayer) this.trackedEntity);
		} else if (this.trackedEntity instanceof EntityMinecart) {
			EntityMinecart var8 = (EntityMinecart) this.trackedEntity;
			return new Packet23VehicleSpawn(this.trackedEntity, 10, var8.getMinecartType());
		} else if (this.trackedEntity instanceof EntityBoat) {
			return new Packet23VehicleSpawn(this.trackedEntity, 1);
		} else if (!(this.trackedEntity instanceof IAnimals) && !(this.trackedEntity instanceof EntityDragon)) {
			if (this.trackedEntity instanceof EntityFishHook) {
				EntityPlayer var7 = ((EntityFishHook) this.trackedEntity).angler;
				return new Packet23VehicleSpawn(this.trackedEntity, 90,
						var7 != null ? var7.entityId : this.trackedEntity.entityId);
			} else if (this.trackedEntity instanceof EntityArrow) {
				Entity var6 = ((EntityArrow) this.trackedEntity).shootingEntity;
				return new Packet23VehicleSpawn(this.trackedEntity, 60,
						var6 != null ? var6.entityId : this.trackedEntity.entityId);
			} else if (this.trackedEntity instanceof EntitySnowball) {
				return new Packet23VehicleSpawn(this.trackedEntity, 61);
			} else if (this.trackedEntity instanceof EntityPotion) {
				return new Packet23VehicleSpawn(this.trackedEntity, 73,
						((EntityPotion) this.trackedEntity).getPotionDamage());
			} else if (this.trackedEntity instanceof EntityExpBottle) {
				return new Packet23VehicleSpawn(this.trackedEntity, 75);
			} else if (this.trackedEntity instanceof EntityEnderPearl) {
				return new Packet23VehicleSpawn(this.trackedEntity, 65);
			} else if (this.trackedEntity instanceof EntityEnderEye) {
				return new Packet23VehicleSpawn(this.trackedEntity, 72);
			} else if (this.trackedEntity instanceof EntityFireworkRocket) {
				return new Packet23VehicleSpawn(this.trackedEntity, 76);
			} else {
				Packet23VehicleSpawn var2;

				if (this.trackedEntity instanceof EntityFireball) {
					EntityFireball var5 = (EntityFireball) this.trackedEntity;
					var2 = null;
					byte var3 = 63;

					if (this.trackedEntity instanceof EntitySmallFireball) {
						var3 = 64;
					} else if (this.trackedEntity instanceof EntityWitherSkull) {
						var3 = 66;
					}

					if (var5.shootingEntity != null) {
						var2 = new Packet23VehicleSpawn(this.trackedEntity, var3,
								((EntityFireball) this.trackedEntity).shootingEntity.entityId);
					} else {
						var2 = new Packet23VehicleSpawn(this.trackedEntity, var3, 0);
					}

					var2.speedX = (int) (var5.accelerationX * 8000.0D);
					var2.speedY = (int) (var5.accelerationY * 8000.0D);
					var2.speedZ = (int) (var5.accelerationZ * 8000.0D);
					return var2;
				} else if (this.trackedEntity instanceof EntityEgg) {
					return new Packet23VehicleSpawn(this.trackedEntity, 62);
				} else if (this.trackedEntity instanceof EntityTNTPrimed) {
					return new Packet23VehicleSpawn(this.trackedEntity, 50);
				} else if (this.trackedEntity instanceof EntityEnderCrystal) {
					return new Packet23VehicleSpawn(this.trackedEntity, 51);
				} else if (this.trackedEntity instanceof EntityFallingSand) {
					EntityFallingSand var4 = (EntityFallingSand) this.trackedEntity;
					return new Packet23VehicleSpawn(this.trackedEntity, 70, var4.blockID | var4.metadata << 16);
				} else if (this.trackedEntity instanceof EntityPainting) {
					return new Packet25EntityPainting((EntityPainting) this.trackedEntity);
				} else if (this.trackedEntity instanceof EntityItemFrame) {
					EntityItemFrame var1 = (EntityItemFrame) this.trackedEntity;
					var2 = new Packet23VehicleSpawn(this.trackedEntity, 71, var1.hangingDirection);
					var2.xPosition = MathHelper.floor_float((float) (var1.xPosition * 32));
					var2.yPosition = MathHelper.floor_float((float) (var1.yPosition * 32));
					var2.zPosition = MathHelper.floor_float((float) (var1.zPosition * 32));
					return var2;
				} else if (this.trackedEntity instanceof EntityXPOrb) {
					return new Packet26EntityExpOrb((EntityXPOrb) this.trackedEntity);
				} else {
					throw new IllegalArgumentException("Don\'t know how to add " + this.trackedEntity.getClass() + "!");
				}
			}
		} else {
			this.lastHeadMotion = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
			return new Packet24MobSpawn((EntityLiving) this.trackedEntity);
		}
	}

	/**
	 * Remove a tracked player from our list and tell the tracked player to destroy
	 * us from their world.
	 */
	public void removeTrackedPlayerSymmetric(EntityPlayerMP par1EntityPlayerMP) {
		removeTrackedPlayerSymmetric(par1EntityPlayerMP, false);
	}
	public void removeTrackedPlayerSymmetric(EntityPlayerMP par1EntityPlayerMP, boolean asap) {
		if (this.trackingPlayers.contains(par1EntityPlayerMP)) {
			this.trackingPlayers.remove(par1EntityPlayerMP);
			//System.out.println(this.trackedEntity.getEntityName() + ": removeTrackedPlayerSymmetric");
			if (asap) {
				par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet29DestroyEntity(Integer.valueOf(this.trackedEntity.entityId)));
			} else {
				par1EntityPlayerMP.destroyedItemsNetCache.add(Integer.valueOf(this.trackedEntity.entityId));
			}
		}
	}
}
