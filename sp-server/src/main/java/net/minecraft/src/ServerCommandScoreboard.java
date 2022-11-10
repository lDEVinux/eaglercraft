package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.server.MinecraftServer;

public class ServerCommandScoreboard extends CommandBase {
	public String getCommandName() {
		return "scoreboard";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length >= 1) {
			if (par2ArrayOfStr[0].equalsIgnoreCase("objectives")) {
				if (par2ArrayOfStr.length == 1) {
					throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
				}

				if (par2ArrayOfStr[1].equalsIgnoreCase("list")) {
					this.getObjectivesList(par1ICommandSender);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("add")) {
					if (par2ArrayOfStr.length < 4) {
						throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
					}

					this.addObjective(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("remove")) {
					if (par2ArrayOfStr.length != 3) {
						throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
					}

					this.removeObjective(par1ICommandSender, par2ArrayOfStr[2]);
				} else {
					if (!par2ArrayOfStr[1].equalsIgnoreCase("setdisplay")) {
						throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
					}

					if (par2ArrayOfStr.length != 3 && par2ArrayOfStr.length != 4) {
						throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
					}

					this.setObjectivesDisplay(par1ICommandSender, par2ArrayOfStr, 2);
				}

				return;
			}

			if (par2ArrayOfStr[0].equalsIgnoreCase("players")) {
				if (par2ArrayOfStr.length == 1) {
					throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
				}

				if (par2ArrayOfStr[1].equalsIgnoreCase("list")) {
					if (par2ArrayOfStr.length > 3) {
						throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
					}

					this.listPlayers(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("add")) {
					if (par2ArrayOfStr.length != 5) {
						throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
					}

					this.setPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("remove")) {
					if (par2ArrayOfStr.length != 5) {
						throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
					}

					this.setPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("set")) {
					if (par2ArrayOfStr.length != 5) {
						throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
					}

					this.setPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
				} else {
					if (!par2ArrayOfStr[1].equalsIgnoreCase("reset")) {
						throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
					}

					if (par2ArrayOfStr.length != 3) {
						throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
					}

					this.resetPlayerScore(par1ICommandSender, par2ArrayOfStr, 2);
				}

				return;
			}

			if (par2ArrayOfStr[0].equalsIgnoreCase("teams")) {
				if (par2ArrayOfStr.length == 1) {
					throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
				}

				if (par2ArrayOfStr[1].equalsIgnoreCase("list")) {
					if (par2ArrayOfStr.length > 3) {
						throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
					}

					this.getTeamList(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("add")) {
					if (par2ArrayOfStr.length < 3) {
						throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
					}

					this.addTeam(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("remove")) {
					if (par2ArrayOfStr.length != 3) {
						throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
					}

					this.removeTeam(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("empty")) {
					if (par2ArrayOfStr.length != 3) {
						throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
					}

					this.emptyTeam(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("join")) {
					if (par2ArrayOfStr.length < 4
							&& (par2ArrayOfStr.length != 3 || !(par1ICommandSender instanceof EntityPlayer))) {
						throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
					}

					this.joinTeam(par1ICommandSender, par2ArrayOfStr, 2);
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("leave")) {
					if (par2ArrayOfStr.length < 3 && !(par1ICommandSender instanceof EntityPlayer)) {
						throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
					}

					this.leaveTeam(par1ICommandSender, par2ArrayOfStr, 2);
				} else {
					if (!par2ArrayOfStr[1].equalsIgnoreCase("option")) {
						throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
					}

					if (par2ArrayOfStr.length != 4 && par2ArrayOfStr.length != 5) {
						throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
					}

					this.setTeamOption(par1ICommandSender, par2ArrayOfStr, 2);
				}

				return;
			}
		}

		throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
	}

	protected Scoreboard getScoreboardFromWorldServer() {
		return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
	}

	/**
	 * User-safe version of Scoreboard.getObjective, does checks against the
	 * objective not being found and whether it's read-only or not. If true, the
	 * second parameter makes the function throw an exception if the objective is
	 * read- only.
	 */
	protected ScoreObjective getScoreObjective(String par1Str, boolean par2) {
		Scoreboard var3 = this.getScoreboardFromWorldServer();
		ScoreObjective var4 = var3.getObjective(par1Str);

		if (var4 == null) {
			throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { par1Str });
		} else if (par2 && var4.getCriteria().isReadOnly()) {
			throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { par1Str });
		} else {
			return var4;
		}
	}

	/**
	 * Returns the ScorePlayerTeam for the given team name.
	 */
	protected ScorePlayerTeam getTeam(String par1Str) {
		Scoreboard var2 = this.getScoreboardFromWorldServer();
		ScorePlayerTeam var3 = var2.func_96508_e(par1Str);

		if (var3 == null) {
			throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { par1Str });
		} else {
			return var3;
		}
	}

	/**
	 * Handler for the 'scoreboard objectives add' command.
	 */
	protected void addObjective(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		String var4 = par2ArrayOfStr[par3++];
		String var5 = par2ArrayOfStr[par3++];
		Scoreboard var6 = this.getScoreboardFromWorldServer();
		ScoreObjectiveCriteria var7 = (ScoreObjectiveCriteria) ScoreObjectiveCriteria.field_96643_a.get(var5);

		if (var7 == null) {
			String[] var10 = (String[]) ScoreObjectiveCriteria.field_96643_a.keySet().toArray(new String[0]);
			throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType",
					new Object[] { joinNiceString(var10) });
		} else if (var6.getObjective(var4) != null) {
			throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { var4 });
		} else if (var4.length() > 16) {
			throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong",
					new Object[] { var4, Integer.valueOf(16) });
		} else {
			ScoreObjective var8 = var6.func_96535_a(var4, var7);

			if (par2ArrayOfStr.length > par3) {
				String var9 = func_82360_a(par1ICommandSender, par2ArrayOfStr, par3);

				if (var9.length() > 32) {
					throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong",
							new Object[] { var9, Integer.valueOf(32) });
				}

				if (var9.length() > 0) {
					var8.setDisplayName(var9);
				}
			}

			notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.add.success", new Object[] { var4 });
		}
	}

	/**
	 * Handler for the 'scoreboard teams add' command.
	 */
	protected void addTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		String var4 = par2ArrayOfStr[par3++];
		Scoreboard var5 = this.getScoreboardFromWorldServer();

		if (var5.func_96508_e(var4) != null) {
			throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { var4 });
		} else if (var4.length() > 16) {
			throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong",
					new Object[] { var4, Integer.valueOf(16) });
		} else {
			ScorePlayerTeam var6 = var5.func_96527_f(var4);

			if (par2ArrayOfStr.length > par3) {
				String var7 = func_82360_a(par1ICommandSender, par2ArrayOfStr, par3);

				if (var7.length() > 32) {
					throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong",
							new Object[] { var7, Integer.valueOf(32) });
				}

				if (var7.length() > 0) {
					var6.func_96664_a(var7);
				}
			}

			notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.add.success", new Object[] { var4 });
		}
	}

	/**
	 * Handler for the 'scoreboard teams option' command.
	 */
	protected void setTeamOption(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		ScorePlayerTeam var4 = this.getTeam(par2ArrayOfStr[par3++]);
		String var5 = par2ArrayOfStr[par3++].toLowerCase();

		if (!var5.equalsIgnoreCase("color") && !var5.equalsIgnoreCase("friendlyfire")
				&& !var5.equalsIgnoreCase("seeFriendlyInvisibles")) {
			throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
		} else if (par2ArrayOfStr.length == 4) {
			if (var5.equalsIgnoreCase("color")) {
				throw new WrongUsageException("commands.scoreboard.teams.option.noValue",
						new Object[] { var5, func_96333_a(EnumChatFormatting.func_96296_a(true, false)) });
			} else if (!var5.equalsIgnoreCase("friendlyfire") && !var5.equalsIgnoreCase("seeFriendlyInvisibles")) {
				throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
			} else {
				throw new WrongUsageException("commands.scoreboard.teams.option.noValue",
						new Object[] { var5, func_96333_a(Arrays.asList(new String[] { "true", "false" })) });
			}
		} else {
			String var6 = par2ArrayOfStr[par3++];

			if (var5.equalsIgnoreCase("color")) {
				EnumChatFormatting var7 = EnumChatFormatting.func_96300_b(var6);

				if (var6 == null) {
					throw new WrongUsageException("commands.scoreboard.teams.option.noValue",
							new Object[] { var5, func_96333_a(EnumChatFormatting.func_96296_a(true, false)) });
				}

				var4.func_96666_b(var7.toString());
				var4.func_96662_c(EnumChatFormatting.RESET.toString());
			} else if (var5.equalsIgnoreCase("friendlyfire")) {
				if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false")) {
					throw new WrongUsageException("commands.scoreboard.teams.option.noValue",
							new Object[] { var5, func_96333_a(Arrays.asList(new String[] { "true", "false" })) });
				}

				var4.func_96660_a(var6.equalsIgnoreCase("true"));
			} else if (var5.equalsIgnoreCase("seeFriendlyInvisibles")) {
				if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false")) {
					throw new WrongUsageException("commands.scoreboard.teams.option.noValue",
							new Object[] { var5, func_96333_a(Arrays.asList(new String[] { "true", "false" })) });
				}

				var4.func_98300_b(var6.equalsIgnoreCase("true"));
			}

			notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.option.success",
					new Object[] { var5, var4.func_96661_b(), var6 });
		}
	}

	/**
	 * Handler for the 'scoreboard teams remove' command.
	 */
	protected void removeTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();
		ScorePlayerTeam var5 = this.getTeam(par2ArrayOfStr[par3++]);
		var4.func_96511_d(var5);
		notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.remove.success",
				new Object[] { var5.func_96661_b() });
	}

	/**
	 * Handler for the 'scoreboard teams list' command.
	 */
	protected void getTeamList(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();

		if (par2ArrayOfStr.length > par3) {
			ScorePlayerTeam var5 = this.getTeam(par2ArrayOfStr[par3++]);
			Collection var6 = var5.getMembershipCollection();

			if (var6.size() <= 0) {
				throw new CommandException("commands.scoreboard.teams.list.player.empty",
						new Object[] { var5.func_96661_b() });
			}

			par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN
					+ par1ICommandSender.translateString("commands.scoreboard.teams.list.player.count",
							new Object[] { Integer.valueOf(var6.size()), var5.func_96661_b() }));
			par1ICommandSender.sendChatToPlayer(joinNiceString(var6.toArray()));
		} else {
			Collection var8 = var4.func_96525_g();

			if (var8.size() <= 0) {
				throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
			}

			par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString(
					"commands.scoreboard.teams.list.count", new Object[] { Integer.valueOf(var8.size()) }));
			Iterator var9 = var8.iterator();

			while (var9.hasNext()) {
				ScorePlayerTeam var7 = (ScorePlayerTeam) var9.next();
				par1ICommandSender.sendChatToPlayer(par1ICommandSender
						.translateString("commands.scoreboard.teams.list.entry", new Object[] { var7.func_96661_b(),
								var7.func_96669_c(), Integer.valueOf(var7.getMembershipCollection().size()) }));
			}
		}
	}

	/**
	 * Handler for the 'scoreboard teams join' command.
	 */
	protected void joinTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();
		ScorePlayerTeam var5 = var4.func_96508_e(par2ArrayOfStr[par3++]);
		HashSet var6 = new HashSet();
		String var7;

		if (par1ICommandSender instanceof EntityPlayer && par3 == par2ArrayOfStr.length) {
			var7 = getCommandSenderAsPlayer(par1ICommandSender).getEntityName();
			var4.func_96521_a(var7, var5);
			var6.add(var7);
		} else {
			while (par3 < par2ArrayOfStr.length) {
				var7 = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
				var4.func_96521_a(var7, var5);
				var6.add(var7);
			}
		}

		if (!var6.isEmpty()) {
			notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.join.success", new Object[] {
					Integer.valueOf(var6.size()), var5.func_96661_b(), joinNiceString(var6.toArray(new String[0])) });
		}
	}

	/**
	 * Handler for the 'scoreboard teams leave' command.
	 */
	protected void leaveTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();
		HashSet var5 = new HashSet();
		HashSet var6 = new HashSet();
		String var7;

		if (par1ICommandSender instanceof EntityPlayer && par3 == par2ArrayOfStr.length) {
			var7 = getCommandSenderAsPlayer(par1ICommandSender).getEntityName();

			if (var4.func_96524_g(var7)) {
				var5.add(var7);
			} else {
				var6.add(var7);
			}
		} else {
			while (par3 < par2ArrayOfStr.length) {
				var7 = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);

				if (var4.func_96524_g(var7)) {
					var5.add(var7);
				} else {
					var6.add(var7);
				}
			}
		}

		if (!var5.isEmpty()) {
			notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.leave.success",
					new Object[] { Integer.valueOf(var5.size()), joinNiceString(var5.toArray(new String[0])) });
		}

		if (!var6.isEmpty()) {
			throw new CommandException("commands.scoreboard.teams.leave.failure",
					new Object[] { Integer.valueOf(var6.size()), joinNiceString(var6.toArray(new String[0])) });
		}
	}

	/**
	 * Handler for the 'scoreboard teams empty' command.
	 */
	protected void emptyTeam(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();
		ScorePlayerTeam var5 = this.getTeam(par2ArrayOfStr[par3++]);
		ArrayList var6 = new ArrayList(var5.getMembershipCollection());

		if (var6.isEmpty()) {
			throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty",
					new Object[] { var5.func_96661_b() });
		} else {
			Iterator var7 = var6.iterator();

			while (var7.hasNext()) {
				String var8 = (String) var7.next();
				var4.removePlayerFromTeam(var8, var5);
			}

			notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.empty.success",
					new Object[] { Integer.valueOf(var6.size()), var5.func_96661_b() });
		}
	}

	/**
	 * Handler for the 'scoreboard objectives remove' command.
	 */
	protected void removeObjective(ICommandSender par1ICommandSender, String par2Str) {
		Scoreboard var3 = this.getScoreboardFromWorldServer();
		ScoreObjective var4 = this.getScoreObjective(par2Str, false);
		var3.func_96519_k(var4);
		notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.remove.success", new Object[] { par2Str });
	}

	/**
	 * Handler for the 'scoreboard objectives list' command.
	 */
	protected void getObjectivesList(ICommandSender par1ICommandSender) {
		Scoreboard var2 = this.getScoreboardFromWorldServer();
		Collection var3 = var2.getScoreObjectives();

		if (var3.size() <= 0) {
			throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
		} else {
			par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString(
					"commands.scoreboard.objectives.list.count", new Object[] { Integer.valueOf(var3.size()) }));
			Iterator var4 = var3.iterator();

			while (var4.hasNext()) {
				ScoreObjective var5 = (ScoreObjective) var4.next();
				par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString(
						"commands.scoreboard.objectives.list.entry",
						new Object[] { var5.getName(), var5.getDisplayName(), var5.getCriteria().func_96636_a() }));
			}
		}
	}

	/**
	 * Handler for the 'scoreboard objectives setdisplay' command.
	 */
	protected void setObjectivesDisplay(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();
		String var5 = par2ArrayOfStr[par3++];
		int var6 = Scoreboard.getObjectiveDisplaySlotNumber(var5);
		ScoreObjective var7 = null;

		if (par2ArrayOfStr.length == 4) {
			var7 = this.getScoreObjective(par2ArrayOfStr[par3++], false);
		}

		if (var6 < 0) {
			throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { var5 });
		} else {
			var4.func_96530_a(var6, var7);

			if (var7 != null) {
				notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.setdisplay.successSet",
						new Object[] { Scoreboard.getObjectiveDisplaySlot(var6), var7.getName() });
			} else {
				notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.setdisplay.successCleared",
						new Object[] { Scoreboard.getObjectiveDisplaySlot(var6) });
			}
		}
	}

	/**
	 * Handler for the 'scoreboard players list' command.
	 */
	protected void listPlayers(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();

		if (par2ArrayOfStr.length > par3) {
			String var5 = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
			Map var6 = var4.func_96510_d(var5);

			if (var6.size() <= 0) {
				throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { var5 });
			}

			par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN
					+ par1ICommandSender.translateString("commands.scoreboard.players.list.player.count",
							new Object[] { Integer.valueOf(var6.size()), var5 }));
			Iterator var7 = var6.values().iterator();

			while (var7.hasNext()) {
				Score var8 = (Score) var7.next();
				par1ICommandSender.sendChatToPlayer(
						par1ICommandSender.translateString("commands.scoreboard.players.list.player.entry",
								new Object[] { Integer.valueOf(var8.func_96652_c()),
										var8.func_96645_d().getDisplayName(), var8.func_96645_d().getName() }));
			}
		} else {
			Collection var9 = var4.getObjectiveNames();

			if (var9.size() <= 0) {
				throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
			}

			par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString(
					"commands.scoreboard.players.list.count", new Object[] { Integer.valueOf(var9.size()) }));
			par1ICommandSender.sendChatToPlayer(joinNiceString(var9.toArray()));
		}
	}

	/**
	 * Handler for the 'scoreboard players [add|remove|set]' commands.
	 */
	protected void setPlayerScore(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		String var4 = par2ArrayOfStr[par3 - 1];
		String var5 = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
		ScoreObjective var6 = this.getScoreObjective(par2ArrayOfStr[par3++], true);
		int var7 = var4.equalsIgnoreCase("set") ? parseInt(par1ICommandSender, par2ArrayOfStr[par3++])
				: parseIntWithMin(par1ICommandSender, par2ArrayOfStr[par3++], 1);
		Scoreboard var8 = this.getScoreboardFromWorldServer();
		Score var9 = var8.func_96529_a(var5, var6);

		if (var4.equalsIgnoreCase("set")) {
			var9.func_96647_c(var7);
		} else if (var4.equalsIgnoreCase("add")) {
			var9.func_96649_a(var7);
		} else {
			var9.func_96646_b(var7);
		}

		notifyAdmins(par1ICommandSender, "commands.scoreboard.players.set.success",
				new Object[] { var6.getName(), var5, Integer.valueOf(var9.func_96652_c()) });
	}

	/**
	 * Handler for the 'scoreboard players reset' command.
	 */
	protected void resetPlayerScore(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3) {
		Scoreboard var4 = this.getScoreboardFromWorldServer();
		String var5 = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
		var4.func_96515_c(var5);
		notifyAdmins(par1ICommandSender, "commands.scoreboard.players.reset.success", new Object[] { var5 });
	}

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length == 1) {
			return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] { "objectives", "players", "teams" });
		} else {
			if (par2ArrayOfStr[0].equalsIgnoreCase("objectives")) {
				if (par2ArrayOfStr.length == 2) {
					return getListOfStringsMatchingLastWord(par2ArrayOfStr,
							new String[] { "list", "add", "remove", "setdisplay" });
				}

				if (par2ArrayOfStr[1].equalsIgnoreCase("add")) {
					if (par2ArrayOfStr.length == 4) {
						return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
								ScoreObjectiveCriteria.field_96643_a.keySet());
					}
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("remove")) {
					if (par2ArrayOfStr.length == 3) {
						return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
								this.getScoreObjectivesList(false));
					}
				} else if (par2ArrayOfStr[1].equalsIgnoreCase("setdisplay")) {
					if (par2ArrayOfStr.length == 3) {
						return getListOfStringsMatchingLastWord(par2ArrayOfStr,
								new String[] { "list", "sidebar", "belowName" });
					}

					if (par2ArrayOfStr.length == 4) {
						return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
								this.getScoreObjectivesList(false));
					}
				}
			} else if (par2ArrayOfStr[0].equalsIgnoreCase("players")) {
				if (par2ArrayOfStr.length == 2) {
					return getListOfStringsMatchingLastWord(par2ArrayOfStr,
							new String[] { "set", "add", "remove", "reset", "list" });
				}

				if (!par2ArrayOfStr[1].equalsIgnoreCase("set") && !par2ArrayOfStr[1].equalsIgnoreCase("add")
						&& !par2ArrayOfStr[1].equalsIgnoreCase("remove")) {
					if ((par2ArrayOfStr[1].equalsIgnoreCase("reset") || par2ArrayOfStr[1].equalsIgnoreCase("list"))
							&& par2ArrayOfStr.length == 3) {
						return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
								this.getScoreboardFromWorldServer().getObjectiveNames());
					}
				} else {
					if (par2ArrayOfStr.length == 3) {
						return getListOfStringsMatchingLastWord(par2ArrayOfStr,
								MinecraftServer.getServer().getAllUsernames());
					}

					if (par2ArrayOfStr.length == 4) {
						return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
								this.getScoreObjectivesList(true));
					}
				}
			} else if (par2ArrayOfStr[0].equalsIgnoreCase("teams")) {
				if (par2ArrayOfStr.length == 2) {
					return getListOfStringsMatchingLastWord(par2ArrayOfStr,
							new String[] { "add", "remove", "join", "leave", "empty", "list", "option" });
				}

				if (par2ArrayOfStr[1].equalsIgnoreCase("join")) {
					if (par2ArrayOfStr.length == 3) {
						return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
								this.getScoreboardFromWorldServer().func_96531_f());
					}

					if (par2ArrayOfStr.length >= 4) {
						return getListOfStringsMatchingLastWord(par2ArrayOfStr,
								MinecraftServer.getServer().getAllUsernames());
					}
				} else {
					if (par2ArrayOfStr[1].equalsIgnoreCase("leave")) {
						return getListOfStringsMatchingLastWord(par2ArrayOfStr,
								MinecraftServer.getServer().getAllUsernames());
					}

					if (!par2ArrayOfStr[1].equalsIgnoreCase("empty") && !par2ArrayOfStr[1].equalsIgnoreCase("list")
							&& !par2ArrayOfStr[1].equalsIgnoreCase("remove")) {
						if (par2ArrayOfStr[1].equalsIgnoreCase("option")) {
							if (par2ArrayOfStr.length == 3) {
								return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
										this.getScoreboardFromWorldServer().func_96531_f());
							}

							if (par2ArrayOfStr.length == 4) {
								return getListOfStringsMatchingLastWord(par2ArrayOfStr,
										new String[] { "color", "friendlyfire", "seeFriendlyInvisibles" });
							}

							if (par2ArrayOfStr.length == 5) {
								if (par2ArrayOfStr[3].equalsIgnoreCase("color")) {
									return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
											EnumChatFormatting.func_96296_a(true, false));
								}

								if (par2ArrayOfStr[3].equalsIgnoreCase("friendlyfire")
										|| par2ArrayOfStr[3].equalsIgnoreCase("seeFriendlyInvisibles")) {
									return getListOfStringsMatchingLastWord(par2ArrayOfStr,
											new String[] { "true", "false" });
								}
							}
						}
					} else if (par2ArrayOfStr.length == 3) {
						return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr,
								this.getScoreboardFromWorldServer().func_96531_f());
					}
				}
			}

			return null;
		}
	}

	/**
	 * If the parameter is true, does not return read-only entries.
	 */
	protected List getScoreObjectivesList(boolean par1) {
		Collection var2 = this.getScoreboardFromWorldServer().getScoreObjectives();
		ArrayList var3 = new ArrayList();
		Iterator var4 = var2.iterator();

		while (var4.hasNext()) {
			ScoreObjective var5 = (ScoreObjective) var4.next();

			if (!par1 || !var5.getCriteria().isReadOnly()) {
				var3.add(var5.getName());
			}
		}

		return var3;
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
		return par1ArrayOfStr[0].equalsIgnoreCase("players") ? par2 == 2
				: (!par1ArrayOfStr[0].equalsIgnoreCase("teams") ? false : par2 == 2 || par2 == 3);
	}
}
