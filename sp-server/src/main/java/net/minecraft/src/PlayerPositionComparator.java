package net.minecraft.src;

import java.util.Comparator;

public class PlayerPositionComparator implements Comparator {
	private final ChunkCoordinates theChunkCoordinates;

	public PlayerPositionComparator(ChunkCoordinates par1ChunkCoordinates) {
		this.theChunkCoordinates = par1ChunkCoordinates;
	}

	/**
	 * Compare the position of two players.
	 */
	public int comparePlayers(EntityPlayerMP par1EntityPlayerMP, EntityPlayerMP par2EntityPlayerMP) {
		double var3 = par1EntityPlayerMP.getDistanceSq((double) this.theChunkCoordinates.posX,
				(double) this.theChunkCoordinates.posY, (double) this.theChunkCoordinates.posZ);
		double var5 = par2EntityPlayerMP.getDistanceSq((double) this.theChunkCoordinates.posX,
				(double) this.theChunkCoordinates.posY, (double) this.theChunkCoordinates.posZ);
		return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
	}

	public int compare(Object par1Obj, Object par2Obj) {
		return this.comparePlayers((EntityPlayerMP) par1Obj, (EntityPlayerMP) par2Obj);
	}
}
