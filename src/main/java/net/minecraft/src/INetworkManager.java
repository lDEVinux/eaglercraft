package net.minecraft.src;

public interface INetworkManager {
	/**
	 * Sets the NetHandler for this NetworkManager. Server-only.
	 */
	public void setNetHandler(NetHandler var1);

	/**
	 * Adds the packet to the correct send queue (chunk data packets go to a
	 * separate queue).
	 */
	public void addToSendQueue(Packet var1);

	/**
	 * Wakes reader and writer threads
	 */
	public void wakeThreads();

	/**
	 * Checks timeouts and processes all pending read packets.
	 */
	public void processReadPackets();

	/**
	 * Shuts down the server. (Only actually used on the server)
	 */
	public void serverShutdown();

	/**
	 * returns 0 for memoryConnections
	 */
	public int packetSize();

	/**
	 * Shuts down the network with the specified reason. Closes all streams and
	 * sockets, spawns NetworkMasterThread to stop reading and writing threads.
	 */
	public void networkShutdown(String var1, Object... var2);

	public void closeConnections();
	
	public String getServerURI();
}
