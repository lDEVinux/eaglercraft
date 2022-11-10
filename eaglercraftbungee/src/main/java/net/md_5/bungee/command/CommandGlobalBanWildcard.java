package net.md_5.bungee.command;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;

public class CommandGlobalBanWildcard extends Command {

	private final boolean replaceBukkit;
	
	public CommandGlobalBanWildcard(boolean replaceBukkit) {
		super(replaceBukkit ? "ban-wildcard" : "eag-ban-wildcard", "bungeecord.command.eag.banwildcard", replaceBukkit ? new String[] { "eag-ban-wildcard", "e-ban-wildcard", "gban-wildcard",
					"banwildcard", "eag-banwildcard", "banwildcard"} : new String[] { "e-ban-wildcard", "gban-wildcard", "eag-banwildcard"});
		this.replaceBukkit = replaceBukkit;
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		String w = (String) p0.getAttachment().get("banWildcardWaitingToAdd");
		if(w != null) {
			List<ProxiedPlayer> lst = (List<ProxiedPlayer>)p0.getAttachment().get("banWildcardWaitingToKick");
			if(p1.length != 1 || (!p1[0].equalsIgnoreCase("confirm") && !p1[0].equalsIgnoreCase("cancel"))) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-wildcard" : "/eag-ban-wildcard") + " confirm" + ChatColor.RED + " to add wildcard " + ChatColor.WHITE + w +
						ChatColor.RED + " and ban " + ChatColor.WHITE + lst.size() + ChatColor.RED + " players");
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-wildcard" : "/eag-ban-wildcard") + " cancel" + ChatColor.RED + " to cancel this operation");
			}else {
				if(p1[0].equalsIgnoreCase("confirm")) {
					if(BanList.banWildcard(w)) {
						for(ProxiedPlayer pp : lst) {
							pp.disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: banned by wildcard");
							p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Kicked: " + ChatColor.WHITE + pp.getName());
						}
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Added wildcard '" + ChatColor.WHITE + w + ChatColor.GREEN + "' to the ban list");
					}else {
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Wildcard '" + ChatColor.WHITE + w + ChatColor.RED + "' is already banned");
					}
				}else {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Canceled ban");
				}
				p0.getAttachment().remove("banWildcardWaitingToAdd");
				p0.getAttachment().remove("banWildcardWaitingToKick");
			}
			return;
		}
		if(p1.length != 1) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "How to use: " + ChatColor.WHITE + (replaceBukkit ? "/ban-wildcard" : "/eag-ban-wildcard") + " <pattern>");
			return;
		}
		p1[0] = p1[0].toLowerCase();
		String s = p1[0];
		boolean startStar = s.startsWith("*");
		if(startStar) {
			s = s.substring(1);
		}
		boolean endStar = s.endsWith("*");
		if(endStar) {
			s = s.substring(0, s.length() - 1);
		}
		if(!startStar && !endStar) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "'" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is not a wildcard, try '"
					+ ChatColor.WHITE + "*" + p1[0] + ChatColor.RED + "' or '" + ChatColor.WHITE + p1[0] + "*" + ChatColor.RED + "' or '" + ChatColor.WHITE
						+ "*" + p1[0] + "*" + ChatColor.RED + "' instead");
			return;
		}
		boolean isSenderGonnaGetKicked = false;
		List<ProxiedPlayer> usersThatAreGonnaBeKicked = new ArrayList();
		for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers()) {
			String n = pp.getName().toLowerCase();
			if(startStar && endStar) {
				if(n.contains(s)) {
					usersThatAreGonnaBeKicked.add(pp);
					if(pp.getName().equalsIgnoreCase(p0.getName())) {
						isSenderGonnaGetKicked = true;
						break;
					}
				}
			}else if(startStar) {
				if(n.endsWith(s)) {
					usersThatAreGonnaBeKicked.add(pp);
					if(pp.getName().equalsIgnoreCase(p0.getName())) {
						isSenderGonnaGetKicked = true;
						break;
					}
				}
			}else if(endStar) {
				if(n.startsWith(s)) {
					usersThatAreGonnaBeKicked.add(pp);
					if(pp.getName().equalsIgnoreCase(p0.getName())) {
						isSenderGonnaGetKicked = true;
						break;
					}
				}
			}
		}
		if(isSenderGonnaGetKicked) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "banning wildcard '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is gonna ban your own username");
			return;
		}
		if(usersThatAreGonnaBeKicked.size() > 1) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "WARNING: banning wildcard '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is gonna ban " +
					ChatColor.WHITE + usersThatAreGonnaBeKicked.size() + ChatColor.RED + " players off of your server");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-wildcard" : "/eag-ban-wildcard") + " confirm" + ChatColor.RED + " to continue, or type " +
					ChatColor.WHITE + (replaceBukkit ? "/ban-wildcard" : "/eag-ban-wildcard") + " cancel" + ChatColor.RED + " to cancel");
			p0.getAttachment().put("banWildcardWaitingToKick", usersThatAreGonnaBeKicked);
			p0.getAttachment().put("banWildcardWaitingToAdd", p1[0]);
		}else {
			if(BanList.banWildcard(p1[0])) {
				if(usersThatAreGonnaBeKicked.size() > 0) {
					usersThatAreGonnaBeKicked.get(0).disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: banned by wildcard");
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Kicked: " + ChatColor.WHITE + usersThatAreGonnaBeKicked.get(0).getName());
				}
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Added wildcard '" + ChatColor.WHITE + p1[0] + ChatColor.GREEN + "' to the ban list");
			}else {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Wildcard '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is already banned");
			}
		}
	}

}
