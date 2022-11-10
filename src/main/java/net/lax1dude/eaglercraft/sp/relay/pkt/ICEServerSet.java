package net.lax1dude.eaglercraft.sp.relay.pkt;

public class ICEServerSet {

	public static enum RelayType {
		STUN, TURN;
	}

	public static class RelayServer {
		
		public final RelayType type;
		public final String address;
		public final String username;
		public final String password;
		
		protected RelayServer(RelayType type, String address, String username, String password) {
			this.type = type;
			this.address = address;
			this.username = username;
			this.password = password;
		}
		
		protected RelayServer(RelayType type, String address) {
			this.type = type;
			this.address = address;
			this.username = null;
			this.password = null;
		}

		public String getICEString() {
			if(username == null) {
				return address;
			}else {
				return address + ";" + username + ";" + password;
			}
		}
		
	}
	
}
