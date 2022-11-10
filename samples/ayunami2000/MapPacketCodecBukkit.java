package ayunami2000;

import java.util.List;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_5_R3.Packet;
import net.minecraft.server.v1_5_R3.Packet131ItemData;

public class MapPacketCodecBukkit extends MapPacketCodec {

	public MapPacketCodecBukkit(int mapId) {
		super(mapId);
	}
	
	public Object getNextBukkitPacket() {
		byte[] pkt = getNextPacket();
		if(pkt == null) {
			return null;
		}
		return new Packet131ItemData((short)103, (short)mapId, pkt);
	}
	
	public Object getDisableBukkitPacket() {
		byte[] pkt = getDisablePacket();
		if(pkt == null) {
			return null;
		}
		return new Packet131ItemData((short)103, (short)mapId, pkt);
	}
	
	public void sendNextPacketToPlayer(Player p) {
		nativeSendPacketToPlayer(p, getNextBukkitPacket());
	}
	
	public void sendDisablePacketToPlayer(Player p) {
		nativeSendPacketToPlayer(p, getDisableBukkitPacket());
	}
	
	public void sendNextPacketToPlayers(Player... p) {
		Object pkt = getNextBukkitPacket();
		for(Player pl : p) {
			nativeSendPacketToPlayer(pl, pkt);
		}
	}
	
	public void sendDisablePacketToPlayers(Player... p) {
		Object pkt = getDisableBukkitPacket();
		for(Player pl : p) {
			nativeSendPacketToPlayer(pl, pkt);
		}
	}
	
	public void sendNextPacketToPlayers(List<Player> p) {
		Object pkt = getNextBukkitPacket();
		for(Player pl : p) {
			nativeSendPacketToPlayer(pl, pkt);
		}
	}
	
	public void sendDisablePacketToPlayers(List<Player> p) {
		Object pkt = getDisableBukkitPacket();
		for(Player pl : p) {
			nativeSendPacketToPlayer(pl, pkt);
		}
	}
	
	public static void nativeSendPacketToPlayer(Player player, Object obj) {
		if(obj == null) {
			return;
		}
		((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)obj);
	}
	
}
