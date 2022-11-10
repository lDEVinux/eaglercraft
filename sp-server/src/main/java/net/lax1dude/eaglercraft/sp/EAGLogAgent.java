package net.lax1dude.eaglercraft.sp;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.src.ILogAgent;

public class EAGLogAgent implements ILogAgent {
	
	private final Logger logger = Logger.getLogger("IntegratedServer");

	public Logger getServerLogger() {
		return this.logger;
	}

	public void func_98233_a(String par1Str) {
		this.logger.log(Level.INFO, par1Str);
	}

	public void func_98236_b(String par1Str) {
		this.logger.log(Level.WARNING, par1Str);
	}

	public void logWarningFormatted(String par1Str, Object... par2ArrayOfObj) {
		this.logger.log(Level.WARNING, par1Str, par2ArrayOfObj);
	}

	public void logWarningException(String par1Str, Throwable par2Throwable) {
		this.logger.log(Level.WARNING, par1Str, par2Throwable);
	}

	public void logSevere(String par1Str) {
		this.logger.log(Level.SEVERE, par1Str);
	}

	public void logSevereException(String par1Str, Throwable par2Throwable) {
		this.logger.log(Level.SEVERE, par1Str, par2Throwable);
	}

}
