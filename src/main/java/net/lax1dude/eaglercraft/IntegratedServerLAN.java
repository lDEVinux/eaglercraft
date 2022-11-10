package net.lax1dude.eaglercraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.sp.ipc.IPCPacket0CPlayerChannel;
import net.lax1dude.eaglercraft.sp.relay.pkt.*;

public class IntegratedServerLAN {
	
	public static final List<String> currentICEServers = new ArrayList();

	private static RelayServerSocket lanRelaySocket = null;
	
	private static String currentCode = null;

	public static String shareToLAN(Consumer<String> progressCallback, String worldName, boolean worldHidden) {
		currentCode = null;
		RelayServerSocket sock = IntegratedServer.relayManager.getWorkingRelay((str) -> progressCallback.accept("Connecting: " + str),
				IntegratedServer.preferredRelayVersion, worldName + (worldHidden ? ";1" : ";0"));
		if(sock == null) {
			lanRelaySocket = null;
			return null;
		}else {
			progressCallback.accept("Opening: " + sock.getURI());
			IPacket00Handshake hs = (IPacket00Handshake)sock.readPacket();
			lanRelaySocket = sock;
			String code = hs.connectionCode;
			System.out.println("Relay [" + sock.getURI() + "] connected as 'server', code: " + code);
			progressCallback.accept("Opened '" + code + "' on " + sock.getURI());
			long millis = System.currentTimeMillis();
			do {
				if(sock.isClosed()) {
					System.out.println("Relay [" + sock.getURI() + "] connection lost");
					lanRelaySocket = null;
					return null;
				}
				IPacket pkt = sock.readPacket();
				if(pkt != null) {
					if(pkt instanceof IPacket01ICEServers) {
						IPacket01ICEServers ipkt = (IPacket01ICEServers)pkt;
						System.out.println("Relay [" + sock.getURI() + "] provided ICE servers:");
						currentICEServers.clear();
						for(net.lax1dude.eaglercraft.sp.relay.pkt.ICEServerSet.RelayServer srv : ipkt.servers) {
							System.out.println("Relay [" + sock.getURI() + "]     " + srv.type.name()
									+ ": " + srv.address);
							currentICEServers.add(srv.getICEString());
						}
						EaglerAdapter.serverLANInitializeServer(currentICEServers.toArray(new String[currentICEServers.size()]));
						return currentCode = code;
					}else {
						System.err.println("Relay [" + sock.getURI() + "] unexpected packet: " + pkt.getClass().getSimpleName());
						closeLAN();
						return null;
					}
				}
				try {
					Thread.sleep(50l);
				} catch (InterruptedException e) {
				}
			}while(System.currentTimeMillis() - millis < 1000l);
			System.out.println("Relay [" + sock.getURI() + "] relay provide ICE servers timeout");
			closeLAN();
			return null;
		}
	}
	
	public static String getCurrentURI() {
		return lanRelaySocket == null ? "<disconnected>" : lanRelaySocket.getURI();
	}
	
	public static String getCurrentCode() {
		return currentCode == null ? "<undefined>" : currentCode;
	}

	public static void closeLAN() {
		closeLANNoKick();
		EaglerAdapter.serverLANCloseServer();
		cleanupLAN();
	}
	
	public static void closeLANNoKick() {
		if(lanRelaySocket != null) {
			lanRelaySocket.close();
			lanRelaySocket = null;
			currentCode = null;
		}
	}
	
	public static void cleanupLAN() {
		Iterator<LANClient> itr = clients.values().iterator();
		while(itr.hasNext()) {
			itr.next().disconnect();
		}
		clients.clear();
	}

	public static boolean isHostingLAN() {
		return lanRelaySocket != null || EaglerAdapter.countPeers() > 0;
	}

	public static boolean isLANOpen() {
		return lanRelaySocket != null;
	}
	
	private static final Map<String, LANClient> clients = new HashMap();
	
	public static void updateLANServer() {
		if(lanRelaySocket != null) {
			IPacket pkt;
			while((pkt = lanRelaySocket.readPacket()) != null) {
				if(pkt instanceof IPacket02NewClient) {
					IPacket02NewClient ipkt = (IPacket02NewClient) pkt;
					if(clients.containsKey(ipkt.clientId)) {
						System.err.println("Relay [" + lanRelaySocket.getURI() + "] relay provided duplicate client '" + ipkt.clientId + "'");
					}else {
						clients.put(ipkt.clientId, new LANClient(ipkt.clientId));
					}
				}else if(pkt instanceof IPacket03ICECandidate) {
					IPacket03ICECandidate ipkt = (IPacket03ICECandidate) pkt;
					LANClient c = clients.get(ipkt.peerId);
					if(c != null) {
						c.handleICECandidates(ipkt.candidate);
					}else {
						System.err.println("Relay [" + lanRelaySocket.getURI() + "] relay sent IPacket03ICECandidate for unknown client '" + ipkt.peerId + "'");
					}
				}else if(pkt instanceof IPacket04Description) {
					IPacket04Description ipkt = (IPacket04Description) pkt;
					LANClient c = clients.get(ipkt.peerId);
					if(c != null) {
						c.handleDescription(ipkt.description);
					}else {
						System.err.println("Relay [" + lanRelaySocket.getURI() + "] relay sent IPacket04Description for unknown client '" + ipkt.peerId + "'");
					}
				}else if(pkt instanceof IPacket05ClientSuccess) {
					IPacket05ClientSuccess ipkt = (IPacket05ClientSuccess) pkt;
					LANClient c = clients.get(ipkt.clientId);
					if(c != null) {
						c.handleSuccess();
					}else {
						System.err.println("Relay [" + lanRelaySocket.getURI() + "] relay sent IPacket05ClientSuccess for unknown client '" + ipkt.clientId + "'");
					}
				}else if(pkt instanceof IPacket06ClientFailure) {
					IPacket06ClientFailure ipkt = (IPacket06ClientFailure) pkt;
					LANClient c = clients.get(ipkt.clientId);
					if(c != null) {
						c.handleFailure();
					}else {
						System.err.println("Relay [" + lanRelaySocket.getURI() + "] relay sent IPacket06ClientFailure for unknown client '" + ipkt.clientId + "'");
					}
				}else if(pkt instanceof IPacketFFErrorCode) {
					IPacketFFErrorCode ipkt = (IPacketFFErrorCode) pkt;
					System.err.println("Relay [" + lanRelaySocket.getURI() + "] error code thrown: " +
							IPacketFFErrorCode.code2string(ipkt.code) + "(" + ipkt.code + "): " + ipkt.desc);
					Throwable t;
					while((t = lanRelaySocket.getException()) != null) {
						t.printStackTrace();
					}
				}else {
					System.err.println("Relay [" + lanRelaySocket.getURI() + "] unexpected packet: " + pkt.getClass().getSimpleName());
				}
			}
			if(lanRelaySocket.isClosed()) {
				lanRelaySocket = null;
			}
		}
		Iterator<LANClient> itr = clients.values().iterator();
		while(itr.hasNext()) {
			LANClient cl = itr.next();
			cl.update();
			if(cl.dead) {
				itr.remove();
			}
		}
	}
	
	private static final class LANClient {

		private static final int PRE = 0, SENT_ICE_CANDIDATE = 2, SENT_DESCRIPTION = 3, CONNECTED = 4, CLOSED = 5;
		
		protected final String clientId;
		
		protected int state = PRE;
		protected boolean dead = false;
		
		protected LANClient(String clientId) {
			this.clientId = clientId;
			EaglerAdapter.serverLANCreatePeer(clientId);
		}
		
		protected void handleICECandidates(String candidates) {
			if(state == SENT_DESCRIPTION) {
				EaglerAdapter.serverLANPeerICECandidates(clientId, candidates);
				long millis = System.currentTimeMillis();
				do {
					LANPeerEvent evt;
					if((evt = EaglerAdapter.serverLANGetEvent(clientId)) != null) {
						if(evt instanceof LANPeerEvent.LANPeerICECandidateEvent) {
							lanRelaySocket.writePacket(new IPacket03ICECandidate(clientId, ((LANPeerEvent.LANPeerICECandidateEvent)evt).candidates));
							state = SENT_ICE_CANDIDATE;
							return;
						}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
							System.err.println("LAN client '" + clientId + "' disconnected while waiting for server ICE candidates");
						}else {
							System.err.println("LAN client '" + clientId + "' had an accident: " + evt.getClass().getSimpleName());
						}
						disconnect();
						return;
					}
					try {
						Thread.sleep(20l);
					} catch (InterruptedException e) {
					}
				}while(System.currentTimeMillis() - millis < 5000l);
				System.err.println("Getting server ICE candidates for '" + clientId + "' timed out!");
				disconnect();
			}else {
				System.err.println("Relay [" + lanRelaySocket.getURI() + "] unexpected IPacket03ICECandidate for '" + clientId + "'");
			}
		}
		
		protected void handleDescription(String description) {
			if(state == PRE) {
				EaglerAdapter.serverLANPeerDescription(clientId, description);
				long millis = System.currentTimeMillis();
				do {
					LANPeerEvent evt;
					if((evt = EaglerAdapter.serverLANGetEvent(clientId)) != null) {
						if(evt instanceof LANPeerEvent.LANPeerDescriptionEvent) {
							lanRelaySocket.writePacket(new IPacket04Description(clientId, ((LANPeerEvent.LANPeerDescriptionEvent)evt).description));
							state = SENT_DESCRIPTION;
							return;
						}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
							System.err.println("LAN client '" + clientId + "' disconnected while waiting for server description");
						}else {
							System.err.println("LAN client '" + clientId + "' had an accident: " + evt.getClass().getSimpleName());
						}
						disconnect();
						return;
					}
					try {
						Thread.sleep(20l);
					} catch (InterruptedException e) {
					}
				}while(System.currentTimeMillis() - millis < 5000l);
				System.err.println("Getting server description for '" + clientId + "' timed out!");
				disconnect();
			}else {
				System.err.println("Relay [" + lanRelaySocket.getURI() + "] unexpected IPacket04Description for '" + clientId + "'");
			}
		}
		
		protected void handleSuccess() {
			if(state == SENT_ICE_CANDIDATE) {
				long millis = System.currentTimeMillis();
				do {
					LANPeerEvent evt;
					while((evt = EaglerAdapter.serverLANGetEvent(clientId)) != null && evt instanceof LANPeerEvent.LANPeerICECandidateEvent) {
						// skip ice candidates
					}
					if(evt != null) {
						if(evt instanceof LANPeerEvent.LANPeerDataChannelEvent) {
							EaglerAdapter.enableChannel("NET|" + clientId);
							IntegratedServer.sendIPCPacket(new IPCPacket0CPlayerChannel(clientId, true));
							state = CONNECTED;
							return;
						}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
							System.err.println("LAN client '" + clientId + "' disconnected while waiting for connection");
						}else {
							System.err.println("LAN client '" + clientId + "' had an accident: " + evt.getClass().getSimpleName());
						}
						disconnect();
						return;
					}
					try {
						Thread.sleep(20l);
					} catch (InterruptedException e) {
					}
				}while(System.currentTimeMillis() - millis < 5000l);
				System.err.println("Getting server description for '" + clientId + "' timed out!");
				disconnect();
			}else {
				System.err.println("Relay [" + lanRelaySocket.getURI() + "] unexpected IPacket05ClientSuccess for '" + clientId + "'");
			}
		}
		
		protected void handleFailure() {
			if(state == SENT_ICE_CANDIDATE) {
				System.err.println("Client '" + clientId + "' failed to connect");
				disconnect();
			}else {
				System.err.println("Relay [" + lanRelaySocket.getURI() + "] unexpected IPacket06ClientFailure for '" + clientId + "'");
			}
		}
		
		protected void update() {
			if(state == CONNECTED) {
				PKT pk;
				while(state == CONNECTED && (pk = EaglerAdapter.recieveFromIntegratedServer("NET|" + clientId)) != null) {
					EaglerAdapter.serverLANWritePacket(clientId, pk.data);
				}
				LANPeerEvent evt;
				while(state == CONNECTED && (evt = EaglerAdapter.serverLANGetEvent(clientId)) != null) {
					if(evt instanceof LANPeerEvent.LANPeerPacketEvent) {
						EaglerAdapter.sendToIntegratedServer("NET|" + clientId, ((LANPeerEvent.LANPeerPacketEvent)evt).payload);
					}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
						System.out.println("LAN client '" + clientId + "' disconnected");
						disconnect();
					}else {
						System.err.println("LAN client '" + clientId + "' had an accident: " + evt.getClass().getSimpleName());
						disconnect();
					}
				}
			}
		}
		
		protected void disconnect() {
			if(!dead) {
				if(state == CONNECTED) {
					IntegratedServer.sendIPCPacket(new IPCPacket0CPlayerChannel(clientId, false));
					EaglerAdapter.disableChannel("NET|" + clientId);
				}
				state = CLOSED;
				EaglerAdapter.serverLANDisconnectPeer(clientId);
				dead = true;
			}
		}
		
	}
}
