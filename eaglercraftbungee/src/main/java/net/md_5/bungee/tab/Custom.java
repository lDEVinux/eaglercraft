// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.tab;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;
import net.md_5.bungee.api.tab.TabListAdapter;
import net.md_5.bungee.protocol.packet.PacketC9PlayerListItem;

public class Custom extends TabListAdapter implements CustomTabList {
	private static final int ROWS = 20;
	private static final int COLUMNS = 3;
	private static final char[] FILLER;
	private static final int MAX_LEN = 16;
	private final Collection<String> sentStuff;
	private String[][] sent;
	private String[][] slots;
	private int rowLim;
	private int colLim;

	public Custom(final ProxiedPlayer player) {
		this.sentStuff = new HashSet<String>();
		this.sent = new String[20][3];
		this.slots = new String[20][3];
		this.init(player);
	}

	@Override
	public synchronized String setSlot(final int row, final int column, final String text) {
		return this.setSlot(row, column, text, true);
	}

	@Override
	public synchronized String setSlot(int row, int column, String text, final boolean update) {
		Preconditions.checkArgument(row > 0 && row <= 20, (Object) "row out of range");
		Preconditions.checkArgument(column > 0 && column <= 3, (Object) "column out of range");
		if (text != null) {
			Preconditions.checkArgument(text.length() <= 16, "text must be <= %s chars", new Object[] { 16 });
			Preconditions.checkArgument(!ChatColor.stripColor(text).isEmpty(), (Object) "Text cannot consist entirely of colour codes");
			text = this.attempt(text);
			this.sentStuff.add(text);
			if (this.rowLim < row || this.colLim < column) {
				this.rowLim = row;
				this.colLim = column;
			}
		} else {
			this.sentStuff.remove(text);
		}
		this.slots[--row][--column] = text;
		if (update) {
			this.update();
		}
		return text;
	}

	private String attempt(final String s) {
		for (final char c : Custom.FILLER) {
			final String attempt = s + Character.toString('§') + c;
			if (!this.sentStuff.contains(attempt)) {
				return attempt;
			}
		}
		if (s.length() <= 12) {
			return this.attempt(s + Character.toString('§') + Custom.FILLER[0]);
		}
		throw new IllegalArgumentException("List already contains all variants of string");
	}

	@Override
	public synchronized void update() {
		this.clear();
		for (int i = 0; i < this.rowLim; ++i) {
			for (int j = 0; j < this.colLim; ++j) {
				final String text = (this.slots[i][j] != null) ? this.slots[i][j] : new StringBuilder().append(base(i)).append(base(j)).toString();
				this.sent[i][j] = text;
				this.getPlayer().unsafe().sendPacket(new PacketC9PlayerListItem(text, true, (short) 0));
			}
		}
	}

	@Override
	public synchronized void clear() {
		for (int i = 0; i < this.rowLim; ++i) {
			for (int j = 0; j < this.colLim; ++j) {
				if (this.sent[i][j] != null) {
					final String text = this.sent[i][j];
					this.sent[i][j] = null;
					this.getPlayer().unsafe().sendPacket(new PacketC9PlayerListItem(text, false, (short) 9999));
				}
			}
		}
	}

	@Override
	public synchronized int getRows() {
		return 20;
	}

	@Override
	public synchronized int getColumns() {
		return 3;
	}

	@Override
	public synchronized int getSize() {
		return 60;
	}

	@Override
	public boolean onListUpdate(final String name, final boolean online, final int ping) {
		return false;
	}

	private static char[] base(final int n) {
		final String hex = Integer.toHexString(n + 1);
		final char[] alloc = new char[hex.length() * 2];
		for (int i = 0; i < alloc.length; ++i) {
			if (i % 2 == 0) {
				alloc[i] = '§';
			} else {
				alloc[i] = hex.charAt(i / 2);
			}
		}
		return alloc;
	}

	static {
		FILLER = new char[] { '0', '1', '2', '2', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	}
}
