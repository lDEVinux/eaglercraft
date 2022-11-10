package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatException;
import java.util.Properties;
import java.util.TreeMap;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class StringTranslate {
	/** Is the private singleton instance of StringTranslate. */
	private static StringTranslate instance = new StringTranslate("en_US");

	/**
	 * Contains all key/value pairs to be translated - is loaded from
	 * '/lang/en_US.lang' when the StringTranslate is created.
	 */
	private Properties translateTable = new Properties();
	private TreeMap languageList;
	private TreeMap field_94521_d = new TreeMap();
	private String currentLanguage;
	private boolean isUnicode;

	public StringTranslate(String par1Str) {
		this.loadLanguageList();
	}

	/**
	 * Return the StringTranslate singleton instance
	 */
	public static StringTranslate getInstance() {
		return instance;
	}

	private void loadLanguageList() {
		this.languageList = new TreeMap();
		this.languageList.put("en_US", "English (US)");
	}

	public TreeMap getLanguageList() {
		return this.languageList;
	}

	private void loadLanguage(Properties par1Properties, String par2Str) throws IOException {
		String[] var3 = EaglerAdapter.fileContents("/lang/" + par2Str + ".lang").replace('\r', '\n')
				.replaceAll("\n+", "\n").split("\n");

		for (String var4 : var3) {
			var4 = var4.trim();

			if (!var4.isEmpty() && !var4.startsWith("#")) {
				int var5 = var4.indexOf('=');

				if (var5 != -1) {
					par1Properties.setProperty(var4.substring(0, var5), var4.substring(var5 + 1));
				}
			}
		}
	}

	public synchronized void setLanguage(String par1Str, boolean par2) {
		if (par2 || !par1Str.equals(this.currentLanguage)) {
			Properties var3 = new Properties();

			try {
				this.loadLanguage(var3, "en_US");
			} catch (IOException var9) {
				;
			}

			this.isUnicode = false;
			/*
			if (!"en_US".equals(par1Str)) {
				try {
					this.loadLanguage(var3, par1Str);
					Enumeration var4 = var3.propertyNames();

					while (var4.hasMoreElements() && !this.isUnicode) {
						Object var5 = var4.nextElement();
						Object var6 = var3.get(var5);

						if (var6 != null) {
							String var7 = var6.toString();

							for (int var8 = 0; var8 < var7.length(); ++var8) {
								if (var7.charAt(var8) >= 256) {
									this.isUnicode = true;
									break;
								}
							}
						}
					}
				} catch (IOException var10) {
					var10.printStackTrace();
					return;
				}
			}
			*/

			this.currentLanguage = par1Str;
			this.translateTable = var3;
		}
	}

	public String getCurrentLanguage() {
		return this.currentLanguage;
	}

	public boolean isUnicode() {
		return this.isUnicode;
	}

	/**
	 * Translate a key to current language.
	 */
	public synchronized String translateKey(String par1Str) {
		return this.translateTable.getProperty(par1Str, par1Str);
	}

	/**
	 * Translate a key to current language applying String.format()
	 */
	public synchronized String translateKeyFormat(String par1Str, Object... par2ArrayOfObj) {
		String var3 = this.translateTable.getProperty(par1Str, par1Str);

		try {
			return String.format(var3, par2ArrayOfObj);
		} catch (IllegalFormatException var5) {
			return "Format error: " + var3;
		}
	}

	public synchronized boolean containsTranslateKey(String par1Str) {
		return this.translateTable.containsKey(par1Str);
	}

	/**
	 * Translate a key with a extra '.name' at end added, is used by blocks and
	 * items.
	 */
	public synchronized String translateNamedKey(String par1Str) {
		return this.translateTable.getProperty(par1Str + ".name", "");
	}

	public static boolean isBidirectional(String par0Str) {
		return "ar_SA".equals(par0Str) || "he_IL".equals(par0Str);
	}
	
}
