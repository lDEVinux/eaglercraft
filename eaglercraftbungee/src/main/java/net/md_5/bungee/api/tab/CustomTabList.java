// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.tab;

public interface CustomTabList extends TabListHandler {
	void clear();

	int getColumns();

	int getRows();

	int getSize();

	String setSlot(final int p0, final int p1, final String p2);

	String setSlot(final int p0, final int p1, final String p2, final boolean p3);

	void update();
}
