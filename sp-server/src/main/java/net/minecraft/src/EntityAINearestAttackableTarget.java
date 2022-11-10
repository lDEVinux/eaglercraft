package net.minecraft.src;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EntityAINearestAttackableTarget extends EntityAITarget {
	EntityLiving targetEntity;
	Class targetClass;
	int targetChance;
	private final IEntitySelector field_82643_g;

	/** Instance of EntityAINearestAttackableTargetSorter. */
	private EntityAINearestAttackableTargetSorter theNearestAttackableTargetSorter;

	public EntityAINearestAttackableTarget(EntityLiving par1EntityLiving, Class par2Class, float par3, int par4,
			boolean par5) {
		this(par1EntityLiving, par2Class, par3, par4, par5, false);
	}

	public EntityAINearestAttackableTarget(EntityLiving par1EntityLiving, Class par2Class, float par3, int par4,
			boolean par5, boolean par6) {
		this(par1EntityLiving, par2Class, par3, par4, par5, par6, (IEntitySelector) null);
	}

	public EntityAINearestAttackableTarget(EntityLiving par1, Class par2, float par3, int par4, boolean par5,
			boolean par6, IEntitySelector par7IEntitySelector) {
		super(par1, par3, par5, par6);
		this.targetClass = par2;
		this.targetDistance = par3;
		this.targetChance = par4;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTargetSorter(this, par1);
		this.field_82643_g = par7IEntitySelector;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		} else {
			if (this.targetClass == EntityPlayer.class) {
				EntityPlayer var1 = this.taskOwner.worldObj.getClosestVulnerablePlayerToEntity(this.taskOwner,
						(double) this.targetDistance);

				if (this.isSuitableTarget(var1, false)) {
					this.targetEntity = var1;
					return true;
				}
			} else {
				List var5 = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass,
						this.taskOwner.boundingBox.expand((double) this.targetDistance, 4.0D,
								(double) this.targetDistance),
						this.field_82643_g);
				Collections.sort(var5, this.theNearestAttackableTargetSorter);
				Iterator var2 = var5.iterator();

				while (var2.hasNext()) {
					Entity var3 = (Entity) var2.next();
					EntityLiving var4 = (EntityLiving) var3;

					if (this.isSuitableTarget(var4, false)) {
						this.targetEntity = var4;
						return true;
					}
				}
			}

			return false;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}
}
