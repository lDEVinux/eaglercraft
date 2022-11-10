// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.connection;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EntityMap;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.TexturePackInfo;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.packet.Packet0KeepAlive;
import net.md_5.bungee.protocol.packet.Packet3Chat;
import net.md_5.bungee.protocol.packet.PacketCCSettings;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;

public class UpstreamBridge extends PacketHandler {
	private final ProxyServer bungee;
	private final UserConnection con;

	public UpstreamBridge(final ProxyServer bungee, final UserConnection con) {
		this.bungee = bungee;
		this.con = con;
		BungeeCord.getInstance().addConnection(con);
		con.getTabList().onConnect();
		con.unsafe().sendPacket(BungeeCord.getInstance().registerChannels());
		final TexturePackInfo texture = con.getPendingConnection().getListener().getTexturePack();
		if (texture != null) {
			con.setTexturePack(texture);
		}
	}

	@Override
	public void exception(final Throwable t) throws Exception {
		this.con.disconnect(Util.exception(t));
	}

	@Override
	public void disconnected(final ChannelWrapper channel) throws Exception {
		final PlayerDisconnectEvent event = new PlayerDisconnectEvent(this.con);
		this.bungee.getPluginManager().callEvent(event);
		this.con.getTabList().onDisconnect();
		BungeeCord.getInstance().removeConnection(this.con);
		if (this.con.getServer() != null && this.con.getServer().getInfo() != null) {
			this.con.getServer().disconnect("Quitting");
		}
	}

	@Override
	public void handle(final byte[] buf) throws Exception {
		EntityMap.rewrite(buf, this.con.getClientEntityId(), this.con.getServerEntityId());
		if (this.con.getServer() != null && this.con.getServer().getCh() != null) {
			this.con.getServer().getCh().write(buf);
		}
	}

	@Override
	public void handle(final Packet0KeepAlive alive) throws Exception {
		if (alive.getRandomId() == this.con.getSentPingId()) {
			final int newPing = (int) (System.currentTimeMillis() - this.con.getSentPingTime());
			this.con.getTabList().onPingChange(newPing);
			this.con.setPing(newPing);
		}
	}

	@Override
	public void handle(final Packet3Chat chat) throws Exception {
		final ChatEvent chatEvent = new ChatEvent(this.con, this.con.getServer(), chat.getMessage());
		if (this.bungee.getPluginManager().callEvent(chatEvent).isCancelled()) {
			throw new CancelSendSignal();
		}
		if (chatEvent.isCommand() && this.bungee.getPluginManager().dispatchCommand(this.con, chat.getMessage().substring(1))) {
			throw new CancelSendSignal();
		}
	}

	@Override
	public void handle(final PacketCCSettings settings) throws Exception {
		this.con.setSettings(settings);
	}

	@Override
	public void handle(final PacketFAPluginMessage pluginMessage) throws Exception {
		if (pluginMessage.getTag().equals("BungeeCord")) {
			throw new CancelSendSignal();
		}
		if (pluginMessage.getTag().equals("FML") && (pluginMessage.getData()[0] & 0xFF) == 0x1) {
			throw new CancelSendSignal();
		}
		final PluginMessageEvent event = new PluginMessageEvent(this.con, this.con.getServer(), pluginMessage.getTag(), pluginMessage.getData().clone());
		if (this.bungee.getPluginManager().callEvent(event).isCancelled()) {
			throw new CancelSendSignal();
		}
		if (pluginMessage.getTag().equals("REGISTER")) {
			this.con.getPendingConnection().getRegisterMessages().add(pluginMessage);
		}
	}

	@Override
	public String toString() {
		return "[" + this.con.getName() + "] -> UpstreamBridge";
	}
}
