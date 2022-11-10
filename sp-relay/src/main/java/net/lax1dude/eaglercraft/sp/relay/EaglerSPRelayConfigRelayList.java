package net.lax1dude.eaglercraft.sp.relay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class EaglerSPRelayConfigRelayList {
	
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
		
	}
	
	public static final Collection<RelayServer> relayServers = new ArrayList();
	
	public static void loadRelays(File list) throws IOException {
		ArrayList<RelayServer> loading = new ArrayList();
		
		if(!list.isFile()) {
			EaglerSPRelay.logger.info("Creating new {}...", list.getName());
			try(InputStream is = EaglerSPRelayConfigRelayList.class.getResourceAsStream("/relays.txt");
					FileOutputStream os = new FileOutputStream(list)) {
				byte[] buffer = new byte[4096];
				int i;
				while((i = is.read(buffer)) != -1) {
					os.write(buffer, 0, i);
				}
			}
		}

		EaglerSPRelay.logger.info("Loading STUN/TURN relays from: {}", list.getName());
		
		RelayType addType = null;
		String addAddress = null;
		String addUsername = null;
		String addPassword = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(list))) {
			String line;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.length() == 0) {
					continue;
				}
				boolean isSTUNHead = line.equals("[STUN]");
				boolean isTURNHead = line.equals("[TURN]");
				if(isSTUNHead || isTURNHead) {
					if(addType != null) {
						add(list.getName(), loading, addType, addAddress, addUsername, addPassword);
					}
					addAddress = null;
					addUsername = null;
					addPassword = null;
					addType = null;
				}
				if(isSTUNHead) {
					addType = RelayType.STUN;
				}else if(isTURNHead) {
					addType = RelayType.TURN;
				}else if(line.startsWith("url")) {
					int spidx = line.indexOf('=') + 1;
					if(spidx < 3) {
						EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
					}else {
						line = line.substring(spidx).trim();
						if(line.length() < 1) {
							EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
						}else {
							addAddress = line;
						}
					}
				}else if(line.startsWith("username")) {
					int spidx = line.indexOf('=') + 1;
					if(spidx < 8) {
						EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
					}else {
						line = line.substring(spidx).trim();
						if(line.length() < 1) {
							EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
						}else {
							addUsername = line;
						}
					}
				}else if(line.startsWith("password")) {
					int spidx = line.indexOf('=') + 1;
					if(spidx < 8) {
						EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
					}else {
						line = line.substring(spidx).trim();
						if(line.length() < 1) {
							EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
						}else {
							addPassword = line;
						}
					}
				}else {
					EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
				}
			}
		}
		
		if(addType != null) {
			add(list.getName(), loading, addType, addAddress, addUsername, addPassword);
		}
		
		if(loading.size() == 0) {
			throw new IOException(list.getName() + ": no servers loaded");
		}else {
			relayServers.clear();
			relayServers.addAll(loading);
		}
		
	}
	
	private static void add(String filename, Collection<RelayServer> loading, RelayType type, String url, String user, String pass) {
		if(url == null) {
			EaglerSPRelay.logger.error("Error: Invalid relay in {}, missing 'url'", filename);
		}else {
			loading.add(new RelayServer(type, url, user, pass));
		}
	}
	
}
