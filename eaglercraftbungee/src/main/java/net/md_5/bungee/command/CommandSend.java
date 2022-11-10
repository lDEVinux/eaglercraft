// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandSend extends Command {
	public CommandSend() {
		super("send", "bungeecord.command.send", new String[0]);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (args.length != 2) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments, usage: /send <player|all|current> <target>");
			return;
		}
		final ServerInfo target = ProxyServer.getInstance().getServerInfo(args[1]);
		if (target == null) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_server"));
			return;
		}
		if (args[0].equalsIgnoreCase("all")) {
			for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
				this.summon(p, target, sender);
			}
		} else if (args[0].equalsIgnoreCase("current")) {
			if (!(sender instanceof ProxiedPlayer)) {
				sender.sendMessage(ChatColor.RED + "Only in game players can use this command");
				return;
			}
			final ProxiedPlayer player = (ProxiedPlayer) sender;
			for (final ProxiedPlayer p2 : player.getServer().getInfo().getPlayers()) {
				this.summon(p2, target, sender);
			}
		} else {
			final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "That player is not online");
				return;
			}
			this.summon(player, target, sender);
		}
		sender.sendMessage(ChatColor.GREEN + "Successfully summoned player(s)");
	}

	private void summon(final ProxiedPlayer player, final ServerInfo target, final CommandSender sender) {
		if (player.getServer() != null && player.getServer().getInfo() != null && !player.getServer().getInfo().equals(target)) {
			player.connect(target);
			player.sendMessage(ChatColor.GOLD + "Summoned to " + target.getName() + " by " + sender.getName());
		}
	}
}
