package net.md_5.bungee.eaglercraft;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.java_websocket.WebSocket;

import net.md_5.bungee.api.QueryConnection;
import net.md_5.bungee.api.config.ListenerInfo;

public class QueryConnectionImpl implements QueryConnection {
	
	protected ListenerInfo listener;
	protected InetAddress addr;
	protected WebSocket socket;
	protected String accept;
	protected String responseType;
	protected List<String> packetBuffer = new LinkedList();
	protected long creationTime;
	protected boolean keepAlive = false;

	public static String confirmHash = null;
	
	public QueryConnectionImpl(ListenerInfo listener, InetAddress addr, WebSocket socket, String accept) {
		this.listener = listener;
		this.addr = addr;
		this.socket = socket;
		this.accept = accept.toLowerCase();
		this.responseType = "unknown";
		this.creationTime = System.currentTimeMillis();
	}
	
	public void postMessage(String m) {
		synchronized(packetBuffer) {
			packetBuffer.add(m);
		}
	}

	@Override
	public InetAddress getRemoteAddress() {
		return addr;
	}

	@Override
	public ListenerInfo getListener() {
		return listener;
	}

	@Override
	public String getAccept() {
		return accept;
	}

	@Override
	public int availableRequests() {
		synchronized(packetBuffer) {
			return packetBuffer.size();
		}
	}

	@Override
	public String readRequestString() {
		synchronized(packetBuffer) {
			if(packetBuffer.size() > 0) {
				return packetBuffer.remove(0);
			}else {
				return null;
			}
		}
	}

	@Override
	public long getConnectionTimestamp() {
		return creationTime;
	}

	@Override
	public void writeResponseRaw(String msg) {
		socket.send(msg);
	}

	@Override
	public void writeResponseBinary(byte[] blob) {
		socket.send(blob);
	}

	@Override
	public void keepAlive(boolean yes) {
		keepAlive = yes;
	}
	
	@Override
	public boolean shouldKeepAlive() {
		return keepAlive;
	}

	@Override
	public boolean isClosed() {
		return socket.isClosing() || socket.isClosed();
	}

	@Override
	public void close() {
		socket.close();
	}

	@Override
	public void setReturnType(String type) {
		if(!"unknown".equals(responseType) && !type.equalsIgnoreCase(responseType)) {
			throw new IllegalStateException("Tried to change query return type to '" + type + "' when it was already set to '" + responseType + "'");
		}
		responseType = type;
	}

	@Override
	public String getReturnType() {
		return responseType;
	}

}
