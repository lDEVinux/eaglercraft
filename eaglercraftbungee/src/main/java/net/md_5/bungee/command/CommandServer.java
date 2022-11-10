// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import java.util.Map;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandServer extends Command {
	public CommandServer() {
		super("server", "bungeecord.command.server", new String[0]);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		final ProxiedPlayer player = (ProxiedPlayer) sender;
		final Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
		if (args.length == 0) {
			player.sendMessage(ProxyServer.getInstance().getTranslation("current_server") + player.getServer().getInfo().getName());
			final StringBuilder serverList = new StringBuilder();
			for (final ServerInfo server : servers.values()) {
				if (server.canAccess(player)) {
					serverList.append(server.getName());
					serverList.append(", ");
				}
			}
			if (serverList.length() != 0) {
				serverList.setLength(serverList.length() - 2);
			}
			player.sendMessage(ProxyServer.getInstance().getTranslation("server_list") + serverList.toString());
		} else {
			final ServerInfo server2 = servers.get(args[0]);
			if (server2 == null) {
				player.sendMessage(ProxyServer.getInstance().getTranslation("no_server"));
			} else if (!server2.canAccess(player)) {
				player.sendMessage(ProxyServer.getInstance().getTranslation("no_server_permission"));
			} else {
				player.connect(server2);
			}
		}
	}
}
