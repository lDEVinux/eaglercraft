package net.minecraft.src;

import java.util.List;

import net.minecraft.server.MinecraftServer;

public class CommandServerEmote extends CommandBase {
	public String getCommandName() {
		return "me";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 0;
	}

	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return par1ICommandSender.translateString("commands.me.usage", new Object[0]);
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length > 0) {
			String var3 = func_82361_a(par1ICommandSender, par2ArrayOfStr, 0,
					par1ICommandSender.canCommandSenderUseCommand(1, "me"));
			MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(
					new Packet3Chat("* " + par1ICommandSender.getCommandSenderName() + " " + var3));
		} else {
			throw new WrongUsageException("commands.me.usage", new Object[0]);
		}
	}

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
	}
}
