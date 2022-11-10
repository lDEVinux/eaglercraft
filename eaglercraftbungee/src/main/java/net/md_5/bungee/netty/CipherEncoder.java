// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import javax.crypto.Cipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {
	private final CipherBase cipher;

	public CipherEncoder(final Cipher cipher) {
		this.cipher = new CipherBase(cipher);
	}

	protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
		this.cipher.cipher(in, out);
	}
}
