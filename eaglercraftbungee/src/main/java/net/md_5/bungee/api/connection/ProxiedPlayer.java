// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.connection;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.config.TexturePackInfo;
import net.md_5.bungee.api.tab.TabListHandler;

public interface ProxiedPlayer extends Connection, CommandSender {
	String getDisplayName();

	void setDisplayName(final String p0);

	void connect(final ServerInfo p0);

	Server getServer();

	int getPing();

	void sendData(final String p0, final byte[] p1);

	PendingConnection getPendingConnection();

	void chat(final String p0);

	void setTexturePack(final TexturePackInfo p0);

	void setTabList(final TabListHandler p0);

	TabListHandler getTabList();
}
