package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class IPacket01ICEServers extends IPacket {
	
	public final Collection<ICEServerSet.RelayServer> servers;
	
	public IPacket01ICEServers() {
		servers = new ArrayList();
	}
	
	public void read(DataInputStream input) throws IOException {
		servers.clear();
		int l = input.readUnsignedShort();
		for(int i = 0; i < l; ++i) {
			char type = (char)input.read();
			ICEServerSet.RelayType typeEnum;
			if(type == 'S') {
				typeEnum = ICEServerSet.RelayType.STUN;
			}else if(type == 'T') {
				typeEnum = ICEServerSet.RelayType.TURN;
			}else {
				throw new IOException("Unknown/Unsupported Relay Type: '" + type + "'");
			}
			servers.add(new ICEServerSet.RelayServer(
					typeEnum,
					readASCII16(input),
					readASCII8(input),
					readASCII8(input)
			));
		}
	}
	
}
