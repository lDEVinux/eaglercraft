package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.RelayQuery.VersionMismatch;
import net.minecraft.client.Minecraft;

public class RelayServer {
	
	public final String address;
	public final String comment;
	private boolean primary;
	
	private RelayQuery query = null;
	private int queriedVersion = -1;
	private String queriedComment;
	private String queriedVendor;
	private VersionMismatch queriedCompatible;
	private long ping = 0l;
	private long workingPing = 0l;
	public long lastPing = 0l;

	public RelayServer(String address, String comment, boolean primary) {
		this.address = address;
		this.comment = comment;
		this.primary = primary;
	}
	
	public RelayServer(RelayEntry etr) {
		this(etr.address, etr.comment, etr.primary);
	}
	
	public boolean isPrimary() {
		return primary;
	}
	
	public void setPrimary(boolean primaryee) {
		primary = primaryee;
	}

	public long getPing() {
		return ping;
	}

	public long getWorkingPing() {
		return workingPing;
	}
	
	public int getPingVersion() {
		return queriedVersion;
	}
	
	public String getPingComment() {
		return queriedComment == null ? "" : queriedComment;
	}
	
	public String getPingVendor() {
		return queriedVendor == null ? "" : queriedVendor;
	}
	
	public VersionMismatch getPingCompatible() {
		return queriedCompatible;
	}
	
	public void pingBlocking() {
		ping();
		while(getPing() < 0l) {
			try {
				Thread.sleep(250l);
			}catch(InterruptedException ex) {
			}
			update();
		}
	}
	
	public void ping() {
		close();
		query = EaglerAdapter.openRelayQuery(address);
		queriedVersion = -1;
		queriedComment = null;
		queriedVendor = null;
		queriedCompatible = VersionMismatch.UNKNOWN;
		ping = -1l;
	}
	
	public void update() {
		if(query != null && !query.isQueryOpen()) {
			if(query.isQueryFailed()) {
				queriedVersion = -1;
				queriedComment = null;
				queriedVendor = null;
				queriedCompatible = VersionMismatch.UNKNOWN;
				ping = 0l;
			}else {
				queriedVersion = query.getVersion();
				queriedComment = query.getComment();
				queriedVendor = query.getBrand();
				ping = query.getPing();
				queriedCompatible = query.getCompatible();
				workingPing = ping;
			}
			lastPing = System.currentTimeMillis();
			query = null;
		}
	}
	
	public void close() {
		if(query != null && query.isQueryOpen()) {
			query.close();
			query = null;
			queriedVersion = -1;
			queriedComment = null;
			queriedVendor = null;
			queriedCompatible = VersionMismatch.UNKNOWN;
			ping = 0l;
		}
	}
	
	public RelayServerSocket openSocket() {
		return EaglerAdapter.openRelayConnection(address, Minecraft.getMinecraft().gameSettings.relayTimeout * 1000);
	}
	
}
