package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IPacket04Description extends IPacket {

	public String peerId;
	public String description;
	
	public IPacket04Description(String peerId, String desc) {
		this.peerId = peerId;
		this.description = desc;
	}
	
	public IPacket04Description() {
	}
	
	public void read(DataInputStream input) throws IOException {
		peerId = readASCII8(input);
		description = readASCII16(input);
	}

	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, peerId);
		writeASCII16(output, description);
	}
	
	public int packetLength() {
		return 1 + peerId.length() + 2 + description.length();
	}

}
