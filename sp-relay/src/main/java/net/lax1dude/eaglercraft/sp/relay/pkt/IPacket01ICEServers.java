package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import net.lax1dude.eaglercraft.sp.relay.EaglerSPRelayConfigRelayList.RelayServer;
import net.lax1dude.eaglercraft.sp.relay.EaglerSPRelayConfigRelayList.RelayType;

public class IPacket01ICEServers extends IPacket {
	
	public final Collection<RelayServer> servers;
	
	public IPacket01ICEServers(Collection<RelayServer> servers) {
		this.servers = servers;
	}

	public void write(DataOutputStream output) throws IOException {
		int l = servers.size();
		output.writeShort(l);
		Iterator<RelayServer> itr = servers.iterator();
		while(itr.hasNext()) {
			RelayServer srv = itr.next();
			if(srv.type == RelayType.STUN) {
				output.write('S');
			}else if(srv.type == RelayType.TURN) {
				output.write('T');
			}else {
				throw new IOException("Unknown/Unsupported Relay Type: " + srv.type.name());
			}
			writeASCII16(output, srv.address);
			writeASCII8(output, srv.username);
			writeASCII8(output, srv.password);
		}
	}
	
}
