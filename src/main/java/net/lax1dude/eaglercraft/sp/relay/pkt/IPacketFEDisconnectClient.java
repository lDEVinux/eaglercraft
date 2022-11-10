package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class IPacketFEDisconnectClient extends IPacket {

	public static final int TYPE_FINISHED_SUCCESS = 0x00;
	public static final int TYPE_FINISHED_FAILED = 0x01;
	public static final int TYPE_TIMEOUT = 0x02;
	public static final int TYPE_INVALID_OPERATION = 0x03;
	public static final int TYPE_INTERNAL_ERROR = 0x04;
	public static final int TYPE_SERVER_DISCONNECT = 0x05;
	public static final int TYPE_UNKNOWN = 0xFF;
	
	public String clientId;
	public int code;
	public String reason;
	
	public IPacketFEDisconnectClient() {
	}
	
	public IPacketFEDisconnectClient(String clientId, int code, String reason) {
		this.clientId = clientId;
		this.code = code;
		this.reason = reason;
	}
	
	public void read(DataInputStream input) throws IOException {
		clientId = readASCII8(input);
		code = input.read();
		reason = readASCII16(input);
	}

	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, clientId);
		output.write(code);
		writeASCII16(output, reason);
	}
	
	public int packetLength() {
		return -1;
	}

	public static final ByteBuffer ratelimitPacketTooMany = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x00 });
	public static final ByteBuffer ratelimitPacketBlock = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x01 });
	public static final ByteBuffer ratelimitPacketBlockLock = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x02 });
	public static final ByteBuffer ratelimitPacketLocked = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x03 });
	
}
