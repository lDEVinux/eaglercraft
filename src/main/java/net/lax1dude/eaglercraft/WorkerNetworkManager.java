package net.lax1dude.eaglercraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class WorkerNetworkManager implements INetworkManager {
	
	private NetHandler theNetHandler;
	private String ipcChannel;
	private boolean hasClosed;
	
	public WorkerNetworkManager(String ipcChannel, NetHandler netHandler) {
		this.ipcChannel = ipcChannel;
		this.theNetHandler = netHandler;
		this.hasClosed = false;
	}

	@Override
	public void setNetHandler(NetHandler var1) {
		theNetHandler = var1;
	}
	
	private ByteArrayOutputStream sendBuffer = new ByteArrayOutputStream();

	@Override
	public void addToSendQueue(Packet var1) {
		try {
			sendBuffer.reset();
			Packet.writePacket(var1, new DataOutputStream(sendBuffer));
			EaglerAdapter.sendToIntegratedServer("NET|" + ipcChannel, sendBuffer.toByteArray());
		}catch(IOException e) {
			System.err.println("Failed to serialize minecraft packet '" + var1.getClass().getSimpleName() + "' for IPC channel 'NET|" + ipcChannel + "'");
			e.printStackTrace();
		}
	}

	@Override
	public void wakeThreads() {
		// no
	}

	@Override
	public void processReadPackets() {
		PKT ipcPacket;
		while((ipcPacket = EaglerAdapter.recieveFromIntegratedServer("NET|" + ipcChannel)) != null) {
			byte[] bytes = ipcPacket.data;
			try {
				ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
				int pktId = bai.read();
				
				if(pktId == -1) {
					System.err.println("Recieved invalid '-1' packet");
					continue;
				}
				
				Packet pkt = Packet.getNewPacket(pktId);
				
				if(pkt == null) {
					System.err.println("Recieved invalid '" + pktId + "' packet");
					continue;
				}
				
				pkt.readPacketData(new DataInputStream(bai));
				
				//System.out.println("[Client][" + ipcChannel + "]: packet 0x" + Integer.toHexString(pkt.getPacketId()) + " class '" + pkt.getClass().getSimpleName() + "' recieved");
				
				try {
					pkt.processPacket(theNetHandler);
				}catch(Throwable t) {
					System.err.println("Could not process minecraft packet 0x" + Integer.toHexString(pkt.getPacketId()) + " class '" + pkt.getClass().getSimpleName() + "' on channel 'NET|" + ipcChannel + "'");
					t.printStackTrace();
				}
				
			}catch(IOException ex) {
				System.err.println("Could not deserialize a " + bytes.length + " byte long minecraft packet of type '" + (bytes.length <= 0 ? -1 : (int)(bytes[0] & 0xFF)) + "' on channel 'NET|" + ipcChannel + "'");
			}
		}
		
	}

	@Override
	public void serverShutdown() {
		if(!hasClosed) {
			hasClosed = true;
			IntegratedServer.closeChannel(ipcChannel);
		}
	}

	@Override
	public void networkShutdown(String var1, Object... var2) {
		if(!hasClosed) {
			hasClosed = true;
			IntegratedServer.closeChannel(ipcChannel);
		}
	}

	@Override
	public int packetSize() {
		return 0;
	}

	@Override
	public void closeConnections() {
		if(!hasClosed) {
			hasClosed = true;
			IntegratedServer.closeChannel(ipcChannel);
		}
	}

	@Override
	public String getServerURI() {
		return "[integrated]";
	}

}
