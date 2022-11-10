package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;

public class CommandGlobalListBan extends Command {
	
	private final boolean replaceBukkit;

	public CommandGlobalListBan(boolean replaceBukkit) {
		super(replaceBukkit ? "banlist" : "eag-banlist", "bungeecord.command.eag.banlist", replaceBukkit ? new String[] { "eag-banlist", "gbanlist", "e-banlist",
				"gbanlist" } : new String[] { "gbanlist", "e-banlist" });
		this.replaceBukkit = replaceBukkit;
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if(p1.length == 0 || (p1.length == 1 && (p1[0].equalsIgnoreCase("user") || p1[0].equalsIgnoreCase("username")
				|| p1[0].equalsIgnoreCase("users") || p1[0].equalsIgnoreCase("usernames")))) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Players banned by username: " + ChatColor.WHITE + BanList.listAllBans());
			return;
		}else if(p1.length == 1) {
			if(p1[0].equalsIgnoreCase("regex") || p1[0].equalsIgnoreCase("regexes")) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Regex ban list: " + ChatColor.WHITE + BanList.listAllRegexBans());
				return;
			}else if(p1[0].equalsIgnoreCase("wildcard") || p1[0].equalsIgnoreCase("wildcards")) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Wildcard ban list: " + ChatColor.WHITE + BanList.listAllWildcardBans());
				return;
			}else if(p1[0].equalsIgnoreCase("ip") || p1[0].equalsIgnoreCase("ips")) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To list IP bans, use: " + ChatColor.WHITE + (replaceBukkit ? "/banlist" : "/eag-banlist") + " ip <addr|netmask> [v4|v6]");
				return;
			}
		}else if(p1.length > 1 && p1.length <= 3 && (p1[0].equalsIgnoreCase("ip") || p1[0].equalsIgnoreCase("ips"))) {
			int addrOrNetmask = 0;
			if(p1[1].equalsIgnoreCase("addr") || p1[1].equalsIgnoreCase("addrs")) {
				addrOrNetmask = 1;
			}else if(p1[1].equalsIgnoreCase("netmask") || p1[1].equalsIgnoreCase("netmasks")) {
				addrOrNetmask = 2;
			}
			if(addrOrNetmask > 0) {
				boolean yes = false;
				if(p1.length == 2 || (p1.length == 3 && (p1[2].equalsIgnoreCase("v4") || p1[2].equals("4")))) {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "IPv4 " + (addrOrNetmask == 2 ? "netmask" : "address") + " ban list: " + ChatColor.WHITE + BanList.listAllIPBans(false, addrOrNetmask == 2));
					yes = true;
				}
				if(p1.length == 2 || (p1.length == 3 && (p1[2].equalsIgnoreCase("v6") || p1[2].equals("6")))) {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "IPv6 " + (addrOrNetmask == 2 ? "netmask" : "address") + " ban list: " + ChatColor.WHITE + BanList.listAllIPBans(true, addrOrNetmask == 2));
					yes = true;
				}
				if(yes) {
					return;
				}
			}
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To list IP bans, use: " + ChatColor.WHITE + (replaceBukkit ? "/banlist" : "/eag-banlist") + " ip <addr|netmask> [v4|v6]");
			return;
		}
		p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To list all user bans, use: " + ChatColor.WHITE + (replaceBukkit ? "/banlist" : "/eag-banlist"));
		p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To list ips, regexes, and wildcards, use: " + ChatColor.WHITE + (replaceBukkit ? "/banlist" : "/eag-banlist") + " <ip|regex|wildcard>");
		return;
	}

}
