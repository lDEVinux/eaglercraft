package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandShowSeed extends CommandBase {
	/**
	 * Returns true if the given command sender is allowed to use this command.
	 */
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
		return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(par1ICommandSender);
	}

	public String getCommandName() {
		return "seed";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		Object var3 = par1ICommandSender instanceof EntityPlayer ? ((EntityPlayer) par1ICommandSender).worldObj
				: MinecraftServer.getServer().worldServerForDimension(0);
		par1ICommandSender.sendChatToPlayer("Seed: " + ((World) var3).getSeed());
	}
}
