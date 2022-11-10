// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import java.beans.ConstructorProperties;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.ReplayingDecoder;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.DefinedPacket;

public class PacketDecoder extends ReplayingDecoder<Void> {
	private Protocol protocol;

	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final MessageList<Object> out) throws Exception {
		while (true) {
			final int startIndex = in.readerIndex();
			final DefinedPacket packet = this.protocol.read(in.readUnsignedByte(), in);
			final int endIndex = in.readerIndex();
			final byte[] buf = new byte[endIndex - startIndex];
			in.readerIndex(startIndex);
			in.readBytes(buf, 0, buf.length);
			in.readerIndex(endIndex);
			this.checkpoint();
			if (packet != null) {
				out.add((Object) new PacketWrapper(packet, buf));
			} else {
				out.add((Object) buf);
			}
		}
	}

	@ConstructorProperties({ "protocol" })
	public PacketDecoder(final Protocol protocol) {
		this.protocol = protocol;
	}

	public Protocol getProtocol() {
		return this.protocol;
	}

	public void setProtocol(final Protocol protocol) {
		this.protocol = protocol;
	}
}
