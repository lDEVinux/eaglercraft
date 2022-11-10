package net.lax1dude.eaglercraft.sp.relay;

import net.lax1dude.eaglercraft.sp.relay.pkt.IPacketFEDisconnectClient;

/**
 * SENT = Client has sent something to the server<br>
 * RECIEVED = Server has sent something to the client
 */
public enum LoginState {
	
	INIT, SENT_ICE_CANDIDATE, RECIEVED_ICE_CANIDATE, SENT_DESCRIPTION, RECIEVED_DESCRIPTION, FINISHED;
	
	public static boolean assertEquals(EaglerSPClient client, LoginState state) {
		if(client.state != state) {
			String msg = "client is in state " + client.state.name() + " when it was supposed to be " + state.name();
			client.disconnect(IPacketFEDisconnectClient.TYPE_INVALID_OPERATION, msg);
			EaglerSPRelay.logger.debug("[{}][Relay -> Client] PKT 0xFE: TYPE_INVALID_OPERATION: {}", (String) client.socket.getAttachment(), msg);
			return false;
		}else {
			return true;
		}
	}
	
}
