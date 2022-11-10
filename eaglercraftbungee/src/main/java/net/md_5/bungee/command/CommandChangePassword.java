package net.md_5.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.AuthSystem;

public class CommandChangePassword extends Command {
	private final AuthSystem authSystem;

	public CommandChangePassword(AuthSystem authSystem) {
		super("changepassword", "bungeecord.command.eag.changepassword",
				new String[] { "changepwd", "changepasswd", "changepass" });
		this.authSystem = authSystem;
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		String username = sender.getName();
		if (args.length == 0 || args.length == 1) {
			sender.sendMessage("\u00A7cUsage: /changepassword <oldPassword> <newPassword>");
		} else if (this.authSystem.login(username, args[0])) {
			if (this.authSystem.changePass(username, args[1])) {
				sender.sendMessage("\u00A7cPassword changed successfully!");
			} else {
				sender.sendMessage("\u00A7cUnable to change your password...");
			}
		} else {
			sender.sendMessage("\u00A7cThe old password specified is incorrect!");
		}
	}
}