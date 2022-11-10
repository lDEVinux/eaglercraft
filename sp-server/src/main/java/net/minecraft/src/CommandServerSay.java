package net.minecraft.src;

import java.util.List;

import net.minecraft.server.MinecraftServer;

public class CommandServerSay extends CommandBase {
	public String getCommandName() {
		return "say";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 1;
	}

	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return par1ICommandSender.translateString("commands.say.usage", new Object[0]);
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length > 0 && par2ArrayOfStr[0].length() > 0) {
			String var3 = func_82361_a(par1ICommandSender, par2ArrayOfStr, 0, true);
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(
					String.format("[%s] %s", new Object[] { par1ICommandSender.getCommandSenderName(), var3 }));
		} else {
			throw new WrongUsageException("commands.say.usage", new Object[0]);
		}
	}

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return par2ArrayOfStr.length >= 1
				? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames())
				: null;
	}
}
