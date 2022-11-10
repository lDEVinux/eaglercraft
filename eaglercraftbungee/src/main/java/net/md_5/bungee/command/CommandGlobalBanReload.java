package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;

public class CommandGlobalBanReload extends Command {

	public CommandGlobalBanReload(boolean replaceBukkit) {
		super(replaceBukkit ? "reloadban" : "eag-reloadban", "bungeecord.command.eag.reloadban", replaceBukkit ? new String[] { "eag-reloadban", "banreload", "eag-banreload", "e-reloadban",
				"e-banreload", "gbanreload", "greloadban"} : new String[] { "eag-banreload", "e-reloadban", "e-banreload", "gbanreload", "greloadban"});
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		BanList.maybeReloadBans(p0);
		p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.WHITE + "Ban list reloaded");
	}

}
