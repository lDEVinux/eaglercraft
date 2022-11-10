package net.minecraft.src;

public class CommandKill extends CommandBase {
	public String getCommandName() {
		return "kill";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 0;
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		EntityPlayerMP var3 = getCommandSenderAsPlayer(par1ICommandSender);
		var3.attackEntityFrom(DamageSource.outOfWorld, 1000);
		par1ICommandSender.sendChatToPlayer("Ouch. That looks like it hurt.");
	}
}
