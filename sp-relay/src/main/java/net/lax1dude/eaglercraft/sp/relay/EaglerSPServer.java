package net.lax1dude.eaglercraft.sp.relay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;

import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket02NewClient;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket04Description;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket03ICECandidate;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket05ClientSuccess;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket06ClientFailure;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacketFEDisconnectClient;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacketFFErrorCode;

public class EaglerSPServer {
	
	public final WebSocket socket;
	public final String code;
	public final Map<String,EaglerSPClient> clients;
	public final String serverName;
	public final String serverAddress;
	public final boolean serverHidden;
	
	EaglerSPServer(WebSocket sock, String code, String serverName, String serverAddress) {
		this.socket = sock;
		this.code = code;
		this.clients = new HashMap();
		
		if(serverName.endsWith(";1")) {
			this.serverHidden = true;
			serverName = serverName.substring(0, serverName.length() - 2);
		}else if(serverName.endsWith(";0")) {
			this.serverHidden = false;
			serverName = serverName.substring(0, serverName.length() - 2);
		}else {
			this.serverHidden = false;
		}
		
		this.serverName = serverName;
		this.serverAddress = serverAddress;
	}
	
	public void send(IPacket packet) {
		if(this.socket.isOpen()) {
			try {
				this.socket.send(IPacket.writePacket(packet));
			}catch(IOException ex) {
				EaglerSPRelay.logger.debug("Error sending data to {}", this.serverAddress);
				EaglerSPRelay.logger.debug(ex);
				try {
					this.socket.send(IPacket.writePacket(new IPacketFFErrorCode(IPacketFFErrorCode.TYPE_INTERNAL_ERROR,
							"Internal Server Error")));
				}catch(IOException ex2) {
				}
				this.socket.close();
			}
		}else {
			EaglerSPRelay.logger.debug("WARNING: Tried to send data to {} after the connection closed.", this.serverAddress);
		}
	}
	
	public boolean handle(IPacket _packet) throws IOException {
		if(_packet instanceof IPacket03ICECandidate) {
			IPacket03ICECandidate packet = (IPacket03ICECandidate)_packet;
			EaglerSPClient cl = clients.get(packet.peerId);
			if(cl != null) {
				if(LoginState.assertEquals(cl, LoginState.SENT_ICE_CANDIDATE)) {
					cl.state = LoginState.RECIEVED_ICE_CANIDATE;
					cl.handleServerICECandidate(packet);
					EaglerSPRelay.logger.debug("[{}][Server -> Relay -> Client] PKT 0x03: ICECandidate", (String) cl.socket.getAttachment());
				}
			}else {
				this.socket.send(IPacket.writePacket(new IPacketFFErrorCode(IPacketFFErrorCode.TYPE_UNKNOWN_CLIENT,
						"Unknown Client ID: " + packet.peerId)));
			}
			return true;
		}else if(_packet instanceof IPacket04Description) {
			IPacket04Description packet = (IPacket04Description)_packet;
			EaglerSPClient cl = clients.get(packet.peerId);
			if(cl != null) {
				if(LoginState.assertEquals(cl, LoginState.SENT_DESCRIPTION)) {
					cl.state = LoginState.RECIEVED_DESCRIPTION;
					cl.handleServerDescription(packet);
					EaglerSPRelay.logger.debug("[{}][Server -> Relay -> Client] PKT 0x04: Description", (String) cl.socket.getAttachment());
				}
			}else {
				this.socket.send(IPacket.writePacket(new IPacketFFErrorCode(IPacketFFErrorCode.TYPE_UNKNOWN_CLIENT,
						"Unknown Client ID: " + packet.peerId)));
			}
			return true;
		}else if(_packet instanceof IPacketFEDisconnectClient) {
			IPacketFEDisconnectClient packet = (IPacketFEDisconnectClient)_packet;
			EaglerSPClient cl = clients.get(packet.clientId);
			if(cl != null) {
				cl.handleServerDisconnectClient(packet);
				EaglerSPRelay.logger.debug("[{}][Server -> Relay -> Client] PKT 0xFE: Disconnect: {}: {}", (String) cl.socket.getAttachment(),
						packet.code, packet.reason);
			}else {
				this.socket.send(IPacket.writePacket(new IPacketFFErrorCode(IPacketFFErrorCode.TYPE_UNKNOWN_CLIENT,
						"Unknown Client ID: " + packet.clientId)));
			}
			return true;
		}else {
			return false;
		}
	}
	
	public void handleNewClient(EaglerSPClient client) {
		synchronized(clients) {
			clients.put(client.id, client);
			send(new IPacket02NewClient(client.id));
			EaglerSPRelay.logger.debug("[{}][Relay -> Server] PKT 0x02: Notify server of the client, id: {}", serverAddress, client.id);
		}
	}
	
	public void handleClientDisconnect(EaglerSPClient client) {
		synchronized(clients) {
			clients.remove(client.id);
		}
		if(!client.serverNotifiedOfClose) {
			send(new IPacketFEDisconnectClient(client.id, IPacketFEDisconnectClient.TYPE_UNKNOWN, "End of stream"));
			client.serverNotifiedOfClose = true;
		}
	}

	public void handleClientICECandidate(EaglerSPClient client, IPacket03ICECandidate packet) {
		send(new IPacket03ICECandidate(client.id, packet.candidate));
	}

	public void handleClientDescription(EaglerSPClient client, IPacket04Description packet) {
		send(new IPacket04Description(client.id, packet.description));
	}

	public void handleClientSuccess(EaglerSPClient client, IPacket05ClientSuccess packet) {
		send(new IPacket05ClientSuccess(client.id));
	}

	public void handleClientFailure(EaglerSPClient client, IPacket06ClientFailure packet) {
		send(new IPacket06ClientFailure(client.id));
	}

}
