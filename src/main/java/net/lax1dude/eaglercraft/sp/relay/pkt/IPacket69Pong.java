package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
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
	
	public void read(DataInputStream output) throws IOException {
		protcolVersion = output.read();
		comment = readASCII8(output);
		brand = readASCII8(output);
	}
	
}
