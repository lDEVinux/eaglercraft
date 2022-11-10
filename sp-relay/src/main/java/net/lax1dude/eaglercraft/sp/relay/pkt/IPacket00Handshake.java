package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IPacket00Handshake extends IPacket {

	public int connectionType = 0;
	public int connectionVersion = 1;
	public String connectionCode = null;
	
	public IPacket00Handshake() {
	}
	
	public IPacket00Handshake(int connectionType, int connectionVersion,
			String connectionCode) {
		this.connectionType = connectionType;
		this.connectionVersion = connectionVersion;
		this.connectionCode = connectionCode;
	}
	
	@Override
	public void read(DataInputStream input) throws IOException {
		connectionType = input.read();
		connectionVersion = input.read();
		connectionCode = IPacket.readASCII8(input);
	}
	
	@Override
	public void write(DataOutputStream output) throws IOException {
		output.write(connectionType);
		output.write(connectionVersion);
		IPacket.writeASCII8(output, connectionCode);
	}
	
	@Override
	public int packetLength() {
		return 1 + 1 + (connectionCode != null ? 1 + connectionCode.length() : 0);
	}

}
