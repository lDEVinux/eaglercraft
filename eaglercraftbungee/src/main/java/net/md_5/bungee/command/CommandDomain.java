package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandDomain extends Command {

	public CommandDomain() {
		super("domain", "bungeecord.command.eag.domain");
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if (p1.length < 1) {
			p0.sendMessage(ChatColor.RED + "Please follow this command by a user name");
			return;
		}
		final ProxiedPlayer user = ProxyServer.getInstance().getPlayer(p1[0]);
		if (user == null) {
			p0.sendMessage(ChatColor.RED + "That user is not online");
		} else {
			Object o = user.getAttachment().get("origin");
			if(o != null) {
				p0.sendMessage(ChatColor.BLUE + "Domain of " + p1[0] + " is " + o);
				if(p0.hasPermission("bungeecord.command.eag.blockdomain")) {
					p0.sendMessage(ChatColor.BLUE + "Type " + ChatColor.WHITE + "/block-domain " + p1[0] + ChatColor.BLUE + " to block this person");
				}
			}else {
				p0.sendMessage(ChatColor.RED + "Domain of " + p1[0] + " is unknown");
			}
		}
	}

}
