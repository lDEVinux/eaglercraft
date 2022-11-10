package net.minecraft.src;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogAgent implements ILogAgent {
	private final Logger serverLogger;
	private final String logFile;
	private final String loggerName;
	private final String loggerPrefix;

	public LogAgent(String par1Str, String par2Str, String par3Str) {
		this.serverLogger = Logger.getLogger(par1Str);
		this.loggerName = par1Str;
		this.loggerPrefix = par2Str;
		this.logFile = par3Str;
		this.setupLogger();
	}

	/**
	 * Sets up the logger for usage.
	 */
	private void setupLogger() {
		this.serverLogger.setUseParentHandlers(false);
		Handler[] var1 = this.serverLogger.getHandlers();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			Handler var4 = var1[var3];
			this.serverLogger.removeHandler(var4);
		}

		LogFormatter var6 = new LogFormatter(this, (LogAgentINNER1) null);
		ConsoleHandler var7 = new ConsoleHandler();
		var7.setFormatter(var6);
		this.serverLogger.addHandler(var7);

		try {
			FileHandler var8 = new FileHandler(this.logFile, true);
			var8.setFormatter(var6);
			this.serverLogger.addHandler(var8);
		} catch (Exception var5) {
			this.serverLogger.log(Level.WARNING, "Failed to log " + this.loggerName + " to " + this.logFile, var5);
		}
	}

	public Logger getServerLogger() {
		return this.serverLogger;
	}

	public void func_98233_a(String par1Str) {
		this.serverLogger.log(Level.INFO, par1Str);
	}

	public void func_98236_b(String par1Str) {
		this.serverLogger.log(Level.WARNING, par1Str);
	}

	public void logWarningFormatted(String par1Str, Object... par2ArrayOfObj) {
		this.serverLogger.log(Level.WARNING, par1Str, par2ArrayOfObj);
	}

	public void logWarningException(String par1Str, Throwable par2Throwable) {
		this.serverLogger.log(Level.WARNING, par1Str, par2Throwable);
	}

	public void logSevere(String par1Str) {
		this.serverLogger.log(Level.SEVERE, par1Str);
	}

	public void logSevereException(String par1Str, Throwable par2Throwable) {
		this.serverLogger.log(Level.SEVERE, par1Str, par2Throwable);
	}

	static String func_98237_a(LogAgent par0LogAgent) {
		return par0LogAgent.loggerPrefix;
	}
}
