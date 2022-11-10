package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.DomainBlacklist;

public class CommandDomainBlock extends Command {

	public CommandDomainBlock() {
		super("block-domain", "bungeecord.command.eag.blockdomain");
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if (p1.length < 1) {
			p0.sendMessage(ChatColor.RED + "Please follow this command by a username");
			return;
		}
		final ProxiedPlayer user = ProxyServer.getInstance().getPlayer(p1[0]);
		if (user == null) {
			p0.sendMessage(ChatColor.RED + "That user is not online");
		}else {
			Object o = user.getAttachment().get("origin");
			if(o != null) {
				DomainBlacklist.addLocal((String)o);
				p0.sendMessage(ChatColor.RED + "Domain of " + ChatColor.WHITE + p1[0] + ChatColor.RED + " is " + ChatColor.WHITE + o);
				p0.sendMessage(ChatColor.RED + "It was added to the local block list.");
				user.disconnect("client blocked");
			}else {
				p0.sendMessage(ChatColor.RED + "Domain of " + p1[0] + " is unknown");
			}
		}
	}

}
