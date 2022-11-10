package net.md_5.bungee.eaglercraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.eaglercraft.sun.net.util.IPAddressUtil;

public class BanList {
	
	private static final Object banListMutex = new Object();

	public static enum BanState {
		NOT_BANNED, USER_BANNED, IP_BANNED, WILDCARD_BANNED, REGEX_BANNED;
	}
	
	public static class BanCheck {
		public final BanState reason;
		public final String match;
		public final String string;
		private BanCheck(BanState reason, String match, String string) {
			this.reason = reason;
			this.match = match;
			this.string = string;
		}
		public boolean isBanned() {
			return reason != BanState.NOT_BANNED;
		}
	}

	private static class RegexBan {
		public final String string;
		public final Pattern compiled;
		private RegexBan(String string, Pattern compiled) {
			this.string = string;
			this.compiled = compiled;
		}
		public String toString() {
			return string;
		}
		public int hashCode() {
			return string.hashCode();
		}
	}
	
	public static interface IPBan {
		
		boolean checkBan(InetAddress addr);
		InetAddress getBaseAddress();
		boolean hasNetMask();
		
	}
	
	private static class IPBan4 implements IPBan {

		private final int addr;
		private final InetAddress addrI;
		private final int mask;
		private final String string;
		
		protected IPBan4(Inet4Address addr, String s, int mask) {
			if(mask >= 32) {
				this.mask = 0xFFFFFFFF;
			}else {
				this.mask = ~((1 << (32 - mask)) - 1);
			}
			this.string = s;
			byte[] bits = addr.getAddress();
			this.addr = this.mask & ((bits[0] << 24) | (bits[1] << 16) | (bits[2] << 8) | (bits[3] & 0xFF));
			this.addrI = addr;
		}

		@Override
		public boolean checkBan(InetAddress addr4) {
			if(addr4 instanceof Inet4Address) {
				Inet4Address a = (Inet4Address)addr4;
				byte[] bits = a.getAddress();
				int addrBits = ((bits[0] << 24) | (bits[1] << 16) | (bits[2] << 8) | (bits[3] & 0xFF));
				return (mask & addrBits) == addr;
			}else {
				return false;
			}
		}
		
		@Override
		public InetAddress getBaseAddress() {
			return addrI;
		}
		
		@Override
		public String toString() {
			return string;
		}
		
		@Override
		public int hashCode() {
			return string.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			return o != null && o instanceof IPBan4 && ((IPBan4)o).addr == addr && ((IPBan4)o).mask == mask;
		}
		
		@Override
		public boolean hasNetMask() {
			return mask != 0xFFFFFFFF;
		}
		
	}
	
	private static class IPBan6 implements IPBan {
		
		private static final BigInteger mask128 = new BigInteger(1, new byte[] {
				(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
				(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
				(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
				(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF
		});
		
		private final BigInteger addr;
		private final InetAddress addrI;
		private final BigInteger mask;
		private final String string;
		
		protected IPBan6(Inet6Address addr, String s, int mask) {
			this.mask = BigInteger.valueOf(1l).shiftLeft(128 - mask).subtract(BigInteger.valueOf(1l)).xor(mask128);
			this.string = s.toLowerCase();
			this.addr = new BigInteger(1, addr.getAddress()).and(this.mask);
			this.addrI = addr;
		}

		@Override
		public boolean checkBan(InetAddress addr6) {
			if(addr6 instanceof Inet6Address) {
				Inet6Address a = (Inet6Address)addr6;
				BigInteger addrBits = new BigInteger(1, a.getAddress()).and(this.mask);
				return addr.equals(addrBits);
			}else {
				return false;
			}
		}
		
		@Override
		public InetAddress getBaseAddress() {
			return addrI;
		}
		
		@Override
		public String toString() {
			return string;
		}
		
		@Override
		public int hashCode() {
			return string.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			return o != null && o instanceof IPBan6 && ((IPBan6)o).addr.equals(addr) && ((IPBan6)o).mask.equals(mask);
		}
		
		@Override
		public boolean hasNetMask() {
			return !mask.equals(mask128);
		}
		
	}
	
	public static final File bansFile = new File("bans.txt");
	
	public static final String banChatMessagePrefix = ChatColor.GOLD + "[BanList] ";
	
	public static final Map<String,String> userBans = new HashMap();
	public static final Set<IPBan> ipBans = new HashSet();
	public static final Set<String> wildcardBans = new HashSet();
	public static final Set<RegexBan> regexBans = new HashSet();
	private static List<String> currentBanList = null;
	
	public static final List<IPBan> blockedBans = new ArrayList();
	
	static {
		try {
			blockedBans.add(constructIpBan("127.0.0.0/8"));
		}catch(UnknownHostException e) {
			BungeeCord.getInstance().getLogger().severe("Error: could not whitelist '127.0.0.0/8'");
			e.printStackTrace();
		}
		try {
			blockedBans.add(constructIpBan("10.0.0.0/8"));
		}catch(UnknownHostException e) {
			BungeeCord.getInstance().getLogger().severe("Error: could not whitelist '10.0.0.0/8'");
			e.printStackTrace();
		}
		try {
			blockedBans.add(constructIpBan("172.24.0.0/14"));
		}catch(UnknownHostException e) {
			BungeeCord.getInstance().getLogger().severe("Error: could not whitelist '172.24.0.0/14'");
			e.printStackTrace();
		}
		try {
			blockedBans.add(constructIpBan("192.168.0.0/16"));
		}catch(UnknownHostException e) {
			BungeeCord.getInstance().getLogger().severe("Error: could not whitelist '192.168.0.0/16'");
			e.printStackTrace();
		}
		try {
			blockedBans.add(constructIpBan("::1/128"));
		}catch(UnknownHostException e) {
			BungeeCord.getInstance().getLogger().severe("Error: could not whitelist '::1/128'");
			e.printStackTrace();
		}
	}
	
	public static boolean isBlockedBan(InetAddress addr) {
		for(IPBan b : BanList.blockedBans) {
			if(b.checkBan(addr)) {
				return true;
			}
		}
		return false;
	}
	
	private static long lastListTest = 0l;
	private static long lastListLoad = 0l;
	private static boolean fileIsBroken = false;
	
	public static BanCheck checkIpBanned(InetAddress addr) {
		synchronized(banListMutex) {
			for(IPBan b : ipBans) {
				if(b.checkBan(addr)) {
					return new BanCheck(BanState.IP_BANNED, b.toString(), b.hasNetMask() ? "Banned by Netmask" : "Banned by IP");
				}
			}
		}
		return new BanCheck(BanState.NOT_BANNED, "none", "not banned");
	}
	
	public static BanCheck checkBanned(String player) {
		synchronized(banListMutex) {
			player = player.trim().toLowerCase();
			String r = userBans.get(player);
			if(r != null) {
				if(r.length() <= 0) {
					r = "The ban hammer has spoken";
				}
				return new BanCheck(BanState.USER_BANNED, player, r);
			}
			for(String ss : wildcardBans) {
				String s = ss;
				boolean startStar = s.startsWith("*");
				if(startStar) {
					s = s.substring(1);
				}
				boolean endStar = s.endsWith("*");
				if(endStar) {
					s = s.substring(0, s.length() - 1);
				}
				if(startStar && endStar) {
					if(player.contains(s)) {
						return new BanCheck(BanState.WILDCARD_BANNED, ss, "You've been banned via wildcard");
					}
				}else if(endStar) {
					if(player.startsWith(s)) {
						return new BanCheck(BanState.WILDCARD_BANNED, ss, "You've been banned via wildcard");
					}
				}else if(startStar) {
					if(player.endsWith(s)) {
						return new BanCheck(BanState.WILDCARD_BANNED, ss, "You've been banned via wildcard");
					}
				}else {
					if(player.equals(s)) {
						return new BanCheck(BanState.WILDCARD_BANNED, ss, "You've been banned via wildcard");
					}
				}
			}
			for(RegexBan p : regexBans) {
				if(p.compiled.matcher(player).matches()) {
					return new BanCheck(BanState.REGEX_BANNED, p.string, "You've been banned via regex");
				}
			}
		}
		return new BanCheck(BanState.NOT_BANNED, "none", "not banned");
	}
	
	private static void saveCurrentBanListLines() {
		try {
			PrintWriter pf = new PrintWriter(new FileWriter(bansFile));
			for(String s : currentBanList) {
				pf.println(s);
			}
			pf.close();
			lastListLoad = lastListTest = System.currentTimeMillis();
		}catch(Throwable t) {
			BungeeCord.getInstance().getLogger().severe("ERROR: the ban list could not be saved to file '" + bansFile.getName() + "', please fix this or you will lose all your bans next time this server restarts");
			t.printStackTrace();
		}
	}
	
	private static boolean addEntryToFile(BanState b, String s) {
		if(b == null || b == BanState.NOT_BANNED) {
			return false;
		}
		String wantedHeader = b == BanState.USER_BANNED ? "[Usernames]" : (b == BanState.WILDCARD_BANNED ? "[Wildcards]" : (b == BanState.REGEX_BANNED ? "[Regex]" : (b == BanState.IP_BANNED ? "[IPs]" : "shit")));
		int lastFullPart = -1;
		boolean isFilePart = false;
		boolean isPartStart = false;
		for(int i = 0, l = currentBanList.size(); i < l; ++i) {
			String ss = currentBanList.get(i).trim();
			if(ss.length() <= 0) {
				continue;
			}
			if(ss.startsWith("#")) {
				continue;
			}
			if(ss.equalsIgnoreCase(wantedHeader)) {
				isFilePart = true;
				isPartStart = true;
				lastFullPart = i;
			}else if(ss.indexOf('[') != -1) {
				if(isFilePart) {
					break;
				}
			}else {
				if(isFilePart) {
					lastFullPart = i;
					isPartStart = false;
				}
			}
		}
		if(lastFullPart != -1) {
			if(isPartStart) {
				lastFullPart += 1;
				currentBanList.add(lastFullPart, "");
			}
			lastFullPart += 1;
			currentBanList.add(lastFullPart, s);
			lastFullPart += 1;
			if(currentBanList.size() > lastFullPart && currentBanList.get(lastFullPart).trim().length() > 0) {
				currentBanList.add(lastFullPart, "");
			}
		}else {
			if(currentBanList.size() > 0 && currentBanList.get(currentBanList.size() - 1).trim().length() > 0) {
				currentBanList.add("");
			}
			currentBanList.add(wantedHeader);
			currentBanList.add("");
			currentBanList.add(s);
			currentBanList.add("");
		}
		saveCurrentBanListLines();
		return true;
	}
	
	private static boolean removeEntryFromFile(BanState b, String s, boolean ignoreCase) {
		if(b == null || b == BanState.NOT_BANNED) {
			return false;
		}
		String wantedHeader = b == BanState.USER_BANNED ? "[Usernames]" : (b == BanState.WILDCARD_BANNED ? "[Wildcards]" : (b == BanState.REGEX_BANNED ? "[Regex]" : (b == BanState.IP_BANNED ? "[IPs]" : "shit")));
		Iterator<String> lns = currentBanList.iterator();
		boolean isFilePart = false;
		boolean wasRemoved = false;
		while(lns.hasNext()) {
			String ss = lns.next().trim();
			if(ss.length() <= 0) {
				continue;
			}
			if(ss.startsWith("#")) {
				continue;
			}
			if(ss.equalsIgnoreCase(wantedHeader)) {
				isFilePart = true;
			}else if(ss.indexOf('[') != -1) {
				isFilePart = false;
			}else {
				if(b == BanState.USER_BANNED && ss.contains(":")) {
					ss = ss.substring(0, ss.indexOf(':')).trim();
				}
				if(isFilePart && (ignoreCase ? ss.equalsIgnoreCase(s) : ss.equals(s))) {
					lns.remove();
					wasRemoved = true;
				}
			}
		}
		if(wasRemoved) {
			saveCurrentBanListLines();
		}
		return wasRemoved;
	}

	public static boolean unban(String player) {
		synchronized(banListMutex) {
			String s = player.trim().toLowerCase();
			if(userBans.remove(s) != null) {
				removeEntryFromFile(BanState.USER_BANNED, player, true);
				return true;
			}else {
				return false;
			}
		}
	}

	public static boolean ban(String player, String reason) {
		synchronized(banListMutex) {
			player = player.trim().toLowerCase();
			if(userBans.put(player, reason) == null) {
				addEntryToFile(BanState.USER_BANNED, player + (reason == null || reason.length() <= 0 ? "" : ": " + reason));
				return true;
			}else {
				return false;
			}
		}
	}

	public static boolean banWildcard(String wc) throws PatternSyntaxException {
		synchronized(banListMutex) {
			wc = wc.trim().toLowerCase();
			boolean b = wc.contains("*");
			if(!b || (b && !wc.startsWith("*") && !wc.endsWith("*"))) {
				throw new PatternSyntaxException("Wildcard can only begin and/or end with *", wc, 0);
			}
			if(wildcardBans.add(wc)) {
				addEntryToFile(BanState.WILDCARD_BANNED, wc);
				return true;
			}else {
				return false;
			}
		}
	}

	public static boolean unbanWildcard(String wc) {
		synchronized(banListMutex) {
			wc = wc.trim().toLowerCase();
			if(wildcardBans.remove(wc)) {
				removeEntryFromFile(BanState.WILDCARD_BANNED, wc, true);
				return true;
			}else {
				return false;
			}
		}
	}

	public static boolean banRegex(String regex) throws PatternSyntaxException {
		synchronized(banListMutex) {
			regex = regex.trim();
			Pattern p = Pattern.compile(regex);
			if(regexBans.add(new RegexBan(regex, p))) {
				addEntryToFile(BanState.REGEX_BANNED, regex);
				return true;
			}else {
				return false;
			}
		}
	}

	public static boolean unbanRegex(String regex) {
		synchronized(banListMutex) {
			regex = regex.trim();
			Iterator<RegexBan> banz = regexBans.iterator();
			while(banz.hasNext()) {
				if(banz.next().string.equals(regex)) {
					banz.remove();
					removeEntryFromFile(BanState.REGEX_BANNED, regex, false);
					return true;
				}
			}
			return false;
		}
	}
	
	public static IPBan constructIpBan(String ip) throws UnknownHostException {
		synchronized(banListMutex) {
			ip = ip.trim();
			String s = ip;
			int subnet = -1;
			int i = s.indexOf('/');
			if(i != -1) {
				String s2 = s.substring(i + 1);
				s = s.substring(0, i);
				try {
					subnet = Integer.parseInt(s2);
				}catch(Throwable t) {
					throw new UnknownHostException("Invalid netmask: '" + s + "'");
				}
			}
			
			if(!IPAddressUtil.isIPv4LiteralAddress(s) && !IPAddressUtil.isIPv6LiteralAddress(s)) {
				throw new UnknownHostException("Invalid address: '" + s + "'");
			}
			
			InetAddress aa = InetAddress.getByName(s);
			if(aa instanceof Inet4Address) {
				if(subnet > 32 || subnet < -1) {
					throw new UnknownHostException("IPv4 netmask '" + subnet + "' is invalid");
				}
				if(subnet == -1) {
					subnet = 32;
				}
				return new IPBan4((Inet4Address)aa, ip, subnet);
			}else if(aa instanceof Inet6Address) {
				if(subnet > 128 || subnet < -1) {
					throw new UnknownHostException("IPv6 netmask '" + subnet + "' is invalid");
				}
				if(subnet == -1) {
					subnet = 128;
				}
				return new IPBan6((Inet6Address)aa, ip, subnet);
			}else {
				throw new UnknownHostException("Only ipv4 and ipv6 addresses allowed in Eaglercraft");
			}
		}
	}

	public static boolean banIP(String ip) throws UnknownHostException {
		synchronized(banListMutex) {
			ip = ip.trim();
			IPBan b = constructIpBan(ip);
			if(b != null) {
				if(ipBans.add(b)) {
					addEntryToFile(BanState.IP_BANNED, ip);
					return true;
				}
			}
			return false;
		}
	}

	public static boolean unbanIP(String ip) throws UnknownHostException {
		synchronized(banListMutex) {
			ip = ip.trim();
			IPBan b = constructIpBan(ip);
			if(b != null) {
				Iterator<IPBan> banz = ipBans.iterator();
				while(banz.hasNext()) {
					IPBan bb = banz.next();
					if(bb.equals(b)) {
						banz.remove();
						removeEntryFromFile(BanState.IP_BANNED, bb.toString(), true);
						return true;
					}
				}
			}
			return false;
		}
	}
	
	private static final int MAX_CHAT_LENGTH = 118;

	public static String listAllBans() {
		synchronized(banListMutex) {
			String ret = "";
			for(String s : userBans.keySet()) {
				if(ret.length() > 0) {
					ret += ", ";
				}
				ret += s;
			}
			return ret.length() > 0 ? ret : "(none)";
		}
	}

	public static String listAllWildcardBans() {
		synchronized(banListMutex) {
			String ret = "";
			for(String s : wildcardBans) {
				if(ret.length() > 0) {
					ret += ", ";
				}
				ret += s;
			}
			return ret.length() > 0 ? ret : "(none)";
		}
	}

	public static String listAllRegexBans() {
		synchronized(banListMutex) {
			String ret = "";
			for(RegexBan s : regexBans) {
				if(ret.length() > 0) {
					ret += " | ";
				}
				ret += s.string;
			}
			return ret.length() > 0 ? ret : "(none)";
		}
	}
	
	public static String listAllIPBans(boolean v6, boolean netmask) {
		synchronized(banListMutex) {
			String ret = "";
			for(IPBan b : ipBans) {
				if(v6) {
					if(b instanceof IPBan6) {
						IPBan6 b2 = (IPBan6)b;
						if(netmask == b2.hasNetMask()) {
							if(ret.length() > 0) {
								ret += ", ";
							}
							ret += b2.string;
						}
					}
				}else {
					if(b instanceof IPBan4) {
						IPBan4 b2 = (IPBan4)b;
						if(netmask == b2.hasNetMask()) {
							if(ret.length() > 0) {
								ret += ", ";
							}
							ret += b2.string;
						}
					}
				}
			}
			if(ret.length() <= 0) {
				ret = "(none)";
			}
			return ret;
		}
	}
	
	public static void maybeReloadBans(CommandSender cs) {
		synchronized(banListMutex) {
			long st = System.currentTimeMillis();
			if(cs == null && st - lastListTest < 1000l) {
				return;
			}
			lastListTest = st;
			boolean ex = bansFile.exists();
			if(!fileIsBroken && !ex) {
				try {
					PrintWriter p = new PrintWriter(new FileWriter(bansFile));
					p.println();
					p.println("#");
					p.println("# This file allows you to configure bans for eaglercraftbungee");
					p.println("# When it is saved, eaglercraft should reload it automatically");
					p.println("# (check the console though to be safe)");
					p.println("#");
					p.println("# For a [Usernames] ban, just add the player's name. Use a colon ':' to put in a ban reason");
					p.println("# For a [IPs] ban, just add the player's IP, or a subnet like 69.69.0.0/16 to ban all IPs beginning with 69.69.*");
					p.println("# For a [Wildcards] ban, type a string and prefix and/or suffix it with * to define the wildcard");
					p.println("# For a [Regex] ban, type a valid regular expression in the java.util.regex format");
					p.println("#");
					p.println("# All bans are case-insensitive, USERNAMES ARE CONVERTED TO LOWERCASE BEFORE BEING MATCHED VIA REGEX");
					p.println("# Java regex syntax: https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html");
					p.println("#");
					p.println();
					p.println("# set this to to true to use \"/ban\" to ban on bungee instead of \"/eag-ban\"");
					p.println("# (most likely needs a restart to take effect)");
					p.println("replace-bukkit=false");
					p.println();
					p.println();
					p.println("[Usernames]");
					p.println();
					p.println("# ban_test1: The ban hammer has spoken!");
					p.println("# ban_test2: custom ban message here");
					p.println("# ban_test3");
					p.println();
					p.println("# (remove the '#' before each line to enable)");
					p.println();
					p.println("[IPs]");
					p.println();
					p.println("# WARNING: if you're using nginx, banning any player's IP is gonna ban ALL PLAYERS on your server");
					p.println("# For this reason, the ban IP command doesn't ban 127.0.0.1 or any other 'private' range IPs");
					p.println();
					p.println("# 101.202.69.11");
					p.println("# 123.21.43.0/24");
					p.println("# 2601:1062:69:418:BEEF::10");
					p.println("# 2601:6090:420::/48");
					p.println();
					p.println();
					p.println("[Wildcards]");
					p.println();
					p.println("# *fuck*");
					p.println("# shi*");
					p.println();
					p.println();
					p.println("[Regex]");
					p.println();
					p.println("# you.+are.(a|the).+bitch");
					p.println();
					p.println();
					p.println("# end of file");
					p.println();
					p.close();
					BungeeCord.getInstance().getLogger().info("Wrote a new bans.txt to: " + bansFile.getAbsolutePath());
					lastListLoad = 0l;
				}catch(Throwable t) {
					fileIsBroken = true;
					if(cs != null) {
						cs.sendMessage(banChatMessagePrefix + ChatColor.RED + "Could not create blank 'bans.txt' list file");
						cs.sendMessage(banChatMessagePrefix + ChatColor.RED + "(Reason: " + t.toString() + ")");
					}
					BungeeCord.getInstance().getLogger().severe("Could not create blank 'bans.txt' list file");
					BungeeCord.getInstance().getLogger().severe("(Reason: " + t.toString() + ")");
					t.printStackTrace();
				}
				return;
			}
			if(fileIsBroken && ex) {
				fileIsBroken = false;
				lastListLoad = 0l;
			}
			if(fileIsBroken) {
				return;
			}
			long lastEdit = bansFile.lastModified();
			if(cs != null || lastEdit - lastListLoad > 400l) {
				try {
					BufferedReader r = new BufferedReader(new FileReader(bansFile));
					currentBanList = new LinkedList();
					String s;
					while((s = r.readLine()) != null) {
						currentBanList.add(s);
					}
					r.close();
					lastListLoad = lastEdit;
					BungeeCord.getInstance().getLogger().info("Server bans.txt changed, it will be reloaded automatically");
					if(cs == null) {
						for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers()) {
							if(pp.hasPermission("bungeecord.command.eag.reloadban")) {
								pp.sendMessage(BanList.banChatMessagePrefix + ChatColor.WHITE + "Your Eaglercraftbungee bans.txt just got modified, it will be reloaded asap");
								pp.sendMessage(BanList.banChatMessagePrefix + ChatColor.YELLOW + "Stop your server and check your config immediately if you don't know how this happened!!");
							}
						}
					}
					parseListFrom();
					BungeeCord.getInstance().getLogger().info("Reload complete");
				}catch(Throwable t) {
					if(cs != null) {
						cs.sendMessage(banChatMessagePrefix + ChatColor.RED + "Could not reload 'bans.txt' list file");
						cs.sendMessage(banChatMessagePrefix + ChatColor.RED + "(Reason: " + t.toString() + ")");
					}
					BungeeCord.getInstance().getLogger().severe("Could not reload 'bans.txt' list file");
					BungeeCord.getInstance().getLogger().severe("(Reason: " + t.toString() + ")");
				}
			}
		}
	}
	
	private static void parseListFrom() {
		userBans.clear();
		ipBans.clear();
		wildcardBans.clear();
		regexBans.clear();
		
		int filePart = 0;
		boolean replaceBukkit = false;
		
		for(String s : currentBanList) {
			s = s.trim();
			if(s.length() <= 0) {
				continue;
			}
			if(s.startsWith("#")) {
				continue;
			}
			if(s.equals("[Usernames]")) {
				filePart = 1;
			}else if(s.equals("[Wildcards]")) {
				filePart = 2;
			}else if(s.equals("[Regex]")) {
				filePart = 3;
			}else if(s.equals("[IPs]")) {
				filePart = 4;
			}else if(s.equals("replace-bukkit=true")) {
				replaceBukkit = true;
			}else if(s.equals("replace-bukkit=false")) {
				continue;
			}else {
				if(filePart == 1) {
					int i = s.indexOf(':');
					if(i == -1) {
						userBans.put(s.toLowerCase(), "");
					}else {
						userBans.put(s.substring(0, i).trim().toLowerCase(), s.substring(i + 1).trim());
					}
				}else if(filePart == 2) {
					boolean ws = s.startsWith("*");
					boolean we = s.endsWith("*");
					if(!ws && !we) {
						if(s.contains("*")) {
							BungeeCord.getInstance().getLogger().severe("wildcard '" + s + "' contains a '*' not at the start/end of the string");
						}else {
							BungeeCord.getInstance().getLogger().severe("wildcard '" + s + "' does not contain a '*' wildcard character");
						}
					}else {
						int total = (ws ? 1 : 0) + (we ? 1 : 0);
						int t2 = 0;
						for(char c : s.toCharArray()) {
							if(c == '*') ++t2;
						}
						if(total != t2) {
							BungeeCord.getInstance().getLogger().severe("wildcard '" + s + "' contains a '*' not at the start/end of the string");
						}
					}
					wildcardBans.add(s.toLowerCase());
				}else if(filePart == 3) {
					Pattern p = null;
					try {
						p = Pattern.compile(s);
					}catch(Throwable t) {
						BungeeCord.getInstance().getLogger().severe("the regex " + s.toLowerCase() + " is invalid");
						BungeeCord.getInstance().getLogger().severe("Reason: " + t.getClass().getSimpleName() + ": " + t.getLocalizedMessage());
					}
					if(p != null) {
						regexBans.add(new RegexBan(s, p));
					}
				}else if(filePart == 4) {
					String ss = s;
					int subnet = -1;
					int i = s.indexOf('/');
					if(i != -1) {
						String s2 = s.substring(i + 1);
						s = s.substring(0, i);
						try {
							subnet = Integer.parseInt(s2);
						}catch(Throwable t) {
							BungeeCord.getInstance().getLogger().severe("the subnet '"+ s2 +"' for IP ban address " + s + " was invalid");
							subnet = -2;
						}
					}
					if(subnet >= -1) {
						try {
							InetAddress aa = InetAddress.getByName(s);
							if(aa instanceof Inet4Address) {
								if(subnet == -1) {
									subnet = 32;
								}
								ipBans.add(new IPBan4((Inet4Address)aa, ss, subnet));
							}else if(aa instanceof Inet6Address) {
								if(subnet == -1) {
									subnet = 128;
								}
								ipBans.add(new IPBan6((Inet6Address)aa, ss, subnet));
							}else {
								throw new UnknownHostException("Only ipv4 and ipv6 addresses allowed in Eaglercraft");
							}
						}catch(Throwable t) {
							BungeeCord.getInstance().getLogger().severe("the IP ban address " + s + " could not be parsed");
							t.printStackTrace();
						}
					}
				}
			}
		}
		
		BungeeCord.getInstance().reconfigureBanCommands(replaceBukkit);
	}
	
}
