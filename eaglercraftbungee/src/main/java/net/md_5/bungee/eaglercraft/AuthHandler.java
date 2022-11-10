package net.md_5.bungee.eaglercraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.connection.CancelSendSignal;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.packet.Packet1Login;
import net.md_5.bungee.protocol.packet.Packet9Respawn;
import net.md_5.bungee.protocol.packet.Packet0DPositionAndLook;
import net.md_5.bungee.protocol.packet.Packet3Chat;
import net.md_5.bungee.protocol.packet.Packet0KeepAlive;
import net.md_5.bungee.protocol.packet.PacketCCSettings;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.PacketFEPing;

public class AuthHandler extends PacketHandler {
	private static final AuthSystem authSystem = BungeeCord.getInstance().authSystem;

	private final ProxyServer bungee;
	private final UserConnection con;
	private final HandlerBoss handlerBoss;
	private final String username;

	private static final Collection<AuthHandler> openHandlers = new LinkedList();
	private boolean loggedIn = false;
	private long startTime;

	public AuthHandler(final ProxyServer bungee, final UserConnection con, final HandlerBoss handlerBoss) {
		this.bungee = bungee;
		this.con = con;
		this.handlerBoss = handlerBoss;
		this.username = this.con.getName();
		this.startTime = System.currentTimeMillis();

		synchronized(openHandlers) {
			openHandlers.add(this);
		}
		
		this.con.unsafe().sendPacket(new Packet1Login(0, "END", (byte) 2, 1, (byte) 0, (byte) 0,
				(byte) this.con.getPendingConnection().getListener().getTabListSize()));
		this.con.unsafe().sendPacket(new Packet9Respawn(1, (byte) 0, (byte) 2, (short) 255, "END"));
		this.con.unsafe().sendPacket(new Packet0DPositionAndLook(0, 0, 0, 0, 0f, 0f, true));

		this.con.sendMessages(authSystem.joinMessages);

		if (authSystem.isRegistered(this.username)) {
			this.con.sendMessage("\u00A7cPlease login to continue! /login <password>");
		} else {
			this.con.sendMessage("\u00A7cPlease register to continue! /register <password> <confirmPassword>");
		}
	}

	@Override
	public void exception(final Throwable t) throws Exception {
		this.con.disconnect(Util.exception(t));
	}

	@Override
	public void disconnected(final ChannelWrapper channel) {
		this.loggedIn = true;
	}

	@Override
	public void handle(final Packet0KeepAlive alive) throws Exception {
		if (alive.getRandomId() == this.con.getSentPingId()) {
			final int newPing = (int) (System.currentTimeMillis() - this.con.getSentPingTime());
			this.con.setPing(newPing);
		}
	}

	private List<PacketFAPluginMessage> pms = new ArrayList<>();

	@Override
	public void handle(final PacketFAPluginMessage p) throws Exception {
		pms.add(p);
		throw new CancelSendSignal();
	}

	@Override
	public void handle(final PacketFEPing p) throws Exception {
		this.con.getPendingConnection().handle(p);
	}

	@Override
	public void handle(final Packet3Chat chat) throws Exception {
		String message = chat.getMessage();
		if (message.startsWith("/")) {
			String[] args = message.substring(1).trim().split(" ");
			switch (args[0]) {
			case "login":
			case "l":
				if (args.length == 1) {
					this.con.sendMessage("\u00A7cYou must specify a password to login! /login <password>");
				} else if (!authSystem.isRegistered(this.username)) {
					this.con.sendMessage("\u00A7cThis username is not registered on this server!");
				} else if (authSystem.login(this.username, args[1])) {
					this.con.sendMessage("\u00A7cLogging in...");
					this.onLogin();
				} else {
					this.con.sendMessage("\u00A7cThat password is invalid!");
				}
				break;
			case "register":
			case "reg":
				if(BungeeCord.getInstance().config.getAuthInfo().isRegisterEnabled()) {
					if (args.length == 1 || args.length == 2) {
						this.con.sendMessage("\u00A7cUsage: /" + args[0].toLowerCase() + " <password> <confirmPassword>");
					} else if (!args[1].equals(args[2])) {
						this.con.sendMessage("\u00A7cThose passwords do not match!");
					} else if (authSystem.isRegistered(this.username)) {
						this.con.sendMessage("\u00A7cThis username is already registered!");
					} else if (authSystem.register(this.username, args[1],
							this.con.getAddress().getAddress().getHostAddress())) {
						this.con.sendMessage("\u00A7cSuccessfully registered and logging in...");
						this.onLogin();
					} else {
						this.con.sendMessage("\u00A7cUnable to register...");
					}
				}else {
					this.con.disconnect("Registration is not enabled!");
				}
				break;
			case "changepassword":
			case "changepasswd":
			case "changepwd":
			case "changepass":
				if (args.length == 1 || args.length == 2) {
					this.con.sendMessage("\u00A7cUsage: /" + args[0].toLowerCase() + " <oldPassword> <newPassword>");
				} else if (authSystem.login(this.username, args[1])) {
					if (authSystem.changePass(this.username, args[2])) {
						this.con.sendMessage("\u00A7cPassword changed successfully!");
					} else {
						this.con.sendMessage("\u00A7cUnable to change your password...");
					}
				} else {
					this.con.sendMessage("\u00A7cThe old password specified is incorrect!");
				}
				break;
			default:
			}
		}
	}

	private void onLogin() throws Exception {
		this.loggedIn = true;
		this.bungee.getPluginManager().callEvent(new PostLoginEvent(this.con));
		UpstreamBridge ub = new UpstreamBridge(this.bungee, this.con);
		handlerBoss.setHandler(ub);
		final ServerInfo server = this.bungee.getReconnectHandler().getServer(this.con);
		this.con.setServer(new ServerConnection(null, null));
		this.con.connect(server, true);
		for (PacketFAPluginMessage pm : pms) {
			try {
				ub.handle(pm);
				this.con.getPendingConnection().getLoginMessages().add(pm);
			} catch (CancelSendSignal e) {
				// don't forward to server
			}
		}
		pms.clear();
	}

	@Override
	public void handle(final PacketCCSettings settings) throws Exception {
		this.con.setSettings(settings);
	}

	@Override
	public String toString() {
		return "[" + this.con.getName() + "] -> AuthHandler";
	}
	
	public static void closeInactive(int timeout) {
		synchronized(openHandlers) {
			long millis = System.currentTimeMillis();
			timeout *= 1000;
			Iterator<AuthHandler> handlers = openHandlers.iterator();
			while(handlers.hasNext()) {
				AuthHandler h = handlers.next();
				if(!h.loggedIn) {
					if(millis - h.startTime > timeout) {
						h.con.disconnect("You did not login in time you eagler!");
						handlers.remove();
					}
				}else {
					handlers.remove();
				}
			}
		}
	}
	
}
