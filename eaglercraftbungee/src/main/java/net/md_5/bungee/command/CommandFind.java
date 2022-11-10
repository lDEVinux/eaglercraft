// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandFind extends Command {
	public CommandFind() {
		super("find", "bungeecord.command.find", new String[0]);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Please follow this command by a user name");
		} else {
			final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
			if (player == null || player.getServer() == null || player.getServer().getInfo() == null) {
				sender.sendMessage(ChatColor.RED + "That user is not online");
			} else {
				sender.sendMessage(ChatColor.BLUE + args[0] + " is online at " + player.getServer().getInfo().getName());
			}
		}
	}
}
