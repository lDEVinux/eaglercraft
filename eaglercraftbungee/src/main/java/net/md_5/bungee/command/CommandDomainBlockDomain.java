package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.DomainBlacklist;

public class CommandDomainBlockDomain extends Command {

	public CommandDomainBlockDomain() {
		super("block-domain-name", "bungeecord.command.eag.blockdomainname");
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if (p1.length < 1) {
			p0.sendMessage(ChatColor.RED + "Please follow this command by a domain");
			return;
		}
		DomainBlacklist.addLocal(p1[0]);
		p0.sendMessage(ChatColor.GREEN + "The domain '" + ChatColor.WHITE + p1[0] + ChatColor.GREEN + "' was added to the block list");
	}

}
