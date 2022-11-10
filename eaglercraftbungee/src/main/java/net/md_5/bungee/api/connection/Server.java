// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.connection;

import net.md_5.bungee.api.config.ServerInfo;

public interface Server extends Connection {
	ServerInfo getInfo();

	void sendData(final String p0, final byte[] p1);
}
