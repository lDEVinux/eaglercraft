// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.packet.forge.Forge1Login;
import net.md_5.bungee.protocol.skip.PacketReader;

public class Forge extends Vanilla {
	private static final Forge instance;

	public Forge() {
		this.classes[1] = Forge1Login.class;
		this.skipper = new PacketReader(this);
	}

	@Override
	public DefinedPacket read(final short packetId, final ByteBuf buf) {
		final int start = buf.readerIndex();
		DefinedPacket packet = Vanilla.read(packetId, buf, this);
		if (buf.readerIndex() == start) {
			packet = super.read(packetId, buf);
		}
		return packet;
	}

	public static Forge getInstance() {
		return Forge.instance;
	}

	static {
		instance = new Forge();
	}
}
