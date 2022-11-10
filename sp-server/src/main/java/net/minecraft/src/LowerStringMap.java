package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LowerStringMap implements Map {
	private final Map internalMap = new LinkedHashMap();

	public int size() {
		return this.internalMap.size();
	}

	public boolean isEmpty() {
		return this.internalMap.isEmpty();
	}

	public boolean containsKey(Object par1Obj) {
		return this.internalMap.containsKey(par1Obj.toString().toLowerCase());
	}

	public boolean containsValue(Object par1Obj) {
		return this.internalMap.containsKey(par1Obj);
	}

	public Object get(Object par1Obj) {
		return this.internalMap.get(par1Obj.toString().toLowerCase());
	}

	/**
	 * a map already defines a general put
	 */
	public Object putLower(String par1Str, Object par2Obj) {
		return this.internalMap.put(par1Str.toLowerCase(), par2Obj);
	}

	public Object remove(Object par1Obj) {
		return this.internalMap.remove(par1Obj.toString().toLowerCase());
	}

	public void putAll(Map par1Map) {
		Iterator var2 = par1Map.entrySet().iterator();

		while (var2.hasNext()) {
			Entry var3 = (Entry) var2.next();
			this.putLower((String) var3.getKey(), var3.getValue());
		}
	}

	public void clear() {
		this.internalMap.clear();
	}

	public Set keySet() {
		return this.internalMap.keySet();
	}

	public Collection values() {
		return this.internalMap.values();
	}

	public Set entrySet() {
		return this.internalMap.entrySet();
	}

	public Object put(Object par1Obj, Object par2Obj) {
		return this.putLower((String) par1Obj, par2Obj);
	}
}
