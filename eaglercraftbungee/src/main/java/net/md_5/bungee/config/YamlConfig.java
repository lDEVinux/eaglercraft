// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.AuthServiceInfo;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.MOTDCacheConfiguration;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.config.TexturePackInfo;
import net.md_5.bungee.api.tab.TabListHandler;
import net.md_5.bungee.eaglercraft.RedirectServerInfo;
import net.md_5.bungee.eaglercraft.WebSocketRateLimiter;
import net.md_5.bungee.tab.Global;
import net.md_5.bungee.tab.GlobalPing;
import net.md_5.bungee.tab.ServerUnique;
import net.md_5.bungee.util.CaseInsensitiveMap;

public class YamlConfig implements ConfigurationAdapter {
	private Yaml yaml;
	private Map config;
	private final File file;

	public YamlConfig() {
		this.file = new File("config.yml");
	}

	@Override
	public void load() {
		try {
			this.file.createNewFile();
			final DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			this.yaml = new Yaml(options);
			try (final InputStream is = new FileInputStream(this.file)) {
				this.config = (Map) this.yaml.load(is);
			}
			if (this.config == null) {
				this.config = (Map) new CaseInsensitiveMap();
			} else {
				this.config = (Map) new CaseInsensitiveMap(this.config);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Could not load configuration!", ex);
		}
		final Map<String, List<String>> permissions = this.get("permissions", new HashMap<String, List<String>>());
		if (permissions.isEmpty()) {
			permissions.put("default", Arrays.asList("bungeecord.command.server", "bungeecord.command.list", "bungeecord.command.eag.domain", "bungeecord.command.eag.changepassword"));
			permissions.put("admin", Arrays.asList("bungeecord.command.alert", "bungeecord.command.end", "bungeecord.command.ip", "bungeecord.command.reload",
			"bungeecord.command.eag.ban", "bungeecord.command.eag.banwildcard", "bungeecord.command.eag.banip", "bungeecord.command.eag.banregex",
			"bungeecord.command.eag.reloadban", "bungeecord.command.eag.banned", "bungeecord.command.eag.banlist", "bungeecord.command.eag.unban", "bungeecord.command.eag.ratelimit",
			"bungeecord.command.eag.blockdomain", "bungeecord.command.eag.blockdomainname", "bungeecord.command.eag.unblockdomain"));
		} else if (this.get("authservice", new HashMap<String, Object>()).isEmpty() && permissions.containsKey("default") && !permissions.get("default").contains("bungeecord.command.eag.changepassword")) {
			permissions.get("default").add("bungeecord.command.eag.changepassword");
		}
		this.get("groups", new HashMap<String, Object>());
	}

	private <T> T get(final String path, final T def) {
		return this.get(path, def, this.config);
	}

	private <T> T get(final String path, final T def, final Map submap) {
		final int index = path.indexOf(46);
		if (index == -1) {
			Object val = submap.get(path);
			if (val == null && def != null) {
				val = def;
				submap.put(path, def);
				this.save();
			}
			return (T) val;
		}
		final String first = path.substring(0, index);
		final String second = path.substring(index + 1, path.length());
		Map sub = (Map) submap.get(first);
		if (sub == null) {
			sub = new LinkedHashMap();
			submap.put(first, sub);
		}
		return (T) this.get(second, (Object) def, sub);
	}

	private void save() {
		try (final FileWriter wr = new FileWriter(this.file)) {
			this.yaml.dump((Object) this.config, (Writer) wr);
		} catch (IOException ex) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Could not save config", ex);
		}
	}

	@Override
	public int getInt(final String path, final int def) {
		return this.get(path, def);
	}

	@Override
	public String getString(final String path, final String def) {
		return this.get(path, def);
	}

	@Override
	public boolean getBoolean(final String path, final boolean def) {
		return this.get(path, def);
	}

	@Override
	public Map<String, ServerInfo> getServers() {
		final Map<String, HashMap> base = this.get("servers", (Map<String, HashMap>) Collections.singletonMap("lobby", new HashMap()));
		final Map<String, ServerInfo> ret = new HashMap<String, ServerInfo>();
		for (final Map.Entry<String, HashMap> entry : base.entrySet()) {
			final Map<String, Object> val = entry.getValue();
			final String name = entry.getKey();
			if(val.containsKey("redirect")) {
				final String addr = this.get("redirect", "ws://someOtherServer/", val);
				final boolean restricted = this.get("restricted", false, val);
				final ServerInfo info = new RedirectServerInfo(name, addr, restricted);
				ret.put(name, info);
			}else {
				final String addr = this.get("address", "localhost:25501", val);
				final boolean restricted = this.get("restricted", false, val);
				final InetSocketAddress address = Util.getAddr(addr);
				final ServerInfo info = ProxyServer.getInstance().constructServerInfo(name, address, restricted);
				ret.put(name, info);
			}
		}
		return ret;
	}

	@Override
	public Collection<ListenerInfo> getListeners() {
		final Collection<HashMap> base = this.get("listeners", (Collection<HashMap>) Arrays.asList(new HashMap()));
		final Map<String, String> forcedDef = new HashMap<String, String>();
		//forcedDef.put("pvp.md-5.net", "pvp");
		final Collection<ListenerInfo> ret = new HashSet<ListenerInfo>();
		for (final Map<String, Object> val : base) {
			String motd = this.get("motd", null, val);
			if(motd != null) {
				val.remove("motd");
			}
			motd = this.get("motd1", motd, val);
			if(motd == null) {
				motd = this.get("motd1", "&6An Eaglercraft server", val);
			}
			motd = ChatColor.translateAlternateColorCodes('&', motd);
			String motd2 = this.get("motd2", null, val);
			if(motd2 != null && motd2.length() > 0) {
				motd = motd + "\n" + ChatColor.translateAlternateColorCodes('&', motd2);
			}
			final int maxPlayers = this.get("max_players", 60, val);
			final String defaultServer = this.get("default_server", "lobby", val);
			final String fallbackServer = this.get("fallback_server", defaultServer, val);
			final boolean forceDefault = this.get("force_default_server", true, val);
			final boolean websocket = this.get("websocket", true, val);
			final boolean forwardIp = this.get("forward_ip", false, val);
			final String forwardIpHeader = this.get("forward_ip_header", "X-Real-IP", val);
			final String host = this.get("host", "0.0.0.0:25565", val);
			final String javaHost = this.get("java_host", "null", val);
			final int tabListSize = this.get("tab_size", 60, val);
			final InetSocketAddress address = Util.getAddr(host);
			final InetSocketAddress javaAddress = (javaHost.equalsIgnoreCase("null") || javaHost == null) ? null : Util.getAddr(javaHost);
			final Map<String, String> forced = (Map<String, String>) new CaseInsensitiveMap(this.get("forced_hosts", forcedDef, val));
			final String textureURL = this.get("texture_url", (String) null, val);
			final int textureSize = this.get("texture_size", 16, val);
			final TexturePackInfo texture = (textureURL == null) ? null : new TexturePackInfo(textureURL, textureSize);
			final String tabListName = this.get("tab_list", "GLOBAL_PING", val);
			final String serverIcon = this.get("server_icon", "server-icon.png", val);
			final boolean allowMOTD = this.get("allow_motd", true, val);
			final boolean allowQuery = this.get("allow_query", true, val);
			final MOTDCacheConfiguration cacheConfig = readCacheConfiguration(this.get("request_motd_cache", new HashMap<String, Object>(), val));
			
			WebSocketRateLimiter ratelimitIP = null;
			WebSocketRateLimiter ratelimitLogin = null;
			WebSocketRateLimiter ratelimitMOTD = null;
			WebSocketRateLimiter ratelimitQuery = null;
			final Map<String, Object> rateLimits = this.get("ratelimit", new HashMap<String, Object>(), val);
			final Map<String, Object> ratelimitIPConfig = this.get("ip", new HashMap<String, Object>(), rateLimits);
			final Map<String, Object> ratelimitLoginConfig = this.get("login", new HashMap<String, Object>(), rateLimits);
			final Map<String, Object> ratelimitMOTDConfig = this.get("motd", new HashMap<String, Object>(), rateLimits);
			final Map<String, Object> ratelimitQueryConfig = this.get("query", new HashMap<String, Object>(), rateLimits);

			if(this.get("enable", true, ratelimitIPConfig)) {
				ratelimitIP = new WebSocketRateLimiter(
						this.get("period", 90, ratelimitIPConfig),
						this.get("limit", 60, ratelimitIPConfig),
						this.get("limit_lockout", 80, ratelimitIPConfig),
						this.get("lockout_duration", 1200, ratelimitIPConfig),
						this.get("exceptions", new ArrayList<String>(), ratelimitIPConfig)
				);
			}
			if(this.get("enable", true, ratelimitLoginConfig)) {
				ratelimitLogin = new WebSocketRateLimiter(
						this.get("period", 50, ratelimitLoginConfig),
						this.get("limit", 5, ratelimitLoginConfig),
						this.get("limit_lockout", 10, ratelimitLoginConfig),
						this.get("lockout_duration", 300, ratelimitLoginConfig),
						this.get("exceptions", new ArrayList<String>(), ratelimitLoginConfig)
				);
			}
			if(this.get("enable", true, ratelimitMOTDConfig)) {
				ratelimitMOTD = new WebSocketRateLimiter(
						this.get("period", 30, ratelimitMOTDConfig),
						this.get("limit", 5, ratelimitMOTDConfig),
						this.get("limit_lockout", 15, ratelimitMOTDConfig),
						this.get("lockout_duration", 1200, ratelimitMOTDConfig),
						this.get("exceptions", new ArrayList<String>(), ratelimitMOTDConfig)
				);
			}
			if(this.get("enable", true, ratelimitQueryConfig)) {
				ratelimitQuery = new WebSocketRateLimiter(
						this.get("period", 90, ratelimitQueryConfig),
						this.get("limit", 60, ratelimitQueryConfig),
						this.get("limit_lockout", 80, ratelimitQueryConfig),
						this.get("lockout_duration", 1200, ratelimitQueryConfig),
						this.get("exceptions", new ArrayList<String>(), ratelimitQueryConfig)
				);
			}
			
			
			DefaultTabList value = DefaultTabList.valueOf(tabListName.toUpperCase());
			if (value == null) {
				value = DefaultTabList.GLOBAL_PING;
			}
			ret.add(new ListenerInfo(host, address, javaAddress, forwardIpHeader, motd, maxPlayers, tabListSize, defaultServer,
					fallbackServer, forceDefault, websocket, forwardIp, forced, texture, value.clazz, serverIcon, cacheConfig,
					allowMOTD, allowQuery, ratelimitIP, ratelimitLogin, ratelimitMOTD, ratelimitQuery));
		}
		return ret;
	}
	
	private MOTDCacheConfiguration readCacheConfiguration(Map<String, Object> val) {
		final int ttl = this.get("cache_ttl", 7200, val);
		final boolean anim = this.get("online_server_list_animation", false, val);
		final boolean results = this.get("online_server_list_results", true, val);
		final boolean trending = this.get("online_server_list_trending", true, val);
		final boolean portfolios = this.get("online_server_list_portfolios", false, val);
		return new MOTDCacheConfiguration(ttl, anim, results, trending, portfolios);
	}

	@Override
	public Collection<String> getGroups(final String player) {
		final Collection<String> groups = this.get("groups." + player, (Collection<String>) null);
		final Collection<String> ret = (groups == null) ? new HashSet<String>() : new HashSet<String>(groups);
		ret.add("default");
		return ret;
	}

	@Override
	public Collection<String> getPermissions(final String group) {
		return this.get("permissions." + group, (Collection<String>) Collections.EMPTY_LIST);
	}

	private enum DefaultTabList {
		GLOBAL((Class<? extends TabListHandler>) Global.class), GLOBAL_PING((Class<? extends TabListHandler>) GlobalPing.class), SERVER((Class<? extends TabListHandler>) ServerUnique.class);

		private final Class<? extends TabListHandler> clazz;

		private DefaultTabList(final Class<? extends TabListHandler> clazz) {
			this.clazz = clazz;
		}
	}

	@Override
	public AuthServiceInfo getAuthSettings() {
		final Map<String, Object> auth = this.get("authservice", new HashMap<String, Object>());
		final List<String> defaultJoinMessages = new ArrayList<String>();
		defaultJoinMessages.add("&3Welcome to my &aEaglercraftBungee &3server!");
		return new AuthServiceInfo(this.get("enabled", false, auth), this.get("register_enabled", true, auth), this.get("authfile", "auths.db", auth),
				this.get("ip_limit", 0, auth), this.get("join_messages", defaultJoinMessages, auth), this.get("login_timeout", 30, auth));
	}
	
	@Override
	public Map<String, Object> getMap() {
		return config;
	}

	@Override
	public void forceSave() {
		this.save();
	}

	@Override
	public Collection<String> getBlacklistURLs() {
		boolean blacklistEnable = this.getBoolean("enable_web_origin_blacklist", true);
		if(!blacklistEnable) {
			return null;
		}
		Collection<String> c = this.get("origin_blacklist_subscriptions", null);
		if(c == null) {
			c = new ArrayList();
			c.add("https://g.lax1dude.net/eaglercraft/origin_blacklist.txt");
			c.add("https://raw.githubusercontent.com/LAX1DUDE/eaglercraft/main/stable-download/origin_blacklist.txt");
			c = this.get("origin_blacklist_subscriptions", c);
		}else {
			if(c.remove("https://g.eags.us/eaglercraft/origin_blacklist.txt")) {
				c.add("https://g.lax1dude.net/eaglercraft/origin_blacklist.txt");
				this.save();
				BungeeCord.getInstance().getLogger().warning("Your origin blacklist has been patched to use g.lax1dude.net instead");
			}
		}
		return c;
	}

	@Override
	public Collection<String> getBlacklistSimpleWhitelist() {
		Collection<String> c = this.get("origin_blacklist_simple_whitelist", null);
		if(c == null) {
			c = new ArrayList();
			c.add("type the name of your client's domain here");
			c.add("(if 'origin_blacklist_use_simple_whitelist' is true)");
			c.add("g.lax1dude.net");
			c = this.get("origin_blacklist_simple_whitelist", c);
		}
		return c;
	}

	@Override
	public Collection<String> getDisabledCommands() {
		return this.get("disabled_commands", new ArrayList());
	}

	@Override
	public Collection<String> getICEServers() {
		Collection<String> ret = new ArrayList();
		
		Collection<String> c = this.get("voice_stun_servers", null);
		if(c == null) {
			c = new ArrayList();
			c.add("stun:stun.l.google.com:19302");
			c.add("stun:stun1.l.google.com:19302");
			c.add("stun:stun2.l.google.com:19302");
			c.add("stun:stun3.l.google.com:19302");
			c.add("stun:stun4.l.google.com:19302");
			c.add("stun:openrelay.metered.ca:80");
			c = this.get("voice_stun_servers", c);
		}
		
		ret.addAll(c);
		
		Map<String, Object> turnServerList = this.get("voice_turn_servers", null);
		if(turnServerList == null) {
			turnServerList = new HashMap();
			HashMap<String, Object> n = new HashMap();
			n.put("url", "turn:openrelay.metered.ca:80");
			n.put("username", "openrelayproject");
			n.put("password", "openrelayproject");
			turnServerList.put("openrelay1", n);
			
			n = new HashMap();
			n.put("url", "turn:openrelay.metered.ca:443");
			n.put("username", "openrelayproject");
			n.put("password", "openrelayproject");
			turnServerList.put("openrelay2", n);
			
			n = new HashMap();
			n.put("url", "turn:openrelay.metered.ca:443?transport=tcp");
			n.put("username", "openrelayproject");
			n.put("password", "openrelayproject");
			turnServerList.put("openrelay3", n);
			turnServerList = this.get("voice_turn_servers", turnServerList);
		}
		
		for(Entry<String, Object> trn : turnServerList.entrySet()) {
			Object o = trn.getValue();
			if(o instanceof Map) {
				Map<String, Object> o2 = (Map<String, Object>) o;
				ret.add("" + o2.get("url") + ";" + o2.get("username") + ";" + o2.get("password"));
			}
		}
		
		return ret;
	}
	
}
