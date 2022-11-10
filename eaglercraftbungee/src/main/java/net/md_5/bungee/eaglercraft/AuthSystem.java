package net.md_5.bungee.eaglercraft;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.AuthServiceInfo;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthSystem {
	private final String authFileName;
	private final int ipLimit;
	public final String[] joinMessages;

	public AuthSystem(AuthServiceInfo authInfo) {
		this.authFileName = authInfo.getAuthfile();
		this.ipLimit = authInfo.getIpLimit();
		List<String> listJoinMessages = authInfo.getJoinMessages();
		String[] arrayJoinMessages = new String[listJoinMessages.size()];
		for (int i = 0; i < listJoinMessages.size(); i++) {
			arrayJoinMessages[i] = ChatColor.translateAlternateColorCodes('&', listJoinMessages.get(i));
		}
		this.joinMessages = arrayJoinMessages;

		this.readDatabase();
	}

	private static class AuthData {
		public String salt;
		public String hash;
		public String ip;
		public long timestamp;

		public AuthData(String salt, String hash, String ip, long timestamp) {
			this.salt = salt;
			this.hash = hash;
			this.ip = ip;
			this.timestamp = timestamp;
		}
	}

	private final Map<String, AuthData> database = new HashMap<>();

	public boolean register(String username, String password, String ip) {
		username = username.toLowerCase();
		synchronized (database) {
			AuthData authData = database.get(username);
			if (authData != null)
				return false;
			if (isIpAtTheLimit(ip))
				return false;
			String salt = createSalt(16);
			String hash = getSaltedHash(password, salt);
			database.put(username, new AuthData(salt, hash, ip, System.currentTimeMillis()));
			writeDatabase();
			return true;
		}
	}

	public boolean isRegistered(String username) {
		username = username.toLowerCase();
		synchronized (database) {
			return database.containsKey(username);
		}
	}

	public boolean changePass(String username, String password) {
		username = username.toLowerCase();
		synchronized (database) {
			AuthData authData = database.get(username);
			authData.salt = createSalt(16);
			authData.hash = getSaltedHash(password, authData.salt);
			writeDatabase();
			return true;
		}
	}

	public boolean login(String username, String password) {
		username = username.toLowerCase();
		synchronized (database) {
			AuthData authData = database.get(username);
			if (authData == null)
				return false;
			return authData.hash.equals(getSaltedHash(password, authData.salt));
		}
	}

	private boolean isIpAtTheLimit(String ip) {
		synchronized (database) {
			if (this.ipLimit <= 0)
				return false;
			int num = 0;
			for (AuthData authData : database.values()) {
				if (authData.ip.equals(ip))
					num++;
				if (num >= this.ipLimit) {
					return true;
				}
			}
			return false;
		}
	}

	// only use once, on load
	public void readDatabase() {
		synchronized (database) {
			try {
				File authFile = new File(this.authFileName);
				if (!authFile.exists())
					authFile.createNewFile();

				database.clear();

				String[] lines = new String(Files.readAllBytes(authFile.toPath())).trim().split("\n");
				if (lines.length == 1 && lines[0].isEmpty())
					return;
				boolean alreadyLogged = false;
				for (String line : lines) {
					String[] pieces = line.split(":");
					if (!pieces[1].startsWith("$SHA$")) {
						if (!alreadyLogged) {
							alreadyLogged = true;
							BungeeCord.getInstance().getLogger().warning(
									"One or more entries in the auth file are hashed in an unsupported format! (not SHA-256!)");
						}
						// continue;
					}
					String[] saltHash = pieces[1].substring(pieces[1].substring(1).indexOf('$') + 2).split("\\$");
					database.put(pieces[0],
							new AuthData(saltHash[0], saltHash[1], pieces[2], Long.parseLong(pieces[3])));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeDatabase() {
		synchronized (database) {
			StringBuilder out = new StringBuilder();

			for (String username : database.keySet()) {
				AuthData entry = database.get(username);
				out.append(username);
				out.append(":$SHA$");
				out.append(entry.salt);
				out.append("$");
				out.append(entry.hash);
				out.append(":");
				out.append(entry.ip);
				out.append(":");
				out.append(entry.timestamp);
				out.append("\n");
			}

			try {
				Files.write(Paths.get(this.authFileName), out.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// hashing used is based on hashing from AuthMe

	private static final SecureRandom rnd = new SecureRandom();

	private static String getSHA256(String message) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			sha256.reset();
			sha256.update(message.getBytes());
			byte[] digest = sha256.digest();
			return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

	private static String getSaltedHash(String message, String salt) {
		return getSHA256(getSHA256(message) + salt);
	}

	private static String createSalt(int length) {
		try {
			byte[] msg = new byte[40];
			rnd.nextBytes(msg);
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			sha1.reset();
			byte[] digest = sha1.digest(msg);
			return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).substring(0, length);
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}
}