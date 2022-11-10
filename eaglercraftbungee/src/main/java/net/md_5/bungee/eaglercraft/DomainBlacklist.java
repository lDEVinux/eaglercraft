package net.md_5.bungee.eaglercraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.config.Configuration;

public class DomainBlacklist {

	public static final Collection<Pattern> regexBlacklist = new ArrayList();
	public static final Collection<Pattern> regexLocalBlacklist = new ArrayList();
	public static final Collection<Pattern> regexBlacklistReplit = new ArrayList();
	public static final Collection<String> simpleWhitelist = new ArrayList();
	public static final File localBlacklist = new File("origin_blacklist.txt");
	private static Collection<String> blacklistSubscriptions = null;
	private static boolean blockOfflineDownload = false;
	private static boolean blockAllReplits = false;
	private static boolean localWhitelistMode = false;
	private static boolean simpleWhitelistMode = false;
	private static final HashSet<String> brokenURLs = new HashSet();
	private static final HashSet<String> brokenRegex = new HashSet();

	public static final HashSet<String> regexBlacklistReplitInternalStrings = new HashSet();
	public static final Collection<Pattern> regexBlacklistReplitInternal = new ArrayList();
	
	static {
		regexBlacklistReplitInternalStrings.add(".*repl(it)?\\..{1,5}$");
		for(String s : regexBlacklistReplitInternalStrings) {
			regexBlacklistReplitInternal.add(Pattern.compile(s));
		}
	}

	private static int updateRate = 15 * 60 * 1000;
	private static long lastLocalUpdate = 0l;
	private static long lastUpdate = 0;
	
	public static boolean test(String origin) {
		synchronized(regexBlacklist) {
			if(blockOfflineDownload && origin.equalsIgnoreCase("null")) {
				return true;
			}
			if(simpleWhitelistMode) {
				for(String st : simpleWhitelist) {
					if(origin.equalsIgnoreCase(st)) {
						return false;
					}
				}
			}
			if(localWhitelistMode || simpleWhitelistMode) {
				if(!blockOfflineDownload && origin.equalsIgnoreCase("null")) {
					return false;
				}
				for(Pattern m : regexLocalBlacklist) {
					if(m.matcher(origin).matches()) {
						return false;
					}
				}
				return true;
			}else {
				if(blockAllReplits) {
					for(Pattern m : regexBlacklistReplitInternal) {
						if(m.matcher(origin).matches()) {
							return true;
						}
					}
					for(Pattern m : regexBlacklistReplit) {
						if(m.matcher(origin).matches()) {
							return true;
						}
					}
				}
				for(Pattern m : regexBlacklist) {
					if(m.matcher(origin).matches()) {
						return true;
					}
				}
				for(Pattern m : regexLocalBlacklist) {
					if(m.matcher(origin).matches()) {
						return true;
					}
				}
				return false;
			}
		}
	}
	
	public static void init(BungeeCord bg) {
		synchronized(regexBlacklist) {
			brokenURLs.clear();
			brokenRegex.clear();
			regexBlacklist.clear();
			regexLocalBlacklist.clear();
			regexBlacklistReplit.clear();
			simpleWhitelist.clear();
			ConfigurationAdapter cfg2 = bg.getConfigurationAdapter();
			Configuration cfg = bg.config;
			blacklistSubscriptions = cfg2.getBlacklistURLs();
			blockOfflineDownload = cfg.shouldBlacklistOfflineDownload();
			blockAllReplits = cfg.shouldBlacklistReplits();
			simpleWhitelistMode = cfg.isSimpleWhitelistEnabled();
			simpleWhitelist.addAll(cfg2.getBlacklistSimpleWhitelist());
			lastLocalUpdate = 0l;
			lastUpdate = System.currentTimeMillis() - updateRate - 1000l;
			update();
		}
	}
	
	public static void update() {
		long ct = System.currentTimeMillis();
		if((int)(ct - lastUpdate) > updateRate) {
			lastUpdate = ct;
			synchronized(regexBlacklist) {
				if(blacklistSubscriptions != null) {
					ArrayList<Pattern> newBlacklist = new ArrayList();
					ArrayList<Pattern> newReplitBlacklist = new ArrayList();
					HashSet<String> newBlacklistSet = new HashSet();
					newBlacklistSet.addAll(regexBlacklistReplitInternalStrings);
					for(String str : blacklistSubscriptions) {
						try {
							URL u;
							try {
								u = new URL(str);
							}catch(MalformedURLException e) {
								if(brokenURLs.add(str)) {
									BungeeCord.getInstance().getLogger().severe("The blacklist subscription URL '" + str + "' is invalid");
								}
								continue;
							}
							URLConnection cc = u.openConnection();
							if(cc instanceof HttpURLConnection) {
								HttpURLConnection ccc = (HttpURLConnection)cc;
								ccc.setRequestProperty("Accept", "text/plain,text/html,application/xhtml+xml,application/xml");
								ccc.setRequestProperty("User-Agent", "Mozilla/5.0 EaglercraftBungee/" + EaglercraftBungee.version);
							}
							cc.connect();
							BufferedReader is = new BufferedReader(new InputStreamReader(cc.getInputStream()));
							String firstLine = is.readLine();
							if(firstLine == null) {
								is.close();
								throw new IOException("Could not read line");
							}
							firstLine = firstLine.trim();
							if(!firstLine.startsWith("#") || !firstLine.substring(1).trim().toLowerCase().startsWith("eaglercraft domain blacklist")) {
								throw new IOException("File does not contain a list of domains");
							}
							String ss;
							while((ss = is.readLine()) != null) {
								if((ss = ss.trim()).length() > 0) {
									if(ss.startsWith("#")) {
										ss = ss.substring(1).trim();
										if(ss.startsWith("replit-wildcard:")) {
											ss = ss.substring(16).trim();
											if(newBlacklistSet.add(ss)) {
												try {
													newReplitBlacklist.add(Pattern.compile(ss));
												}catch(PatternSyntaxException shit) {
													if(brokenRegex.add(ss)) {
														BungeeCord.getInstance().getLogger().severe("the blacklist replit wildcard regex '" + ss + "' is invalid");
														continue;
													}
												}
												brokenRegex.remove(ss);
											}
										}
										continue;
									}
									if(newBlacklistSet.add(ss)) {
										try {
											newBlacklist.add(Pattern.compile(ss));
										}catch(PatternSyntaxException shit) {
											if(brokenRegex.add(ss)) {
												BungeeCord.getInstance().getLogger().severe("the blacklist regex '" + ss + "' is invalid");
												continue;
											}
										}
										brokenRegex.remove(ss);
									}
								}
							}
							is.close();
							brokenURLs.remove(str);
						}catch(Throwable t) {
							if(brokenURLs.add(str)) {
								BungeeCord.getInstance().getLogger().severe("the blacklist subscription URL '" + str + "' is invalid");
							}
							t.printStackTrace();
						}
					}
					if(!newBlacklist.isEmpty()) {
						regexBlacklist.clear();
						regexBlacklist.addAll(newBlacklist);
					}
					if(!newReplitBlacklist.isEmpty()) {
						regexBlacklistReplit.clear();
						regexBlacklistReplit.addAll(newReplitBlacklist);
					}
				}else {
					brokenURLs.clear();
					brokenRegex.clear();
					regexBlacklist.clear();
					lastLocalUpdate = 0l;
				}
			}
		}
		if(localBlacklist.exists()) {
			long lastLocalEdit = localBlacklist.lastModified();
			if(lastLocalEdit != lastLocalUpdate) {
				lastLocalUpdate = lastLocalEdit;
				synchronized(regexBlacklist) {
					try {
						BufferedReader is = new BufferedReader(new FileReader(localBlacklist));
						regexLocalBlacklist.clear();
						localWhitelistMode = false;
						boolean foundWhitelistStatement = false;
						String ss;
						while((ss = is.readLine()) != null) {
							try {
								if((ss = ss.trim()).length() > 0) {
									if(!ss.startsWith("#")) {
										regexLocalBlacklist.add(Pattern.compile(ss));
									}else {
										String st = ss.substring(1).trim();
										if(st.startsWith("whitelistMode:")) {
											foundWhitelistStatement = true;
											String str = st.substring(14).trim().toLowerCase();
											localWhitelistMode = str.equals("true") || str.equals("on") || str.equals("1");
										}
									}
								}
							}catch(PatternSyntaxException shit) {
								BungeeCord.getInstance().getLogger().severe("the local " + (localWhitelistMode ? "whitelist" : "blacklist") + " regex '" + ss + "' is invalid");
							}
						}
						is.close();
						if(!foundWhitelistStatement) {
							List<String> newLines = new ArrayList();
							newLines.add("#whitelistMode: false");
							newLines.add("");
							BufferedReader is2 = new BufferedReader(new FileReader(localBlacklist));
							while((ss = is2.readLine()) != null) {
								newLines.add(ss);
							}
							is2.close();
							PrintWriter os = new PrintWriter(new FileWriter(localBlacklist));
							for(String str : newLines) {
								os.println(str);
							}
							os.close();
							lastLocalUpdate = localBlacklist.lastModified();
						}
						BungeeCord.getInstance().getLogger().info("Reloaded '" + localBlacklist.getName() + "'.");
					}catch(IOException ex) {
						regexLocalBlacklist.clear();
						BungeeCord.getInstance().getLogger().severe("failed to read local " + (localWhitelistMode ? "whitelist" : "blacklist") + " file '" + localBlacklist.getName() + "'");
						ex.printStackTrace();
					}
				}
			}
		}else {
			synchronized(regexBlacklist) {
				if(!regexLocalBlacklist.isEmpty()) {
					BungeeCord.getInstance().getLogger().warning("the blacklist file '" + localBlacklist.getName() + "' has been deleted");
				}
				regexLocalBlacklist.clear();
			}
		}
	}

	public static void addLocal(String o) {
		String p = "^" + Pattern.quote(o.trim()) + "$";
		ArrayList<String> lines = new ArrayList();
		if(localBlacklist.exists()) {
			try {
				BufferedReader is = new BufferedReader(new FileReader(localBlacklist));
				String ss;
				while((ss = is.readLine()) != null) {
					if((ss = ss.trim()).length() > 0) {
						lines.add(ss);
					}
				}
				is.close();
			}catch(IOException ex) {
				// ?
			}
		}
		if(lines.isEmpty()) {
			lines.add("#whitelist false");
			lines.add("");
		}
		if(!lines.contains(p)) {
			lines.add(p);
			try {
				PrintWriter os = new PrintWriter(new FileWriter(localBlacklist));
				for(String s : lines) {
					os.println(s);
				}
				os.close();
				lastLocalUpdate = 0l;
				update();
			}catch(IOException ex) {
				// ?
			}
		}
	}

	public static boolean removeLocal(String o) {
		String p = "^" + Pattern.quote(o.trim()) + "$";
		ArrayList<String> lines = new ArrayList();
		if(localBlacklist.exists()) {
			try {
				BufferedReader is = new BufferedReader(new FileReader(localBlacklist));
				String ss;
				while((ss = is.readLine()) != null) {
					if((ss = ss.trim()).length() > 0) {
						lines.add(ss);
					}
				}
				is.close();
			}catch(IOException ex) {
				// ?
			}
		}
		if(lines.contains(p)) {
			lines.remove(p);
			try {
				PrintWriter os = new PrintWriter(new FileWriter(localBlacklist));
				for(String s : lines) {
					os.println(s);
				}
				os.close();
				lastLocalUpdate = 0l;
				update();
				return true;
			}catch(IOException ex) {
				BungeeCord.getInstance().getLogger().severe("Failed to save '" + localBlacklist.getName() + "'");
				ex.printStackTrace();
			}
		}
		return false;
	}

}
