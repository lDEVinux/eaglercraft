package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;

public class CommandGlobalUnban extends Command {
	
	private final boolean replaceBukkit;

	public CommandGlobalUnban(boolean replaceBukkit) {
		super(replaceBukkit ? "unban" : "eag-unban", "bungeecord.command.eag.unban", replaceBukkit ? new String[] {"eag-unban", "e-unban", "gunban"} :new String[] {"e-unban", "gunban"});
		this.replaceBukkit = replaceBukkit;
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if(p1.length != 2 || (!p1[0].equalsIgnoreCase("user") && !p1[0].equalsIgnoreCase("username") && !p1[0].equalsIgnoreCase("player")
				&& !p1[0].equalsIgnoreCase("wildcard") && !p1[0].equalsIgnoreCase("regex") && !p1[0].equalsIgnoreCase("ip"))) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To unban a player, use: " + ChatColor.WHITE + "/" + (replaceBukkit?"":"eag-") + "unban user <player>");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To unban an ip/wildcard/regex, use: " + ChatColor.WHITE + "/" + (replaceBukkit?"":"eag-") + "unban <ip|wildcard|regex> <value>");
			return;
		}
		if(p1[0].equalsIgnoreCase("user") || p1[0].equalsIgnoreCase("username") || p1[0].equalsIgnoreCase("player")) {
			if(BanList.unban(p1[1])) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "User '" + ChatColor.WHITE + p1[1] + ChatColor.GREEN + "' was unbanned");
			}else {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "User '" + ChatColor.WHITE + p1[1] + ChatColor.RED + "' is not banned");
			}
		}else if(p1[0].equalsIgnoreCase("ip")) {
			try {
				if(BanList.unbanIP(p1[1])) {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "IP '" + ChatColor.WHITE + p1[1] + ChatColor.GREEN + "' was unbanned");
				}else {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "IP '" + ChatColor.WHITE + p1[1] + ChatColor.RED + "' is not banned");
				}
			}catch(Throwable t) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "IP address '" + ChatColor.WHITE + p1[1] + ChatColor.RED + "' is invalid: " + t.getMessage());
			}
		}else if(p1[0].equalsIgnoreCase("wildcard")) {
			if(BanList.unbanWildcard(p1[1])) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Wildcard '" + ChatColor.WHITE + p1[1] + ChatColor.GREEN + "' was unbanned");
			}else {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Wildcard '" + ChatColor.WHITE + p1[1] + ChatColor.RED + "' is not banned");
			}
		}else if(p1[0].equalsIgnoreCase("regex")) {
			if(BanList.unbanRegex(p1[1])) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Regex '" + ChatColor.WHITE + p1[1] + ChatColor.GREEN + "' was unbanned");
			}else {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Regex '" + ChatColor.WHITE + p1[1] + ChatColor.RED + "' is not banned");
			}
		}
	}

}
