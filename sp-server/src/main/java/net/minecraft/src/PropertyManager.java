package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	/** The server properties object. */
	private final Properties serverProperties = new Properties();

	/** Reference to the logger. */
	private final ILogAgent logger;

	/** The server properties file. */
	private final File serverPropertiesFile;

	public PropertyManager(File par1File, ILogAgent par2ILogAgent) {
		this.serverPropertiesFile = par1File;
		this.logger = par2ILogAgent;

		if (par1File.exists()) {
			FileInputStream var3 = null;

			try {
				var3 = new FileInputStream(par1File);
				this.serverProperties.load(var3);
			} catch (Exception var13) {
				par2ILogAgent.logWarningException("Failed to load " + par1File, var13);
				this.generateNewProperties();
			} finally {
				if (var3 != null) {
					try {
						var3.close();
					} catch (IOException var12) {
						;
					}
				}
			}
		} else {
			par2ILogAgent.func_98236_b(par1File + " does not exist");
			this.generateNewProperties();
		}
	}

	/**
	 * Generates a new properties file.
	 */
	public void generateNewProperties() {
		this.logger.func_98233_a("Generating new properties file");
		this.saveProperties();
	}

	/**
	 * Writes the properties to the properties file.
	 */
	public void saveProperties() {
		FileOutputStream var1 = null;

		try {
			var1 = new FileOutputStream(this.serverPropertiesFile);
			this.serverProperties.store(var1, "Minecraft server properties");
		} catch (Exception var11) {
			this.logger.logWarningException("Failed to save " + this.serverPropertiesFile, var11);
			this.generateNewProperties();
		} finally {
			if (var1 != null) {
				try {
					var1.close();
				} catch (IOException var10) {
					;
				}
			}
		}
	}

	/**
	 * Returns this PropertyManager's file object used for property saving.
	 */
	public File getPropertiesFile() {
		return this.serverPropertiesFile;
	}

	/**
	 * Returns a string property. If the property doesn't exist the default is
	 * returned.
	 */
	public String getStringProperty(String par1Str, String par2Str) {
		if (!this.serverProperties.containsKey(par1Str)) {
			this.serverProperties.setProperty(par1Str, par2Str);
			this.saveProperties();
		}

		return this.serverProperties.getProperty(par1Str, par2Str);
	}

	/**
	 * Gets an integer property. If it does not exist, set it to the specified
	 * value.
	 */
	public int getIntProperty(String par1Str, int par2) {
		try {
			return Integer.parseInt(this.getStringProperty(par1Str, "" + par2));
		} catch (Exception var4) {
			this.serverProperties.setProperty(par1Str, "" + par2);
			return par2;
		}
	}

	/**
	 * Gets a boolean property. If it does not exist, set it to the specified value.
	 */
	public boolean getBooleanProperty(String par1Str, boolean par2) {
		try {
			return Boolean.parseBoolean(this.getStringProperty(par1Str, "" + par2));
		} catch (Exception var4) {
			this.serverProperties.setProperty(par1Str, "" + par2);
			return par2;
		}
	}

	/**
	 * Saves an Object with the given property name.
	 */
	public void setProperty(String par1Str, Object par2Obj) {
		this.serverProperties.setProperty(par1Str, "" + par2Obj);
	}
}
