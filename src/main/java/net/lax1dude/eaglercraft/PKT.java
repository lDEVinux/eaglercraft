package net.lax1dude.eaglercraft;

public class PKT {
	
	public final String channel;
	public final byte[] data;
	
	public PKT(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}

}
