// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import java.net.InetAddress;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandIP extends Command {
	public CommandIP() {
		super("ip", "bungeecord.command.ip", new String[0]);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Please follow this command by a user name");
			return;
		}
		final ProxiedPlayer user = ProxyServer.getInstance().getPlayer(args[0]);
		if (user == null) {
			sender.sendMessage(ChatColor.RED + "That user is not online");
		} else {
			Object o = user.getAttachment().get("remoteAddr");
			if(o != null) {
				sender.sendMessage(ChatColor.BLUE + "IP of " + args[0] + " is " + (InetAddress)o);
			}else {
				sender.sendMessage(ChatColor.BLUE + "IP of " + args[0] + " is " + user.getAddress());
			}
		}
	}
}
