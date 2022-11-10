// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import java.beans.ConstructorProperties;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

public class PermissionCheckEvent extends Event {
	private final CommandSender sender;
	private final String permission;
	private boolean hasPermission;

	public boolean hasPermission() {
		return this.hasPermission;
	}

	public CommandSender getSender() {
		return this.sender;
	}

	public String getPermission() {
		return this.permission;
	}

	public void setHasPermission(final boolean hasPermission) {
		this.hasPermission = hasPermission;
	}

	@ConstructorProperties({ "sender", "permission", "hasPermission" })
	public PermissionCheckEvent(final CommandSender sender, final String permission, final boolean hasPermission) {
		this.sender = sender;
		this.permission = permission;
		this.hasPermission = hasPermission;
	}

	@Override
	public String toString() {
		return "PermissionCheckEvent(sender=" + this.getSender() + ", permission=" + this.getPermission() + ", hasPermission=" + this.hasPermission + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PermissionCheckEvent)) {
			return false;
		}
		final PermissionCheckEvent other = (PermissionCheckEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$sender = this.getSender();
		final Object other$sender = other.getSender();
		Label_0065: {
			if (this$sender == null) {
				if (other$sender == null) {
					break Label_0065;
				}
			} else if (this$sender.equals(other$sender)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$permission = this.getPermission();
		final Object other$permission = other.getPermission();
		if (this$permission == null) {
			if (other$permission == null) {
				return this.hasPermission == other.hasPermission;
			}
		} else if (this$permission.equals(other$permission)) {
			return this.hasPermission == other.hasPermission;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof PermissionCheckEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $sender = this.getSender();
		result = result * 31 + (($sender == null) ? 0 : $sender.hashCode());
		final Object $permission = this.getPermission();
		result = result * 31 + (($permission == null) ? 0 : $permission.hashCode());
		result = result * 31 + (this.hasPermission ? 1231 : 1237);
		return result;
	}
}
