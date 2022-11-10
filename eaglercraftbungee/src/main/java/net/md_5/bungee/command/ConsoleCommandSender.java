// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class ConsoleCommandSender implements CommandSender {
	private static final ConsoleCommandSender instance;
	private static final Map<String, Object> attachment = new WeakHashMap();

	private ConsoleCommandSender() {
	}

	@Override
	public void sendMessage(final String message) {
		ProxyServer.getInstance().getLogger().info(message);
	}

	@Override
	public void sendMessages(final String... messages) {
		for (final String message : messages) {
			this.sendMessage(message);
		}
	}

	@Override
	public String getName() {
		return "CONSOLE";
	}

	@Override
	public Collection<String> getGroups() {
		return (Collection<String>) new HashSet();
	}

	@Override
	public void addGroups(final String... groups) {
		throw new UnsupportedOperationException("Console may not have groups");
	}

	@Override
	public void removeGroups(final String... groups) {
		throw new UnsupportedOperationException("Console may not have groups");
	}

	@Override
	public boolean hasPermission(final String permission) {
		return true;
	}

	@Override
	public void setPermission(final String permission, final boolean value) {
		throw new UnsupportedOperationException("Console has all permissions");
	}

	public static ConsoleCommandSender getInstance() {
		return ConsoleCommandSender.instance;
	}

	static {
		instance = new ConsoleCommandSender();
	}

	@Override
	public Map<String, Object> getAttachment() {
		return attachment;
	}
}
