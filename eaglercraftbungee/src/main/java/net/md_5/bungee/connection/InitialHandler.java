// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.connection;

import java.beans.ConstructorProperties;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.PacketConstants;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.eaglercraft.AuthHandler;
import net.md_5.bungee.eaglercraft.BanList;
import net.md_5.bungee.eaglercraft.BanList.BanCheck;
import net.md_5.bungee.eaglercraft.BanList.BanState;
import net.md_5.bungee.eaglercraft.WebSocketProxy;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.CipherDecoder;
import net.md_5.bungee.netty.CipherEncoder;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PacketDecoder;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Forge;
import net.md_5.bungee.protocol.packet.*;

public class InitialHandler extends PacketHandler implements PendingConnection {
	private final ProxyServer bungee;
	private ChannelWrapper ch;
	private final ListenerInfo listener;
	private Packet1Login forgeLogin;
	private Packet2Handshake handshake;
	private PacketFDEncryptionRequest request;
	private List<PacketFAPluginMessage> loginMessages;
	private List<PacketFAPluginMessage> registerMessages;
	private State thisState;
	private SecretKey sharedKey;
	private final Connection.Unsafe unsafe;

	@Override
	public void connected(final ChannelWrapper channel) throws Exception {
		this.ch = channel;
	}

	@Override
	public void exception(final Throwable t) throws Exception {
		this.disconnect(ChatColor.RED + Util.exception(t));
	}

	@Override
	public void handle(final PacketFAPluginMessage pluginMessage) throws Exception {
		if (pluginMessage.getTag().equals("REGISTER")) {
			this.registerMessages.add(pluginMessage);
		} else {
			this.loginMessages.add(pluginMessage);
		}
	}

	@Override
	public void handle(final PacketFEPing ping) throws Exception {
		ServerPing response = new ServerPing(this.bungee.getProtocolVersion(), this.bungee.getGameVersion(), this.listener.getMotd(), this.bungee.getOnlineCount(), this.listener.getMaxPlayers());
		response = this.bungee.getPluginManager().callEvent(new ProxyPingEvent(this, response)).getResponse();
		final String kickMessage = ChatColor.DARK_BLUE + "\u0000" + response.getProtocolVersion() + "\u0000" + response.getGameVersion() + "\u0000" + response.getMotd() + "\u0000" + response.getCurrentPlayers() + "\u0000"
				+ response.getMaxPlayers();
		this.disconnect(kickMessage);
	}

	@Override
	public void handle(final Packet1Login login) throws Exception {
		Preconditions.checkState(this.thisState == State.LOGIN, (Object) "Not expecting FORGE LOGIN");
		Preconditions.checkState(this.forgeLogin == null, (Object) "Already received FORGE LOGIN");
		this.forgeLogin = login;
		((PacketDecoder) this.ch.getHandle().pipeline().get((Class) PacketDecoder.class)).setProtocol(Forge.getInstance());
	}

	@Override
	public void handle(final Packet2Handshake handshake) throws Exception {
		Preconditions.checkState(this.thisState == State.HANDSHAKE, (Object) "Not expecting HANDSHAKE");
		this.handshake = handshake;
		this.bungee.getLogger().log(Level.INFO, "{0} has connected", this);
		boolean skipEncryption = false;
		if (handshake.getProcolVersion() == 69) {
			skipEncryption = true;
			this.handshake.swapProtocol((byte) 61);
		}else if(handshake.getProcolVersion() == 71) {
			this.disconnect("this server does not support microsoft accounts");
			return;
		}else if(handshake.getProcolVersion() != 61) {
			this.disconnect("minecraft 1.5.2 required for eaglercraft backdoor access");
			return;
		}
		String un = handshake.getUsername();
		if (un.length() < 3) {
			this.disconnect("Username must be at least 3 characters");
			return;
		}
		if (un.length() > 16) {
			this.disconnect("Cannot have username longer than 16 characters");
			return;
		}
		if(!un.equals(un.replaceAll("[^A-Za-z0-9\\-_]", "_").trim())) {
			this.disconnect("Go fuck yourself");
			return;
		}
		if (BungeeCord.getInstance().tokenVerify.isEmpty()) {
			String hostname = handshake.getHost();
			if (hostname.contains(":")) {
				handshake.setHost(hostname.substring(0, hostname.indexOf(':')));
			}
		} else {
			handshake.setHost(BungeeCord.getInstance().tokenVerify);
			handshake.setPort(0);
		}
		InetAddress sc;
		synchronized(WebSocketProxy.localToRemote) {
			sc = WebSocketProxy.localToRemote.get(this.ch.getHandle().remoteAddress());
		}
		if(sc == null) {
			this.bungee.getLogger().log(Level.WARNING, "player '" + un + "' doesn't have a websocket IP, remote address: " + this.ch.getHandle().remoteAddress().toString());
		}else {
			BanCheck bc = BanList.checkIpBanned(sc);
			if(bc.isBanned()) {
				this.bungee.getLogger().log(Level.SEVERE, "Player '" + un + "' [" + sc.toString() + "] is banned by IP: " + bc.match + " (" + bc.string + ")");
				this.disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: " + bc.string);
				return;
			}else {
				this.bungee.getLogger().log(Level.INFO, "Player '" + un + "' [" + sc.toString() + "] has remote websocket IP: " + sc.getHostAddress());
			}
		}
		String dnm;
		synchronized(WebSocketProxy.localToRemote) {
			dnm = WebSocketProxy.origins.get(this.ch.getHandle().remoteAddress());
		}
		if(dnm != null) {
			if(dnm.equalsIgnoreCase("null")) {
				this.bungee.getLogger().log(Level.INFO, "Player '" + un + "' [" + sc.toString() + "] is using an offline download");
			}else {
				this.bungee.getLogger().log(Level.INFO, "Player '" + un + "' [" + sc.toString() + "] is using a client at: " + dnm);
			}
		}
		BanCheck bc = BanList.checkBanned(un);
		if(bc.isBanned()) {
			switch(bc.reason) {
			case USER_BANNED:
				this.bungee.getLogger().log(Level.SEVERE, "Player '" + un + "' is banned by username, because '" + bc.string + "'");
				break;
			case WILDCARD_BANNED:
				this.bungee.getLogger().log(Level.SEVERE, "Player '" + un + "' is banned by wildcard: " + bc.match);
				break;
			case REGEX_BANNED:
				this.bungee.getLogger().log(Level.SEVERE, "Player '" + un + "' is banned by regex: " + bc.match);
				break;
			default:
				this.bungee.getLogger().log(Level.SEVERE, "Player '" + un + "' is banned: " + bc.string);
			}
			if(bc.reason == BanState.USER_BANNED || ((BungeeCord)bungee).config.shouldShowBanType()) {
				this.disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: " + bc.string);
			}else {
				this.disconnect("" + ChatColor.RED + "You are banned.");
			}
			return;
		}
		final int limit = BungeeCord.getInstance().config.getPlayerLimit();
		if (limit > 0 && this.bungee.getOnlineCount() > limit) {
			this.disconnect(this.bungee.getTranslation("proxy_full"));
			return;
		}
		if (!BungeeCord.getInstance().config.isOnlineMode() && this.bungee.getPlayer(un) != null) {
			this.disconnect(this.bungee.getTranslation("already_connected"));
			return;
		}
		this.unsafe().sendPacket(PacketConstants.I_AM_BUNGEE);
		this.unsafe().sendPacket(PacketConstants.FORGE_MOD_REQUEST);
		if(skipEncryption) {
			InitialHandler.this.thisState = State.LOGIN;
			handle((PacketCDClientStatus)null);
		}else {
			this.unsafe().sendPacket(this.request = EncryptionUtil.encryptRequest());
			this.thisState = State.ENCRYPT;
		}
	}

	@Override
	public void handle(final PacketFCEncryptionResponse encryptResponse) throws Exception {
		Preconditions.checkState(this.thisState == State.ENCRYPT, (Object) "Not expecting ENCRYPT");
		this.sharedKey = EncryptionUtil.getSecret(encryptResponse, this.request);
        final Cipher decrypt = EncryptionUtil.getCipher(2, this.sharedKey);
        this.ch.getHandle().pipeline().addBefore(PipelineUtils.PACKET_DECODE_HANDLER, PipelineUtils.DECRYPT_HANDLER, (ChannelHandler)new CipherDecoder(decrypt));
        this.finish();
	}

	private void finish() throws GeneralSecurityException {
		final ProxiedPlayer old = this.bungee.getPlayer(this.handshake.getUsername());
		if (old != null) {
			old.disconnect(this.bungee.getTranslation("already_connected"));
		}
		final Callback<LoginEvent> complete = new Callback<LoginEvent>() {
			@Override
			public void done(final LoginEvent result, final Throwable error) {
				if (result.isCancelled()) {
					InitialHandler.this.disconnect(result.getCancelReason());
				}
				if (InitialHandler.this.ch.isClosed()) {
					return;
				}
				InitialHandler.this.thisState = State.LOGIN;
				InitialHandler.this.ch.getHandle().eventLoop().execute((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        InitialHandler.this.unsafe().sendPacket(new PacketFCEncryptionResponse(new byte[0], new byte[0]));
                        try {
                            final Cipher encrypt = EncryptionUtil.getCipher(1, InitialHandler.this.sharedKey);
                            InitialHandler.this.ch.getHandle().pipeline().addBefore(PipelineUtils.DECRYPT_HANDLER, PipelineUtils.ENCRYPT_HANDLER, (ChannelHandler)new CipherEncoder(encrypt));
                        }
                        catch (GeneralSecurityException ex) {
                            InitialHandler.this.disconnect("Cipher error: " + Util.exception(ex));
                        }
                    }
                });
			}
		};
		this.bungee.getPluginManager().callEvent(new LoginEvent(this, complete));
	}

	@Override
	public void handle(final PacketCDClientStatus clientStatus) throws Exception {
		Preconditions.checkState(this.thisState == State.LOGIN, (Object) "Not expecting LOGIN");
		final UserConnection userCon = new UserConnection(this.bungee, this.ch, this.getName(), this);
		InetAddress ins = WebSocketProxy.localToRemote.get(this.ch.getHandle().remoteAddress());
		if(ins != null) {
			userCon.getAttachment().put("remoteAddr", ins);
		}
		String origin = WebSocketProxy.origins.get(this.ch.getHandle().remoteAddress());
		if(origin != null) {
			userCon.getAttachment().put("origin", origin);
		}
		userCon.init();
		HandlerBoss handlerBoss = ((HandlerBoss) this.ch.getHandle().pipeline().get((Class) HandlerBoss.class));
		if (BungeeCord.getInstance().config.getAuthInfo().isEnabled()) {
			handlerBoss.setHandler(new AuthHandler(this.bungee, userCon, handlerBoss));
		} else {
			this.bungee.getPluginManager().callEvent(new PostLoginEvent(userCon));
			handlerBoss.setHandler(new UpstreamBridge(this.bungee, userCon));
			final ServerInfo server = this.bungee.getReconnectHandler().getServer(userCon);
			userCon.connect(server, true);
		}
		this.thisState = State.FINISHED;
		throw new CancelSendSignal();
	}

	@Override
	public synchronized void disconnect(final String reason) {
		if (!this.ch.isClosed()) {
			this.unsafe().sendPacket(new PacketFFKick(reason));
			this.ch.close();
		}
	}

	@Override
	public String getName() {
		return (this.handshake == null) ? null : this.handshake.getUsername();
	}

	@Override
	public byte getVersion() {
		return (byte) ((this.handshake == null) ? -1 : this.handshake.getProcolVersion());
	}

	@Override
	public InetSocketAddress getVirtualHost() {
		return (this.handshake == null) ? null : new InetSocketAddress(this.handshake.getHost(), this.handshake.getPort() & 0xFFFF);
	}

	@Override
	public InetSocketAddress getAddress() {
		return (InetSocketAddress) this.ch.getHandle().remoteAddress();
	}

	@Override
	public Connection.Unsafe unsafe() {
		return this.unsafe;
	}

	@Override
	public String toString() {
		return "[" + ((this.getName() != null) ? this.getName() : this.getAddress()) + "] <-> InitialHandler";
	}

	@ConstructorProperties({ "bungee", "listener" })
	public InitialHandler(final ProxyServer bungee, final ListenerInfo listener) {
		this.loginMessages = new ArrayList<PacketFAPluginMessage>();
		this.registerMessages = new ArrayList<PacketFAPluginMessage>();
		this.thisState = State.HANDSHAKE;
		this.unsafe = new Connection.Unsafe() {
			@Override
			public void sendPacket(final DefinedPacket packet) {
				InitialHandler.this.ch.write(packet);
			}
		};
		this.bungee = bungee;
		this.listener = listener;
	}

	@Override
	public ListenerInfo getListener() {
		return this.listener;
	}

	public Packet1Login getForgeLogin() {
		return this.forgeLogin;
	}

	public Packet2Handshake getHandshake() {
		return this.handshake;
	}

	public List<PacketFAPluginMessage> getLoginMessages() {
		return this.loginMessages;
	}

	public List<PacketFAPluginMessage> getRegisterMessages() {
		return this.registerMessages;
	}

	private enum State {
		HANDSHAKE, ENCRYPT, LOGIN, FINISHED;
	}
}
