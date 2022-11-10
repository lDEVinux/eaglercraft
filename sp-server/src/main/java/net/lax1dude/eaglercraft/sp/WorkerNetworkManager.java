package net.lax1dude.eaglercraft.sp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UnknownFormatConversionException;

import net.lax1dude.eaglercraft.sp.ipc.IPCPacket0CPlayerChannel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet204ClientInfo;

public class WorkerNetworkManager implements INetworkManager {
	
	private NetHandler theNetHandler;
	private MinecraftServer minecraftServer;
	private String ipcChannel;
	private boolean isAlive;
	private WorkerListenThread listenThread;
	
	private LinkedList<byte[]> frags = new LinkedList();
	
	public WorkerNetworkManager(String ipcChannel, MinecraftServer srv, WorkerListenThread th) {
		this.ipcChannel = ipcChannel;
		this.theNetHandler = new NetLoginHandler(srv, this);
		th.addPlayer(theNetHandler);
		this.minecraftServer = srv;
		this.isAlive = true;
		this.listenThread = th;
	}

	@Override
	public void setNetHandler(NetHandler var1) {
		theNetHandler = var1;
		listenThread.addPlayer(theNetHandler);
	}

	@Override
	public void addToSendQueue(Packet var1) {
		if(!isAlive) {
			return;
		}
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream(var1.getPacketSize() + 1);
			Packet.writePacket(var1, new DataOutputStream(bao));
			IntegratedServer.sendPlayerPacket(ipcChannel, bao.toByteArray());
		}catch(IOException e) {
			System.err.println("Failed to serialize minecraft packet '" + var1.getPacketId() + "' for IPC channel 'NET|" + ipcChannel + "'");
			e.printStackTrace();
			return;
		}
	}
	
	public void addToRecieveQueue(byte[] fragment) {
		//System.out.println("[Server][READ][QUEUE][" + ipcChannel + "]: " + fragment.length);
		if(!isAlive) {
			return;
		}
		frags.add(fragment);
	}

	@Override
	public void wakeThreads() {
		// no
	}

	@Override
	public void processReadPackets() {
		while(frags.size() > 0) {
			byte[] pktBytes = frags.remove(0);
			try {
				ByteArrayInputStream bai = new ByteArrayInputStream(pktBytes);
				int pktId = bai.read();
				
				if(pktId == -1) {
					System.err.println("Recieved invalid '-1' packet");
					continue;
				}
				
				Packet pkt = Packet.getNewPacket(minecraftServer.getLogAgent(), pktId);
				
				if(pkt == null) {
					System.err.println("Recieved invalid '" + pktId + "' packet");
					continue;
				}
				
				pkt.readPacketData(new DataInputStream(bai));
				
				//System.out.println("[Server][" + ipcChannel + "]: packet '" + pkt.getClass().getSimpleName() + "' recieved");
				
				try {
					pkt.processPacket(theNetHandler);
				}catch(Throwable t) {
					System.err.println("Could not process minecraft packet 0x" + Integer.toHexString(pkt.getPacketId()) + " class '" + pkt.getClass().getSimpleName() + "' on channel 'NET|" + ipcChannel + "'");
					t.printStackTrace();
				}
				
			}catch(IOException ex) {
				System.err.println("Could not deserialize a " + pktBytes.length + " byte long minecraft packet of type '" + (pktBytes.length <= 0 ? -1 : (int)(pktBytes[0] & 0xFF)) + "' on channel 'NET|" + ipcChannel + "'");
			}
		}
		
	}

	@Override
	public void serverShutdown() {
		if(isAlive) {
			listenThread.closeChannel(ipcChannel);
			IntegratedServer.sendIPCPacket(new IPCPacket0CPlayerChannel(ipcChannel, false));
		}
		if(theNetHandler != null && (theNetHandler instanceof NetServerHandler)) {
			((NetServerHandler)theNetHandler).kickPlayer(null);
		}
		isAlive = false;
	}

	@Override
	public int getNumChunkDataPackets() { // why is this a thing
		return 0;
	}

	@Override
	public void networkShutdown(String var1, Object... var2) {
		if(isAlive) {
			listenThread.closeChannel(ipcChannel);
			IntegratedServer.sendIPCPacket(new IPCPacket0CPlayerChannel(ipcChannel, false));
		}
		if(theNetHandler != null && (theNetHandler instanceof NetServerHandler)) {
			((NetServerHandler)theNetHandler).kickPlayer(null);
		}
		isAlive = false;
	}
	
	public boolean equals(Object o) {
		return (o instanceof WorkerNetworkManager) && ((WorkerNetworkManager)o).ipcChannel.equals(ipcChannel);
	}
	
	public int hashCode() {
		return ipcChannel.hashCode();
	}

}
