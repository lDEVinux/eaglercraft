package net.minecraft.src;

public class PortalPosition extends ChunkCoordinates {
	/** The worldtime at which this PortalPosition was last verified */
	public long lastUpdateTime;

	/** The teleporter to which this PortalPosition applies */
	final Teleporter teleporterInstance;

	public PortalPosition(Teleporter par1Teleporter, int par2, int par3, int par4, long par5) {
		super(par2, par3, par4);
		this.teleporterInstance = par1Teleporter;
		this.lastUpdateTime = par5;
	}
}
