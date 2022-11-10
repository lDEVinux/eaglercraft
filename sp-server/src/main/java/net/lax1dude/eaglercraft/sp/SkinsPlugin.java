package net.lax1dude.eaglercraft.sp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet250CustomPayload;

public class SkinsPlugin {
	
	private static final HashMap<String,byte[]> skinCollection = new HashMap();
	private static final HashMap<String,byte[]> capeCollection = new HashMap();
	private static final HashMap<String,Long> lastSkinLayerUpdate = new HashMap();

	private static final int[] SKIN_DATA_SIZE = new int[] { 64*32*4, 64*64*4, -9, -9, 1, 64*64*4, -9 }; // 128 pixel skins crash clients
	private static final int[] CAPE_DATA_SIZE = new int[] { 32*32*4, -9, 1 };
	
	public static boolean handleMessage(EntityPlayerMP player, Packet250CustomPayload payload) {
		if(payload.data.length > 0) {
			String user = player.username;
			byte[] msg = payload.data;
			try {
				if("EAG|MySkin".equals(payload.channel)) {
					if(!skinCollection.containsKey(user)) {
						int t = (int)msg[0] & 0xFF;
						if(t < SKIN_DATA_SIZE.length && msg.length == (SKIN_DATA_SIZE[t] + 1)) {
							skinCollection.put(user, msg);
						}
					}
					return true;
				}
				if("EAG|MyCape".equals(payload.channel)) {
					if(!capeCollection.containsKey(user)) {
						int t = (int)msg[0] & 0xFF;
						if(t < CAPE_DATA_SIZE.length && msg.length == (CAPE_DATA_SIZE[t] + 2)) {
							capeCollection.put(user, msg);
						}
					}
					return true;
				}
				if("EAG|FetchSkin".equals(payload.channel)) {
					if(msg.length > 2) {
						String fetch = new String(msg, 2, msg.length - 2, StandardCharsets.UTF_8);
						byte[] data;
						if((data = skinCollection.get(fetch)) != null) {
							byte[] conc = new byte[data.length + 2];
							conc[0] = msg[0]; conc[1] = msg[1]; //synchronization cookie
							System.arraycopy(data, 0, conc, 2, data.length);
							if((data = capeCollection.get(fetch)) != null) {
								byte[] conc2 = new byte[conc.length + data.length];
								System.arraycopy(conc, 0, conc2, 0, conc.length);
								System.arraycopy(data, 0, conc2, conc.length, data.length);
								conc = conc2;
							}
							player.playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|UserSkin", conc));
						}
					}
					return true;
				}
				if("EAG|SkinLayers".equals(payload.channel)) {
					long millis = System.currentTimeMillis();
					Long lsu = lastSkinLayerUpdate.get(user);
					if(lsu != null && millis - lsu < 700L) { // DoS protection
						return true;
					}
					lastSkinLayerUpdate.put(user, millis);
					byte[] data;
					if((data = capeCollection.get(user)) != null) {
						data[1] = msg[0];
					}else {
						data = new byte[] { (byte)2, msg[0], (byte)0 };
						capeCollection.put(user, data);
					}
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					DataOutputStream dd = new DataOutputStream(bao);
					dd.write(msg[0]);
					dd.writeUTF(user);
					byte[] bpacket = bao.toByteArray();
					for(Object o : player.mcServer.getConfigurationManager().playerEntityList) {
						EntityPlayerMP pl = (EntityPlayerMP) o;
						if(!pl.username.equals(user)) {
							pl.playerNetServerHandler.sendPacket(new Packet250CustomPayload("EAG|SkinLayers", bpacket));
						}
					}
					return true;
				}
			}catch(Throwable t) {
				// hacker
			}
		}
		return false;
	}
	
	public static void handleDisconnect(EntityPlayerMP player) {
		skinCollection.remove(player.username);
		capeCollection.remove(player.username);
		lastSkinLayerUpdate.remove(player.username);
	}

	public static void reset() {
		skinCollection.clear();
		capeCollection.clear();
		lastSkinLayerUpdate.clear();
	}
	
}
