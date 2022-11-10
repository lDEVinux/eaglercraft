package net.minecraft.src;

import java.util.Comparator;

public class RenderSorter implements Comparator {
	/** The entity (usually the player) that the camera is inside. */
	private EntityLiving baseEntity;

	public RenderSorter(EntityLiving par1EntityLiving) {
		this.baseEntity = par1EntityLiving;
	}

	public int doCompare(WorldRenderer par1WorldRenderer, WorldRenderer par2WorldRenderer) {
		if (par1WorldRenderer.isInFrustum && !par2WorldRenderer.isInFrustum) {
			return 1;
		} else if (par2WorldRenderer.isInFrustum && !par1WorldRenderer.isInFrustum) {
			return -1;
		} else {
			double var3 = (double) par1WorldRenderer.distanceToEntitySquared(this.baseEntity);
			double var5 = (double) par2WorldRenderer.distanceToEntitySquared(this.baseEntity);
			return var3 < var5 ? 1 : (var3 > var5 ? -1 : (par1WorldRenderer.chunkIndex < par2WorldRenderer.chunkIndex ? 1 : -1));
		}
	}

	public int compare(Object par1Obj, Object par2Obj) {
		return this.doCompare((WorldRenderer) par1Obj, (WorldRenderer) par2Obj);
	}
}
