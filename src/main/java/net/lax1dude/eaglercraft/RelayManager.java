package net.lax1dude.eaglercraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket00Handshake;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacketFFErrorCode;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

public class RelayManager {
	
	private final List<RelayServer> relays = new ArrayList();
	private long lastPingThrough = 0l;
	
	public void load(NBTTagList relayConfig) {
		relays.clear();
		if(relayConfig != null && relayConfig.tagCount() > 0) {
			boolean gotAPrimary = false;
			for(int i = 0, l = relayConfig.tagCount(); i < l; ++i) {
				NBTBase relay = relayConfig.tagAt(i);
				if(relay instanceof NBTTagCompound) {
					NBTTagCompound relayee = (NBTTagCompound) relay;
					boolean p = relayee.getBoolean("primary");
					if(p) {
						if(gotAPrimary) {
							p = false;
						}else {
							gotAPrimary = true;
						}
					}
					relays.add(new RelayServer(relayee.getString("addr"), relayee.getString("comment"), p));
				}
			}
		}
		if(relays.size() == 0) {
			for(int i = 0, l = ConfigConstants.relays.size(); i < l; ++i) {
				relays.add(new RelayServer(ConfigConstants.relays.get(i)));
			}
		}
		sort();
	}
	
	public void save() {
		NBTTagList lst = new NBTTagList();
		for(int i = 0, l = relays.size(); i < l; ++i) {
			RelayServer srv = relays.get(i);
			NBTTagCompound etr = new NBTTagCompound();
			etr.setString("addr", srv.address);
			etr.setString("comment", srv.comment);
			etr.setBoolean("primary", srv.isPrimary());
			lst.appendTag(etr);
		}
		LocalStorageManager.gameSettingsStorage.setTag("relays", lst);
		LocalStorageManager.saveStorageG();
	}
	
	private void sort() {
		if(relays.size() == 0) {
			return;
		}
		int j = -1;
		for(int i = 0, l = relays.size(); i < l; ++i) {
			if(relays.get(i).isPrimary()) {
				if(j == -1) {
					j = i;
				}else {
					relays.get(i).setPrimary(false);
				}
			}
		}
		if(j == -1) {
			boolean found = false;
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(srv.getPing() > 0l) {
					found = true;
					srv.setPrimary(true);
					break;
				}
			}
			if(!found) {
				relays.get(0).setPrimary(true);
			}
		}else {
			RelayServer srv = relays.remove(j);
			relays.add(0, srv);
		}
	}
	
	public void ping() {
		lastPingThrough = System.currentTimeMillis();
		for(int i = 0, l = relays.size(); i < l; ++i) {
			relays.get(i).ping();
		}
	}
	
	public void update() {
		for(int i = 0, l = relays.size(); i < l; ++i) {
			relays.get(i).update();
		}
	}
	
	public void close() {
		for(int i = 0, l = relays.size(); i < l; ++i) {
			relays.get(i).close();
		}
	}
	
	public int count() {
		return relays.size();
	}
	
	public RelayServer get(int idx) {
		return relays.get(idx);
	}
	
	public void add(String addr, String comment, boolean primary) {
		lastPingThrough = 0l;
		int i = relays.size();
		relays.add(new RelayServer(addr, comment, false));
		if(primary) {
			setPrimary0(i);
		}
		save();
	}
	
	public void addNew(String addr, String comment, boolean primary) {
		lastPingThrough = 0l;
		int i = relays.size();
		int j = primary || i == 0 ? 0 : 1;
		RelayServer newServer = new RelayServer(addr, comment, false);
		relays.add(j, newServer);
		newServer.ping();
		if(primary) {
			setPrimary0(j);
		}
		save();
	}
	
	public void setPrimary(int idx) {
		setPrimary0(idx);
		save();
	}

	private void setPrimary0(int idx) {
		if(idx >= 0 && idx < relays.size()) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(srv.isPrimary()) {
					srv.setPrimary(false);
				}
			}
			RelayServer pr = relays.remove(idx);
			pr.setPrimary(true);
			relays.add(0, pr);
		}
	}
	
	public void remove(int idx) {
		RelayServer srv = relays.remove(idx);
		srv.close();
		sort();
		save();
	}
	
	public RelayServer getPrimary() {
		if(relays.size() > 0) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(srv.isPrimary()) {
					return srv;
				}
			}
			sort();
			save();
			return getPrimary();
		}else {
			return null;
		}
	}
	
	public RelayServerSocket connectHandshake(RelayServer relay, int type, String code) {
		RelayServerSocket sock = relay.openSocket();
		while(!sock.isClosed()) {
			if(sock.isOpen()) {
				sock.writePacket(new IPacket00Handshake(type, IntegratedServer.preferredRelayVersion, code));
				while(!sock.isClosed()) {
					IPacket pkt = sock.nextPacket();
					if(pkt != null) {
						if(pkt instanceof IPacket00Handshake) {
							return sock;
						}else if(pkt instanceof IPacketFFErrorCode) {
							IPacketFFErrorCode ipkt = (IPacketFFErrorCode) pkt;
							System.err.println("Relay [" + relay.address + "] failed: " + IPacketFFErrorCode.code2string(ipkt.code) +
									"(" + ipkt.code + "): " + ipkt.desc);
							Throwable t;
							while((t = sock.getException()) != null) {
								t.printStackTrace();
							}
							sock.close();
							return null;
						}else {
							System.err.println("Relay [" + relay.address + "] unexpected packet: " + pkt.getClass().getSimpleName());
							sock.close();
							return null;
						}
					}
					try {
						Thread.sleep(20l);
					} catch (InterruptedException e) {
					}
				}
			}
			try {
				Thread.sleep(20l);
			} catch (InterruptedException e) {
			}
		}
		System.err.println("Relay [" + relay.address + "] connection failed!");
		Throwable t;
		while((t = sock.getException()) != null) {
			t.printStackTrace();
		}
		return null;
	}
	
	private final List<RelayServer> brokenServers = new LinkedList();

	public RelayServerSocket getWorkingRelay(Consumer<String> progressCallback, int type, String code) {
		brokenServers.clear();
		if(relays.size() > 0) {
			long millis = System.currentTimeMillis();
			if(millis - lastPingThrough < 10000l) {
				RelayServer relay = getPrimary();
				if(relay.getPing() > 0l && relay.getPingCompatible().isCompatible()) {
					progressCallback.accept(relay.address);
					RelayServerSocket sock = connectHandshake(relay, type, code);
					if(sock != null) {
						if(!sock.isFailed()) {
							return sock;
						}
					}else {
						brokenServers.add(relay);
					}
				}
				for(int i = 0, l = relays.size(); i < l; ++i) {
					RelayServer relayEtr = relays.get(i);
					if(relayEtr != relay) {
						if(relayEtr.getPing() > 0l && relayEtr.getPingCompatible().isCompatible()) {
							progressCallback.accept(relayEtr.address);
							RelayServerSocket sock = connectHandshake(relayEtr, type, code);
							if(sock != null) {
								if(!sock.isFailed()) {
									return sock;
								}
							}else {
								brokenServers.add(relayEtr);
							}
						}
					}
				}
			}
			return getWorkingCodeRelayActive(progressCallback, type, code);
		}else {
			return null;
		}
	}
	
	private RelayServerSocket getWorkingCodeRelayActive(Consumer<String> progressCallback, int type, String code) {
		if(relays.size() > 0) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(!brokenServers.contains(srv)) {
					progressCallback.accept(srv.address);
					RelayServerSocket sock = connectHandshake(srv, type, code);
					if(sock != null) {
						if(!sock.isFailed()) {
							return sock;
						}
					}else {
						brokenServers.add(srv);
					}
				}
			}
			return null;
		}else {
			return null;
		}
	}
	
	public void loadDefaults() {
		int setPrimary = relays.size();
		eee: for(RelayEntry etr : ConfigConstants.relays) {
			for(RelayServer exEtr : relays) {
				if(exEtr.address.equalsIgnoreCase(etr.address)) {
					continue eee;
				}
			}
			relays.add(new RelayServer(etr));
		}
		setPrimary(setPrimary);
	}
	
	public String makeNewRelayName() {
		String str = "Relay Server #" + (relays.size() + 1);
		for(int i = relays.size() + 2, l = relays.size() + 50; i < l; ++i) {
			if(str.equalsIgnoreCase("Relay Server #" + i)) {
				str = "Relay Server #" + (i + 1);
			}
		}
		eee: while(true) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				if(str.equalsIgnoreCase(relays.get(i).comment)) {
					str = str + "_";
					continue eee;
				}
			}
			break;
		}
		return str;
	}
	
	public RelayServer getByURI(String uri) {
		Iterator<RelayServer> itr = relays.iterator();
		while(itr.hasNext()) {
			RelayServer rl = itr.next();
			if(rl.address.equals(uri)) {
				return rl;
			}
		}
		return null;
	}
	
}
