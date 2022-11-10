package net.md_5.bungee.eaglercraft;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.MOTD;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.MOTDCacheConfiguration;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MOTDConnectionImpl extends QueryConnectionImpl implements MOTD {

	private String line1;
	private String line2;
	private List<String> players;
	private int[] bitmap;
	private int onlinePlayers;
	private int maxPlayers;
	private boolean hasIcon;
	private boolean iconDirty;
	private String subType;
	
	public MOTDConnectionImpl(ListenerInfo listener, InetAddress addr, WebSocket socket, String arg1) {
		super(listener, addr, socket, "motd");
		String[] lns = listener.getMotd().split("\n");
		if(lns.length >= 1) {
			line1 = lns[0];
		}
		if(lns.length >= 2) {
			line2 = lns[1];
		}
		maxPlayers = listener.getMaxPlayers();
		onlinePlayers = BungeeCord.getInstance().getOnlineCount();
		players = new ArrayList();
		for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers()) {
			players.add(pp.getDisplayName());
			if(players.size() >= 9) {
				players.add("" + ChatColor.GRAY + ChatColor.ITALIC + "(" + (onlinePlayers - players.size()) + " more)");
				break;
			}
		}
		bitmap = new int[4096];
		setReturnType("motd");
		int i = arg1.indexOf('.');
		if(i > 0) {
			subType = arg1.substring(i + 1);
			if(subType.length() == 0) {
				subType = "motd";
			}
		}else {
			subType = "motd";
		}
		if(!subType.startsWith("noicon") && !subType.startsWith("cache.noicon")) {
			iconDirty = hasIcon = listener.isIconSet();
			if(hasIcon) {
				System.arraycopy(listener.getServerIconCache(), 0, bitmap, 0, 4096);
			}
		}
	}

	@Override
	public String getLine1() {
		return line1;
	}

	@Override
	public String getLine2() {
		return line2;
	}

	@Override
	public List<String> getPlayerList() {
		return players;
	}

	@Override
	public int[] getBitmap() {
		return bitmap;
	}

	@Override
	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	@Override
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	@Override
	public String getSubType() {
		return subType;
	}

	@Override
	public void setLine1(String p) {
		line1 = p;
	}

	@Override
	public void setLine2(String p) {
		line2 = p;
	}

	@Override
	public void setPlayerList(List<String> p) {
		players = p;
	}

	@Override
	public void setPlayerList(String... p) {
		players = Arrays.asList(p);
	}

	@Override
	public void setBitmap(int[] p) {
		iconDirty = hasIcon = true;
		bitmap = p;
	}

	@Override
	public void setOnlinePlayers(int i) {
		onlinePlayers = i;
	}

	@Override
	public void setMaxPlayers(int i) {
		maxPlayers = i;
	}

	@Override
	public void sendToUser() {
		if(!isClosed()) {
			JSONObject obj = new JSONObject();
			if(subType.startsWith("cache.anim")) {
				obj.put("unsupported", true);
				writeResponse(obj);
				close();
				return;
			}else if(subType.startsWith("cache")) {
				JSONArray cacheControl = new JSONArray();
				MOTDCacheConfiguration cc = listener.getCacheConfig();
				if(cc.cacheServerListAnimation) {
					cacheControl.put("animation");
				}
				if(cc.cacheServerListResults) {
					cacheControl.put("results");
				}
				if(cc.cacheServerListTrending) {
					cacheControl.put("trending");
				}
				if(cc.cacheServerListPortfolios) {
					cacheControl.put("portfolio");
				}
				obj.put("cache", cacheControl);
				obj.put("ttl", cc.cacheTTL);
			}else {
				MOTDCacheConfiguration cc = listener.getCacheConfig();
				obj.put("cache", cc.cacheServerListAnimation || cc.cacheServerListResults ||
						cc.cacheServerListTrending || cc.cacheServerListPortfolios);
			}
			boolean noIcon = subType.startsWith("noicon") || subType.startsWith("cache.noicon");
			JSONArray motd = new JSONArray();
			if(line1 != null && line1.length() > 0) motd.put(line1);
			if(line2 != null && line2.length() > 0) motd.put(line2);
			obj.put("motd", motd);
			obj.put("icon", hasIcon && !noIcon);
			obj.put("online", onlinePlayers);
			obj.put("max", maxPlayers);
			JSONArray playerz = new JSONArray();
			for(String s : players) {
				playerz.put(s);
			}
			obj.put("players", playerz);
			writeResponse(obj);
			if(hasIcon && !noIcon && iconDirty && bitmap != null) {
				byte[] iconPixels = new byte[16384];
				for(int i = 0; i < 4096; ++i) {
					iconPixels[i * 4] = (byte)((bitmap[i] >> 16) & 0xFF);
					iconPixels[i * 4 + 1] = (byte)((bitmap[i] >> 8) & 0xFF);
					iconPixels[i * 4 + 2] = (byte)(bitmap[i] & 0xFF);
					iconPixels[i * 4 + 3] = (byte)((bitmap[i] >> 24) & 0xFF);
				}
				writeResponseBinary(iconPixels);
				iconDirty = false;
			}
			if(subType.startsWith("cache")) {
				close();
			}
		}
	}

}
