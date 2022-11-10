package net.md_5.bungee.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;

public class CommandGlobalBanRegex extends Command {
	
	private final boolean replaceBukkit;

	public CommandGlobalBanRegex(boolean replaceBukkit) {
		super(replaceBukkit ? "ban-regex" : "eag-ban-regex", "bungeecord.command.eag.banregex", replaceBukkit ? new String[] { "eag-ban-regex", "e-ban-regex",	
				"gban-regex", "eag-banregex", "e-banregex", "gbanregex", "banregex" } : new String[] { "e-ban-regex", "gban-regex",
						"eag-banregex", "e-banregex", "gbanregex" });
		this.replaceBukkit = replaceBukkit;
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		String w = (String) p0.getAttachment().get("banRegexWaitingToAdd");
		if(w != null) {
			List<ProxiedPlayer> lst = (List<ProxiedPlayer>)p0.getAttachment().get("banRegexWaitingToKick");
			if(p1.length != 1 || (!p1[0].equalsIgnoreCase("confirm") && !p1[0].equalsIgnoreCase("cancel"))) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-regex" : "/eag-ban-regex") + " confirm" + ChatColor.RED + " to add regex " + ChatColor.WHITE + w +
						ChatColor.RED + " and ban " + ChatColor.WHITE + lst.size() + ChatColor.RED + " players");
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-regex" : "/eag-ban-regex") + " cancel" + ChatColor.RED + " to cancel this operation");
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Note: all usernames are converted to lowercase before being matched");
			}else {
				if(p1[0].equalsIgnoreCase("confirm")) {
					if(BanList.banRegex(w)) {
						for(ProxiedPlayer pp : lst) {
							pp.disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: banned by regex");
							p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Kicked: " + ChatColor.WHITE + pp.getName());
						}
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Added regex '" + ChatColor.WHITE + w + ChatColor.GREEN + "' to the ban list");
					}else {
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Regex '" + ChatColor.WHITE + w + ChatColor.RED + "' is already banned");
					}
				}else {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Canceled ban");
				}
				p0.getAttachment().remove("banRegexWaitingToAdd");
				p0.getAttachment().remove("banRegexWaitingToKick");
			}
			return;
		}
		if(p1.length != 1) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "How to use: " + ChatColor.WHITE + (replaceBukkit ? "/ban-regex" : "/eag-ban-regex") + " <pattern>");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Note: all usernames are converted to lowercase before being matched");
			return;
		}
		Pattern p;
		try {
			p = Pattern.compile(p1[0]);
		}catch(Throwable t) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Regex syntax error: " + t.getMessage());
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Note: all usernames are converted to lowercase before being matched");
			return;
		}
		boolean isSenderGonnaGetKicked = false;
		List<ProxiedPlayer> usersThatAreGonnaBeKicked = new ArrayList();
		for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers()) {
			String n = pp.getName().toLowerCase();
			if(p.matcher(n).matches()) {
				usersThatAreGonnaBeKicked.add(pp);
				if(n.equalsIgnoreCase(p0.getName())) {
					isSenderGonnaGetKicked = true;
					break;
				}
			}
		}
		if(isSenderGonnaGetKicked) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "banning regex '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is gonna ban your own username");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Note: all usernames are converted to lowercase before being matched");
			return;
		}
		if(usersThatAreGonnaBeKicked.size() > 1) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "WARNING: banning regex '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is gonna ban " +
					ChatColor.WHITE + usersThatAreGonnaBeKicked.size() + ChatColor.RED + " players off of your server");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-regex" : "/eag-ban-regex") + " confirm" + ChatColor.RED + " to continue, or type " +
					ChatColor.WHITE + "/eag-ban-regex cancel" + ChatColor.RED + " to cancel");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Note: all usernames are converted to lowercase before being matched");
			p0.getAttachment().put("banRegexWaitingToKick", usersThatAreGonnaBeKicked);
			p0.getAttachment().put("banRegexWaitingToAdd", p1[0]);
		}else {
			if(BanList.banRegex(p1[0])) {
				if(usersThatAreGonnaBeKicked.size() > 0) {
					usersThatAreGonnaBeKicked.get(0).disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: banned by regex");
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Kicked: " + ChatColor.WHITE + usersThatAreGonnaBeKicked.get(0).getName());
				}
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Added regex '" + ChatColor.WHITE + p1[0] + ChatColor.GREEN + "' to the ban list");
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Note: all usernames are converted to lowercase before being matched");
			}else {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Regex '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is already banned");
			}
		}
	}

}
