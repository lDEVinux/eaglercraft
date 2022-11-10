package net.minecraft.src;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

public class StringTranslate {
	/** Is the private singleton instance of StringTranslate. */
	private static StringTranslate instance = new StringTranslate(null);

	/**
	 * Contains all key/value pairs to be translated - is loaded from
	 * '/lang/en_US.lang' when the StringTranslate is created.
	 */
	private Properties translateTable = new Properties();
	private TreeMap languageList;

	private StringTranslate(List<String> en_us) {
		loadLanguageList(en_us);
	}
	
	public static void init(List<String> en_us) {
		instance.loadLanguageList(en_us);
	}

	/**
	 * Return the StringTranslate singleton instance
	 */
	public static StringTranslate getInstance() {
		return instance;
	}

	private void loadLanguageList(List<String> en_us) {
		this.languageList = new TreeMap();
		this.languageList.put("en_US", "English (US)");
		if(en_us != null) {
			loadLanguage(en_us);
		}
	}

	public TreeMap getLanguageList() {
		return this.languageList;
	}

	private void loadLanguage(List<String> en_us) {
		for (String var4 : en_us) {
			var4 = var4.trim();

			if (!var4.startsWith("#")) {
				String[] var5 = var4.split("=");

				if (var5 != null && var5.length == 2) {
					translateTable.setProperty(var5[0], var5[1]);
				}
			}
		}
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

	/**
	 * Returns true if the passed key is in the translation table.
	 */
	public synchronized boolean isKeyTranslated(String par1Str) {
		return this.translateTable.containsKey(par1Str);
	}

	/**
	 * Translate a key with a extra '.name' at end added, is used by blocks and
	 * items.
	 */
	public synchronized String translateNamedKey(String par1Str) {
		return this.translateTable.getProperty(par1Str + ".name", "");
	}
}
