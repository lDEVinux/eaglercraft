package net.minecraft.src;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.server.MinecraftServer;

public class PlayerSelector {
	/**
	 * This matches the at-tokens introduced for command blocks, including their
	 * arguments, if any.
	 */
	private static final Pattern tokenPattern = Pattern.compile("^@([parf])(?:\\[([\\w=,!-]*)\\])?$");

	/**
	 * This matches things like "-1,,4", and is used for getting x,y,z,range from
	 * the token's argument list.
	 */
	private static final Pattern intListPattern = Pattern.compile("\\G([-!]?\\w*)(?:$|,)");

	/**
	 * This matches things like "rm=4,c=2" and is used for handling named token
	 * arguments.
	 */
	private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?\\w*)(?:$|,)");

	/**
	 * Returns the one player that matches the given at-token. Returns null if more
	 * than one player matches.
	 */
	public static EntityPlayerMP matchOnePlayer(ICommandSender par0ICommandSender, String par1Str) {
		EntityPlayerMP[] var2 = matchPlayers(par0ICommandSender, par1Str);
		return var2 != null && var2.length == 1 ? var2[0] : null;
	}

	/**
	 * Returns a nicely-formatted string listing the matching players.
	 */
	public static String matchPlayersAsString(ICommandSender par0ICommandSender, String par1Str) {
		EntityPlayerMP[] var2 = matchPlayers(par0ICommandSender, par1Str);

		if (var2 != null && var2.length != 0) {
			String[] var3 = new String[var2.length];

			for (int var4 = 0; var4 < var3.length; ++var4) {
				var3[var4] = var2[var4].getTranslatedEntityName();
			}

			return CommandBase.joinNiceString(var3);
		} else {
			return null;
		}
	}

	/**
	 * Returns an array of all players matched by the given at-token.
	 */
	public static EntityPlayerMP[] matchPlayers(ICommandSender par0ICommandSender, String par1Str) {
		Matcher var2 = tokenPattern.matcher(par1Str);

		if (var2.matches()) {
			Map var3 = getArgumentMap(var2.group(2));
			String var4 = var2.group(1);
			int var5 = getDefaultMinimumRange(var4);
			int var6 = getDefaultMaximumRange(var4);
			int var7 = getDefaultMinimumLevel(var4);
			int var8 = getDefaultMaximumLevel(var4);
			int var9 = getDefaultCount(var4);
			int var10 = EnumGameType.NOT_SET.getID();
			ChunkCoordinates var11 = par0ICommandSender.getCommandSenderPosition();
			Map var12 = func_96560_a(var3);
			String var13 = null;
			String var14 = null;

			if (var3.containsKey("rm")) {
				var5 = MathHelper.parseIntWithDefault((String) var3.get("rm"), var5);
			}

			if (var3.containsKey("r")) {
				var6 = MathHelper.parseIntWithDefault((String) var3.get("r"), var6);
			}

			if (var3.containsKey("lm")) {
				var7 = MathHelper.parseIntWithDefault((String) var3.get("lm"), var7);
			}

			if (var3.containsKey("l")) {
				var8 = MathHelper.parseIntWithDefault((String) var3.get("l"), var8);
			}

			if (var3.containsKey("x")) {
				var11.posX = MathHelper.parseIntWithDefault((String) var3.get("x"), var11.posX);
			}

			if (var3.containsKey("y")) {
				var11.posY = MathHelper.parseIntWithDefault((String) var3.get("y"), var11.posY);
			}

			if (var3.containsKey("z")) {
				var11.posZ = MathHelper.parseIntWithDefault((String) var3.get("z"), var11.posZ);
			}

			if (var3.containsKey("m")) {
				var10 = MathHelper.parseIntWithDefault((String) var3.get("m"), var10);
			}

			if (var3.containsKey("c")) {
				var9 = MathHelper.parseIntWithDefault((String) var3.get("c"), var9);
			}

			if (var3.containsKey("team")) {
				var14 = (String) var3.get("team");
			}

			if (var3.containsKey("name")) {
				var13 = (String) var3.get("name");
			}

			List var15;

			if (!var4.equals("p") && !var4.equals("a")) {
				if (!var4.equals("r")) {
					return null;
				} else {
					var15 = MinecraftServer.getServer().getConfigurationManager().findPlayers(var11, var5, var6, 0,
							var10, var7, var8, var12, var13, var14);
					Collections.shuffle(var15);
					var15 = var15.subList(0, Math.min(var9, var15.size()));
					return var15 != null && !var15.isEmpty() ? (EntityPlayerMP[]) var15.toArray(new EntityPlayerMP[0])
							: new EntityPlayerMP[0];
				}
			} else {
				var15 = MinecraftServer.getServer().getConfigurationManager().findPlayers(var11, var5, var6, var9,
						var10, var7, var8, var12, var13, var14);
				return var15 != null && !var15.isEmpty() ? (EntityPlayerMP[]) var15.toArray(new EntityPlayerMP[0])
						: new EntityPlayerMP[0];
			}
		} else {
			return null;
		}
	}

	public static Map func_96560_a(Map par0Map) {
		HashMap var1 = new HashMap();
		Iterator var2 = par0Map.keySet().iterator();

		while (var2.hasNext()) {
			String var3 = (String) var2.next();

			if (var3.startsWith("score_") && var3.length() > "score_".length()) {
				String var4 = var3.substring("score_".length());
				var1.put(var4, Integer.valueOf(MathHelper.parseIntWithDefault((String) par0Map.get(var3), 1)));
			}
		}

		return var1;
	}

	/**
	 * Returns whether the given pattern can match more than one player.
	 */
	public static boolean matchesMultiplePlayers(String par0Str) {
		Matcher var1 = tokenPattern.matcher(par0Str);

		if (var1.matches()) {
			Map var2 = getArgumentMap(var1.group(2));
			String var3 = var1.group(1);
			int var4 = getDefaultCount(var3);

			if (var2.containsKey("c")) {
				var4 = MathHelper.parseIntWithDefault((String) var2.get("c"), var4);
			}

			return var4 != 1;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether the given token (parameter 1) has exactly the given arguments
	 * (parameter 2).
	 */
	public static boolean hasTheseArguments(String par0Str, String par1Str) {
		Matcher var2 = tokenPattern.matcher(par0Str);

		if (var2.matches()) {
			String var3 = var2.group(1);
			return par1Str == null || par1Str.equals(var3);
		} else {
			return false;
		}
	}

	/**
	 * Returns whether the given token has any arguments set.
	 */
	public static boolean hasArguments(String par0Str) {
		return hasTheseArguments(par0Str, (String) null);
	}

	/**
	 * Gets the default minimum range (argument rm).
	 */
	private static final int getDefaultMinimumRange(String par0Str) {
		return 0;
	}

	/**
	 * Gets the default maximum range (argument r).
	 */
	private static final int getDefaultMaximumRange(String par0Str) {
		return 0;
	}

	/**
	 * Gets the default maximum experience level (argument l)
	 */
	private static final int getDefaultMaximumLevel(String par0Str) {
		return Integer.MAX_VALUE;
	}

	/**
	 * Gets the default minimum experience level (argument lm)
	 */
	private static final int getDefaultMinimumLevel(String par0Str) {
		return 0;
	}

	/**
	 * Gets the default number of players to return (argument c, 0 for infinite)
	 */
	private static final int getDefaultCount(String par0Str) {
		return par0Str.equals("a") ? 0 : 1;
	}

	/**
	 * Parses the given argument string, turning it into a HashMap&lt;String,
	 * String&gt; of name-&gt;value.
	 */
	private static Map getArgumentMap(String par0Str) {
		HashMap var1 = new HashMap();

		if (par0Str == null) {
			return var1;
		} else {
			Matcher var2 = intListPattern.matcher(par0Str);
			int var3 = 0;
			int var4;

			for (var4 = -1; var2.find(); var4 = var2.end()) {
				String var5 = null;

				switch (var3++) {
				case 0:
					var5 = "x";
					break;

				case 1:
					var5 = "y";
					break;

				case 2:
					var5 = "z";
					break;

				case 3:
					var5 = "r";
				}

				if (var5 != null && var2.group(1).length() > 0) {
					var1.put(var5, var2.group(1));
				}
			}

			if (var4 < par0Str.length()) {
				var2 = keyValueListPattern.matcher(var4 == -1 ? par0Str : par0Str.substring(var4));

				while (var2.find()) {
					var1.put(var2.group(1), var2.group(2));
				}
			}

			return var1;
		}
	}
}
