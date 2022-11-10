// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee;

import java.beans.ConstructorProperties;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.PacketFFKick;

public class ServerConnection implements Server {
	private final ChannelWrapper ch;
	private final BungeeServerInfo info;
	private boolean isObsolete;
	private final Connection.Unsafe unsafe;

	@Override
	public void sendData(final String channel, final byte[] data) {
		this.unsafe().sendPacket(new PacketFAPluginMessage(channel, data));
	}

	@Override
	public synchronized void disconnect(final String reason) {
		if (this.ch != null && !this.ch.isClosed()) {
			this.unsafe().sendPacket(new PacketFFKick(reason));
			this.ch.getHandle().eventLoop().schedule((Runnable) new Runnable() {
				@Override
				public void run() {
					ServerConnection.this.ch.getHandle().close();
				}
			}, 100L, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public InetSocketAddress getAddress() {
		return this.getInfo() == null ? null : this.getInfo().getAddress();
	}

	@Override
	public Connection.Unsafe unsafe() {
		return this.unsafe;
	}

	@ConstructorProperties({ "ch", "info" })
	public ServerConnection(final ChannelWrapper ch, final BungeeServerInfo info) {
		this.unsafe = new Connection.Unsafe() {
			@Override
			public void sendPacket(final DefinedPacket packet) {
				if (ServerConnection.this.ch != null) ServerConnection.this.ch.write(packet);
			}
		};
		this.ch = ch;
		this.info = info;
	}

	public ChannelWrapper getCh() {
		return this.ch;
	}

	@Override
	public BungeeServerInfo getInfo() {
		return this.info;
	}

	public boolean isObsolete() {
		return this.isObsolete;
	}

	public void setObsolete(final boolean isObsolete) {
		this.isObsolete = isObsolete;
	}
}
