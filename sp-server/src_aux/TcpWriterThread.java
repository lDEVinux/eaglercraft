package net.minecraft.src;

import java.io.IOException;

class TcpWriterThread extends Thread {
	final TcpConnection theTcpConnection;

	TcpWriterThread(TcpConnection par1TcpConnection, String par2Str) {
		super(par2Str);
		this.theTcpConnection = par1TcpConnection;
	}

	public void run() {
		TcpConnection.field_74469_b.getAndIncrement();

		try {
			while (TcpConnection.isRunning(this.theTcpConnection)) {
				boolean var1;

				for (var1 = false; TcpConnection.sendNetworkPacket(this.theTcpConnection); var1 = true) {
					;
				}

				try {
					if (var1 && TcpConnection.getOutputStream(this.theTcpConnection) != null) {
						TcpConnection.getOutputStream(this.theTcpConnection).flush();
					}
				} catch (IOException var8) {
					if (!TcpConnection.isTerminating(this.theTcpConnection)) {
						TcpConnection.sendError(this.theTcpConnection, var8);
					}

					var8.printStackTrace();
				}

				try {
					sleep(2L);
				} catch (InterruptedException var7) {
					;
				}
			}
		} finally {
			TcpConnection.field_74469_b.getAndDecrement();
		}
	}
}
