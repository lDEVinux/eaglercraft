package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.lax1dude.eaglercraft.sp.WorkerListenThread;

public class ServerListenThread extends Thread {
	private final List pendingConnections = Collections.synchronizedList(new ArrayList());

	/**
	 * This map stores a list of InetAddresses and the last time which they
	 * connected at
	 */
	private final HashMap recentConnections = new HashMap();
	private int connectionCounter = 0;
	private final ServerSocket myServerSocket;
	private WorkerListenThread myNetworkListenThread;
	private final InetAddress myServerAddress;
	private final int myPort;

	public ServerListenThread(WorkerListenThread par1NetworkListenThread, InetAddress par2InetAddress, int par3)
			throws IOException {
		super("Listen thread");
		this.myNetworkListenThread = par1NetworkListenThread;
		this.myPort = par3;
		this.myServerSocket = new ServerSocket(par3, 0, par2InetAddress);
		this.myServerAddress = par2InetAddress == null ? this.myServerSocket.getInetAddress() : par2InetAddress;
		this.myServerSocket.setPerformancePreferences(0, 2, 1);
	}

	public void processPendingConnections() {
		List var1 = this.pendingConnections;

		synchronized (this.pendingConnections) {
			for (int var2 = 0; var2 < this.pendingConnections.size(); ++var2) {
				NetLoginHandler var3 = (NetLoginHandler) this.pendingConnections.get(var2);

				try {
					var3.tryLogin();
				} catch (Exception var6) {
					var3.kickUser("Internal server error");
					this.myNetworkListenThread.getServer().getLogAgent().logWarningException(
							"Failed to handle packet for " + var3.getUsernameAndAddress() + ": " + var6, var6);
				}

				if (var3.finishedProcessing) {
					this.pendingConnections.remove(var2--);
				}

				var3.myTCPConnection.wakeThreads();
			}
		}
	}

	public void run() {
		while (this.myNetworkListenThread.isListening) {
			try {
				Socket var1 = this.myServerSocket.accept();
				NetLoginHandler var2 = new NetLoginHandler(this.myNetworkListenThread.getServer(), var1,
						"Connection #" + this.connectionCounter++);
				this.addPendingConnection(var2);
			} catch (IOException var3) {
				var3.printStackTrace();
			}
		}

		this.myNetworkListenThread.getServer().getLogAgent().func_98233_a("Closing listening thread");
	}

	private void addPendingConnection(NetLoginHandler par1NetLoginHandler) {
		if (par1NetLoginHandler == null) {
			throw new IllegalArgumentException("Got null pendingconnection!");
		} else {
			List var2 = this.pendingConnections;

			synchronized (this.pendingConnections) {
				this.pendingConnections.add(par1NetLoginHandler);
			}
		}
	}

	public void func_71769_a(InetAddress par1InetAddress) {
		if (par1InetAddress != null) {
			HashMap var2 = this.recentConnections;

			synchronized (this.recentConnections) {
				this.recentConnections.remove(par1InetAddress);
			}
		}
	}

	public void func_71768_b() {
		try {
			this.myServerSocket.close();
		} catch (Throwable var2) {
			;
		}
	}
}
