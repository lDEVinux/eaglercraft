// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.config;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Map;

import net.md_5.bungee.api.ServerIcon;
import net.md_5.bungee.api.tab.TabListHandler;
import net.md_5.bungee.eaglercraft.WebSocketRateLimiter;

public class ListenerInfo {
	private final String hostString;
	private final InetSocketAddress host;
	private final InetSocketAddress javaHost;
	private final String motd;
	private final int maxPlayers;
	private final int tabListSize;
	private final String defaultServer;
	private final String fallbackServer;
	private final boolean forceDefault;
	private final boolean websocket;
	private final boolean forwardIp;
	private final String forwardIpHeader;
	private final Map<String, String> forcedHosts;
	private final TexturePackInfo texturePack;
	private final Class<? extends TabListHandler> tabList;
	private final String serverIcon;
	private final int[] serverIconCache;
	private boolean serverIconLoaded;
	private boolean serverIconSet;
	private final boolean allowMOTD;
	private final boolean allowQuery;
	private final MOTDCacheConfiguration cacheConfig;
	private final WebSocketRateLimiter rateLimitIP;
	private final WebSocketRateLimiter rateLimitLogin;
	private final WebSocketRateLimiter rateLimitMOTD;
	private final WebSocketRateLimiter rateLimitQuery;
	

	public ListenerInfo(final String hostString, final InetSocketAddress host, final InetSocketAddress javaHost,
			final String forwardIpHeader, final String motd, final int maxPlayers, final int tabListSize,
			final String defaultServer, final String fallbackServer, final boolean forceDefault,
			final boolean websocket, final boolean forwardIp, final Map<String, String> forcedHosts,
			final TexturePackInfo texturePack, final Class<? extends TabListHandler> tabList, final String serverIcon,
			final MOTDCacheConfiguration cacheConfig, final boolean allowMOTD, final boolean allowQuery,
			final WebSocketRateLimiter rateLimitIP, final WebSocketRateLimiter rateLimitLogin,
			final WebSocketRateLimiter rateLimitMOTD, final WebSocketRateLimiter rateLimitQuery) {
		this.hostString = hostString;
		this.host = host;
		this.javaHost = javaHost;
		this.motd = motd;
		this.maxPlayers = maxPlayers;
		this.tabListSize = tabListSize;
		this.defaultServer = defaultServer;
		this.fallbackServer = fallbackServer;
		this.forceDefault = forceDefault;
		this.websocket = websocket;
		this.forwardIp = forwardIp;
		this.forwardIpHeader = forwardIpHeader;
		this.forcedHosts = forcedHosts;
		this.texturePack = texturePack;
		this.tabList = tabList;
		this.serverIcon = serverIcon;
		this.serverIconCache = new int[4096];
		this.serverIconLoaded = false;
		this.serverIconSet = false;
		this.allowMOTD = allowMOTD;
		this.allowQuery = allowQuery;
		this.cacheConfig = cacheConfig;
		this.rateLimitIP = rateLimitIP;
		this.rateLimitLogin = rateLimitLogin;
		this.rateLimitMOTD = rateLimitMOTD;
		this.rateLimitQuery = rateLimitQuery;
	}

	public String getHostString() {
		return this.hostString;
	}

	public InetSocketAddress getHost() {
		return this.host;
	}

	public InetSocketAddress getJavaHost() {
		return this.javaHost;
	}

	public String getMotd() {
		return this.motd;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public int getTabListSize() {
		return this.tabListSize;
	}

	public String getDefaultServer() {
		return this.defaultServer;
	}

	public String getFallbackServer() {
		return this.fallbackServer;
	}

	public boolean isForceDefault() {
		return this.forceDefault;
	}

	public Map<String, String> getForcedHosts() {
		return this.forcedHosts;
	}

	public TexturePackInfo getTexturePack() {
		return this.texturePack;
	}

	public Class<? extends TabListHandler> getTabList() {
		return this.tabList;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ListenerInfo)) {
			return false;
		}
		final ListenerInfo other = (ListenerInfo) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$host = this.getHost();
		final Object other$host = other.getHost();
		Label_0065: {
			if (this$host == null) {
				if (other$host == null) {
					break Label_0065;
				}
			} else if (this$host.equals(other$host)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$motd = this.getMotd();
		final Object other$motd = other.getMotd();
		Label_0102: {
			if (this$motd == null) {
				if (other$motd == null) {
					break Label_0102;
				}
			} else if (this$motd.equals(other$motd)) {
				break Label_0102;
			}
			return false;
		}
		if (this.getMaxPlayers() != other.getMaxPlayers()) {
			return false;
		}
		if (this.getTabListSize() != other.getTabListSize()) {
			return false;
		}
		if (this.isWebsocket() != other.isWebsocket()) {
			return false;
		}
		final Object this$defaultServer = this.getDefaultServer();
		final Object other$defaultServer = other.getDefaultServer();
		Label_0165: {
			if (this$defaultServer == null) {
				if (other$defaultServer == null) {
					break Label_0165;
				}
			} else if (this$defaultServer.equals(other$defaultServer)) {
				break Label_0165;
			}
			return false;
		}
		final Object this$fallbackServer = this.getFallbackServer();
		final Object other$fallbackServer = other.getFallbackServer();
		Label_0202: {
			if (this$fallbackServer == null) {
				if (other$fallbackServer == null) {
					break Label_0202;
				}
			} else if (this$fallbackServer.equals(other$fallbackServer)) {
				break Label_0202;
			}
			return false;
		}
		if (this.isForceDefault() != other.isForceDefault()) {
			return false;
		}
		final Object this$forcedHosts = this.getForcedHosts();
		final Object other$forcedHosts = other.getForcedHosts();
		Label_0252: {
			if (this$forcedHosts == null) {
				if (other$forcedHosts == null) {
					break Label_0252;
				}
			} else if (this$forcedHosts.equals(other$forcedHosts)) {
				break Label_0252;
			}
			return false;
		}
		final Object this$texturePack = this.getTexturePack();
		final Object other$texturePack = other.getTexturePack();
		Label_0289: {
			if (this$texturePack == null) {
				if (other$texturePack == null) {
					break Label_0289;
				}
			} else if (this$texturePack.equals(other$texturePack)) {
				break Label_0289;
			}
			return false;
		}
		final Object this$tabList = this.getTabList();
		final Object other$tabList = other.getTabList();
		if (this$tabList == null) {
			if (other$tabList == null) {
				return true;
			}
		} else if (this$tabList.equals(other$tabList)) {
			return true;
		}
		final Object this$getServerIcon = this.getServerIcon();
		final Object other$getServerIcon = other.getServerIcon();
		if (this$getServerIcon == null) {
			if (other$getServerIcon == null) {
				return true;
			}
		} else if (this$getServerIcon.equals(other$getServerIcon)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof ListenerInfo;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $host = this.getHost();
		result = result * 31 + (($host == null) ? 0 : $host.hashCode());
		final Object $motd = this.getMotd();
		result = result * 31 + (($motd == null) ? 0 : $motd.hashCode());
		result = result * 31 + this.getMaxPlayers();
		result = result * 31 + this.getTabListSize();
		final Object $defaultServer = this.getDefaultServer();
		result = result * 31 + (($defaultServer == null) ? 0 : $defaultServer.hashCode());
		final Object $fallbackServer = this.getFallbackServer();
		result = result * 31 + (($fallbackServer == null) ? 0 : $fallbackServer.hashCode());
		result = result * 31 + (this.isForceDefault() ? 1231 : 1237);
		final Object $forcedHosts = this.getForcedHosts();
		result = result * 31 + (($forcedHosts == null) ? 0 : $forcedHosts.hashCode());
		final Object $texturePack = this.getTexturePack();
		result = result * 31 + (($texturePack == null) ? 0 : $texturePack.hashCode());
		final Object $tabList = this.getTabList();
		result = result * 31 + (($tabList == null) ? 0 : $tabList.hashCode());
		final Object $serverIconCache = this.getTabList();
		result = result * 31 + (($serverIconCache == null) ? 0 : $serverIconCache.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ListenerInfo(host=" + this.getHost() + ", motd=" + this.getMotd() + ", maxPlayers=" + this.getMaxPlayers() + ", tabListSize=" + this.getTabListSize() + ", defaultServer=" + this.getDefaultServer() + ", fallbackServer="
				+ this.getFallbackServer() + ", forceDefault=" + this.isForceDefault() + ", websocket=" + this.isWebsocket() + ", forcedHosts=" + this.getForcedHosts() + ", texturePack=" + this.getTexturePack() + ", tabList=" + this.getTabList() + ")";
	}

	public boolean isWebsocket() {
		return websocket;
	}

	public boolean hasForwardedHeaders() {
		return forwardIp;
	}

	public String getForwardedIPHeader() {
		return forwardIpHeader;
	}

	public String getServerIcon() {
		return serverIcon;
	}

	public int[] getServerIconCache() {
		if(!serverIconLoaded) {
			if(serverIcon != null) {
				int[] img = ServerIcon.createServerIcon(new File(serverIcon));
				if(img != null) {
					System.arraycopy(img, 0, serverIconCache, 0, img.length);
					serverIconSet = true;
				}else {
					serverIconSet = false;
				}
			}else {
				serverIconSet = false;
			}
			serverIconLoaded = true;
		}
		return serverIconCache;
	}
	
	public boolean isIconSet() {
		getServerIconCache();
		return serverIconSet;
	}

	public boolean isForwardIp() {
		return forwardIp;
	}

	public boolean isServerIconLoaded() {
		return serverIconLoaded;
	}

	public boolean isServerIconSet() {
		return serverIconSet;
	}

	public boolean isAllowMOTD() {
		return allowMOTD;
	}

	public boolean isAllowQuery() {
		return allowQuery;
	}

	public MOTDCacheConfiguration getCacheConfig() {
		return cacheConfig;
	}

	public WebSocketRateLimiter getRateLimitIP() {
		return rateLimitIP;
	}

	public WebSocketRateLimiter getRateLimitLogin() {
		return rateLimitLogin;
	}

	public WebSocketRateLimiter getRateLimitMOTD() {
		return rateLimitMOTD;
	}

	public WebSocketRateLimiter getRateLimitQuery() {
		return rateLimitQuery;
	}

}
