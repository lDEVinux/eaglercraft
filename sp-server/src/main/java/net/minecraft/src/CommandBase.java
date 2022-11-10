package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.MinecraftServer;

public abstract class CommandBase implements ICommand {
	private static IAdminCommand theAdmin = null;

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 4;
	}

	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return "/" + this.getCommandName();
	}

	public List getCommandAliases() {
		return null;
	}

	/**
	 * Returns true if the given command sender is allowed to use this command.
	 */
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
		return par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
	}

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return null;
	}

	/**
	 * Parses an int from the given string.
	 */
	public static int parseInt(ICommandSender par0ICommandSender, String par1Str) {
		try {
			return Integer.parseInt(par1Str);
		} catch (NumberFormatException var3) {
			throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { par1Str });
		}
	}

	/**
	 * Parses an int from the given sring with a specified minimum.
	 */
	public static int parseIntWithMin(ICommandSender par0ICommandSender, String par1Str, int par2) {
		return parseIntBounded(par0ICommandSender, par1Str, par2, Integer.MAX_VALUE);
	}

	/**
	 * Parses an int from the given string within a specified bound.
	 */
	public static int parseIntBounded(ICommandSender par0ICommandSender, String par1Str, int par2, int par3) {
		int var4 = parseInt(par0ICommandSender, par1Str);

		if (var4 < par2) {
			throw new NumberInvalidException("commands.generic.num.tooSmall",
					new Object[] { Integer.valueOf(var4), Integer.valueOf(par2) });
		} else if (var4 > par3) {
			throw new NumberInvalidException("commands.generic.num.tooBig",
					new Object[] { Integer.valueOf(var4), Integer.valueOf(par3) });
		} else {
			return var4;
		}
	}

	/**
	 * Parses a double from the given string or throws an exception if it's not a
	 * double.
	 */
	public static double parseDouble(ICommandSender par0ICommandSender, String par1Str) {
		try {
			return Double.parseDouble(par1Str);
		} catch (NumberFormatException var3) {
			throw new NumberInvalidException("commands.generic.double.invalid", new Object[] { par1Str });
		}
	}

	/**
	 * Returns the given ICommandSender as a EntityPlayer or throw an exception.
	 */
	public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender par0ICommandSender) {
		if (par0ICommandSender instanceof EntityPlayerMP) {
			return (EntityPlayerMP) par0ICommandSender;
		} else {
			throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.",
					new Object[0]);
		}
	}

	public static EntityPlayerMP func_82359_c(ICommandSender par0ICommandSender, String par1Str) {
		EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(par0ICommandSender, par1Str);

		if (var2 != null) {
			return var2;
		} else {
			var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerEntity(par1Str);

			if (var2 == null) {
				throw new PlayerNotFoundException();
			} else {
				return var2;
			}
		}
	}

	public static String func_96332_d(ICommandSender par0ICommandSender, String par1Str) {
		EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(par0ICommandSender, par1Str);

		if (var2 != null) {
			return var2.getEntityName();
		} else if (PlayerSelector.hasArguments(par1Str)) {
			throw new PlayerNotFoundException();
		} else {
			return par1Str;
		}
	}

	public static String func_82360_a(ICommandSender par0ICommandSender, String[] par1ArrayOfStr, int par2) {
		return func_82361_a(par0ICommandSender, par1ArrayOfStr, par2, false);
	}

	public static String func_82361_a(ICommandSender par0ICommandSender, String[] par1ArrayOfStr, int par2,
			boolean par3) {
		StringBuilder var4 = new StringBuilder();

		for (int var5 = par2; var5 < par1ArrayOfStr.length; ++var5) {
			if (var5 > par2) {
				var4.append(" ");
			}

			String var6 = par1ArrayOfStr[var5];

			if (par3) {
				String var7 = PlayerSelector.matchPlayersAsString(par0ICommandSender, var6);

				if (var7 != null) {
					var6 = var7;
				} else if (PlayerSelector.hasArguments(var6)) {
					throw new PlayerNotFoundException();
				}
			}

			var4.append(var6);
		}

		return var4.toString();
	}

	/**
	 * Joins the given string array into a "x, y, and z" seperated string.
	 */
	public static String joinNiceString(Object[] par0ArrayOfObj) {
		StringBuilder var1 = new StringBuilder();

		for (int var2 = 0; var2 < par0ArrayOfObj.length; ++var2) {
			String var3 = par0ArrayOfObj[var2].toString();

			if (var2 > 0) {
				if (var2 == par0ArrayOfObj.length - 1) {
					var1.append(" and ");
				} else {
					var1.append(", ");
				}
			}

			var1.append(var3);
		}

		return var1.toString();
	}

	public static String func_96333_a(Collection par0Collection) {
		return joinNiceString(par0Collection.toArray(new String[0]));
	}

	/**
	 * Returns true if the given substring is exactly equal to the start of the
	 * given string (case insensitive).
	 */
	public static boolean doesStringStartWith(String par0Str, String par1Str) {
		return par1Str.regionMatches(true, 0, par0Str, 0, par0Str.length());
	}

	/**
	 * Returns a List of strings (chosen from the given strings) which the last word
	 * in the given string array is a beginning-match for. (Tab completion).
	 */
	public static List getListOfStringsMatchingLastWord(String[] par0ArrayOfStr, String... par1ArrayOfStr) {
		String var2 = par0ArrayOfStr[par0ArrayOfStr.length - 1];
		ArrayList var3 = new ArrayList();
		String[] var4 = par1ArrayOfStr;
		int var5 = par1ArrayOfStr.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			String var7 = var4[var6];

			if (doesStringStartWith(var2, var7)) {
				var3.add(var7);
			}
		}

		return var3;
	}

	/**
	 * Returns a List of strings (chosen from the given string iterable) which the
	 * last word in the given string array is a beginning-match for. (Tab
	 * completion).
	 */
	public static List getListOfStringsFromIterableMatchingLastWord(String[] par0ArrayOfStr, Iterable par1Iterable) {
		String var2 = par0ArrayOfStr[par0ArrayOfStr.length - 1];
		ArrayList var3 = new ArrayList();
		Iterator var4 = par1Iterable.iterator();

		while (var4.hasNext()) {
			String var5 = (String) var4.next();

			if (doesStringStartWith(var2, var5)) {
				var3.add(var5);
			}
		}

		return var3;
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
		return false;
	}

	public static void notifyAdmins(ICommandSender par0ICommandSender, String par1Str, Object... par2ArrayOfObj) {
		notifyAdmins(par0ICommandSender, 0, par1Str, par2ArrayOfObj);
	}

	public static void notifyAdmins(ICommandSender par0ICommandSender, int par1, String par2Str,
			Object... par3ArrayOfObj) {
		if (theAdmin != null) {
			theAdmin.notifyAdmins(par0ICommandSender, par1, par2Str, par3ArrayOfObj);
		}
	}

	/**
	 * Sets the static IAdminCommander.
	 */
	public static void setAdminCommander(IAdminCommand par0IAdminCommand) {
		theAdmin = par0IAdminCommand;
	}

	/**
	 * Compares the name of this command to the name of the given command.
	 */
	public int compareTo(ICommand par1ICommand) {
		return this.getCommandName().compareTo(par1ICommand.getCommandName());
	}

	public int compareTo(Object par1Obj) {
		return this.compareTo((ICommand) par1Obj);
	}
}
