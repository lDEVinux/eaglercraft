// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.plugin;

import java.util.Arrays;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.CommandSender;

public abstract class Command {
	private final String name;
	private final String permission;
	private final String[] aliases;

	public Command(final String name) {
		this(name, null, new String[0]);
	}

	public Command(final String name, final String permission, final String... aliases) {
		Preconditions.checkArgument(name != null, (Object) "name");
		this.name = name;
		this.permission = permission;
		this.aliases = aliases;
	}

	public abstract void execute(final CommandSender p0, final String[] p1);

	public String getName() {
		return this.name;
	}

	public String getPermission() {
		return this.permission;
	}

	public String[] getAliases() {
		return this.aliases;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Command)) {
			return false;
		}
		final Command other = (Command) o;
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
		final Object this$permission = this.getPermission();
		final Object other$permission = other.getPermission();
		if (this$permission == null) {
			if (other$permission == null) {
				return Arrays.deepEquals(this.getAliases(), other.getAliases());
			}
		} else if (this$permission.equals(other$permission)) {
			return Arrays.deepEquals(this.getAliases(), other.getAliases());
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Command;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		final Object $permission = this.getPermission();
		result = result * 31 + (($permission == null) ? 0 : $permission.hashCode());
		result = result * 31 + Arrays.deepHashCode(this.getAliases());
		return result;
	}

	@Override
	public String toString() {
		return "Command(name=" + this.getName() + ", permission=" + this.getPermission() + ", aliases=" + Arrays.deepToString(this.getAliases()) + ")";
	}
}
