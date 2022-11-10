// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.connection;

import java.beans.ConstructorProperties;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.packet.PacketFFKick;

public class PingHandler extends PacketHandler {
	private final ServerInfo target;
	private final Callback<ServerPing> callback;
	private static final byte[] pingBuf;

	@Override
	public void connected(final ChannelWrapper channel) throws Exception {
		channel.write(PingHandler.pingBuf);
	}

	@Override
	public void exception(final Throwable t) throws Exception {
		this.callback.done(null, t);
	}

	@Override
	public void handle(final PacketFFKick kick) throws Exception {
		final String[] split = kick.getMessage().split("\u0000");
		final ServerPing ping = new ServerPing(Byte.parseByte(split[1]), split[2], split[3], Integer.parseInt(split[4]), Integer.parseInt(split[5]));
		this.callback.done(ping, null);
	}

	@Override
	public String toString() {
		return "[Ping Handler] -> " + this.target.getName();
	}

	@ConstructorProperties({ "target", "callback" })
	public PingHandler(final ServerInfo target, final Callback<ServerPing> callback) {
		this.target = target;
		this.callback = callback;
	}

	static {
		pingBuf = new byte[] { -2, 1 };
	}
}
