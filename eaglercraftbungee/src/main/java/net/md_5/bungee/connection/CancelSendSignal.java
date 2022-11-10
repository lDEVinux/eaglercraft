// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.connection;

public class CancelSendSignal extends Error {
	@Override
	public Throwable initCause(final Throwable cause) {
		return this;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
