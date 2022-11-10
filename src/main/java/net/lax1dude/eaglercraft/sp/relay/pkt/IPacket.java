package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class IPacket {

	private static final Map<Integer,Class<? extends IPacket>> definedPacketClasses = new HashMap();
	private static final Map<Class<? extends IPacket>,Integer> definedPacketIds = new HashMap();
	
	private static void register(int id, Class<? extends IPacket> clazz) {
		definedPacketClasses.put(id, clazz);
		definedPacketIds.put(clazz, id);
	}
	
	static {
		register(0x00, IPacket00Handshake.class);
		register(0x01, IPacket01ICEServers.class);
		register(0x02, IPacket02NewClient.class);
		register(0x03, IPacket03ICECandidate.class);
		register(0x04, IPacket04Description.class);
		register(0x05, IPacket05ClientSuccess.class);
		register(0x06, IPacket06ClientFailure.class);
		register(0x07, IPacket07LocalWorlds.class);
		register(0x69, IPacket69Pong.class);
		register(0xFE, IPacketFEDisconnectClient.class);
		register(0xFF, IPacketFFErrorCode.class);
	}
	
	public static IPacket readPacket(DataInputStream input) throws IOException {
		int i = input.read();
		try {
			Class<? extends IPacket> clazz = definedPacketClasses.get(i);
			if(clazz == null) {
				throw new IOException("Unknown packet type: " + i);
			}
			IPacket pkt = clazz.newInstance();
			pkt.read(input);
			return pkt;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IOException("Unknown packet type: " + i);
		}
	}
	
	public static byte[] writePacket(IPacket packet) throws IOException {
		Integer i = definedPacketIds.get(packet.getClass());
		if(i != null) {
			int len = packet.packetLength();
			ByteArrayOutputStream bao = len == -1 ? new ByteArrayOutputStream() :
				new ByteArrayOutputStream(len + 1);
			bao.write(i);
			packet.write(new DataOutputStream(bao));
			byte[] ret = bao.toByteArray();
			if(len != -1 && ret.length != len + 1) {
				System.err.println("writePacket buffer for packet " + packet.getClass().getSimpleName() + " " +
						(len + 1 < ret.length ? "overflowed" : "underflowed") + " by " + (len + 1 < ret.length ?
								ret.length - len - 1 : len + 1 - ret.length) + " bytes");
			}
			return ret;
		}else {
			throw new IOException("Unknown packet type: " + packet.getClass().getSimpleName());
		}
	}

	public void read(DataInputStream input) throws IOException {
	}

	public void write(DataOutputStream output) throws IOException {
	}
	
	public int packetLength() {
		return -1;
	}
	
	public static String readASCII(InputStream is, int len) throws IOException {
		char[] ret = new char[len];
		for(int i = 0; i < len; ++i) {
			int j = is.read();
			if(j < 0) {
				return null;
			}
			ret[i] = (char)j;
		}
		return new String(ret);
	}
	
	public static void writeASCII(OutputStream is, String txt) throws IOException {
		for(int i = 0, l = txt.length(); i < l; ++i) {
			is.write((int)txt.charAt(i));
		}
	}
	
	public static String readASCII8(InputStream is) throws IOException {
		int i = is.read();
		if(i < 0) {
			return null;
		}else {
			return readASCII(is, i);
		}
	}
	
	public static void writeASCII8(OutputStream is, String txt) throws IOException {
		if(txt == null) {
			is.write(0);
		}else {
			int l = txt.length();
			is.write(l);
			for(int i = 0; i < l; ++i) {
				is.write((int)txt.charAt(i));
			}
		}
	}

	public static String readASCII16(InputStream is) throws IOException {
		int hi = is.read();
		int lo = is.read();
		if(hi < 0 || lo < 0) {
			return null;
		}else {
			return readASCII(is, (hi << 8) | lo);
		}
	}
	
	public static void writeASCII16(OutputStream is, String txt) throws IOException {
		if(txt == null) {
			is.write(0);
			is.write(0);
		}else {
			int l = txt.length();
			is.write((l >> 8) & 0xFF);
			is.write(l & 0xFF);
			for(int i = 0; i < l; ++i) {
				is.write((int)txt.charAt(i));
			}
		}
	}
	
}
