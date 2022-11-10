package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IPacketFFErrorCode extends IPacket {

	public static final int TYPE_INTERNAL_ERROR = 0x00;
	public static final int TYPE_PROTOCOL_VERSION = 0x01;
	public static final int TYPE_INVALID_PACKET = 0x02;
	public static final int TYPE_ILLEGAL_OPERATION = 0x03;
	public static final int TYPE_CODE_LENGTH = 0x04;
	public static final int TYPE_INCORRECT_CODE = 0x05;
	public static final int TYPE_SERVER_DISCONNECTED = 0x06;
	public static final int TYPE_UNKNOWN_CLIENT = 0x07;
	
	public int code;
	public String desc;
	
	public IPacketFFErrorCode() {
	}
	
	public IPacketFFErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	@Override
	public void read(DataInputStream input) throws IOException {
		code = input.read();
		desc = readASCII16(input);
	}

	@Override
	public void write(DataOutputStream input) throws IOException {
		input.write(code);
		writeASCII16(input, desc);
	}

	@Override
	public int packetLength() {
		return 1 + 2 + desc.length();
	}

}
