// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.event;

import java.beans.ConstructorProperties;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.plugin.Event;

public class ProxyPingEvent extends Event {
	private final PendingConnection connection;
	private ServerPing response;

	public PendingConnection getConnection() {
		return this.connection;
	}

	public ServerPing getResponse() {
		return this.response;
	}

	public void setResponse(final ServerPing response) {
		this.response = response;
	}

	@ConstructorProperties({ "connection", "response" })
	public ProxyPingEvent(final PendingConnection connection, final ServerPing response) {
		this.connection = connection;
		this.response = response;
	}

	@Override
	public String toString() {
		return "ProxyPingEvent(connection=" + this.getConnection() + ", response=" + this.getResponse() + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ProxyPingEvent)) {
			return false;
		}
		final ProxyPingEvent other = (ProxyPingEvent) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$connection = this.getConnection();
		final Object other$connection = other.getConnection();
		Label_0065: {
			if (this$connection == null) {
				if (other$connection == null) {
					break Label_0065;
				}
			} else if (this$connection.equals(other$connection)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$response = this.getResponse();
		final Object other$response = other.getResponse();
		if (this$response == null) {
			if (other$response == null) {
				return true;
			}
		} else if (this$response.equals(other$response)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof ProxyPingEvent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $connection = this.getConnection();
		result = result * 31 + (($connection == null) ? 0 : $connection.hashCode());
		final Object $response = this.getResponse();
		result = result * 31 + (($response == null) ? 0 : $response.hashCode());
		return result;
	}
}
