package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IPacket07LocalWorlds extends IPacket {
	
	public static class LocalWorld {
		
		public final String worldName;
		public final String worldCode;
		
		public LocalWorld(String worldName, String worldCode) {
			this.worldName = worldName;
			this.worldCode = worldCode;
		}
		
	}
	
	public final List<LocalWorld> worldsList;
	
	public IPacket07LocalWorlds() {
		this.worldsList = new ArrayList();
	}

	public void read(DataInputStream input) throws IOException {
		int l = input.read();
		for(int i = 0; i < l; ++i) {
			worldsList.add(new LocalWorld(readASCII8(input), readASCII8(input)));
		}
	}
	
}
