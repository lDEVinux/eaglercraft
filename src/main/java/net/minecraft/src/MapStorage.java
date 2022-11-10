package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage {

	/** Map of item data String id to loaded MapDataBases */
	private Map loadedDataMap = new HashMap();

	/** List of loaded MapDataBases. */
	private List loadedDataList = new ArrayList();

	/**
	 * Map of MapDataBase id String prefixes ('map' etc) to max known unique Short
	 * id (the 0 part etc) for that prefix
	 */
	private Map idCounts = new HashMap();

	public MapStorage() {
		this.loadIdCounts();
	}

	/**
	 * Loads an existing MapDataBase corresponding to the given String id from disk,
	 * instantiating the given Class, or returns null if none such file exists.
	 * args: Class to instantiate, String dataid
	 */
	public WorldSavedData loadData(Class par1Class, String par2Str) {
		return (WorldSavedData) this.loadedDataMap.get(par2Str);
	}

	/**
	 * Assigns the given String id to the given MapDataBase, removing any existing
	 * ones of the same id.
	 */
	public void setData(String par1Str, WorldSavedData par2WorldSavedData) {
		if (par2WorldSavedData == null) {
			throw new RuntimeException("Can\'t set null data");
		} else {
			if (this.loadedDataMap.containsKey(par1Str)) {
				this.loadedDataList.remove(this.loadedDataMap.remove(par1Str));
			}

			this.loadedDataMap.put(par1Str, par2WorldSavedData);
			this.loadedDataList.add(par2WorldSavedData);
		}
	}

	/**
	 * Saves all dirty loaded MapDataBases to disk.
	 */
	public void saveAllData() {
		for (int var1 = 0; var1 < this.loadedDataList.size(); ++var1) {
			WorldSavedData var2 = (WorldSavedData) this.loadedDataList.get(var1);

			if (var2.isDirty()) {
				this.saveData(var2);
				var2.setDirty(false);
			}
		}
	}

	/**
	 * Saves the given MapDataBase to disk.
	 */
	private void saveData(WorldSavedData par1WorldSavedData) {
		
	}

	/**
	 * Loads the idCounts Map from the 'idcounts' file.
	 */
	private void loadIdCounts() {
		try {
			this.idCounts.clear();
		} catch (Exception var9) {
			var9.printStackTrace();
		}
	}

	/**
	 * Returns an unique new data id for the given prefix and saves the idCounts map
	 * to the 'idcounts' file.
	 */
	public int getUniqueDataId(String par1Str) {
		Short var2 = (Short) this.idCounts.get(par1Str);

		if (var2 == null) {
			var2 = Short.valueOf((short) 0);
		} else {
			var2 = Short.valueOf((short) (var2.shortValue() + 1));
		}

		this.idCounts.put(par1Str, var2);

		return var2.shortValue();
	}
}