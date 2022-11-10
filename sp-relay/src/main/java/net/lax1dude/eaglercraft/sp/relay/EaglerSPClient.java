package net.lax1dude.eaglercraft.sp.relay;

import java.io.IOException;
import java.util.Random;

import org.java_websocket.WebSocket;

import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket04Description;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket03ICECandidate;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket05ClientSuccess;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket06ClientFailure;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacketFEDisconnectClient;

public class EaglerSPClient {
	
	public final WebSocket socket;
	public final EaglerSPServer server;
	public final String id;
	public final long createdOn;
	public boolean serverNotifiedOfClose = false;
	public LoginState state = LoginState.INIT;
	public final String address;
	
	EaglerSPClient(WebSocket sock, EaglerSPServer srv, String id, String addr) {
		this.socket = sock;
		this.server = srv;
		this.id = id;
		this.createdOn = System.currentTimeMillis();
		this.address = addr;
	}
	
	public void send(IPacket packet) {
		if(this.socket.isOpen()) {
			try {
				this.socket.send(IPacket.writePacket(packet));
			}catch(IOException ex) {
				EaglerSPRelay.logger.debug("Error sending data to {}", (String) this.socket.getAttachment());
				EaglerSPRelay.logger.debug(ex);
				disconnect(IPacketFEDisconnectClient.TYPE_INTERNAL_ERROR, "Internal Server Error");
				this.socket.close();
			}
		}else {
			EaglerSPRelay.logger.debug("WARNING: Tried to send data to {} after the connection closed.", (String) this.socket.getAttachment());
		}
	}
	
	public boolean handle(IPacket packet) throws IOException {
		if(packet instanceof IPacket03ICECandidate) {
			if(LoginState.assertEquals(this, LoginState.RECIEVED_DESCRIPTION)) {
				state = LoginState.SENT_ICE_CANDIDATE;
				server.handleClientICECandidate(this, (IPacket03ICECandidate)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x03: ICECandidate", (String) socket.getAttachment());
			}
			return true;
		}else if(packet instanceof IPacket04Description) {
			if(LoginState.assertEquals(this, LoginState.INIT)) {
				state = LoginState.SENT_DESCRIPTION;
				server.handleClientDescription(this, (IPacket04Description)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x04: Description", (String) socket.getAttachment());
			}
			return true;
		}else if(packet instanceof IPacket05ClientSuccess) {
			if(LoginState.assertEquals(this, LoginState.RECIEVED_ICE_CANIDATE)) {
				state = LoginState.FINISHED;
				server.handleClientSuccess(this, (IPacket05ClientSuccess)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x05: ClientSuccess", (String) socket.getAttachment());
				disconnect(IPacketFEDisconnectClient.TYPE_FINISHED_SUCCESS, "Successful connection");
			}
			return true;
		}else if(packet instanceof IPacket06ClientFailure) {
			if(LoginState.assertEquals(this, LoginState.RECIEVED_ICE_CANIDATE)) {
				state = LoginState.FINISHED;
				server.handleClientFailure(this, (IPacket06ClientFailure)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x05: ClientFailure", (String) socket.getAttachment());
				disconnect(IPacketFEDisconnectClient.TYPE_FINISHED_FAILED, "Failed connection");
			}
			return true;
		}else {
			return false;
		}
	}
	
	public void handleServerICECandidate(IPacket03ICECandidate desc) {
		send(new IPacket03ICECandidate("", desc.candidate));
	}
	
	public void handleServerDescription(IPacket04Description desc) {
		send(new IPacket04Description("", desc.description));
	}

	public void handleServerDisconnectClient(IPacketFEDisconnectClient packet) {
		disconnect(packet.code, packet.reason);
	}
	
	public void disconnect(int code, String reason) {
		IPacket pkt = new IPacketFEDisconnectClient(id, code, reason);
		if(!serverNotifiedOfClose) {
			if (code != IPacketFEDisconnectClient.TYPE_FINISHED_SUCCESS) server.send(pkt);
			serverNotifiedOfClose = true;
		}
		if(this.socket.isOpen()) {
			send(pkt);
			socket.close();
		}
		EaglerSPRelay.logger.debug("[{}][Relay -> Client] PKT 0xFE: #{} {}", (String) socket.getAttachment(), code, reason);
	}
	
	public static final int clientCodeLength = 16;
	private static final String clientCodeChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static String generateClientId() {
		Random r = new Random();
		char[] ret = new char[clientCodeLength];
		for(int i = 0; i < ret.length; ++i) {
			ret[i] = clientCodeChars.charAt(r.nextInt(clientCodeChars.length()));
		}
		return new String(ret);
	}

}
