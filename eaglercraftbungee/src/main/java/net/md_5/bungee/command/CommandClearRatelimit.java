package net.md_5.bungee.command;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.plugin.Command;

public class CommandClearRatelimit extends Command {

	public CommandClearRatelimit() {
		super("eag-ratelimit", "bungeecord.command.eag.ratelimit", "e-ratelimit", "gratelimit");
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if(p1.length >= 1 && ("clear".equalsIgnoreCase(p1[0]) || "reset".equalsIgnoreCase(p1[0]))) {
			if(p1.length == 1 || (p1.length == 2 && "all".equalsIgnoreCase(p1[1]))) {
				for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
					if(l.getRateLimitIP() != null) l.getRateLimitIP().resetLimiters();
					if(l.getRateLimitLogin() != null) l.getRateLimitLogin().resetLimiters();
					if(l.getRateLimitMOTD() != null) l.getRateLimitMOTD().resetLimiters();
					if(l.getRateLimitQuery() != null) l.getRateLimitQuery().resetLimiters();
				}
				p0.sendMessage(ChatColor.GREEN + "Reset all ratelimits");
				return;
			}else if(p1.length == 2 || p1.length == 3) {
				ListenerInfo ll = null;
				if(p1.length == 3) {
					for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
						if(l.getHostString().equalsIgnoreCase(p1[2])) {
							ll = l;
							break;
						}
					}
					if(ll == null) {
						p0.sendMessage(ChatColor.RED + "Listener does not exist: " + ChatColor.WHITE + p1[2]);
						String accum = "";
						for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
							if(accum.length() > 0) {
								accum += ", ";
							}
							accum += l.getHostString();
						}
						p0.sendMessage(ChatColor.GREEN + "Listeners Available: " + ChatColor.WHITE + accum);
						return;
					}
				}
				if("all".equalsIgnoreCase(p1[1])) {
					if(ll != null) {
						if(ll.getRateLimitIP() != null) ll.getRateLimitIP().resetLimiters();
						if(ll.getRateLimitLogin() != null) ll.getRateLimitLogin().resetLimiters();
						if(ll.getRateLimitMOTD() != null) ll.getRateLimitMOTD().resetLimiters();
						if(ll.getRateLimitQuery() != null) ll.getRateLimitQuery().resetLimiters();
						p0.sendMessage(ChatColor.GREEN + "Reset all ratelimits on listener: " + ChatColor.WHITE + ll.getHostString());
					}else {
						for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
							if(l.getRateLimitIP() != null) l.getRateLimitIP().resetLimiters();
							if(l.getRateLimitLogin() != null) l.getRateLimitLogin().resetLimiters();
							if(l.getRateLimitMOTD() != null) l.getRateLimitMOTD().resetLimiters();
							if(l.getRateLimitQuery() != null) l.getRateLimitQuery().resetLimiters();
						}
						p0.sendMessage(ChatColor.GREEN + "Reset all ratelimits");
					}
					return;
				}else if("ip".equalsIgnoreCase(p1[1])) {
					if(ll != null) {
						if(ll.getRateLimitIP() != null) ll.getRateLimitIP().resetLimiters();
						p0.sendMessage(ChatColor.GREEN + "Reset all IP ratelimits on listener: " + ChatColor.WHITE + ll.getHostString());
					}else {
						for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
							if(l.getRateLimitIP() != null) l.getRateLimitIP().resetLimiters();
						}
						p0.sendMessage(ChatColor.GREEN + "Reset all IP ratelimits.");
					}
					return;
				}else if("login".equalsIgnoreCase(p1[1])) {
					if(ll != null) {
						if(ll.getRateLimitLogin() != null) ll.getRateLimitLogin().resetLimiters();
						p0.sendMessage(ChatColor.GREEN + "Reset all login ratelimits on listener: " + ChatColor.WHITE + ll.getHostString());
					}else {
						for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
							if(l.getRateLimitLogin() != null) l.getRateLimitLogin().resetLimiters();
						}
						p0.sendMessage(ChatColor.GREEN + "Reset all login ratelimits.");
					}
					return;
				}else if("motd".equalsIgnoreCase(p1[1])) {
					if(ll != null) {
						if(ll.getRateLimitMOTD() != null) ll.getRateLimitMOTD().resetLimiters();
						p0.sendMessage(ChatColor.GREEN + "Reset all MOTD ratelimits on listener: " + ChatColor.WHITE + ll.getHostString());
					}else {
						for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
							if(l.getRateLimitMOTD() != null) l.getRateLimitMOTD().resetLimiters();
						}
						p0.sendMessage(ChatColor.GREEN + "Reset all MOTD ratelimits.");
					}
					return;
				}else if("query".equalsIgnoreCase(p1[1])) {
					if(ll != null) {
						if(ll.getRateLimitMOTD() != null) ll.getRateLimitMOTD().resetLimiters();
						p0.sendMessage(ChatColor.GREEN + "Reset all query ratelimits on listener: " + ChatColor.WHITE + ll.getHostString());
					}else {
						for(ListenerInfo l : BungeeCord.getInstance().config.getListeners()) {
							if(l.getRateLimitMOTD() != null) l.getRateLimitMOTD().resetLimiters();
						}
						p0.sendMessage(ChatColor.GREEN + "Reset all query ratelimits.");
					}
					return;
				}
			}
		}
		p0.sendMessage(ChatColor.RED + "How to reset all rate limits: " + ChatColor.WHITE + "/eag-ratelimit reset");
		p0.sendMessage(ChatColor.RED + "How to reset a specific rate limit: " + ChatColor.WHITE + "/eag-ratelimit reset <ip|login|motd|query>");
		p0.sendMessage(ChatColor.RED + "How to reset a specific listener: " + ChatColor.WHITE + "/eag-ratelimit reset <all|ip|login|motd|query> <host>");
	}

}
