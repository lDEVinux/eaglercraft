// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.reconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SQLReconnectHandler extends AbstractReconnectManager {
	private final Connection connection;

	public SQLReconnectHandler() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		this.connection = DriverManager.getConnection("jdbc:sqlite:bungee.sqlite");
		try (final PreparedStatement ps = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (playerId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,username TEXT NOT NULL UNIQUE COLLATE NOCASE,seen INTEGER,server TEXT);")) {
			ps.executeUpdate();
		}
	}

	@Override
	protected String getStoredServer(final ProxiedPlayer player) {
		String server = null;
		try (final PreparedStatement ps = this.connection.prepareStatement("SELECT server FROM players WHERE username = ?")) {
			ps.setString(1, player.getName());
			try (final ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					server = rs.getString(1);
				} else {
					try (final PreparedStatement playerUpdate = this.connection.prepareStatement("INSERT INTO players( username ) VALUES( ? )")) {
						playerUpdate.setString(1, player.getName());
						playerUpdate.executeUpdate();
					}
				}
			}
		} catch (SQLException ex) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Could not load location for player " + player.getName(), ex);
		}
		return server;
	}

	@Override
	public void setServer(final ProxiedPlayer player) {
		try (final PreparedStatement ps = this.connection.prepareStatement("UPDATE players SET server = ?, seen = ? WHERE username = ?")) {
			ps.setString(1, player.getServer().getInfo().getName());
			ps.setInt(2, (int) (System.currentTimeMillis() / 1000L));
			ps.setString(3, player.getName());
			ps.executeUpdate();
		} catch (SQLException ex) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Could not save location for player " + player.getName() + " on server " + player.getServer().getInfo().getName(), ex);
		}
	}

	@Override
	public void save() {
	}

	@Override
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException ex) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Error closing SQLite connection", ex);
		}
	}
}
