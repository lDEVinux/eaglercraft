package net.minecraft.src;

public class MovingObjectPosition {
	/** What type of ray trace hit was this? 0 = block, 1 = entity */
	public EnumMovingObjectType typeOfHit;

	/** x coordinate of the block ray traced against */
	public int blockX;

	/** y coordinate of the block ray traced against */
	public int blockY;

	/** z coordinate of the block ray traced against */
	public int blockZ;

	/**
	 * Which side was hit. If its -1 then it went the full length of the ray trace.
	 * Bottom = 0, Top = 1, East = 2, West = 3, North = 4, South = 5.
	 */
	public int sideHit;

	/** The vector position of the hit */
	public Vec3 hitVec;

	/** The hit entity */
	public Entity entityHit;

	public MovingObjectPosition(int par1, int par2, int par3, int par4, Vec3 par5Vec3) {
		this.typeOfHit = EnumMovingObjectType.TILE;
		this.blockX = par1;
		this.blockY = par2;
		this.blockZ = par3;
		this.sideHit = par4;
		this.hitVec = par5Vec3.myVec3LocalPool.getVecFromPool(par5Vec3.xCoord, par5Vec3.yCoord, par5Vec3.zCoord);
	}

	public MovingObjectPosition(Entity par1Entity) {
		this.typeOfHit = EnumMovingObjectType.ENTITY;
		this.entityHit = par1Entity;
		this.hitVec = par1Entity.worldObj.getWorldVec3Pool().getVecFromPool(par1Entity.posX, par1Entity.posY,
				par1Entity.posZ);
	}
}
