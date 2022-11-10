// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee;

import java.net.InetSocketAddress;
import java.util.Collection;

public class Util {
	private static final int DEFAULT_PORT = 25565;

	public static InetSocketAddress getAddr(final String hostline) {
		final String[] split = hostline.split(":");
		int port = 25565;
		if (split.length > 1) {
			port = Integer.parseInt(split[1]);
		}
		return new InetSocketAddress(split[0], port);
	}

	public static String hex(final int i) {
		return String.format("0x%02X", i);
	}

	public static String exception(final Throwable t) {
		final StackTraceElement[] trace = t.getStackTrace();
		return t.getClass().getSimpleName() + " : " + t.getMessage() + ((trace.length > 0) ? (" @ " + t.getStackTrace()[0].getClassName() + ":" + t.getStackTrace()[0].getLineNumber()) : "");
	}

	public static String csv(final Collection<?> objects) {
		return format(objects, ", ");
	}

	public static String format(final Collection<?> objects, final String separators) {
		final StringBuilder ret = new StringBuilder();
		for (final Object o : objects) {
			ret.append(o);
			ret.append(separators);
		}
		return (ret.length() == 0) ? "" : ret.substring(0, ret.length() - separators.length());
	}
}
