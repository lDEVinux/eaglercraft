// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnector;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.protocol.Vanilla;

public class PipelineUtils {
	public static final AttributeKey<ListenerInfo> LISTENER;
	public static final AttributeKey<UserConnection> USER;
	public static final AttributeKey<BungeeServerInfo> TARGET;
	public static final ChannelInitializer<Channel> SERVER_CHILD;
	public static final ChannelInitializer<Channel> CLIENT;
	public static final Base BASE;
	private static final DefinedPacketEncoder packetEncoder;
	private static final ByteArrayEncoder arrayEncoder;
	public static String TIMEOUT_HANDLER;
	public static String PACKET_DECODE_HANDLER;
	public static String PACKET_ENCODE_HANDLER;
	public static String ARRAY_ENCODE_HANDLER;
	public static String BOSS_HANDLER;
	public static String ENCRYPT_HANDLER;
	public static String DECRYPT_HANDLER;

	static {
		LISTENER = new AttributeKey("ListerInfo");
		USER = new AttributeKey("User");
		TARGET = new AttributeKey("Target");
		SERVER_CHILD = new ChannelInitializer<Channel>() {
			protected void initChannel(final Channel ch) throws Exception {
				PipelineUtils.BASE.initChannel(ch);
				((HandlerBoss) ch.pipeline().get((Class) HandlerBoss.class)).setHandler(new InitialHandler(ProxyServer.getInstance(), (ListenerInfo) ch.attr((AttributeKey) PipelineUtils.LISTENER).get()));
			}
		};
		CLIENT = new ChannelInitializer<Channel>() {
			protected void initChannel(final Channel ch) throws Exception {
				PipelineUtils.BASE.initChannel(ch);
				((HandlerBoss) ch.pipeline().get((Class) HandlerBoss.class))
						.setHandler(new ServerConnector(ProxyServer.getInstance(), (UserConnection) ch.attr((AttributeKey) PipelineUtils.USER).get(), (BungeeServerInfo) ch.attr((AttributeKey) PipelineUtils.TARGET).get()));
			}
		};
		BASE = new Base();
		packetEncoder = new DefinedPacketEncoder();
		arrayEncoder = new ByteArrayEncoder();
		PipelineUtils.TIMEOUT_HANDLER = "timeout";
		PipelineUtils.PACKET_DECODE_HANDLER = "packet-decoder";
		PipelineUtils.PACKET_ENCODE_HANDLER = "packet-encoder";
		PipelineUtils.ARRAY_ENCODE_HANDLER = "array-encoder";
		PipelineUtils.BOSS_HANDLER = "inbound-boss";
		PipelineUtils.ENCRYPT_HANDLER = "encrypt";
		PipelineUtils.DECRYPT_HANDLER = "decrypt";
	}

	public static final class Base extends ChannelInitializer<Channel> {
		public void initChannel(final Channel ch) throws Exception {
			try {
				ch.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
			} catch (ChannelException ex) {
			}
			ch.pipeline().addLast(PipelineUtils.TIMEOUT_HANDLER, (ChannelHandler) new ReadTimeoutHandler((long) BungeeCord.getInstance().config.getTimeout(), TimeUnit.MILLISECONDS));
			ch.pipeline().addLast(PipelineUtils.PACKET_DECODE_HANDLER, (ChannelHandler) new PacketDecoder(Vanilla.getInstance()));
			ch.pipeline().addLast(PipelineUtils.PACKET_ENCODE_HANDLER, (ChannelHandler) PipelineUtils.packetEncoder);
			ch.pipeline().addLast(PipelineUtils.ARRAY_ENCODE_HANDLER, (ChannelHandler) PipelineUtils.arrayEncoder);
			ch.pipeline().addLast(PipelineUtils.BOSS_HANDLER, (ChannelHandler) new HandlerBoss());
		}
	}
}
