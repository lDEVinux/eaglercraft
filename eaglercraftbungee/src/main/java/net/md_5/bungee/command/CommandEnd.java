// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.command;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CommandEnd extends Command {
	public CommandEnd() {
		super("end", "bungeecord.command.end", new String[] { "exit", "quit" });
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		BungeeCord.getInstance().stop();
	}
}
