package net.md_5.bungee.api.event;

import net.md_5.bungee.api.MOTD;

public class WebsocketMOTDEvent extends WebsocketQueryEvent {

	public WebsocketMOTDEvent(MOTD connection) {
		super(connection);
	}
	
	public MOTD getMOTD() {
		return (MOTD)connection;
	}
	
}
