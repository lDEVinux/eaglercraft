// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.plugin;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;

public class PluginLogger extends Logger {
	private String pluginName;

	protected PluginLogger(final Plugin plugin) {
		super(plugin.getClass().getCanonicalName(), null);
		this.pluginName = "[" + plugin.getDescription().getName() + "] ";
		this.setParent(ProxyServer.getInstance().getLogger());
	}

	@Override
	public void log(final LogRecord logRecord) {
		logRecord.setMessage(this.pluginName + logRecord.getMessage());
		super.log(logRecord);
	}
}
