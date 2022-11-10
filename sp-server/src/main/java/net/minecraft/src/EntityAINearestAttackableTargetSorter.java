package net.minecraft.src;

import java.util.Comparator;

public class EntityAINearestAttackableTargetSorter implements Comparator {
	private Entity theEntity;

	final EntityAINearestAttackableTarget parent;

	public EntityAINearestAttackableTargetSorter(EntityAINearestAttackableTarget par1EntityAINearestAttackableTarget,
			Entity par2Entity) {
		this.parent = par1EntityAINearestAttackableTarget;
		this.theEntity = par2Entity;
	}

	public int compareDistanceSq(Entity par1Entity, Entity par2Entity) {
		double var3 = this.theEntity.getDistanceSqToEntity(par1Entity);
		double var5 = this.theEntity.getDistanceSqToEntity(par2Entity);
		return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
	}

	public int compare(Object par1Obj, Object par2Obj) {
		return this.compareDistanceSq((Entity) par1Obj, (Entity) par2Obj);
	}
}
