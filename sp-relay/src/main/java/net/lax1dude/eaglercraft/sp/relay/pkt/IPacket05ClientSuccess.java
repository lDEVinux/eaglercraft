package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IPacket05ClientSuccess extends IPacket {
	
	public String clientId;
	
	public IPacket05ClientSuccess() {
	}
	
	public IPacket05ClientSuccess(String clientId) {
		this.clientId = clientId;
	}

	public void read(DataInputStream input) throws IOException {
		clientId = readASCII8(input);
	}

	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, clientId);
	}
	
	public int packetLength() {
		return 1 + clientId.length();
	}
	
}
