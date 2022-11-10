// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api;

import java.beans.ConstructorProperties;

public class ServerPing {
	private final byte protocolVersion;
	private final String gameVersion;
	private final String motd;
	private final int currentPlayers;
	private final int maxPlayers;

	@ConstructorProperties({ "protocolVersion", "gameVersion", "motd", "currentPlayers", "maxPlayers" })
	public ServerPing(final byte protocolVersion, final String gameVersion, final String motd, final int currentPlayers, final int maxPlayers) {
		this.protocolVersion = protocolVersion;
		this.gameVersion = gameVersion;
		this.motd = motd;
		this.currentPlayers = currentPlayers;
		this.maxPlayers = maxPlayers;
	}

	public byte getProtocolVersion() {
		return this.protocolVersion;
	}

	public String getGameVersion() {
		return this.gameVersion;
	}

	public String getMotd() {
		return this.motd;
	}

	public int getCurrentPlayers() {
		return this.currentPlayers;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ServerPing)) {
			return false;
		}
		final ServerPing other = (ServerPing) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getProtocolVersion() != other.getProtocolVersion()) {
			return false;
		}
		final Object this$gameVersion = this.getGameVersion();
		final Object other$gameVersion = other.getGameVersion();
		Label_0078: {
			if (this$gameVersion == null) {
				if (other$gameVersion == null) {
					break Label_0078;
				}
			} else if (this$gameVersion.equals(other$gameVersion)) {
				break Label_0078;
			}
			return false;
		}
		final Object this$motd = this.getMotd();
		final Object other$motd = other.getMotd();
		if (this$motd == null) {
			if (other$motd == null) {
				return this.getCurrentPlayers() == other.getCurrentPlayers() && this.getMaxPlayers() == other.getMaxPlayers();
			}
		} else if (this$motd.equals(other$motd)) {
			return this.getCurrentPlayers() == other.getCurrentPlayers() && this.getMaxPlayers() == other.getMaxPlayers();
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof ServerPing;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * 31 + this.getProtocolVersion();
		final Object $gameVersion = this.getGameVersion();
		result = result * 31 + (($gameVersion == null) ? 0 : $gameVersion.hashCode());
		final Object $motd = this.getMotd();
		result = result * 31 + (($motd == null) ? 0 : $motd.hashCode());
		result = result * 31 + this.getCurrentPlayers();
		result = result * 31 + this.getMaxPlayers();
		return result;
	}

	@Override
	public String toString() {
		return "ServerPing(protocolVersion=" + this.getProtocolVersion() + ", gameVersion=" + this.getGameVersion() + ", motd=" + this.getMotd() + ", currentPlayers=" + this.getCurrentPlayers() + ", maxPlayers=" + this.getMaxPlayers()
				+ ")";
	}
}
