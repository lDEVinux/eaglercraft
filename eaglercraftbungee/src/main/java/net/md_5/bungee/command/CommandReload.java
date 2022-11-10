// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CommandReload extends Command {
	public CommandReload() {
		super("greload", "bungeecord.command.reload", new String[0]);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		BungeeCord.getInstance().config.load();
		BungeeCord.getInstance().stopListeners();
		BungeeCord.getInstance().startListeners();
		sender.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED.toString() + "BungeeCord has been reloaded." + " This is NOT advisable and you will not be supported with any issues that arise! Please restart BungeeCord ASAP.");
	}
}
