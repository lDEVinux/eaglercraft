package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataOutputStream;
import java.io.IOException;

public class IPacket69Pong extends IPacket {

	public int protcolVersion;
	public String comment;
	public String brand;
	
	public IPacket69Pong(int protcolVersion, String comment, String brand) {
		if(comment.length() > 255) {
			comment = comment.substring(0, 256);
		}
		this.protcolVersion = protcolVersion;
		this.comment = comment;
		this.brand = brand;
	}

	public IPacket69Pong() {
	}
	
	public void write(DataOutputStream output) throws IOException {
		output.write(protcolVersion);
		writeASCII8(output, comment);
		writeASCII8(output, brand);
	}
	
	public int packetLength() {
		return 3 + comment.length() + brand.length();
	}
	
}
