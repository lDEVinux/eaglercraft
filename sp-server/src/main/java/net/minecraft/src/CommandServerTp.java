package net.minecraft.src;

import java.util.List;

import net.minecraft.server.MinecraftServer;

public class CommandServerTp extends CommandBase {
	public String getCommandName() {
		return "tp";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return par1ICommandSender.translateString("commands.tp.usage", new Object[0]);
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length < 1) {
			throw new WrongUsageException("commands.tp.usage", new Object[0]);
		} else {
			EntityPlayerMP var3;

			if (par2ArrayOfStr.length != 2 && par2ArrayOfStr.length != 4) {
				var3 = getCommandSenderAsPlayer(par1ICommandSender);
			} else {
				var3 = func_82359_c(par1ICommandSender, par2ArrayOfStr[0]);

				if (var3 == null) {
					throw new PlayerNotFoundException();
				}
			}

			if (par2ArrayOfStr.length != 3 && par2ArrayOfStr.length != 4) {
				if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2) {
					EntityPlayerMP var11 = func_82359_c(par1ICommandSender, par2ArrayOfStr[par2ArrayOfStr.length - 1]);

					if (var11 == null) {
						throw new PlayerNotFoundException();
					}

					// var3.mountEntity((Entity) null);

					if (var11.worldObj != var3.worldObj) {
						notifyAdmins(par1ICommandSender, "commands.tp.notSameDimension", new Object[0]);
						return;
						// var3.mcServer.getConfigurationManager().transferPlayerToDimension(var3, var11.dimension);
						// var3.playerNetServerHandler.playerEntity = var3.mcServer.getConfigurationManager().recreatePlayerEntity(var3, var11.dimension, true, false);
						// var3 = var3.playerNetServerHandler.playerEntity;
					}

					var3.mountEntity((Entity) null);
					
					var3.playerNetServerHandler.setPlayerLocation(var11.posX, var11.posY, var11.posZ, var11.rotationYaw,
							var11.rotationPitch);
					notifyAdmins(par1ICommandSender, "commands.tp.success",
							new Object[] { var3.getEntityName(), var11.getEntityName() });
				}
			} else if (var3.worldObj != null) {
				int var4 = par2ArrayOfStr.length - 3;
				double var5 = this.func_82368_a(par1ICommandSender, var3.posX, par2ArrayOfStr[var4++]);
				double var7 = this.func_82367_a(par1ICommandSender, var3.posY, par2ArrayOfStr[var4++], 0, 0);
				double var9 = this.func_82368_a(par1ICommandSender, var3.posZ, par2ArrayOfStr[var4++]);
				var3.mountEntity((Entity) null);
				var3.setPositionAndUpdate(var5, var7, var9);
				notifyAdmins(par1ICommandSender, "commands.tp.success.coordinates", new Object[] { var3.getEntityName(),
						Double.valueOf(var5), Double.valueOf(var7), Double.valueOf(var9) });
			}
		}
	}

	private double func_82368_a(ICommandSender par1ICommandSender, double par2, String par4Str) {
		return this.func_82367_a(par1ICommandSender, par2, par4Str, -30000000, 30000000);
	}

	private double func_82367_a(ICommandSender par1ICommandSender, double par2, String par4Str, int par5, int par6) {
		boolean var7 = par4Str.startsWith("~");
		double var8 = var7 ? par2 : 0.0D;

		if (!var7 || par4Str.length() > 1) {
			boolean var10 = par4Str.contains(".");

			if (var7) {
				par4Str = par4Str.substring(1);
			}

			var8 += parseDouble(par1ICommandSender, par4Str);

			if (!var10 && !var7) {
				var8 += 0.5D;
			}
		}

		if (par5 != 0 || par6 != 0) {
			if (var8 < (double) par5) {
				throw new NumberInvalidException("commands.generic.double.tooSmall",
						new Object[] { Double.valueOf(var8), Integer.valueOf(par5) });
			}

			if (var8 > (double) par6) {
				throw new NumberInvalidException("commands.generic.double.tooBig",
						new Object[] { Double.valueOf(var8), Integer.valueOf(par6) });
			}
		}

		return var8;
	}

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return par2ArrayOfStr.length != 1 && par2ArrayOfStr.length != 2 ? null
				: getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
		return par2 == 0;
	}
}
