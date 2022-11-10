package net.minecraft.src;

public interface INetworkManager {
	/**
	 * Sets the NetHandler for this NetworkManager. Server-only.
	 */
	void setNetHandler(NetHandler var1);

	/**
	 * Adds the packet to the correct send queue (chunk data packets go to a
	 * separate queue).
	 */
	void addToSendQueue(Packet var1);

	/**
	 * Wakes reader and writer threads
	 */
	void wakeThreads();

	/**
	 * Checks timeouts and processes all pending read packets.
	 */
	void processReadPackets();

	/**
	 * Shuts down the server. (Only actually used on the server)
	 */
	void serverShutdown();

	/**
	 * Returns the number of chunk data packets waiting to be sent.
	 */
	int getNumChunkDataPackets();

	/**
	 * Shuts down the network with the specified reason. Closes all streams and
	 * sockets, spawns NetworkMasterThread to stop reading and writing threads.
	 */
	void networkShutdown(String var1, Object... var2);
}
