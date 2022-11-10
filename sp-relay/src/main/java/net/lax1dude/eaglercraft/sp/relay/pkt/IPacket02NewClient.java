package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataOutputStream;
import java.io.IOException;

public class IPacket02NewClient extends IPacket {
	
	public String clientId;
	
	public IPacket02NewClient(String clientId) {
		this.clientId = clientId;
	}
	
	public IPacket02NewClient() {
	}
	
	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, clientId);
	}
	
	public int packetLength() {
		return 1 + clientId.length();
	}
	
}
