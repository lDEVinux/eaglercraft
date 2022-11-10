package net.md_5.bungee.api.event;

import java.net.InetAddress;

import net.md_5.bungee.api.QueryConnection;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.plugin.Event;

public class WebsocketQueryEvent extends Event {

	protected final QueryConnection connection;
	
	public WebsocketQueryEvent(QueryConnection connection) {
		this.connection = connection;
	}
	
	public InetAddress getRemoteAddress() {
		return connection.getRemoteAddress();
	}
	
	public ListenerInfo getListener() {
		return connection.getListener();
	}
	
	public String getAccept() {
		return connection.getAccept();
	}
	
	public QueryConnection getQuery() {
		return connection;
	}
	
}
