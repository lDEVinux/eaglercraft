package net.lax1dude.eaglercraft.sp.relay.pkt;

import java.io.DataOutputStream;
import java.io.IOException;
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
	
	public IPacket07LocalWorlds(List<LocalWorld> worldsList) {
		this.worldsList = worldsList;
	}

	public void write(DataOutputStream output) throws IOException {
		if(worldsList == null) {
			output.write(0);
		}else {
			int i = worldsList.size();
			if(i > 255) {
				i = 255;
			}
			output.write(i);
			for(int j = 0; j < i; ++j) {
				LocalWorld w = worldsList.get(j);
				writeASCII8(output, w.worldName);
				writeASCII8(output, w.worldCode);
			}
		}
	}
	
}
