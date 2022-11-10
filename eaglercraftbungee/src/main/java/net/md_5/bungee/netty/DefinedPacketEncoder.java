// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.md_5.bungee.protocol.packet.DefinedPacket;

@ChannelHandler.Sharable
public class DefinedPacketEncoder extends MessageToByteEncoder<DefinedPacket> {
	protected void encode(final ChannelHandlerContext ctx, final DefinedPacket msg, final ByteBuf out) throws Exception {
		out.writeByte(msg.getId());
		msg.write(out);
	}
}
