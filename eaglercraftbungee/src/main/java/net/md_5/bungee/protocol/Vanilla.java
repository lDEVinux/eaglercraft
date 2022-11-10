// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.packet.Packet0KeepAlive;
import net.md_5.bungee.protocol.packet.Packet1Login;
import net.md_5.bungee.protocol.packet.Packet2Handshake;
import net.md_5.bungee.protocol.packet.Packet3Chat;
import net.md_5.bungee.protocol.packet.Packet9Respawn;
import net.md_5.bungee.protocol.packet.PacketC9PlayerListItem;
import net.md_5.bungee.protocol.packet.PacketCCSettings;
import net.md_5.bungee.protocol.packet.PacketCDClientStatus;
import net.md_5.bungee.protocol.packet.PacketCEScoreboardObjective;
import net.md_5.bungee.protocol.packet.PacketCFScoreboardScore;
import net.md_5.bungee.protocol.packet.PacketD0DisplayScoreboard;
import net.md_5.bungee.protocol.packet.PacketD1Team;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.PacketFCEncryptionResponse;
import net.md_5.bungee.protocol.packet.PacketFDEncryptionRequest;
import net.md_5.bungee.protocol.packet.PacketFEPing;
import net.md_5.bungee.protocol.packet.PacketFFKick;
import net.md_5.bungee.protocol.skip.PacketReader;

public class Vanilla implements Protocol {
	public static final byte PROTOCOL_VERSION = 61;
	public static final String GAME_VERSION = "1.5.2";
	private static final Vanilla instance;
	private final OpCode[][] opCodes;
	protected Class<? extends DefinedPacket>[] classes;
	private Constructor<? extends DefinedPacket>[] constructors;
	protected PacketReader skipper;

	public Vanilla() {
		this.opCodes = new OpCode[256][];
		this.classes = (Class<? extends DefinedPacket>[]) new Class[256];
		this.constructors = (Constructor<? extends DefinedPacket>[]) new Constructor[256];
		this.opCodes[4] = new OpCode[] { OpCode.LONG, OpCode.LONG };
		this.opCodes[5] = new OpCode[] { OpCode.INT, OpCode.SHORT, OpCode.ITEM };
		this.opCodes[6] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.INT };
		this.opCodes[7] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.BOOLEAN };
		this.opCodes[8] = new OpCode[] { OpCode.SHORT, OpCode.SHORT, OpCode.FLOAT };
		this.opCodes[10] = new OpCode[] { OpCode.BOOLEAN };
		this.opCodes[11] = new OpCode[] { OpCode.DOUBLE, OpCode.DOUBLE, OpCode.DOUBLE, OpCode.DOUBLE, OpCode.BOOLEAN };
		this.opCodes[12] = new OpCode[] { OpCode.FLOAT, OpCode.FLOAT, OpCode.BOOLEAN };
		this.opCodes[13] = new OpCode[] { OpCode.DOUBLE, OpCode.DOUBLE, OpCode.DOUBLE, OpCode.DOUBLE, OpCode.FLOAT, OpCode.FLOAT, OpCode.BOOLEAN };
		this.opCodes[14] = new OpCode[] { OpCode.BYTE, OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.BYTE };
		this.opCodes[15] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.BYTE, OpCode.ITEM, OpCode.BYTE, OpCode.BYTE, OpCode.BYTE };
		this.opCodes[16] = new OpCode[] { OpCode.SHORT };
		this.opCodes[17] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.BYTE, OpCode.INT };
		this.opCodes[18] = new OpCode[] { OpCode.INT, OpCode.BYTE };
		this.opCodes[19] = new OpCode[] { OpCode.INT, OpCode.BYTE };
		this.opCodes[20] = new OpCode[] { OpCode.INT, OpCode.STRING, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.BYTE, OpCode.BYTE, OpCode.SHORT, OpCode.METADATA };
		this.opCodes[22] = new OpCode[] { OpCode.INT, OpCode.INT };
		this.opCodes[23] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.BYTE, OpCode.BYTE, OpCode.OPTIONAL_MOTION };
		this.opCodes[24] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.BYTE, OpCode.BYTE, OpCode.BYTE, OpCode.SHORT, OpCode.SHORT, OpCode.SHORT, OpCode.METADATA };
		this.opCodes[25] = new OpCode[] { OpCode.INT, OpCode.STRING, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.INT };
		this.opCodes[26] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.SHORT };
		this.opCodes[28] = new OpCode[] { OpCode.INT, OpCode.SHORT, OpCode.SHORT, OpCode.SHORT };
		this.opCodes[29] = new OpCode[] { OpCode.BYTE_INT };
		this.opCodes[30] = new OpCode[] { OpCode.INT };
		this.opCodes[31] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.BYTE, OpCode.BYTE };
		this.opCodes[32] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.BYTE };
		this.opCodes[33] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.BYTE, OpCode.BYTE, OpCode.BYTE, OpCode.BYTE };
		this.opCodes[34] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.BYTE, OpCode.BYTE };
		this.opCodes[35] = new OpCode[] { OpCode.INT, OpCode.BYTE };
		this.opCodes[38] = new OpCode[] { OpCode.INT, OpCode.BYTE };
		this.opCodes[39] = new OpCode[] { OpCode.INT, OpCode.INT };
		this.opCodes[40] = new OpCode[] { OpCode.INT, OpCode.METADATA };
		this.opCodes[41] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.BYTE, OpCode.SHORT };
		this.opCodes[42] = new OpCode[] { OpCode.INT, OpCode.BYTE };
		this.opCodes[43] = new OpCode[] { OpCode.FLOAT, OpCode.SHORT, OpCode.SHORT };
		this.opCodes[51] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.BOOLEAN, OpCode.SHORT, OpCode.SHORT, OpCode.INT_BYTE };
		this.opCodes[52] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.SHORT, OpCode.INT_BYTE };
		this.opCodes[53] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.SHORT, OpCode.BYTE };
		this.opCodes[54] = new OpCode[] { OpCode.INT, OpCode.SHORT, OpCode.INT, OpCode.BYTE, OpCode.BYTE, OpCode.SHORT };
		this.opCodes[55] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.BYTE };
		this.opCodes[56] = new OpCode[] { OpCode.BULK_CHUNK };
		this.opCodes[60] = new OpCode[] { OpCode.DOUBLE, OpCode.DOUBLE, OpCode.DOUBLE, OpCode.FLOAT, OpCode.INT_3, OpCode.FLOAT, OpCode.FLOAT, OpCode.FLOAT };
		this.opCodes[61] = new OpCode[] { OpCode.INT, OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.INT, OpCode.BOOLEAN };
		this.opCodes[62] = new OpCode[] { OpCode.STRING, OpCode.INT, OpCode.INT, OpCode.INT, OpCode.FLOAT, OpCode.BYTE };
		this.opCodes[63] = new OpCode[] { OpCode.STRING, OpCode.FLOAT, OpCode.FLOAT, OpCode.FLOAT, OpCode.FLOAT, OpCode.FLOAT, OpCode.FLOAT, OpCode.FLOAT, OpCode.INT };
		this.opCodes[70] = new OpCode[] { OpCode.BYTE, OpCode.BYTE };
		this.opCodes[71] = new OpCode[] { OpCode.INT, OpCode.BYTE, OpCode.INT, OpCode.INT, OpCode.INT };
		this.opCodes[100] = new OpCode[] { OpCode.BYTE, OpCode.BYTE, OpCode.STRING, OpCode.BYTE, OpCode.BOOLEAN };
		this.opCodes[101] = new OpCode[] { OpCode.BYTE };
		this.opCodes[102] = new OpCode[] { OpCode.BYTE, OpCode.SHORT, OpCode.BYTE, OpCode.SHORT, OpCode.BOOLEAN, OpCode.ITEM };
		this.opCodes[103] = new OpCode[] { OpCode.BYTE, OpCode.SHORT, OpCode.ITEM };
		this.opCodes[104] = new OpCode[] { OpCode.BYTE, OpCode.SHORT_ITEM };
		this.opCodes[105] = new OpCode[] { OpCode.BYTE, OpCode.SHORT, OpCode.SHORT };
		this.opCodes[106] = new OpCode[] { OpCode.BYTE, OpCode.SHORT, OpCode.BOOLEAN };
		this.opCodes[107] = new OpCode[] { OpCode.SHORT, OpCode.ITEM };
		this.opCodes[108] = new OpCode[] { OpCode.BYTE, OpCode.BYTE };
		this.opCodes[130] = new OpCode[] { OpCode.INT, OpCode.SHORT, OpCode.INT, OpCode.STRING, OpCode.STRING, OpCode.STRING, OpCode.STRING };
		this.opCodes[131] = new OpCode[] { OpCode.SHORT, OpCode.SHORT, OpCode.USHORT_BYTE };
		this.opCodes[132] = new OpCode[] { OpCode.INT, OpCode.SHORT, OpCode.INT, OpCode.BYTE, OpCode.SHORT_BYTE };
		this.opCodes[195] = new OpCode[] { OpCode.SHORT, OpCode.SHORT, OpCode.INT_BYTE };
		this.opCodes[200] = new OpCode[] { OpCode.INT, OpCode.BYTE };
		this.opCodes[202] = new OpCode[] { OpCode.BYTE, OpCode.BYTE, OpCode.BYTE };
		this.opCodes[203] = new OpCode[] { OpCode.STRING };
		this.classes[0] = Packet0KeepAlive.class;
		this.classes[1] = Packet1Login.class;
		this.classes[2] = Packet2Handshake.class;
		this.classes[3] = Packet3Chat.class;
		this.classes[9] = Packet9Respawn.class;
		this.classes[201] = PacketC9PlayerListItem.class;
		this.classes[204] = PacketCCSettings.class;
		this.classes[205] = PacketCDClientStatus.class;
		this.classes[206] = PacketCEScoreboardObjective.class;
		this.classes[207] = PacketCFScoreboardScore.class;
		this.classes[208] = PacketD0DisplayScoreboard.class;
		this.classes[209] = PacketD1Team.class;
		this.classes[250] = PacketFAPluginMessage.class;
		this.classes[252] = PacketFCEncryptionResponse.class;
		this.classes[253] = PacketFDEncryptionRequest.class;
		this.classes[254] = PacketFEPing.class;
		this.classes[255] = PacketFFKick.class;
		this.skipper = new PacketReader(this);
	}

	@Override
	public DefinedPacket read(final short packetId, final ByteBuf buf) {
		final int start = buf.readerIndex();
		final DefinedPacket packet = read(packetId, buf, this);
		if (buf.readerIndex() == start) {
			throw new RuntimeException("Unknown packet id " + packetId);
		}
		return packet;
	}

	public static DefinedPacket read(final short id, final ByteBuf buf, final Protocol protocol) {
		final DefinedPacket packet = packet(id, protocol);
		if (packet != null) {
			packet.read(buf);
			return packet;
		}
		protocol.getSkipper().tryRead(id, buf);
		return null;
	}

	public static DefinedPacket packet(final short id, final Protocol protocol) {
		DefinedPacket ret = null;
		final Class<? extends DefinedPacket> clazz = protocol.getClasses()[id];
		if (clazz != null) {
			try {
				Constructor<? extends DefinedPacket> constructor = protocol.getConstructors()[id];
				if (constructor == null) {
					constructor = clazz.getDeclaredConstructor((Class<?>[]) new Class[0]);
					constructor.setAccessible(true);
					protocol.getConstructors()[id] = constructor;
				}
				if (constructor != null) {
					ret = (DefinedPacket) constructor.newInstance(new Object[0]);
				}
			} catch (NoSuchMethodException ex) {
			} catch (SecurityException ex2) {
			} catch (InstantiationException ex3) {
			} catch (IllegalAccessException ex4) {
			} catch (IllegalArgumentException ex5) {
			} catch (InvocationTargetException ex6) {
			}
		}
		return ret;
	}

	public static Vanilla getInstance() {
		return Vanilla.instance;
	}

	@Override
	public OpCode[][] getOpCodes() {
		return this.opCodes;
	}

	@Override
	public Class<? extends DefinedPacket>[] getClasses() {
		return this.classes;
	}

	@Override
	public Constructor<? extends DefinedPacket>[] getConstructors() {
		return this.constructors;
	}

	@Override
	public PacketReader getSkipper() {
		return this.skipper;
	}

	static {
		instance = new Vanilla();
	}
}
