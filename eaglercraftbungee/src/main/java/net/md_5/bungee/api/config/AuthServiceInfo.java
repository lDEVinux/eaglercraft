package net.md_5.bungee.api.config;

import java.util.List;

public class AuthServiceInfo {

	private final boolean enabled;
	private final boolean registerEnabled;
	private final String authfile;
	private final int ipLimit;
	private final List<String> joinMessages;
	private final int loginTimeout;
	
	public AuthServiceInfo(boolean enabled, boolean registerEnabled, String authfile,
			int timeout, List<String> joinMessages, int loginTimeout) {
		this.enabled = enabled;
		this.registerEnabled = registerEnabled;
		this.authfile = authfile;
		this.ipLimit = timeout;
		this.joinMessages = joinMessages;
		this.loginTimeout = loginTimeout;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isRegisterEnabled() {
		return registerEnabled;
	}

	public String getAuthfile() {
		return authfile;
	}

	public int getIpLimit() {
		return ipLimit;
	}

	public List<String> getJoinMessages() {
		return joinMessages;
	}

	public int getLoginTimeout() {
		return loginTimeout;
	}

}
