package net.md_5.bungee.eaglercraft;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.java_websocket.WebSocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Not the ideal solution but what are we supposed to do
 * 
 */
public class WebSocketProxy extends SimpleChannelInboundHandler<ByteBuf> {
	
	private WebSocket client;
	private InetSocketAddress tcpListener;
	private InetSocketAddress localAddress;
	private InetAddress realRemoteAddr;
	private String origin;
	private NioSocketChannel tcpChannel;
	
	private static final EventLoopGroup group = new NioEventLoopGroup(4);
	public static final HashMap<InetSocketAddress,InetAddress> localToRemote = new HashMap();
	public static final HashMap<InetSocketAddress,String> origins = new HashMap();
	
	public WebSocketProxy(WebSocket w, InetAddress remoteAddr, String originz, InetSocketAddress addr) {
		client = w;
		realRemoteAddr = remoteAddr;
		origin = originz;
		tcpListener = addr;
		tcpChannel = null;
	}
	
	public void killConnection() {
		synchronized(localToRemote) {
			localToRemote.remove(localAddress);
			origins.remove(localAddress);
		}
		if(tcpChannel != null && tcpChannel.isOpen()) {
			try {
				tcpChannel.disconnect().sync();
			} catch (InterruptedException e) {
				;
			}
		}
	}

	public boolean connect() {
		try {
			if(tcpChannel == null) {
				Bootstrap clientBootstrap = new Bootstrap();
				clientBootstrap.group(group);
				clientBootstrap.channel(NioSocketChannel.class);
				clientBootstrap.remoteAddress(tcpListener);
				clientBootstrap.option(ChannelOption.TCP_NODELAY, true);
				clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
				    protected void initChannel(SocketChannel socketChannel) throws Exception {
				        socketChannel.pipeline().addLast(WebSocketProxy.this);
				        socketChannel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
							@Override
							public void operationComplete(Future<? super Void> paramF) throws Exception {
								synchronized(localToRemote) {
									localToRemote.remove(localAddress);
									origins.remove(localAddress);
								}
							}
						});
				    }
				});
				tcpChannel = (NioSocketChannel) clientBootstrap.connect().sync().channel();
				synchronized(localToRemote) {
					localToRemote.put(localAddress = tcpChannel.localAddress(), realRemoteAddr);
					if(origin != null) {
						origins.put(localAddress, origin);
					}
				}
				return true;
			}
		}catch(Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, ByteBuf buffer) throws Exception {
    	ByteBuffer toSend = ByteBuffer.allocateDirect(buffer.capacity());
    	toSend.put(buffer.nioBuffer());
    	toSend.flip();
		if (client.isOpen()) {
			client.send(toSend);
		} else {
			killConnection();
		}
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

	public void sendPacket(ByteBuffer arg1) {
		if(tcpChannel != null && tcpChannel.isOpen()) {
			tcpChannel.write(Unpooled.wrappedBuffer(arg1));
		}
	}
	
	public void finalize() {
		synchronized(localToRemote) {
			localToRemote.remove(localAddress);
			origins.remove(localAddress);
		}
	}
	
}
