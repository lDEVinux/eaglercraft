package net.md_5.bungee.api;

import java.net.InetAddress;

import org.json.JSONObject;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.eaglercraft.EaglercraftBungee;

public interface QueryConnection {
	
	public InetAddress getRemoteAddress();
	public ListenerInfo getListener();

	public String getAccept();
	public void setReturnType(String type);
	public String getReturnType();

	public int availableRequests();
	
	public default JSONObject readRequestData() {
		String s = readRequestString();
		return s == null ? null : new JSONObject(s);
	}
	
	public String readRequestString();
	public long getConnectionTimestamp();
	
	public default long getConnectionAge() {
		return System.currentTimeMillis() - getConnectionTimestamp();
	}

	public default void writeResponse(JSONObject msg) {
		JSONObject toSend = new JSONObject();
		toSend.put("type", getReturnType());
		toSend.put("name", BungeeCord.getInstance().config.getServerName());
		toSend.put("brand", EaglercraftBungee.brand);
		toSend.put("vers", EaglercraftBungee.version);
		toSend.put("cracked", EaglercraftBungee.cracked);
		toSend.put("secure", false);
		toSend.put("time", System.currentTimeMillis());
		toSend.put("uuid", BungeeCord.getInstance().config.getUuid());
		toSend.put("data", msg);
		writeResponseRaw(toSend.toString());
	}
	
	public default void writeResponse(String msg) {
		JSONObject toSend = new JSONObject();
		toSend.put("type", getReturnType());
		toSend.put("name", BungeeCord.getInstance().config.getServerName());
		toSend.put("brand", EaglercraftBungee.brand);
		toSend.put("vers", EaglercraftBungee.version);
		toSend.put("cracked", EaglercraftBungee.cracked);
		toSend.put("secure", false);
		toSend.put("time", System.currentTimeMillis());
		toSend.put("uuid", BungeeCord.getInstance().config.getUuid());
		toSend.put("data", msg);
		writeResponseRaw(toSend.toString());
	}
	
	public void writeResponseRaw(String msg);
	public void writeResponseBinary(byte[] blob);

	public void keepAlive(boolean yes);
	public boolean shouldKeepAlive();
	public boolean isClosed();
	public void close();
	
}
