package net.md_5.bungee.command;

import java.util.Collection;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;

public class CommandGlobalBan extends Command {
	
	private final boolean replaceBukkit;

	public CommandGlobalBan(boolean replaceBukkit) {
		super(replaceBukkit ? "ban" : "eag-ban", "bungeecord.command.eag.ban", replaceBukkit ? new String[] { "kickban", "eag-ban", "e-ban", "gban" } : new String[] { "e-ban", "gban" });
		this.replaceBukkit = replaceBukkit;
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if(p1.length >= 1) {
			String p = p1[0].trim().toLowerCase();
			if(p0.getName().equalsIgnoreCase(p)) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "You cannot ban yourself");
				return;
			}
			String reason = "The ban hammer has spoken!";
			if(p1.length >= 2) {
				reason = "";
				for(int i = 1; i < p1.length; ++i) {
					if(reason.length() > 0) {
						reason += " ";
					}
					reason += p1[i];
				}
			}
			String wasTheKick = null;
			Collection<ProxiedPlayer> playerz = BungeeCord.getInstance().getPlayers();
			for(ProxiedPlayer pp : playerz) {
				if(pp.getName().equalsIgnoreCase(p)) {
					wasTheKick = pp.getName();
					pp.disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: " + reason);
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.WHITE + "Kicked: " + pp.getName());
				}
			}
			if(BanList.ban(p, reason)) {
				if(wasTheKick == null) {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Warning! '" + ChatColor.WHITE + p + ChatColor.YELLOW + "' is not currently on this server");
				}
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Username '" + ChatColor.WHITE + (wasTheKick == null ? p : wasTheKick) + ChatColor.GREEN + "' was added to the ban list");
			}else {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Username '" + ChatColor.WHITE + p + ChatColor.RED + "' is already banned");
			}
		}else {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To ban a player, use: " + ChatColor.WHITE + "/" + (replaceBukkit?"":"eag-") + "ban <player> [reason]");
		}
	}

}
