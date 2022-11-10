// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.config;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;

import gnu.trove.map.TMap;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.AuthServiceInfo;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.eaglercraft.EaglercraftBungee;
import net.md_5.bungee.util.CaseInsensitiveMap;

public class Configuration {
	private int timeout;
	private String uuid;
	private Collection<ListenerInfo> listeners;
	private TMap<String, ServerInfo> servers;
	private AuthServiceInfo authInfo;
	private boolean onlineMode;
	private boolean voiceEnabled;
	private boolean protocolSupport;
	private String tokenVerify;
	private int playerLimit;
	private String name;
	private boolean showBanType;
	private boolean blacklistOfflineDownload;
	private boolean blacklistReplits;
	private boolean blacklistOriginless;
	private boolean simpleWhitelistEnabled;
	private boolean acceptBukkitConsoleCommandPacket;
	private Collection<String> disabledCommands;
	private Collection<String> iceServers;
	private boolean bungeeOnBungee;

	public Configuration() {
		this.timeout = 30000;
		this.uuid = UUID.randomUUID().toString();
		this.onlineMode = true;
		this.playerLimit = -1;
	}

	public void load() {
		final ConfigurationAdapter adapter = ProxyServer.getInstance().getConfigurationAdapter();
		adapter.load();
		this.listeners = adapter.getListeners();
		this.timeout = adapter.getInt("timeout", this.timeout);
		this.uuid = adapter.getString("stats", this.uuid);
		if(this.uuid.equalsIgnoreCase("595698b3-9c36-4e86-b1ee-cb3027038f41")) {
			this.uuid = UUID.randomUUID().toString();
			System.err.println("Notice: this server has the stats UUID \"595698b3-9c36-4e86-b1ee-cb3027038f41\" which is a known duplicate");
			System.err.println("It has been updated to \"" + this.uuid + "\". This is not an error");
			adapter.getMap().put("stats", this.uuid);
			adapter.forceSave();
		}
		this.authInfo = adapter.getAuthSettings();
		this.onlineMode = false;
		this.voiceEnabled = adapter.getBoolean("voice_enabled", true);
		this.protocolSupport = adapter.getBoolean("protocol_support_fix", false);
		this.tokenVerify = adapter.getString("token_verify", "");
		this.playerLimit = adapter.getInt("player_limit", this.playerLimit);
		this.name = adapter.getString("server_name", EaglercraftBungee.name + " Server");
		this.showBanType = adapter.getBoolean("display_ban_type_on_kick", false);
		this.blacklistOfflineDownload = adapter.getBoolean("origin_blacklist_block_offline_download", false);
		this.blacklistReplits = adapter.getBoolean("origin_blacklist_block_replit_clients", false);
		adapter.getMap().remove("origin_blacklist_block_missing_origin_header");
		this.blacklistOriginless = adapter.getBoolean("origin_blacklist_block_invalid_origin_header", true);
		this.simpleWhitelistEnabled = adapter.getBoolean("origin_blacklist_use_simple_whitelist", false);
		this.acceptBukkitConsoleCommandPacket = adapter.getBoolean("accept_bukkit_console_command_packets", false);
		this.bungeeOnBungee = adapter.getBoolean("bungee_on_bungee", false);
		this.disabledCommands = adapter.getDisabledCommands();
		this.iceServers = adapter.getICEServers();
		Preconditions.checkArgument(this.listeners != null && !this.listeners.isEmpty(), (Object) "No listeners defined.");
		final Map<String, ServerInfo> newServers = adapter.getServers();
		Preconditions.checkArgument(newServers != null && !newServers.isEmpty(), (Object) "No servers defined");
		if (this.servers == null) {
			this.servers = (TMap<String, ServerInfo>) new CaseInsensitiveMap(newServers);
		} else {
			for (final ServerInfo oldServer : this.servers.values()) {
				Preconditions.checkArgument(newServers.containsValue(oldServer), "Server %s removed on reload!", new Object[] { oldServer.getName() });
			}
			for (final Map.Entry<String, ServerInfo> newServer : newServers.entrySet()) {
				if (!this.servers.containsValue(newServer.getValue())) {
					this.servers.put(newServer.getKey(), newServer.getValue());
				}
			}
		}
		for (final ListenerInfo listener : this.listeners) {
			Preconditions.checkArgument(this.servers.containsKey((Object) listener.getDefaultServer()), "Default server %s is not defined", new Object[] { listener.getDefaultServer() });
		}
	}

	public int getTimeout() {
		return this.timeout;
	}

	public String getUuid() {
		return this.uuid;
	}

	public Collection<ListenerInfo> getListeners() {
		return this.listeners;
	}

	public TMap<String, ServerInfo> getServers() {
		return this.servers;
	}

	public boolean isOnlineMode() {
		return this.onlineMode;
	}

	public int getPlayerLimit() {
		return this.playerLimit;
	}
	
	public AuthServiceInfo getAuthInfo() {
		return authInfo;
	}

	public boolean getVoiceEnabled() {
		return voiceEnabled;
	}

	public boolean getProtocolSupport() {
		return protocolSupport;
	}

	public String getTokenVerify() {
		return tokenVerify;
	}

	public String getServerName() {
		return name;
	}

	public boolean shouldShowBanType() {
		return this.showBanType;
	}

	public boolean shouldBlacklistOfflineDownload() {
		return blacklistOfflineDownload;
	}

	public boolean shouldBlacklistReplits() {
		return blacklistReplits;
	}

	public boolean shouldBlacklistOriginless() {
		return blacklistOriginless;
	}

	public boolean isSimpleWhitelistEnabled() {
		return simpleWhitelistEnabled;
	}

	public boolean shouldAcceptBukkitConsoleCommandPacket() {
		return acceptBukkitConsoleCommandPacket;
	}
	
	public Collection<String> getDisabledCommands() {
		return disabledCommands;
	}
	
	public Collection<String> getICEServers() {
		return iceServers;
	}

	public boolean allowBungeeOnBungee() {
		return bungeeOnBungee;
	}
	
}
