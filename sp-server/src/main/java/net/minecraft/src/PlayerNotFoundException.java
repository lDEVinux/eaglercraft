package net.minecraft.src;

public class PlayerNotFoundException extends CommandException {
	public PlayerNotFoundException() {
		this("commands.generic.player.notFound", new Object[0]);
	}

	public PlayerNotFoundException(String par1Str, Object... par2ArrayOfObj) {
		super(par1Str, par2ArrayOfObj);
	}
}
