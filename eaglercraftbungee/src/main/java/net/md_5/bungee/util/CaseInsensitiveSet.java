// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.util;

import java.util.Collection;

import gnu.trove.set.hash.TCustomHashSet;
import gnu.trove.strategy.HashingStrategy;

public class CaseInsensitiveSet extends TCustomHashSet<String> {
	public CaseInsensitiveSet() {
		super((HashingStrategy) CaseInsensitiveHashingStrategy.INSTANCE);
	}

	public CaseInsensitiveSet(final Collection<? extends String> collection) {
		super((HashingStrategy) CaseInsensitiveHashingStrategy.INSTANCE, (Collection) collection);
	}
}
