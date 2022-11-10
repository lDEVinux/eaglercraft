package net.minecraft.src;

public class ServerCommand {
	/** The command string. */
	public final String command;
	public final ICommandSender sender;

	public ServerCommand(String par1Str, ICommandSender par2ICommandSender) {
		this.command = par1Str;
		this.sender = par2ICommandSender;
	}
}
