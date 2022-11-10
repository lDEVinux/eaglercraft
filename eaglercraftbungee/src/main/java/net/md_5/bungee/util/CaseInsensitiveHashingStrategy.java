// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.util;

import gnu.trove.strategy.HashingStrategy;

class CaseInsensitiveHashingStrategy implements HashingStrategy {
	static final CaseInsensitiveHashingStrategy INSTANCE;

	public int computeHashCode(final Object object) {
		return ((String) object).toLowerCase().hashCode();
	}

	public boolean equals(final Object o1, final Object o2) {
		return o1.equals(o2) || (o1 instanceof String && o2 instanceof String && ((String) o1).toLowerCase().equals(((String) o2).toLowerCase()));
	}

	static {
		INSTANCE = new CaseInsensitiveHashingStrategy();
	}
}
