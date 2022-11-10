// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.util;

import java.util.Map;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

public class CaseInsensitiveMap<V> extends TCustomHashMap<String, V> {
	public CaseInsensitiveMap() {
		super((HashingStrategy) CaseInsensitiveHashingStrategy.INSTANCE);
	}

	public CaseInsensitiveMap(final Map<? extends String, ? extends V> map) {
		super((HashingStrategy) CaseInsensitiveHashingStrategy.INSTANCE, (Map) map);
	}
}
