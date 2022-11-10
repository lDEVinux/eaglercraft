package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.sp.EaglerUUID;
import net.minecraft.server.MinecraftServer;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {
	private StringTranslate translator = StringTranslate.getInstance();

	/**
	 * The NetServerHandler assigned to this player by the
	 * ServerConfigurationManager.
	 */
	public NetServerHandler playerNetServerHandler;

	/** Reference to the MinecraftServer object. */
	public MinecraftServer mcServer;

	/** The ItemInWorldManager belonging to this player */
	public ItemInWorldManager theItemInWorldManager;

	/** player X position as seen by PlayerManager */
	public double managedPosX;

	/** player Z position as seen by PlayerManager */
	public double managedPosZ;

	/** LinkedList that holds the loaded chunks. */
	public final List loadedChunks = new LinkedList();

	/** entities added to this list will be packet29'd to the player */
	public final List destroyedItemsNetCache = new LinkedList();

	/** amount of health the client was last set to */
	private int lastHealth = -99999999;

	/** set to foodStats.GetFoodLevel */
	private int lastFoodLevel = -99999999;

	/** set to foodStats.getSaturationLevel() == 0.0F each tick */
	private boolean wasHungry = true;

	/** Amount of experience the client was last set to */
	private int lastExperience = -99999999;

	/** how many ticks of invulnerability(spawn protection) this player has */
	private int ticksOfInvuln = 60;

	/** must be between 3>x>15 (strictly between) */
	public int renderDistance = 4;
	public int lastRenderDistance = 4;
	public int chatVisibility = 0;
	private boolean chatColours = true;

	/**
	 * The currently in use window ID. Incremented every time a window is opened.
	 */
	private int currentWindowId = 0;

	/**
	 * set to true when player is moving quantity of items from one inventory to
	 * another(crafting) but item in either slot is not changed
	 */
	public boolean isChangingQuantityOnly;
	public int ping;

	/**
	 * Set when a player beats the ender dragon, used to respawn the player at the
	 * spawn point while retaining inventory and XP
	 */
	public boolean playerConqueredTheEnd = false;

	public EntityPlayerMP(MinecraftServer par1MinecraftServer, World par2World, String par3Str,
			ItemInWorldManager par4ItemInWorldManager) {
		super(par2World);
		par4ItemInWorldManager.thisPlayerMP = this;
		this.theItemInWorldManager = par4ItemInWorldManager;
		ChunkCoordinates var5 = par2World.getSpawnPoint();
		int var6 = var5.posX;
		int var7 = var5.posZ;
		int var8 = var5.posY;

		if (!par2World.provider.hasNoSky && par2World.getWorldInfo().getGameType() != EnumGameType.ADVENTURE) {
			int var9 = Math.max(5, par1MinecraftServer.getSpawnProtectionSize() - 6);
			var6 += this.rand.nextInt(var9 * 2) - var9;
			var7 += this.rand.nextInt(var9 * 2) - var9;
			var8 = par2World.getTopSolidOrLiquidBlock(var6, var7);
		}

		this.mcServer = par1MinecraftServer;
		this.stepHeight = 0.0F;
		this.username = par3Str;
		this.yOffset = 0.0F;
		this.setLocationAndAngles((double) var6 + 0.5D, (double) var8, (double) var7 + 0.5D, 0.0F, 0.0F);

		while (!par2World.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
			this.setPosition(this.posX, this.posY + 1.0D, this.posZ);
		}
		
		this.entityUniqueID = EaglerUUID.nameUUIDFromBytes(("OfflinePlayer:" + par3Str).getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.hasKey("playerGameType")) {
			if (MinecraftServer.getServer().func_104056_am()) {
				this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
			} else {
				this.theItemInWorldManager
						.setGameType(EnumGameType.getByID(par1NBTTagCompound.getInteger("playerGameType")));
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
	}

	/**
	 * Add experience levels to this player.
	 */
	public void addExperienceLevel(int par1) {
		super.addExperienceLevel(par1);
		this.lastExperience = -1;
	}

	public void addSelfToInternalCraftingInventory() {
		this.openContainer.onCraftGuiOpened(this);
	}

	/**
	 * sets the players height back to normal after doing things like sleeping and
	 * dieing
	 */
	protected void resetHeight() {
		this.yOffset = 0.0F;
	}

	public float getEyeHeight() {
		return 1.62F;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.theItemInWorldManager.updateBlockRemoving();
		--this.ticksOfInvuln;
		this.openContainer.detectAndSendChanges();

		while (!this.destroyedItemsNetCache.isEmpty()) {
			int var1 = Math.min(this.destroyedItemsNetCache.size(), 127);
			int[] var2 = new int[var1];
			Iterator var3 = this.destroyedItemsNetCache.iterator();
			int var4 = 0;

			while (var3.hasNext() && var4 < var1) {
				var2[var4++] = ((Integer) var3.next()).intValue();
				var3.remove();
			}

			this.playerNetServerHandler.sendPacket(new Packet29DestroyEntity(var2));
		}

		if (!this.loadedChunks.isEmpty()) {
			ArrayList var6 = new ArrayList();
			Iterator var7 = this.loadedChunks.iterator();
			ArrayList var8 = new ArrayList();

			while (var7.hasNext() && var6.size() < this.renderDistance / 2) {
				ChunkCoordIntPair var9 = (ChunkCoordIntPair) var7.next();
				var7.remove();

				if (var9 != null && this.worldObj.blockExists(var9.chunkXPos << 4, 0, var9.chunkZPos << 4)) {
					var6.add(this.worldObj.getChunkFromChunkCoords(var9.chunkXPos, var9.chunkZPos));
					var8.addAll(((WorldServer) this.worldObj).getTileEntityList(var9.chunkXPos * 16, 0,
							var9.chunkZPos * 16, var9.chunkXPos * 16 + 16, 256, var9.chunkZPos * 16 + 16));
				}
			}

			if (!var6.isEmpty()) {
				this.playerNetServerHandler.sendPacket(new Packet56MapChunks(var6));
				Iterator var10 = var8.iterator();

				while (var10.hasNext()) {
					TileEntity var5 = (TileEntity) var10.next();
					this.getTileEntityInfo(var5);
				}

				var10 = var6.iterator();

				while (var10.hasNext()) {
					Chunk var11 = (Chunk) var10.next();
					this.getServerForPlayer().getEntityTracker().func_85172_a(this, var11);
				}
			}
		}
	}

	public void setEntityHealth(int par1) {
		super.setEntityHealth(par1);
		Collection var2 = this.getWorldScoreboard().func_96520_a(ScoreObjectiveCriteria.field_96638_f);
		Iterator var3 = var2.iterator();

		while (var3.hasNext()) {
			ScoreObjective var4 = (ScoreObjective) var3.next();
			this.getWorldScoreboard().func_96529_a(this.getEntityName(), var4)
					.func_96651_a(Arrays.asList(new EntityPlayer[] { this }));
		}
	}

	public void onUpdateEntity() {
		super.onUpdate();

		for (int var1 = 0; var1 < this.inventory.getSizeInventory(); ++var1) {
			ItemStack var5 = this.inventory.getStackInSlot(var1);

			if (var5 != null && Item.itemsList[var5.itemID].isMap()
					&& this.playerNetServerHandler.getNumChunkDataPackets() <= 5) {
				Packet var6 = ((ItemMapBase) Item.itemsList[var5.itemID]).getUpdatePacket(var5, this.worldObj,
						this);

				if (var6 != null) {
					this.playerNetServerHandler.sendPacket(var6);
				}
			}
		}

		if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel()
				|| this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry) {
			this.playerNetServerHandler.sendPacket(new Packet8UpdateHealth(this.getHealth(),
					this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
			this.lastHealth = this.getHealth();
			this.lastFoodLevel = this.foodStats.getFoodLevel();
			this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
		}

		if (this.experienceTotal != this.lastExperience) {
			this.lastExperience = this.experienceTotal;
			this.playerNetServerHandler.sendPacket(
					new Packet43Experience(this.experience, this.experienceTotal, this.experienceLevel));
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {
		this.mcServer.getConfigurationManager().sendChatMsg(this.field_94063_bt.func_94546_b());

		if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
			this.inventory.dropAllItems();
		}

		Collection var2 = this.worldObj.getScoreboard().func_96520_a(ScoreObjectiveCriteria.field_96642_c);
		Iterator var3 = var2.iterator();

		while (var3.hasNext()) {
			ScoreObjective var4 = (ScoreObjective) var3.next();
			Score var5 = this.getWorldScoreboard().func_96529_a(this.getEntityName(), var4);
			var5.func_96648_a();
		}

		EntityLiving var6 = this.func_94060_bK();

		if (var6 != null) {
			var6.addToPlayerScore(this, this.scoreValue);
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else {
			boolean var3 = this.mcServer.isDedicatedServer() && this.mcServer.isPVPEnabled()
					&& "fall".equals(par1DamageSource.damageType);

			if (!var3 && this.ticksOfInvuln > 0 && par1DamageSource != DamageSource.outOfWorld) {
				return false;
			} else {
				if (par1DamageSource instanceof EntityDamageSource) {
					Entity var4 = par1DamageSource.getEntity();

					if (var4 instanceof EntityPlayer && !this.func_96122_a((EntityPlayer) var4)) {
						return false;
					}

					if (var4 instanceof EntityArrow) {
						EntityArrow var5 = (EntityArrow) var4;

						if (var5.shootingEntity instanceof EntityPlayer
								&& !this.func_96122_a((EntityPlayer) var5.shootingEntity)) {
							return false;
						}
					}
				}

				return super.attackEntityFrom(par1DamageSource, par2);
			}
		}
	}

	public boolean func_96122_a(EntityPlayer par1EntityPlayer) {
		return !this.mcServer.isPVPEnabled() ? false : super.func_96122_a(par1EntityPlayer);
	}

	public void travelToTheEnd(int par1) {
		if (this.dimension == 1 && par1 == 1) {
			this.triggerAchievement(AchievementList.theEnd2);
			this.worldObj.removeEntity(this);
			this.playerConqueredTheEnd = true;
			this.playerNetServerHandler.sendPacket(new Packet70GameEvent(4, 0));
		} else {
			if (this.dimension == 1 && par1 == 0) {
				this.triggerAchievement(AchievementList.theEnd);
				ChunkCoordinates var2 = this.mcServer.worldServerForDimension(par1).getEntrancePortalLocation();

				if (var2 != null) {
					this.playerNetServerHandler.setPlayerLocation((double) var2.posX, (double) var2.posY,
							(double) var2.posZ, 0.0F, 0.0F);
				}

				par1 = 1;
			} else {
				this.triggerAchievement(AchievementList.portal);
			}

			this.mcServer.getConfigurationManager().transferPlayerToDimension(this, par1);
			this.lastExperience = -1;
			this.lastHealth = -1;
			this.lastFoodLevel = -1;
		}
	}

	/**
	 * gets description packets from all TileEntity's that override func_20070
	 */
	private void getTileEntityInfo(TileEntity par1TileEntity) {
		if (par1TileEntity != null) {
			Packet var2 = par1TileEntity.getDescriptionPacket();

			if (var2 != null) {
				this.playerNetServerHandler.sendPacket(var2);
			}
		}
	}

	/**
	 * Called whenever an item is picked up from walking over it. Args:
	 * pickedUpEntity, stackSize
	 */
	public void onItemPickup(Entity par1Entity, int par2) {
		super.onItemPickup(par1Entity, par2);
		this.openContainer.detectAndSendChanges();
	}

	/**
	 * puts player to sleep on specified bed if possible
	 */
	public EnumStatus sleepInBedAt(int par1, int par2, int par3) {
		EnumStatus var4 = super.sleepInBedAt(par1, par2, par3);

		if (var4 == EnumStatus.OK) {
			Packet17Sleep var5 = new Packet17Sleep(this, 0, par1, par2, par3);
			this.getServerForPlayer().getEntityTracker().sendPacketToTrackedPlayers(this, var5);
			this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw,
					this.rotationPitch);
			this.playerNetServerHandler.sendPacket(var5);
		}

		return var4;
	}

	/**
	 * Wake up the player if they're sleeping.
	 */
	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3) {
		if (this.isPlayerSleeping()) {
			this.getServerForPlayer().getEntityTracker().sendPacketToTrackedPlayersAndTrackedEntity(this,
					new Packet18Animation(this, 3));
		}

		super.wakeUpPlayer(par1, par2, par3);

		if (this.playerNetServerHandler != null) {
			this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw,
					this.rotationPitch);
		}
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	public void mountEntity(Entity par1Entity) {
		super.mountEntity(par1Entity);
		this.playerNetServerHandler.sendPacket(new Packet39AttachEntity(this, this.ridingEntity));
		this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw,
				this.rotationPitch);
	}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on the
	 * ground to update the fall distance and deal fall damage if landing on the
	 * ground. Args: distanceFallenThisTick, onGround
	 */
	protected void updateFallState(double par1, boolean par3) {
	}

	/**
	 * process player falling based on movement packet
	 */
	public void handleFalling(double par1, boolean par3) {
		super.updateFallState(par1, par3);
	}

	/**
	 * get the next window id to use
	 */
	private void getNextWindowId() {
		this.currentWindowId = this.currentWindowId % 100 + 1;
	}

	/**
	 * Displays the crafting GUI for a workbench.
	 */
	public void displayGUIWorkbench(int par1, int par2, int par3) {
		this.getNextWindowId();
		this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 1, "Crafting", 9, true));
		this.openContainer = new ContainerWorkbench(this.inventory, this.worldObj, par1, par2, par3);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str) {
		this.getNextWindowId();
		this.playerNetServerHandler.sendPacket(
				new Packet100OpenWindow(this.currentWindowId, 4, par4Str == null ? "" : par4Str, 9, par4Str != null));
		this.openContainer = new ContainerEnchantment(this.inventory, this.worldObj, par1, par2, par3);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	/**
	 * Displays the GUI for interacting with an anvil.
	 */
	public void displayGUIAnvil(int par1, int par2, int par3) {
		this.getNextWindowId();
		this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 8, "Repairing", 9, true));
		this.openContainer = new ContainerRepair(this.inventory, this.worldObj, par1, par2, par3, this);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	/**
	 * Displays the GUI for interacting with a chest inventory. Args: chestInventory
	 */
	public void displayGUIChest(IInventory par1IInventory) {
		if (this.openContainer != this.inventoryContainer) {
			this.closeScreen();
		}

		this.getNextWindowId();
		this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 0,
				par1IInventory.getInvName(), par1IInventory.getSizeInventory(), par1IInventory.isInvNameLocalized()));
		this.openContainer = new ContainerChest(this.inventory, par1IInventory);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	public void displayGUIHopper(TileEntityHopper par1TileEntityHopper) {
		this.getNextWindowId();
		this.playerNetServerHandler
				.sendPacket(new Packet100OpenWindow(this.currentWindowId, 9, par1TileEntityHopper.getInvName(),
						par1TileEntityHopper.getSizeInventory(), par1TileEntityHopper.isInvNameLocalized()));
		this.openContainer = new ContainerHopper(this.inventory, par1TileEntityHopper);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	public void displayGUIHopperMinecart(EntityMinecartHopper par1EntityMinecartHopper) {
		this.getNextWindowId();
		this.playerNetServerHandler
				.sendPacket(new Packet100OpenWindow(this.currentWindowId, 9, par1EntityMinecartHopper.getInvName(),
						par1EntityMinecartHopper.getSizeInventory(), par1EntityMinecartHopper.isInvNameLocalized()));
		this.openContainer = new ContainerHopper(this.inventory, par1EntityMinecartHopper);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	/**
	 * Displays the furnace GUI for the passed in furnace entity. Args:
	 * tileEntityFurnace
	 */
	public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace) {
		this.getNextWindowId();
		this.playerNetServerHandler
				.sendPacket(new Packet100OpenWindow(this.currentWindowId, 2, par1TileEntityFurnace.getInvName(),
						par1TileEntityFurnace.getSizeInventory(), par1TileEntityFurnace.isInvNameLocalized()));
		this.openContainer = new ContainerFurnace(this.inventory, par1TileEntityFurnace);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	/**
	 * Displays the dipsenser GUI for the passed in dispenser entity. Args:
	 * TileEntityDispenser
	 */
	public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser) {
		this.getNextWindowId();
		this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId,
				par1TileEntityDispenser instanceof TileEntityDropper ? 10 : 3, par1TileEntityDispenser.getInvName(),
				par1TileEntityDispenser.getSizeInventory(), par1TileEntityDispenser.isInvNameLocalized()));
		this.openContainer = new ContainerDispenser(this.inventory, par1TileEntityDispenser);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	/**
	 * Displays the GUI for interacting with a brewing stand.
	 */
	public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand) {
		this.getNextWindowId();
		this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 5,
				par1TileEntityBrewingStand.getInvName(), par1TileEntityBrewingStand.getSizeInventory(),
				par1TileEntityBrewingStand.isInvNameLocalized()));
		this.openContainer = new ContainerBrewingStand(this.inventory, par1TileEntityBrewingStand);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	/**
	 * Displays the GUI for interacting with a beacon.
	 */
	public void displayGUIBeacon(TileEntityBeacon par1TileEntityBeacon) {
		this.getNextWindowId();
		this.playerNetServerHandler
				.sendPacket(new Packet100OpenWindow(this.currentWindowId, 7, par1TileEntityBeacon.getInvName(),
						par1TileEntityBeacon.getSizeInventory(), par1TileEntityBeacon.isInvNameLocalized()));
		this.openContainer = new ContainerBeacon(this.inventory, par1TileEntityBeacon);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
	}

	public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str) {
		this.getNextWindowId();
		this.openContainer = new ContainerMerchant(this.inventory, par1IMerchant, this.worldObj);
		this.openContainer.windowId = this.currentWindowId;
		this.openContainer.onCraftGuiOpened(this);
		InventoryMerchant var3 = ((ContainerMerchant) this.openContainer).getMerchantInventory();
		this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 6,
				par2Str == null ? "" : par2Str, var3.getSizeInventory(), par2Str != null));
		MerchantRecipeList var4 = par1IMerchant.getRecipes(this);

		if (var4 != null) {
			try {
				ByteArrayOutputStream var5 = new ByteArrayOutputStream();
				DataOutputStream var6 = new DataOutputStream(var5);
				var6.writeInt(this.currentWindowId);
				var4.writeRecipiesToStream(var6);
				this.playerNetServerHandler.sendPacket(new Packet250CustomPayload("MC|TrList", var5.toByteArray()));
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		}
	}

	/**
	 * Sends the contents of an inventory slot to the client-side Container. This
	 * doesn't have to match the actual contents of that slot. Args: Container, slot
	 * number, slot contents
	 */
	public void sendSlotContents(Container par1Container, int par2, ItemStack par3ItemStack) {
		if (!(par1Container.getSlot(par2) instanceof SlotCrafting)) {
			if (!this.isChangingQuantityOnly) {
				this.playerNetServerHandler
						.sendPacket(new Packet103SetSlot(par1Container.windowId, par2, par3ItemStack));
			}
		}
	}

	public void sendContainerToPlayer(Container par1Container) {
		this.updateCraftingInventory(par1Container, par1Container.getInventory());
	}

	/**
	 * update the crafting window inventory with the items in the list
	 */
	public void updateCraftingInventory(Container par1Container, List par2List) {
		this.playerNetServerHandler.sendPacket(new Packet104WindowItems(par1Container.windowId, par2List));
		this.playerNetServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
	}

	/**
	 * Sends two ints to the client-side Container. Used for furnace burning time,
	 * smelting progress, brewing progress, and enchanting level. Normally the first
	 * int identifies which variable to update, and the second contains the new
	 * value. Both are truncated to shorts in non-local SMP.
	 */
	public void sendProgressBarUpdate(Container par1Container, int par2, int par3) {
		this.playerNetServerHandler.sendPacket(new Packet105UpdateProgressbar(par1Container.windowId, par2, par3));
	}

	/**
	 * set current crafting inventory back to the 2x2 square
	 */
	public void closeScreen() {
		this.playerNetServerHandler.sendPacket(new Packet101CloseWindow(this.openContainer.windowId));
		this.closeCraftingGui();
	}

	/**
	 * updates item held by mouse
	 */
	public void updateHeldItem() {
		if (!this.isChangingQuantityOnly) {
			this.playerNetServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
		}
	}

	/**
	 * close the current crafting gui
	 */
	public void closeCraftingGui() {
		this.openContainer.onCraftGuiClosed(this);
		this.openContainer = this.inventoryContainer;
	}

	/**
	 * Adds a value to a statistic field.
	 */
	public void addStat(StatBase par1StatBase, int par2) {
		if (par1StatBase != null) {
			if (!par1StatBase.isIndependent) {
				while (par2 > 100) {
					this.playerNetServerHandler.sendPacket(new Packet200Statistic(par1StatBase.statId, 100));
					par2 -= 100;
				}

				this.playerNetServerHandler.sendPacket(new Packet200Statistic(par1StatBase.statId, par2));
			}
		}
	}

	public void mountEntityAndWakeUp() {
		if (this.riddenByEntity != null) {
			this.riddenByEntity.mountEntity(this);
		}

		if (this.sleeping) {
			this.wakeUpPlayer(true, false, false);
		}
	}

	/**
	 * this function is called when a players inventory is sent to him, lastHealth
	 * is updated on any dimension transitions, then reset.
	 */
	public void setPlayerHealthUpdated() {
		this.lastHealth = -99999999;
	}

	/**
	 * Add a chat message to the player
	 */
	public void addChatMessage(String par1Str) {
		StringTranslate var2 = StringTranslate.getInstance();
		String var3 = var2.translateKey(par1Str);
		this.playerNetServerHandler.sendPacket(new Packet3Chat(var3));
	}

	/**
	 * Used for when item use count runs out, ie: eating completed
	 */
	protected void onItemUseFinish() {
		this.playerNetServerHandler.sendPacket(new Packet38EntityStatus(this.entityId, (byte) 9));
		super.onItemUseFinish();
	}

	/**
	 * sets the itemInUse when the use item button is clicked. Args: itemstack, int
	 * maxItemUseDuration
	 */
	public void setItemInUse(ItemStack par1ItemStack, int par2) {
		super.setItemInUse(par1ItemStack, par2);

		if (par1ItemStack != null && par1ItemStack.getItem() != null
				&& par1ItemStack.getItem().getItemUseAction(par1ItemStack) == EnumAction.eat) {
			this.getServerForPlayer().getEntityTracker().sendPacketToTrackedPlayersAndTrackedEntity(this,
					new Packet18Animation(this, 5));
		}
	}

	/**
	 * Copies the values from the given player into this player if boolean par2 is
	 * true. Always clones Ender Chest Inventory.
	 */
	public void clonePlayer(EntityPlayer par1EntityPlayer, boolean par2) {
		super.clonePlayer(par1EntityPlayer, par2);
		this.renderDistance = ((EntityPlayerMP)par1EntityPlayer).renderDistance;
		this.lastExperience = -1;
		this.lastHealth = -1;
		this.lastFoodLevel = -1;
		this.destroyedItemsNetCache.addAll(((EntityPlayerMP) par1EntityPlayer).destroyedItemsNetCache);
	}

	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
		super.onNewPotionEffect(par1PotionEffect);
		this.playerNetServerHandler.sendPacket(new Packet41EntityEffect(this.entityId, par1PotionEffect));
	}

	protected void onChangedPotionEffect(PotionEffect par1PotionEffect) {
		super.onChangedPotionEffect(par1PotionEffect);
		this.playerNetServerHandler.sendPacket(new Packet41EntityEffect(this.entityId, par1PotionEffect));
	}

	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		super.onFinishedPotionEffect(par1PotionEffect);
		this.playerNetServerHandler.sendPacket(new Packet42RemoveEntityEffect(this.entityId, par1PotionEffect));
	}

	/**
	 * Sets the position of the entity and updates the 'last' variables
	 */
	public void setPositionAndUpdate(double par1, double par3, double par5) {
		this.playerNetServerHandler.setPlayerLocation(par1, par3, par5, this.rotationYaw, this.rotationPitch);
	}

	/**
	 * Called when the player performs a critical hit on the Entity. Args: entity
	 * that was hit critically
	 */
	public void onCriticalHit(Entity par1Entity) {
		this.getServerForPlayer().getEntityTracker().sendPacketToTrackedPlayersAndTrackedEntity(this,
				new Packet18Animation(par1Entity, 6));
	}

	public void onEnchantmentCritical(Entity par1Entity) {
		this.getServerForPlayer().getEntityTracker().sendPacketToTrackedPlayersAndTrackedEntity(this,
				new Packet18Animation(par1Entity, 7));
	}

	/**
	 * Sends the player's abilities to the server (if there is one).
	 */
	public void sendPlayerAbilities() {
		if (this.playerNetServerHandler != null) {
			this.playerNetServerHandler.sendPacket(new Packet202PlayerAbilities(this.capabilities));
		}
	}

	public WorldServer getServerForPlayer() {
		return (WorldServer) this.worldObj;
	}

	/**
	 * Sets the player's game mode and sends it to them.
	 */
	public void setGameType(EnumGameType par1EnumGameType) {
		this.theItemInWorldManager.setGameType(par1EnumGameType);
		this.playerNetServerHandler.sendPacket(new Packet70GameEvent(3, par1EnumGameType.getID()));
	}

	public void sendChatToPlayer(String par1Str) {
		this.playerNetServerHandler.sendPacket(new Packet3Chat(par1Str));
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 */
	public boolean canCommandSenderUseCommand(int par1, String par2Str) {
		return "seed".equals(par2Str) && !this.mcServer.isDedicatedServer() ? true
				: (!"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str)
						? this.mcServer.getConfigurationManager().areCommandsAllowed(this.username)
						: true);
	}

	/**
	 * Gets the player's IP address. Used in /banip.
	 */
	public String getPlayerIP() {
		//String var1 = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
		//var1 = var1.substring(var1.indexOf("/") + 1);
		//var1 = var1.substring(0, var1.indexOf(":"));
		return "not implemented";
	}

	public void updateClientInfo(Packet204ClientInfo par1Packet204ClientInfo) {
		
		// rip
		
		//if (this.translator.getLanguageList().containsKey(par1Packet204ClientInfo.getLanguage())) {
		//	this.translator.setLanguage(par1Packet204ClientInfo.getLanguage(), false);
		//}
		
		int var2 = 64 << 3 - par1Packet204ClientInfo.getRenderDistance();
		if(var2 > 400) {
			var2 = 400;
		}
		var2 = (var2 >> 5) + 2;

		if (var2 > 3 && var2 < 15) {
			if (this.mcServer.getServerOwner().equals(this.username)) {
				this.renderDistance = var2;
			} else {
				EntityPlayerMP fard = this.mcServer.getConfigurationManager().getPlayerEntity(this.mcServer.getServerOwner());
				int maxRenderDistance = fard == null ? 10 : (fard.renderDistance > 10 ? 10 : fard.renderDistance);
				this.renderDistance = var2 > maxRenderDistance ? maxRenderDistance : var2;
			}
			if(this.lastRenderDistance != this.renderDistance) {
				if(this.mcServer.isSinglePlayer() && this.mcServer.getServerOwner().equals(this.username)) {
					for(int i = 0; i < this.mcServer.worldServers.length; ++i) {
						this.mcServer.worldServers[i].getEntityTracker().setMainRenderDistance(
								PlayerManager.getFurthestViewableBlock(this.renderDistance));
					}
				}
				((WorldServer)this.worldObj).getPlayerManager().cycleRenderDistance(this);
			}
		}

		this.chatVisibility = par1Packet204ClientInfo.getChatVisibility();
		this.chatColours = par1Packet204ClientInfo.getChatColours();

		if (this.mcServer.isSinglePlayer() && this.mcServer.getServerOwner().equals(this.username)) {
			this.mcServer.setDifficultyForAllWorlds(par1Packet204ClientInfo.getDifficulty());
		}

		this.setHideCape(1, !par1Packet204ClientInfo.getShowCape());
	}

	public StringTranslate getTranslator() {
		return this.translator;
	}

	public int getChatVisibility() {
		return this.chatVisibility;
	}

	/**
	 * on recieving this message the client (if permission is given) will download
	 * the requested textures
	 */
	public void requestTexturePackLoad(String par1Str, int par2) {
		String var3 = par1Str + "\u0000" + par2;
		this.playerNetServerHandler.sendPacket(new Packet250CustomPayload("MC|TPack", var3.getBytes()));
	}

	/**
	 * Return the position for this command sender.
	 */
	public ChunkCoordinates getCommandSenderPosition() {
		return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D),
				MathHelper.floor_double(this.posZ));
	}
}
