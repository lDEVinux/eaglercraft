// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import javax.crypto.Cipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.MessageToMessageDecoder;

public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {
	private final CipherBase cipher;

	public CipherDecoder(final Cipher cipher) {
		this.cipher = new CipherBase(cipher);
	}

	protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final MessageList<Object> out) throws Exception {
		out.add((Object) this.cipher.cipher(ctx, msg));
	}
}
