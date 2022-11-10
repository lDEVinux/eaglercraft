// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConciseFormatter extends Formatter {
	private final DateFormat date;

	public ConciseFormatter() {
		this.date = new SimpleDateFormat("HH:mm:ss");
	}

	@Override
	public String format(final LogRecord record) {
		final StringBuilder formatted = new StringBuilder();
		formatted.append(this.date.format(record.getMillis()));
		formatted.append(" [");
		formatted.append(record.getLevel().getLocalizedName());
		formatted.append("] ");
		formatted.append(this.formatMessage(record));
		formatted.append('\n');
		if (record.getThrown() != null) {
			final StringWriter writer = new StringWriter();
			record.getThrown().printStackTrace(new PrintWriter(writer));
			formatted.append(writer);
		}
		return formatted.toString();
	}
}
