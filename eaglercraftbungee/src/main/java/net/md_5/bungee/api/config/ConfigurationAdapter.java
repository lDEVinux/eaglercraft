// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.config;

import java.util.Collection;
import java.util.Map;

public interface ConfigurationAdapter {
	void load();

	int getInt(final String p0, final int p1);

	String getString(final String p0, final String p1);

	boolean getBoolean(final String p0, final boolean p1);

	Map<String, ServerInfo> getServers();

	Collection<ListenerInfo> getListeners();

	Collection<String> getGroups(final String p0);

	Collection<String> getPermissions(final String p0);

	Collection<String> getBlacklistURLs();
	
	Collection<String> getBlacklistSimpleWhitelist();

	Collection<String> getDisabledCommands();
	
	Collection<String> getICEServers();
	
	AuthServiceInfo getAuthSettings();

	Map<String, Object> getMap();
	
	void forceSave();
}
