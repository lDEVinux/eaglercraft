// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api;

import java.util.Collection;
import java.util.Map;

public interface CommandSender {
	String getName();

	void sendMessage(final String p0);

	void sendMessages(final String... p0);

	Collection<String> getGroups();

	void addGroups(final String... p0);

	void removeGroups(final String... p0);

	boolean hasPermission(final String p0);

	void setPermission(final String p0, final boolean p1);
	
	Map<String, Object> getAttachment();
}
