package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IPacket03ICECandidate extends IPacket {

	public String peerId;
	public String candidate;
	
	public IPacket03ICECandidate(String peerId, String desc) {
		this.peerId = peerId;
		this.candidate = desc;
	}
	
	public IPacket03ICECandidate() {
	}
	
	public void read(DataInputStream input) throws IOException {
		peerId = readASCII8(input);
		candidate = readASCII16(input);
	}

	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, peerId);
		writeASCII16(output, candidate);
	}
	
	public int packetLength() {
		return 1 + peerId.length() + 2 + candidate.length();
	}

}