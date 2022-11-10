package net.minecraft.src;

import java.util.List;

import net.minecraft.server.MinecraftServer;

public class CommandDebug extends CommandBase {
	/** Time the debugging started in milliseconds. */
	private long startTime = 0L;

	/** The number of ticks when debugging started. */
	private int startTicks = 0;

	public String getCommandName() {
		return "debug";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 3;
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length == 1) {
			if (par2ArrayOfStr[0].equals("start")) {
				notifyAdmins(par1ICommandSender, "commands.debug.start", new Object[0]);
				MinecraftServer.getServer().enableProfiling();
				this.startTime = System.currentTimeMillis();
				this.startTicks = MinecraftServer.getServer().getTickCounter();
				return;
			}

			if (par2ArrayOfStr[0].equals("stop")) {
				if (!MinecraftServer.getServer().theProfiler.profilingEnabled) {
					throw new CommandException("commands.debug.notStarted", new Object[0]);
				}

				long var3 = System.currentTimeMillis();
				int var5 = MinecraftServer.getServer().getTickCounter();
				long var6 = var3 - this.startTime;
				int var8 = var5 - this.startTicks;
				this.saveProfilerResults(var6, var8);
				MinecraftServer.getServer().theProfiler.profilingEnabled = false;
				notifyAdmins(par1ICommandSender, "commands.debug.stop",
						new Object[] { Float.valueOf(var6 / 1000.0F), Integer.valueOf(var8) });
				return;
			}
		}

		throw new WrongUsageException("commands.debug.usage", new Object[0]);
	}

	private void saveProfilerResults(long par1, int par3) {
		
		// no
		
//		File var4 = new File(MinecraftServer.getServer().getFile("debug"),
//				"profile-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
//		var4.getParentFile().mkdirs();
//
//		try {
//			FileWriter var5 = new FileWriter(var4);
//			var5.write(this.getProfilerResults(par1, par3));
//			var5.close();
//		} catch (Throwable var6) {
//			MinecraftServer.getServer().getLogAgent().logSevereException("Could not save profiler results to " + var4,
//					var6);
//		}
	}

	private String getProfilerResults(long par1, int par3) {
		StringBuilder var4 = new StringBuilder();
		var4.append("---- Minecraft Profiler Results ----\n");
		var4.append("// ");
		var4.append(getWittyComment());
		var4.append("\n\n");
		var4.append("Time span: ").append(par1).append(" ms\n");
		var4.append("Tick span: ").append(par3).append(" ticks\n");
		var4.append("// This is approximately ")
				.append(String.format("%s", new Object[] { Float.valueOf((float) par3 / ((float) par1 / 1000.0F)) }))
				.append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
		var4.append("--- BEGIN PROFILE DUMP ---\n\n");
		this.getProfileDump(0, "root", var4);
		var4.append("--- END PROFILE DUMP ---\n\n");
		return var4.toString();
	}

	private void getProfileDump(int par1, String par2Str, StringBuilder par3StringBuilder) {
		List var4 = MinecraftServer.getServer().theProfiler.getProfilingData(par2Str);

		if (var4 != null && var4.size() >= 3) {
			for (int var5 = 1; var5 < var4.size(); ++var5) {
				ProfilerResult var6 = (ProfilerResult) var4.get(var5);
				par3StringBuilder.append(String.format("[%d] ", new Object[] { Integer.valueOf(par1) }));

				for (int var7 = 0; var7 < par1; ++var7) {
					par3StringBuilder.append(" ");
				}

				par3StringBuilder.append(var6.field_76331_c);
				par3StringBuilder.append(" - ");
				par3StringBuilder.append(String.format("%s", new Object[] { Double.valueOf(var6.field_76332_a) }));
				par3StringBuilder.append("%/");
				par3StringBuilder.append(String.format("%s", new Object[] { Double.valueOf(var6.field_76330_b) }));
				par3StringBuilder.append("%\n");

				if (!var6.field_76331_c.equals("unspecified")) {
					try {
						this.getProfileDump(par1 + 1, par2Str + "." + var6.field_76331_c, par3StringBuilder);
					} catch (Exception var8) {
						par3StringBuilder.append("[[ EXCEPTION " + var8 + " ]]");
					}
				}
			}
		}
	}

	/**
	 * Returns a random "witty" comment.
	 */
	private static String getWittyComment() {
		String[] var0 = new String[] { "Shiny numbers!", "Am I not running fast enough? :(",
				"I\'m working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!",
				"Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers",
				"Now with the same numbers", "You should add flames to things, it makes them go faster!",
				"Do you feel the need for... optimization?", "*cracks redstone whip*",
				"Maybe if you treated it better then it\'ll have more motivation to work faster! Poor server." };

		try {
			return var0[(int) (System.nanoTime() % (long) var0.length)];
		} catch (Throwable var2) {
			return "Witty comment unavailable :(";
		}
	}

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return par2ArrayOfStr.length == 1
				? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] { "start", "stop" })
				: null;
	}
}
