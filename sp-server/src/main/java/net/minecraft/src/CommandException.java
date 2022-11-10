package net.minecraft.src;

public class CommandException extends RuntimeException {
	private Object[] errorObjects;

	public CommandException(String par1Str, Object... par2ArrayOfObj) {
		super(par1Str);
		this.errorObjects = par2ArrayOfObj;
	}

	public Object[] getErrorOjbects() {
		return this.errorObjects;
	}
}
