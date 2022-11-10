package net.minecraft.src;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.ServerQuery;
import net.lax1dude.eaglercraft.ServerQuery.QueryResponse;
import net.minecraft.client.Minecraft;

public class ServerData {
	public String serverName;
	public String serverIP;
	private final int id;
	
	private static int idCounter = 0;

	/**
	 * the string indicating number of players on and capacity of the server that is
	 * shown on the server browser (i.e. "5/20" meaning 5 slots used out of 20 slots
	 * total)
	 */
	public String populationInfo;

	/**
	 * (better variable name would be 'hostname') server name as displayed in the
	 * server browser's second line (grey text)
	 */
	public String serverMOTD;

	/** last server ping that showed up in the server browser */
	public long pingToServer;
	public long pingSentTime;
	public int field_82821_f = 61;

	/** Game version for this server. */
	public String gameVersion = "1.5.2";
	public boolean hasPing = false;
	private boolean field_78842_g = true;
	private boolean acceptsTextures = false;
	public ServerQuery currentQuery = null;
	public int[] serverIcon = null;
	public boolean serverIconDirty = false;
	public boolean serverIconEnabled = false;
	public boolean hasError = false;
	public List<String> playerList = new ArrayList();
	public int serverIconGL = -1;
	public final boolean isDefault;

	/** Whether to hide the IP address for this server. */
	private boolean hideAddress = false;

	public ServerData(String par1Str, String par2Str, boolean isDefault) {
		this.serverName = par1Str;
		this.serverIP = par2Str;
		this.isDefault = isDefault;
		this.id = ++idCounter;
	}
	
	public int hashCode() {
		return id;
	}
	
	public boolean equals(Object o) {
		return o instanceof ServerData && id == ((ServerData)o).id;
	}

	/**
	 * Returns an NBTTagCompound with the server's name, IP and maybe
	 * acceptTextures.
	 */
	public NBTTagCompound getNBTCompound() {
		NBTTagCompound var1 = new NBTTagCompound();
		var1.setString("name", this.serverName);
		var1.setString("ip", this.serverIP);
		var1.setBoolean("hideAddress", this.hideAddress);

		return var1;
	}

	public boolean getAcceptsTextures() {
		return this.acceptsTextures;
	}

	public boolean func_78840_c() {
		return this.field_78842_g;
	}

	public void setAcceptsTextures(boolean par1) {
		this.acceptsTextures = par1;
		this.field_78842_g = false;
	}

	public boolean isHidingAddress() {
		return this.hideAddress;
	}

	public void setHideAddress(boolean par1) {
		this.hideAddress = par1;
	}

	/**
	 * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a ServerData
	 * instance.
	 */
	public static ServerData getServerDataFromNBTCompound(NBTTagCompound par0NBTTagCompound) {
		ServerData var1 = new ServerData(par0NBTTagCompound.getString("name"), par0NBTTagCompound.getString("ip"), par0NBTTagCompound.getBoolean("default"));
		var1.hideAddress = par0NBTTagCompound.getBoolean("hideAddress");
		return var1;
	}

	public void setMOTDFromQuery(QueryResponse pkt) {
		JSONObject motdData = pkt.getResponseJSON();
		JSONArray motd = motdData.getJSONArray("motd");
		this.serverMOTD = motd.length() > 0 ? (motd.length() > 1 ? motd.getString(0) + "\n" + motd.getString(1) : motd.getString(0)) : "";
		this.populationInfo = "" + motdData.getInt("online") + "/" + motdData.getInt("max");
		this.playerList.clear();
		JSONArray players = motdData.getJSONArray("players");
		for(int i = 0, l = players.length(); i < l; ++i) {
			this.playerList.add(players.getString(i));
		}
		serverIconEnabled = motdData.getBoolean("icon");
		if(!serverIconEnabled) {
			if(serverIconGL != -1) {
				EaglerAdapter.glDeleteTextures(serverIconGL);
				serverIconGL = -1;
			}
		}
		hasError = false;
	}
	
	public void setRateLimitError(boolean lock, boolean isTcp) {
		if(lock) {
			serverMOTD = EnumChatFormatting.RED + "Your IP is banned for DoS\n" + EnumChatFormatting.GRAY + "Try again in an hour";
		}else {
			if(isTcp) {
				serverMOTD = EnumChatFormatting.RED + "Connection Blocked\n" + EnumChatFormatting.GRAY + "Try again in a minute";
			}else {
				serverMOTD = EnumChatFormatting.RED + "Query Was Blocked\n" + EnumChatFormatting.GRAY + "Try again in a minute";
			}
		}
		this.populationInfo = "";
		this.playerList.clear();
		this.serverIconEnabled = false;
		this.hasError = true;
	}
	
	public void refreshIcon() {
		if(serverIconEnabled) {
			if(serverIconDirty && serverIcon != null) {
				if(serverIconGL == -1) {
					serverIconGL = EaglerAdapter.glGenTextures();
					Minecraft.getMinecraft().renderEngine.bindTexture(serverIconGL);
					EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_REPEAT);
					EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_REPEAT);
					EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
					EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
				}else {
					Minecraft.getMinecraft().renderEngine.bindTexture(serverIconGL);
				}
				IntBuffer buf = GLAllocation.createDirectIntBuffer(4096);
				buf.put(serverIcon);
				buf.flip();
				EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 0, EaglerAdapter.GL_RGBA, 64, 64, 0, EaglerAdapter.GL_BGRA, EaglerAdapter.GL_UNSIGNED_INT_8_8_8_8_REV, buf);
				serverIconDirty = false;
			}
		}else {
			if(serverIconGL != -1) {
				EaglerAdapter.glDeleteTextures(serverIconGL);
				serverIconGL = -1;
				serverIconDirty = false;
			}
		}
	}
	
	public void freeIcon() {
		if(serverIconGL != -1) {
			EaglerAdapter.glDeleteTextures(serverIconGL);
			serverIconGL = -1;
		}
		serverIconDirty = false;
		serverIconEnabled = false;
	}
	
}
