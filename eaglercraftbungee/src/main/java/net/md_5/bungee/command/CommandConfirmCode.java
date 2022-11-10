package net.md_5.bungee.command;

import java.nio.charset.StandardCharsets;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.eaglercraft.QueryConnectionImpl;
import net.md_5.bungee.eaglercraft.SHA1Digest;

public class CommandConfirmCode extends Command {

	public CommandConfirmCode() {
		super("confirm-code", "bungeecord.command.eag.confirmcode", "confirmcode");
	}

	@Override
	public void execute(CommandSender p0, String[] p1) {
		if(p1.length != 1) {
			p0.sendMessage(ChatColor.RED + "How to use: " + ChatColor.WHITE + "/confirm-code <code>");
		}else {
			p0.sendMessage(ChatColor.YELLOW + "Server list 2FA code has been set to: " + ChatColor.GREEN + p1[0]);
			p0.sendMessage(ChatColor.YELLOW + "You can now return to the server list site and continue");
			byte[] bts = p1[0].getBytes(StandardCharsets.US_ASCII);
			SHA1Digest dg = new SHA1Digest();
			dg.update(bts, 0, bts.length);
			byte[] f = new byte[20];
			dg.doFinal(f, 0);
			QueryConnectionImpl.confirmHash = SHA1Digest.hash2string(f);
		}
	}

}
