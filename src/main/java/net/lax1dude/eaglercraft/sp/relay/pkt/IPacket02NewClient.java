package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.IOException;

public class IPacket02NewClient extends IPacket {
	
	public String clientId;
	
	public IPacket02NewClient(String clientId) {
		this.clientId = clientId;
	}
	
	public IPacket02NewClient() {
	}
	
	public void read(DataInputStream input) throws IOException {
		clientId = readASCII8(input);
	}
	
}
