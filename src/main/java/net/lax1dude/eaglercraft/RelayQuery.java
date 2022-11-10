package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.RateLimit;

public interface RelayQuery {
	
	public static enum VersionMismatch {
		COMPATIBLE, CLIENT_OUTDATED, RELAY_OUTDATED, UNKNOWN;
		public boolean isCompatible() {
			return this == COMPATIBLE;
		}
	}

	boolean isQueryOpen();
	boolean isQueryFailed();
	RateLimit isQueryRateLimit();
	void close();
	
	int getVersion();
	String getComment();
	String getBrand();
	long getPing();

	VersionMismatch getCompatible();

}
