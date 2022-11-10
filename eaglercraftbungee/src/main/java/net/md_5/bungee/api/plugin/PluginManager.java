// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.plugin;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.command.ConsoleCommandSender;
import net.md_5.bungee.event.EventBus;
import net.md_5.bungee.event.EventHandler;

public class PluginManager {
	private static final Pattern argsSplit;
	private final ProxyServer proxy;
	private final Yaml yaml;
	private final EventBus eventBus;
	private final Map<String, Plugin> plugins;
	private final Map<String, Command> commandMap;
	private Map<String, PluginDescription> toLoad;

	public PluginManager(final ProxyServer proxy) {
		this.yaml = new Yaml();
		this.plugins = new LinkedHashMap<String, Plugin>();
		this.commandMap = new HashMap<String, Command>();
		this.toLoad = new HashMap<String, PluginDescription>();
		this.proxy = proxy;
		this.eventBus = new EventBus(proxy.getLogger(), (Class<? extends Annotation>[]) new Class[] { Subscribe.class, EventHandler.class });
	}

	public void registerCommand(final Plugin plugin, final Command command) {
		this.commandMap.put(command.getName().toLowerCase(), command);
		for (final String alias : command.getAliases()) {
			this.commandMap.put(alias.toLowerCase(), command);
		}
	}

	public void unregisterCommand(final Command command) {
		this.commandMap.values().remove(command);
	}

	public boolean dispatchCommand(final CommandSender sender, final String commandLine) {
		final String[] split = PluginManager.argsSplit.split(commandLine);
		final Command command = this.commandMap.get(split[0].toLowerCase());
		if (command == null) {
			return false;
		}
		if(!(sender instanceof ConsoleCommandSender) && ((BungeeCord)proxy).config.getDisabledCommands().contains(command.getName())) {
			return false;
		}
		final String permission = command.getPermission();
		if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
			sender.sendMessage(this.proxy.getTranslation("no_permission"));
			return true;
		}
		final String[] args = Arrays.copyOfRange(split, 1, split.length);
		try {
			command.execute(sender, args);
		} catch (Exception ex) {
			sender.sendMessage(ChatColor.RED + "An internal error occurred whilst executing this command, please check the console log for details.");
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Error in dispatching command", ex);
		}
		return true;
	}

	public Collection<Plugin> getPlugins() {
		return this.plugins.values();
	}

	public Plugin getPlugin(final String name) {
		return this.plugins.get(name);
	}

	public void loadAndEnablePlugins() {
		final Map<PluginDescription, Boolean> pluginStatuses = new HashMap<PluginDescription, Boolean>();
		for (final Map.Entry<String, PluginDescription> entry : this.toLoad.entrySet()) {
			final PluginDescription plugin = entry.getValue();
			if (!this.enablePlugin(pluginStatuses, new Stack<PluginDescription>(), plugin)) {
				ProxyServer.getInstance().getLogger().warning("Failed to enable " + entry.getKey());
			}
		}
		this.toLoad.clear();
		this.toLoad = null;
		for (final Plugin plugin2 : this.plugins.values()) {
			try {
				plugin2.onEnable();
				ProxyServer.getInstance().getLogger().log(Level.INFO, "Enabled plugin {0} version {1} by {2}",
						new Object[] { plugin2.getDescription().getName(), plugin2.getDescription().getVersion(), plugin2.getDescription().getAuthor() });
			} catch (Throwable t) {
				ProxyServer.getInstance().getLogger().log(Level.WARNING, "Exception encountered when loading plugin: " + plugin2.getDescription().getName(), t);
			}
		}
	}

	private boolean enablePlugin(final Map<PluginDescription, Boolean> pluginStatuses, final Stack<PluginDescription> dependStack, final PluginDescription plugin) {
		if (pluginStatuses.containsKey(plugin)) {
			return pluginStatuses.get(plugin);
		}
		boolean status = true;
		for (final String dependName : plugin.getDepends()) {
			final PluginDescription depend = this.toLoad.get(dependName);
			Boolean dependStatus = (depend != null) ? pluginStatuses.get(depend) : Boolean.FALSE;
			if (dependStatus == null) {
				if (dependStack.contains(depend)) {
					final StringBuilder dependencyGraph = new StringBuilder();
					for (final PluginDescription element : dependStack) {
						dependencyGraph.append(element.getName()).append(" -> ");
					}
					dependencyGraph.append(plugin.getName()).append(" -> ").append(dependName);
					ProxyServer.getInstance().getLogger().log(Level.WARNING, "Circular dependency detected: " + (Object) dependencyGraph);
					status = false;
				} else {
					dependStack.push(plugin);
					dependStatus = this.enablePlugin(pluginStatuses, dependStack, depend);
					dependStack.pop();
				}
			}
			if (dependStatus == Boolean.FALSE) {
				ProxyServer.getInstance().getLogger().log(Level.WARNING, "{0} (required by {1}) is unavailable", new Object[] { depend.getName(), plugin.getName() });
				status = false;
			}
			if (!status) {
				break;
			}
		}
		if (status) {
			try {
				final URLClassLoader loader = new PluginClassloader(new URL[] { plugin.getFile().toURI().toURL() });
				final Class<?> main = loader.loadClass(plugin.getMain());
				final Plugin clazz = (Plugin) main.getDeclaredConstructor((Class<?>[]) new Class[0]).newInstance(new Object[0]);
				clazz.init(this.proxy, plugin);
				this.plugins.put(plugin.getName(), clazz);
				clazz.onLoad();
				ProxyServer.getInstance().getLogger().log(Level.INFO, "Loaded plugin {0} version {1} by {2}", new Object[] { plugin.getName(), plugin.getVersion(), plugin.getAuthor() });
			} catch (Throwable t) {
				this.proxy.getLogger().log(Level.WARNING, "Error enabling plugin " + plugin.getName(), t);
			}
		}
		pluginStatuses.put(plugin, status);
		return status;
	}
	
	public void addInternalPlugin(Plugin plug) {
		this.plugins.put(plug.getDescription().getName(), plug);
		plug.init(proxy, plug.getDescription());
	}

	public void detectPlugins(final File folder) {
		Preconditions.checkNotNull((Object) folder, (Object) "folder");
		Preconditions.checkArgument(folder.isDirectory(), (Object) "Must load from a directory");
		for (final File file : folder.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".jar")) {
				try (final JarFile jar = new JarFile(file)) {
					final JarEntry pdf = jar.getJarEntry("plugin.yml");
					Preconditions.checkNotNull((Object) pdf, (Object) "Plugin must have a plugin.yml");
					try (final InputStream in = jar.getInputStream(pdf)) {
						final PluginDescription desc = (PluginDescription) this.yaml.loadAs(in, (Class) PluginDescription.class);
						desc.setFile(file);
						this.toLoad.put(desc.getName(), desc);
					}
				} catch (Exception ex) {
					ProxyServer.getInstance().getLogger().log(Level.WARNING, "Could not load plugin from file " + file, ex);
				}
			}
		}
	}

	public <T extends Event> T callEvent(final T event) {
		Preconditions.checkNotNull((Object) event, (Object) "event");
		final long start = System.nanoTime();
		this.eventBus.post(event);
		event.postCall();
		final long elapsed = start - System.nanoTime();
		if (elapsed > 250000L) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Event {0} took more {1}ns to process!", new Object[] { event, elapsed });
		}
		return event;
	}

	public void registerListener(final Plugin plugin, final Listener listener) {
		for (final Method method : listener.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent((Class<? extends Annotation>) Subscribe.class)) {
				this.proxy.getLogger().log(Level.WARNING,
						"Listener " + listener + " has registered using depreceated subscribe annotation!" + " Please advice author to update to @EventHandler." + " As a server owner you may safely ignore this.", new Exception());
			}
		}
		this.eventBus.register(listener);
	}

	@ConstructorProperties({ "proxy", "eventBus" })
	public PluginManager(final ProxyServer proxy, final EventBus eventBus) {
		this.yaml = new Yaml();
		this.plugins = new LinkedHashMap<String, Plugin>();
		this.commandMap = new HashMap<String, Command>();
		this.toLoad = new HashMap<String, PluginDescription>();
		this.proxy = proxy;
		this.eventBus = eventBus;
	}

	static {
		argsSplit = Pattern.compile(" ");
	}
}
