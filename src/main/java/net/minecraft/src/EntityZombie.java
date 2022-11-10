package net.minecraft.src;

import java.util.Calendar;



public class EntityZombie extends EntityMob {
	/**
	 * Ticker used to determine the time remaining for this zombie to convert into a
	 * villager when cured.
	 */
	private int conversionTime = 0;

	public EntityZombie() {
		super();
		this.moveSpeed = 0.23F;
	}

	protected int func_96121_ay() {
		return 40;
	}

	/**
	 * This method returns a value to be applied directly to entity speed, this
	 * factor is less than 1 when a slowdown potion effect is applied, more than 1
	 * when a haste potion effect is applied and 2 for fleeing entities.
	 */
	public float getSpeedModifier() {
		return super.getSpeedModifier() * (this.isChild() ? 1.5F : 1.0F);
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(12, Byte.valueOf((byte) 0));
		this.getDataWatcher().addObject(13, Byte.valueOf((byte) 0));
		this.getDataWatcher().addObject(14, Byte.valueOf((byte) 0));
	}

	public int getMaxHealth() {
		return 20;
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue() {
		int var1 = super.getTotalArmorValue() + 2;

		if (var1 > 20) {
			var1 = 20;
		}

		return var1;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild() {
		return this.getDataWatcher().getWatchableObjectByte(12) == 1;
	}

	/**
	 * Set whether this zombie is a child.
	 */
	public void setChild(boolean par1) {
		this.getDataWatcher().updateObject(12, Byte.valueOf((byte) 1));
	}

	/**
	 * Return whether this zombie is a villager.
	 */
	public boolean isVillager() {
		return this.getDataWatcher().getWatchableObjectByte(13) == 1;
	}

	/**
	 * Set whether this zombie is a villager.
	 */
	public void setVillager(boolean par1) {
		this.getDataWatcher().updateObject(13, Byte.valueOf((byte) (par1 ? 1 : 0)));
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		boolean var2 = super.attackEntityAsMob(par1Entity);

		if (var2 && this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < (float) this.worldObj.difficultySetting * 0.3F) {
			par1Entity.setFire(2 * this.worldObj.difficultySetting);
		}

		return var2;
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public int getAttackStrength(Entity par1Entity) {
		ItemStack var2 = this.getHeldItem();
		float var3 = (float) (this.getMaxHealth() - this.getHealth()) / (float) this.getMaxHealth();
		int var4 = 3 + MathHelper.floor_float(var3 * 4.0F);

		if (var2 != null) {
			var4 += var2.getDamageVsEntity(this);
		}

		return var4;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.zombie.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.zombie.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.zombie.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.rottenFlesh.itemID;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	protected void dropRareDrop(int par1) {
		switch (this.rand.nextInt(3)) {
		case 0:
			this.dropItem(Item.ingotIron.itemID, 1);
			break;

		case 1:
			this.dropItem(Item.carrot.itemID, 1);
			break;

		case 2:
			this.dropItem(Item.potato.itemID, 1);
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	protected void addRandomArmor() {
		super.addRandomArmor();

		if (this.rand.nextFloat() < (this.worldObj.difficultySetting == 3 ? 0.05F : 0.01F)) {
			int var1 = this.rand.nextInt(3);

			if (var1 == 0) {
				this.setCurrentItemOrArmor(0, new ItemStack(Item.swordIron));
			} else {
				this.setCurrentItemOrArmor(0, new ItemStack(Item.shovelIron));
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);

		if (this.isChild()) {
			par1NBTTagCompound.setBoolean("IsBaby", true);
		}

		if (this.isVillager()) {
			par1NBTTagCompound.setBoolean("IsVillager", true);
		}

		par1NBTTagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.getBoolean("IsBaby")) {
			this.setChild(true);
		}

		if (par1NBTTagCompound.getBoolean("IsVillager")) {
			this.setVillager(true);
		}

		if (par1NBTTagCompound.hasKey("ConversionTime") && par1NBTTagCompound.getInteger("ConversionTime") > -1) {
			this.startConversion(par1NBTTagCompound.getInteger("ConversionTime"));
		}
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	public void onKillEntity(EntityLiving par1EntityLiving) {
		super.onKillEntity(par1EntityLiving);

		if (this.worldObj.difficultySetting >= 2 && par1EntityLiving instanceof EntityVillager) {
			if (this.worldObj.difficultySetting == 2 && this.rand.nextBoolean()) {
				return;
			}

			EntityZombie var2 = new EntityZombie();
			var2.setWorld(worldObj);
			var2.func_82149_j(par1EntityLiving);
			this.worldObj.removeEntity(par1EntityLiving);
			var2.initCreature();
			var2.setVillager(true);

			if (par1EntityLiving.isChild()) {
				var2.setChild(true);
			}

			this.worldObj.spawnEntityInWorld(var2);
			this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
		}
	}

	/**
	 * Initialize this creature.
	 */
	public void initCreature() {
		this.setCanPickUpLoot(this.rand.nextFloat() < pickUpLootProability[this.worldObj.difficultySetting]);

		if (this.worldObj.rand.nextFloat() < 0.05F) {
			this.setVillager(true);
		}

		this.addRandomArmor();
		this.func_82162_bC();

		if (this.getCurrentItemOrArmor(4) == null) {
			Calendar var1 = this.worldObj.getCurrentDate();

			if (var1.get(2) + 1 == 10 && var1.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
				this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Block.pumpkinLantern : Block.pumpkin));
				this.equipmentDropChances[4] = 0.0F;
			}
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
	 * into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.getCurrentEquippedItem();

		if (var2 != null && var2.getItem() == Item.appleGold && var2.getItemDamage() == 0 && this.isVillager() && this.isPotionActive(Potion.weakness)) {
			if (!par1EntityPlayer.capabilities.isCreativeMode) {
				--var2.stackSize;
			}

			if (var2.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack) null);
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Starts converting this zombie into a villager. The zombie converts into a
	 * villager after the specified time in ticks.
	 */
	protected void startConversion(int par1) {
		this.conversionTime = par1;
		this.getDataWatcher().updateObject(14, Byte.valueOf((byte) 1));
		this.removePotionEffect(Potion.weakness.id);
		this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, par1, Math.min(this.worldObj.difficultySetting - 1, 0)));
		this.worldObj.setEntityState(this, (byte) 16);
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 16) {
			this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	/**
	 * Returns whether this zombie is in the process of converting to a villager
	 */
	public boolean isConverting() {
		return this.getDataWatcher().getWatchableObjectByte(14) == 1;
	}

	/**
	 * Convert this zombie into a villager.
	 */
	protected void convertToVillager() {
		EntityVillager var1 = new EntityVillager();
		var1.setWorld(worldObj);
		var1.func_82149_j(this);
		var1.initCreature();
		var1.func_82187_q();

		if (this.isChild()) {
			var1.setGrowingAge(-24000);
		}

		this.worldObj.removeEntity(this);
		this.worldObj.spawnEntityInWorld(var1);
		var1.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
		this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1017, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
	}

	/**
	 * Return the amount of time decremented from conversionTime every tick.
	 */
	protected int getConversionTimeBoost() {
		int var1 = 1;

		if (this.rand.nextFloat() < 0.01F) {
			int var2 = 0;

			for (int var3 = (int) this.posX - 4; var3 < (int) this.posX + 4 && var2 < 14; ++var3) {
				for (int var4 = (int) this.posY - 4; var4 < (int) this.posY + 4 && var2 < 14; ++var4) {
					for (int var5 = (int) this.posZ - 4; var5 < (int) this.posZ + 4 && var2 < 14; ++var5) {
						int var6 = this.worldObj.getBlockId(var3, var4, var5);

						if (var6 == Block.fenceIron.blockID || var6 == Block.bed.blockID) {
							if (this.rand.nextFloat() < 0.3F) {
								++var1;
							}

							++var2;
						}
					}
				}
			}
		}

		return var1;
	}
}
