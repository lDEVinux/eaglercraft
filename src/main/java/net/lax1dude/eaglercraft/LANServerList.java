package net.lax1dude.eaglercraft;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket07LocalWorlds.LocalWorld;

public class LANServerList {
	
	private final List<LanServer> lanServersList = new LinkedList();
	private final Map<String,RelayWorldsQuery> lanServersQueryList = new LinkedHashMap();
	private final Set<String> deadURIs = new HashSet();
	
	private long lastRefresh = 0l;
	private int refreshCounter = 0;
	
	public void update() {
		long millis = System.currentTimeMillis();
		if(millis - lastRefresh > 10000l) {
			if(++refreshCounter < 10) {
				refresh();
			}else {
				lastRefresh = millis;
			}
		}else {
			Iterator<Entry<String,RelayWorldsQuery>> itr = lanServersQueryList.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<String,RelayWorldsQuery> etr = itr.next();
				String uri = etr.getKey();
				RelayWorldsQuery q = etr.getValue();
				if(!q.isQueryOpen()) {
					itr.remove();
					if(q.isQueryFailed()) {
						deadURIs.add(uri);
						Iterator<LanServer> itr2 = lanServersList.iterator();
						while(itr2.hasNext()) {
							if(itr2.next().lanServerRelay.address.equals(uri)) {
								itr2.remove();
							}
						}
					}else {
						RelayServer rl = IntegratedServer.relayManager.getByURI(uri);
						Iterator<LanServer> itr2 = lanServersList.iterator();
						while(itr2.hasNext()) {
							LanServer l = itr2.next();
							if(l.lanServerRelay.address.equals(uri)) {
								l.flagged = false;
							}
						}
						if(rl != null) {
							Iterator<LocalWorld> itr3 = q.getWorlds().iterator();
							yee: while(itr3.hasNext()) {
								LocalWorld l = itr3.next();
								itr2 = lanServersList.iterator();
								while(itr2.hasNext()) {
									LanServer l2 = itr2.next();
									if(l2.lanServerRelay.address.equals(uri) && l2.lanServerCode.equals(l.worldCode)) {
										l2.lanServerMotd = l.worldName;
										l2.flagged = true;
										continue yee;
									}
								}
								lanServersList.add(new LanServer(l.worldName, rl, l.worldCode));
							}
						}
						itr2 = lanServersList.iterator();
						while(itr2.hasNext()) {
							LanServer l = itr2.next();
							if(l.lanServerRelay.address.equals(uri)) {
								if(!l.flagged) {
									itr2.remove();
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void forceRefresh() {
		deadURIs.clear();
		refreshCounter = 0;
		refresh();
	}

	private void refresh() {
		lastRefresh = System.currentTimeMillis();
		for(int i = 0, l = IntegratedServer.relayManager.count(); i < l; ++i) {
			RelayServer srv = IntegratedServer.relayManager.get(i);
			if(!lanServersQueryList.containsKey(srv.address) && !deadURIs.contains(srv.address)) {
				lanServersQueryList.put(srv.address, EaglerAdapter.openRelayWorldsQuery(srv.address));
			}
		}
	}

	public LanServer getServer(int idx) {
		return lanServersList.get(idx);
	}

	public int countServers() {
		return lanServersList.size();
	}
	
	public class LanServer {
		
		private String lanServerMotd;
		private RelayServer lanServerRelay;
		private String lanServerCode;
		
		protected boolean flagged = true;
		
		protected LanServer(String lanServerMotd, RelayServer lanServerRelay, String lanServerCode) {
			this.lanServerMotd = lanServerMotd;
			this.lanServerRelay = lanServerRelay;
			this.lanServerCode = lanServerCode;
		}
		
		public String getLanServerMotd() {
			return lanServerMotd;
		}
		
		public RelayServer getLanServerRelay() {
			return lanServerRelay;
		}
		
		public String getLanServerCode() {
			return lanServerCode;
		}
		
	}
	
}
