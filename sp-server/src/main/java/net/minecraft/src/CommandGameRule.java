package net.minecraft.src;

import java.util.List;

import net.minecraft.server.MinecraftServer;

public class CommandGameRule extends CommandBase {
	public String getCommandName() {
		return "gamerule";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return par1ICommandSender.translateString("commands.gamerule.usage", new Object[0]);
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		String var6;

		if (par2ArrayOfStr.length == 2) {
			var6 = par2ArrayOfStr[0];
			String var7 = par2ArrayOfStr[1];
			GameRules var8 = this.getGameRules();

			if (var8.hasRule(var6)) {
				var8.setOrCreateGameRule(var6, var7);
				notifyAdmins(par1ICommandSender, "commands.gamerule.success", new Object[0]);
			} else {
				notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] { var6 });
			}
		} else if (par2ArrayOfStr.length == 1) {
			var6 = par2ArrayOfStr[0];
			GameRules var4 = this.getGameRules();

			if (var4.hasRule(var6)) {
				String var5 = var4.getGameRuleStringValue(var6);
				par1ICommandSender.sendChatToPlayer(var6 + " = " + var5);
			} else {
				notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] { var6 });
			}
		} else if (par2ArrayOfStr.length == 0) {
			GameRules var3 = this.getGameRules();
			par1ICommandSender.sendChatToPlayer(joinNiceString(var3.getRules()));
		} else {
			throw new WrongUsageException("commands.gamerule.usage", new Object[0]);
		}
	}

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return par2ArrayOfStr.length == 1
				? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getGameRules().getRules())
				: (par2ArrayOfStr.length == 2
						? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] { "true", "false" })
						: null);
	}

	/**
	 * Return the game rule set this command should be able to manipulate.
	 */
	private GameRules getGameRules() {
		return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
	}
}
