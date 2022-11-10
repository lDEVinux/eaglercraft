package net.lax1dude.eaglercraft.sp.relay;

import java.net.InetSocketAddress;

public class Util {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	public static String sock2String(InetSocketAddress sock) {
		return sock.getAddress().getHostAddress() + ":" + sock.getPort();
	}
	
}
