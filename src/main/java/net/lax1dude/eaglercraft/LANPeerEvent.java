package net.lax1dude.eaglercraft;

public interface LANPeerEvent {

	String getPeerId();
	
	public static class LANPeerICECandidateEvent implements LANPeerEvent {
		
		public final String clientId;
		public final String candidates;
		
		public LANPeerICECandidateEvent(String clientId, String candidates) {
			this.clientId = clientId;
			this.candidates = candidates;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerDescriptionEvent implements LANPeerEvent {
		
		public final String clientId;
		public final String description;
		
		public LANPeerDescriptionEvent(String clientId, String description) {
			this.clientId = clientId;
			this.description = description;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerDataChannelEvent implements LANPeerEvent {
		
		public final String clientId;
		
		public LANPeerDataChannelEvent(String clientId) {
			this.clientId = clientId;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerPacketEvent implements LANPeerEvent {
		
		public final String clientId;
		public final byte[] payload;
		
		public LANPeerPacketEvent(String clientId, byte[] payload) {
			this.clientId = clientId;
			this.payload = payload;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerDisconnectEvent implements LANPeerEvent {
		
		public final String clientId;
		
		public LANPeerDisconnectEvent(String clientId) {
			this.clientId = clientId;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
}
