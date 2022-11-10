// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.score;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Team {
	private final String name;
	private String displayName;
	private String prefix;
	private String suffix;
	private boolean friendlyFire;
	private Set<String> players;

	public Collection<String> getPlayers() {
		return (Collection<String>) (Collection<?>) Collections.unmodifiableSet((Set<?>) this.players);
	}

	public void addPlayer(final String name) {
		this.players.add(name);
	}

	public void removePlayer(final String name) {
		this.players.remove(name);
	}

	@ConstructorProperties({ "name" })
	public Team(final String name) {
		this.players = new HashSet<String>();
		if (name == null) {
			throw new NullPointerException("name");
		}
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public boolean isFriendlyFire() {
		return this.friendlyFire;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	public void setFriendlyFire(final boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public void setPlayers(final Set<String> players) {
		this.players = players;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Team)) {
			return false;
		}
		final Team other = (Team) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		Label_0065: {
			if (this$name == null) {
				if (other$name == null) {
					break Label_0065;
				}
			} else if (this$name.equals(other$name)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$displayName = this.getDisplayName();
		final Object other$displayName = other.getDisplayName();
		Label_0102: {
			if (this$displayName == null) {
				if (other$displayName == null) {
					break Label_0102;
				}
			} else if (this$displayName.equals(other$displayName)) {
				break Label_0102;
			}
			return false;
		}
		final Object this$prefix = this.getPrefix();
		final Object other$prefix = other.getPrefix();
		Label_0139: {
			if (this$prefix == null) {
				if (other$prefix == null) {
					break Label_0139;
				}
			} else if (this$prefix.equals(other$prefix)) {
				break Label_0139;
			}
			return false;
		}
		final Object this$suffix = this.getSuffix();
		final Object other$suffix = other.getSuffix();
		Label_0176: {
			if (this$suffix == null) {
				if (other$suffix == null) {
					break Label_0176;
				}
			} else if (this$suffix.equals(other$suffix)) {
				break Label_0176;
			}
			return false;
		}
		if (this.isFriendlyFire() != other.isFriendlyFire()) {
			return false;
		}
		final Object this$players = this.getPlayers();
		final Object other$players = other.getPlayers();
		if (this$players == null) {
			if (other$players == null) {
				return true;
			}
		} else if (this$players.equals(other$players)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Team;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		final Object $displayName = this.getDisplayName();
		result = result * 31 + (($displayName == null) ? 0 : $displayName.hashCode());
		final Object $prefix = this.getPrefix();
		result = result * 31 + (($prefix == null) ? 0 : $prefix.hashCode());
		final Object $suffix = this.getSuffix();
		result = result * 31 + (($suffix == null) ? 0 : $suffix.hashCode());
		result = result * 31 + (this.isFriendlyFire() ? 1231 : 1237);
		final Object $players = this.getPlayers();
		result = result * 31 + (($players == null) ? 0 : $players.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Team(name=" + this.getName() + ", displayName=" + this.getDisplayName() + ", prefix=" + this.getPrefix() + ", suffix=" + this.getSuffix() + ", friendlyFire=" + this.isFriendlyFire() + ", players=" + this.getPlayers() + ")";
	}
}
