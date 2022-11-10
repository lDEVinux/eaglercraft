package net.minecraft.src;

public class ServerAddress {
	private final String ipAddress;
	private final int serverPort;

	private ServerAddress(String par1Str, int par2) {
		this.ipAddress = par1Str;
		this.serverPort = par2;
	}

	public String getIP() {
		return this.ipAddress;
	}

	public int getPort() {
		return this.serverPort;
	}

	public static ServerAddress func_78860_a(String par0Str) {
		if (par0Str == null) {
			return null;
		} else {
			String[] var1 = par0Str.split(":");

			if (par0Str.startsWith("[")) {
				int var2 = par0Str.indexOf("]");

				if (var2 > 0) {
					String var3 = par0Str.substring(1, var2);
					String var4 = par0Str.substring(var2 + 1).trim();

					if (var4.startsWith(":") && var4.length() > 0) {
						var4 = var4.substring(1);
						var1 = new String[] { var3, var4 };
					} else {
						var1 = new String[] { var3 };
					}
				}
			}

			if (var1.length > 2) {
				var1 = new String[] { par0Str };
			}

			String var5 = var1[0];
			int var6 = var1.length > 1 ? parseIntWithDefault(var1[1], 25565) : 25565;

			return new ServerAddress(var5, var6);
		}
	}

	private static int parseIntWithDefault(String par0Str, int par1) {
		try {
			return Integer.parseInt(par0Str.trim());
		} catch (Exception var3) {
			return par1;
		}
	}
}
