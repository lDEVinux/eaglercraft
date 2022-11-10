// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class CommandPerms extends Command {
	public CommandPerms() {
		super("perms");
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		final StringBuilder groups = new StringBuilder();
		final Set<String> permissions = new HashSet<String>();
		for (final String group : sender.getGroups()) {
			groups.append(group);
			groups.append(", ");
			permissions.addAll(ProxyServer.getInstance().getConfigurationAdapter().getPermissions(group));
		}
		sender.sendMessage(ChatColor.GOLD + "You have the following groups: " + groups.substring(0, groups.length() - 2));
		for (final String permission : permissions) {
			sender.sendMessage(ChatColor.BLUE + "- " + permission);
		}
	}
}
