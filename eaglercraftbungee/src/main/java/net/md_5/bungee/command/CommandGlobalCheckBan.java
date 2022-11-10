package net.md_5.bungee.command;

import java.net.InetAddress;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;
import net.md_5.bungee.eaglercraft.BanList.BanCheck;
import net.md_5.bungee.eaglercraft.BanList.BanState;

public class CommandGlobalCheckBan extends Command {
	
	private final boolean replaceBukkit;

	public CommandGlobalCheckBan(boolean replaceBukkit) {
		super(replaceBukkit ? "banned" : "eag-bannned", "bungeecord.command.eag.banned", replaceBukkit ? new String[] { "eag-banned", "isbanned", "e-banned", "gbanned", "eag-isbanned", "e-isbanned", "gisbanned" } :
				new String[] { "e-banned", "gbanned", "eag-isbanned", "e-isbanned", "gisbanned" });
		this.replaceBukkit = replaceBukkit;
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if(p1.length != 1) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To check if a player or IP is banned, use: " + ChatColor.WHITE + (replaceBukkit ? "/banned" : "/eag-banned") + " <username|ip>");
		}else {
			BanCheck bc = BanList.checkBanned(p1[0]);
			if(!bc.isBanned()) {
				try {
					InetAddress addr = InetAddress.getByName(p1[0]);
					bc = BanList.checkIpBanned(addr);
					if(bc.isBanned()) {
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "IP address '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is banned by: "
								 + "'" + ChatColor.WHITE + bc.match + ChatColor.RED + "' " + ChatColor.YELLOW + "(" + bc.string + ")");
						return;
					}
				}catch(Throwable t) {
					// no
				}
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Player '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' has not been banned");
			}else {
				if(bc.reason == BanState.USER_BANNED) {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Player '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is banned by username, reason: "
								+ ChatColor.YELLOW + "\"" + bc.string + "\"");
				}else if(bc.reason == BanState.WILDCARD_BANNED) {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Player '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is banned by wildcard: "
							+ ChatColor.WHITE + "\"" + bc.match + "\"");
				}else if(bc.reason == BanState.REGEX_BANNED) {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Player '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is banned by regex: "
							+ ChatColor.WHITE + "\"" + bc.match + "\"");
				}
			}
		}
	}

}
