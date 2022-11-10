package net.md_5.bungee.api;

import java.util.List;

public interface MOTD extends QueryConnection {

	public void sendToUser();
	
	public String getLine1();
	public String getLine2();
	public List<String> getPlayerList();
	public int[] getBitmap();
	public int getOnlinePlayers();
	public int getMaxPlayers();
	public String getSubType();
	
	public void setLine1(String p);
	public void setLine2(String p);
	public void setPlayerList(List<String> p);
	public void setPlayerList(String... p);
	public void setBitmap(int[] p);
	public void setOnlinePlayers(int i);
	public void setMaxPlayers(int i);
	
}
