// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;

public class Plugin {
	private PluginDescription description;
	private ProxyServer proxy;
	private File file;
	private Logger logger;
	
	protected Plugin() {
	}
	
	protected Plugin(PluginDescription forceDesc) {
		this.description = forceDesc;
	}

	public void onLoad() {
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	public final File getDataFolder() {
		return new File(this.getProxy().getPluginsFolder(), this.getDescription().getName());
	}

	public final InputStream getResourceAsStream(final String name) {
		return this.getClass().getClassLoader().getResourceAsStream(name);
	}

	final void init(final ProxyServer proxy, final PluginDescription description) {
		this.proxy = proxy;
		this.description = (this.description == null ? description : this.description);
		this.file = description.getFile();
		this.logger = new PluginLogger(this);
	}

	public PluginDescription getDescription() {
		return this.description;
	}

	public ProxyServer getProxy() {
		return this.proxy;
	}

	public File getFile() {
		return this.file;
	}

	public Logger getLogger() {
		return this.logger;
	}
}
