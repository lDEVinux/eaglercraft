// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.log;

import java.beans.ConstructorProperties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingOutputStream extends ByteArrayOutputStream {
	private static final String separator;
	private final Logger logger;
	private final Level level;

	@Override
	public void flush() throws IOException {
		final String contents = this.toString();
		super.reset();
		if (!contents.isEmpty() && !contents.equals(LoggingOutputStream.separator)) {
			this.logger.logp(this.level, "", "", contents);
		}
	}

	@ConstructorProperties({ "logger", "level" })
	public LoggingOutputStream(final Logger logger, final Level level) {
		this.logger = logger;
		this.level = level;
	}

	static {
		separator = System.getProperty("line.separator");
	}
}
