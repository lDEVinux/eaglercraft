// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandList extends Command {
	public CommandList() {
		super("glist", "bungeecord.command.list", new String[0]);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		for (final ServerInfo server : ProxyServer.getInstance().getServers().values()) {
			if (!server.canAccess(sender)) {
				continue;
			}
			final Collection<ProxiedPlayer> serverPlayers = server.getPlayers();
			final StringBuilder message = new StringBuilder();
			message.append(ChatColor.GREEN);
			message.append("[");
			message.append(server.getName());
			message.append("] ");
			message.append(ChatColor.YELLOW);
			message.append("(");
			message.append(serverPlayers.size());
			message.append("): ");
			message.append(ChatColor.RESET);
			final List<String> players = new ArrayList<String>();
			for (final ProxiedPlayer player : serverPlayers) {
				players.add(player.getDisplayName());
			}
			Collections.sort(players, String.CASE_INSENSITIVE_ORDER);
			message.append(Util.format(players, ChatColor.RESET + ", "));
			sender.sendMessage(message.toString());
		}
		sender.sendMessage(ProxyServer.getInstance().getTranslation("total_players") + ProxyServer.getInstance().getOnlineCount());
	}
}
