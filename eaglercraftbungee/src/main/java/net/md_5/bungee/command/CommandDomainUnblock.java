package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.DomainBlacklist;

public class CommandDomainUnblock extends Command {

	public CommandDomainUnblock() {
		super("unblock-domain", "bungeecord.command.eag.unblockdomain", "unblock-domain-name");
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if (p1.length < 1) {
			p0.sendMessage(ChatColor.RED + "Please follow this command by a domain");
			return;
		}
		if(DomainBlacklist.removeLocal(p1[0])) {
			p0.sendMessage(ChatColor.GREEN + "The domain '" + p1[0] + "' was removed from the local block list");
		}else {
			p0.sendMessage(ChatColor.RED + "The domain was not removed, is it on the block list? Check '" + DomainBlacklist.localBlacklist.getName() + "' in your bungeecord directory");
		}
	}

}
