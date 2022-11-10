package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandToggleDownfall extends CommandBase {
	public String getCommandName() {
		return "toggledownfall";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		this.toggleDownfall();
		notifyAdmins(par1ICommandSender, "commands.downfall.success", new Object[0]);
	}

	/**
	 * Toggle rain and enable thundering.
	 */
	protected void toggleDownfall() {
		MinecraftServer.getServer().worldServers[0].commandToggleDownfall();
		MinecraftServer.getServer().worldServers[0].getWorldInfo().setThundering(true);
	}
}
