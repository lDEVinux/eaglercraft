package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.RateLimit;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket;

public interface RelayServerSocket {

	boolean isOpen();
	boolean isClosed();
	void close();
	
	boolean isFailed();
	Throwable getException();
	
	void writePacket(IPacket pkt);
	
	IPacket readPacket();
	IPacket nextPacket();
	
	RateLimit getRatelimitHistory();
	
	String getURI();
	
}
