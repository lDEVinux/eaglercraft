package net.md_5.bungee.eaglercraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.MOTD;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.event.WebsocketMOTDEvent;
import net.md_5.bungee.api.event.WebsocketQueryEvent;
import net.md_5.bungee.eaglercraft.WebSocketRateLimiter.RateLimit;

public class WebSocketListener extends WebSocketServer {

	public static final String queryResponseBlocked = "{\"type\":\"blocked\"}";
	public static final String queryResponseLockout = "{\"type\":\"locked\"}";

	public static final String ipBlockedString = "BLOCKED";
	public static final String ipLockedString = "LOCKED";
	
	public static class PendingSocket {
		public long openTime;
		public InetAddress realAddress;
		public String origin;
		public boolean bypassBan;
		protected PendingSocket(long openTime, InetAddress realAddress, String origin, boolean bypassBan) {
			this.openTime = openTime;
			this.realAddress = realAddress;
			this.origin = origin;
			this.bypassBan = bypassBan;
		}
	}

	private InetSocketAddress bungeeProxy;
	private ProxyServer bungeeCord;
	private boolean blockOriginless;
	private ListenerInfo info;
	private final WebSocketRateLimiter ratelimitIP;
	private final WebSocketRateLimiter ratelimitLogin;
	private final WebSocketRateLimiter ratelimitMOTD;
	private final WebSocketRateLimiter ratelimitQuery;
	
	public WebSocketListener(ListenerInfo info, InetSocketAddress sock, ProxyServer bungeeCord) {
		super(info.getHost());
		this.setReuseAddr(true);
		this.setTcpNoDelay(true);
		this.setConnectionLostTimeout(20);
		this.start();
		this.info = info;
		this.bungeeProxy = sock;
		this.bungeeCord = bungeeCord;
		this.blockOriginless = ((BungeeCord)bungeeCord).config.shouldBlacklistOriginless();
		this.ratelimitIP = info.getRateLimitIP();
		this.ratelimitLogin = info.getRateLimitLogin();
		this.ratelimitMOTD = info.getRateLimitMOTD();
		this.ratelimitQuery = info.getRateLimitQuery();
		if(this.ratelimitIP != null) {
			this.ratelimitIP.resetLimiters();
		}
		if(this.ratelimitLogin != null) {
			this.ratelimitLogin.resetLimiters();
		}
		if(this.ratelimitMOTD != null) {
			this.ratelimitMOTD.resetLimiters();
		}
		if(this.ratelimitQuery != null) {
			this.ratelimitQuery.resetLimiters();
		}
	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		Object o = arg0.getAttachment();
		if(o != null) {
			if(o instanceof WebSocketProxy) {
				((WebSocketProxy)arg0.getAttachment()).killConnection();
			}
		}
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		arg1.printStackTrace();
	}

	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		Object o = arg0.getAttachment();
		if(o != null) {
			if(o instanceof PendingSocket) {
				InetAddress realAddr = ((PendingSocket)o).realAddress;
				arg1 = arg1.trim().toLowerCase();
				QueryConnectionImpl con;
				if(arg1.startsWith("accept:")) {
					arg1 = arg1.substring(7).trim();
					WebsocketQueryEvent evt;
					if(arg1.startsWith("motd")) {
						if(info.isAllowMOTD()) {
							if(ratelimitMOTD != null && !BanList.isBlockedBan(realAddr)) {
								RateLimit l = ratelimitMOTD.rateLimit(realAddr);
								if(l.blocked()) {
									if(l == RateLimit.LIMIT) {
										arg0.send(queryResponseBlocked);
									}else if(l == RateLimit.NOW_LOCKED_OUT) {
										arg0.send(queryResponseLockout);
									}
									arg0.close();
									return;
								}
							}
							con = new MOTDConnectionImpl(info, realAddr, arg0, arg1);
							evt = new WebsocketMOTDEvent((MOTD)con);
						}else {
							arg0.send(queryResponseBlocked);
							arg0.close();
							return;
						}
					}else {
						if(QueryConnectionImpl.confirmHash != null && arg1.equalsIgnoreCase(QueryConnectionImpl.confirmHash)) {
							QueryConnectionImpl.confirmHash = null;
							arg0.send("OK");
							arg0.close();
							return;
						}else if(info.isAllowQuery()) {
							if(ratelimitQuery != null && !BanList.isBlockedBan(realAddr)) {
								RateLimit l = ratelimitQuery.rateLimit(realAddr);
								if(l.blocked()) {
									if(l == RateLimit.LIMIT) {
										arg0.send(queryResponseBlocked);
									}else if(l == RateLimit.NOW_LOCKED_OUT) {
										arg0.send(queryResponseLockout);
									}
									arg0.close();
									return;
								}
							}
							con = new QueryConnectionImpl(info, realAddr, arg0, arg1);
							evt = new WebsocketQueryEvent(con);
						}else {
							arg0.send(queryResponseBlocked);
							arg0.close();
							return;
						}
					}
					BungeeCord.getInstance().getPluginManager().callEvent(evt);
					if(!con.isClosed() && (con instanceof MOTDConnectionImpl)) {
						((MOTDConnectionImpl)con).sendToUser();
					}
					if(!con.shouldKeepAlive() && !con.isClosed()) {
						con.close();
					}else {
						if(!arg0.isClosed()) {
							arg0.setAttachment(con);
						}
					}
				}else {
					arg0.close();
				}
				return;
			}else if(o instanceof QueryConnectionImpl) {
				((QueryConnectionImpl)o).postMessage(arg1);
			}
		}else {
			arg0.close();
		}
	}

	@Override
	public void onMessage(WebSocket arg0, ByteBuffer arg1) {
		Object o = arg0.getAttachment();
		if(o == null || (o instanceof PendingSocket)) {
			InetAddress realAddr;
			if(o == null) {
				realAddr = arg0.getRemoteSocketAddress().getAddress();
			}else {
				realAddr = ((PendingSocket)o).realAddress;
			}
			if(ratelimitLogin != null && !BanList.isBlockedBan(realAddr)) {
				RateLimit l = ratelimitLogin.rateLimit(realAddr);
				if(l.blocked()) {
					if(l == RateLimit.LIMIT) {
						arg0.send(createRawKickPacket("BLOCKED"));
					}else if(l == RateLimit.NOW_LOCKED_OUT) {
						arg0.send(createRawKickPacket("LOCKED"));
					}
					arg0.close();
					return;
				}
			}
			WebSocketProxy proxyObj = new WebSocketProxy(arg0, realAddr, ((PendingSocket)o).origin, bungeeProxy);
			arg0.setAttachment(proxyObj);
			if(!proxyObj.connect()) {
				System.err.println("loopback to '" + bungeeProxy.toString() + "' failed - " + realAddr);
				arg0.close();
				return;
			}
			o = proxyObj;
		}
		if(o != null) {
			if(o instanceof WebSocketProxy) {
				((WebSocketProxy)o).sendPacket(arg1);
			}else {
				arg0.close();
			}
		}
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		String origin = arg1.getFieldValue("Origin");
		if(origin != null) {
			int idx = origin.indexOf("://");
			if(idx != -1) {
				origin = origin.substring(idx + 3);
			}
			origin = origin.trim().toLowerCase();
			if(DomainBlacklist.test(origin)) {
				arg0.send(createRawKickPacket("End of Stream"));
				arg0.close();
				return;
			}
		}else {
			if(blockOriginless) {
				arg0.send(createRawKickPacket("End of Stream"));
				arg0.close();
				return;
			}
		}
		String ua = arg1.getFieldValue("User-Agent");
		if(blockOriginless && (ua == null || (ua = ua.toLowerCase()).contains("java-websocket") || ua.contains("tootallnate") || ua.contains("eaglercraft"))) {
			arg0.send(createRawKickPacket("End of Stream"));
			arg0.close();
			return;
		}
		InetAddress addr;
		if(info.hasForwardedHeaders()) {
			String s = arg1.getFieldValue(info.getForwardedIPHeader());
			if(s != null) {
				try {
					addr = InetAddress.getByName(s.split(",", 2)[0]);
				}catch(UnknownHostException e) {
					System.out.println("invalid '" + info.getForwardedIPHeader() + "' header - " + e.toString());
					arg0.close();
					return;
				}
			}else {
				addr = arg0.getRemoteSocketAddress().getAddress();
			}
		}else {
			addr = arg0.getRemoteSocketAddress().getAddress();
		}
		boolean bypassBan = BanList.isBlockedBan(addr);
		if(!bypassBan && ratelimitIP != null) {
			RateLimit l = ratelimitIP.rateLimit(addr);
			if(l.blocked()) {
				if(l == RateLimit.LIMIT) {
					arg0.send(ipBlockedString);
				}else if(l == RateLimit.NOW_LOCKED_OUT) {
					arg0.send(ipLockedString);
				}
				arg0.close();
				return;
			}
		}
		arg0.setAttachment(new PendingSocket(System.currentTimeMillis(), addr, origin, bypassBan));
	}

	@Override
	public void onStart() {
	}

	public void closeInactiveSockets() {
		for(WebSocket w : this.getConnections()) {
			Object o = w.getAttachment();
			if(o == null) {
				w.close();
			}else if(o instanceof PendingSocket) {
				if(System.currentTimeMillis() - ((PendingSocket)o).openTime > 1500l) {
					w.close();
				}
			}
		}
	}
	
	@Override
	public void stop() throws IOException, InterruptedException {
		for(WebSocket w : this.getConnections()) {
			Object o = w.getAttachment();
			if(o != null && o instanceof WebSocketProxy) {
				((WebSocketProxy)o).killConnection();
			}
		}
		super.stop();
	}
	
	private byte[] createRawKickPacket(String str) {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bao);
		try {
			dout.write(255);
			dout.writeShort(str.length());
			dout.writeChars(str);
			return bao.toByteArray();
		}catch(IOException e) {
			return new byte[] { (byte)255, 0, 0 };
		}
	}
	
	public ListenerInfo getInfo() {
		return info;
	}

}
