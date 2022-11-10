package net.lax1dude.eaglercraft;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class WebsocketNetworkManager implements INetworkManager {
	
	private NetHandler netHandler;
	private String serverURI;
	
	public WebsocketNetworkManager(String uri, String eagler, NetHandler netHandler) throws IOException {
		this.serverURI = uri;
		this.netHandler = netHandler;
		if(!EaglerAdapter.startConnection(uri)) {
			throw new IOException("websocket to "+uri+" failed");
		}
		EaglerAdapter.setDebugVar("minecraftServer", uri);
	}
	
	public void setNetHandler(NetHandler netHandler) {
		this.netHandler = netHandler;
	}

	private ByteArrayOutputStream sendBuffer = new ByteArrayOutputStream();
	
	public void addToSendQueue(Packet var1) {
		try {
			sendBuffer.reset();
			DataOutputStream yee = new DataOutputStream(sendBuffer);
			Packet.writePacket(var1, yee);
			EaglerAdapter.writePacket(sendBuffer.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void wakeThreads() {
	}
	
	private static class ByteBufferDirectInputStream extends InputStream {
		private ByteBuffer buf;
		private ByteBufferDirectInputStream(ByteBuffer b) {
			this.buf = b;
		}
		
		@Override
		public int read() throws IOException {
			return buf.remaining() > 0 ? ((int)buf.get() & 0xFF) : -1;
		}
		
		@Override
		public int available() {
			return buf.remaining();
		}
	}
	
	private ByteBuffer oldChunkBuffer = null;
	private LinkedList<ByteBuffer> readChunks = new LinkedList();
	
	public void processReadPackets() {
		readChunks.clear();
		
		if(oldChunkBuffer != null) {
			readChunks.add(oldChunkBuffer);
		}
		
		byte[] packet;
		while((packet = EaglerAdapter.readPacket()) != null) {
			readChunks.add(ByteBuffer.wrap(packet));
		}
		if(!readChunks.isEmpty()) {
			int cap = 0;
			for(ByteBuffer b : readChunks) {
				cap += b.limit();
			}
			
			ByteBuffer stream = ByteBuffer.allocate(cap);
			for(ByteBuffer b : readChunks) {
				stream.put(b);
			}
			stream.flip();
			
			DataInputStream packetStream = new DataInputStream(new ByteBufferDirectInputStream(stream));
			while(stream.hasRemaining()) {
				stream.mark();
				try {
					Packet pkt = Packet.readPacket(packetStream, false);
					pkt.processPacket(this.netHandler);
				} catch (EOFException e) {
					stream.reset();
					break;
				}  catch (IOException e) {
					continue;
				} catch (Throwable e2) {
					e2.printStackTrace();
				}
			}
			
			if(stream.hasRemaining()) {
				oldChunkBuffer = stream.slice();
			}else {
				oldChunkBuffer = null;
			}
			
		}
	}
	
	public void serverShutdown() {
		if(EaglerAdapter.connectionOpen()) {
			EaglerAdapter.endConnection();
			EaglerAdapter.setDebugVar("minecraftServer", "null");
		}
	}
	
	public int packetSize() {
		return 0;
	}
	
	public void networkShutdown(String var1, Object... var2) {
		serverShutdown();
	}
	
	public void closeConnections() {
		if(EaglerAdapter.connectionOpen()) {
			EaglerAdapter.endConnection();
			EaglerAdapter.setDebugVar("minecraftServer", "null");
		}
	}
	
	public String getServerURI() {
		return this.serverURI;
	}

}
