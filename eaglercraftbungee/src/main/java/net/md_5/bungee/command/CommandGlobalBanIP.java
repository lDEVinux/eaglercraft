package net.md_5.bungee.command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.BanList;
import net.md_5.bungee.eaglercraft.BanList.IPBan;

public class CommandGlobalBanIP extends Command {
	
	private final boolean replaceBukkit;

	public CommandGlobalBanIP(boolean replaceBukkit) {
		super(replaceBukkit ? "ban-ip" : "eag-ban-ip", "bungeecord.command.eag.banip", (replaceBukkit ? new String[] {"eag-ban-ip", "banip", "e-ban-ip", "gban-ip"} : 
				new String[] {"gban-ip", "e-ban-ip", "gbanip", "e-banip"}) );
		this.replaceBukkit = replaceBukkit;
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		String w = (String) p0.getAttachment().get("banIPWaitingToAdd");
		if(w != null) {
			List<ProxiedPlayer> lst = (List<ProxiedPlayer>)p0.getAttachment().get("banIPWaitingToKick");
			if(p1.length != 1 || (!p1[0].equalsIgnoreCase("confirm") && !p1[0].equalsIgnoreCase("cancel"))) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-ip" : "/eag-ban-ip") + " confirm" + ChatColor.RED + " to add IP " + ChatColor.WHITE + w +
						ChatColor.RED + " and ban " + ChatColor.WHITE + lst.size() + ChatColor.RED + " players");
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-ip" : "/eag-ban-ip") + " cancel" + ChatColor.RED + " to cancel this operation");
			}else {
				if(p1[0].equalsIgnoreCase("confirm")) {
					try {
						if(BanList.banIP(w)) {
							for(ProxiedPlayer pp : lst) {
								pp.disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: banned by IP");
								p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Kicked: " + ChatColor.WHITE + pp.getName());
							}
							p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Added IP '" + ChatColor.WHITE + w + ChatColor.GREEN + "' to the ban list");
						}else {
							p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "IP '" + ChatColor.WHITE + w + ChatColor.RED + "' is already on the ban list");
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "ERROR: address '" + ChatColor.WHITE + w + ChatColor.RED + "' is suddenly invalid for some reason");
					}
				}else {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Canceled ban");
				}
				p0.getAttachment().remove("banIPWaitingToAdd");
				p0.getAttachment().remove("banIPWaitingToKick");
			}
			return;
		}
		if(p1.length != 1) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "How to use: " + ChatColor.WHITE + (replaceBukkit ? "/ban-ip" : "/eag-ban-ip") + " <addr|player>");
			return;
		}
		boolean isPlayer = false;
		IPBan p = null;
		try {
			p = BanList.constructIpBan(p1[0]);
		}catch(Throwable t) {
			for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers()) {
				if(pp.getName().equalsIgnoreCase(p1[0])) {
					Object addr = pp.getAttachment().get("remoteAddr");
					if(addr != null) {
						String newAddr = ((InetAddress)addr).getHostAddress();
						isPlayer = true;
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Player '" + ChatColor.WHITE + p1[0] + ChatColor.GREEN + "' has IP " + ChatColor.WHITE + newAddr);
						p1[0] = newAddr;
						try {
							p = BanList.constructIpBan(p1[0]);
						}catch(UnknownHostException ex) {
							p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Address '" + ChatColor.WHITE + p1[0] + "' is suddenly invalid: " + ChatColor.WHITE + p1[0]);
							return;
						}
					}
					break;
				}
			}
			if(!isPlayer) {
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Player '" + ChatColor.WHITE + p1[0] + "' is not on this server");
				return;
			}
		}
		boolean blocked = false;
		for(IPBan b : BanList.blockedBans) {
			if(b.checkBan(p.getBaseAddress()) || p.checkBan(b.getBaseAddress())) {
				blocked = true;
			}
		}
		if(blocked) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Cannot ban '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "', it will ban local addresses that may break your game");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "To force, add to the " + ChatColor.WHITE + "[IPs]" + ChatColor.RED + " section of " + ChatColor.WHITE + "bans.txt" + ChatColor.RED + " in your bungee directory");
			return;
		}
		boolean isSenderGonnaGetKicked = false;
		List<ProxiedPlayer> usersThatAreGonnaBeKicked = new ArrayList();
		for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers()) {
			Object addr = pp.getAttachment().get("remoteAddr");
			if(addr != null) {
				InetAddress addrr = (InetAddress)addr;
				if(p.checkBan(addrr)) {
					usersThatAreGonnaBeKicked.add(pp);
					if(pp.getName().equalsIgnoreCase(p0.getName())) {
						isSenderGonnaGetKicked = true;
						break;
					}
				}
			}
		}
		if(isSenderGonnaGetKicked) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "banning address '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' will ban you off of your own server");
			return;
		}
		if(usersThatAreGonnaBeKicked.size() > 1) {
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "WARNING: banning address '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is gonna ban " +
					ChatColor.WHITE + usersThatAreGonnaBeKicked.size() + ChatColor.RED + " players off of your server");
			p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "Type " + ChatColor.WHITE + (replaceBukkit ? "/ban-ip" : "/eag-ban-ip") + " confirm" + ChatColor.RED + " to continue, or type " +
					ChatColor.WHITE + (replaceBukkit ? "/ban-ip" : "/eag-ban-ip") + " cancel" + ChatColor.RED + " to cancel");
			p0.getAttachment().put("banIPWaitingToKick", usersThatAreGonnaBeKicked);
			p0.getAttachment().put("banIPWaitingToAdd", p1[0]);
		}else {
			try {
				if(BanList.banIP(p1[0])) {
					if(usersThatAreGonnaBeKicked.size() > 0) {
						usersThatAreGonnaBeKicked.get(0).disconnect("" + ChatColor.RED + "You are banned.\n" + ChatColor.DARK_GRAY + "Reason: banned by IP");
						p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Kicked: " + ChatColor.WHITE + usersThatAreGonnaBeKicked.get(0).getName());
					}
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.GREEN + "Added IP '" + ChatColor.WHITE + p1[0] + ChatColor.GREEN + "' to the ban list");
				}else {
					p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "IP '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is already on the ban list");
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
				p0.sendMessage(BanList.banChatMessagePrefix + ChatColor.RED + "ERROR: address '" + ChatColor.WHITE + p1[0] + ChatColor.RED + "' is suddenly invalid for some reason");
				return;
			}
		}
	}

}
