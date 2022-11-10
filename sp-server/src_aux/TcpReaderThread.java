package net.minecraft.src;

class TcpReaderThread extends Thread {
	final TcpConnection theTcpConnection;

	TcpReaderThread(TcpConnection par1TcpConnection, String par2Str) {
		super(par2Str);
		this.theTcpConnection = par1TcpConnection;
	}

	public void run() {
		TcpConnection.field_74471_a.getAndIncrement();

		try {
			while (TcpConnection.isRunning(this.theTcpConnection)
					&& !TcpConnection.isServerTerminating(this.theTcpConnection)) {
				while (true) {
					if (!TcpConnection.readNetworkPacket(this.theTcpConnection)) {
						try {
							sleep(2L);
						} catch (InterruptedException var5) {
							;
						}
					}
				}
			}
		} finally {
			TcpConnection.field_74471_a.getAndDecrement();
		}
	}
}
